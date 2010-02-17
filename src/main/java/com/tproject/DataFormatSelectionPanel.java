package com.tproject;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.StringValidator.MinimumLengthValidator;
/**
 * 
 * @author Oh
 *
 */
@SuppressWarnings("serial")
public class DataFormatSelectionPanel extends Panel {
	//データの長さ
	protected int length;
	//データフォーマットを核のした配列
	protected String[] dataformat;
	protected String[] datatext;
	protected String[] datastring;
	protected RequiredTextField<String> assignment;
	protected RequiredTextField<String> repeat;
	protected RequiredTextField<String> rangeC1;
	protected RequiredTextField<String> rangeC2;
	protected RequiredTextField<String> rangeC3;
	protected RequiredTextField<String> rangeC4;
	protected RequiredTextField<Integer> rangeC5;
	protected RequiredTextField<Integer> rangeC6;
	protected RequiredTextField<Integer> rangeN1;
 	protected RequiredTextField<Integer> rangeN2;
	protected RequiredTextField<String> direct;
	protected RequiredTextField<Integer> directNumber;
	
	protected AjaxButton upperButton;
	protected AjaxButton lowerButton;
	protected AjaxButton figureButton;
	protected AjaxButton upperlowerButton;
	protected AjaxButton upperfigureButton;
	protected AjaxButton lowerfigureButton;
	protected AjaxButton allcaseButton;
	protected AjaxButton assignmentButton;
	protected AjaxButton repeatButton;
	protected AjaxButton rangeCButton;
	protected AjaxButton rangeNButton;
	protected AjaxButton directButton;
		
	protected String assignmentT="A";
	protected String repeatT="2";
	protected String rangeC1T="A";
	protected String rangeC2T="Z"; 
	protected String rangeC3T="a";
	protected String rangeC4T="z";
	protected String rangeC5T="0";
	protected String rangeC6T="9"; 
	protected String rangeN1T="0";
	protected String rangeN2T="9";
	protected String directT="A";
	protected String directN="1";
	protected String textareaT="";
	protected int index;
	protected int index_before;
	protected boolean repeatB;	
	
	protected Label assignmentL;
	protected Label assignmentR;
	protected Label repeatL;
	protected Label repeatR;
	protected Label rangeCL;	
	protected Label rangeC1L;
	protected Label rangeC_1;
	protected Label rangeC_2;
	protected Label rangeC2L;
	protected Label rangeC_3;
	protected Label rangeCR;
	protected Label rangeNL;
 	protected Label rangeNR;
 	protected Label rangeN_1;
 	protected Label directL;
	protected Label directR;
	
	public DataFormatSelectionPanel(String id, String length) {
		super(id);
		constructPage(length);
		
	}

	protected void constructPage(String length) {
		this.length = Integer.parseInt(length);
		this.index = 0;
		this.index_before = 0;
		this.dataformat = new String[this.length];
		this.datatext = new String[this.length];
		this.datastring = new String[this.length];
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		Form<Void> datacreateForm = new Form<Void>("dataformatForm");
		add(datacreateForm);
		
		//ユーザー指定フォーマットの表示
		final TextArea<String> textarea = new TextArea<String>("textarea", new PropertyModel<String>(this,"textareaT"));
		textarea.setOutputMarkupId(true);
		datacreateForm.add(textarea);
		
		assignment = new RequiredTextField<String>("assignment", new PropertyModel<String>(this,"assignmentT"));
		assignment.setOutputMarkupId(true);
		assignment.add(new StringValidator.MinimumLengthValidator(1));
		assignment.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = assignment.getDefaultModelObjectAsString();
				
				setAssignmentT(text);
				text ="["+text+"]";
				oneDelete();
				setFormat("Assignment",text);
				Textarea();
				
				target.addComponent(textarea);
//				target.addComponent(assignment);
				target.addComponent(feedback);
			}			
		});
