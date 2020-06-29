package com.lalala.repository;
/*
 * 建立一个用户资源库  一个用户的Repository接口
 */


import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lalala.pojo.User;
/*
 * JpaRepository可以对数据进行分页查询以及排序
 */
public interface UserRepository extends JpaRepository<User, Long>{ //会有提供默认的实现类
     /*
      * 根据用户姓名分页查询用户列表
      * name
      * pageable
      */
	Page<User> findByNameLike(String name,Pageable pageable);
	
	/*
	 * 根据用户账号查询用户
	 * username
	 */
	User findByUsername(String username);

	/*
	 * 根据名称列表查询用户
	 */
	List<User> findByUsernameIn(Collection<String> usernames);
	
	
}
