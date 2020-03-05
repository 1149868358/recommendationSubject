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
	 * 计算用户的信任相似度
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
				
				//找出两个用户共同评价过的项目
				Set<Integer> movieSet1 = (Set<Integer>)userMap1.get("movieSet");
				Set<Integer> movieSet2 = (Set<Integer>)userMap2.get("movieSet");
				Set<Integer> set = new HashSet<Integer>(movieSet1);
				if(!set.removeAll(movieSet2)){
					logger.info("用户"+ userId1 + "与用户" + userId2 + "无共同评价的项目!");
					continue;
				}
				
				Set<Integer> commonSet = new HashSet<Integer>(movieSet1);
				commonSet.removeAll(set);
				logger.info("用户" + userId1 + "与用户" + userId2 + "共同评价了这些项目：" + commonSet);
				
				//用户1与用户2的jacCard相似度计算为：
				double jacCard = Double.parseDouble(df.format((double)commonSet.size()/(movieSet1.size()+movieSet2.size()-commonSet.size())));
				
				//约束皮尔逊相关相似度(person相关相似度)计算：
				double cpc = cpcSimilarityCal(userMap1, userMap2, commonSet);
				
				//直接信任度
				double Dtrust = Math.abs(cpc)*jacCard;
				
				TrustSimilarity trustSimilarity = new TrustSimilarity();
				trustSimilarity.setUserId1(userId1);
				trustSimilarity.setUserId2(userId2);
				trustSimilarity.setJacCard(jacCard);
				trustSimilarity.setPersonSimilarity(cpc);
				trustSimilarity.setTrustSimilarity(Dtrust);
				trustSimilarity.setIfDtrust(true); //直接信任度
				this.insertOrUpdateTrustSimilarity(trustSimilarity);
			}
			
		}
		
		
	}
	
	public void insertOrUpdateTrustSimilarity(TrustSimilarity trustSimilarity) {
		
		//直接信任度和间接信任度都查出来
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
	 * 约束皮尔逊相关相似度(person相关相似度)计算：
	 * @param userMap1
	 * @param userMap2
	 * @param commonSet
	 * @return
	 */
	private double cpcSimilarityCal(Map<String, Object> userMap1, Map<String, Object> userMap2,
			Set<Integer> commonSet) {
		
		//movieScoreMap 格式如下：{movieId:score;movieId:score}
		Map<Integer, Integer> movieScoreMap1 = (Map<Integer, Integer>)userMap1.get("movieScoreMap");
		Map<Integer, Integer> movieScoreMap2 = (Map<Integer, Integer>)userMap2.get("movieScoreMap");
		double averageScore1 = Double.parseDouble(userMap1.get("averageScore").toString());
		double averageScore2 = Double.parseDouble(userMap2.get("averageScore").toString());
		
		//分子：fenzi  分母中用户1的平方根：pf1  分母中用户2的平方根：pf2
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
	 * 根据传入的用户Id将该用户的评分记录按照我们需要的格式组织起来，方便循环调用
	 * 1,平均分，2,该用户评价的项目总数，3,项目与评分的对照Map
	 * @param userId
	 * @return
	 */
	private Map<String, Object> dealMsgByUserId(int userId){
		
		Map<String,Object> scoreMap = new HashMap<String,Object>();
		List<UserMovieScore> scoreList = scoreService.getScoreByUserId(userId,false);
		if(scoreList.size() <= 0){
			logger.info("用户" + userId + "未对任何项目进行评价！");
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
		logger.info("用户：" + userId + "共评价的项目数：" + scoreList.size() + "项目平均分：" + averageScore);
		
		return scoreMap;
	}

	
	/**
	 * 本方法根据已经存在的直接信任度计算间接信任度
	 */
	public void pTrustSimilarityCal() {

		List<User> userList = userService.getAllUser();
		//int i = 100;
		int i = 140;
		Map<Integer,Map<String,Object>> allUserIdsListMap = getAllUserIdsDTrustMap(userList);
		for( ;i < userList.size();i++){
			
			int userId1 = userList.get(i).getUserId();
			//找到与本用户有直接信任度的所有用户集合
			//Map<String, Object> dTrustMap2 = dealDTrustByUserId(userId1);
			Map<String, Object> dTrustMap2 = allUserIdsListMap.get(userId1);
			//将与本用户有间接相似度的用户id放到一个集合中避免接下来的校验中多次查询数据库
			Set<Integer> pTrustSet = dealPTrustByUserId(userId1);
			if(null == dTrustMap2)continue;
			List<Integer> userId2List = (List<Integer>)dTrustMap2.get("userIdOtherList");
			
			//一次将全部userId2List加载到内存中，减少查询数据库的次数
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
					 * 现在已知userId1 与 userId2有直接信任度，且 userId2与userId3有直接信任度，
					 * 且userId1与userId3没有直接信任度
					 */
					//查找下是否已经计算过userId1与userId3的间接信任度，防止重复计算
					/*TrustSimilarity ts = getTrustByUserId(userId1,userId3,pTrust);
					if(null != ts){
						logger.info("用户" + userId1 + "与用户" + userId3 + "的间接相似度已经计算过不用重复计算");
						continue;
					}*/
					//优化此处查询代码
					if(pTrustSet.contains(userId3)){
						logger.info("用户" + userId1 + "与用户" + userId3 + "的间接相似度已经计算过不用重复计算");
						continue;
					}
					
					//mediumUser中放入所有介于userId1与userId3之间的传递信任度的用户集合
					//Map<Integer,double[]>中存放三个元素：中间用户id,中间用户与userId1的直接信任度,中间用户与userId3的直接信任度
					Map<Integer,double[]> mediumUserMap = new HashMap<Integer,double[]>();
					//userId1与userId3尚未计算间接信任度，现在开始计算
					Map<Integer, Double> userId2DTrustMap = (Map<Integer, Double>)dTrustMap2.get("userIdOtherDTrustMap");
					Map<Integer, Double> userId3DTrustMap = (Map<Integer, Double>)dTrustMap3.get("userIdOtherDTrustMap");
					
					mediumUserMap.put(userId2, new double[]{userId2DTrustMap.get(userId2),userId3DTrustMap.get(userId3)});
					//此次循环找到与userId2一样与userId3有直接相似度的用户
					for(Integer otherBetweenUser : userId2List){
						if(otherBetweenUser == userId2)continue;
						//TrustSimilarity tsBetween = getTrustByUserId(otherBetweenUser,userId3,dTrust); //看看这两个用户有没有直接信任度
						Map<Integer, Double> tsBetweenMap = (Map<Integer, Double>)allUserIdsListMap.get(otherBetweenUser).get("userIdOtherDTrustMap");//看看这两个用户有没有直接信任度
						if(null == tsBetweenMap.get(userId3))continue;
						
						mediumUserMap.put(otherBetweenUser, new double[]{userId2DTrustMap.get(otherBetweenUser),tsBetweenMap.get(userId3)});
					}
					
					//mediumUserMap中已经把所有的userId1与userId3之间的传递用户，以及双方的信任度汇总出来
					StringBuilder mediumUserId = new StringBuilder();
					double fenzi = 0d;
					for(Map.Entry<Integer, double[]> mediumUser : mediumUserMap.entrySet()){
						fenzi += mediumUser.getValue()[0]*mediumUser.getValue()[1];
						mediumUserId.append(mediumUser.getKey() + ";");
					}
					double pTrustSimilarity = Double.parseDouble(df.format((double)fenzi/mediumUserMap.size()));
					
					logger.info("用户" + userId1 + " 与用户" + userId3 + "中间用户：" + mediumUserId.toString());
					TrustSimilarity pTrust = new TrustSimilarity();
					pTrust.setIfDtrust(false);//间接信任度
					pTrust.setUserId1(userId1);
					pTrust.setUserId2(userId3);
					pTrust.setMediumUserId(mediumUserId.toString());
					pTrust.setTrustSimilarity(pTrustSimilarity);
					insertOrUpdateTrustSimilarity(pTrust);
					//userId1的间接信任度的set中放上最新的userId3
					pTrustSet.add(userId3);
					
				}
				
			}
			
		}
		
		
		
	}

	

	private Map<Integer, Map<String, Object>> getAllUserIdsDTrustMap(List<User> userList) {
		//一次将全部用户的直接信任度全部按照相应格式组织起来加载到内存中，减少查询数据库的次数
		
		Map<Integer,Map<String,Object>> allUserIdsListMap = new HashMap<Integer,Map<String,Object>>();
		for(int k=0; k<userList.size(); k++){
			int userId2 = userList.get(k).getUserId();
			Map<String, Object> dTrustMap3 = dealDTrustByUserId(userId2);
			allUserIdsListMap.put(userId2, dTrustMap3);
		}
		
		return allUserIdsListMap;
	}

	//找到与本用户有直接信任度的所有用户集合
	private Map<String, Object> dealDTrustByUserId(int userId) {

		Map<String, Object> dTrustMap = new HashMap<String, Object>();
		List<TrustSimilarity> dTrustList = selectDtrustByUserId(userId); //限制条件：只选出直接相似度 ifDTrust = trust
		if(null == dTrustList || dTrustList.size() <= 0)return null;
		
		//与用户userId有直接信任度的所有用户以及直接信任度的值的对照关系map {userId2:dTrust;userId2:dTrust;userId2:dTrust}
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
