package com.cn.bjut.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.bjut.dao.UserMovieScoreDao;
import com.cn.bjut.pojo.UserMovieScore;
import com.cn.bjut.service.UserMovieScoreService;

@Service("userMovieScoreService")
public class UserMovieScoreServiceImpl implements UserMovieScoreService {

	@Autowired
	private UserMovieScoreDao scoreDao;
	
	public void save(UserMovieScore score) {
		scoreDao.insertUserMovieScore(score);
	}

	public List<UserMovieScore> getScoreByUserId(int userId, boolean ifTestData) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("ifTestData", ifTestData);
		return scoreDao.selectUserMovieScoreByUserId(params);
	}

}
