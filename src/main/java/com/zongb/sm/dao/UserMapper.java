package com.zongb.sm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zongb.sm.entity.User;
/**
 * 此接口无需实现类
 * @author Administrator
 *
 */
@Repository
public interface UserMapper {

	/**
	 * 获取所有用户
	 * @return
	 */
	public List<User> getAllUser();
	
	/**
	 * 根据用户id查找用户
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
