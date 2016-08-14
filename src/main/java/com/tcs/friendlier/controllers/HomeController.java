package com.tcs.friendlier.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tcs.friendlier.pojo.Messages;
import com.tcs.friendlier.pojo.Post;
import com.tcs.friendlier.pojo.User;
import com.tcs.friendlier.service.IService;

@Controller
public class HomeController {

	@Autowired
	private IService service;
	
	List<User> data = new ArrayList<User>();
	
	User sUser = null;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(HttpSession httpSession) {
		User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (loggedInUser == null) {
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("loginRegister");
			return modelAndView;
		}
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(HttpSession httpSession) {
		User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (loggedInUser == null) {
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("redirect:/loginRegister");
			return modelAndView;
		}
		List<Post> feed = service.getPostList();
	//	List<PostCopy> feed2 = service.getPostList();
		List<User> friendRequests = service.getFriendRequests(loggedInUser.getId());
		List<User> friends = service.getFriends(loggedInUser.getId());
		for (User user : friendRequests) {
			System.out.println(user.getName());
		}
		
		modelAndView.addObject("feed",feed);
		modelAndView.addObject("friendRequests",friendRequests);
		modelAndView.addObject("friends",friends);
		data = service.getUserList();
		if(sUser == null){
			sUser = loggedInUser;
			httpSession.setAttribute("sUser", loggedInUser);
		}
		modelAndView.setViewName("home");
		return modelAndView;
	}
	
	@RequestMapping(value = "/findUser", method = RequestMethod.POST)
	public ModelAndView findUser(@RequestParam("name") String name,HttpSession httpSession) {
		User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (loggedInUser == null) {
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("redirect:/loginRegister");
			return modelAndView;
		}
	//	String selectedName = name.split(",")[0];
		String selectedEmail = name.split(",   ")[1];
		System.out.println("hey this the selected email");
		System.out.println(selectedEmail);
		sUser = service.findUserByEmail(selectedEmail);
		httpSession.setAttribute("sUser", sUser);
		
		System.out.println("session "+httpSession.getAttribute("sUser"));
		System.out.println("sUser normal "+ sUser);
//		modelAndView.addObject("sUser", sUser);
//		System.out.println("sUser of findUser post "+ sUser.getName() + " " + sUser.getId());
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}
	@RequestMapping(value = { "/loginRegister", "/login", "register" }, method = RequestMethod.GET)
	public ModelAndView register(HttpSession httpSession) {
		User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (loggedInUser == null) {
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("loginRegister");
			return modelAndView;
		}
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(@ModelAttribute User user, BindingResult result, HttpSession httpSession) {
		if (result.hasErrors()) {
			System.out.println("Number of errors: " + result.getErrorCount());
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors) {
				System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
			}
		}
		ModelAndView modelAndView = new ModelAndView();
		User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			modelAndView.setViewName("redirect:/home");
			return modelAndView;
		}

		int success = service.save(user);
		if (success == 1) {
			httpSession.setAttribute("loggedInUser", user);
			if(sUser == null){
				sUser = loggedInUser;
				httpSession.setAttribute("sUser", loggedInUser);
			}
			modelAndView.setViewName("redirect:/home");
			return modelAndView;
		} else {
			if (success == -1)
				modelAndView.addObject("msg", "This email already exists!! Please choose another one");
			else
				modelAndView.addObject("msg", "Something went wrong! Please try again.");

			modelAndView.setViewName("loginRegister");
			return modelAndView;
		}

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute User dummy, @RequestParam("email_id") String email_id,
			@RequestParam("password") String password, HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (user != null) {
			modelAndView.setViewName("redirect:/home");
			return modelAndView;
		}
//		System.out.println("dummy: " + dummy);
//		System.out.println(email_id + "/////" + password);
		try {
			int id = Integer.parseInt(email_id);
			user = service.findUserById(id, password);
		} catch (NumberFormatException numberFormatException) {
			user = service.findUserByEmail(email_id, password);
		}
		System.out.println("user: " + user);
		if (user == null) {
			modelAndView.addObject("msg", "Invalid Credentials!");
			modelAndView.setViewName("loginRegister");
			return modelAndView;
		}
		httpSession.setAttribute("loggedInUser", user);
		if(sUser == null){
			sUser = user;
			httpSession.setAttribute("sUser", user);
		}
		System.out.println(user.getName());
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession httpSession) {
		httpSession.invalidate();
		return "redirect:/loginRegister";
	}
	
