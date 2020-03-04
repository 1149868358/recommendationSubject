package com.cn.bjut.dao;

import com.cn.bjut.pojo.Occupation;

public interface OccupationDao {

	public void insertOccupation(Occupation occ);

	public Occupation selectOccupationByName(String occupationName);
}
