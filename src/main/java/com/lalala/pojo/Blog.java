package com.lalala.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.rjeschke.txtmark.Processor;


@Entity //实体
public class Blog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -242735909070098804L;

	@Id //主键
	@GeneratedValue(strategy=GenerationType.IDENTITY) //自增
	private Long id;
    
	@NotEmpty(message="标题不能为空")
	@Size(min=2,max=50)
	@Column(nullable=false ,length=50)  //映射为字段，且不能为空
	private String title;
	
	@NotEmpty(message="摘要不能为空")
	@Size(min=2,max=300)
	@Column(nullable=false)  //映射为字段，且不能为空
    private String summary;
	
	@Lob  //大对象,映射 Mysql的Long Text类型
	@Basic(fetch=FetchType.LAZY) //懒加载
	@NotEmpty(message="内容不能为空")
	@Size(min=2)
	@Column(nullable=false) //映射为字段，值不为空
	private String content;
	
	@Lob //大对象,映射MYSQL的Long Text类型
	@Basic(fetch=FetchType.LAZY) //懒加载
	@NotEmpty(message="内容不能为空")
	@Size(min=2)
	@Column(nullable=false)  //映射为字段，值不为空
	private String htmlContent; //将md转换为HTML
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable=false) //映射为字段且不能为空
	@CreationTimestamp  //由数据库自动创建时间
	private Timestamp createTime;
	
	@Column(name="readSize")
	private Integer readSize=0;  //访问量，阅读量
	
	@Column(name="commentSize")
	private Integer commentSize = 0;  // 评论量
	
	@Column(name="voteSize")
	private Integer voteSize=0; //点赞量
	
	@Column(name="tags",length=100)
	private String tags; //标签“a,b,c”
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER) //一篇博客有对种评论  一对多的关系
	@JoinTable(name="blog_comment",joinColumns=@JoinColumn(name="blog_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="Comment_id",referencedColumnName="id"))
	private List<Comment> comments;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	//用列表来保存votes  因为有很多点赞
	@JoinTable(name="blog_vote",joinColumns=@JoinColumn(name="blog_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="vote_id",referencedColumnName="id"))
	private List<Vote> votes;
	
	@OneToOne(cascade=CascadeType.DETACH,fetch=FetchType.LAZY)
	@JoinColumn(name="catalog_id")
	private Catalog catalog;

	
	protected Blog() {
		
	}

	public Blog(String title, String summary, String content) {
		this.title = title;
		this.summary = summary;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.htmlContent=Processor.process(content);  //将Markdown 内容转换为HTML格式
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getReadSize() {
		return readSize;
	}

	public void setReadSize(Integer readSize) {
		this.readSize = readSize;
	}

	public Integer getVoteSize() {
		return voteSize;
	}

	public void setVoteSize(Integer voteSize) {
		this.voteSize = voteSize;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getCommentSize() {
		return commentSize;
	}

	public void setCommentSize(Integer commentSize) {
		this.commentSize = commentSize;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
		this.commentSize=this.comments.size();
	}
	
	/*
	 * 添加评论
	 */
	public void addComment(Comment comment) {
		this.comments.add(comment);
		this.commentSize=this.comments.size(); //同时要修改评论量
	}
	
	/*
	 * 删除评论
	 */
	public void removeComment(Long commentId) {
		for(int index=0;index<this.comments.size();index++) {
		  if(comments.get(index).getId()==commentId) {	
			this.comments.remove(index); //list自带的remove方法
			break;
		  }
		}
		this.commentSize=this.comments.size();
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
		this.voteSize=this.votes.size();
	}
	
	/*
	 * 点赞评论
	 */
	public boolean addVote(Vote vote) {
		boolean isExist=false;   //先是预设不重复
		//判断是否重复
		for(int index=0; index<this.votes.size();index++) {
			if(this.votes.get(index).getUser().getId()==vote.getUser().getId()) {
				isExist=true;
				break;
			}
		}
		if(!isExist) {
			this.votes.add(vote);
			this.voteSize=this.votes.size();
		}
		return isExist;
	}
	
	/*
	 * 取消点赞
	 */
	public void removeVote(Long voteId) {   //将传进来的ID值与数据库中的值对匹配 
		for(int index=0;index<this.votes.size();index++) {
			if(this.votes.get(index).getId()==voteId) {//将传进来的ID值与数据库中的值对匹配 ,正确则取消点赞
				this.votes.remove(index);
				break;
			}
		}
		this.voteSize=this.votes.size();
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
}