	@RequestMapping(value = "/status", method = RequestMethod.POST)
	public ModelAndView statusUpdate(@ModelAttribute User dummy, @RequestParam("content") String content, HttpSession httpSession) {
		User user = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (user == null) {
			modelAndView.addObject("msg", "login is required");
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
	//	System.out.println(content);
		int writerId = user.getId();
		boolean flag = service.updateStatus(writerId, content);
	//	System.out.println(flag);
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}
	
	@RequestMapping(value = "/getMembers", method = RequestMethod.GET)
	public @ResponseBody List<User> getUsers(@RequestParam String name) {
		
	//	System.out.println("hello i m inside getMembers");
		List<User> result = new ArrayList<User>();
		for (User user : data) {
			if (user.getName().contains(name)) {
				result.add(user);
			}
		}
		/*for (User user : result) {
			System.out.println(user.getName());
		}*/
		return result;
		
	}
	
	@RequestMapping(value="/addFriend",method=RequestMethod.GET)
	public ModelAndView addFriend(HttpSession httpSession){
		User user = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (user == null) {
			modelAndView.addObject("msg", "login is required");
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		System.out.println("i m inside addFriend");
		int senderId = ((User)httpSession.getAttribute("loggedInUser")).getId();
		int recieverId = ((User)httpSession.getAttribute("sUser")).getId();
		service.sendRequest(senderId,recieverId);
		
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/acceptRequest/{id}",method=RequestMethod.GET)
	public ModelAndView acceptRequest(@PathVariable String id, HttpSession httpSession){
		User user = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("value in pathVarible is " + id);
		if (user == null) {
			modelAndView.addObject("msg", "login is required");
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		int friendId = ((User)httpSession.getAttribute("loggedInUser")).getId();
		int senderId = Integer.parseInt(id);
		service.updateRequest(senderId,friendId);
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
		}
	
	@RequestMapping(value="/decline",method=RequestMethod.GET)
	public ModelAndView decline(User friend, HttpSession httpSession){
		User user = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (user == null) {
			modelAndView.addObject("msg", "login is required");
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		int senderId = ((User)httpSession.getAttribute("loggedInUser")).getId();
		int friendId = friend.getId();
		service.declineRequest(senderId,friendId);
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
		}
	
	@RequestMapping(value="/sendMessage/{id}",method=RequestMethod.POST)
	public ModelAndView sendMessage(@PathVariable String id,@RequestParam("message") String message,  HttpSession httpSession){
		User user = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("value in pathVarible is " + id);
		if (user == null) {
			modelAndView.addObject("msg", "login is required");
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		int recieverId = Integer.parseInt(id);
		Messages msg = new Messages();
		msg.setRecieverId(recieverId);
		msg.setMessage(message);
		msg.setSenderId(user.getId());
		service.updateMessages(msg);
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}
	
	@RequestMapping(value="/messages",method=RequestMethod.GET)
	public ModelAndView showMessages(HttpSession httpSession){
		User user = (User) httpSession.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView();
		if (user == null) {
			modelAndView.addObject("msg", "login is required");
			modelAndView.setViewName("redirect:/login");
			return modelAndView;
		}
		
		List<Messages> msgs = service.getAllMessages(user.getId());
		modelAndView.addObject("msgs",msgs);
		modelAndView.setViewName("messages");
		return modelAndView;
		}
}
