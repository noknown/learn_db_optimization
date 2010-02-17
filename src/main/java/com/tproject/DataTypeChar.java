package com.tproject;

enum DataTypeChar{
	UPPER("uppercase","[A-Z]"),LOWER("lowercase","[a-z]"),
	FIGURE("figure","[0-9]"),UPPERLOWER("upperlower","[A-Z/a-z]"),
	UPPERFIGURE("upperfigure","[A-Z/0-9]"),LOWERFIGURE("lowerfigure","[a-z/0-9]"),
	ALLCASE("allcase","[A-Z/a-z/0-9]"),ADDRESS("address","Address"),
	MAILADDRESS("mailaddress","MailAddress"),NAME("name","Name"),
	TELEPHONENUMBER("telephonenumber","Telephonenumber"),FREE("free","Free");
	private String id;
	private String type;
	private DataTypeChar (String id, String type){
		this.setId(id);
		this.setType(type);		
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}

}
