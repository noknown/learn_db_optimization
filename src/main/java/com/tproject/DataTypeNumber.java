package com.tproject;

enum DataTypeNumber{
	SEQUENCE("sequence","Sequence"),FIGURE("figure","[0-9]"),FREE("free","Free");
	private String id;
	private String type;
	private DataTypeNumber (String id, String type){
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