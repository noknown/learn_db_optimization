package com.tproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import com.tproject.ColumnLength.LENGTH;
import com.tproject.ColumnTypes.TYPES;

/**
 * テーブル変更情報を実際のテーブル上に適応させるクラス。
 * @author no_known
 * 
 */
public class DatabaseEdit {

	/**
	 * テーブル全体に変更情報を適用させる。
	 * 
	 * @param dao データベース操作のためのDAO
	 * @param differencetablelist テーブル変更情報のリスト
	 * @param username テーブル変更を適用するユーザー
	 * @param databasename テーブル変更を適用するユーザーのパスワード
	 */
	public static void edit(IDao dao, List<DifferenceTable> differencetablelist, String username, String databasename) {
		// TODO Auto-generated method stub
		
		for(DifferenceTable dt : differencetablelist) {
			String old_name = dt.getObjectId().split(":")[0];
			
			
			// テーブル処理
			if(dt.getDifferentChar().equals('U')) {
				
				//属性処理
				attributeEdit(dao, old_name, dt.getDifferenceColumns());
				
				if(dt.isTableNameFlag()) {
					dao.insert("alter table " + old_name + " rename to "+ dt.getTableName() + "");
					dao.insert("update system.tables set table_name = '" + dt.getTableName() + "' where table_id = '" + dt.getTableId() +"'");
				}
				
			} else if(dt.getDifferentChar().equals('A')) {
				StringBuilder command = new StringBuilder("create table " + dt.getTableName() + " (");
				List<DifferenceColumn> indexcolumn = new ArrayList<DifferenceColumn>();
				List<String> primarycolumn = new ArrayList<String>();

				HashMap<String, List<DifferenceColumn>> foreigncolumns = new HashMap<String, List<DifferenceColumn>>();
				
				for(int i = 0; i < dt.getDifferenceColumns().size(); i++) {
			
					command.append(createColumnString(dt.getDifferenceColumns().get(i)));
					if(i != dt.getDifferenceColumns().size()-1) command.append(", ");
					
					if(dt.getDifferenceColumns().get(i).getColumnIndex() != null && dt.getDifferenceColumns().get(i).getColumnIndex() != IndexTypes.NONE)
						indexcolumn.add(dt.getDifferenceColumns().get(i));
					
					if(dt.getDifferenceColumns().get(i).isColumnPrimary()) {
						primarycolumn.add(dt.getDifferenceColumns().get(i).getColumnName());
					}
					if(dt.getDifferenceColumns().get(i).isColumnForeignFlag()) {
						if(foreigncolumns.containsKey(dt.getDifferenceColumns().get(i).getColumnReferenceTable())) {
							List<DifferenceColumn> dfc = foreigncolumns.get(dt.getDifferenceColumns().get(i).getColumnReferenceTable());
							dfc.add(dt.getDifferenceColumns().get(i));
						} else {
							List<DifferenceColumn> dfc = new ArrayList<DifferenceColumn>();
							dfc.add(dt.getDifferenceColumns().get(i));
							
							foreigncolumns.put(dt.getDifferenceColumns().get(i).getColumnReferenceTable(), dfc);
						}
//						foreigncolumn.add(dt.getDifferenceColumns().get(i));
					}
				}
				command.append(" )");
				System.err.println(command.toString());
				dao.insert(command.toString());
				
				// 主キー設定
				StringBuilder tables = new StringBuilder();
				for(String tablename : primarycolumn) {
					tables.append(tablename+",");
				}
				if(!tables.toString().equals("")) {
					String ptables = tables.substring(0, tables.length()-1);
					System.err.println("alter table "+ dt.getTableName() +" add primary key (" + ptables + ")");
					dao.insert("alter table "+ dt.getTableName() +" add primary key (" + ptables + ")");
				}
				
				// インデックス設定
				for(DifferenceColumn dc : indexcolumn) {
					System.err.println(dc.getColumnName() +":"+ old_name);
					addIndex(dc, dao, dt.getTableName(), dc.getColumnName());
				}
				
				// 外部キー設定
				for(String table : foreigncolumns.keySet()) {
					List<DifferenceColumn> colList = foreigncolumns.get(table);
					
					StringBuilder columns = new StringBuilder();
					StringBuilder mycolumns = new StringBuilder();					
					
					for(DifferenceColumn dc : colList) {
						columns.append(dc.getColumnReferenceTableColumn()+",");
						mycolumns.append(dc.getColumnName()+",");
					}
					
					if(!columns.toString().equals("")) {
						String pcolumns = columns.substring(0, columns.length()-1);
						String pmycolumns = mycolumns.substring(0, mycolumns.length()-1);

						System.err.println("alter table "+ dt.getTableName() +" add foreign key ("+ pmycolumns +")" +
								" references "+ table +"("+ pcolumns +")");
						dao.insert("alter table "+ dt.getTableName() +" add foreign key ("+ pmycolumns +")" +
								" references "+ table +"("+ pcolumns +")");
					}
				}
				
//				for(DifferenceColumn dc : foreigncolumn) {
//					tables.append(dc.getColumnReferenceTable()+",");
//					columns.append(dc.getColumnReferenceTableColumn()+",");
//					mycolumns.append(dc.getColumnName()+",");
//				}
//				if(!tables.toString().equals("")) {
//					String ptables = tables.substring(0, tables.length()-1);
//					String pcolumns = columns.substring(0, columns.length()-1);
//					String pmycolumns = mycolumns.substring(0, mycolumns.length()-1);
//					
//					System.err.println("alter table "+ dt.getTableName() + " add foreign key ("+ ptables +")" +
//							" references "+pcolumns +"("+ pmycolumns + ")");
//					dao.insert("alter table "+ dt.getTableName() + " add foreign key ("+ ptables +")" +
//							" references "+pcolumns +"("+ pmycolumns + ")");
//				}
//				
				
				ResultStruct rs = dao.read("select database_id from system.databases where user_name = '"+
						username +"' and database_name = '"+ databasename +"'");
				dao.insert("insert into system.tables values (system.table_seq1.nextval, '"+ dt.getTableName() +"', '"+ rs.getResult().get(0).getDataList().get(0)+"')");
				
				
			} else if(dt.getDifferentChar().equals('D')) {
				System.err.println("drop table "+old_name);
				dao.insert("drop table "+old_name);
				
				ResultStruct rs = dao.read("select database_id from system.databases where user_name = '"+
						username +"' and database_name = '"+ databasename +"'");
				dao.insert("delete from system.tables where database_id = '"+
						rs.getResult().get(0).getDataList().get(0)+"' and table_name = '"+ old_name +"'");
				
			}
			

		}
		
	}
	
