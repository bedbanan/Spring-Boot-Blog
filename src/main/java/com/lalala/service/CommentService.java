package com.lalala.service;
/**
 * 评论service接口，
 * @author hasee
 *
 */

import com.lalala.pojo.Comment;

public interface CommentService {
	
	/**
	 * 根据Id来获取comment
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);

	/**
	 * 删除评论根据id来删除
	 */
	void removeComment(Long id);
	
	
}
