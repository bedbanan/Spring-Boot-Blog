package com.lalala.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lalala.pojo.Comment;
/*
 * Comment 仓库 
 */
public interface CommentRespository  extends JpaRepository<Comment, Long>{
    
	
}
