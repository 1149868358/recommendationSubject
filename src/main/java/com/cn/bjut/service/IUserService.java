package com.cn.bjut.service;

import java.util.List;

import com.cn.bjut.pojo.User;

public interface IUserService {

	public User getUserById(int userId);
	
	public void save(User user);

	public List<User> getAllUser();
}
