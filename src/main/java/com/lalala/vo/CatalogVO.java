package com.lalala.vo;

import java.io.Serializable;

import com.lalala.pojo.Catalog;

public class CatalogVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8540942643150011639L;
	private String username;
	private Catalog catalog;
	
	public CatalogVO() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	

}
