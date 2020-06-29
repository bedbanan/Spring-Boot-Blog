package com.lalala.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lalala.pojo.Blog;
import com.lalala.pojo.Catalog;
import com.lalala.pojo.User;

public interface BlogService {
	/*
	 * 保存博客
	 */
	Blog saveBlog(Blog blog);
    
	/*
	 * 删除博客
	 */
	void removeBlog(Long id);
	
	/*
	 * 根据Id获取Blog
	 */
	Blog getBlogById(Long id);
	
	/*
	 * 根据用户进行博客名称分页的模糊查询，按最新排序
	 */
	Page<Blog> listBlogsByTitleVote(User user,String title,Pageable pageable);
	
	/* 
	 * 根据用户进行博客名称分页的模糊查询。按最热排序
	 */
	Page<Blog> listBlogsByTitleVoteAndSort(User user,String title,Pageable pageable);
	
	/*
	 * 阅读递增量
	 */
	void readingIncrease(Long id);
	
	/*
	 * 发表创建评论
	 */
	Blog createComment(Long blogId,String commentContent);
	
	/*
	 * 删除评论
	 */
	void removeComment(Long blogId,Long commentId);
	
	/*
	 * 创建点赞
	 */
	Blog createVote(Long blogId);
	
	/*
	 * 取消点赞
	 */
	void removeVote(Long blogId,Long voteId);
	
	/*
	 * 根据分类查询
	 */
	Page<Blog> listBlogsByCatalog(Catalog catalog,Pageable pageable); 
}
