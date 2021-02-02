package kr.or.ddit.user.web;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.test.config.WebTestConfig;

public class UserControllerTest extends WebTestConfig {

	@Test
	public void allUserTest() throws Exception {
		mockMvc.perform(get("/user/allUser"))
				.andExpect(view().name("user/allUser"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("userList"))
				.andDo(print());
	}

	@Test
	public void pagingUserTest() throws Exception {
		mockMvc.perform(get("/user/pagingUser"))
				.andExpect(view().name("user/pagingUser"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("userList"))
				.andExpect(model().attributeExists("pageVo"))
				.andExpect(model().attributeExists("pagination"))
				.andDo(print());
	}
	
	@Test
	public void pagingUserTest2() throws Exception {
		mockMvc.perform(get("/user/pagingUser").param("page", "2"))
				.andExpect(view().name("user/pagingUser"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("userList"))
				.andExpect(model().attributeExists("pageVo"))
				.andExpect(model().attributeExists("pagination"))
				.andDo(print());
	}
	
	@Test
	public void viewTest() throws Exception {
		mockMvc.perform(get("/user/view").param("userid", "sally"))
			.andExpect(view().name("user/user"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("user"))
			.andDo(print());
	}
	
	@Test
	public void modifyGetTest() throws Exception {
		mockMvc.perform(get("/user/modify").param("userid", "sally"))
			.andExpect(view().name("user/userModify"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("user"))
			.andDo(print());
	}
	
	@Test
	public void userRegistGetTest() throws Exception {
		mockMvc.perform(get("/user/regist"))
			.andExpect(view().name("user/userRegist"))
			.andExpect(status().isOk())
			.andDo(print());
	}
}

