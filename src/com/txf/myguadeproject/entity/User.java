package com.txf.myguadeproject.entity;

public class User {
	private int id;
	private String userName;
	private String nick;
	private String photo;
	private String phone;
	private String sex;
	private String birth;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int id, String userName, String nick, String photo,
			String phone, String sex, String birth) {
		super();
		this.id = id;
		this.userName = userName;
		this.nick = nick;
		this.photo = photo;
		this.phone = phone;
		this.sex = sex;
		this.birth = birth;
	}

}
