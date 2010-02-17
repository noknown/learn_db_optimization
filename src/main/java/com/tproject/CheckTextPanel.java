package com.tproject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
/**
 * チェックボックスボタンにチェックするとテキストフィールドが現れる
 * パネルコンポーネントの作成。
 * 
 * @author no_known
 *
 */
public class CheckTextPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean check;
	
	private TextField<String> tf;

	public CheckTextPanel(String id,final PropertyModel<String> propertyModel) {
		super(id, propertyModel);
		// TODO Auto-generated constructor stub
		
		Form<Void> form = new Form<Void>("form") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5077516954838414918L;

			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				if(!check) propertyModel.setObject(null);
			}
		};
		
		tf = new TextField<String>("text",
				propertyModel);
		final WebMarkupContainer cont = new WebMarkupContainer("cont");
		tf.setVisible(check);
		cont.add(tf);
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
				
				tf.setVisible(CheckTextPanel.this.check);
				
				target.addComponent(cont);
			}
			
		};
		
		form.add(cb);
		form.add(cont);
		
		add(form);
	}

	/**
	 * チェックボックスの状態を取得する。チェックがついている状態
	 * ならtrue、ついていないならfalse。
	 * この値はサブミットされるごとに初期値のfalseになる。
	 * @return チェックボックスの状態
	 */
	public boolean isCheck() {
		return check;		
	}

	/**
	 * チェックボックスの状態を入力する。
	 * @param check 入力するチェックボックスの状態
	 */
	public void setCheck(boolean check) {
		this.check = check;

	}


}
