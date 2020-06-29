package com.lalala.service;
/*
 * Authority 服务接口
 */

import com.lalala.pojo.Authority;

public interface AuthorityService  {
	/*
	 * 根据Id查权限
	 */
      Authority getAuthorityById(Long id); 
}
