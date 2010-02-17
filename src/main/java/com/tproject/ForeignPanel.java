package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.diff.ToString;

/**
 * 外部キー参照の設定を行うパネル
 * @author no_known
 *
 */
public class ForeignPanel extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean check = false;
	private ForeignConstraint foreignconst;
	private TableColumnM columnm;

	private TableStructure temptable;
	private TableColumnM tempcolumn;
	private ChoiceElement tempforeign;

	public ForeignPanel(String panelid, final PropertyModel<TableColumnM> propertymodel,
			final List<TableStructure> tablelist) {
		// TODO Auto-generated constructor stub
		super(panelid, propertymodel);
		
		columnm = propertymodel.getObject();
		if(columnm == null) columnm = new TableColumnM();
		
		foreignconst = columnm.getConstraint().getForeign();
		if(foreignconst == null) foreignconst = new ForeignConstraint();
		
		final List<TableColumnM> tclist = new ArrayList<TableColumnM>();
		
		final WebMarkupContainer container = new WebMarkupContainer("cont");
		container.setOutputMarkupId(true);
		
		final DropDownChoice<TableStructure> tablest;
		final DropDownChoice<TableColumnM> columns;
		final DropDownChoice<ChoiceElement> foreigns;
		
		if(foreignconst.getReferenceTable() == null) {
			temptable = new TableStructure("");
		} else {
			TableStructure sts = null;
			
			for(TableStructure ts : tablelist) {
				if(ts.getTableName().toLowerCase().equals(foreignconst.getReferenceTable().toLowerCase())) {
					sts = ts;
					break;
				}
			}
			temptable = sts;
		}
		
		if(foreignconst == null || foreignconst.getReferenceTableColumn() == null || temptable == null) {
			tempcolumn = new TableColumnM("","",0,new ColumnConstraint());
		} else {
			TableColumnM stcm = null;
			
			for(TableColumnM tcm : temptable.getSchemas()) {
				if(isReferenceColumn(tcm)) tclist.add(tcm);
				if(tcm.getName().toLowerCase().equals(foreignconst.getReferenceTableColumn().toLowerCase())) {
					stcm = tcm;
				}
			}
			tempcolumn = stcm;
		}
		
		if(foreignconst == null || foreignconst.getDeleteType() == null) {
			tempforeign = foreelemnt.get(0);
		} else {
			ChoiceElement fele = null;
			
			for(ChoiceElement fe : foreelemnt) {
				if(fe.getName().equals(foreignconst.getDeleteType())) {
					fele = fe;
				}
			}
			tempforeign = fele;
		}

		// ページ描画時にチェックを入れるかどうか
		if(foreignconst.getReferenceTable() != null) {
			this.check = true;
		}
				
		tablest = new DropDownChoice<TableStructure>("rtable", new PropertyModel<TableStructure>(this, "temptable"),
				tablelist, new ChoiceRenderer<TableStructure>("tableName", "tableName"));
		tablest.setOutputMarkupId(true);
		
		columns = new DropDownChoice<TableColumnM>("rrefer", new PropertyModel<TableColumnM>(this, "tempcolumn"),
				tclist, new ChoiceRenderer<TableColumnM>("name", "name"));
		columns.setOutputMarkupId(true);
		
		foreigns = new DropDownChoice<ChoiceElement>("detype", new PropertyModel<ChoiceElement>(this, "tempforeign"),
				foreelemnt, new ChoiceRenderer<ChoiceElement>("name", "id"));
		foreigns.setOutputMarkupId(true);
		
		// 主キーリスト更新のためのビヘイビア
		tablest.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
				tclist.clear();
				for(TableColumnM tcm : tablest.getModelObject().getSchemas()) {
					if(isReferenceColumn(tcm)) {
						tclist.add(tcm);
					}
				}
				
				target.addComponent(tablest);
				target.addComponent(columns);
		
			}
			
		});
		
		Form<Void> form = new Form<Void>("form") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				
				if(check) {
					if(temptable != null && tempcolumn != null)
						foreignconst.setReferenceTable(temptable.getTableName());
					if(tempcolumn != null)
						foreignconst.setReferenceTableColumn(tempcolumn.getName());
					if(tempforeign != null)
						foreignconst.setDeleteType(tempforeign.getName());
				} else {
					foreignconst.init();
				}
			}
			
		};
		
//		constr.setVisible(false);
		tablest.setVisible(this.check);
		columns.setVisible(this.check);
		foreigns.setVisible(this.check);
		
//		constr.setOutputMarkupId(true);
		tablest.setOutputMarkupId(true);
		columns.setOutputMarkupId(true);
		foreigns.setOutputMarkupId(true);
		
//		container.add(constr);
		container.add(tablest);
		container.add(columns);
		container.add(foreigns);
		
		form.add(container);

		form.add(new AjaxCheckBox("check", new PropertyModel<Boolean>(this, "check")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
//				constr.setVisible(ForeignPanel.this.check);
				tablest.setVisible(ForeignPanel.this.check);
				columns.setVisible(ForeignPanel.this.check);
				foreigns.setVisible(ForeignPanel.this.check);

//				target.addComponent(constr);
				target.addComponent(tablest);
				target.addComponent(columns);
				target.addComponent(foreigns);
				target.addComponent(container);
				
			}
			
		});
		
		add(form);

	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
	
	/**
	 * カラムが外部参照先として追加出きるかどうかを判断する
	 * @param tablecolumn 外部参照可か判断するカラム 
	 * @return 外部参照するカラムとして適切ならばtrue,そうでないならばfalse
	 */
	private boolean isReferenceColumn(TableColumnM tablecolumn) {
		return (tablecolumn.getConstraint().isPrimary() || tablecolumn.getConstraint().isUnique())
			&& tablecolumn.getType().equals(columnm.getType()) 
			&& tablecolumn.getLength().equals(columnm.getLength());		
	}

	private static List<ChoiceElement> foreelemnt = Arrays.asList(new ChoiceElement[]{new ChoiceElement("1","NO ACTION"),
			new ChoiceElement("2", "RESTRICT"), new ChoiceElement("3", "SET NULL"),
			new ChoiceElement("4", "SET DEFAULT"), new ChoiceElement("5", "CASCADE")});
	

}
