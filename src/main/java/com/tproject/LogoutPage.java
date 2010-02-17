package com.tproject;

import org.apache.wicket.markup.html.WebPage;

public class LogoutPage extends WebPage
{
	public LogoutPage()
	{
		getSession().invalidate();
	}
}
