package com.tcs.friendlier.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tcs.friendlier.pojo.User;
import com.tcs.friendlier.service.IService;

@Controller
public class HomeController {

	@Autowired
	private IService service;
	
	List<User> data = new ArrayList<User>();
	
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
		data = service.getUserList();
		for (User user : data) {
			System.out.println(user.getName());
		}
		modelAndView.setViewName("home");
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
		System.out.println("dummy: " + dummy);
		System.out.println(email_id + "/////" + password);
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
		System.out.println(content);
		int writerId = user.getId();
		boolean flag = service.updateStatus(writerId, content);
		System.out.println(flag);
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}
	
	@RequestMapping(value = "/getMembers", method = RequestMethod.GET)
	public @ResponseBody List<User> getUsers(@RequestParam String name) {
		
		System.out.println("hello i m inside getMembers");
		List<User> result = new ArrayList<User>();
		for (User user : data) {
			if (user.getName().contains(name)) {
				result.add(user);
			}
		}
		for (User user : result) {
			System.out.println(user.getName());
		}
		return result;
		
		
	}
}
