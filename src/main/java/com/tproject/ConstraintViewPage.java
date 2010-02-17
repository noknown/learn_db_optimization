package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * 制約を表示するためのパネル。外部キー制約は別パネルで管理。
 * @author no_known
 *
 */
public class ConstraintViewPage extends WebPage {
	
	private TableColumnM tablecolumn;
	private String colType;
	private ChoiceElement choiceelement;
//	private IndexTypes index;

	@SuppressWarnings("deprecation")
	public ConstraintViewPage(PageParameters param) {
		// TODO Auto-generated constructor stub
		
		this.tablecolumn = (TableColumnM) param.get("tcm");
		DatabaseStructure dbs = (DatabaseStructure) param.get("dbstruc");
//		index = tcm.getConstraint().getIndex();
		
		for(ChoiceElement c : clist) {
			if(c.getName().equals(tablecolumn.getType())) {
				choiceelement = c;
				colType = c.getName();
			}
		}
		
		Form<Void> form = new Form<Void>("form") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				ConstraintViewPage.this.tablecolumn.setType(colType);
//				ConstraintViewPage.this.tcm.getConstraint().setIndex(index);
			}
		};
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		TextField<String> constname = new TextField<String>("name", new PropertyModel<String>(tablecolumn, "name"));
		constname.setRequired(true);
		TextField<Integer> length = new TextField<Integer>("leng", new PropertyModel<Integer>(tablecolumn, "length"));
		length.setRequired(true);
		final TextField<Integer> plength = new TextField<Integer>("pleng", new PropertyModel<Integer>(tablecolumn, "precisionLength"));
		if(!choiceelement.getName().equals("NUMBER")) plength.setVisible(false);
		
		DropDownChoice<ChoiceElement> datatype = new DropDownChoice<ChoiceElement>("type", 
				new PropertyModel<ChoiceElement>(this, "choiceelement"), Model.of(clist), new ChoiceRenderer<ChoiceElement>("name", "id")) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean wantOnSelectionChangedNotifications() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			protected void onSelectionChanged(ChoiceElement newSelection) {
				// TODO Auto-generated method stub
				super.onSelectionChanged(newSelection);
				
				ConstraintViewPage.this.colType = newSelection.getName();
				if(newSelection.getName().equals("NUMBER"))
					plength.setVisible(true);
				else
					plength.setVisible(false);
				
			}
		};
		datatype.setRequired(true);
		
		CheckBox primary = new CheckBox("primary", new PropertyModel<Boolean>(tablecolumn, "constraint.primary"));
//		if(tcm.getConstraint() != null && tcm.getConstraint().getPrimaryConstName() != null) primary.setd;
		
		CheckBox unique = new CheckBox("unique", new PropertyModel<Boolean>(tablecolumn, "constraint.unique"));
		CheckBox notnull = new CheckBox("notnull", new PropertyModel<Boolean>(tablecolumn, "constraint.notnull"));
		
		CheckTextPanel check = new CheckTextPanel("check", new PropertyModel<String>(tablecolumn, "constraint.check"));
		if(tablecolumn.getConstraint() != null && tablecolumn.getConstraint().getCheck() != null) check.setCheck(true);

		CheckTextPanel defaults = new CheckTextPanel("defaults", new PropertyModel<String>(tablecolumn, "constraint.default"));
		if(tablecolumn.getConstraint() != null && tablecolumn.getConstraint().getDefault() != null) defaults.setCheck(true);

		//インデックス設定
		CheckChoicePanel index = new CheckChoicePanel("index", new PropertyModel<IndexTypes>(tablecolumn, "constraint.index"),
				ilist, new ChoiceRenderer<IndexTypes>("name", "name"));
		if(tablecolumn.getConstraint() != null && tablecolumn.getConstraint().getIndex() != IndexTypes.NONE) index.setCheck(true);
		
		ForeignPanel foreign = new ForeignPanel("foreign", new PropertyModel<TableColumnM>(this, "tablecolumn"), dbs.getTables());

		form.add(constname);
		form.add(datatype);
		form.add(length);
		form.add(plength);
		form.add(primary);
		form.add(unique);
		form.add(notnull);
		form.add(check);
		form.add(defaults);
		form.add(index);
		form.add(foreign);

		form.add(new AjaxSubmitButton("submit", form) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				form.onFormSubmitted();
				ModalWindow.closeCurrent(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				target.addComponent(feedback);
			}
			
		});

		add(form);
	}

	private static List<ChoiceElement> clist = Arrays.asList(new ChoiceElement("1", "VARCHAR2"),
			new ChoiceElement("2", "NVARCHAR2"), new ChoiceElement("3", "CHAR"),
			new ChoiceElement("4", "NCHAR"), new ChoiceElement("5", "LONG"),
			new ChoiceElement("6", "CLOB"), new ChoiceElement("7", "NUMBER"),
			new ChoiceElement("8", "BINARY_FLOAT"), new ChoiceElement("9", "BINARY_DOUBLE"),
			new ChoiceElement("10", "DATE"), new ChoiceElement("11", "TIMESTAMP"),
			new ChoiceElement("12", "TIMESTAMP WITH TIMEZONE"),
			new ChoiceElement("13", "TIMESTAMP WITH LOCAL TIMEZONE"), new ChoiceElement("14", "INTERVAL YEAR TO MONTH"),
			new ChoiceElement("15", "INTERVAL DAY TO SECOND"), new ChoiceElement("16", "RAW"),
			new ChoiceElement("17", "LONG RAW"), new ChoiceElement("18", "BLOB"),
			new ChoiceElement("19", "BFILE"), new ChoiceElement("20", "ROWID")
			);
	
	private static List<IndexTypes> ilist = Arrays.asList(
			IndexTypes.NORMAL, IndexTypes.REVERSE
			);
	

}
