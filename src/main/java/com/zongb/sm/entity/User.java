package com.zongb.sm.entity;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 3124180411765674666L;
	private String id ;
	private String name ;
	private String pass;
	private int age ;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[name="+this.name+",pass="+this.pass+",age="+this.age+"]";
	}
}
