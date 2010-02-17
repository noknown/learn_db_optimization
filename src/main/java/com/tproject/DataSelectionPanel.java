package com.tproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.apache.wicket.validation.validator.RangeValidator;
/**
 * 
 * @author Oh
 *
 */
@SuppressWarnings("serial")
public class DataSelectionPanel extends Panel{
	
	protected int index;
	protected int datalength;
	protected DropDownChoice<DataTypeChar> choice1;
	protected DropDownChoice<DataTypeNumber> choice2;
	protected List<DataFormatSelectionPanel> dataformatList = new ArrayList<DataFormatSelectionPanel>();
	protected List<DataSquencePanel> datasquenceList = new ArrayList<DataSquencePanel>();
	protected DataFormatSelectionPanel dataformatselection;
	protected Label choice;
	protected Label dataL;
	protected String type;
	protected String id;
	protected DataTypeChar dataC;
	protected DataTypeNumber dataN;
	protected TableColumnM tablename;
	protected boolean set;
	protected RequiredTextField<Integer> startYear;
	protected RequiredTextField<Integer> startMonth;
	protected RequiredTextField<Integer> startDay;
	protected RequiredTextField<Integer> endYear;
	protected RequiredTextField<Integer> endMonth;
	protected RequiredTextField<Integer> endDay;
	protected Label startyear;
	protected Label startmonth;
	protected Label srash;
	protected Label stopyear;
	protected Label stopmonth;
		
	public DataSelectionPanel(String id,int index,PropertyModel<TableColumnM> tablename) {
		super(id);
		this.tablename = (TableColumnM)tablename.getObject();
		constructPage();
	}

