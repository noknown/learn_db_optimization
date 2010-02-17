package com.tproject;

import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.odlabs.wiquery.utils.WiQueryWebApplication;




/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see com.start.Start#main(String[])
 */
public class WicketApplication extends WiQueryWebApplication
{
	private IDao dao;
	
	/**
     * Constructor
     */
	public WicketApplication()
	{
	}

	public WicketApplication(IDao dao) {
		setDao(dao);
	}

    public IDao getDao() {
		return dao;
	}

	public void setDao(IDao dao) {
		this.dao = dao;
	}
	
	public Session newSession(Request request, Response response)
	{
		return new LoginSession(request);
	}

	
	@Override
	protected void init()
	{
		super.init();
		
		//リクエスト・レスポンス時の文字エンコード
		getRequestCycleSettings().setResponseRequestEncoding( "UTF-8");
		//Wicketが読み込むHTMLファイルのエンコード
		getMarkupSettings().setDefaultMarkupEncoding( "UTF-8");


		getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy()
		{
			public boolean isActionAuthorized(Component component, Action action)
			{
				return true;
			}

			public boolean isInstantiationAuthorized(Class componentClass)
			{
				if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
					if (((LoginSession)Session.get()).isLoggedIn()) {
						return true;
					}

					throw new RestartResponseAtInterceptPageException(LoginPage.class);
				}

				return true;
			}
		});
	}
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<LoginPage> getHomePage()
	{
		return LoginPage.class;

	}
}
