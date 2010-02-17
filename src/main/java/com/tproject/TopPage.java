package com.tproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
/**
 * 
 * @author Oh
 *
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class TopPage extends WebPage{
	// 保持しているデータリスト。     
	private List<DatabaseName> datas = new ArrayList<DatabaseName>();
	private List<DatabaseName> selection;
	private DatabaseName databaseN;
	private String inputtext;
	
	private String user;
	private String pass;
	
	// コンストラクタ
	@SuppressWarnings("serial")
	public TopPage(final PageParameters param){
		
		this.user = param.getString("user");
		this.pass = param.getString("pass");
		
		final IDao con = ((WicketApplication)getApplication()).getDao();
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		final ModalWindow window = new ModalWindow("modal");		
		add(window);
		add(new AdminLabel("admin",user +" さん＾＾"));
		feedback.setOutputMarkupId(true);
		add(feedback);
	
		//ログアウトの処理
		add(new Link("logout"){
			@Override
			public void onClick(){
				setResponsePage(LogoutPage.class);
			}
		});
		Form<Void> form = new Form<Void>("myForm");  
		form.setOutputMarkupId(true); 
		//データベースサーバからデータを読み込む
		final ResultStruct rs= con.read("select database_name,to_char(createday,'yyyy mm/dd'),to_char(updateday,'yyyy mm/dd') from system.databases where user_name = '"+ user +"' order by updateday");
		if(rs != null){
			//読み込んだデータをリストに入力				
			for (int i = 0; i < rs.getResult().size(); i++) {     
				DatabaseName data = new DatabaseName(	rs.getResult().get(i).getDataList().get(0),
														rs.getResult().get(i).getDataList().get(1),
														rs.getResult().get(i).getDataList().get(2));     

				datas.add(data);  			
			}      
		}
		//チェックボクスのグループ化する
		final CheckGroup<DatabaseName> datacheck = new CheckGroup<DatabaseName>("datacheck",new ArrayList<DatabaseName>());
		add(datacheck);
		//コンテナ作成
		final WebMarkupContainer viewContainer = new WebMarkupContainer("viewContainer");  
		viewContainer.setOutputMarkupId(true);
		
		//データの表示
		final PageableListView<DatabaseName> listview = new PageableListView<DatabaseName>("list", datas, 10) { 
			protected void populateItem(final ListItem<DatabaseName> item) {   
				final DatabaseName data = item.getModelObject();
				item.setOutputMarkupId(true);
				item.setDefaultModel(new CompoundPropertyModel<DatabaseName>(data));
				item.add(new Label("index", String.valueOf(item.getIndex() + 1)));  
				item.add(new Label("databaseName", new PropertyModel<String>(data, "databasename")));  
				item.add(new Label("createDate", new PropertyModel<String>(data, "createday")));
				item.add(new Label("updateDate", new PropertyModel<String>(data, "updateday")));   
				item.add(new Check("check", item.getDefaultModel()));
		
				//データベースネームの変更
				item.add(new AjaxLink<Void>("rename"){
					/**
					 * データベースネームの変更
					 */
					public void onClick(AjaxRequestTarget target){
						window.setContent(new DatabaseRenamePanel(window.getContentId(),data,user));
						window.setCssClassName(ModalWindow.CSS_CLASS_BLUE);
						window.setTitle("Change the Name");
						window.setResizable(false);
						window.setInitialWidth(400);
						window.setWidthUnit("px");
						window.setUseInitialHeight(false);
						window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
							public void onClose(AjaxRequestTarget target) {								
								target.addComponent(item);							
							}
						});
						window.show(target);
					}					
				});
				/**
				 * データベースネームの削除
				 */
				item.add(new AjaxLink("delete") {  
															
					final ResultStruct del = con.read("select database_id from system.databases where database_name ='"+data.getDatabaseName()+"' and user_name = '"+user+"'");
				
					final ResultStruct del1 = con.read("select table_name from system.tables where database_id ='"+del.getResult().get(0).getDataList().get(0)+"'");

					public void onClick(AjaxRequestTarget target) {  
						/**
						 * 選択したデータベースネームに属しているテーブルの削除
						 */
						DatabaseStructure ds = DatabaseTransform.toDatabaseStructure(con, user, data.getDatabaseName());
						
						List<TableStructure> list = TableDesignerPage.getSortedTables(ds);
						if(!del1.isEmpty()){
							for(int k = list.size()-1;k>=0;k--){
								con.insert("drop table "+list.get(k).getTableName());									
							}
						}
						/**
						 * データベースネームの削除
						 */
						if(!del.isEmpty()){
							con.insert("delete from system.databases where database_name='"+data.getDatabaseName()+"'");
							con.insert("commit");							
						}
						datas.remove(data);  
						target.addComponent(viewContainer);
					}            
				});  
				/**
				 * データベースのコピー
				 */
				item.add(new AjaxLink("copy"){
					
						public void onClick(AjaxRequestTarget target){
							//データベースネームをコピー処理
							ResultStruct rcopy = con.read("select database_name from system.databases where user_name='"+user+"'");

							int count=1;
							String str = data.getDatabaseName()+"_"+String.valueOf(count);
							if(!rcopy.isEmpty()){
								for(int k=0;k<rcopy.getResult().size();k++){
									if(str.equals(rcopy.getResult().get(k).getDataList().get(0))){
										count++;
										str = data.getDatabaseName()+"_"+String.valueOf(count);
										k=0;
									}
								}
							}
							con.insert("insert into system.databases values(system.db_seq1.nextval,'"+data.getDatabaseName()+"_"+String.valueOf(count)+"','"+user+"',sysdate,sysdate)");														
													
							DatabaseName da = new DatabaseName(data.getDatabaseName()+"_"+String.valueOf(count),data.getCreateday(),data.getUpdateday()); 
							datas.add(da);						
							target.addComponent(viewContainer);
							//テーブルのコピー処理
							count=0;
							//コピーしたデータベースネーム
							final ResultStruct copy = con.read("select database_id from system.databases where database_name ='"+da.getDatabaseName()+"' and user_name = '"+user+"'");
							//コピー対象のデータベースネーム
							final ResultStruct copy1 = con.read("select database_id from system.databases where database_name ='"+data.getDatabaseName()+"' and user_name = '"+user+"'");
							//データベースネームに属しているテーブルネームを取り出す
							final ResultStruct copy2 = con.read("select table_name from system.tables where database_id ='"+copy1.getResult().get(0).getDataList().get(0)+"'");
							
							DatabaseStructure ds = DatabaseTransform.toDatabaseStructure(con, user, data.getDatabaseName());
																				
							List<TableStructure> list = TableDesignerPage.getSortedTables(ds);
							int cnt;
							if(!list.isEmpty()){
								ResultStruct tablename = con.read("select table_name from system.tables natural join system.databases where user_name='"+user+"'");
								
								HashMap<String, List<TableColumnM>> reference = new HashMap<String, List<TableColumnM>>();
								List<TableColumnM> primary = new ArrayList<TableColumnM>();
								for(int k=0;k<list.size();k++){
									cnt=1;
									String stn = list.get(k).getTableName()+"_"+String.valueOf(cnt);
									
									for(int j=0;j<tablename.getResult().size();j++){
										if(!tablename.isEmpty()){
											if(stn.equals(tablename.getResult().get(j).getDataList().get(0))){
												cnt++;
												stn = list.get(j).getTableName()+"_"+String.valueOf(cnt);
												j=0;
											}
										}
									}									
									stn = list.get(k).getTableName()+"_"+String.valueOf(cnt);
									con.insert("create table "+stn+" as select * from "+list.get(k).getTableName());
									con.insert("insert into system.tables values(system.table_seq1.nextval,'"+stn+"','"+copy.getResult().get(0).getDataList().get(0)+"')");	
									
									for(int j=0;j<list.get(k).getSchemas().size();j++){
										list.get(k).getSchemas().get(j).setTableName(stn);
									}
									
								}
								for(int i=0;i<list.size();i++){
									for(int j=0;j<list.size();j++){
										for(int k=0;k<list.get(j).getSchemas().size();k++){
											if(list.get(j).getSchemas().get(k).getConstraint().getForeign().getReferenceTable() != null ){
												if(list.get(i).getTableName().toLowerCase().equals(list.get(j).getSchemas().get(k).getConstraint().getForeign().getReferenceTable().toLowerCase())){
													list.get(j).getSchemas().get(k).getConstraint().getForeign().setReferenceTable(list.get(i).getSchemas().get(0).getTableName());
												}
											}
										}
									}
								}
								for(int k=0;k<list.size();k++){
									primary.clear();
									reference.clear();
									for(int i=0;i < list.get(k).getSchemas().size();i++){
										if(list.get(k).getSchemas().get(i).getConstraint().isPrimary()){
											primary.add(list.get(k).getSchemas().get(i));											
										}
										if(list.get(k).getSchemas().get(i).getConstraint().getForeign().getReferenceTable()!= null){
											if(!reference.containsKey(list.get(k).getSchemas().get(i).getConstraint().getForeign().getReferenceTable())){
												reference.put(list.get(k).getSchemas().get(i).getConstraint().getForeign().getReferenceTable(), new ArrayList<TableColumnM>());
											}
											reference.get(list.get(k).getSchemas().get(i).getConstraint().getForeign().getReferenceTable()).add(list.get(k).getSchemas().get(i));
										}
										
										if(list.get(k).getSchemas().get(i).getConstraint().isUnique()){ 
											System.out.println("alter table "+list.get(k).getSchemas().get(i).getTableName()+" add unique ("+list.get(k).getSchemas().get(i).getName()+")");
											con.insert("alter table "+list.get(k).getSchemas().get(i).getTableName()+" add unique ("+list.get(k).getSchemas().get(i).getName()+")");
										}
										if(list.get(k).getSchemas().get(i).getConstraint().isNotnull()){ 
											System.out.println("alter table "+list.get(k).getSchemas().get(i).getTableName()+" modify "+list.get(k).getSchemas().get(i).getName()+" not null");
											con.insert("alter table "+list.get(k).getSchemas().get(i).getTableName()+" modify "+list.get(k).getSchemas().get(i).getName()+" not null");
										}
									}
									if(primary.size()>0){
										if(primary.size() == 1){
											System.err.println("alter table "+primary.get(0).getTableName()+" add primary key("+primary.get(0).getName()+")");

											con.insert("alter table "+primary.get(0).getTableName()+" add primary key("+primary.get(0).getName()+")");
										}else{
											String t = "";
											for(int j=0;j<(primary.size()-1);j++){
												t += primary.get(j).getName()+",";
											}
											t += primary.get(primary.size()-1).getName();
											System.err.println("alter table "+primary.get(0).getTableName()+" add primary key("+t+")");

											con.insert("alter table "+primary.get(0).getTableName()+" add primary key("+t+")");
										}

									}
									if(reference.size()>0){
										Set<String> keysets = reference.keySet();
										for(String key : keysets){
											List<TableColumnM> c = reference.get(key);
											if(c.size()>0){
												if(c.size() == 1){
													System.err.println("alter table "+c.get(0).getTableName()+" add foreign key ("+c.get(0).getName()+") references "+c.get(0).getConstraint().getForeign().getReferenceTable()+" ("+c.get(0).getConstraint().getForeign().getReferenceTableColumn()+")");

													con.insert("alter table "+c.get(0).getTableName()+" add foreign key ("+c.get(0).getName()+") references "+c.get(0).getConstraint().getForeign().getReferenceTable()+" ("+c.get(0).getConstraint().getForeign().getReferenceTableColumn()+")");
												}else{
													String t = "";
													String r = "";
													for(int j=0;j<(c.size()-1);j++){
														t += c.get(j).getName()+",";
														r += c.get(j).getConstraint().getForeign().getReferenceTableColumn().toLowerCase()+",";
													}
													t += c.get(c.size()-1).getName();
													r += c.get(c.size()-1).getConstraint().getForeign().getReferenceTableColumn();
													System.err.println("alter table "+c.get(0).getConstraint().getForeign().getReferenceTable()+" add foreign key ("+r+") references "+c.get(0).getTableName()+" ("+t+")");

													con.insert("alter table "+c.get(0).getTableName()+" add foreign key ("+t+") references "+c.get(0).getConstraint().getForeign().getReferenceTable()+" ("+r+")");
												}

											}
										}
									}
								}								
							}
							con.insert("commit");
						}
					
				});
				item.add(new Link<Void>("edit"){
					/**
					 * データベースの編集ぺーじへ遷移
					 */
					@Override
					public	void onClick(){
						param.put("user", user);
						param.put("database", data.getDatabaseName());
						//
						setResponsePage(TableDesignerPage.class, param);		
					}
				});
				item.add(new Link<Void>("createData"){
					/**
					 * データ自動生成インタフェースへの遷移
					 */
					@Override
					public void onClick(){
						PageParameters page = new PageParameters();
						page.put("database", data.getDatabaseName());
						page.put("user", getUser());
						setResponsePage(DataAutoCreatePage.class, page);
					}
				});
			}		
		

		};
		datacheck.add(listview);
		viewContainer.add(datacheck);
		viewContainer.add(new AjaxPagingNavigator("navi", listview));   
		form.add(viewContainer); 
		form.add(window);
		form.add(new AjaxSubmitButton("response"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				// TODO Auto-generated method stub
					
					List<DatabaseName> select = (List<DatabaseName>) datacheck.getModelObject();
					// 要確認
					if(select.size() == 1) {
						select.add(select.get(0));
					}
					String[] databases = new String[select.size()];
					
					if(select.size()>0){
						for(int i=0;i<select.size();i++){
							databases[i] = select.get(i).getDatabaseName();
						}
						
						param.put("databases", databases);						
						setResponsePage(ResponseMeasurementPage.class, param);
					}else{
						error("Select databases");
					}

			}
			protected void onError(AjaxRequestTarget target, Form form){
				target.addComponent(feedback);
			}	
			
		});
		form.add(new Link<Void>("SQLBehaviorConfirmPage"){

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				setResponsePage(new SQLBehaviorConfirmPage());
			}
			
		});
		final TextField<String> text = new TextField<String>("databaseName", new PropertyModel<String>(this, "inputtext"));
		form.add(text);
		/**
		 * データベースの新規作成
		 */
		form.add(new AjaxSubmitButton("addLocale", form) {  
					
			protected void onSubmit(AjaxRequestTarget target, Form form) { 
				
				String db_name = getInputtext();

				if(db_name != null && !db_name.equals("")){
					Date date= new Date();
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
				    sdf1.applyPattern("yyyy MM/dd"); 
				    				    
				    
				    boolean sn = false;
				    ResultStruct rinput = con.read("select database_name from system.databases where user_name='"+user+"'");
				    for(int i=0;i<datas.size();i++){
				    	if(rinput.getResult().get(i).getDataList().get(0).equals(db_name)) sn=true;
				    }
				    if(sn == false){
				    	con.insert("insert into system.databases values(system.db_seq1.nextval,'"+db_name+"','"+user+"',sysdate,sysdate)");
				    	con.insert("commit");
				    	DatabaseName input = new DatabaseName(db_name,sdf1.format(date),sdf1.format(date));
				    	input.setCreateday(sdf1.format(date));
				    	input.setUpdateday(sdf1.format(date));
				    	datas.add(input);       
				    	setInputtext("");             

				    	listview.setCurrentPage(listview.getPageCount());   
//				    	target.addComponent(table);      
						target.addComponent(form);
						target.addComponent(feedback);
				    }else{
				    	//すでに登録されているデータベースネームを入力した時のエラーメッセージ
				    	setInputtext("");
				    	error("すでに登録されているデータベースネームです。");
				    }
				}else{
					//データベースネームがNULLのときのエラーメッセージ
					setInputtext("");             
			    	listview.setCurrentPage(listview.getPageCount());   
//			    	target.addComponent(table);      
					target.addComponent(form);
					target.addComponent(feedback);
					error("データベースネームを入力してください。");
				}
			}
			protected void onError(AjaxRequestTarget target, Form form){
				target.addComponent(feedback);
			}			
		});     
		this.add(form);  
	}

	public DatabaseName getDatabaseN() {
		return databaseN;
	}

	public void setDatabaseN(DatabaseName databaseN) {
		this.databaseN = databaseN;
	}
	
	public String getInputtext() {
		return inputtext;
	}

	public void setInputtext(String inputtext) {
		this.inputtext = inputtext;
	}

	public List<DatabaseName> getSelection() {
		return selection;
	}

	public void setSelection(List<DatabaseName> selection) {
		this.selection = selection;
	}

	public List<DatabaseName> getDatas() {
		return datas;
	}

	public void setDatas(List<DatabaseName> datas) {
		this.datas = datas;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}