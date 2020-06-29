package com.lalala.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//分类实体类
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
@Entity //实体
public class Catalog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 724100121733614622L;

	@Id //主键
	@GeneratedValue(strategy=GenerationType.IDENTITY)// 自增长策略
	private Long id; //唯一标识
	
	@NotEmpty(message="名字不能为空")
	@Size(min=2,max=30)
	@Column(nullable=false)  //映射字段为空，值不能为空
	private String name;
	
	@OneToOne(cascade=CascadeType.DETACH,fetch=FetchType.LAZY) //一对一
	@JoinColumn(name="user_id")
	private User user;
	
	protected Catalog() {
		
	}
	public Catalog(User user, String name) {
		this.name = name;
		this.user = user;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

}
