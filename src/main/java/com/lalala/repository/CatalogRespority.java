package com.lalala.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lalala.pojo.Catalog;
import com.lalala.pojo.User;

/**
 * Catalog的respority
 * @author hasee
 *
 */
public interface CatalogRespority extends JpaRepository<Catalog, Long>{
	
	/**
	 * 根据用户来查询
	 */
	List<Catalog> findByUser(User user);
	
	/*
	 * 根据用户以及分类名称来进行查询
	 */
	List<Catalog> findByUserAndName(User user,String name);

}
