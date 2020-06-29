package com.lalala.service;
/**
 * vote Service接口
 * @author hasee
 *
 */

import com.lalala.pojo.Vote;

public interface VoteService {
	
	/**
	 * 根据ID来获取点赞服务对象
	 */
	Vote getVoteById(Long id);
	
	/**
	 * 删除Vote
	 */
	void removeVote(Long id);

}
