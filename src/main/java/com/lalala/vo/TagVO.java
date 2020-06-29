package com.lalala.vo;

import java.io.Serializable;

/**
 * Tag 标签的对象
 * 把标签的热门信息传递到首页
 * @author hasee
 *
 */
public class TagVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6694723930807660737L;
	private String name;   //名字
	private Long count;   //数量
	
	public TagVO(String name,Long count) {
		this.name=name;
		this.count=count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