	/**
	 * カラム（属性）の変更を適用させるメソッド
	 * @param dao データベース操作のためのDAO
	 * @param old_name 変更するカラムの存在するテーブル名
	 * @param differencecolumnlist 変更するカラムの情報リスト
	 */
	private static void attributeEdit(IDao dao, final String old_name, List<DifferenceColumn> differencecolumnlist) {

		ArrayList<String> updatestrlist = new ArrayList<String>();
		boolean updateprimaryflag = false;
		boolean deleteprimaryflag = false;

		HashMap<String, List<DifferenceColumn>> tablehash = new HashMap<String, List<DifferenceColumn>>();
		HashMap<String, List<String>> cnamehash = new HashMap<String, List<String>>();
		boolean foreignflag = false;
		
		
		for(DifferenceColumn dc : differencecolumnlist) {
			String old_cname = dc.getObjectId().split(":")[0];
			
			if(dc.getDifferentChar().equals('U')) {
				

				if(dc.isColumnTypeFlag()) {
					int length = dc.getColumnLength();
					Integer plength = dc.getColumnPrecisionLength();
					//属性を変えるのですべてのデータを消しておく
					dao.insert("update "+ old_name+ " set " + old_cname + " = NULL");
					
					ColumnLength.LENGTH l = ColumnLength.COLUMN_LENGTH.get(dc.getColumnType());
					if(l == LENGTH.YES)
						if(dc.getColumnType().equals("NUMBER") && plength != null)
							dao.insert("alter table "+ old_name +" modify ("+ old_cname +" "+ dc.getColumnType() + "("+ length + ","+plength+"))");
						else
							dao.insert("alter table "+ old_name +" modify (" + old_cname + " " + dc.getColumnType() + "(" + length +"))");
					else
						dao.insert("alter table "+ old_name +" modify (" + old_cname + " " + dc.getColumnType() + " )");
				}
				if(dc.isColumnDefaultFlag()) {
					ColumnTypes.TYPES type = ColumnTypes.COLUMN_TYPES.get(dc.getColumnType());
					
					if(dc.getColumnDefault() == null) {
						dao.insert("alter table "+ old_name +" modify " +old_cname+ " default null");
					} else {
						if(type == TYPES.CHAR) {
							dao.insert("alter table "+ old_name +" modify " +old_cname+ " default '"+dc.getColumnDefault() +"'");
						} else if(type == TYPES.DIGIT) {
							dao.insert("alter table "+ old_name +" modify " +old_cname+ " default "+dc.getColumnDefault() +"");
						} else if(type == TYPES.TIME) {

						} else {

						}
					}
				}
				if(dc.isColumnNotNullFlag()) {
					if(dc.isColumnNotNull())
					dao.insert("alter table "+ old_name +" modify ("+ old_cname +" not null)");
					else
					dao.insert("alter table "+ old_name +" modify ("+ old_cname +" null)");
				}
				if(dc.isColumnUniqueFlag()) {
					if(dc.isColumnUnique())
					dao.insert("alter table " + old_name + " add unique (" + old_cname + ")");
					else
					dao.insert("alter table " + old_name + " drop unique (" + old_cname + ")");
				}
				if(dc.isColumnCheckFlag()) {
					System.err.println(dc.getColumnCheck() + " : " + dc.getColumnCheckConst());
					
					if(dc.getColumnCheck() != null && dc.getColumnCheckConst() == null) {
						
						dao.insert("alter table " + old_name + " add check(" + dc.getColumnCheck() +")");
						
					} else if(dc.getColumnCheck() != null && dc.getColumnCheckConst() != null) {
						
						dao.insert("alter table " + old_name + " drop constraint " + dc.getColumnCheckConst());
						dao.insert("alter table " + old_name + " add constraint "
								+ dc.getColumnCheckConst() +" check ("+ dc.getColumnCheck() +")");
						
					} else if(dc.getColumnCheck() == null && dc.getColumnCheckConst() != null) {
						
						dao.insert("alter table " + old_name + " drop constraint "+dc.getColumnCheckConst());
						
					}
					
				}
				if(dc.isColumnForeignFlag()) {
					// 外部参照制約を消しておく
					if(dc.getColumnForeignName() != null)
						dao.insert("alter table "+ old_name +" drop constraint "+dc.getColumnForeignName());
//					
//					if(dc.getColumnReferenceTable() != null) {
//						String consts = " ";
//						if(dc.getColumnForeignName() != null) consts = " constraint "+dc.getColumnForeignName()+" ";
//
//						String deletes = (dc.getColumnDeleteType() != null && !dc.getColumnDeleteType().equals("NO ACTION")) ? "on delete "+ dc.getColumnDeleteType() : "";
//						
//						System.err.println("alter table "+ old_name +" add " + consts +
//								" foreign key ("+ old_cname +") references "+ dc.getColumnReferenceTable() +
//								" ("+ dc.getColumnReferenceTableColumn() +") "+ deletes);
						if(dc.getColumnReferenceTable() != null) {
							if(tablehash.containsKey(dc.getColumnReferenceTable())) {
								List<DifferenceColumn> dlist = tablehash.get(dc.getColumnReferenceTable());
								dlist.add(dc);

								List<String> slist = cnamehash.get(dc.getColumnReferenceTable());
								slist.add(old_cname);
							} else {
								List<DifferenceColumn> dlist = new ArrayList<DifferenceColumn>();
								dlist.add(dc);
								tablehash.put(dc.getColumnReferenceTable(), dlist);

								List<String> slist = new ArrayList<String>();
								slist.add(old_cname);
								cnamehash.put(dc.getColumnReferenceTable(), slist);
							}
							foreignflag = true;
						}
//						dao.insert("alter table "+ old_name +" add " + consts +
//								" foreign key ("+ old_cname +") references "+ dc.getColumnReferenceTable() +
//								" ("+ dc.getColumnReferenceTableColumn() +") "+ deletes);
//					}
				}
				if(dc.isColumnPrimaryFlag()) {
					
					
					updateprimaryflag = true;
					if(!dc.isColumnPrimary()) {
						deleteprimaryflag = true;
					}
					
				}
				if(dc.isColumnIndexFlag()) {
					ResultStruct rs = dao.read("select index_name from user_ind_columns where table_name = '"+ old_name.toUpperCase() +"'" +
							" and column_name = '"+ old_cname.toUpperCase() + "'");
					if(!rs.isEmpty()) {
						String indexn = rs.getResult().get(0).getDataList().get(0);
						dao.insert("drop index "+indexn);
					}
					
					addIndex(dc, dao, old_name, old_cname);
				}
				if(dc.isColumnNameFlag()) {
					dao.insert("alter table "+ old_name +" rename column "+ old_cname +" to "+ dc.getColumnName());
				}

				
			} else if(dc.getDifferentChar().equals('A')) {
				if(dc.getColumnTableName() != null && dc.getColumnTableName().equals(old_name)) {
					if(dc.isColumnPrimaryFlag()) {
						updateprimaryflag = true;
					}
					if(dc.getColumnReferenceTable() != null) {
						if(tablehash.containsKey(dc.getColumnReferenceTable())) {
							List<DifferenceColumn> dlist = tablehash.get(dc.getColumnReferenceTable());
							dlist.add(dc);

							List<String> slist = cnamehash.get(dc.getColumnReferenceTable());
							slist.add(old_cname);
						} else {
							List<DifferenceColumn> dlist = new ArrayList<DifferenceColumn>();
							dlist.add(dc);
							tablehash.put(dc.getColumnReferenceTable(), dlist);

							List<String> slist = new ArrayList<String>();
							slist.add(old_cname);
							cnamehash.put(dc.getColumnReferenceTable(), slist);
						}
						foreignflag = true;
					}
					
					String command = "alter table "+ old_name + " add (" + createColumnString(dc) + ")";
					dao.insert(command);
				} else {
//					String idr = "", idp = "";
//					
//					dao.insert("alter table "+old_name+" add ("+ createColumnString(dc) +")");
//					System.err.println("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ dc.getColumnTableName().toUpperCase() +"' and constraint_type = 'R' and position = '1') and table_name = '"+old_name.toUpperCase()+"'");
//					ResultStruct rs = dao.read("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ dc.getColumnTableName().toUpperCase() +"' and constraint_type = 'R' and position = '1') and table_name = '"+old_name.toUpperCase()+"'");
//					if(!rs.isEmpty()) idr = old_name+"."+rs.getResult().get(0).getDataList().get(0);
//					rs = null;
//					System.err.println("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ old_name.toUpperCase() +"' and constraint_type = 'R' and position = '1') and table_name = '"+dc.getColumnTableName().toUpperCase()+"'");
//					rs = dao.read("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ old_name.toUpperCase() +"' and constraint_type = 'R' and position = '1') and table_name = '"+dc.getColumnTableName().toUpperCase()+"'");
//					if(!rs.isEmpty()) idr = dc.getColumnTableName()+"."+rs.getResult().get(0).getDataList().get(0);
//					
//					
//					rs = null;
//					System.err.println("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ dc.getColumnTableName().toUpperCase() +"' and constraint_type = 'P' and position = '1') and table_name = '"+old_name.toUpperCase()+"'");
//					rs = dao.read("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ dc.getColumnTableName().toUpperCase() +"' and constraint_type = 'P' and position = '1') and table_name = '"+old_name.toUpperCase()+"'");
//					if(!rs.isEmpty()) idp = old_name+"."+rs.getResult().get(0).getDataList().get(0);
//					
//					rs = null;
//					System.err.println("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ old_name.toUpperCase() +"' and constraint_type = 'P' and position = '1') and table_name = '"+dc.getColumnTableName().toUpperCase()+"'");
//					rs = dao.read("select column_name from user_cons_columns where column_name = "+
//							"(select column_name from user_cons_columns natural join user_constraints where " +
//							"table_name = '"+ old_name.toUpperCase() +"' and constraint_type = 'P' and position = '1') and table_name = '"+dc.getColumnTableName().toUpperCase()+"'");
//					if(!rs.isEmpty()) idp = dc.getColumnTableName()+"."+rs.getResult().get(0).getDataList().get(0);

					
					dao.insert("alter table "+old_name+" add ("+ createColumnString(dc) +")");
					List<String> from = new ArrayList<String>();
					List<String> to = new ArrayList<String>();
					
					System.err.println("select cons.column_name, cons.r_constraint_name " +
							"from (select * from user_constraints natural join user_cons_columns) cons " +
							"where cons.table_name = '"+ old_name.toUpperCase() +"' and cons.constraint_type = 'R' and r_constraint_name " +
							"in (select constraint_name from user_constraints where table_name = '"+ dc.getColumnTableName().toUpperCase() +"') " +
							"union select cons.column_name, cons.r_constraint_name " +
							"from (select * from user_constraints natural join user_cons_columns) cons " +
							"where cons.table_name = '"+ dc.getColumnTableName().toUpperCase() +"' and cons.constraint_type = 'R' and r_constraint_name " +
							"in (select constraint_name from user_constraints where table_name = '"+ old_name.toUpperCase() +"');");
					ResultStruct rs = dao.read("select cons.column_name, cons.r_constraint_name " +
							"from (select * from user_constraints natural join user_cons_columns) cons " +
							"where cons.table_name = '"+ old_name.toUpperCase() +"' and cons.constraint_type = 'R' and r_constraint_name " +
							"in (select constraint_name from user_constraints where table_name = '"+ dc.getColumnTableName().toUpperCase() +"') " +
							"union select cons.column_name, cons.r_constraint_name " +
							"from (select * from user_constraints natural join user_cons_columns) cons " +
							"where cons.table_name = '"+ dc.getColumnTableName().toUpperCase() +"' and cons.constraint_type = 'R' and r_constraint_name " +
							"in (select constraint_name from user_constraints where table_name = '"+ old_name.toUpperCase() +"')");
					for(ResultData rd : rs.getResult()) {
						from.add(rd.getDataList().get(0));
					}
					
					String constraints = rs.getResult().get(0).getDataList().get(1);
					ResultStruct rr = dao.read("select table_name, column_name from user_cons_columns " +
							"where constraint_name = '"+ constraints +"'");
					for(ResultData rd : rr.getResult()) {
						to.add(rd.getDataList().get(1));
					}
				
					StringBuilder wheres = new StringBuilder();
					for(int i = 0; i < from.size(); i++) {
						wheres.append(old_name+"."+from.get(i) + " = " + dc.getColumnTableName()+"."+to.get(i) + " and ");
					}
					String wh = wheres.substring(0, wheres.length()-4);
					
					System.err.println("update "+ old_name +" set "+ old_cname + " = (select distinct "+
							old_cname+" from "+ dc.getColumnTableName() +" where "+ wh +")");
					dao.insert("update "+ old_name +" set "+ old_cname + " = (select distinct "+
							old_cname+" from "+ dc.getColumnTableName() +" where "+ wh +")");
					
//					System.err.println("update "+ old_name +" set "+ old_cname + " = (select distinct "+
//							old_cname+" from "+ dc.getColumnTableName() + " where " + idr + " = "+ idp+")");
//					dao.insert("update "+ old_name +" set "+ old_cname + " = (select distinct "+
//							old_cname+" from "+ dc.getColumnTableName() + " where " + idr + " = "+ idp+")");
					
//					dao.insert("alter table "+dc.getColumnTableName()+" drop column "+old_cname);
				}
				
				if(dc.getColumnIndex() != IndexTypes.NONE) {
					addIndex(dc, dao, old_name, dc.getColumnName());
				}
				
			} else if(dc.getDifferentChar().equals('D')) {
				
				dao.insert("alter table "+ old_name +" drop column "+ old_cname);
				
			}
			
			if(dc.isColumnPrimary()) {
				updatestrlist.add(old_cname.toUpperCase());
			}
		}
		
		
		// 主キー制約設定
		// 主キーの削除。主キーの変更がある場合は行う必要がある。
		// 単純に主キーのみを消したい場合はdeleteprimaryflagがtrueになっていることで判断
		if(updateprimaryflag) {
			ResultStruct rs =  dao.read("select constraint_name, column_name from user_constraints natural join user_cons_columns " +
			" where constraint_type = 'P' and table_name = '"+ old_name.toUpperCase() + "'");

			if(!rs.isEmpty()) {
				String constname = rs.getResult().get(0).getDataList().get(0);

				System.err.println("alter table "+ old_name +" drop constraint " + constname);
				dao.insert("alter table "+ old_name +" drop constraint " + constname);
			}
			if(deleteprimaryflag) return;
			
			StringBuilder tables = new StringBuilder();
			for(int i = 0; i < rs.getResult().size(); i++) {
				String columname = rs.getResult().get(i).getDataList().get(1);
				
				if(!updatestrlist.contains(columname)) {
					tables.append(columname+",");
				}

			}
			for(String columnname : updatestrlist) {
				tables.append(columnname+",");
			}
			
			if(!tables.toString().equals("")) {
				String ptables = tables.substring(0, tables.length()-1);
				System.err.println(ptables);
				dao.insert("alter table "+ old_name +" add primary key (" + ptables + ")");
			}
		}
		
		// foreign keyの設定
		if(foreignflag) {
			
			
			for(String strkey : tablehash.keySet()) {
				List<DifferenceColumn> dclist = tablehash.get(strkey);
				List<String> stlist = cnamehash.get(strkey);
				
				StringBuilder dcolumns = new StringBuilder();
				StringBuilder fcolumns = new StringBuilder();
				String foreignname = null;
				String deletetype = null;
				
				for(String str : stlist) {
					dcolumns.append(str+",");
				}
				for(DifferenceColumn dc : dclist) {
					fcolumns.append(dc.getColumnReferenceTableColumn()+",");
					
					
					if(dc.getColumnForeignName() != null) {
//						dao.insert("alter table "+ old_name +" drop constraint "+dc.getColumnForeignName());
						foreignname = dc.getColumnForeignName();
						
						if(deletetype == null) deletetype = dc.getColumnDeleteType();
					}
				}
				
				String dcolumn = dcolumns.substring(0, dcolumns.length()-1);
				String fcolumn = fcolumns.substring(0, fcolumns.length()-1);
				
				String consts = (foreignname != null) ? " constraint "+ foreignname + " " : "";
				String deletes = (deletetype != null && !deletetype.equals("NO ACTION")) ? "on delete "+deletetype : "";

				System.err.println("alter table "+ old_name +" add " + consts +
						" foreign key ("+ dcolumn +") references "+ strkey + " ("+ fcolumn + ") "+ deletes);
				dao.insert("alter table "+ old_name +" add " + consts +
						" foreign key ("+ dcolumn +") references "+ strkey + " ("+ fcolumn + ") "+ deletes);
				
			}
			
//			dao.insert("alter table "+ old_name +" add " + consts +
//			" foreign key ("+ old_cname +") references "+ dc.getColumnReferenceTable() +
//			" ("+ dc.getColumnReferenceTableColumn() +") "+ deletes);
			
//			if(dc.getColumnForeignName() != null)
//				dao.insert("alter table "+ old_name +" drop constraint "+dc.getColumnForeignName());
//			
//			if(dc.getColumnReferenceTable() != null) {
//				String consts = " ";
//				if(dc.getColumnForeignName() != null) consts = " constraint "+dc.getColumnForeignName()+" ";
//
//				String deletes = (dc.getColumnDeleteType() != null && !dc.getColumnDeleteType().equals("NO ACTION")) ? "on delete "+ dc.getColumnDeleteType() : "";

		}

		
	}
	
