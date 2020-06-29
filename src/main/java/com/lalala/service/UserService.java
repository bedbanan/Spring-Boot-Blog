package com.lalala.service;
/*
 * 用户服务接口
 */

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lalala.pojo.User;

public interface UserService {
     /*
      * 新增，编辑，保存用户
      */
	User saveOrUpdateUser(User user);
	
	/*
	 * 注册用户
	 */
	User registerUser(User user);
	/*
	 * 删除用户
	 */
	void removeUser(Long id);
	
	/*
	 * 根据ID获取用户
	 */
	/**
	 * 
	 */
	User getUserById(Long id);
	
	/*
	 * 根据用户名进行分页的模糊查询
	 */
	Page<User> listUsersByNameLike(String name,Pageable pageable);
	
	/*
	 * 根据用户名集合，查询用户详细信息列表
	 */
	List<User> listUserByUsernames(Collection<String> usernames);
	
	
}
