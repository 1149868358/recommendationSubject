package com.cn.bjut.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * �����������ۺ����ƶ�
	 */
	public void TotalSimilarityCalucate() {
		
		List<User> userList = userService.getAllUser();
		int i = 400;
		for(; i < userList.size(); i++){
			int userId1 = userList.get(i).getUserId();
			//�ֱ��ѯuserId1��userId2���������ƶȺ����ζ���ʮ������������Ҫ��ʮ��β�ѯ��̫���ˣ������Ż�Ϊ���´���
			//dealMap������Ĳ���{userId2:value}
			Map<Integer,Double> attrMap = dealAttrMsgByUserId(userId1);
			Map<Integer,Double> trustMap = dealTrustMsgByUserId(userId1);
			
			for(int j=i+1 ; j < userList.size() ; j++){
				//�ۺ����ƶ� = a*�������ƶ� + b*���ζ�
				int userId2 = userList.get(j).getUserId();
				//����ǰ
				//AttributeSimilarity attribute = attriService.getAttributeSimilarityTwoUser(userId1, userId2);
				//�����
				double attributeSim = 0d;
				if(null != attrMap.get(userId2))attributeSim = attrMap.get(userId2);
				
				//����ǰ
				//TrustSimilarity trust = trustSimilarityService.getTrustSimilarityByUser1AndUser2(userId1,userId2);
				double trustSim = 0d;
				//if(null != trust) trustSim = trust.getTrustSimilarity();
				if(null != trustMap.get(userId2)) trustSim = trustMap.get(userId2);
				
				double totalSimilarity = TOTALSIMILARITY1*attributeSim + TOTALSIMILARITY2*trustSim;
				
				//���
				TotalSimilrity total = new TotalSimilrity();
				total.setDate(timeFormate(new Date()));
				total.setUserId1(userId1);
				total.setUserId2(userId2);
				total.setTotalSimilarity(totalSimilarity);
				totalSimilarityDao.insertTotalSimilarity(total);
			}
		}
	}

	/**
	 * ����userId�Ȳ�����û����������ζȵ���Ϣ���ٽ���Ϣ��֯������Map��ʽ
     * ��ʽΪ�� {userId2:value}
	 * @param userId1
	 * @return
	 */
    private Map<Integer, Double> dealTrustMsgByUserId(int userId1) {
		
    	Map<Integer, Double> resultMap = new HashMap<Integer,Double>();
    	List<TrustSimilarity> trustList = trustSimilarityService.getTrustSimilarityByUserId(userId1);
    	for(TrustSimilarity trust : trustList){
    		resultMap.put(trust.getUserId1()==userId1?trust.getUserId2():trust.getUserId1(), trust.getTrustSimilarity());
    	}
		return resultMap;
	}

	/**
     * ����userId�Ȳ�����û��������������ƶȵ���Ϣ���ٽ���Ϣ��֯������Map��ʽ
     * ��ʽΪ�� {userId2:value}
     * @param userId1
     * @return
     */
	private Map<Integer, Double> dealAttrMsgByUserId(int userId1) {
		
		Map<Integer, Double> resultMap = new HashMap<Integer,Double>();
		List<AttributeSimilarity> attrList = attriService.getAttributeSimilarityByUserId(userId1);
		for(AttributeSimilarity attr : attrList){
			resultMap.put(attr.getUser1()==userId1?attr.getUser2():attr.getUser1(), attr.getAttributeSim());
		}
		return resultMap;
	}


	public List<TotalSimilrity> getTotalSimilarityByUserId(int userId) {
		return totalSimilarityDao.selectTotalSimilarityByUserId(userId);
	}

}
