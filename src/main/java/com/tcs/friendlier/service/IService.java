package com.tcs.friendlier.service;

import java.util.List;

import com.tcs.friendlier.pojo.Messages;
import com.tcs.friendlier.pojo.Post;
import com.tcs.friendlier.pojo.User;
import com.tcs.friendlier.util.MessagesCopy;
import com.tcs.friendlier.util.PostCopy;

public interface IService {

	public int save(User user);

	public User findUserById(int id, String password);
	/*
	 * for login authentication
	 */
	public User findUserByEmail(String email, String password);
	
	/*
	 * for searching a user
	 */
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

	public List<Messages> getAllMessages(int id);

	public List<PostCopy> getPostCopyList();

	public List<MessagesCopy> getAllMessagesCopy(int id);
}
