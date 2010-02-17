package com.tproject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * SQL応答時間確認ページ
 * @author no_known
 *
 */
public class ResponseMeasurementPage extends WebPage {
	
	ChoiceElement element;
	List<SQLResponseMeasurementPanel> sqllist;
//	List<ChoiceElement> choices;
//	List<AnalyticsInformation> analysislist;
	
	Command command;
	
	public ResponseMeasurementPage(PageParameters param, Command icommand) {
		// TODO Auto-generated constructor stub

		String[] databases = (String[]) param.get("databases");
		
		final String user = ((LoginSession)this.getSession()).getUserName();
		final String pass = ((LoginSession)this.getSession()).getPassword();
		
		sqllist = new ArrayList<SQLResponseMeasurementPanel>();
		final List<ChoiceElement> choices = new ArrayList<ChoiceElement>();
		final List<AnalyticsInformation> analysislist = new ArrayList<AnalyticsInformation>();
		this.command = icommand;
		
//		PageParameters par = new PageParameters();
//		par.put("analysis", analysislist);
		final AnalysisPanel ap = new AnalysisPanel("response", analysislist);
//		ap.setVisible(false);
		ap.setOutputMarkupId(true);
		
		for(int i = 0; i < databases.length; i++) {
			choices.add(new ChoiceElement(String.valueOf(i), databases[i]));
			SQLResponseMeasurementPanel panel = new SQLResponseMeasurementPanel("sqlinput");
			panel.setOutputMarkupId(true);
			sqllist.add(panel);
		}
		
		add(new BookmarkablePageLink<Void>("toppage", TopPage.class, param));
		add(new BookmarkablePageLink<Void>("behavior", SQLBehaviorConfirmPage.class));
		
		final Form<Void> form = new Form<Void>("form") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				Queue<String> commands = new LinkedBlockingQueue<String>();

				analysislist.clear();
//				analysislist = new ArrayList<AnalyticsInformation>();
				
				for(SQLResponseMeasurementPanel sip : sqllist) {
					
					commands.add(sip.getText());
					
					sip.setVisible(false);
				}
				sqllist.get(0).setVisible(true);
				
				//
				for(String s : commands) {
					AnalyticsInformation ai;
					
					ai = command.execCommand(user, pass, s);
					if(ai == null) continue;
					
					analysislist.add(ai);
					
				}
        		//
				ap.setAnalysisList(analysislist);
			}
			
			@Override
			protected void onBeforeRender() {
				// TODO Auto-generated method stub
				super.onBeforeRender();
				for(SQLResponseMeasurementPanel sip : sqllist) sip.setVisible(false);
				sqllist.get(0).setVisible(true);
			}

		};
		
		
		final WebMarkupContainer container = new WebMarkupContainer("cont");
		container.setOutputMarkupId(true);
		
		container.add(new ListView<ChoiceElement>("tabs", choices) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ChoiceElement> item) {
				// TODO Auto-generated method stub
				
				ChoiceElement ce = item.getModelObject();
				
				item.add(new LabelInAjaxLink<ChoiceElement>("link", Model.of(ce)) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						
						ChoiceElement ces = this.getModelObject();
						if(element == ces) return;
						
						for(SQLResponseMeasurementPanel sip : sqllist) sip.setVisible(false);
						sqllist.get(Integer.parseInt(ces.getId())).setVisible(true);
						
						element = ces;
						
						target.addComponent(container);
					}
					
				});
			}
			
		});
		
		container.add(new ListView<SQLResponseMeasurementPanel>("text", sqllist) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SQLResponseMeasurementPanel> item) {
				// TODO Auto-generated method stub
				item.add((SQLResponseMeasurementPanel) item.getDefaultModelObject());
			}
			
		});
		
		form.add(container);
		
		// このボタンがないとテキストがきちんと格納されない。
		form.add(new IndicatingAjaxButton("button", form) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

				target.addComponent(container);
				ap.setVisible(true);
				target.addComponent(ap);

			}

		});
		add(form);
		add(ap);
		
	}

	public ResponseMeasurementPage(PageParameters param) {
		this(param, new ExternalCommand());
	}
	
	/**
	 * リンクとラベルを関連づけるためのクラス
	 * @author 池本　孝則
	 *
	 */
	@SuppressWarnings("hiding")
	public abstract class LabelInAjaxLink<ChoiceElement> extends AjaxLink<ChoiceElement>{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public LabelInAjaxLink(String id, IModel<ChoiceElement> model) {
			// TODO Auto-generated constructor stub
			super(id, model);
			
			add(new Label("label", model.getObject().toString()));
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getInputList() {
		List<String> list = new ArrayList<String>();
		
		for(SQLResponseMeasurementPanel sip : sqllist) {
			list.add(sip.getText());
		}
		
		return list;
	}
	
}
