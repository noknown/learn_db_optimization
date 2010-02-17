package com.tproject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
/**
 * 
 * @author Oh
 *
 */


public class LoginPage extends WebPage{
	
	public LoginPage(){
		this(null);
	}
	public LoginPage(final PageParameters parameters)
	{
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
        add(feedback);
		add(new LoginForm("loginForm"));
		
	}

	@SuppressWarnings("unchecked")
	class LoginForm extends Form{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final ValueMap properties = new ValueMap();

		public LoginForm(final String id)
		{
			super(id);
			add(new TextField<String>("userName", new PropertyModel<String>(properties, "userName")));
			add(new PasswordTextField("password", new PropertyModel<String>(properties, "password")));
		}

		@Override
		public void onSubmit()
		{
			
			LoginSession session=(LoginSession) getSession();
			
			if (session.authenticate(properties.getString("userName"),properties.getString("password"))) {

				if (! continueToOriginalDestination()) {
					PageParameters param = new PageParameters();
					param.put("user", session.getUserName());
					param.put("pass", session.getPassword());
					IDao con = UserDao.newInstance(session.getUserName(), session.getPassword());
					((WicketApplication)getApplication()).setDao(con);
					setResponsePage(TopPage.class,param);
				}
			}
			else {
				error("ユーザーネームかパスワードが間違っています。");
			}
		}
	}
}
