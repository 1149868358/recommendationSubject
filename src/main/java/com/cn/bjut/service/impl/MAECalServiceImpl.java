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
	 * �����Ƽ��㷨��MAEֵ
	 */
	public void maeCal() {
		
		List<Double> MAEList = new ArrayList<Double>();
		List<User> userList = userService.getAllUser();
		int i = 0;
		
		//for(; i<userList.size(); i++){
		for(; i<5; i++){
			int userId = userList.get(i).getUserId();
			//�ҵ����û�userId���ƶ���ߵ�N���û�
			Map<Integer,Double> topNMap = getSimi_N_User(userId);
			
			//�������Ϣ-->�û���ƽ���֣���Ŀ������{userId:{averageScore:**;movieScoreMap:{movieId:score}}}
			//����N���û���������Ϣһ�μ��ص��ڴ���
			Map<Integer,Map<String,Object>> userIdScoreMap = new HashMap<Integer,Map<String, Object>>();
			for(Entry<Integer,Double> entry : topNMap.entrySet()){
				//�����û���Id
				int userIdSim = (Integer)entry.getKey();
				//�����û���Id�����û����еĶԵ�Ӱ�����ְ��������ʽ��֯����
				userIdScoreMap.put(userIdSim, dealScoreByUserId(userIdSim));
						
			}
			//�鿴�±��û��Ĳ�������
			List<UserMovieScore> testScoreList = userMovieScoreService.getScoreByUserId(userId, true);
			//evaluateMap�д�ŵ�ӰId��Ԥ������{movieId:evaluateScore}
			Map<Integer, Double> evaluateMap = new HashMap<Integer,Double>();
			Map<String,Object> currentUserIdScoreMap = dealScoreByUserId(userId);
			for(UserMovieScore testScore : testScoreList){
				
				int movieId = testScore.getMovieId();
				
				//�������ݶ�movieId���������ۣ������ҳ�topN���ݼ���Ҳ��MovieId���й����۵Ľ����û�������Ԥ������
				//����ѭ����Ҫ�õ���������ʽΪ��evaluateSingleScoreList ==> [{userId:**;averageScore:**;movieIdScore:**;totalSim:**}]
				List<Map<String,Object>> evaluateSingleScoreList = new ArrayList<Map<String,Object>>();
				
				for(Entry<Integer,Map<String,Object>> entry : userIdScoreMap.entrySet()){
					
					Map<String,Object> movieScoreMap = (Map<String,Object>)entry.getValue().get("movieScoreMap");
					if(null == movieScoreMap.get(movieId))continue;
					
					//�������û���ͬ���Ա���Ŀ���۹����û�����Ϣ�ҳ��� ���룺evaluateSingleScoreList
					Map<String, Object> sMap = new HashMap<String,Object>();
					sMap.put("userId", entry.getKey());
					sMap.put("averageScore", entry.getValue().get("averageScore"));
					sMap.put("movieIdScore", movieScoreMap.get(movieId));
					sMap.put("totalSim", topNMap.get(entry.getKey()));
					evaluateSingleScoreList.add(sMap);
				}
				//�ھ��û�û���˶Ըõ�Ӱ�й�������õ�Ӱ�Ƽ�����Ϊ0d
				double evaluateScore = 0d;
				if(evaluateSingleScoreList.size() > 0){
					
					//ѭ����Ϻ�ɼ���ĳ��Ŀ��Ԥ������
					double averageScore1 = ctd(currentUserIdScoreMap.get("averageScore"));
					
					double fenzi = 0d;
					double fenmu = 0d;
					for(Map<String, Object> emap : evaluateSingleScoreList){
						fenzi += ctd(emap.get("totalSim"))*(ctd(emap.get("movieIdScore")) - ctd(emap.get("averageScore")));
						fenmu += ctd(emap.get("totalSim"));
					}
					evaluateScore = averageScore1 + df5(fenzi/fenmu);
					logger.info("�û�" + userId + " �Ե�Ӱ" + movieId + " ��Ԥ������Ϊ ��" + evaluateScore);
				}else{
					logger.info("�û�" + userId + " �Ե�Ӱ" + movieId + " ��Ԥ������");
				}
				evaluateMap.put(movieId, evaluateScore);
			}
			
			//�õ��������ֺ�Ԥ�����֣�����MAEֵ
			double MAEfenzi = 0d;
			for(UserMovieScore testScore : testScoreList){
				int movieId = testScore.getMovieId();
				int tScore = testScore.getScore();
				double evaluateScore = evaluateMap.get(movieId);
				//|ʵ�ʵ÷�-Ԥ��÷�|
				MAEfenzi += Math.abs(tScore - evaluateScore);
			}
			double MAE = df5(MAEfenzi/testScoreList.size());
			logger.info("�û�" + userId + "Ԥ����ɣ������MAEֵΪ��" + MAE);
			MAEList.add(MAE);
		}
		
		Double tMae = 0d;
		for(Double d : MAEList){
			tMae += d;
		}
		logger.info("ִ������ۺ�MAEƽ��ֵΪ��" + df5(tMae/MAEList.size()));
		
	}

	//�������Ϣ-->�û���ƽ���֣���Ŀ������{userId:{averageScore:**;movieScoreMap:{movieId:score}}}
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
	 * �ҵ���ָ���û����ƶ���ߵ�N���û�
	 * @param userId
	 * @return
	 */
	private Map<Integer, Double> getSimi_N_User(int userId) {
		List<TotalSimilrity> totalList = totalSimilarityService.getTotalSimilarityByUserId(userId);
		//����ҳ��������ƶ���ߵ�N���û�==> {userId:totalSim}
		Map<Integer,Double> topNMap = new HashMap<Integer,Double>();
		for(int j=0; j < NUser; j++){
			TotalSimilrity totalSim = Collections.max(totalList);
			totalList.remove(totalSim);
			topNMap.put(totalSim.getUserId1()==userId?totalSim.getUserId2():totalSim.getUserId1(),totalSim.getTotalSimilarity());
		}
		return topNMap;
	}

	
	
	

}
