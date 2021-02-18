package kr.or.ddit.user.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.test.config.ModelTestConfig;
import kr.or.ddit.user.model.UserVo;

public class UserServiceTest extends ModelTestConfig{

	@Resource(name="userService")
	private UserService userService;
	
	@Before
	public void setup() {
		// 테스트에서 사용할 신규 사용자 추가
		UserVo userVo = new UserVo("testUser", "테스트용", "1234", new Date(), "테스트",
							 		"대전시 중구 중앙로79", "4층", "34940", "brown.png", "uuid-generated-filename.png");
		
		userService.insertUser(userVo);
		
		// 신규 입력  테스트를 위해 테스트 과정에서 입력된 데이터를 삭제
		userService.deleteUser("ddit_s");
	}
	
	@After
	public void tearDown() {
		userService.deleteUser("testUser");
	}
	
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
		Map<String , Object> map = userService.selectPagingUser(page);
		List<UserVo> userList = (List<UserVo>) map.get("userList");
		
		/***Then***/
		assertEquals(5, userList.size());
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
