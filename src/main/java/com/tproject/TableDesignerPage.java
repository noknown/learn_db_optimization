package com.tproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.draggable.DraggableBehavior;
import org.odlabs.wiquery.ui.draggable.DraggableContainment;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

/**
 * テーブル設計用インターフェースページ
 * @author no_known
 *
 */
public class TableDesignerPage extends WebPage {
	
//	private IDao tdao;
	private DatabaseStructure databasestructure, tempdbstructure;
	private LinkedList<WebMarkupContainer> tcontainer;
	
	// カラム移動の際の仮保存領域
	private TableColumnM deletecolumn;
	private TableStructure deletetable;
	
	// リレーションシップの保持
	private PrimaryForeignRelationship relate = new PrimaryForeignRelationship();
	
	// カラムのコピー有無を表す論理値
	private boolean copycheck;
	
	public TableDesignerPage(final PageParameters param) {
		// TODO Auto-generated constructor stub		
		
		final String databasename = param.getString("database");
		final String username = ((LoginSession)this.getSession()).getUserName();
		System.err.println(username);
//		this.tdao = dao;
		tcontainer = new LinkedList<WebMarkupContainer>();
		
		final IDao dao = ((WicketApplication)getApplication()).getDao();
		
		Form<Void> form = new Form<Void>("form") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				//作りかけ
				
				// テーブルの作成をエラーが発生しない順に並べる処理
				List<TableStructure> ntslist = getSortedTables(tempdbstructure);
				List<TableStructure> otslist = getSortedTables(databasestructure);
				
				CompareTable ct = new CompareTable();
				List<? extends AbstractDifferenceObject> dtlist = ct.compare(otslist, ntslist);
				List<DifferenceTable> dtables = new ArrayList<DifferenceTable>();
				
				for(int i = 0; i < dtlist.size(); i++) {
					dtables.add((DifferenceTable)dtlist.get(i));
				}
				
				DatabaseEdit.edit(dao, dtables, username, databasename);
				
				IDao sysdao = ConnectDao.getInstance();
				System.err.println("update system.databases set updateday = sysdate where database_name = '"+databasename+"' " +
						"and user_name = '"+ username +"'");
				sysdao.insert("update system.databases set updateday = sysdate where database_name = '"+databasename+"' " +
						"and user_name = '"+ username +"'");
				
				setResponsePage(TopPage.class, param);
				
			}
		};
		
		form.add(new BookmarkablePageLink<Void>("toppage", TopPage.class, param));
		
		final WebMarkupContainer container = new WebMarkupContainer("tableview");
		container.setOutputMarkupId(true);
		
		final ModalWindow window = new ModalWindow("modal");
		add(window);
		
		form.add(new Button("addtable").add(new AjaxEventBehavior("onclick") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int number = 1;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				//新規作成されたテーブルには必ず一つのカラムがつく。
				TableStructure tss = new TableStructure("new_table_"+number);
				do {
					tss = new TableStructure("new_table_"+number++);
					tss.addSchema(new TableColumnM("new_column_1", "NUMBER", 1, 0, new ColumnConstraint(), tss.getTableName()));
				} while(!tempdbstructure.addTable(tss));
				
				target.addComponent(container);
				
//				target.appendJavascript("var newNode = document.createElement('DIV');" +
//						"newNode.id='dis'; var preNode = document.getElementsByTagName('body').item(0);" +
//						"newNode.innerHTML='gayos';" +
//						"preNode.appendChild(newNode);");
			}
		
		}));
		
		form.add(new AjaxCheckBox("copycheck", new PropertyModel<Boolean>(this, "copycheck")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		add(form);

		databasestructure = DatabaseTransform.toDatabaseStructure(dao, username, databasename);
		tempdbstructure = new DatabaseStructure(databasestructure.getDatabaseName());
		for(TableStructure ts : databasestructure.getTables()) {
			tempdbstructure.addTable((TableStructure)ts.clone());
		}
		
		// テーブル群の生成処理
		container.add(new ListView<TableStructure>("tables",  new PropertyModel<List<TableStructure>>(tempdbstructure, "tables")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<TableStructure> item) {
				// TODO Auto-generated method stub
				final TableStructure ts = item.getModelObject();
				
		
				final WebMarkupContainer cont = new WebMarkupContainer("cont");
				cont.setOutputMarkupId(true);
				
				DraggableBehavior draggable = new DraggableBehavior();
				draggable.setContainment(new DraggableContainment(DraggableContainment.ContainmentEnum.WINDOW));
				draggable.setHandle("span.dragtable");
				cont.add(draggable);
			
				cont.add(new AjaxEditableLabel<String>("name", new PropertyModel<String>(ts, "tableName")) {
					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						super.onSubmit(target);
						
						for(TableColumnM tcms : ts.getSchemas()) {
							tcms.setTableName(ts.getTableName());
						}
					}
				});
				
				cont.add(new AjaxLink<Void>("create") {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					private int number = 1;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						while(!ts.addSchema(new TableColumnM("new_column_"+number, "NUMBER", 1, 0, new ColumnConstraint(), ts.getTableName()))) number++;
//						number++;
//						target.addComponent(cont);
						for(WebMarkupContainer w : tcontainer) target.addComponent(w);
					}
					
				});
				cont.add(new AjaxLink<Void>("delete") {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						tempdbstructure.getTables().remove(ts);
						
						target.addComponent(container);
						
					}
					
				});
				
				//Sortable設定のためのコンテナ
				final WebMarkupContainer sortable = new WebMarkupContainer("sorted");
				sortable.setOutputMarkupId(true);
				
				//更新用コンテナリスト
				tcontainer.add(sortable);
				
				SortableAjaxBehavior sort = new SortableAjaxBehavior() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					
					private boolean received = false;

					@Override
					public void onReceive(Component item, int index,
							Component container, AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						received = true;
					}

					@Override
					public void onRemove(Component item, AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						deletecolumn = (TableColumnM) item.getDefaultModelObject();
						deletetable = ts;
					}

					@Override
					public void onUpdate(Component item, int index,
							AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						
						TableColumnM tcm = (TableColumnM) item.getDefaultModelObject();
						
						if(received) {
							
							// カラムのコピーが許可されていれば、元のカラムも削除せずに置いておく。
							// また、テーブル間の関連性があるもの同士でないと移動はできない。
							if(ts.isRelation(deletetable) && ts.addSchema(tcm) && !copycheck)
								deletetable.removeSchema(deletecolumn);
							
							
							received = false;
							
							for(WebMarkupContainer w : tcontainer) target.addComponent(w);
						}

					}
				};
				sort.getSortableBehavior().setConnectWith(".sorted");
				sort.getSortableBehavior().setHandle("span.draggable");
				sort.getSortableBehavior().setCancel("span.non");
				
				sortable.add(sort);
				
				// カラム群の生成処理
				sortable.add(new ListView<TableColumnM>("table", new PropertyModel<List<TableColumnM>>(ts, "schemas")) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<TableColumnM> iteml) {
						// TODO Auto-generated method stub
						iteml.setOutputMarkupId(true);
						
						final TableColumnM tcolumn = iteml.getModelObject();
						
						// 最初読み込まれたときと、追加ボタンを押された時のために必要。
						final boolean flag = addClassAttribute(iteml);
						
						Label sort = new Label("sort", "　　");
						if(tcolumn.getConstraint().isPrimary()) {
							sort.add(new SimpleAttributeModifier("style", "background-color: blue; color: blue;"));
						}
						
						// primaryかforeignなら移動させないためにclass属性にnonを追加
						sort.add(new AttributeAppender("class", Model.of(flag ? "non" : ""), " "));
						iteml.add(sort);
												
						iteml.add(new Label("col", new PropertyModel<String>(tcolumn, "name")));
						iteml.add(new AjaxLink<Void>("del") {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								// TODO Auto-generated method stub
								
								ts.removeSchema(tcolumn);
								//テーブル内のカラムがなくなったときはテーブル自体も消す
								if(ts.getSchemas().isEmpty()) {
									tempdbstructure.getTables().remove(ts);
									tcontainer.remove(sortable);
								}
								
								// 全体を再描画しないと、なんだかおかしなことに
								// setReuseItemメソッドを呼び出さないようにしたことで解決した。
//								target.addComponent(container);
								for(WebMarkupContainer w : tcontainer) target.addComponent(w);
							}
							
						});
						
						iteml.add(new AjaxLink<Void>("edit") {

							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								// TODO Auto-generated method stub
//								window.setContent(new ConstraintViewPage(databasestruc,tcolumn));
								window.setPageCreator(new ModalWindow.PageCreator() {

									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									public Page createPage() {
										// TODO Auto-generated method stub
										PageParameters param = new PageParameters();
										param.put("dbstruc", tempdbstructure);
										param.put("tcm", tcolumn);
										
										return new ConstraintViewPage(param);
									}
									
								});
								
								window.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
								window.setTitle("カラム情報");
								window.setResizable(false);
								window.setInitialWidth(400);
								window.setInitialHeight(500);
								window.setWidthUnit("px");
								window.setHeightUnit("px");
								
								window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									public void onClose(AjaxRequestTarget target) {
										// TODO Auto-generated method stub
										
										boolean flag = addClassAttribute(iteml);
										Label label = (Label) iteml.get("sort");
										label.add(new AttributeAppender("class", Model.of(flag ? "non" : ""), " "));
										
										target.addComponent(iteml);
										target.addComponent(sortable);
//										target.addComponent(container);
										
									}
									
								});
								window.show(target);
							}
							
						});
					}
					
				});
				
				cont.add(sortable);
				
				item.add(cont);
			}
			
		});

		add(container);
		InsertJavascript ij = new InsertJavascript("non");
		ij.add(new DraggableBehavior());
		ij.add(new AbstractBehavior() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(IHeaderResponse response) {
				// TODO Auto-generated method stub
				super.renderHead(response);

				response.renderJavascriptReference(new ResourceReference(InsertJavascript.class,
						"jquery.svg.js"));
				response.renderJavascriptReference(new ResourceReference(InsertJavascript.class, 
						"jquery.drawinglibrary.js"));
			}
		});
		add(ij);
		
	}
	
	/**
	 * テーブル間の関連性をhtml上のclass属性に追加する
	 * @param iteml タグを追加するhtml上のカラム
	 * @return primaryか、foreignが付いたらtrue。それ以外はfalse
	 */
	private boolean addClassAttribute(ListItem<TableColumnM> iteml) {
	
		boolean flag = false;
		TableColumnM tcm = iteml.getModelObject();
		if(tcm.getConstraint().isPrimary() && tcm.getConstraint().getForeign() != null && tcm.getConstraint().getForeign().getReferenceTable() != null) {
			String primary = (tcm.getTableName()+tcm.getName()).toUpperCase();
			String foreign = (tcm.getConstraint().getForeign().getReferenceTable()+tcm.getConstraint().getForeign().getReferenceTableColumn()).toUpperCase();

			relate.addRelation(primary);
			relate.addRelation(foreign);
			int number = relate.getRelationNumber(foreign);
			
			if(number != PrimaryForeignRelationship.UNDEFINED_RELATION) {
				iteml.add(new SimpleAttributeModifier("class", "ctarget primary"+relate.getRelationNumber(primary) 
						+" foreign"+number));
				flag = true;
			}
			
		} else if(tcm.getConstraint().isPrimary()) {
			String primary = (tcm.getTableName()+tcm.getName()).toUpperCase();
			System.err.println("primary : "+primary);
			relate.addRelation(primary);
			
			iteml.add(new SimpleAttributeModifier("class", "ctarget primary"+relate.getRelationNumber(primary)));
			
			flag = true;
		} else if(tcm.getConstraint().getForeign() != null && tcm.getConstraint().getForeign().getReferenceTable() != null) {
			String foreign = (tcm.getConstraint().getForeign().getReferenceTable()+tcm.getConstraint().getForeign().getReferenceTableColumn()).toUpperCase();
			System.err.println("foreign : "+foreign);
			relate.addRelation(foreign);
			int number = relate.getRelationNumber(foreign);
			
			if(number != PrimaryForeignRelationship.UNDEFINED_RELATION) {
				iteml.add(new SimpleAttributeModifier("class", "ctarget foreign"+number));
				flag = true;
			}
		}
		
		return flag;
	}
	
	/**
	 * テーブルの生成、削除をエラーが発生しない順番に並び替える処理
	 * @param structure 並び替えるテーブル群が入ったDatabaseStructure
	 * @return 並び替えられたTableList群
	 */
	public static List<TableStructure> getSortedTables(DatabaseStructure structure) {
		Queue<String> queue = new LinkedBlockingQueue<String>();
		List<TableStructure> tslist = new ArrayList<TableStructure>();
		
		for(TableStructure ts : structure.getTables()) {
			if(!ts.getPrimarycolumn().isEmpty() && ts.getForeigncolumn().isEmpty()) {
				for(String sname : ts.getPrimarycolumn()) queue.add(sname);
				
				tslist.add(ts);
			}
		}		
		while(!queue.isEmpty()) {
			String sname = queue.poll();
			for(TableStructure ts : structure.getTables()) {
				if(ts.getForeigncolumn().contains(sname)) {
					for(String tname : ts.getPrimarycolumn()) queue.add(tname);
					tslist.add(ts);
					structure.getTables().remove(ts);
					break;
				}
			}
		}
		for(TableStructure ts : structure.getTables()) {
			if(ts.getPrimarycolumn().isEmpty() && ts.getForeigncolumn().isEmpty()) {
				tslist.add(ts);
			}
		}
		
		//テーブル消去
		for(TableStructure ts : tslist) {
			structure.getTables().remove(ts);
		}
		
		for(TableStructure ts : structure.getTables()) {
			tslist.add(ts);
		}
		
		return tslist;
	}
	
}
