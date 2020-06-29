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
/*
 * 点赞实体类
 */
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
@Entity
public class Vote implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7808195973066805283L;

	@Id//主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) //自增长
	private Long id;  //用户唯一标识
	
	@OneToOne(cascade=CascadeType.DETACH,fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable=false) //映射字段不能为空
	@CreationTimestamp//数据库自动创建时间
	private Timestamp createTime; //点赞时间
	
	protected Vote() {
		
	}
	
	public Vote(User user) {
		this.user=user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
