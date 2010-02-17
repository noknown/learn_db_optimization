package com.tproject;


import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.PropertyModel;
/**
 * SQL動作確認用ページ
 * @author no_known
 *
 */
public class SQLBehaviorConfirmPage extends WebPage {
	
	public SQLBehaviorConfirmPage() {
		// TODO Auto-generated constructor stub
	
		SQLInputPanel inputpanel = new SQLInputPanel("sqlpanel");
		
		add(new ListView<String>("head", new PropertyModel<List<String>>(inputpanel, "schema.datalist")) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item) {
				// TODO Auto-generated method stub
				item.add(new Label("header", item.getDefaultModelObjectAsString()));
			}
		});
		
		PageableListView<ResultData> view;
		add(view = new PageableListView<ResultData>("row", new PropertyModel<List<ResultData>>(inputpanel, "result"), 10) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ResultData> item) {
				// TODO Auto-generated method stub
				ResultData rd = item.getModelObject();
				
				item.add(new ListView<String>("col", new PropertyModel<List<String>>(rd, "datalist")) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<String> items) {
						// TODO Auto-generated method stub
						items.add(new Label("values", items.getDefaultModelObjectAsString()));
					}
					
				});
			}
			
		});
		
		final PagingNavigator navi = new PagingNavigator("navigator", view);
		add(navi);

		Form<Void> form = new Form<Void>("form");

		form.add(inputpanel);
		
		add(form);

	}
	
}
