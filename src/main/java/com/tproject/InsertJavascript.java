package com.tproject;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * javascriptによる描画を行うためのクラス
 * @author no_known
 *
 */
public class InsertJavascript extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsertJavascript(String id) {
		// TODO Auto-generated constructor stub
		super(id);

	}

	@Override
	protected void onRender(MarkupStream markupstream) {
		// TODO Auto-generated method stub
		renderComponent(markupstream);
	}
	
}
