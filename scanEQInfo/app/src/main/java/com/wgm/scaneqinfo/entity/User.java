package com.wgm.scaneqinfo.entity;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private int openId;
	private String mobile;
	private String username;
	private String pwd;
	private String name;
	private String headerImg;
	private String signature;
	private int sex;
	private int schoolId;
	private String school;
	private int professionId;
	private String profession;
	private String hobby;
	private String localHeaderImg;
	private int remberMe;
	private int backgroundColor;
	private int receive;
	private boolean hasLogin = false;
	
	private int level;	//用户等级
	private int score;	//积分
	
	public int getOpenId() {
		return openId;
	}
	public void setOpenId(int openId) {
		this.openId = openId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public boolean isHasLogin() {
		return hasLogin;
	}
	public void setHasLogin(boolean hasLogin) {
		this.hasLogin = hasLogin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeaderImg() {
		return headerImg;
	}
	public void setHeaderImg(String headerImg) {
		this.headerImg = headerImg;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}
	public int getProfessionId() {
		return professionId;
	}
	public void setProfessionId(int professionId) {
		this.professionId = professionId;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	public String getLocalHeaderImg() {
		return localHeaderImg;
	}
	public void setLocalHeaderImg(String localHeaderImg) {
		this.localHeaderImg = localHeaderImg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getRemberMe() {
		return remberMe;
	}
	public void setRemberMe(int remberMe) {
		this.remberMe = remberMe;
	}
	public int getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public int getReceive() {
		return receive;
	}
	public void setReceive(int receive) {
		this.receive = receive;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
