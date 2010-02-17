package com.tproject;
/**
 * 
 * @author Oh
 *
 */
public class UserTestDao{
	private IDao con;

	public UserTestDao() {
		// TODO 自動生成されたコンストラクター・スタブ
		createUser();
		createData();
	}
	public void createUser(){
		ConnectDao con = ConnectDao.getInstance();
		con.insert("insert into system.users values ('"+getUsername()+"','"+getPassword()+"')");
		/**
		 * Oracleにユーザー登録				
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
		
		con.insert("commit");
//		con.disconnect();
		
		
	}
	public void createData(){
		ConnectDao con = ConnectDao.getInstance();
		con.insert("insert into system.databases values ('1', 'database_test1', 'user_test', '09-01-01', '09-01-01')");
		con.insert("insert into system.databases values ('2', 'database_test2', 'user_test', '09-01-01', '09-01-01')");
		con.insert("insert into system.tables values ('1', 'table_test0', '1')");
		con.insert("insert into system.tables values ('2', 'table_test1', '2')");
		
		con.insert("commit");
//		con.disconnect();
		
	}
	
	public String getUsername() {		
		return "user_test";
	}
	public String getPassword() {		
		return "pass_test";
	}
	public DatabaseName getDatabaseName(){
		return new DatabaseName("database_test1","09-01-01", "09-01-01");
	}
	public void deleteUser(){
		ConnectDao con = ConnectDao.getInstance();
		con.insert("delete from system.tables where database_id='1' or database_id='2'");
		con.insert("delete from system.databases where user_name='"+getUsername()+"'");
		con.insert("delete from system.users where user_name='"+getUsername()+"'");		
		con.insert("drop user "+getUsername());
		con.insert("commit");
//		con.disconnect();
		
	}

}