	/**
	 * Create Table文で用いるカラムの情報を生成する。
	 * 
	 * @param differencecolumn カラム情報を生成するために使用する変更情報
	 * @return 生成されたカラム情報
	 */
	public static String createColumnString(DifferenceColumn differencecolumn) {
		ColumnLength.LENGTH l = ColumnLength.COLUMN_LENGTH.get(differencecolumn.getColumnType());
		String basis;
		if(l == LENGTH.YES)
			if(differencecolumn.getColumnType().equals("NUMBER") && differencecolumn.getColumnPrecisionLength() != null) 
				basis = differencecolumn.getColumnName()+" "+differencecolumn.getColumnType()+"("+differencecolumn.getColumnLength()+","+differencecolumn.getColumnPrecisionLength()+")";
			else
				basis = differencecolumn.getColumnName()+" "+differencecolumn.getColumnType()+"("+differencecolumn.getColumnLength()+")";
		else
			basis = differencecolumn.getColumnName()+" "+differencecolumn.getColumnType()+" ";
		
		String notnull = differencecolumn.isColumnNotNullFlag() && differencecolumn.isColumnNotNull() ? " NOT NULL" : "";
		String unique = differencecolumn.isColumnUniqueFlag() && differencecolumn.isColumnUnique() ? " UNIQUE" : "";
		ColumnTypes.TYPES t = ColumnTypes.COLUMN_TYPES.get(differencecolumn.getColumnType());
		String defaults = differencecolumn.isColumnDefaultFlag() ? " DEFAULT " + (t == TYPES.DIGIT ? differencecolumn.getColumnDefault() : "'"+differencecolumn.getColumnDefault()+"'") : "";
		String delete = differencecolumn.isColumnForeignFlag() && differencecolumn.getColumnDeleteType() != null 
			&& !differencecolumn.getColumnDeleteType().equals("NO ACTION") ? " ON DELETE "+ differencecolumn.getColumnDeleteType() : "";
	
		
		return basis + defaults + notnull + unique + delete;
	}
	
	/**
	 * インデックス情報を適用させるメソッド
	 * 
	 * @param differencecolumn カラム変更情報
	 * @param dao データベース操作のためのDAO
	 * @param old_name 変更対象のテーブル名
	 * @param old_cname 変更対象のカラム名
	 */
	public static void addIndex(DifferenceColumn differencecolumn, IDao dao, String old_name, String old_cname) {
		if(differencecolumn.getColumnIndex() == IndexTypes.NORMAL)
			dao.insert("create index "+ old_name.toUpperCase() +"_IDX on "+ old_name +"("+ old_cname +")");
//		else if(dc.getColumnIndex() == IndexTypes.BITMAP)
//			dao.insert("create bitmap index "+ old_name.toUpperCase() +"_IDX on "+ old_name +"("+ old_cname +")");
		else if(differencecolumn.getColumnIndex() == IndexTypes.REVERSE)
			dao.insert("create index "+ old_name.toUpperCase() +"_IDX on "+ old_name +"("+ old_cname +") reverse");
	}

}
