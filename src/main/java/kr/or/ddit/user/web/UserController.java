package kr.or.ddit.user.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.service.UserService;


@Controller
@RequestMapping("user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping("allUser")
	public String allUser(Model model) {
		
		model.addAttribute("userList", userService.selectAllUser());
		
		return "user/allUser";
	}
	
	@RequestMapping("pagingUser")
	public String pagingUser(@RequestParam(defaultValue = "1") int page, 
							 @RequestParam(defaultValue = "5") int pageSize, 
							 Model model) {
		
		PageVo pageVo = new PageVo(page, pageSize);
		
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		
//		int userCnt = (int)map.get("userCnt");
//		int pagination = (int)Math.ceil((double)userCnt/pageSize);
		
//		model.addAttribute("userList", map.get("userList"));
//		model.addAttribute("pagination", pagination);
//		model.addAttribute("pageVo", pageVo);
		
		return "user/pagingUser";
	}
	
	//@RequestMapping("pagingUser")
	public String pagingUser(PageVo pageVo) {
		
		logger.debug("pageVo: {} ", pageVo);
		
		return "";
	}
	
	@RequestMapping("view")
	public String user(String userid, Model model) {
		
		model.addAttribute("user", userService.selectUser(userid));
		
		return "user/user";
	}
	
	@RequestMapping(path = "modify", method=RequestMethod.GET)
	public String modify(String userid, Model model) {
		
		model.addAttribute("user", userService.selectUser(userid));
		
		return "user/userModify";
	}
	@RequestMapping(path="regist", method = RequestMethod.GET)
	public String regist() {
		return "user/userRegist";
	}
	
	@RequestMapping(path="regist", method = RequestMethod.POST)
	public String regist(UserVo userVo, Model model,MultipartFile profile) {
		
		int insertCnt = 0;
		if(profile.getSize() > 0) {
			String originalFilename = profile.getOriginalFilename();
			String filename = UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			
			userVo.setFilename(originalFilename);
			userVo.setRealfilename("d:\\upload\\" + filename);
			
			try {
				profile.transferTo(new File(userVo.getRealfilename()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		//userVo.setReg_dt(new Date());
		try {
			insertCnt = userService.insertUser(userVo);
		} catch (Exception e) {
			e.printStackTrace();
			insertCnt = 0;
		}
		if(insertCnt == 1) {
			return "redirect:/user/pagingUser";
		} 
		else {	
			return "user/userRegist";
		}
		
	}

	@RequestMapping(path="modify", method=RequestMethod.POST )
	public String modify(UserVo userVo, Model model, RedirectAttributes ra, MultipartFile profile) {
		
		logger.debug("userVo : {} " ,userVo);
		
		int updateCnt = 0;
		if(profile.getSize() > 0) {
			String originalFilename = profile.getOriginalFilename();
			String filename = UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			
			userVo.setFilename(originalFilename);
			userVo.setRealfilename("d:\\upload\\" + filename);
			
			try {
				profile.transferTo(new File(userVo.getRealfilename()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		try {
			updateCnt = userService.modifyUser(userVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(updateCnt == 1) {
			ra.addAttribute("userid", userVo.getUserid());
			return "redirect:/user/view";
		} else {
			return "user/usermodify";
			//return modify(userVo.getUserid(), model);
		}
		
	}
	
	@RequestMapping("delete")
	public String delete(String userid) {
		int deleteCnt = 0;
		
		try {
			deleteCnt = userService.deleteUser(userid);
		} catch (Exception e) {
			deleteCnt = 0;
		}
		
		if(deleteCnt == 1) {
			return "redirect:/user/pagingUser";
		} else {
			return "redirect:/user/view?userid="+userid;
		}
	}
	
}

