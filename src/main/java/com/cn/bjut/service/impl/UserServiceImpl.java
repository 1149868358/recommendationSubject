package com.cn.bjut.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.bjut.dao.IUserDao;
import com.cn.bjut.pojo.User;
import com.cn.bjut.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Resource
	private IUserDao userDao;
	
	public User getUserById(int userId) {
		return userDao.selectUserById(userId);
	}

	public void save(User user) {
		userDao.insertUser(user);
		
	}

	/**
	 * 返回所有用户
	 */
	public List<User> getAllUser() {
		return userDao.selectAllUser();
		
	}

}
