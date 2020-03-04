package com.cn.bjut.service;

import com.cn.bjut.pojo.Occupation;

public interface OccupationService {

	void save(Occupation occ);

	Occupation getOccupationByName(String occupationName);
}
