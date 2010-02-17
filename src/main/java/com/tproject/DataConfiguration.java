package com.tproject;

enum DataConfiguration{
	UPPERCASE("uppercase","A-Z(Random)"),LOWER("lowercase","a-z(Random)"),
	FIGURE("figure","0-9(Random)");
	private String id;
	private String type;
	private DataConfiguration(String id, String type){
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

