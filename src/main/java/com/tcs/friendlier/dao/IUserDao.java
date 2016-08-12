package com.tcs.friendlier.dao;

import java.util.List;

import com.tcs.friendlier.pojo.User;

public interface IUserDao {

	public int save(User user);

	public User findUserById(int id, String password);

	public User findUserByEmail(String email, String password);
	
	public boolean updateStatus(int writerId,String content);

	public List<User> getUserList();
}
