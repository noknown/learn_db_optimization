package com.tproject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;

/**
 * 応答時間比較のためのSQL入力フォーム
 * @author no_known
 *
 */
public class SQLResponseMeasurementPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String text = "";

	public SQLResponseMeasurementPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
		
		Form<Void> form = new Form<Void>("form");
		
		TextArea<String> textarea = new TextArea<String>("textarea", new PropertyModel<String>(this, "text"));
		textarea.add(new OnChangeAjaxBehavior() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		textarea.add(new PatternValidator("(?s:\\s*select[\\s].+ from[\\s].+)"));
		
		form.add(textarea);
		
		add(form);
	}

	/**
	 * テキストエリアに入力された文字列を取得する。
	 * @return テキストエリアの文字列
	 */
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}

}
