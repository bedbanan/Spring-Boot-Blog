package com.lalala.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lalala.pojo.Blog;
import com.lalala.pojo.Catalog;
import com.lalala.pojo.Comment;
import com.lalala.pojo.User;
import com.lalala.pojo.Vote;
import com.lalala.pojo.es.EsBlog;
import com.lalala.repository.BlogRepository;
import com.lalala.service.BlogService;
import com.lalala.service.EsBlogService;
/*
 * Blog服务接口的实现
 */
@Service
public class BlogServiceImpl implements BlogService{

	@Autowired
	private BlogRepository blogRepository;
	@Autowired
	private EsBlogService esBlogService;
	
	/*
	 * 保存博客 除了存储在关系型数据库，还需要把博客存储到esblog非关系数据库中
	 */
	@Transactional //事务处理注解，在出现异常的情况下，保证数据的一致性；数据提交操作回滚至异常发生前的状态，既回滚
	@Override
	public Blog saveBlog(Blog blog) {
		boolean isNew=(blog.getId()==null);  //看传进来的blog对象是新建还是更新
		EsBlog esBlog=null;
		
		Blog returnBlog=blogRepository.save(blog); //存储进关系型数据库mysql中
		if(isNew) {  //如果isnew为true 
			esBlog=new EsBlog(returnBlog); //则新建一个
		}else {  //更新
			esBlog=esBlogService.getEsBlogByBlogId(blog.getId()); //要找到esblog的id
			esBlog.update(returnBlog); //然后再更新
		}
		esBlogService.updateEsBlog(esBlog);
		return returnBlog ;
	}

	/*
	 * 删除博客
	 */
	@Transactional
	@Override
	public void removeBlog(Long id) {   //id是博客实体的id
		blogRepository.delete(id);
		EsBlog esBlog=esBlogService.getEsBlogByBlogId(id); //查找esblog的id
		esBlogService.removeESBlog(esBlog.getId());
	}
	
	/*
	 * 根据Id获取Blog
	 */
	@Override
	public Blog getBlogById(Long id) {
		
		return blogRepository.findOne(id);
	}

	/*
	 * 根据用户进行博客名称分页的模糊查询，按最新排序
	 */
	@Override
	public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
		title="%"+title+"%";
		String tags=title;
		Page<Blog> blogs=blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user, tags, user, pageable);
		return blogs;
	}

	/*
	 * 根据用户进行博客名称分页的模糊查询。按最热排序
	 */
	@Override
	public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
		title="%"+title+"%";
		Page<Blog> blogs=blogRepository.findByUserAndTitleLike(user, title, pageable);
		return blogs;
	}

	/*
	 * 阅读递增量
	 */
	
	@Override
	public void readingIncrease(Long id) {
		Blog blog=blogRepository.findOne(id);   
		blog.setReadSize(blog.getReadSize()+1);   //在博客原有的阅读量的基础上递增+1
		this.saveBlog(blog);
		
	}

	/*
	 * 创建评论（非 Javadoc）
	 * @see com.lalala.service.BlogService#createComment(java.lang.Long, java.lang.String)
	 */
	@Override
	public Blog createComment(Long blogId, String commentContent) {  //创建评论
		Blog originalBlog=blogRepository.findOne(blogId);  //查找博客id
		User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //安全验证
		Comment comment=new Comment(user,commentContent); 
		originalBlog.addComment(comment); //添加评论
		return this.saveBlog(originalBlog); //保存博客
	}

	/**
	 * 删除评论
	 */
	@Override
	public void removeComment(Long blogId, Long commentId) {
		Blog originalBlog=blogRepository.findOne(blogId);
		originalBlog.removeComment(commentId);
		this.saveBlog(originalBlog);
		
		
	}

	/**
	 * 创建点赞
	 */
	@Override
	public Blog createVote(Long blogId) {
		Blog originalBlog=blogRepository.findOne(blogId); //从blog数据库中把对象取出来
		//安全验证user getAuthentication认证信息  getPrincipal获取验证准则
		User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
	    Vote vote=new Vote(user);
	    boolean isExist=originalBlog.addVote(vote);
	    if(isExist) {  //如果用户点了赞
	    	throw new IllegalArgumentException("该用户已经点过赞了！"); //抛出异常
	    }
	    return this.saveBlog(originalBlog); 
	}

	/**
	 * 取消点赞
	 */
	@Override
	public void removeVote(Long blogId, Long voteId) {
		Blog originalBlog=blogRepository.findOne(blogId);  //查找博客id
		originalBlog.removeVote(voteId); //根据博客id 验证然后取消点赞
		this.saveBlog(originalBlog);
		
		
	}
	/*
	 * 根据分类查询
	 */
	@Override
	public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
		Page<Blog> blogs=blogRepository.findByCatalog(catalog, pageable); //根据blogrepository的分类查询
		return blogs;
	}

}