//		assignment.setVisible(false);
		datacreateForm.add(assignment);
		
		repeat = new RequiredTextField<String>("repeat", new PropertyModel<String>(this,"repeatT"));
		repeat.setOutputMarkupId(true);
//		repeat.add(new MinimumValidator<Integer>(2));
		repeat.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
				String text = repeat.getDefaultModelObjectAsString();
				for(int i=0;i<getIndex_before()-1;i++) oneDelete();
				if(!text.equals("") && Integer.parseInt(text)>1){
					setRepeatT(text);					
					setFormat("Repeat",text);					
					Textarea();
					setIndex_before(Integer.parseInt(text));
					target.addComponent(repeat);
					
				}else{
					error("Input Number");
					setIndex_before(0);
				}
				target.addComponent(textarea);
				target.addComponent(feedback);
			}				
			
		});
//		repeat.setVisible(false);
		datacreateForm.add(repeat);
		
		rangeC1 = new RequiredTextField<String>("rangeC1", new PropertyModel<String>(this,"rangeC1T"));
		rangeC1.setOutputMarkupId(true);
		rangeC1.add(new StringValidator.ExactLengthValidator(1));
		rangeC1.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeC1.getDefaultModelObjectAsString();
				
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=65 && (int)(text.charAt(0))<=90){
						int sa = (int)getRangeC2T().charAt(0)-(int)text.charAt(0);
						if(sa<0){
							error("Smaller than the previous data!!");
						}else{
							setRangeC1T(text);				
							text ="["+text+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
							oneDelete();
							setFormat("RangeC",text);
							Textarea();
						}
					}else{
						error("Input Uppercase!!");
					}
				}else{
					setRangeC1T(text);				
					text ="["+text+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
					oneDelete();
					setFormat("RangeC",text);
					Textarea();
				}
				target.addComponent(textarea);
				target.addComponent(rangeC1);
				target.addComponent(feedback);
			}
			protected void onError(AjaxRequestTarget target, RuntimeException form){
				target.addComponent(feedback);
				super.onError(target, form);
			}
			
		});
//		rangeC1.setVisible(false);
		datacreateForm.add(rangeC1);
		
		rangeC2 = new RequiredTextField<String>("rangeC2", new PropertyModel<String>(this,"rangeC2T"));
		rangeC2.setOutputMarkupId(true);
		rangeC2.add(new StringValidator.ExactLengthValidator(1));
		rangeC2.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeC2.getDefaultModelObjectAsString();
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=65 && (int)(text.charAt(0))<=90){
						int sa = (int)text.charAt(0)- (int)getRangeC1T().charAt(0); 
						if(sa<0){
							error("Smaller than the previous data!!");
						}else{
							setRangeC2T(text);				
							text ="["+getRangeC1T()+"-"+text+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
							oneDelete();
							setFormat("RangeC",text);
							Textarea();
						}
					}else{
						error("Input Uppercase!!");
					}
				}else{
					setRangeC2T(text);				
					text ="["+getRangeC1T()+"-"+text+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
					oneDelete();
					setFormat("RangeC",text);
					Textarea();
				}
				target.addComponent(textarea);
				target.addComponent(rangeC2);
				target.addComponent(feedback);
			}
			
		});
//		rangeC2.setVisible(false);
		datacreateForm.add(rangeC2);
		
		rangeC3 = new RequiredTextField<String>("rangeC3", new PropertyModel<String>(this,"rangeC3T"));
		rangeC3.setOutputMarkupId(true);
		rangeC3.add(new StringValidator.ExactLengthValidator(1));
		rangeC3.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeC3.getDefaultModelObjectAsString();
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=97 && (int)(text.charAt(0))<=122){
						int sa = (int)getRangeC4T().charAt(0)-(int)text.charAt(0);
						if(sa<0){
							error("Largger than the previous data!!");
						}else{
							setRangeC3T(text);				
							text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+text+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
							oneDelete();
							setFormat("RangeC",text);
							Textarea();
						}
					}else{
						error("Input Lowercase!!");
					}
				}else{
					setRangeC3T(text);				
					text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+text+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
					oneDelete();
					setFormat("RangeC",text);
					Textarea();
				}
				
				target.addComponent(textarea);
				target.addComponent(rangeC3);
				target.addComponent(feedback);
			}
			
		});
