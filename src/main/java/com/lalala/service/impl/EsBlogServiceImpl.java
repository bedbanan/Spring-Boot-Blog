package com.lalala.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.lalala.pojo.User;
import com.lalala.pojo.es.EsBlog;
import com.lalala.repository.es.EsBlogRespository;
import com.lalala.service.EsBlogService;
import com.lalala.service.UserService;
import com.lalala.vo.TagVO;


/**
 * EsBlog 服务.
 * 
 * @since 1.0.0 2017年4月12日
 * @author <a href="https://waylau.com">Way Lau</a>
 */
@Service
public class EsBlogServiceImpl implements EsBlogService {
	@Autowired
	private EsBlogRespository esBlogRepository;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private UserService userService;
	
	private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
	private static final String EMPTY_KEYWORD = "";
	
	/*
	 * 删除ESBlog
	 */
	@Override
	public void removeESBlog(String id) {
		esBlogRepository.delete(id);
		
	}
	
	/**
	 * 更新EsBlog内容
	 */
	@Override
	public EsBlog updateEsBlog(EsBlog esBlog) {
		
		return esBlogRepository.save(esBlog);
	}
	
	/**
	 * 根据ID获取ESBlog内容
	 */
	@Override
	public EsBlog getEsBlogByBlogId(Long blogId) {
		
		return esBlogRepository.findByBlogId(blogId);
	}
	
	/**
	 * 按照最新顺序排序查询内容
	 */
	@Override
	public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) throws SearchParseException{
		Page<EsBlog> pages = null;
		Sort sort = new Sort(Direction.DESC,"createTime");  //根据创建时间来排序
		if (pageable.getSort() == null) {  //判断是否为空
			pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		}

		//查重
		pages = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword,keyword,keyword,keyword, pageable);
 
		return pages;
	}
	
	/**
	 * 按照最热顺序排序
	 * keyword 关键字  pageable多少篇
	 */
	@Override
	public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) throws SearchParseException{
         
		//按照阅读量，评论量，点赞量以及创建时间排序
		Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize","createTime"); 
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		}
        //查重
		return esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
	}
	
	/**
	 * 分页显示
	 */
	@Override
	public Page<EsBlog> listEsBlogs(Pageable pageable) {
		return esBlogRepository.findAll(pageable);
	}
	
	/**
	 * 按照最新的5篇博客
	 */
	@Override
	public List<EsBlog> listTop5NewestEsBlog() {
		Page<EsBlog> page = this.listNewestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
		//EMPTY_KEYWORD 关键字设置为空  不需要关心
		return page.getContent();
	}
	
	/**
	 * 最热的5篇内容
	 */
	@Override
	public List<EsBlog> listTop5HotestEsBlog() {
		Page<EsBlog> page = this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
		//只需要查询5篇
		return page.getContent();
	}
	
	/**
	 * 前30个标签
	 */
	@Override
	public List<TagVO> listTop30Tags() {
		List<TagVO> list=new ArrayList<>();
		//用原生的查询方式
		//聚合
		SearchQuery searchQuery= new NativeSearchQueryBuilder().withQuery(matchAllQuery())
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("blog").withTypes("blog")
				.addAggregation(terms("tags").field("tags").order(Terms.Order.count(false)).size(30)).build();
		//统计
		Aggregations aggregations=elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations(); //获取总数
			}
		
		});
		StringTerms modelTerms=(StringTerms)aggregations.asMap().get("tags");
		Iterator<Bucket> modelBucketIt=modelTerms.getBuckets().iterator();
		while (modelBucketIt.hasNext()) {   //遍历
			Bucket actiontypeBucket=modelBucketIt.next();
			list.add(new TagVO(actiontypeBucket.getKey().toString(), actiontypeBucket.getDocCount()));
			//将查询出来的标签的值放入TagVo中， key对应名称，和docCount数量
		}
		return list;
	}
	
	/**
	 * 最热的前12名 与上面类似
	 */
	@Override
	public List<User> listTop12Users() {
		List<String> usernamelist = new ArrayList<>();
		// given  用SearchQuery原生查询
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchAllQuery())
				.withSearchType(SearchType.QUERY_THEN_FETCH)
				.withIndices("blog").withTypes("blog")
				.addAggregation(terms("users").field("username").order(Terms.Order.count(false)).size(12))
				.build();
		// when
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();  //统计数量
			}
		});
		
		StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("users"); 
	        
        Iterator<Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = modelBucketIt.next();
            String username = actiontypeBucket.getKey().toString();
            usernamelist.add(username);
        }
        
        // 根据用户名，查出用户的详细信息
        List<User> list = userService.listUserByUsernames(usernamelist);
        
		return list;
	}
	
	
}
