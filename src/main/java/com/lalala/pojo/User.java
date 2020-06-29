package com.lalala.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity // 标识user是一个实体
public class User implements UserDetails { // spring security要求实现UserDetails的方法
	/**
	 * 
	 */
	private static final long serialVersionUID = 4659535982337129350L;

	@Id // 标识id是主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 根据数据库自增
	private Long id; // 账号的唯一标识

	@NotEmpty(message = "姓名不能为空")
	@Size(min = 2, max = 20) // 设置长度的大小
	@Column(nullable = false, length = 20) // 映射为字段，且不能为空
	private String name;

	@NotEmpty(message = "邮箱不能为空")
	@Size(max = 50)
	@Email(message = "邮箱格式不对")
	private String email;

	@NotEmpty(message = "账号不能为空")
	@Size(min = 3, max = 20)
	@Column(nullable = false, length = 20, unique = true)
	private String username; // 用户账号,账号登录的唯一标识

	@NotEmpty(message = "密码不能为空")
	@Size(max = 100)
	@Column(length = 100)
	private String password; // 用户密码

	@Column(length = 200)
	private String avatar; // 用户头像信息

	@ManyToMany(cascade=CascadeType.DETACH,fetch=FetchType.EAGER)
	@JoinTable(name="user_authority",joinColumns=@JoinColumn(name="user_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="authority_id",referencedColumnName="id"))
	private List<Authority> authorities;  //一个用户会有多个权限用list
 	protected User() { // 设置为protected方式直接使用

	}

	public User(Long id, String name, String email, String username) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.username = username;
	}
    /*
     * 加密密码（非 Javadoc）
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
	public void setEncodePassword(String password) {
		PasswordEncoder encoder=new BCryptPasswordEncoder();
		String encodePassword=encoder.encode(password);
		this.password=encodePassword;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d,name='%s',email='%s',username='%s']", id, name, email, username);
	}
	public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	//  需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
        List<SimpleGrantedAuthority> simpleGrantedAuthorities=new ArrayList<>();  //转成字符串形式才能在前台显示
        for (GrantedAuthority authority : this.authorities) {
			simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}
		return simpleGrantedAuthorities;
	}

	@Override
	public boolean isAccountNonExpired() { // 账户是否过期

		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // 账户是否锁定

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { // 是否重要

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

}