	protected void constructPage() {
		
		this.datalength = tablename.getLength();
		final Form<Void> dataForm = new Form<Void>("dataForm");

		add(dataForm);

		choice1 = new DropDownChoice<DataTypeChar>(
				"dataCchoice",
				new PropertyModel<DataTypeChar>(this, "dataC"),
				Arrays.asList(DataTypeChar.values()),
				new ChoiceRenderer<DataTypeChar>("type","id"));
		dataForm.add(choice1);
		choice1.setVisible(false);
		choice1.add(new AjaxFormComponentUpdatingBehavior("onchange"){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				DataTypeChar datatypechar = choice1.getModelObject();
				type = datatypechar.getType(); 
				id = datatypechar.getId();
				
				if(type.equals("Free")){
					dataformatList.add(dataformatselection = new DataFormatSelectionPanel("dataformatPanel",String.valueOf(getTablename().getLength())));
					dataformatselection.setOutputMarkupId(true);
				}else{
					dataformatList.clear();
				}
				setSet(true);				
				target.addComponent(dataForm);
			}			
		});
		choice2 = new DropDownChoice<DataTypeNumber>(
				"dataNchoice",
				new PropertyModel<DataTypeNumber>(this, "dataN"),
				Arrays.asList(DataTypeNumber.values()),
				new ChoiceRenderer<DataTypeNumber>("type","id"));
		dataForm.add(choice2);
		choice2.setVisible(false);		
		choice2.add(new AjaxFormComponentUpdatingBehavior("onchange"){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				DataTypeNumber datatypenumber=choice2.getModelObject();
				type = datatypenumber.getType(); 
				id	 = datatypenumber.getId();
				if(type.equals("Free")){
					dataformatList.add(dataformatselection = new DataFormatSelectionPanel("dataformatPanel",String.valueOf(getTablename().getLength())));
					dataformatselection.setOutputMarkupId(true);
					dataformatselection.setNumnervisiable();					
				}else if(type.equals("Sequence")){
					dataformatList.clear();
					datasquenceList.add(new DataSquencePanel("datasquencePanel"));					
				}else{
					dataformatList.clear();	
					datasquenceList.clear();
				}
				setSet(true);	
				target.addComponent(dataForm);

			}			
		});
		choice = new Label("choice",new PropertyModel<String>(this,"type")); 
		choice.setOutputMarkupId(true);
		choice.setVisible(false);
		dataForm.add(choice);
		
		dataL = new Label("dataLength",new PropertyModel<String>(this,"datalength"));
		dataForm.add(dataL);
		
		dataForm.add(new ListView<DataFormatSelectionPanel>("dataformatList",dataformatList){

			@Override
			protected void populateItem(ListItem<DataFormatSelectionPanel> item) {
				// TODO Auto-generated method stub
				DataFormatSelectionPanel panel = item.getModelObject();
				item.add(panel);
			}
			
		});
		dataForm.add(new ListView<DataSquencePanel>("datasquenceList",datasquenceList){

			@Override
			protected void populateItem(ListItem<DataSquencePanel> item) {
				// TODO Auto-generated method stub
				DataSquencePanel panel = item.getModelObject();
				item.add(panel);
			}
			
		});
		
		startYear = new RequiredTextField<Integer>("startYear",new Model<Integer>(),Integer.class);
		startYear.setVisible(false);
		startYear.setOutputMarkupId(true);
		startYear.add(new MinimumValidator<Integer>(1900));
		startYear.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				int startyear = startYear.getModelObject();
				startYear.setDefaultModelObject(startyear);
			}
			
		});
		dataForm.add(startYear);
		
		
		startMonth = new RequiredTextField<Integer>("startMonth",new Model<Integer>(),Integer.class);
		startMonth.setVisible(false);
		startMonth.setOutputMarkupId(true);
		startMonth.add(new RangeValidator<Integer>(1,12));
		startMonth.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				int startmonth = startMonth.getModelObject();
				startMonth.setDefaultModelObject(startmonth);
			}
			
		});
		dataForm.add(startMonth);
		
		startDay = new RequiredTextField<Integer>("startDay",new Model<Integer>(),Integer.class);
		startDay.setVisible(false);
		startDay.setOutputMarkupId(true);
		startDay.add(new RangeValidator<Integer>(1,30));
		startDay.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				int startday = startDay.getModelObject();
				startDay.setDefaultModelObject(startday);
			}
			
		});
		dataForm.add(startDay);
		
		endYear = new RequiredTextField<Integer>("stopYear",new Model<Integer>(),Integer.class);
		endYear.setVisible(false);
		endYear.setOutputMarkupId(true);
		endYear.add(new MinimumValidator<Integer>(1900));
		endYear.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				int endyear = endYear.getModelObject();
				endYear.setDefaultModelObject(endyear);
			}
			
		});
		dataForm.add(endYear);
		
		endMonth = new RequiredTextField<Integer>("stopMonth",new Model<Integer>(),Integer.class);
		endMonth.setVisible(false);
		endMonth.setOutputMarkupId(true);
		endMonth.add(new RangeValidator<Integer>(1,12));
		endMonth.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				int endmonth = endMonth.getModelObject();
				endMonth.setDefaultModelObject(endmonth);
			}
			
		});
		dataForm.add(endMonth);
		
		endDay = new RequiredTextField<Integer>("stopDay",new Model<Integer>(),Integer.class);
		endDay.setVisible(false);
		endDay.setOutputMarkupId(true);
		endDay.add(new RangeValidator<Integer>(1,30));
		endDay.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				int endday = endDay.getModelObject();
				endDay.setDefaultModelObject(endday);
			}
			
		});
		dataForm.add(endDay);
		
		startyear = new Label("startyear","/");
		startyear.setVisible(false);
		startyear.setOutputMarkupId(true);
		dataForm.add(startyear);
		
		startmonth = new Label("startmonth","/");
		startmonth.setVisible(false);
		startmonth.setOutputMarkupId(true);
		dataForm.add(startmonth);
		
		srash = new Label("srash","～");
		srash.setVisible(false);
		srash.setOutputMarkupId(true);
		dataForm.add(srash);
		
		stopyear = new Label("stopyear","/");
		stopyear.setVisible(false);
		stopyear.setOutputMarkupId(true);
		dataForm.add(stopyear);
		
		stopmonth = new Label("stopmonth","/");
		stopmonth.setVisible(false);
		stopmonth.setOutputMarkupId(true);
		dataForm.add(stopmonth);
		
	}
	public DataTypeChar getDataC() {
		return dataC;
	}

	public void setDataC(DataTypeChar dataC) {
		this.dataC = dataC;
	}

	public DataTypeNumber getDataN() {
		return dataN;
	}

	public void setDataN(DataTypeNumber dataN) {
		this.dataN = dataN;
	}

	
	public void setVisi(String str){
		this.setType("");
		if("NUMBER".equals(str)){
			choice1.setVisible(false);
			choice2.setVisible(true);
		}else if("DATE".equals(str) ){
			startYear.setVisible(true);
			startMonth.setVisible(true);
			startDay.setVisible(true);
			endYear.setVisible(true);
			endMonth.setVisible(true);
			endDay.setVisible(true);
			startyear.setVisible(true);
			startmonth.setVisible(true);
			srash.setVisible(true);
			stopyear.setVisible(true);
			stopmonth.setVisible(true);
			setType("DATE");
		}else{
			choice1.setVisible(true);
			choice2.setVisible(false);
		}		
		choice.setVisible(true);	
		
	}
	public void setInit(){
		this.choice1.setVisible(false);
		this.choice2.setVisible(false);
		this.choice.setVisible(false);
		
	}	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public int getDatalength() {
		return datalength;
	}

	public void setDatalength(int datalength) {
		this.datalength = datalength;
	}
	public void setVisibleDataformat(){
		this.dataformatselection.setVisible(true);
	}

	public TableColumnM getTablename() {
		return tablename;
	}

	public void setTablename(TableColumnM tablename) {
		this.tablename = tablename;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

}
