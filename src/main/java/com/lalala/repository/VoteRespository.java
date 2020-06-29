package com.lalala.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lalala.pojo.Vote;
/*
 * Vote Respority接口
 */
public interface VoteRespository extends JpaRepository<Vote,Long>{

}
