package com.tproject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class DataSquencePanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected RequiredTextField<Integer> startText;
	protected RequiredTextField<Integer> stepText;
	protected int start=1;
	protected int step=1;
	protected Label startLabel;
	protected Label stepLabel;
	
	public DataSquencePanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
		Form<Void> squenceForm = new Form<Void>("squenceForm");
		add(squenceForm);
		
		startLabel = new Label("startLabel","START : ");
		startLabel.setOutputMarkupId(true);
		squenceForm.add(startLabel);
		
		startText = new RequiredTextField<Integer>("startText",new PropertyModel<Integer>(this,"start"),Integer.class);
		startText.setOutputMarkupId(true);
		startText.add(new OnChangeAjaxBehavior(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				int start = Integer.parseInt(startText.getDefaultModelObjectAsString());
				setStart(start);
				
				target.addComponent(startText);				
			}
			
		});
		squenceForm.add(startText);
		
		stepLabel = new Label("stepLabel","STEP : ");
		stepLabel.setOutputMarkupId(true);
		squenceForm.add(stepLabel);
		
		stepText = new RequiredTextField<Integer>("stepText",new PropertyModel<Integer>(this,"step"),Integer.class);
		stepText.setOutputMarkupId(false);
		stepText.add(new OnChangeAjaxBehavior(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				int step = Integer.parseInt(startText.getDefaultModelObjectAsString());
				setStart(step);
				
				target.addComponent(stepText);				
			}
			
		});
		squenceForm.add(stepText);
	}
	public void setSequenceVisible(){
		startLabel.setVisible(true);
		startText.setVisible(true);
		stepLabel.setVisible(true);
		stepText.setVisible(true);
	}
	public void setSequenceUnvisible(){
		startLabel.setVisible(false);
		startText.setVisible(false);
		stepLabel.setVisible(false);
		stepText.setVisible(false);
	}
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
}
