package com.lalala.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lalala.pojo.Authority;
/*
 * AuthorityRepository 权限的仓库
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long>{
      
}
