package com.lalala.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lalala.pojo.Comment;
import com.lalala.repository.CommentRespository;
import com.lalala.service.CommentService;
/**
 * comment service的实现类
 * @author hasee
 *
 */
@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRespository commentRespository;
	
	/**
	 * 通过ID获取评论内容
	 */
	
	@Override
	public Comment getCommentById(Long id) {
		
		return commentRespository.findOne(id);
	}

	/**
	 * 根据id删除评论内容
	 */
	@Override
	public void removeComment(Long id) {
		commentRespository.delete(id);
		
	}

}