//		rangeC3.setVisible(false);
		datacreateForm.add(rangeC3);
		
		rangeC4 = new RequiredTextField<String>("rangeC4", new PropertyModel<String>(this,"rangeC4T"));
		rangeC4.setOutputMarkupId(true);
		rangeC4.add(new StringValidator.ExactLengthValidator(1));
		rangeC4.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeC4.getDefaultModelObjectAsString();
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=97 && (int)(text.charAt(0))<=122){
						int sa = (int)text.charAt(0)- (int)getRangeC3T().charAt(0); 
						if(sa<0){
							error("Smaller than the previous data!!");
						}else{
							setRangeC4T(text);
							text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+text+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
							oneDelete();
							setFormat("RangeC",text);
							Textarea();
						}
					}else{
						error("Input Lowercase!!");
					}
				}else{
					setRangeC4T(text);
					text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+text+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
					oneDelete();
					setFormat("RangeC",text);
					Textarea();
				}
				
				target.addComponent(textarea);
				target.addComponent(rangeC4);
				target.addComponent(feedback);
			}
			
		});
//		rangeC4.setVisible(false);
		datacreateForm.add(rangeC4);
		
		rangeC5 = new RequiredTextField<Integer>("rangeC5", new PropertyModel<Integer>(this,"rangeC5T"),Integer.class);
		rangeC5.setOutputMarkupId(true);
		rangeC5.add(new MinimumValidator<Integer>(0));
		rangeC5.add(new MaximumValidator<Integer>(9));
		rangeC5.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeC5.getDefaultModelObjectAsString();
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=0){
						int sa = Integer.parseInt(getRangeC6T())-Integer.parseInt(text);
						if(sa<0){
							error("Largger than the previous data!!");
						}else{
							setRangeC5T(text);				
							text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+text+"-"+getRangeC6T()+"]";
							oneDelete();
							setFormat("RangeC",text);
							Textarea();
						}
					}else{
						error("Enter at least zero!!");
					}
				}else{
					setRangeC5T(text);				
					text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+text+"-"+getRangeC6T()+"]";
					oneDelete();
					setFormat("RangeC",text);
					Textarea();
				}
				
				target.addComponent(textarea);
				target.addComponent(rangeC5);
				target.addComponent(feedback);
			}
			
		});
//		rangeC5.setVisible(false);
		datacreateForm.add(rangeC5);
		
		rangeC6 = new RequiredTextField<Integer>("rangeC6", new PropertyModel<Integer>(this,"rangeC6T"),Integer.class);
		rangeC6.setOutputMarkupId(true);
		rangeC6.add(new MinimumValidator<Integer>(0));
		rangeC6.add(new MaximumValidator<Integer>(9));
		rangeC6.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeC6.getDefaultModelObjectAsString();
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=0){
						int sa = Integer.parseInt(text)-Integer.parseInt(getRangeC5T());
						if(sa<0){
							error("Smaller than the previous data!!");
						}else{
							setRangeC6T(text);			
							text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+text+"]";
							oneDelete();
							setFormat("RangeC",text);
							Textarea();
						}
					}else{
						error("Enter at least zero!!");
					}
				}else{
					setRangeC6T(text);			
					text ="["+getRangeC1T()+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+text+"]";
					oneDelete();
					setFormat("RangeC",text);
					Textarea();
				}
				
				target.addComponent(textarea);
				target.addComponent(rangeC6);
				target.addComponent(feedback);
			}
			
		});
