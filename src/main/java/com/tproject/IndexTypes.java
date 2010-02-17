package com.tproject;

/**
 * 索引のタイプを表すenum
 * @author no_known
 *
 */
public enum IndexTypes {
	NONE("NONE"), NORMAL("NORMAL"), BITMAP("BITMAP"), REVERSE("NORMAL/REV");
	private final String name;
	
	private IndexTypes(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
