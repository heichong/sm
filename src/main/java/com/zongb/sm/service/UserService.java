package com.zongb.sm.service;

import java.util.List;

import com.zongb.sm.entity.User;

public interface UserService {

	/**
	 * 获取所有用户
	 * @return
	 */
	public List<User> getAllUser();
	
	/**
	 * 根据用户名查找用户
	 * @param name
	 * @return
	 */
	public User getUserById(String id);

	/**
	 * 根据用户id删除用户
	 * @param name
	 * @return
	 */
	public User deleteUserById(String id);

	/**
	 * 根据用户id修改用户
	 * @param name
	 * @return
	 */
	public User updateUser(User user);
	
	/**
	 * 添加用户
	 * @param user
	 */
	public void saveUser(User user);
}
