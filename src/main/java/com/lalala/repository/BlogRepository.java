package com.lalala.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lalala.pojo.Blog;
import com.lalala.pojo.Catalog;
import com.lalala.pojo.User;

public interface BlogRepository extends JpaRepository<Blog, Long>{

	
	/**
	 * 根据用户名。博客标题分页查询博客列表
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByUserAndTitleLike(User user,String title,Pageable pageable);
	
	/**
	 * 根据用户名，博客查询博客列表(时间逆序)
	 * @param title
	 * @param user
	 * @param tags
	 * @param user2
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title,User user,String tags,User user2,Pageable pageable);
	
	
	/**
	 * 根据分类来查询博客列表
	 */
	Page<Blog> findByCatalog(Catalog catalog,Pageable pageable);
}
