package com.lalala.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.lalala.pojo.es.EsBlog;
/*
 * EsBlog的文档类
 */
public interface EsBlogRespository extends ElasticsearchRepository<EsBlog, String>{
	/*
	 * 模糊查询(去重)
	 */
	Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,String Summary,String content,String tags,Pageable pageable);

	/*
	 * 根据blog的id来进行查询
	 */
	EsBlog findByBlogId(Long blogId);
}
