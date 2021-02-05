package kr.or.ddit.user.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import kr.or.ddit.validator.UserVoValidator;


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
	
	@RequestMapping("allUserTiles")
	public String allUserTiles(Model model) {
		
		model.addAttribute("userList", userService.selectAllUser());
		
		return "tiles.user.allUser";
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
	
	@RequestMapping("pagingUserTiles")
	public String pagingUserTiles(@RequestParam(defaultValue = "1") int page, 
									 @RequestParam(defaultValue = "5") int pageSize, 
									 Model model) {
		
		PageVo pageVo = new PageVo(page, pageSize);
		
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		
		// tiles-definition에 설정한 name
		return "tiles.user.pagingUser";
	}
	
	// 사용자 리스트가 없는 상태의 화면만 응답으로 생성
	@RequestMapping("pagingUserAjaxView")
	public String pagingUserAjaxView() {
		return "tiles.user.pagingUserAjax";
	}
	@RequestMapping("pagingUserAjax")
	public String pagingUserAjax(@RequestParam(defaultValue = "1") int page, 
									 @RequestParam(defaultValue = "5") int pageSize, 
									 Model model) {
		
		PageVo pageVo = new PageVo(page, pageSize);
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		return "jsonView";
	}
	
	@RequestMapping("pagingUserAjaxHtml")
	public String pagingUserAjaxHtml(@RequestParam(defaultValue = "1") int page, 
									 @RequestParam(defaultValue = "5") int pageSize, 
									 Model model) {
		PageVo pageVo = new PageVo(page, pageSize);
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		return "user/pagingUserAjaxHtml";
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
	
	@RequestMapping("viewTiles")
	public String userTiles(String userid, Model model) {
		
		model.addAttribute("user", userService.selectUser(userid));
		
		return "tiles.user.user";
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
	@RequestMapping(path="registTiles", method = RequestMethod.GET)
	public String registTiles() {
		return "tiles.user.userRegist";
	}
	// bindingResult 객체는 command 객체 바로 뒤에 인자로 기술해야 한다
	@RequestMapping(path="regist", method = RequestMethod.POST)
	public String regist(@Valid UserVo userVo, BindingResult result ,Model model,MultipartFile profile) {
		
		//							  검증할 대상, 에러 
		//new UserVoValidator().validate(userVo, result);
		
		// 에러가 있는지 확인 후 있으면 등록 페이지로 포워드
		if(result.hasErrors()) {
			logger.debug("result has error");
			return "user/userRegist";
		}
		
		int insertCnt = 0;
		String originalFilename = "";
		String filename = "";
		
		if(profile.getSize() > 0) {
			originalFilename = profile.getOriginalFilename();
			filename = UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			
			userVo.setRealfilename("d:\\upload\\" + filename);
			
			try {
				profile.transferTo(new File(userVo.getRealfilename()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		} else {
			
			userVo.setRealfilename(filename);
		}
		userVo.setFilename(originalFilename);
		
		
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
	
	@RequestMapping("excelDownload")
	public String excelDownload(Model model) {
		
		List<String> header = new ArrayList<String>();
		header.add("사용자 아이디");
		header.add("사용자 이름");
		header.add("사용자 별명");
		
		model.addAttribute("header", header);
		model.addAttribute("data", userService.selectAllUser());
		
		return "userExcelDownloadView";
	}
	
	@RequestMapping("profile")
	public void profile(HttpServletResponse response, String userid, HttpServletRequest req) {
		
		response.setContentType("image");		
		
		UserVo userVo = userService.selectUser(userid);
		
		String path = "";
		if(userVo.getRealfilename() == null) {		
			// DB에 사진이 등록되지않았을 경우 image에 있는 unknown.png로 보여준다
			path = req.getServletContext().getRealPath("/image/unknown.png");
		} else {
			path = userVo.getRealfilename();
		}
		try {
			FileInputStream fis = new FileInputStream(path);
			ServletOutputStream sos = response.getOutputStream();
			
			byte[] buff = new byte[512];
			
			while(fis.read(buff) != -1) {
				sos.write(buff);
			}
			fis.close();
			sos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("profileDownload")
	public void profileDownload(HttpServletResponse response, String userid, HttpServletRequest req) {

		UserVo userVo = userService.selectUser(userid);
		
		String path = "";
		String filename="";
		if(userVo.getRealfilename() == null) {
			path = req.getServletContext().getRealPath("/image/unknown.png");
			filename = "unknown.png";
		} else {
			path = userVo.getRealfilename();
			filename = userVo.getFilename();
		}
		
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		try {
			FileInputStream fis = new FileInputStream(path);
			ServletOutputStream sos = response.getOutputStream();
			
			byte[] buff = new byte[512];
			
			while(fis.read(buff) != -1) {
				sos.write(buff);
			}
			
			fis.close();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

