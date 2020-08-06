package com.poweredbypace.pace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.user.Role;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.domain.user.User.UserStatus;
import com.poweredbypace.pace.repository.UserRepository;
import com.poweredbypace.pace.service.CrudService;
import com.poweredbypace.pace.service.UserService;

@Controller
@RequestMapping(value = "/api/user")
public class UserCrudController extends CrudController<User> {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(value = "/current", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public User get() {
		return userService.getCurrentUser();
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public List<User> seach(@RequestParam(required=false) String q,
			@RequestParam(required=false) UserStatus[] status,
			@RequestParam(required=false) Long group,
			@RequestParam(required=false) String role,
			@RequestParam(required=false) Integer pageIndex,
			@RequestParam(required=false) Integer pageSize) {
		
		if ("user".equals(role)) role = Role.ROLE_USER;
		else if ("admin".equals(role)) role = Role.ROLE_ADMIN;
		
		if (status==null) {
			status = new UserStatus[] { UserStatus.New, UserStatus.Verified, 
					UserStatus.Enabled, UserStatus.Suspended };
		}
		
		return userRepo.findByQuery(q, status, group, role,
			new PageRequest(pageIndex, pageSize, new Sort(Direction.ASC, "status")));
		
	}
	
	@RequestMapping(value = "", params={"email"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public User getByEmail(@RequestParam String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	protected CrudService<User> getService() {
		return userService;
	}
	
	@Override
	protected JpaRepository<User, Long> getRepository() {
		return userRepo;
	}

}