package com.tproject;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;
/**
 * 
 * @author Oh
 *
 */
public class RegisterPage extends WebPage {
	
	private String pass_word;
	private Username username;
	
	public RegisterPage() {
		// TODO Auto-generated constructor stub
		this.username = new Username();
		
		this.setDefaultModel(new CompoundPropertyModel<Username>(username));
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		final RequiredTextField<String> user = new RequiredTextField<String>(	"User_name",
																				new PropertyModel<String>(this,"username.username"));
		user.setOutputMarkupId(true);	
		user.add(StringValidator.maximumLength(30));
		user.add(new OnChangeAjaxBehavior(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = user.getDefaultModelObjectAsString();
				username.setUsername(text);				
			}			
		});
		

		final PasswordTextField Password = new PasswordTextField("Password",
				new PropertyModel<String>(this, "pass_word"));
		final PasswordTextField Comp_Password = new PasswordTextField("Comp_Password", 
				new PropertyModel<String>(this, "pass_word"));
		
		Password.add(StringValidator.lengthBetween(8,16));

		final Form<Void> testForm = new Form<Void>("testForm"){
		
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(){
				if(username.isUsercheck() == true){
					ConfirmationPage page = new ConfirmationPage(username.getUsername(),getPassword());
					setResponsePage(page);
				}else{
					error("IDの確認を行ってください。"); 
				}
			}
			
		};
		final ModalWindow window = new ModalWindow("modal");
		add(window);
		testForm.add(new AjaxLink<Void>("duplication"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override			
			public	void onClick(AjaxRequestTarget target){
				window.setPageCreator(new ModalWindow.PageCreator() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public Page createPage() {
						// TODO Auto-generated method stub
						
						PageParameters param = new PageParameters();
						param.put("username", username);
												
						return new IdconfirmPage(param);
					}
					
				});
				
				window.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
				window.setTitle("ID Confirm");
				window.setResizable(false);
				window.setInitialWidth(400);
				window.setInitialHeight(300);
				window.setWidthUnit("px");
				window.setHeightUnit("px");
				
				window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void onClose(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						target.addComponent(user);
					}
					
				});
				window.show(target);
			}

			
		});
		
		testForm.add(user);
		testForm.add(Password);
		testForm.add(Comp_Password);
		testForm.add(new EqualPasswordInputValidator(Password, Comp_Password));
		add(testForm);

		
	}
		
	public String getPassword() {
		// TODO Auto-generated method stub
		return pass_word;
	}


	public String getComppassword() {
		// TODO Auto-generated method stub
		return pass_word;
	}

	public Username getUsername() {
		return username;
	}

	public void setUsername(Username username) {
		this.username = username;
	}
		
}
