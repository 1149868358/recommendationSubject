package com.cn.bjut.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.bjut.dao.TrustSimilarityDao;
import com.cn.bjut.pojo.TrustSimilarity;
import com.cn.bjut.pojo.User;
import com.cn.bjut.pojo.UserMovieScore;
import com.cn.bjut.service.IUserService;
import com.cn.bjut.service.TrustSimilarityService;
import com.cn.bjut.service.UserMovieScoreService;

@Service("trustSimilarityService")
public class TrustSimilarityServiceImpl implements TrustSimilarityService {

	private Logger logger = Logger.getLogger(TrustSimilarityServiceImpl.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private UserMovieScoreService scoreService;   //********
	@Autowired
	private TrustSimilarityDao trustSimilarityDao;
	
	public static DecimalFormat df = new DecimalFormat("0.00000");
	public static String pTrust = "pTrust";
	public static String dTrust = "dTrust";
	
	/**
	 * �����û����������ƶ�
	 */
	public void trustSimilaritycal() {
		
		List<User> userList = userService.getAllUser();
		int i = 0;
		for(; i < userList.size() ; i++){
			int userId1 = userList.get(i).getUserId();
			Map<String, Object> userMap1 = dealMsgByUserId(userId1);
			if(null == userMap1)continue;
			
			for(int j = i+1 ; j < userList.size() ; j++){
				int userId2 = userList.get(j).getUserId();
				Map<String, Object> userMap2 = dealMsgByUserId(userId2);
				if(null == userMap2)continue;
				
				//�ҳ������û���ͬ���۹�����Ŀ
				Set<Integer> movieSet1 = (Set<Integer>)userMap1.get("movieSet");
				Set<Integer> movieSet2 = (Set<Integer>)userMap2.get("movieSet");
				Set<Integer> set = new HashSet<Integer>(movieSet1);
				if(!set.removeAll(movieSet2)){
					logger.info("�û�"+ userId1 + "���û�" + userId2 + "�޹�ͬ���۵���Ŀ!");
					continue;
				}
				
				Set<Integer> commonSet = new HashSet<Integer>(movieSet1);
				commonSet.removeAll(set);
				logger.info("�û�" + userId1 + "���û�" + userId2 + "��ͬ��������Щ��Ŀ��" + commonSet);
				
				//�û�1���û�2��jacCard���ƶȼ���Ϊ��
				double jacCard = Double.parseDouble(df.format((double)commonSet.size()/(movieSet1.size()+movieSet2.size()-commonSet.size())));
				
				//Լ��Ƥ��ѷ������ƶ�(person������ƶ�)���㣺
				double cpc = cpcSimilarityCal(userMap1, userMap2, commonSet);
				
				//ֱ�����ζ�
				double Dtrust = Math.abs(cpc)*jacCard;
				
				TrustSimilarity trustSimilarity = new TrustSimilarity();
				trustSimilarity.setUserId1(userId1);
				trustSimilarity.setUserId2(userId2);
				trustSimilarity.setJacCard(jacCard);
				trustSimilarity.setPersonSimilarity(cpc);
				trustSimilarity.setTrustSimilarity(Dtrust);
				trustSimilarity.setIfDtrust(true); //ֱ�����ζ�
				this.insertOrUpdateTrustSimilarity(trustSimilarity);
			}
			
		}
		
		
	}
	
	public void insertOrUpdateTrustSimilarity(TrustSimilarity trustSimilarity) {
		
		//ֱ�����ζȺͼ�����ζȶ������
		TrustSimilarity ts = getTrustByUserId(trustSimilarity.getUserId1(),trustSimilarity.getUserId2(),"all");
		if(null == ts){
			trustSimilarityDao.insertTrustSimilarity(trustSimilarity);
		}else{
			trustSimilarityDao.updateTrustSimilarity(trustSimilarity);
		}
	}

	
	public TrustSimilarity getTrustByUserId(int userId1, int userId2, String ifDtrustStr){
		Map<String, Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("userId1", userId1);
		paramsMap.put("userId2", userId2);
		
		if(!"all".equals(ifDtrustStr)){
			
			paramsMap.put("ifDtrust", dTrust.equals(ifDtrustStr)?true:false);
		}
		return trustSimilarityDao.selectTrustSimilarityByUserId(paramsMap);
	}
	
	public List<TrustSimilarity> selectDtrustByUserId(int userId){
		return trustSimilarityDao.selectDtrustByUserId(userId);
	}
	
	/**
	 * Լ��Ƥ��ѷ������ƶ�(person������ƶ�)���㣺
	 * @param userMap1
	 * @param userMap2
	 * @param commonSet
	 * @return
	 */
	private double cpcSimilarityCal(Map<String, Object> userMap1, Map<String, Object> userMap2,
			Set<Integer> commonSet) {
		
		//movieScoreMap ��ʽ���£�{movieId:score;movieId:score}
		Map<Integer, Integer> movieScoreMap1 = (Map<Integer, Integer>)userMap1.get("movieScoreMap");
		Map<Integer, Integer> movieScoreMap2 = (Map<Integer, Integer>)userMap2.get("movieScoreMap");
		double averageScore1 = Double.parseDouble(userMap1.get("averageScore").toString());
		double averageScore2 = Double.parseDouble(userMap2.get("averageScore").toString());
		
		//���ӣ�fenzi  ��ĸ���û�1��ƽ������pf1  ��ĸ���û�2��ƽ������pf2
		double fenzi = 0d, pf1 = 0d, pf2 = 0d;
		
		for(Integer commonMovieItem : commonSet){
			fenzi += (movieScoreMap1.get(commonMovieItem)- averageScore1) * (movieScoreMap2.get(commonMovieItem)- averageScore2);
			pf1 += Math.pow(movieScoreMap1.get(commonMovieItem)- averageScore1, 2);
			pf2 += Math.pow(movieScoreMap2.get(commonMovieItem)- averageScore2, 2);
		}
		
		if(0d == pf1 || 0d ==pf2)return 0d;
		double cpc = (double)fenzi/(Math.sqrt(pf1)*Math.sqrt(pf2));
		return cpc;
	}

	
	/**
	 * ���ݴ�����û�Id�����û������ּ�¼����������Ҫ�ĸ�ʽ��֯����������ѭ������
	 * 1,ƽ���֣�2,���û����۵���Ŀ������3,��Ŀ�����ֵĶ���Map
	 * @param userId
	 * @return
	 */
	private Map<String, Object> dealMsgByUserId(int userId){
		
		Map<String,Object> scoreMap = new HashMap<String,Object>();
		List<UserMovieScore> scoreList = scoreService.getScoreByUserId(userId,false);
		if(scoreList.size() <= 0){
			logger.info("�û�" + userId + "δ���κ���Ŀ�������ۣ�");
			return null;
			
		}
		
		double totalScore = 0d;
		Map<Integer, Integer> movieScoreMap = new HashMap<Integer,Integer>();
		Set<Integer> movieSet = new HashSet<Integer>(); 
		for(UserMovieScore score : scoreList){
			movieScoreMap.put(score.getMovieId(), score.getScore());
			totalScore += score.getScore();
			movieSet.add(score.getMovieId());
		}
		
		double averageScore = Double.parseDouble(df.format((double)totalScore/scoreList.size()));
		scoreMap.put("averageScore", averageScore);
		scoreMap.put("totalItem", scoreList.size());
		scoreMap.put("movieScoreMap", movieScoreMap);
		scoreMap.put("movieSet", movieSet);
		logger.info("�û���" + userId + "�����۵���Ŀ����" + scoreList.size() + "��Ŀƽ���֣�" + averageScore);
		
		return scoreMap;
	}

	
	/**
	 * �����������Ѿ����ڵ�ֱ�����ζȼ��������ζ�
	 */
	public void pTrustSimilarityCal() {

		List<User> userList = userService.getAllUser();
		//int i = 100;
		int i = 140;
		Map<Integer,Map<String,Object>> allUserIdsListMap = getAllUserIdsDTrustMap(userList);
		for( ;i < userList.size();i++){
			
			int userId1 = userList.get(i).getUserId();
			//�ҵ��뱾�û���ֱ�����ζȵ������û�����
			//Map<String, Object> dTrustMap2 = dealDTrustByUserId(userId1);
			Map<String, Object> dTrustMap2 = allUserIdsListMap.get(userId1);
			//���뱾�û��м�����ƶȵ��û�id�ŵ�һ�������б����������У���ж�β�ѯ���ݿ�
			Set<Integer> pTrustSet = dealPTrustByUserId(userId1);
			if(null == dTrustMap2)continue;
			List<Integer> userId2List = (List<Integer>)dTrustMap2.get("userIdOtherList");
			
			//һ�ν�ȫ��userId2List���ص��ڴ��У����ٲ�ѯ���ݿ�Ĵ���
			/*Map<Integer,Map<String,Object>> allUserIdsListMap = new HashMap<Integer,Map<String,Object>>();
			for(int k=0; k<userId2List.size(); k++){
				int userId2 = userId2List.get(k);
				Map<String, Object> dTrustMap3 = dealDTrustByUserId(userId2);
				allUserIdsListMap.put(userId2, dTrustMap3);
			}*/
			for(int j=0; j<userId2List.size(); j++){
				
				int userId2 = userId2List.get(j);
				//Map<String, Object> dTrustMap3 = dealDTrustByUserId(userId2);
				Map<String, Object> dTrustMap3 = allUserIdsListMap.get(userId2);
				if(null == dTrustMap3)continue;
				List<Integer> userId3List = (List<Integer>)dTrustMap3.get("userIdOtherList");
				List<Integer> list = new ArrayList<Integer>(userId3List);
				list.removeAll(userId2List);
				list.remove((Object)userId1);	 
				if(list.size() <= 0)continue;
				
				for(int m=0 ; m<list.size() ; m++){
					int userId3 = list.get(m);
					/*
					 * ������֪userId1 �� userId2��ֱ�����ζȣ��� userId2��userId3��ֱ�����ζȣ�
					 * ��userId1��userId3û��ֱ�����ζ�
					 */
					//�������Ƿ��Ѿ������userId1��userId3�ļ�����ζȣ���ֹ�ظ�����
					/*TrustSimilarity ts = getTrustByUserId(userId1,userId3,pTrust);
					if(null != ts){
						logger.info("�û�" + userId1 + "���û�" + userId3 + "�ļ�����ƶ��Ѿ�����������ظ�����");
						continue;
					}*/
					//�Ż��˴���ѯ����
					if(pTrustSet.contains(userId3)){
						logger.info("�û�" + userId1 + "���û�" + userId3 + "�ļ�����ƶ��Ѿ�����������ظ�����");
						continue;
					}
					
					//mediumUser�з������н���userId1��userId3֮��Ĵ������ζȵ��û�����
					//Map<Integer,double[]>�д������Ԫ�أ��м��û�id,�м��û���userId1��ֱ�����ζ�,�м��û���userId3��ֱ�����ζ�
					Map<Integer,double[]> mediumUserMap = new HashMap<Integer,double[]>();
					//userId1��userId3��δ���������ζȣ����ڿ�ʼ����
					Map<Integer, Double> userId2DTrustMap = (Map<Integer, Double>)dTrustMap2.get("userIdOtherDTrustMap");
					Map<Integer, Double> userId3DTrustMap = (Map<Integer, Double>)dTrustMap3.get("userIdOtherDTrustMap");
					
					mediumUserMap.put(userId2, new double[]{userId2DTrustMap.get(userId2),userId3DTrustMap.get(userId3)});
					//�˴�ѭ���ҵ���userId2һ����userId3��ֱ�����ƶȵ��û�
					for(Integer otherBetweenUser : userId2List){
						if(otherBetweenUser == userId2)continue;
						//TrustSimilarity tsBetween = getTrustByUserId(otherBetweenUser,userId3,dTrust); //�����������û���û��ֱ�����ζ�
						Map<Integer, Double> tsBetweenMap = (Map<Integer, Double>)allUserIdsListMap.get(otherBetweenUser).get("userIdOtherDTrustMap");//�����������û���û��ֱ�����ζ�
						if(null == tsBetweenMap.get(userId3))continue;
						
						mediumUserMap.put(otherBetweenUser, new double[]{userId2DTrustMap.get(otherBetweenUser),tsBetweenMap.get(userId3)});
					}
					
					//mediumUserMap���Ѿ������е�userId1��userId3֮��Ĵ����û����Լ�˫�������ζȻ��ܳ���
					StringBuilder mediumUserId = new StringBuilder();
					double fenzi = 0d;
					for(Map.Entry<Integer, double[]> mediumUser : mediumUserMap.entrySet()){
						fenzi += mediumUser.getValue()[0]*mediumUser.getValue()[1];
						mediumUserId.append(mediumUser.getKey() + ";");
					}
					double pTrustSimilarity = Double.parseDouble(df.format((double)fenzi/mediumUserMap.size()));
					
					logger.info("�û�" + userId1 + " ���û�" + userId3 + "�м��û���" + mediumUserId.toString());
					TrustSimilarity pTrust = new TrustSimilarity();
					pTrust.setIfDtrust(false);//������ζ�
					pTrust.setUserId1(userId1);
					pTrust.setUserId2(userId3);
					pTrust.setMediumUserId(mediumUserId.toString());
					pTrust.setTrustSimilarity(pTrustSimilarity);
					insertOrUpdateTrustSimilarity(pTrust);
					//userId1�ļ�����ζȵ�set�з������µ�userId3
					pTrustSet.add(userId3);
					
				}
				
			}
			
		}
		
		
		
	}

	

	private Map<Integer, Map<String, Object>> getAllUserIdsDTrustMap(List<User> userList) {
		//һ�ν�ȫ���û���ֱ�����ζ�ȫ��������Ӧ��ʽ��֯�������ص��ڴ��У����ٲ�ѯ���ݿ�Ĵ���
		
		Map<Integer,Map<String,Object>> allUserIdsListMap = new HashMap<Integer,Map<String,Object>>();
		for(int k=0; k<userList.size(); k++){
			int userId2 = userList.get(k).getUserId();
			Map<String, Object> dTrustMap3 = dealDTrustByUserId(userId2);
			allUserIdsListMap.put(userId2, dTrustMap3);
		}
		
		return allUserIdsListMap;
	}

	//�ҵ��뱾�û���ֱ�����ζȵ������û�����
	private Map<String, Object> dealDTrustByUserId(int userId) {

		Map<String, Object> dTrustMap = new HashMap<String, Object>();
		List<TrustSimilarity> dTrustList = selectDtrustByUserId(userId); //����������ֻѡ��ֱ�����ƶ� ifDTrust = trust
		if(null == dTrustList || dTrustList.size() <= 0)return null;
		
		//���û�userId��ֱ�����ζȵ������û��Լ�ֱ�����ζȵ�ֵ�Ķ��չ�ϵmap {userId2:dTrust;userId2:dTrust;userId2:dTrust}
		Map<Integer, Double> userIdOtherDTrustMap = new HashMap<Integer,Double>();
		//{userId2,userId2,userId}
		List<Integer> userIdOtherList = new ArrayList<Integer>();
		for(TrustSimilarity sim : dTrustList){
			int userId2 = sim.getUserId1()==userId?sim.getUserId2():sim.getUserId1();
			userIdOtherDTrustMap.put(userId2, new Double(sim.getTrustSimilarity()));
			userIdOtherList.add(userId2);
		}
		dTrustMap.put("userIdOtherDTrustMap", userIdOtherDTrustMap);
		dTrustMap.put("userIdOtherList", userIdOtherList);
		return dTrustMap;
	}
	
	private Set<Integer> dealPTrustByUserId(int userId){
		Set<Integer> pTrustSet = new HashSet<Integer>();
		List<TrustSimilarity> pTrustList = selectPTrustByUserId(userId); //
		if(null == pTrustList || pTrustList.size() <= 0)return null;
		
		for(TrustSimilarity ptrust : pTrustList){
			pTrustSet.add(ptrust.getUserId1()==userId?ptrust.getUserId2():ptrust.getUserId1());
		}
		return pTrustSet;
	}

	private List<TrustSimilarity> selectPTrustByUserId(int userId) {
		return trustSimilarityDao.selectPtrustByUserId(userId);
	}

	public TrustSimilarity getTrustSimilarityByUser1AndUser2(int userId1, int userId2) {
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId1", userId1);
		paramsMap.put("userId2", userId2);
		return trustSimilarityDao.selectTrustSimilarityByUserId(paramsMap);
	}
	
	
	
	
	
}
