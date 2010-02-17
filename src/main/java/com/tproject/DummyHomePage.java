package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.model.PropertyModel;

public class DummyHomePage extends WebPage {
	
	DatabaseStructure dbstruc;
	TableColumnM tcm;
	
	@SuppressWarnings("deprecation")
	public DummyHomePage() {
		// TODO Auto-generated constructor stub
		
		dbstruc = new DatabaseStructure("database1");
		
		TableStructure ts1 = new TableStructure("table1");
		
		ForeignConstraint fc = new ForeignConstraint();
		fc.setConstraintName("FK_TO_TABLE2");
		fc.setDeleteType("NO ACTION");
		fc.setReferenceConstraintName("PK_TABLE2");
		fc.setReferenceTable("table2");
		
		ColumnConstraint cc = new ColumnConstraint();
		cc.setForeign(fc);
				
		TableColumnM tm = new TableColumnM("tt1", "CHAR", 0, cc);
		
		ts1.addSchema(tm);
		
		TableStructure ts2 = new TableStructure("table2");
		
		ColumnConstraint c2 = new ColumnConstraint();
		c2.setPrimaryConstName("PK_TABLE2");
		c2.setPrimary(true);
		
		TableColumnM t2 = new TableColumnM("tt2", "CHAR", 0, c2);
		
		ts2.addSchema(t2);
		
		dbstruc.addTable(ts1);
		dbstruc.addTable(ts2);
		
		
		ColumnConstraint cc2 = new ColumnConstraint();
		cc2.setPrimaryConstName("NUM_PK");
		ForeignConstraint fc2 = new ForeignConstraint();
		fc2.setConstraintName("NUM_FK");
		cc2.setForeign(fc2);
		tcm = new TableColumnM("num", "NUMBER", 10, cc2);
		
		ConnectDao dao = ConnectDao.getInstance();
		((WicketApplication)getApplication()).setDao(dao);
		add(new PageLink<Void>("sqlbehavior", new SQLBehaviorConfirmPage()));
		add(new PageLink<Void>("RegisterPage", new RegisterPage()));
		PageParameters p = new PageParameters();
		Username username= new Username();
		p.put("username", username);
		add(new PageLink<Void>("IdconfirmPage", new IdconfirmPage(p)));
		PageParameters param = new PageParameters();
		param.put("database", "00001");
		param.put("user", "ss");

		param.put("dbstruc", dbstruc);
		param.put("tcm", tcm);
		
		add(new PageLink<Void>("tabledesigner", new TableDesignerPage(param)));
		add(new PageLink<Void>("LoginPage", new LoginPage()));
//		add(new PageLink<Void>("TopPage", new TopPage()));
		PageParameters databasename = new  PageParameters();
		databasename.put("database", "test_primary2");
		databasename.put("user","test_user");
		IDao con = UserDao.newInstance("test_user", "test_user");
		((WicketApplication)getApplication()).setDao(con);
		add(new PageLink<Void>("DataAutoCreate", new DataAutoCreatePage(databasename)));
		

		param.put("databases", new String[]{"str1", "str2"});
		add(new PageLink<Void>("constview", new ConstraintViewPage(param)));
		add(new PageLink<Void>("response", new ResponseMeasurementPage(param, new MockCommand())));
//		add(new PageLink<Void>("toppage", new TopPage()));
//		List<AnalyticsInformation> a = new ArrayList<AnalyticsInformation>();
//		AnalyticsInformation an = new AnalyticsInformation();
//		an.setResponseTime("00:00:00.01");
//		a.add(an);
//		add(new AnalysisPanel("panel", a));
		
//		dao.disconnect();
	}
	
	private class MockCommand implements Command , Serializable{

		public AnalyticsInformation execCommand(String user, String pass,
				String sql) {
			// TODO Auto-generated method stub
			AnalyticsInformation ai = new AnalyticsInformation();
			ai.setResponseTime("00:00:00.01");
			Statistics s = new Statistics();
			s.setRecursiveCalls(1900);
			ai.setStatistics(s);
			
			return ai;
		}
		
	}
}
