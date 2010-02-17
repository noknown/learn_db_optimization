package com.tproject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.StringValidator;



public class IdconfirmPage extends WebPage{
	private Username username;
	private AjaxButton idButton;
	private AjaxButton okButton;
	private Label infomessage;
	
	
	public IdconfirmPage(PageParameters param) {
		
		this.username=(Username) param.get("username");
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		final Username targetUser = new Username();
		targetUser.setUsername(username.getUsername());
		
		infomessage = new Label("infoMessage", "このIDは使用可能です");
		infomessage.setVisible(false);
		final Form<Username> idForm = new Form<Username>("idForm",new CompoundPropertyModel<Username>(targetUser));

		final RequiredTextField<String> Username = new RequiredTextField<String>("username");

		Username.add(StringValidator.maximumLength(30));
		
		idForm.add(infomessage);
		idForm.add(Username);
		idButton = new AjaxButton("idButton",idForm){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				System.err.println(Username.getDefaultModelObjectAsString()+"*******************");
				ConnectDao s = ConnectDao.getInstance();
				ResultStruct rs= s.read("select user_name from system.users where user_name = '"+targetUser.getUsername()+"'");
				String value = "";
				for(int i = 0; i < rs.getResult().size(); i++) {
					value=rs.getResult().get(0).getDataList().get(0);
					System.out.println(value);
				}
				if(value.equals("")){
					setOkbutton(true);
					
				}else{
					info("このIDは使用できません。");
					setOkbutton(false);
				}
				target.addComponent(idForm);
				target.addComponent(feedback);
			}
			@Override
			protected void onError(AjaxRequestTarget target,Form<?> form){
				target.addComponent(feedback);
			}
			
		};
		okButton = new AjaxButton("okButton",idForm){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				username.setUsername(targetUser.getUsername());
				username.setUsercheck(true);
				System.out.println(targetUser.getUsername()+"##################3");
				target.addComponent(feedback);
				ModalWindow.closeCurrent(target);
			}
		};
		okButton.setOutputMarkupId(true);
		okButton.setVisible(false);
		idForm.add(idButton);
		idForm.add(okButton);
		add(idForm);

		
	}
	public void setOkbutton(boolean ok){
		okButton.setVisible(ok);
		infomessage.setVisible(ok);
	}


}
