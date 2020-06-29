package com.lalala.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lalala.pojo.User;
import com.lalala.pojo.es.EsBlog;
import com.lalala.vo.TagVO;

/**
 * EsBlog的服务
 * @author hasee
 *
 */
public interface EsBlogService {
	
	/*
	 * 删除EsBlog
	 */
	void removeESBlog(String id);
	
	/*
	 * 更新EsBlog
	 */
	EsBlog updateEsBlog(EsBlog esBlog); 
	
	/*
	 * 根据EsBlog的ID获取EsBlog
	 */
	EsBlog getEsBlogByBlogId(Long blogId);
	
	/*
	 * 最新博客列表,分页
	 */
	Page<EsBlog> listNewestEsBlogs(String keyword,Pageable pageable);
	
	/*
	 * 最热的博客列表，分页
	 */
	Page<EsBlog> listHotestEsBlogs(String keyword,Pageable pageable);
	
	/*
	 * 博客列表的分页
	 */
	Page<EsBlog> listEsBlogs(Pageable pageable);
	
	/*
	 * 最新的前五
	 */
	List<EsBlog> listTop5NewestEsBlog();
	
	/*
	 * 最热的前五篇
	 */
	List<EsBlog> listTop5HotestEsBlog();

	/*
	 * 最热的前30的标签
	 */
	List<TagVO> listTop30Tags();
	
	/*
	 * 最热前12用户
	 */
	List<User> listTop12Users();
}
