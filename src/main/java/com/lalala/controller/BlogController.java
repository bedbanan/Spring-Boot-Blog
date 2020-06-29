package com.lalala.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lalala.pojo.User;
import com.lalala.pojo.es.EsBlog;
import com.lalala.service.EsBlogService;
import com.lalala.vo.TagVO;

/*
 * Blog控制器
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
	
	@Autowired
	private EsBlogService esBlogService;
     /*
      * @RequestParam(value="order",required=false,defaultValue="new"排序规则根据最新来排序
      * @RequestParam(value="keyword",required=false,defaultValue="")String keyword 根据什么关键词来搜索
      */
	@GetMapping
	public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false)boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,Model model){
		Page<EsBlog> page=null;
		List<EsBlog> list=null;
		boolean isEmpty=true; //系统初始化,没有博客数据
		try {
			if(order.equals("hot")) {  //最热查询
				//按照阅读量，评论量，点赞量，创建时间来进行排序，倒叙排序最热在第一
				Sort sort=new Sort(Direction.DESC,"readSize","commentSize","voteSize","createTime");
				Pageable pageable=new PageRequest(pageIndex, pageSize,sort);
				page=esBlogService.listHotestEsBlogs(keyword, pageable);
			}else if(order.equals("new")){  //最新查询
				Sort sort=new Sort(Direction.DESC,"createTime");  //按照创建时间排序
				Pageable pageable=new PageRequest(pageIndex, pageSize,sort);
				page=esBlogService.listNewestEsBlogs(keyword, pageable);
			}
			isEmpty=false;
		} catch (Exception e) {
			Pageable pageable=new PageRequest(pageIndex,pageSize);
			page=esBlogService.listEsBlogs(pageable);
		} 
		list=page.getContent(); //当前所在数据列表
		
		model.addAttribute("order",order);
		model.addAttribute("keyword",keyword);
		model.addAttribute("page",page);
		model.addAttribute("blogList",list);
		
		//首次访问页面才加载
        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlog();
            model.addAttribute("newest", newest);
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlog();
            model.addAttribute("hotest", hotest);
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("users", users);
        }

        return (async==true?"/index :: #mainContainerRepleace":"/index");
	}
}
