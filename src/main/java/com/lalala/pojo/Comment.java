package com.lalala.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

@Entity //实体类
public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4048917751080454882L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //自增
	private Long id;   //ID
	
	@NotEmpty(message="评论不能为空!")
	@Size(min=2,max=500)
	@Column(nullable=false)  //映射字段不能为空
	private String content;   //评论内容
	
	@OneToOne(cascade=CascadeType.DETACH,fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;   //用户
	
	@Column(nullable=false) //不能为空
	@CreationTimestamp  //数据库自动创建时间
	private Timestamp createTime;  //评论时间
	
	protected Comment() {
		
	}

	public Comment(User user,String content) {
		
		this.content = content;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	

}
