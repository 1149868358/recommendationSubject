package com.cn.bjut.pojo;

import java.util.Date;

/**
 * 电影信息实体
 * @author wkx
 *
 */
public class Movie {

	/**
	 *   movie id | movie title | release date | video release date |
              IMDb URL | unknown | Action | Adventure | Animation |
              Children's | Comedy | Crime | Documentary | Drama | Fantasy |
              Film-Noir | Horror | Musical | Mystery | Romance | Sci-Fi |
              Thriller | War | Western |
	 */
	private int movieId;
	private String movieTitle;
	private String releaseDate; //发布日期
	private String videoReleaseDate; //视频上传日期
	private String url; //链接
	//以下属性为电影类型分类
	private boolean unknown;
	private boolean Action;
	private boolean Adventure;
	private boolean Animation;
	private boolean Childrens;
	private boolean Comedy;
	private boolean Crime;
	private boolean Documentary;
	private boolean Drama;
	private boolean Fantasy;
	private boolean FilmNoir;
	private boolean Horror;
	private boolean Musical;
	private boolean Mystery;
	private boolean Romance;
	private boolean SciFi; //科幻
	private boolean Thriller;
	private boolean War;
	private boolean Western;
	
	
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getVideoReleaseDate() {
		return videoReleaseDate;
	}
	public void setVideoReleaseDate(String videoReleaseDate) {
		this.videoReleaseDate = videoReleaseDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isUnknown() {
		return unknown;
	}
	public void setUnknown(boolean unknown) {
		this.unknown = unknown;
	}
	public boolean isAction() {
		return Action;
	}
	public void setAction(boolean action) {
		Action = action;
	}
	public boolean isAdventure() {
		return Adventure;
	}
	public void setAdventure(boolean adventure) {
		Adventure = adventure;
	}
	public boolean isAnimation() {
		return Animation;
	}
	public void setAnimation(boolean animation) {
		Animation = animation;
	}
	public boolean isChildrens() {
		return Childrens;
	}
	public void setChildrens(boolean childrens) {
		Childrens = childrens;
	}
	public boolean isComedy() {
		return Comedy;
	}
	public void setComedy(boolean comedy) {
		Comedy = comedy;
	}
	public boolean isCrime() {
		return Crime;
	}
	public void setCrime(boolean crime) {
		Crime = crime;
	}
	public boolean isDocumentary() {
		return Documentary;
	}
	public void setDocumentary(boolean documentary) {
		Documentary = documentary;
	}
	public boolean isDrama() {
		return Drama;
	}
	public void setDrama(boolean drama) {
		Drama = drama;
	}
	public boolean isFantasy() {
		return Fantasy;
	}
	public void setFantasy(boolean fantasy) {
		Fantasy = fantasy;
	}
	public boolean isFilmNoir() {
		return FilmNoir;
	}
	public void setFilmNoir(boolean filmNoir) {
		FilmNoir = filmNoir;
	}
	public boolean isHorror() {
		return Horror;
	}
	public void setHorror(boolean horror) {
		Horror = horror;
	}
	public boolean isMusical() {
		return Musical;
	}
	public void setMusical(boolean musical) {
		Musical = musical;
	}
	public boolean isMystery() {
		return Mystery;
	}
	public void setMystery(boolean mystery) {
		Mystery = mystery;
	}
	public boolean isRomance() {
		return Romance;
	}
	public void setRomance(boolean romance) {
		Romance = romance;
	}
	public boolean isSciFi() {
		return SciFi;
	}
	public void setSciFi(boolean sciFi) {
		SciFi = sciFi;
	}
	public boolean isThriller() {
		return Thriller;
	}
	public void setThriller(boolean thriller) {
		Thriller = thriller;
	}
	public boolean isWar() {
		return War;
	}
	public void setWar(boolean war) {
		War = war;
	}
	public boolean isWestern() {
		return Western;
	}
	public void setWestern(boolean western) {
		Western = western;
	}
	
	
	
}
