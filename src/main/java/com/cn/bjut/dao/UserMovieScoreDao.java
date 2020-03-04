package com.cn.bjut.dao;

import java.util.List;
import java.util.Map;

import com.cn.bjut.pojo.UserMovieScore;

public interface UserMovieScoreDao {

	void insertUserMovieScore(UserMovieScore score);

	List<UserMovieScore> selectUserMovieScoreByUserId(Map<String,Object> params);
}
