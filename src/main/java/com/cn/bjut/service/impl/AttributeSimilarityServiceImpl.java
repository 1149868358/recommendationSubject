package com.cn.bjut.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.bjut.dao.AttributeSimilarityDao;
import com.cn.bjut.pojo.AttributeSimilarity;
import com.cn.bjut.pojo.Occupation;
import com.cn.bjut.pojo.User;
import com.cn.bjut.service.AttributeSimilarityService;
import com.cn.bjut.service.OccupationService;

@Service("attriService")
public class AttributeSimilarityServiceImpl implements AttributeSimilarityService {

	//年龄计算参数，以5岁为分界
	public static int range = 5;
	
	//属性相似度 = a*年龄相似度+b*性别相似度+c*职业相似度
	public static double a = 0.1d;
	public static double b = 0.5d;
	//那么参数  c = 1-a-b;
	
	//职业坐标系的斜对角，即最远距离
	public static double maxLength = Math.sqrt(Math.pow(2d, 2)+Math.pow(8d, 2));
	
	@Resource
	private AttributeSimilarityDao attriDao;
	@Resource
	private OccupationService occupationService;
	
	public void save(AttributeSimilarity sim) {
		attriDao.insertAttributeSimilarity(sim);

	}

	public double calculateAgeSimilarity(User user1, User user2) {
		
		int age1 = user1.getAge();
		int age2 = user2.getAge();
		
		if(age1 != age2){
			if(age1 < age2){
				int age = age1; age1 = age2; age2 = age;
			}
			int ageRange = age1-age2;
			DecimalFormat df = new DecimalFormat("0.00");
			return ageRange < range?1d:Double.valueOf(df.format((float)range/ageRange));
			
		}else{
			return 1d;
		}
	}

	public double calculateOccupationSimilarity(User user1, User user2) {
		
		String occupationName1 = user1.getOccupation();
		String occupationName2 = user2.getOccupation();
		if(occupationName1.equals(occupationName2)){
			return 1d;
		}else{
			
			Occupation occ1 = occupationService.getOccupationByName(occupationName1);
			Occupation occ2 = occupationService.getOccupationByName(occupationName2);
			int x1 = occ1.getX(),x2 = occ2.getX(),y1 = occ1.getY(),y2 = occ2.getY();
			if(x1 == x2 && y1 == y2){
				return 1d;
			}else{
				if(x1 > x2){int xtemp = x1;x1 = x2;x2 = xtemp;}
				if(y1 > y2){int ytemp = y1;y1 = y2;y2 = ytemp;}
				DecimalFormat df = new DecimalFormat("0.00");
				return Double.parseDouble(df.format((maxLength - Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)))/maxLength));
				
			}
		}
		
	}

	public double calculateGenderSimilarity(User user1, User user2) {
		
		String gender1 = user1.isGender();
		String gender2 = user2.isGender();
		
		return gender1.equals(gender2)?1d:0d;
	}

	public double calculateAttributeSimilarity(double ageSimilarity, double occupationSimilarity,
			double genderSimilarity) {
		
		//属性相似度 = a*年龄相似度+b*性别相似度+c*职业相似度
		return a*ageSimilarity + b*genderSimilarity + (1-a-b) * occupationSimilarity;
	}

	public AttributeSimilarity getAttributeSimilarityTwoUser(int userId1, int userId2) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId1", userId1);
		paramsMap.put("userId2", userId2);
		return attriDao.selectAttributeSimilarityById(paramsMap);
	}

	
}
