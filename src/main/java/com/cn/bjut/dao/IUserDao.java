package com.cn.bjut.dao;

import java.util.List;

import com.cn.bjut.pojo.User;

public interface IUserDao {

	User selectUserById(int userId);

	void insertUser(User user);

	List<User> selectAllUser();

}