//		rangeC6.setVisible(false);
		datacreateForm.add(rangeC6);
		
		rangeN1 = new RequiredTextField<Integer>("rangeN1", new PropertyModel<Integer>(this,"rangeN1T"),Integer.class);
		rangeN1.setOutputMarkupId(true);
		rangeN1.add(new MinimumValidator<Integer>(0));
		rangeN1.add(new MaximumValidator<Integer>(9));
		rangeN1.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeN1.getDefaultModelObjectAsString();
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=0){
						int sa = Integer.parseInt(getRangeN2T())-Integer.parseInt(text);
						if(sa<0){
							error("Largger than the previous data!!");
						}else{
							setRangeN1T(text);				
							text ="["+text+"-"+getRangeN2T()+"]";
							oneDelete();
							setFormat("RangeN",text);
							Textarea();
						}
					}else{
						error("Enter at least zero!!");
					}
				}
				
				target.addComponent(textarea);				
				target.addComponent(rangeN1);
				target.addComponent(feedback);
			}
			
		});		
//		rangeN1.setVisible(false);
		datacreateForm.add(rangeN1);
		
		rangeN2 = new RequiredTextField<Integer>("rangeN2", new PropertyModel<Integer>(this,"rangeN2T"),Integer.class);
		rangeN2.setOutputMarkupId(true);
		rangeN2.add(new MinimumValidator<Integer>(0));
		rangeN2.add(new MaximumValidator<Integer>(9));
		rangeN2.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = rangeN2.getDefaultModelObjectAsString();
				if(text != null && !text.equals("")){
					if((int)(text.charAt(0))>=0){
						int sa = Integer.parseInt(text)-Integer.parseInt(getRangeN1T());
						if(sa<0){
							error("Smaller than the previous data!!");
						}else{
							setRangeN2T(text);				
							text ="["+getRangeN1T()+"-"+text+"]";
							oneDelete();
							setFormat("RangeN",text);
							Textarea();
						}
					}else{
						error("Enter at least zero!!");
					}
				}
				
				target.addComponent(textarea);		
				target.addComponent(rangeN2);
				target.addComponent(feedback);
			}
			
		});
//		rangeN2.setVisible(false);
		datacreateForm.add(rangeN2);
		
		direct = new RequiredTextField<String>("direct", new PropertyModel<String>(this,"directT"));
		direct.setOutputMarkupId(true);
		direct.add(new StringValidator.ExactLengthValidator(1));
		direct.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = direct.getDefaultModelObjectAsString();
				setDirectT(text);
				
				oneDelete();
				setFormat("Direct",text);
				Textarea();
				
				target.addComponent(textarea);		
				target.addComponent(direct);
				target.addComponent(feedback);
			}
			
		});
//		direct.setVisible(false);		
		datacreateForm.add(direct);
		directNumber = new RequiredTextField<Integer>("directnumber", new PropertyModel<Integer>(this,"directN"),Integer.class);
		directNumber.setOutputMarkupId(true);
		directNumber.add(new MinimumValidator<Integer>(0));
		directNumber.add(new MaximumValidator<Integer>(9));
		directNumber.add(new OnChangeAjaxBehavior(){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				String text = directNumber.getDefaultModelObjectAsString();
				
				setRangeN2T(text);				
				text ="["+getRangeN1T()+"-"+text+"]";
				oneDelete();
				setFormat("Direct",text);
				Textarea();
								
				target.addComponent(textarea);		
				target.addComponent(directNumber);
				target.addComponent(feedback);
			}
			
		});
