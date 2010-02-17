package com.tproject;

enum DataFormatN{
	FIGURE("figure","R"),FREE("free","F");
	private String id;
	private String type;
	private DataFormatN(String id, String type){
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