package com.lalala.vo;
/*
 * 后台管理菜单 
 */
public class Menu {
	/**
	 * 
	 */
	private String name;   //名字
	private String url;   //url
	
	public Menu(String name, String url) {
		this.name = name;
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
