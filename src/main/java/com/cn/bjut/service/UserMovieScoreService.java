package com.cn.bjut.service;

import java.util.List;

import com.cn.bjut.pojo.UserMovieScore;

public interface UserMovieScoreService {

	public void save(UserMovieScore score);

	public List<UserMovieScore> getScoreByUserId(int userId, boolean ifTestData);
}
