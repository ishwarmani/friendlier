package com.tcs.friendlier.service;

import com.tcs.friendlier.pojo.User;

public interface IService {

	public int save(User user);

	public User findUserById(int id, String password);

	public User findUserByEmail(String email, String password);
	
	public boolean updateStatus(int writerId,String content);
}
