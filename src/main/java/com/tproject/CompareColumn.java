package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 古いカラム列と新しいカラム列を比較して変更箇所を確認するクラス
 * @author no_known
 *
 */
public class CompareColumn extends AbstractCompareObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DifferenceColumn情報コピー
	 */
	public void setIdentityInformation(AbstractDifferenceObject diffcolumn, Identity column) {
		DifferenceColumn diffcol = (DifferenceColumn) diffcolumn;
		TableColumnM tablecol = (TableColumnM) column;
		
		diffcol.setObjectId(tablecol.getId());
	}

	/**
	 * 二つのTableColumnMを比較して違う場合は違いを表す
	 * Differenceオブジェクトを返す
	 * @param oldColumn 元のカラム情報
	 * @param newColumn 更新されたカラム情報
	 * @return 変更箇所を表す情報
	 */
	public AbstractDifferenceObject generateSubtraction(Identity oldcolumn, Identity newcolumn) {
		// TODO Auto-generated method stub
		TableColumnM oldColumn = (TableColumnM) oldcolumn;
		TableColumnM newColumn = (TableColumnM) newcolumn;
		
		DifferenceColumn difcolumn = new DifferenceColumn();
		boolean bool = false;
		
		if(!oldColumn.getId().equals(newColumn.getId())) return null;
		
		if(oldColumn.getName() != null
				? !oldColumn.getName().equals(newColumn.getName())
				: newColumn.getName() != null ) {
			difcolumn.setColumnName(newColumn.getName());
			difcolumn.setColumnNameFlag(true);
			bool = true;
		}
		if(oldColumn.getType() != null
				? !oldColumn.getType().equals(newColumn.getType())
				: newColumn.getType() != null) {
			difcolumn.setColumnType(newColumn.getType());
			difcolumn.setColumnTypeFlag(true);
			difcolumn.setColumnLength(newColumn.getLength());
			difcolumn.setColumnLengthFlag(true);
			difcolumn.setColumnPrecisionLength(newColumn.getPrecisionLength());
			difcolumn.setColumnPrecisionLengthFlag(true);
			bool = true;
		}
		if(oldColumn.getLength() != null
				? !oldColumn.getLength().equals(newColumn.getLength())
				: newColumn.getLength() != null) {
			difcolumn.setColumnType(newColumn.getType());
			difcolumn.setColumnTypeFlag(true);
			difcolumn.setColumnLength(newColumn.getLength());
			difcolumn.setColumnLengthFlag(true);
			difcolumn.setColumnPrecisionLength(newColumn.getPrecisionLength());
			difcolumn.setColumnPrecisionLengthFlag(true);
			bool = true;
		}
		if(oldColumn.getPrecisionLength() != null
				? !oldColumn.getPrecisionLength().equals(newColumn.getPrecisionLength())
				: newColumn.getPrecisionLength() != null) {
			difcolumn.setColumnType(newColumn.getType());
			difcolumn.setColumnTypeFlag(true);
			difcolumn.setColumnLength(newColumn.getLength());
			difcolumn.setColumnLengthFlag(true);
			difcolumn.setColumnPrecisionLength(newColumn.getPrecisionLength());
			difcolumn.setColumnPrecisionLengthFlag(true);
			bool = true;
		}
		
		if(oldColumn.getConstraint().getIndex() != newColumn.getConstraint().getIndex()) {
			difcolumn.setColumnIndex(newColumn.getConstraint().getIndex());
			difcolumn.setColumnIndexFlag(true);
			bool = true;
		}
		if(oldColumn.getConstraint().isPrimary() != newColumn.getConstraint().isPrimary()) {
			difcolumn.setColumnPrimary(newColumn.getConstraint().isPrimary());
			difcolumn.setColumnPrimaryFlag(true);
			bool = true;
		}
		if(oldColumn.getConstraint().getPrimaryConstName() != null
				? !oldColumn.getConstraint().getPrimaryConstName().equals(newColumn.getConstraint().getPrimaryConstName())
				: newColumn.getConstraint().getPrimaryConstName() != null) {
			difcolumn.setColumnPrimaryConstName(newColumn.getConstraint().getPrimaryConstName());
			difcolumn.setColumnPrimaryConstNameFlag(true);
			bool = true;
		}
		if(oldColumn.getConstraint().getDefault() != null
				? !oldColumn.getConstraint().getDefault().equals(newColumn.getConstraint().getDefault())
				: newColumn.getConstraint().getDefault() != null) {
			difcolumn.setColumnDefault(newColumn.getConstraint().getDefault());
			difcolumn.setColumnDefaultFlag(true);
			
			difcolumn.setColumnType(newColumn.getType());
			bool = true;
		}
		if(oldColumn.getConstraint().getCheck() != null
				? !oldColumn.getConstraint().getCheck().equals(newColumn.getConstraint().getCheck())
				: newColumn.getConstraint().getCheck() != null) {
			difcolumn.setColumnCheck(newColumn.getConstraint().getCheck());
			difcolumn.setColumnCheckConst(newColumn.getConstraint().getCheckConstName());
			difcolumn.setColumnCheckFlag(true);
			bool = true;
		}
//		if(oldColumn.getConstraint().getCheckConstName() != null
//				? !oldColumn.getConstraint().getCheckConstName().equals(newColumn.getConstraint().getCheckConstName())
//				: newColumn.getConstraint().getCheckConstName() != null) {
//			difcolumn.setColumnCheckConst(newColumn.getConstraint().getCheckConstName());
//			bool = true;
//		}
		if(oldColumn.getConstraint().isUnique() != newColumn.getConstraint().isUnique()) {
			difcolumn.setColumnUnique(newColumn.getConstraint().isUnique());
			difcolumn.setColumnUniqueFlag(true);
			bool = true;
		}
		if(oldColumn.getConstraint().isNotnull() != newColumn.getConstraint().isNotnull()) {
			difcolumn.setColumnNotNull(newColumn.getConstraint().isNotnull());
			difcolumn.setColumnNotNullFlag(true);
			bool = true;
		}
	
		if(oldColumn.getConstraint().getForeign().getReferenceTable() != null
				? !oldColumn.getConstraint().getForeign().getReferenceTable().equals(newColumn.getConstraint().getForeign().getReferenceTable())
				: newColumn.getConstraint().getForeign().getReferenceTable() != null) {
			difcolumn.setColumnReferenceTable(newColumn.getConstraint().getForeign().getReferenceTable());
//			difcolumn.setColumnReferenceTableFlag(true);
			difcolumn.setColumnForeignFlag(true);
			bool = true;
		}
		if(oldColumn.getConstraint().getForeign().getReferenceTableColumn() != null
				? !oldColumn.getConstraint().getForeign().getReferenceTableColumn().equals(newColumn.getConstraint().getForeign().getReferenceTableColumn())
				: newColumn.getConstraint().getForeign().getReferenceTableColumn() != null) {
			difcolumn.setColumnReferenceTableColumn(newColumn.getConstraint().getForeign().getReferenceTableColumn());
//			difcolumn.setColumnReferenceTableColumnFlag(true);
			difcolumn.setColumnForeignFlag(true);
			bool = true;
		}
		if(oldColumn.getConstraint().getForeign().getReferenceConstraintName() != null
				? !oldColumn.getConstraint().getForeign().getReferenceConstraintName().equals(newColumn.getConstraint().getForeign().getReferenceConstraintName())
				: newColumn.getConstraint().getForeign().getReferenceConstraintName() != null) {
			difcolumn.setColumnReferenceConstraintName(newColumn.getConstraint().getForeign().getReferenceConstraintName());
//			difcolumn.setColumnReferenceConstraintNameFlag(true);
			difcolumn.setColumnForeignFlag(true);
			bool = true;
		}
		if(oldColumn.getConstraint().getForeign().getDeleteType() != null
				? !oldColumn.getConstraint().getForeign().getDeleteType().equals(newColumn.getConstraint().getForeign().getDeleteType())
				: newColumn.getConstraint().getForeign().getDeleteType() != null) {
			difcolumn.setColumnDeleteType(newColumn.getConstraint().getForeign().getDeleteType());
//			difcolumn.setColumnDeleteTypeFlag(true);
			difcolumn.setColumnForeignFlag(true);
			bool = true;
		}
		
		// 外部キーの値が更新されているときのみ設定する。
		if(difcolumn.isColumnForeignFlag()) {
			difcolumn.setColumnForeignName(newColumn.getConstraint().getForeign().getConstraintName());
		}
		
		difcolumn.setDifferentChar('U');
		difcolumn.setObjectId(newColumn.getId());
		
		return bool ? difcolumn : null;
	}

	/**
	 * TableColunMオブジェクトに対応するDifferenceColumnを
	 * 生成する。
	 * @param tcm 変換するカラム情報
	 * @return 変換された変更箇所情報
	 */
	public AbstractDifferenceObject generateDifferenceInformation(Identity column) {
		// TODO Auto-generated method stub
		TableColumnM tcm = (TableColumnM) column;
		
		DifferenceColumn dc = new DifferenceColumn();
		
		dc.setDifferentChar('A');
		dc.setObjectId(tcm.getId());
		
		if(tcm.getTableName() != null) {
			dc.setColumnTableName(tcm.getTableName());
		}
		if(tcm.getName() != null) {
			dc.setColumnName(tcm.getName());
			dc.setColumnNameFlag(true);
		}
		if(tcm.getType() != null) {
			dc.setColumnType(tcm.getType());
			dc.setColumnTypeFlag(true);
		}
		if(tcm.getLength() != null) {
			dc.setColumnLength(tcm.getLength());
			dc.setColumnLengthFlag(true);
		}
		if(tcm.getPrecisionLength() != null) {
			dc.setColumnPrecisionLength(tcm.getPrecisionLength());
			dc.setColumnPrecisionLengthFlag(true);
		}
		if(tcm.getConstraint().getIndex() != IndexTypes.NONE) {
			dc.setColumnIndex(tcm.getConstraint().getIndex());
			dc.setColumnIndexFlag(true);
		}
		if(tcm.getConstraint().isPrimary() != false) {
			dc.setColumnPrimary(true);
			dc.setColumnPrimaryFlag(true);
		}
		if(tcm.getConstraint().getPrimaryConstName() != null) {
			dc.setColumnPrimaryConstName(tcm.getConstraint().getPrimaryConstName());
			dc.setColumnPrimaryConstNameFlag(true);
		}
		if(tcm.getConstraint().getCheck() != null) {
			dc.setColumnCheck(tcm.getConstraint().getCheck());
			dc.setColumnCheckFlag(true);
		}
		if(tcm.getConstraint().getDefault() != null) {
			dc.setColumnDefault(tcm.getConstraint().getDefault());
			dc.setColumnDefaultFlag(true);
		}
		/* たとえfalseが設定されていた場合でもColumnConstraintのunique,notnull
		 * は初期値がfalseなので問題はない。
		 */
		if(tcm.getConstraint().isUnique() != false) {
			dc.setColumnUnique(true);
			dc.setColumnUniqueFlag(true);
		}
		if(tcm.getConstraint().isNotnull() != false) {
			dc.setColumnNotNull(true);
			dc.setColumnNotNullFlag(true);
		}
		if(tcm.getConstraint().getForeign().getConstraintName() != null) {
			dc.setColumnForeignName(tcm.getConstraint().getForeign().getConstraintName());
			dc.setColumnForeignFlag(true);
		}
		if(tcm.getConstraint().getForeign().getReferenceTable() != null) {
			dc.setColumnReferenceTable(tcm.getConstraint().getForeign().getReferenceTable());
			dc.setColumnForeignFlag(true);
		}
		if(tcm.getConstraint().getForeign().getReferenceTableColumn() != null) {
			dc.setColumnReferenceTableColumn(tcm.getConstraint().getForeign().getReferenceTableColumn());
			dc.setColumnForeignFlag(true);
		}
		if(tcm.getConstraint().getForeign().getReferenceConstraintName() != null) {
			dc.setColumnReferenceConstraintName(tcm.getConstraint().getForeign().getReferenceConstraintName());
			dc.setColumnForeignFlag(true);
		}
		if(!tcm.getConstraint().getForeign().getDeleteType().equals("NO ACTION")) {
			dc.setColumnDeleteType(tcm.getConstraint().getForeign().getDeleteType());
			dc.setColumnForeignFlag(true);
		}
		
		return dc;
	}

	@Override
	public AbstractDifferenceObject createDifferenceObject() {
		// TODO Auto-generated method stub
		return new DifferenceColumn();
	}


}
