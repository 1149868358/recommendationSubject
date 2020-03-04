package com.cn.bjut.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.bjut.pojo.Movie;
import com.cn.bjut.pojo.User;
import com.cn.bjut.pojo.UserMovieScore;
import com.cn.bjut.service.IMovieService;
import com.cn.bjut.service.IUserService;
import com.cn.bjut.service.UserMovieScoreService;


/**
 * This class for reading training and test files.It can 
 * be suitable for Grouplens and other data sets.
 * @author wkx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class initTestData {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IMovieService movieService;
	@Autowired
	private UserMovieScoreService userMovieScoreService;
	
	/**
	 * 本方法用来初始化用户数据，将u.user中的数据初始化到mysql数据库表中
	 */
	/*@Test
	public void initUserData(){
		
		try {
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(
					new FileReader("C:/Users/Administrator/Desktop/论文/movieLens数据集/ml-100k/u.user"));
			String s;
			while((s = in.readLine()) != null) {
				String[] params = s.split("\t");
				for(String param:params){
					String[] singleUser = param.split("\\|");
					User user = new User();
					user.setUserId(Integer.parseInt(singleUser[0]));
					user.setAge(Integer.parseInt(singleUser[1]));
					user.setGender(singleUser[2]);
					user.setOccupation(singleUser[3]);
					userService.save(user);
				}
								
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 本方法用来初始化电影信息
	 */
	/*@Test
	public void initMovieData(){
		
		
		try {
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new FileReader("C:/Users/Administrator/Desktop/论文/movieLens数据集/ml-100k/u.item"));
			String s;
			while((s = in.readLine()) != null){
				String[] params = s.split("\t");
				
				for(String param : params){
					String[] mv = param.split("\\|");
					Movie movie = new Movie();
					movie.setMovieId(Integer.parseInt(mv[0]));
					movie.setMovieTitle(mv[1]);
					movie.setReleaseDate(mv[2]);
					movie.setVideoReleaseDate(mv[3]);
					movie.setUrl(mv[4]);
					movie.setUnknown("1".equals(mv[5])?true:false);
					movie.setAction("1".equals(mv[6])?true:false);
					movie.setAdventure("1".equals(mv[7])?true:false);
					movie.setAnimation("1".equals(mv[8])?true:false);
					movie.setChildrens("1".equals(mv[9])?true:false);
					movie.setComedy("1".equals(mv[10])?true:false);
					movie.setCrime("1".equals(mv[11])?true:false);
					movie.setDocumentary("1".equals(mv[12])?true:false);
					movie.setDrama("1".equals(mv[13])?true:false);
					movie.setFantasy("1".equals(mv[14])?true:false);
					movie.setFilmNoir("1".equals(mv[15])?true:false);
					movie.setHorror("1".equals(mv[16])?true:false);
					movie.setMusical("1".equals(mv[17])?true:false);
					movie.setMystery("1".equals(mv[18])?true:false);
					movie.setRomance("1".equals(mv[19])?true:false);
					movie.setSciFi("1".equals(mv[20])?true:false);
					movie.setThriller("1".equals(mv[21])?true:false);
					movie.setWar("1".equals(mv[22])?true:false);
					movie.setWestern("1".equals(mv[23])?true:false);
					movieService.save(movie);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
	
	
	/**
	 * 本测试类用来初始化用户电影评分表的数据
	 */
	private static boolean ifTestData = true; //测试数据
	private static String filePath = "C:/Users/Administrator/Desktop/论文/movieLens数据集/ml-100k/u1.test";
	//private static boolean ifTestData = false; //训练数据
	//private static String filePath = "C:/Users/Administrator/Desktop/论文/movieLens数据集/ml-100k/u1.base";
	
	@Test
	public void initUserMovieScoreData(){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String s;
			while((s = in.readLine()) != null){
				String[] params = s.split("\t");
					UserMovieScore score = new UserMovieScore();
					score.setUserId(Integer.parseInt(params[0]));
					score.setMovieId(Integer.parseInt(params[1]));
					score.setScore(Integer.parseInt(params[2]));
					score.setDate(params[3]);
					score.setIfTestData(ifTestData);
					userMovieScoreService.save(score);
			}
			System.out.println("评分数据初始化完毕！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
	
}
