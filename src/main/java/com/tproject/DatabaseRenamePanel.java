package com.tproject;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;


public class DatabaseRenamePanel extends Panel {
	private DatabaseName databasename;
	private String user ="username";
	private String pass ="username";
	
	public DatabaseRenamePanel(String id, DatabaseName databasename, String user) {
		super(id);
		this.databasename = databasename;
		this.user = user;		
		constructPanel();
	}
	
	void constructPanel() {
		// TODO Auto-generated method stub
		final DatabaseName targetDatabaseName = new DatabaseName(databasename.getDatabaseName(),databasename.getCreateday(),databasename.getUpdateday());
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		final IDao dao = ((WicketApplication)getApplication()).getDao();
		
		Form<DatabaseName> databaseForm = new Form<DatabaseName>("databaseForm",new CompoundPropertyModel<DatabaseName>(targetDatabaseName));
		databaseForm.add(new RequiredTextField<String>("databasename"));
				
		final String dana = databasename.getDatabaseName();
		
		databaseForm.add(new AjaxButton("renameButton",databaseForm){
			
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form){				
				
				ResultStruct r = dao.read("select database_name from system.databases where user_name='"+user+"'");

				boolean same_name = false;
				for(int k=0;k<r.getResult().size();k++){
					System.out.println(r.getResult().get(k).getDataList().get(0));
					if(targetDatabaseName.getDatabaseName().equals(r.getResult().get(k).getDataList().get(0))){
						same_name=true;
					}
				}
				if(same_name==false){						
					databasename.setDatabaseName(targetDatabaseName.getDatabaseName());
					dao.insert("update system.databases set database_name = '"+databasename.getDatabaseName()+"' where database_name = '"+dana+"' and user_name = '"+user+"'");
					System.err.println("---------------------");
					System.err.println("update system.databases set database_name = '"+databasename.getDatabaseName()+"' where database_name = '"+dana+"' and user_name = '"+user+"'");
					dao.insert("commit");
					target.addComponent(feedback);
					ModalWindow.closeCurrent(target);
				}else{
					error("同じデータベース名があります。");
				}
							
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form){
				target.addComponent(feedback);
			}			
		});
		
		add(databaseForm);
	}

}
