package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * テーブルの差分情報生成クラス
 * @author no_known
 *
 */
public class CompareTable extends AbstractCompareObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DifferenceTable情報コピー
	 */
	public void setIdentityInformation(AbstractDifferenceObject difftable, Identity table) {
		DifferenceTable diffta = (DifferenceTable) difftable;
		TableStructure tablest = (TableStructure) table;
		
		diffta.setObjectId(tablest.getId());
		diffta.setTableId(tablest.getTableId());
	}
	
	/**
	 * TableStructureオブジェクトに対応するDifferenceTableを
	 * 生成する。
	 * @param tcm 変換するカラム情報
	 * @return 変換された変更箇所情報
	 */
	public AbstractDifferenceObject generateDifferenceInformation(Identity table) {
		// TODO Auto-generated method stub
		TableStructure tcm = (TableStructure) table;
		
		DifferenceTable dt = new DifferenceTable();
		
		dt.setDifferentChar('A');
		dt.setObjectId(tcm.getId());
		dt.setTableId(tcm.getTableId());
		
		if(tcm.getTableName() != null) {
			dt.setTableName(tcm.getTableName());
			
			List<DifferenceColumn> dc = new ArrayList<DifferenceColumn>();
			CompareColumn cc = new CompareColumn();
			for(int i = 0; i < tcm.getSchemas().size(); i++) {
				dc.add((DifferenceColumn) cc.generateDifferenceInformation(tcm.getSchemas().get(i)));
			}
			dt.setDifferenceColumns(dc);
			
			dt.setTableNameFlag(true);
		}
		
		return dt;
	}

	public AbstractDifferenceObject generateSubtraction(Identity oldtable, Identity newtable) {
		// TODO Auto-generated method stub
		TableStructure oldTable = (TableStructure) oldtable;
		TableStructure newTable = (TableStructure) newtable;
		
		
		DifferenceTable dt = new DifferenceTable();
		boolean bool = false;
		
		if(!oldTable.getId().equals(newTable.getId())) return null;
		
		dt.setDifferentChar('U');
		dt.setObjectId(newTable.getId());
		dt.setTableId(newTable.getTableId());
		
		if(oldTable.getTableName() != null
				? !oldTable.getTableName().equals(newTable.getTableName())
				: newTable.getTableName() != null) {
			dt.setTableName(newTable.getTableName());
			dt.setTableNameFlag(true);
			bool = true;
		}
		
		CompareColumn cc = new CompareColumn();
		List<? extends AbstractDifferenceObject> dclist = cc.compare(oldTable.getSchemas(), newTable.getSchemas());
		
		if(!dclist.isEmpty()) {
			List<DifferenceColumn> dl = new ArrayList<DifferenceColumn>();
			for(int i = 0; i < dclist.size(); i++) {
				dl.add((DifferenceColumn)dclist.get(i));
			}
			dt.setDifferenceColumns(dl);
			bool = true;
		}
		
		return bool ? dt : null;
	}

	@Override
	public AbstractDifferenceObject createDifferenceObject() {
		// TODO Auto-generated method stub
		return new DifferenceTable();
	}

}
