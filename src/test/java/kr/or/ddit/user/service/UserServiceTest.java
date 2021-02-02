package kr.or.ddit.user.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.test.config.ModelTestConfig;
import kr.or.ddit.user.model.UserVo;

public class UserServiceTest extends ModelTestConfig{

	@Resource(name="userService")
	private UserService userService;
	
	@Test
	public void getUserTest() {
		/***Given***/
		String userid = "brown";

		/***When***/
		UserVo userVo = userService.selectUser(userid);

		/***Then***/
		assertEquals("브라운", userVo.getUsernm());
	}

	// 전체 사용자 조회 테스트
	@Test
	public void selectAllUserTest() {
		/***Given***/
		
		/***When***/
		List<UserVo> userList = userService.selectAllUser();

		/***Then***/
		assertEquals(20, userList.size());
	}
	
	// 페이징 처리하여 사용자 정보 조회
	@Test
	public void selectPagingUserTest() {
		
		/***Given***/
		PageVo page = new PageVo(2, 5);
		
		/***When***/
		List<UserVo> userList = userService.selectPagingUser(page);
		
		/***Then***/
		assertEquals(5, userList.size());
	}
	
	@Test
	public void selectAllUserCntTest() {
		/***Given***/

		/***When***/
		int userCnt = userService.selectAllUserCnt();

		/***Then***/
		assertEquals(19,userCnt);
	}
	@Test
	public void modifyUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit","대덕인재", "dditPass", new Date(),	"개발원n", "대전시 중구 중앙로79", "4층", "34940", "brown.png", "uuid-generated-filename.png");
		
		/***When***/
		int updateCnt = userService.modifyUser(userVo);

		/***Then***/
		assertEquals(1, updateCnt);
	}
	
	@Test
	public void registUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit_s", "대덕인재", "dditPass", new Date(), 
										"개발원_m", "대전시 중구 중앙로79", "4층", "34940", "brown.png", "uuid-generated-filename.png");

		/***When***/
		int insertCnt = userService.insertUser(userVo);
		

		/***Then***/
		assertEquals(1, insertCnt);
	}
	
	@Test
	public void deleteUserTest() {
		/***Given***/
		String userid = "ddit_s";
		
		/***When***/
		int deleteCnt = userService.deleteUser(userid);

		/***Then***/
		assertEquals(1, deleteCnt);
	}
}