//		directNumber.setVisible(false);
		datacreateForm.add(directNumber);
		
		assignmentL = new Label("assignmentL","[");
		assignmentR = new Label("assignmentR","]");
		assignmentL.setOutputMarkupId(true);
		assignmentR.setOutputMarkupId(true);
		repeatL = new Label("repeatL","{");
		repeatR = new Label("repeatR","}");
		rangeCL = new Label("rangeCL","[");	
		rangeC1L = new Label("rangeC1L","/");
		rangeC_1 = new Label("rangeC_1","-");
		rangeC_2 = new Label("rangeC_2","-");
		rangeC2L = new Label("rangeC2L","/");
		rangeC_3 = new Label("rangeC_3","-");
		rangeCR = new Label("rangeCR","]");
		rangeNL = new Label("rangeNL","[");
	 	rangeNR = new Label("rangeNR","]");
	 	rangeN_1 = new Label("rangeN_1","-");
	 	directL = new Label("directL","[");
		directR = new Label("directR","]");
		
		datacreateForm.add(assignmentL);
		datacreateForm.add(assignmentR);
		datacreateForm.add(repeatL);
		datacreateForm.add(repeatR);
		datacreateForm.add(rangeCL);
		datacreateForm.add(rangeC1L);
		datacreateForm.add(rangeC_1);
		datacreateForm.add(rangeC_2);
		datacreateForm.add(rangeC2L);
		datacreateForm.add(rangeC_3);
		datacreateForm.add(rangeCR);
		datacreateForm.add(rangeNL);
		datacreateForm.add(rangeNR);
		datacreateForm.add(rangeN_1);
		datacreateForm.add(directL);
		datacreateForm.add(directR);
		setUnvisiable();
		
		//大文字のRandom
		datacreateForm.add(upperButton = new AjaxButton("upper",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				String upper = "[A-Z]";
				setFormat("upper", upper);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//小文字のRandom
		datacreateForm.add(lowerButton = new AjaxButton("lower",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				String lower = "[a-z]";
				setFormat("lower", lower);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//数字のRandom
		datacreateForm.add(figureButton = new AjaxButton("figure",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				String figure = "[0-9]";
				setFormat("figure", figure);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//大文字・数字のRandom
		datacreateForm.add(upperfigureButton = new AjaxButton("upperfigure",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				String upperfigure = "[A-Z/0-9]";
				setFormat("upperfigure", upperfigure);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//大文字・小文字のRandom
		datacreateForm.add(upperlowerButton = new AjaxButton("upperlower",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				String upperlower = "[A-Z/a-z]";
				setFormat("upperlower", upperlower);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//小文字・数字のRandom
		datacreateForm.add(lowerfigureButton = new AjaxButton("lowerfigure",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				String lowerfigure = "[a-z/0-9]";
				setFormat("lowerfigure", lowerfigure);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});		
		//大文字・小文字・数字のRandom
		datacreateForm.add(allcaseButton = new AjaxButton("allcase",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				String allcase = "[A-Z/a-z/0-9]";
				setFormat("allcase", allcase);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//文字の範囲指定
		datacreateForm.add( rangeCButton = new AjaxButton("Rangeofcharacters",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				setRangeC1T("A");
				setRangeC2T("Z");
				setRangeC3T("a");
				setRangeC4T("z");
				setRangeC5T("0");
				setRangeC6T("9");				
				setUnvisiable();
				setDataFormatVisiable(this.getId());
				
				String rangeC = "["+getRangeC1T()+"-"+getRangeC2T()+"/"+getRangeC3T()+"-"+getRangeC4T()+"/"+getRangeC5T()+"-"+getRangeC6T()+"]";
				setFormat("RangeC", rangeC);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//数字の範囲指定
		datacreateForm.add(rangeNButton = new AjaxButton("Rangeofnumbers",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				setRangeN1T("0");
				setRangeN2T("9");
				setUnvisiable();
				setDataFormatVisiable(this.getId());
				
				String rangeN = "["+getRangeN1T()+"-"+getRangeN2T()+"]";
				setFormat("RangeN",rangeN);
				Textarea();

				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//Randomの文字列指定
		datacreateForm.add(assignmentButton = new AjaxButton("Assignment",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				setUnvisiable();
				setDataFormatVisiable(this.getId());
				
				String assignment = "["+getAssignmentT()+"]";
				setFormat("Assignment",assignment);
				Textarea();
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});		
		//文字の繰り返し
		datacreateForm.add(repeatButton = new AjaxButton("Repeat",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				if(getIndex()!=0 && isRepeatB()== false){
					setIndex_before(0);
					setRepeatT("2");
					setUnvisiable();
					setDataFormatVisiable(this.getId());
					setRepeatB(true);
					setFormat("Repeat",getRepeatT());					
					Textarea();
					setIndex_before(Integer.parseInt(getRepeatT()));
				}else{
					error("Can't selectting Dataformat");
				}
				
				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});
		//直接入力
		datacreateForm.add( directButton = new AjaxButton("Directlyspecified",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				setRepeatB(false);
				setUnvisiable();
				setDataFormatVisiable(this.getId());

				String direct = upperButton.isVisible() ? getDirectT() : getDirectN();
				setFormat("Direct", direct);
				Textarea();

				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});		
		//1文字削除
		datacreateForm.add(new AjaxButton("CDelete",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				oneDelete();
				setUnvisiable();
				Textarea();

				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});		
		//全文字削除
		datacreateForm.add(new AjaxButton("ADelete",datacreateForm){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				allDelete();	
				setUnvisiable();
				Textarea();

				target.addComponent(form);
				target.addComponent(feedback);
			}
			
		});		
		
	}

	public String getAssignmentT() {
		return assignmentT;
	}

	public void setAssignmentT(String assignmentT) {
		this.assignmentT = assignmentT;
	}

	public String getRepeatT() {
		return repeatT;
	}

	public void setRepeatT(String repeatT) {
		this.repeatT = repeatT;
	}

	public String getRangeC1T() {
		return rangeC1T;
	}

	public void setRangeC1T(String rangeC1T) {
		this.rangeC1T = rangeC1T;
	}

	public String getRangeC2T() {
		return rangeC2T;
	}

	public void setRangeC2T(String rangeC2T) {
		this.rangeC2T = rangeC2T;
	}

	public String getRangeC3T() {
		return rangeC3T;
	}

	public void setRangeC3T(String rangeC3T) {
		this.rangeC3T = rangeC3T;
	}

	public String getRangeC4T() {
		return rangeC4T;
	}

	public void setRangeC4T(String rangeC4T) {
		this.rangeC4T = rangeC4T;
	}

	public String getRangeC5T() {
		return rangeC5T;
	}

	public void setRangeC5T(String rangeC5T) {
		this.rangeC5T = rangeC5T;
	}
	
	public String getRangeC6T() {
		return rangeC6T;
	}

	public void setRangeC6T(String rangeC6T) {
		this.rangeC6T = rangeC6T;
	}

	public String getRangeN1T() {
		return rangeN1T;
	}

	public void setRangeN1T(String rangeN1T) {
		this.rangeN1T = rangeN1T;
	}

	public String getRangeN2T() {
		return rangeN2T;
	}

	public void setRangeN2T(String rangeN2T) {
		this.rangeN2T = rangeN2T;
	}

	public String getDirectT() {
		return directT;
	}

	public void setDirectT(String directT) {
		this.directT = directT;
	}

	public void Textarea() {
		
		setTextareaT("");
		for(int i=0;i<this.length;i++){
			if(this.datatext[i]!= null){
				setTextareaT(getTextareaT() + datatext[i]);
			}else{
				break;
			}			
		}
		
	}
    public String getTextareaT(){
    	return this.textareaT;
    }
	public void setTextareaT(String textarea) {
		this.textareaT = textarea;
	}
	
	public void setFormat(String dataformat,String datatext){
		
		if(getIndex()<this.length){
			if(!dataformat.equals("Repeat")){
				this.dataformat[getIndex()] = dataformat;
				this.datatext[getIndex()] = datatext;
				setIndex(getIndex()+1);
			}else{
				int index = getIndex()-1;
				String r = getRepeatT();
				if(r.equals("")) r = "0";
				for(int i=0;i<Integer.parseInt(r)-1;i++){	
					if(getIndex()<this.length && Integer.parseInt(datatext)>1){
						this.dataformat[getIndex()] = this.dataformat[index]; 
						this.datatext[getIndex()] = this.datatext[index];
						setIndex(getIndex()+1);
					}
				}
				
			}
		}else{
			error("Over Datalength!!");
		}
		
	}
	public void oneDelete(){
		if(getIndex()>0){
			setIndex(getIndex()-1);
			this.dataformat[getIndex()] = "";
			this.datatext[getIndex()] = "";
			Textarea();
		}else{
			error("No delete Data!!");
		}
	}
	public void allDelete(){
		if(getIndex()>0){			
			for(int i=0;i<getIndex();i++){
				this.dataformat[i] = "";
				this.datatext[i] = "";
			}
			setTextareaT("");
			setIndex(0);
		}else{
			error("No delete Data!!");
		}		
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getLength() {
		return length;
	}
	public void setDataFormatVisiable(String dataformat){

		if(dataformat.equals("Assignment")){
			assignment.setVisible(true);
			assignmentL.setVisible(true);
			assignmentR.setVisible(true);
			
		}else if(dataformat.equals("Repeat")){
			repeat.setVisible(true);
			repeatL.setVisible(true);
			repeatR.setVisible(true);			
		}else if(dataformat.equals("Rangeofcharacters")){
			rangeC1.setVisible(true);
			rangeC2.setVisible(true);
			rangeC3.setVisible(true);
			rangeC4.setVisible(true);
			rangeC5.setVisible(true);
			rangeC6.setVisible(true);
			rangeCL.setVisible(true);
			rangeCR.setVisible(true);
			rangeC_1.setVisible(true);
			rangeC_2.setVisible(true);
			rangeC_3.setVisible(true);
			rangeC1L.setVisible(true);
			rangeC2L.setVisible(true);
		}else if(dataformat.equals("Rangeofnumbers")){
			rangeN1.setVisible(true);
			rangeN2.setVisible(true);
			rangeNL.setVisible(true);
			rangeNR.setVisible(true);
			rangeN_1.setVisible(true);
		}else{
			if(upperButton.isVisible()) direct.setVisible(true);
			else directNumber.setVisible(true);
			directL.setVisible(true);
			directR.setVisible(true);			
		}		
	}
	public void setUnvisiable(){
		assignment.setVisible(false);
		repeat.setVisible(false);
		rangeC1.setVisible(false);
		rangeC2.setVisible(false);
		rangeC3.setVisible(false);
		rangeC4.setVisible(false);
		rangeC5.setVisible(false);
		rangeC6.setVisible(false);
		rangeN1.setVisible(false);
		rangeN2.setVisible(false);
		direct.setVisible(false);
		directNumber.setVisible(false);
		
		assignmentL.setVisible(false);
		assignmentR.setVisible(false);		
		repeatL.setVisible(false);
		repeatR.setVisible(false);		
		rangeCL.setVisible(false);
		rangeCR.setVisible(false);
		rangeC_1.setVisible(false);
		rangeC_2.setVisible(false);
		rangeC_3.setVisible(false);
		rangeC1L.setVisible(false);
		rangeC2L.setVisible(false);	
		rangeNL.setVisible(false);
		rangeNR.setVisible(false);
		rangeN_1.setVisible(false);		
		directL.setVisible(false);
		directR.setVisible(false);	
	}
	public void setNumnervisiable(){
		assignmentButton.setVisible(false);
		upperButton.setVisible(false);
		lowerButton.setVisible(false);
		upperlowerButton.setVisible(false);
		upperfigureButton.setVisible(false);
		lowerfigureButton.setVisible(false);
		allcaseButton.setVisible(false);
		rangeCButton.setVisible(false);
		
	}
	public int getIndex_before() {
		return index_before;
	}
	
	public void setIndex_before(int index_before) {
		this.index_before = index_before;
	}

	public boolean isRepeatB() {
		return repeatB;
	}

	public void setRepeatB(boolean repeatB) {
		this.repeatB = repeatB;
	}

	public String getDirectN() {
		return directN;
	}

	public void setDirectN(String directN) {
		this.directN = directN;
	}

}
