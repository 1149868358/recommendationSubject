package com.cn.bjut.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.bjut.dao.IMovieDao;
import com.cn.bjut.pojo.Movie;
import com.cn.bjut.service.IMovieService;

@Service("movieService")
public class MovieServiceImpl implements IMovieService {

	@Resource
	private IMovieDao movieDao;
	
	public void save(Movie movie) {
		movieDao.insertMovie(movie);

	}

}
