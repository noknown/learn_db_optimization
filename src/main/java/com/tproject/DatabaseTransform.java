package com.tproject;

import java.util.List;

/**
 * ユーザが保有するデータベース群をDatabaseStructureオブジェクトに変換する
 * @author no_known
 *
 */
public class DatabaseTransform {

	/**
	 * 指定されたデータベースをDatabaseStructure型に変換する。
	 * @param dao データベースに接続するためのDao
	 * @param username データベースを所有しているユーザ名
	 * @param databasename データベース名
	 * @return 変換されたDatabaseStructure
	 */
	public static DatabaseStructure toDatabaseStructure(IDao dao,
			String username, String databasename) {
		// TODO Auto-generated method stub
		ResultStruct rs;
		
		rs = dao.read("select table_name, table_id from system.users join " +
				"system.databases using(user_name) join system.tables " +
				"using(database_id) where user_name = '"+
				username + "' and database_name = '"+ databasename +"' order by table_id");
		
		List<ResultData> lrdata = rs.getResult();
		DatabaseStructure databasestruc = new DatabaseStructure(databasename);

		for(int i = 0; i < lrdata.size(); i++) {
			ResultStruct rss = dao.read("select column_name, data_type, data_length, data_precision, data_scale from user_tab_columns where table_name = '"+ lrdata.get(i).getDataList().get(0).toUpperCase() +"'");
			
			String table_name = lrdata.get(i).getDataList().get(0).toUpperCase();
			List<ResultData> lrdatar = rss.getResult();
			TableStructure tablestruc = new TableStructure(lrdata.get(i).getDataList().get(0));
			tablestruc.setTableId(lrdata.get(i).getDataList().get(1));
			
			for(int j = 0; j < lrdatar.size(); j++) {
				List<String> datalist = lrdatar.get(j).getDataList();
				ColumnConstraint ccc = toColumnConstraint(dao, table_name, datalist.get(0));
				
				TableColumnM column;
				if(!datalist.get(1).equals("NUMBER"))
					column = new TableColumnM(datalist.get(0).toLowerCase(), datalist.get(1), Integer.parseInt(datalist.get(2)), 0, ccc, "");
				else
					column = new TableColumnM(datalist.get(0).toLowerCase(), datalist.get(1),
							Integer.parseInt(datalist.get(3)), Integer.parseInt(datalist.get(4)), ccc, "");
				column.setTableName(tablestruc.getTableName());
				
				tablestruc.addSchema(column);

			}

			databasestruc.addTable(tablestruc);

		}

		
		return databasestruc;
	}

	public static ColumnConstraint toColumnConstraint(IDao dao,
			String tablename, String columnname) {
		// TODO Auto-generated method stub
		
		ColumnConstraint cc = new ColumnConstraint();
		ForeignConstraint fc = new ForeignConstraint();
		
		ResultStruct rs = dao.read("select constraint_name, constraint_type, search_condition, r_constraint_name, delete_rule, position " +
				"from user_constraints natural join user_cons_columns " +
				"where table_name = '" + tablename.toUpperCase() +"' and column_name = '"+ columnname.toUpperCase() +"'");

		ResultStruct rs2 = dao.read("select index_type from user_indexes join user_ind_columns using(index_name)" +
				"where user_ind_columns.table_name = '"+ tablename.toUpperCase() +"' and user_ind_columns.column_name = '"+ columnname.toUpperCase() +"'");
		
		if(!rs.isEmpty()) {
			
			for(int index = 0; index < rs.getResult().size(); index++) {

				String constraint_name = rs.getResult().get(index).getDataList().get(0);
				String constraint_type = rs.getResult().get(index).getDataList().get(1);
				String search_condition = rs.getResult().get(index).getDataList().get(2);
				String r_constraint_name = rs.getResult().get(index).getDataList().get(3);
				String delete_rule = rs.getResult().get(index).getDataList().get(4);
				String position = rs.getResult().get(index).getDataList().get(5);

				if(constraint_type.equals("P")) {
					cc.setPrimaryConstName(constraint_name);
					cc.setPrimary(true);
				}
				if(constraint_type.equals("U")) {
					cc.setUnique(true);
				}
				if(constraint_type.equals("C")) {
					if(search_condition.toUpperCase().matches(".*IS NOT NULL")) {
						cc.setNotnull(true);
					} else {
						cc.setCheck(search_condition);
						cc.setCheckConstName(constraint_name);
					}
				}
				if(constraint_type.equals("R")) {
					fc.setConstraintName(constraint_name);
					fc.setDeleteType(delete_rule);
					fc.setReferenceConstraintName(r_constraint_name);

					ResultStruct rss = dao.read("select table_name from user_constraints where constraint_name = '"+ r_constraint_name +"'");
					String refetable = rss.getResult().get(0).getDataList().get(0);
					fc.setReferenceTable(refetable);

					rss  = null;
					rss = dao.read("select column_name from user_cons_columns where constraint_name = '" + r_constraint_name + "' and table_name = '" + refetable.toUpperCase() + "' and position = '"+position+"'");
					String colname = rss.getResult().get(0).getDataList().get(0);
					fc.setReferenceTableColumn(colname);
				}
			}
		}
		
		if(!rs2.isEmpty()) {
			String index_type = rs2.getResult().get(0).getDataList().get(0);
			
			if(index_type.equals("NORMAL")) cc.setIndex(IndexTypes.NORMAL);
			else if(index_type.equals("BITMAP")) cc.setIndex(IndexTypes.BITMAP);
			else if(index_type.equals("NORMAL/REV")) cc.setIndex(IndexTypes.REVERSE);
			else cc.setIndex(IndexTypes.NONE);
		}
		
		cc.setForeign(fc);
		
		rs = dao.read("select data_default from user_tab_columns where table_name = '" +tablename.toUpperCase()+ 
				"' and column_name = '" + columnname.toUpperCase() + "'");
		
		if(!rs.isEmpty()) {
		
			String defaults = rs.getResult().get(0).getDataList().get(0);
		
			if(!defaults.equals("") && !defaults.equals("null")) cc.setDefault(defaults);
			
		}

		
		return cc;
	}

}
