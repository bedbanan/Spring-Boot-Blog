package com.lalala.service;

import java.util.List;

import com.lalala.pojo.Catalog;
import com.lalala.pojo.User;

/*
 * Catalog的service类
 */

public interface CatalogService {
	/**
	 * 保存分类
	 * @param catalog
	 * @return
	 */
	Catalog saveCatalog(Catalog catalog);
	
	/*
	 * 删除分裂
	 */
	void removeCatalog(Long id);
	
	/*
	 * 根据id获取分类
	 */
	Catalog getCatalogById(Long id);
	
	/*
	 * 获取分类列表
	 */
	List<Catalog> listCatalogs(User user);

}
