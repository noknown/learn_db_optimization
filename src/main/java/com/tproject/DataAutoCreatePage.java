package com.tproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;
/**
 * 
 * @author Oh
 *
 */
public class DataAutoCreatePage extends WebPage{
	//選択されたDatabaseNameに属するTableのリスト
	private List<TableStructure> tablenameList = new ArrayList<TableStructure>();
	//選択されたTableに属するColumnのリスト
	private List<TableColumnM> tablecolumnList = new ArrayList<TableColumnM>();
	//選択されたColumnのDataTypeとDatalengthのリスト
	private List<DataSelectionPanel> dataselectionList = new ArrayList<DataSelectionPanel>();

	private String user;
	private String databasename;
	private String nullable;
	//Table名
	private TableStructure tableN;
	//Column名
	private TableName[] tableC;
	//データタイプ
	private DataTypeChar dataC;
	//生成するデータの数
	private int dataF;
	//データフォーマット
	private String dataformat;
	//生成したINSERT文
	private String insertT;
	//生成したデータを格納した配列
	private String[] str;
	
	private List<String> insertsentence;
	private IndicatingAjaxButton deleteButton;
	private IndicatingAjaxButton commit;
	private int index;
		
	@SuppressWarnings({ "serial" })
	public DataAutoCreatePage(final PageParameters param) {
				
		this.databasename = param.getString("database");
		this.user = param.getString("user");
		final IDao con = ((WicketApplication)getApplication()).getDao();
		
		//フィードバックパネル
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		//Table名を取得
		final ResultStruct rs = con.read("select table_name from system.databases natural join system.tables where user_name = '"+user+"' and database_name = '"+databasename+"'");
		final DatabaseStructure ds = DatabaseTransform.toDatabaseStructure(con, user, databasename);
		
		if(!ds.getTables().isEmpty()){ 
			for (int i = 0; i < rs.getResult().size(); i++) {   
				TableStructure tables = new TableStructure(ds.getTables().get(i).getTableName());
				tablenameList.add(tables);
			}         
		}else{
			error("データがありません");
		}
		final Form<Void> choiceForm = new Form<Void>("tablechoiceForm");
		
		//選択されたテーブル名を表示
		final Label selectedName = new Label("selectedName", new PropertyModel<TableName>(this,"tableN.tableName"));
		selectedName.setOutputMarkupId(true);
		
		//生成するデータの数を指定する
		final RequiredTextField<Integer> dataF = new RequiredTextField<Integer>("dataF",new PropertyModel<Integer>(this,"dataF"),Integer.class);
		dataF.setOutputMarkupId(true);
		dataF.add(new MinimumValidator<Integer>(0));
		dataF.add(new MaximumValidator<Integer>(1000000));
		
		//生成したINSERT文を表示するテキストエリア
		final TextArea<String> insertText = new TextArea<String>("insertText",new PropertyModel<String>(this,"insertT"));
		insertText.setOutputMarkupId(true);

		
		//テーブル名のドロップダウン
		final DropDownChoice<TableStructure> selectTable = new DropDownChoice<TableStructure>("tablenamechoice",
															new PropertyModel<TableStructure>(this,"tableN"), 
															tablenameList,
															new ChoiceRenderer<TableStructure>("tableName"));	

		final RadioGroup<TableColumnM> group = new RadioGroup<TableColumnM>("group",new Model<TableColumnM>());
		group.setOutputMarkupId(true);
		choiceForm.add(group);
		final ListView<TableColumnM> columnview = new ListView<TableColumnM>("columnview",tablecolumnList){
			
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(final ListItem<TableColumnM> item) {
				// TODO Auto-generated method stub
				TableColumnM tablecolumn = item.getModelObject();
				item.setOutputMarkupId(true);
				item.add(new Radio("radio",item.getDefaultModel()));
				item.add(new Label("columnname",new PropertyModel<TableColumnM>(tablecolumn,"name")));
				
				if(tablecolumn.getConstraint().isPrimary()){
					if(tablecolumn.getConstraint().getForeign().getReferenceTable() != null){
						nullable = "Primary key and Reference "+tablecolumn.getConstraint().getForeign().getReferenceTable()+" - "+tablecolumn.getConstraint().getForeign().getReferenceTableColumn();
					}else{
						nullable = "Primary key";
					}
				}else if(tablecolumn.getConstraint().isNotnull()){
					if(tablecolumn.getConstraint().getForeign().getReferenceTable() != null){
						nullable = "NotNull and Reference "+tablecolumn.getConstraint().getForeign().getReferenceTable()+" - "+tablecolumn.getConstraint().getForeign().getReferenceTableColumn();
					}else{
						nullable = "NotNull";
					}
				}else{
					if(tablecolumn.getConstraint().getForeign().getReferenceTable() != null){
						nullable = "Unconstrained and Reference "+tablecolumn.getConstraint().getForeign().getReferenceTable()+" - "+tablecolumn.getConstraint().getForeign().getReferenceTableColumn();
					}else{
						nullable = "Unconstrained";
					}					
				}
				item.add(new Label("nullable",new Model<String>(nullable)));				
				item.add(new Label("datatype",new PropertyModel<TableColumnM>(tablecolumn, "type")));
				
				final DataSelectionPanel dataselection = new DataSelectionPanel("datatypeselectionPanel",item.getIndex(),new PropertyModel<TableColumnM>(item,"modelObject"));
				dataselection.setOutputMarkupId(true);
				if(tablecolumn.getConstraint().getForeign().getReferenceTable() == null){
					dataselection.setVisi(item.getModelObject().getType().toUpperCase());
				}
				
				dataselection.setEnabled(false);
				dataselectionList.add(dataselection);
				item.add(dataselection);
				
			}			
		}.setReuseItems(true);
		group.add(columnview);		
		group.add(new AjaxFormChoiceComponentUpdatingBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				for(int i=0;i<dataselectionList.size();i++){
					if(group.getModelObject().getName().equals(dataselectionList.get(i).tablename.getName())){
						dataselectionList.get(i).setEnabled(true);
					}else{
						dataselectionList.get(i).setEnabled(false);
					}
					dataselectionList.get(i).dataformatList.clear();
				}				
				target.addComponent(group);
				target.addComponent(feedback);
				
			}
		});
		//テーブル名のドロップダウンのAjaxビヘイビア
		selectTable.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				setTableN(selectTable.getModelObject());
	
				//初期化----------------------------
				setDataF(0);
				dataF.clearInput();
				setInsertT("");		
				tablecolumnList.clear();
				dataselectionList.clear();
				columnview.removeAll();
				//----------------------------
				
				if(tableN!=null){
//					ResultStruct rs_column = con.read("select column_name, data_type, data_length ,data_precision,nullable from user_tab_columns where table_name in(select upper(table_name) from system.databases natural join system.tables where user_name = '"+user+"' and table_name = '"+tableN.getTableName()+"')");
					index = -1;
					for(int i=0;i<ds.getTables().size();i++){
						if(getTableN().getTableName().equals(ds.getTables().get(i).getTableName())) index = i;
					}
					ResultStruct table_cnt = con.read("select * from "+getTableN().getTableName());

					if(table_cnt.isEmpty()) deleteButton.setEnabled(false);
					else deleteButton.setEnabled(true);

					tablecolumnList.clear();
					tableC = new TableName[ds.getTables().get(index).getSchemas().size()];
					for (int i = 0; i < ds.getTables().get(index).getSchemas().size(); i++) {
						TableColumnM tablecolumn = new TableColumnM(	ds.getTables().get(index).getSchemas().get(i).getName(),																	
													ds.getTables().get(index).getSchemas().get(i).getType(),
													ds.getTables().get(index).getSchemas().get(i).getLength(),
													ds.getTables().get(index).getSchemas().get(i).getPrecisionLength(),
													ds.getTables().get(index).getSchemas().get(i).getConstraint(),
													ds.getTables().get(index).getTableName());
						tablecolumnList.add(tablecolumn);

					}					
				}			 
			 
				target.addComponent(selectedName);
				target.addComponent(group);
				target.addComponent(dataF);
				target.addComponent(insertText);
				target.addComponent(choiceForm);
				target.addComponent(feedback);
			}
			
		});
		//入力されているデータを削除するためのBUTTON
		deleteButton = new IndicatingAjaxButton("deleteButton", choiceForm){

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				String tablename = getTableN().getTableName();
				con.insert("DELETE FROM " + tablename );
				con.insert("commit");
				deleteButton.setEnabled(false);
				
				target.addComponent(deleteButton);
			}
			
		};
		deleteButton.setOutputMarkupId(true);
		deleteButton.setEnabled(false);
		choiceForm.add(deleteButton);

		//INSERT文を生成するBUTTON
		IndicatingAjaxButton intgerButton = new IndicatingAjaxButton("intgerButton", choiceForm){
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form){
				
				if(getTableN()== null){
					error("Select Tablename!!");
				}else if(getDataF()<= 0){
					error("Input Number of data!!");
				}else{	
					setInsertT("");
					//必須項目のデータタイプがNULLかどうか
					boolean flag = false;
					
					DataDesigner design = new DataDesigner();				
										
					int pk_fk_cnt = 0;
					int pk_cnt = 0;
					
					//カラムの制約条件別にグループ別けするためのHashMap
					HashMap<String, HashMap<String, List<DataSelectionPanel>>> columngroup = new HashMap<String, HashMap<String,List<DataSelectionPanel>>>();
					
					//カラムのグループ分けの処理
					for(int i=0;i<dataselectionList.size();i++){
						
						if(dataselectionList.get(i).getTablename().getConstraint().isPrimary()) pk_cnt++;
						if(dataselectionList.get(i).getTablename().getConstraint().isPrimary() && dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable() != null) pk_fk_cnt++;
						
						if(dataselectionList.get(i).getTablename().getConstraint().isPrimary()){
							//主キーで、外部キーがない場合
							if(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable() != null){
								if(!columngroup.containsKey("pk_fk")){
									HashMap<String, List<DataSelectionPanel>> pk_fk = new HashMap<String, List<DataSelectionPanel>>();
									columngroup.put("pk_fk", pk_fk);
								}
								if(!columngroup.get("pk_fk").containsKey(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable())){
									columngroup.get("pk_fk").put(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable(), new ArrayList<DataSelectionPanel>());
								}
								columngroup.get("pk_fk").get(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable()).add(dataselectionList.get(i));
							}
							//主キーで、外部キーを持っている場合
							else{
								if(dataselectionList.get(i).type != null && !dataselectionList.get(i).type.equals("")){

									if(!columngroup.containsKey("pk")){
										HashMap<String, List<DataSelectionPanel>> pk = new HashMap<String, List<DataSelectionPanel>>();
										columngroup.put("pk", pk);
									}
									if(!columngroup.get("pk").containsKey("notReference")){
										columngroup.get("pk").put("notReference", new ArrayList<DataSelectionPanel>());
									}
									columngroup.get("pk").get("notReference").add(dataselectionList.get(i));
								}else{
									error(dataselectionList.get(i).getTablename().getName()+"は必須です");
									flag = true;
								}
							}						
						}
						else if(dataselectionList.get(i).getTablename().getConstraint().isNotnull()){
							//NOTNULLで、外部キーを持っていない場合
							if(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable() != null){
								if(!columngroup.containsKey("notnull_fk")){
									HashMap<String, List<DataSelectionPanel>> notnull_fk = new HashMap<String, List<DataSelectionPanel>>();
									columngroup.put("notnull_fk", notnull_fk);
								}
								if(!columngroup.get("notnull_fk").containsKey(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable())){
									columngroup.get("notnull_fk").put(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable(), new ArrayList<DataSelectionPanel>());
								}
								columngroup.get("notnull_fk").get(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable()).add(dataselectionList.get(i));
							}
							//NOTNULLで、外部キーを持っている場合
							else{
								if(dataselectionList.get(i).type != null && !dataselectionList.get(i).type.equals("")){
									if(!columngroup.containsKey("notnull")){
										HashMap<String, List<DataSelectionPanel>> notnull = new HashMap<String, List<DataSelectionPanel>>();
										columngroup.put("notnull", notnull);
									}
									if(!columngroup.get("notnull").containsKey("notReference")){
										columngroup.get("notnull").put("notReference", new ArrayList<DataSelectionPanel>());	
									}
									columngroup.get("notnull").get("notReference").add(dataselectionList.get(i));
								}else{
									error(dataselectionList.get(i).tablename.getName()+"は必須です");
									flag = true;
								}
							}
							
						}else{
							//NULLABLEで、外部キーを持っている場合の処理
							if(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable() != null){
								if(!columngroup.containsKey("none_fk")){
									HashMap<String, List<DataSelectionPanel>> none_fk = new HashMap<String, List<DataSelectionPanel>>();
									columngroup.put("none_fk", none_fk);
								}
								if(!columngroup.get("none_fk").containsKey(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable())){
									columngroup.get("none_fk").put(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable(), new ArrayList<DataSelectionPanel>());
								}
								columngroup.get("none_fk").get(dataselectionList.get(i).getTablename().getConstraint().getForeign().getReferenceTable()).add(dataselectionList.get(i));
							}
							//NULLABLEで、外部キーを持っていない場合の処理
							else{
								if(!columngroup.containsKey("none")){
									HashMap<String, List<DataSelectionPanel>> none = new HashMap<String, List<DataSelectionPanel>>();
									columngroup.put("none", none);
								}
								if(!columngroup.get("none").containsKey("notReference")){
									columngroup.get("none").put("notReference", new ArrayList<DataSelectionPanel>());	
								}
								columngroup.get("none").get("notReference").add(dataselectionList.get(i));
							}
						}
					}
					if(!flag){
						//グループ別にカラム名を格納するリスト
						List<String> columnnames = new ArrayList<String>();
						//生成したデータを格納するリスト
						List<List<String>> values = new ArrayList<List<String>>();
						//必須の項目にデータタイプがNULLかどうか
						boolean defaultcheck = false;
						boolean nullcheck = false;
						
						columnnames.clear();
						values.clear();
						
						Set<String> keysets = columngroup.keySet();
						for(String key : keysets){
							HashMap<String, List<DataSelectionPanel>> column = columngroup.get(key);
							Set<String> keyset = column.keySet();
							defaultcheck = false;
							
							if(key.equals("pk")){
								List<DataSelectionPanel> list = column.get("notReference");
								for(DataSelectionPanel t : list){
									columnnames.add(t.getTablename().getName());
									List<String> value = new ArrayList<String>();
									for(int k=0;k<getDataF();k++){									
										if(t.getTablename().getType().toUpperCase().equals("DATE")){
											value.add(design.createDate(t.startYear.getDefaultModelObjectAsString(),t.startMonth.getDefaultModelObjectAsString(),t.startDay.getDefaultModelObjectAsString(),t.endYear.getDefaultModelObjectAsString(),t.endMonth.getDefaultModelObjectAsString(),t.endDay.getDefaultModelObjectAsString()));
										}else if(t.type.equals("Free")){
											if(t.dataformatselection.textareaT == null || t.dataformatselection.textareaT.equals("")){
												defaultcheck = true;
												break;												
											}else{
												String str = design.CreateDataFree(t.dataformatselection, t.dataformatselection.length);
												boolean same = false;
												if(k != 0){
													for(int i=value.size()-1;i>=0;i--){
														if(value.get(i).toString().equals(str)){
															k=k-1;
															same = true;
														}
													}
												}
												if(!same) value.add(str);
											}
										}else if(t.type.equals("Sequence")){
											if(k == 0){
												value.add(String.valueOf(t.datasquenceList.get(0).start));
											}else{
												value.add(String.valueOf(Integer.parseInt(value.get(k-1).toString())+t.datasquenceList.get(0).step));
											}
										}else{
											String str = design.CreateData(t.tablename.getType().equals("NUMBER") ? t.id : t.id, t.tablename.getLength());
											boolean same = false;
											if(k != 0){
												for(int i=value.size()-1;i>=0;i--){
													if(value.get(i).toString().equals(str)){
														k=k-1;
														same = true;
													}
												}
											}
											if(!same) value.add(str);	
										}
									}
									values.add(value);
								}																
							}else if(key.equals("pk_fk")){
								for(String skey : keyset){
									List<DataSelectionPanel> list = column.get(skey);
									String group = list.get(0).getTablename().getName();
									String group_f = list.get(0).getTablename().getConstraint().getForeign().getReferenceTableColumn();
									if(list.size()>1){
										for(int i=1;i<list.size();i++){
											group += "," + list.get(i).getTablename().getName();
											group_f += "," + list.get(i).getTablename().getConstraint().getForeign().getReferenceTableColumn();
										}
									}
									columnnames.add(group);

									List<String> value = new ArrayList<String>();
									int compare = pk_cnt - pk_fk_cnt;
									ResultStruct r = con.read("select "+group_f+" from "+skey);
									System.err.println("select "+group_f+" from "+skey);
									if(!r.isEmpty()){
										if(compare > 0){
											for(int k=0;k<getDataF();k++){
												value.add(design.Columnseleted(r));
											}
										}else{
											if(r.getResult().size() >= getDataF()){
												for(int k=0;k<getDataF();k++){
													String str = design.Columnseleted(r);
													boolean same = false;
													if(k != 0){
														for(int i=value.size()-1;i>=0;i--){
															if(value.get(i).toString().equals(str)){
																k=k-1;
																same = true;
															}
														}
													}
													if(!same) value.add(str);
												}
											}else{
												error("外部参照先のデータ数より多いデータ数を生成しようとしています。ご確認ください。");
												defaultcheck = true;
											}
										}
									}else{
										error(list.get(0).getTablename().getName()+"参照先の"+list.get(0).getTablename().getConstraint().getForeign().getReferenceTable()+"にデータがありません。確認してください。");
										nullcheck = true;
									}
									values.add(value);

								}
							}else if(key.equals("notnull")){
								List<DataSelectionPanel> list = column.get("notReference");
								for(DataSelectionPanel t : list){
									columnnames.add(t.getTablename().getName());
									List<String> value = new ArrayList<String>();
									for(int k=0;k<getDataF();k++){
										if(t.tablename.getType().toUpperCase().equals("DATE")){
											value.add(design.createDate(t.startYear.getDefaultModelObjectAsString(),t.startMonth.getDefaultModelObjectAsString(),t.startDay.getDefaultModelObjectAsString(),t.endYear.getDefaultModelObjectAsString(),t.endMonth.getDefaultModelObjectAsString(),t.endDay.getDefaultModelObjectAsString()));
										}else if(t.type.equals("Free")){
											if(t.dataformatselection.textareaT == null || t.dataformatselection.textareaT.equals("")){
												defaultcheck = true;
												break;

											}else{
												value.add(design.CreateDataFree(t.dataformatselection, t.dataformatselection.length));
											}
										}else if(t.type.equals("Sequence")){
											if(k == 0){
												value.add(String.valueOf(t.datasquenceList.get(0).start));
											}else{
												value.add(String.valueOf(Integer.parseInt(value.get(k-1))+t.datasquenceList.get(0).step));
											}
										}else{
											value.add(design.CreateData(t.tablename.getType().equals("NUMBER") ? t.id : t.id, t.tablename.getLength()));
										}
									}
									values.add(value);
								}	
							}else if(key.equals("notnull_fk")){
								for(String skey : keyset){
									List<DataSelectionPanel> list = column.get(skey);
									String group = list.get(0).getTablename().getName();
									String group_f = list.get(0).getTablename().getConstraint().getForeign().getReferenceTableColumn();
									if(list.size()>1){
										for(int i=1;i<list.size();i++){
											group += "," + list.get(i).getTablename().getName();
											group_f += "," + list.get(i).getTablename().getConstraint().getForeign().getReferenceTableColumn();
										}
									}
									columnnames.add(group);
									List<String> value = new ArrayList<String>();
									ResultStruct r = con.read("select "+group_f+" from "+skey);
									if(!r.isEmpty()){
										for(int k=0;k<getDataF();k++){
											value.add(design.Columnseleted(r));
										}
									}else{
										error(list.get(0).getTablename().getName()+"参照先の"+list.get(0).getTablename().getConstraint().getForeign().getReferenceTable()+"にデータがありません。確認してください。");
										nullcheck = true;
									}
									values.add(value);
								}
							}else if(key.equals("none")){
								List<DataSelectionPanel> list = column.get("notReference");
								for(DataSelectionPanel t : list){
									System.err.println(t.getTablename().getType());
									if(t.getType() != null && !t.getType().equals("")){
										columnnames.add(t.getTablename().getName());
										List<String> value = new ArrayList<String>();
										for(int k=0;k<getDataF();k++){
											if(t.tablename.getType().toUpperCase().equals("DATE")){
												value.add(design.createDate(t.startYear.getDefaultModelObjectAsString(),t.startMonth.getDefaultModelObjectAsString(),t.startDay.getDefaultModelObjectAsString(),t.endYear.getDefaultModelObjectAsString(),t.endMonth.getDefaultModelObjectAsString(),t.endDay.getDefaultModelObjectAsString()));
											}else if(t.type.equals("Free")){
												if(t.dataformatselection.textareaT == null || t.dataformatselection.textareaT.equals("")){
													defaultcheck = true;
													break;

												}else{
													value.add(design.CreateDataFree(t.dataformatselection, t.dataformatselection.length));
												}
											}else if(t.type.equals("Sequence")){
												if(k == 0){
													value.add(String.valueOf(t.datasquenceList.get(0).start));
												}else{
													value.add(String.valueOf(Integer.parseInt(value.get(k-1))+t.datasquenceList.get(0).step));
												}
											}else{
												value.add(design.CreateData(t.tablename.getType().equals("NUMBER") ? t.id : t.id, t.tablename.getLength()));
											}
										}
										values.add(value);
									}
								}
							}else{
								for(String skey : keyset){
									List<DataSelectionPanel> list = column.get(skey);
									String group = list.get(0).getTablename().getName();
									String group_f = list.get(0).getTablename().getConstraint().getForeign().getReferenceTableColumn();
									if(list.size()>1){
										for(int i=1;i<list.size();i++){
											group += "," + list.get(i).getTablename().getName();
											group_f += "," + list.get(i).getTablename().getConstraint().getForeign().getReferenceTableColumn();
										}
									}
									columnnames.add(group);
									List<String> value = new ArrayList<String>();
									ResultStruct r = con.read("select "+group_f+" from "+skey);
									if(!r.isEmpty()){
										for(int k=0;k<getDataF();k++){
											value.add(design.Columnseleted(r));
										}
									}else{
										error(list.get(0).getTablename().getName()+"参照先の"+list.get(0).getTablename().getConstraint().getForeign().getReferenceTable()+"にデータがありません。確認してください。");
										nullcheck = true;
									}
									values.add(value);
								}
							}
						}
					
						String names = "(" + columnnames.get(0).toString();
						for(int j=1;j<columnnames.size();j++){
							names += "," +columnnames.get(j).toString();

						}
						names += ")";
						System.err.println(names);

						if(!nullcheck){
							if(!defaultcheck){
								String columnname = "";
								String value = "";
								String insert = "";
								insertsentence = new ArrayList<String>();

								for(int h=0;h<getDataF();h++){
									columnname = "(" + columnnames.get(0).toString();
									value = "('" + values.get(0).get(h);
									for(int g=1;g<columnnames.size();g++){
										columnname += "," + columnnames.get(g).toString();											
										value += "','" + values.get(g).get(h).toString();
									}
									columnname += ")";
									value += "')";
									insert += "INSERT INTO "+getTableN().getTableName()+" "+columnname+" VALUES "+value+"\n";
									insertsentence.add("INSERT INTO "+getTableN().getTableName()+" "+columnname+" VALUES "+value);
								}
								setInsertT(insert);
							}else{
								error("Please Input dataformat");
							}									
						}
					}else{
						error("Please choose the type of data to generate");
					}
				}
				group.setDefaultModelObject(null);
				for(int i=0;i<dataselectionList.size();i++) dataselectionList.get(i).setEnabled(false);
				commit.setEnabled(true);
				
				target.addComponent(commit);
				target.addComponent(group);
				target.addComponent(insertText);
				target.addComponent(feedback);
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form){
				target.addComponent(feedback);
				super.onError(target, form);
			}
		};
		//生成したINSERT文をデータベースサーバに送信するBUTTON
		commit = new IndicatingAjaxButton("commit",choiceForm){
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form){
				
				setTableN(selectTable.getModelObject());
				List<String> insert = getInsertsentence();
				if(getTableN()== null){
					error("Select Tablename!!");
				}else if(getDataF()<= 0){
					error("Input Number of data!!");
				}else if(insert == null){
					error("Please push the createData Button");
				}else{
					for(int i=0;i<insert.size();i++){
						con.insert(insert.get(i).toString());
						con.insert("commit");
					}
					commit.setEnabled(false);
					deleteButton.setEnabled(true);
					
					target.addComponent(commit);
					target.addComponent(deleteButton);
					target.addComponent(form);
					target.addComponent(feedback);
				}
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form){
				target.addComponent(feedback);
				super.onError(target, form);
			}
		};
		commit.setEnabled(false);
		Link<Void> top = new Link<Void>("top"){

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				setResponsePage(TopPage.class, param);
			}
			
		};
		add(top);
		choiceForm.add(commit);
		choiceForm.add(intgerButton);
		choiceForm.add(insertText);
		choiceForm.add(dataF);
		choiceForm.add(selectedName);
		choiceForm.add(selectTable);
		add(choiceForm);
	}

	
	public TableStructure getTableN() {
		return tableN;
	}
	
	public void setTableN(TableStructure tableN) {
		this.tableN = tableN;
	}
	
	public void setDataC(DataTypeChar dataC) {
		this.dataC = dataC;
	}
	
	public DataTypeChar getDataC() {
		return dataC;
	}
	
	public void setDataF(int dataF) {
		this.dataF = dataF;
	}
	
	public int getDataF() {
		return dataF;
	}	
	
	public void setInsertT(String insertT) {
		this.insertT = insertT;
	}
	
	public String getInsertT() {
		return insertT;
	}
	
	public void setDataformat(String dataformat) {
		this.dataformat = dataformat.toUpperCase();
	}
	
	public String getDataformat() {
		return dataformat;
	}
	
	public String[] getStr() {
		return str;
	}
	
	public void setStr(String[] str) {
		this.str = str;
	}
	
	public void setTableC(int index,TableName tableC) {
		this.tableC[index] = tableC;
	}

	public List<String> getInsertsentence() {
		return insertsentence;
	}

	public void setInsertsentence(List<String> insertsentence) {
		this.insertsentence = insertsentence;
	}


	public String getNullable() {
		return nullable;
	}


	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

}
