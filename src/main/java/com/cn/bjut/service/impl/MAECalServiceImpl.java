package com.cn.bjut.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.bjut.pojo.TotalSimilrity;
import com.cn.bjut.pojo.User;
import com.cn.bjut.pojo.UserMovieScore;
import com.cn.bjut.service.IUserService;
import com.cn.bjut.service.MAECalService;
import com.cn.bjut.service.TotalSimilarityService;
import com.cn.bjut.service.UserMovieScoreService;

import static com.cn.bjut.tool.Tool.df5;
import static com.cn.bjut.tool.Tool.ctd;
import static com.cn.bjut.tool.Tool.NUser;

@Service("maeCalService")
public class MAECalServiceImpl implements MAECalService {

	private Logger logger = Logger.getLogger(MAECalServiceImpl.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private TotalSimilarityService totalSimilarityService;
	@Autowired
	private UserMovieScoreService userMovieScoreService;
	
	/**
	 * 计算推荐算法的MAE值
	 */
	public void maeCal() {
		
		List<Double> MAEList = new ArrayList<Double>();
		List<User> userList = userService.getAllUser();
		int i = 0;
		
		//for(; i<userList.size(); i++){
		for(; i<5; i++){
			int userId = userList.get(i).getUserId();
			//找到与用户userId相似度最高的N个用户
			Map<Integer,Double> topNMap = getSimi_N_User(userId);
			
			//所需的信息-->用户的平均分，项目：评分{userId:{averageScore:**;movieScoreMap:{movieId:score}}}
			//将这N个用户的所需信息一次加载到内存中
			Map<Integer,Map<String,Object>> userIdScoreMap = new HashMap<Integer,Map<String, Object>>();
			for(Entry<Integer,Double> entry : topNMap.entrySet()){
				//相似用户的Id
				int userIdSim = (Integer)entry.getKey();
				//传入用户的Id，将用户所有的对电影的评分按照需求格式组织起来
				userIdScoreMap.put(userIdSim, dealScoreByUserId(userIdSim));
						
			}
			//查看下本用户的测试数据
			List<UserMovieScore> testScoreList = userMovieScoreService.getScoreByUserId(userId, true);
			//evaluateMap中存放电影Id和预测评分{movieId:evaluateScore}
			Map<Integer, Double> evaluateMap = new HashMap<Integer,Double>();
			Map<String,Object> currentUserIdScoreMap = dealScoreByUserId(userId);
			for(UserMovieScore testScore : testScoreList){
				
				int movieId = testScore.getMovieId();
				
				//测试数据对movieId进行了评价，现在找出topN数据集中也对MovieId进行过评价的近邻用户，计算预估评分
				//本轮循环需要得到的数据形式为：evaluateSingleScoreList ==> [{userId:**;averageScore:**;movieIdScore:**;totalSim:**}]
				List<Map<String,Object>> evaluateSingleScoreList = new ArrayList<Map<String,Object>>();
				
				for(Entry<Integer,Map<String,Object>> entry : userIdScoreMap.entrySet()){
					
					Map<String,Object> movieScoreMap = (Map<String,Object>)entry.getValue().get("movieScoreMap");
					if(null == movieScoreMap.get(movieId))continue;
					
					//将近邻用户中同样对本项目评价过得用户的信息找出来 放入：evaluateSingleScoreList
					Map<String, Object> sMap = new HashMap<String,Object>();
					sMap.put("userId", entry.getKey());
					sMap.put("averageScore", entry.getValue().get("averageScore"));
					sMap.put("movieIdScore", movieScoreMap.get(movieId));
					sMap.put("totalSim", topNMap.get(entry.getKey()));
					evaluateSingleScoreList.add(sMap);
				}
				//邻居用户没有人对该电影有过评分则该电影推荐评分为0d
				double evaluateScore = 0d;
				if(evaluateSingleScoreList.size() > 0){
					
					//循环完毕后可计算某项目的预测评分
					double averageScore1 = ctd(currentUserIdScoreMap.get("averageScore"));
					
					double fenzi = 0d;
					double fenmu = 0d;
					for(Map<String, Object> emap : evaluateSingleScoreList){
						fenzi += ctd(emap.get("totalSim"))*(ctd(emap.get("movieIdScore")) - ctd(emap.get("averageScore")));
						fenmu += ctd(emap.get("totalSim"));
					}
					evaluateScore = averageScore1 + df5(fenzi/fenmu);
					logger.info("用户" + userId + " 对电影" + movieId + " 的预测评分为 ：" + evaluateScore);
				}else{
					logger.info("用户" + userId + " 对电影" + movieId + " 无预测评分");
				}
				evaluateMap.put(movieId, evaluateScore);
			}
			
			//拿到测试评分和预测评分，计算MAE值
			double MAEfenzi = 0d;
			for(UserMovieScore testScore : testScoreList){
				int movieId = testScore.getMovieId();
				int tScore = testScore.getScore();
				double evaluateScore = evaluateMap.get(movieId);
				//|实际得分-预测得分|
				MAEfenzi += Math.abs(tScore - evaluateScore);
			}
			double MAE = df5(MAEfenzi/testScoreList.size());
			logger.info("用户" + userId + "预测完成！计算得MAE值为：" + MAE);
			MAEList.add(MAE);
		}
		
		Double tMae = 0d;
		for(Double d : MAEList){
			tMae += d;
		}
		logger.info("执行完毕综合MAE平均值为：" + df5(tMae/MAEList.size()));
		
	}

	//所需的信息-->用户的平均分，项目：评分{userId:{averageScore:**;movieScoreMap:{movieId:score}}}
	private Map<String, Object> dealScoreByUserId(int userId) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<UserMovieScore> usList = userMovieScoreService.getScoreByUserId(userId, false);
		
		double totalScore = 0d;
		Map<Integer,Double> movieScoreMap = new HashMap<Integer,Double>();
		for(UserMovieScore score : usList){
			totalScore += score.getScore();
			movieScoreMap.put(score.getMovieId(), (double)score.getScore());
		}
		resultMap.put("averageScore", df5(totalScore/usList.size()));
		resultMap.put("movieScoreMap",movieScoreMap);
		
		return resultMap;
	}

	/**
	 * 找到与指定用户相似度最高的N个用户
	 * @param userId
	 * @return
	 */
	private Map<Integer, Double> getSimi_N_User(int userId) {
		List<TotalSimilrity> totalList = totalSimilarityService.getTotalSimilarityByUserId(userId);
		//存放找出来的相似度最高的N个用户==> {userId:totalSim}
		Map<Integer,Double> topNMap = new HashMap<Integer,Double>();
		for(int j=0; j < NUser; j++){
			TotalSimilrity totalSim = Collections.max(totalList);
			totalList.remove(totalSim);
			topNMap.put(totalSim.getUserId1()==userId?totalSim.getUserId2():totalSim.getUserId1(),totalSim.getTotalSimilarity());
		}
		return topNMap;
	}

	
	
	

}
