package com.lalala.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lalala.pojo.Catalog;
import com.lalala.pojo.User;
import com.lalala.repository.CatalogRespority;
import com.lalala.service.CatalogService;
/*
 * catalog分类接口的实现类
 */
@Service
public class CatalogServiceImpl implements CatalogService{

	@Autowired
	private CatalogRespority catalogRespority;
	
	/**
	 * 保存分类
	 */
	@Override
	public Catalog saveCatalog(Catalog catalog) {
		//判断是否重复 通过findByUserAndName查询列表，
		List<Catalog> list=catalogRespority.findByUserAndName(catalog.getUser(), catalog.getName());
		if(list!=null&&list.size()>0) { //如果不等于0 或者size》0
			throw new IllegalArgumentException("该分类已经存在");
		}
		return catalogRespority.save(catalog);
	}

	/*
	 * 删除分类（非 Javadoc）
	 * @see com.lalala.service.CatalogService#removeCatalog(java.lang.Long)
	 */
	@Override
	public void removeCatalog(Long id) {
		catalogRespority.delete(id);
	}

	/**
	 * 查找分类id
	 */
	@Override
	public Catalog getCatalogById(Long id) {
		
		return catalogRespority.findOne(id);
	}

	/*
	 * 显示分类（非 Javadoc）
	 * @see com.lalala.service.CatalogService#listCatalogs(com.lalala.pojo.User)
	 */
	@Override
	public List<Catalog> listCatalogs(User user) {
		
		return catalogRespority.findByUser(user);
	}

}
