package com.tproject;

import java.io.Serializable;

/**
 * ドロップダウンチョイスなどの選択用モデル
 * @author no_known
 *
 */
public class ChoiceElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3900458801210533697L;

	private final String id;
	private final String name;
	
	public ChoiceElement(String id, String name) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}
