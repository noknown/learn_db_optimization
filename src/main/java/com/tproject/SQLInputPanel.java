package com.tproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;
/**
 * 
 * @author no_known
 *
 */
public class SQLInputPanel extends Panel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inputtext = "";
	
	private ResultStruct resultstore;
	private List<ResultData> result = Arrays.asList(new ResultData[]{
			new ResultData(new String[]{""})
	});
	private ResultData schema = new ResultData(new String[]{""});
	
//	private InputForm form;
	public SQLInputPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
		
		InputForm form = new InputForm("form");
		
		final TextArea<String> sqls = new TextArea<String>("textarea", new PropertyModel<String>(this, "inputtext"));
		sqls.setOutputMarkupId(true);
		
		sqls.add(new PatternValidator("(?s:\\s*select[\\s].+ from[\\s].+)"));
		
		
		form.add(sqls);
		add(form);
		
	}
	
	/**
	 * 実行されたSQLの結果を返す。サブミットされていない状態で呼び出されると
	 * 要素数１で１つの空文字列が入ったリストを返す。
	 * @return 実行されたSQL結果のResultSetのリスト
	 */
	public List<ResultData> getResult() {
		// TODO Auto-generated method stub
		if(resultstore == null) return Arrays.asList(new ResultData[]{
									new ResultData(new String[]{""})
								});
		return result;
	}

	/**
	 * 実行されたSQLの結果の要素名を返す。サブミットされていない状態で呼び出されると
	 * １つの空文字列が入ったリストを返す。
	 * @return 実行されたSQL結果の要素名が入ったResultSet
	 */
	public ResultData getSchema() {
		// TODO Auto-generated method stub
		if(resultstore == null) return new ResultData(new String[]{""});
		return schema;
	}
	
	/**
	 * 実行されたSQL文を返す。サブミットされていない状態で呼び出されると
	 * 空文字列を返す。
	 * @return 実行されたSQL文
	 */
	public String getInputText() {
		return inputtext;
	}
	
	private class InputForm extends Form<Void> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InputForm(String id) {
			super(id);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onSubmit() {
			// TODO Auto-generated method stub
			
			IDao idao = ((WicketApplication)getApplication()).getDao();
			if(idao == null) idao = ConnectDao.getInstance();

			resultstore = idao.read(getInputText());
			
			if(resultstore != null) {
				result = resultstore.getResult();
				schema = resultstore.getSchema();
			}
		}
		
	}

//	public void submit() {
//		// TODO Auto-generated method stub
//		form.onSubmit();
//	}
}
