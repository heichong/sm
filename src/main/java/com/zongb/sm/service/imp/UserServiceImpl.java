package com.zongb.sm.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zongb.sm.dao.UserMapper;
import com.zongb.sm.entity.User;
import com.zongb.sm.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper ;
	
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED,  rollbackFor = Exception.class)  
	public List<User> getAllUser() {
		return userMapper.getAllUser();
	}

	/**
	 * 根据用户名查找用户
	 * @param name
	 * @return
	 */
	@Transactional(readOnly = true,propagation=Propagation.REQUIRED,  rollbackFor = Exception.class)  
	public User getUserById(String id){
		return userMapper.getUserById(id) ;
	}

	/**
	 * 根据用户id删除用户
	 * @param name
	 * @return
	 */
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED,  rollbackFor = Exception.class)  
	public User deleteUserById(String id){
		return userMapper.deleteUserById(id) ;
	}

	/**
	 * 根据用户id修改用户
	 * @param name
	 * @return
	 */
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED,  rollbackFor = Exception.class)  
	public User updateUser(User user){
		return userMapper.updateUser(user) ;
	}
	
	/**
	 * 添加用户
	 * @param user
	 */
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED,  rollbackFor = Exception.class)  
	public void saveUser(User user){
		userMapper.saveUser(user) ;
	}

}
