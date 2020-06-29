package com.lalala.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lalala.pojo.Vote;
import com.lalala.repository.VoteRespository;
import com.lalala.service.VoteService;
/**
 * voteService接口的实现
 * @author hasee
 *
 */

@Service
public class VoteServiceImpl implements VoteService{
	
	@Autowired
	private VoteRespository voteRespository;

	/**
	 * 通过id获取点赞用户名
	 * @param id
	 * @return
	 */
	@Override
	public Vote getVoteById(Long id) {
		return voteRespository.findOne(id);
	}

	/**
	 * 取消点赞
	 * @param id
	 */
	@Override
	public void removeVote(Long id) {
		voteRespository.delete(id);
	}

}
