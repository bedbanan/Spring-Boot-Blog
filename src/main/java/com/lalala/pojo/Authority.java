package com.lalala.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/*
 * 用户权限实体接口
 */
@Entity
public class Authority implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4446298373491602147L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 值自增

	private Long id; // 权限id

	@Column(nullable = false) // 映射为字段且不能为空
	private String name;// 权限名称

	@Override
	public String getAuthority() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
