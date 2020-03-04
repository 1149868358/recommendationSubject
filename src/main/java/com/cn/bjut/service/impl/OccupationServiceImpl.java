package com.cn.bjut.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.bjut.dao.OccupationDao;
import com.cn.bjut.pojo.Occupation;
import com.cn.bjut.service.OccupationService;

@Service("occupationServiceImpl")
public class OccupationServiceImpl implements OccupationService {

	@Autowired
	private OccupationDao occDao;
	
	public void save(Occupation occ) {
		occDao.insertOccupation(occ);
	}

	public Occupation getOccupationByName(String occupationName) {
		
		return occDao.selectOccupationByName(occupationName);
	}

}
