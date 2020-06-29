package com.lalala.service.impl;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lalala.pojo.User;
import com.lalala.repository.UserRepository;
import com.lalala.service.UserService;
@Service
public class UserServiceImpl implements UserService,UserDetailsService{
	@Autowired
    private UserRepository userRepository;
	
	@Transactional   //事务处理注解,完成并回滚
	@Override
	public User saveOrUpdateUser(User user) {  //保存或更新对象
		
		return userRepository.save(user);
	}
	@Transactional
	@Override
	public User registerUser(User user) {   //注册对象 实际就是保存对象
		
		return userRepository.save(user);
	}
	@Transactional
	@Override
	public void removeUser(Long id) {   //删除用户
		userRepository.delete(id);
		
	}

	@Override
	public User getUserById(Long id) {  //通过id查询用户
		
		return userRepository.findOne(id);
	}

	@Override
	public Page<User> listUsersByNameLike(String name, Pageable pageable) {  //分页模糊查询
		name="%"+name+"%";  //模糊查询
		Page<User> users=userRepository.findByNameLike(name, pageable); //根据用户姓名分页查询用户列表
		return users;
	}
	/*
	 * 根据用户账号来加载认证信息（非 Javadoc）
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUsername(username);
	}
	
	/*
	 * 根据用户名集合，查询用户详细信息列表
	 */
	@Override
	public List<User> listUserByUsernames(Collection<String> usernames) {
		
		return userRepository.findByUsernameIn(usernames);
	}
	
	


}
