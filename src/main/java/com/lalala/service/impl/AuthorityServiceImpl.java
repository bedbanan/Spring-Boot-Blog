package com.lalala.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lalala.pojo.Authority;
import com.lalala.repository.AuthorityRepository;
import com.lalala.service.AuthorityService;

/*
 * AuthorityServiceImpl接口的实现
 */
@Service
public class AuthorityServiceImpl implements AuthorityService{
    /*
     * 作为Bean标签自动注入 数据库操作s
     */
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Override
	public Authority getAuthorityById(Long id) {
		
		return authorityRepository.findOne(id);  //根据ID查找数据
	}

}
