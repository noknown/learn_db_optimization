package com.tproject;

enum DataFormatC{
	UPPER("upper","A"),LOWER("lower","a"),
	FIGURE("figure","1"),FREE("free","F");
	private String id;
	private String type;
	private DataFormatC(String id, String type){
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