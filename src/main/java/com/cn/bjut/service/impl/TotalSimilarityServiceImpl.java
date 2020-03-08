package com.cn.bjut.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.bjut.dao.TotalSimilarityDao;
import com.cn.bjut.pojo.AttributeSimilarity;
import com.cn.bjut.pojo.TotalSimilrity;
import com.cn.bjut.pojo.TrustSimilarity;
import com.cn.bjut.pojo.User;
import com.cn.bjut.service.AttributeSimilarityService;
import com.cn.bjut.service.IUserService;
import com.cn.bjut.service.TotalSimilarityService;
import com.cn.bjut.service.TrustSimilarityService;
import static com.cn.bjut.tool.Tool.TOTALSIMILARITY1;
import static com.cn.bjut.tool.Tool.TOTALSIMILARITY2;
import static com.cn.bjut.tool.Tool.timeFormate;

@Service("totalSimilarityService")
public class TotalSimilarityServiceImpl implements TotalSimilarityService {

	
	
	@Autowired
	private IUserService userService;
	@Autowired
	private AttributeSimilarityService attriService;
	@Autowired
	private TrustSimilarityService trustSimilarityService;
	@Autowired
	private TotalSimilarityDao totalSimilarityDao;
	
	
	/**
	 * 本方法计算综合相似度
	 */
	public void TotalSimilarityCalucate() {
		
		List<User> userList = userService.getAllUser();
		int i = 301;
		for(; i < userList.size(); i++){
			int userId1 = userList.get(i).getUserId();
			for(int j=i+1 ; j < userList.size() ; j++){
				//综合相似度 = a*属性相似度 + b*信任度
				int userId2 = userList.get(j).getUserId();
				AttributeSimilarity attribute = attriService.getAttributeSimilarityTwoUser(userId1, userId2);
				//
				double attributeSim = 0d;
				if(null != attribute)attributeSim = attribute.getAttributeSim();
				
				TrustSimilarity trust = trustSimilarityService.getTrustSimilarityByUser1AndUser2(userId1,userId2);
				double trustSim = 0d;
				if(null != trust) trustSim = trust.getTrustSimilarity();
				
				double totalSimilarity = TOTALSIMILARITY1*attributeSim + TOTALSIMILARITY2*trustSim;
				
				//入库
				TotalSimilrity total = new TotalSimilrity();
				total.setDate(timeFormate(new Date()));
				total.setUserId1(userId1);
				total.setUserId2(userId2);
				total.setTotalSimilarity(totalSimilarity);
				totalSimilarityDao.insertTotalSimilarity(total);
			}
		}
	}


	public List<TotalSimilrity> getTotalSimilarityByUserId(int userId) {
		return totalSimilarityDao.selectTotalSimilarityByUserId(userId);
	}

}
