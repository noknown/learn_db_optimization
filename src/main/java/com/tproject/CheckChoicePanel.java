package com.tproject;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class CheckChoicePanel extends Panel {

	private static final long serialVersionUID = 1L;
	private boolean check;
//	private IndexTypes types = IndexTypes.NONE;
	
	private DropDownChoice<IndexTypes> ddc;

	public CheckChoicePanel(String id, final PropertyModel<IndexTypes> model, List<IndexTypes> alist, ChoiceRenderer<IndexTypes> render) {
		super(id, model);
		// TODO Auto-generated constructor stub
		
//		if(model.getObject() != null)
//			types = model.getObject();
		
		Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				if(!check) model.setObject(IndexTypes.NONE);
//				System.err.println(model.getObject());
			}
		};
		
		ddc = new DropDownChoice<IndexTypes>("choice", model,
				alist, render);
		
		final WebMarkupContainer cont = new WebMarkupContainer("cont");
		ddc.setVisible(check);
		cont.add(ddc);
		cont.setOutputMarkupId(true);
		
		AjaxCheckBox cb = new AjaxCheckBox("check",
				new PropertyModel<Boolean>(this, "check")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
				ddc.setVisible(CheckChoicePanel.this.check);
				
				target.addComponent(cont);
			}
			
		};
		
		form.add(cb);
		form.add(cont);
		
		add(form);
	}
	
	public boolean isCheck() {
		return check;
	}
	
	public void setCheck(boolean check) {
		this.check = check;
	}
}
