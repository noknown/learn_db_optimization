package com.tproject;



import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
/**
 * 
 * @author Oh
 *
 */

public class ConfirmationPage extends WebPage{
	
	private String user_name = "undefined";
	private String pass_word = "undefined";

	public ConfirmationPage(String username, String password) {
		super();
		this.user_name = username;
		this.pass_word = password;
		constructPage();
		
	}
	
	void constructPage() {
		setDefaultModel(new CompoundPropertyModel<String>(this));
		add(new Label("user_name"));
		add(new Label("pass_word"));
		Form<Void> form = new Form<Void>("submitForm"){
			@Override
			public void onSubmit(){
				ConnectDao con = ConnectDao.getInstance();
				/**
				 * usersデーブに登録
				 */
				con.insert("insert into system.users values ('"+getUsername()+"','"+getPassword()+"')");
				/**
				 * Oraclenにユーザー登録				
				 */
				con.insert("create user "+getUsername()+" identified by "+getPassword());
				
				/**
				 * ユーザーのシステム権限の設定
				 */
				con.insert("grant create session,create table,create sequence,create view,resource to "+getUsername());
				con.insert("grant select on system.databases to "+getUsername());
				con.insert("grant insert on system.databases to "+getUsername());
				con.insert("grant delete on system.databases to "+getUsername());
				con.insert("grant select on system.db_seq1 to "+getUsername());
				con.insert("grant update on system.databases to "+getUsername());
				con.insert("grant update on system.users to "+getUsername());
				con.insert("grant select on system.users to "+getUsername());
				con.insert("grant insert on system.users to "+getUsername());
				con.insert("grant delete on system.users to "+getUsername());
				con.insert("grant update on system.users to "+getUsername());
				con.insert("grant select on system.tables to "+getUsername());
				con.insert("grant insert on system.tables to "+getUsername());
				con.insert("grant delete on system.tables to "+getUsername());
				con.insert("grant select on system.table_seq1 to "+getUsername());
				con.insert("grant update on system.tables to "+getUsername());
				con.insert("grant alter system to "+getUsername());
				con.insert("grant PLUSTRACE to "+getUsername());
				con.insert("commit");
//				con.disconnect();
				setResponsePage(LoginPage.class);
				
			}
		};

		add(form);
		form.add(new BookmarkablePageLink<Void>("returnLink", RegisterPage.class));
	}
	
	public String getUsername() {
		// TODO Auto-generated method stub
		
		return user_name;
	}
		
	public String getPassword() {
		// TODO Auto-generated method stub
		return pass_word;
	}


}
