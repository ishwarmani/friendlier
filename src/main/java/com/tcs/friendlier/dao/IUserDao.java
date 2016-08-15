package com.tcs.friendlier.dao;

import java.util.List;

import com.tcs.friendlier.pojo.Messages;
import com.tcs.friendlier.pojo.Post;
import com.tcs.friendlier.pojo.User;

public interface IUserDao {

	public int save(User user);

	public User findUserById(int id, String password);
	
	public User findUserById(int id);

	public User findUserByEmail(String email, String password);
	
	public User findUserByEmail(String email);
	
	public boolean updateStatus(int writerId,String content);

	public List<User> getUserList();

	public List<Post> getPostList();

	public void sendRequest(int senderId, int recieverId);
	
	public void updateRequest(int senderId, int friendId);
	
	public void declineRequest(int senderId, int friendId);

	public List<User> getFriendRequests(int id);

	public List<User> getFriends(int id);

	public void updateMessages(Messages msg);

	public List<Messages> getReceivedMessages(int id);

	public List<Messages> getSentMessages(int id);
	
	public List<Messages> getChat(int senderId,int receiverId);
}
