package kr.or.ddit.user.repository;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.test.config.ModelTestConfig;
import kr.or.ddit.user.model.UserVo;

// ������ ȯ�濡�� junit �ڵ带 ���� ==> junit �ڵ嵵 ������ ������ ���

public class UserDaoTest extends ModelTestConfig{

	@Resource(name="userDao")
	private UserDao userDao;
	
	@Resource(name="dataSource")
	private DataSource dataSource;
	
	@Before
	public void setup() {
		// initData.sql�� ���� : ���������� �����ϴ� ResourceDatabasePopulator
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		
		// populator�� ���� �����ų sql ������ ����
		populator.addScript(new ClassPathResource("/kr/or/ddit/config/db/initData.sql"));
		
		// script ������ �����ϴ� ������ �߻��� ��� 
		// ���̻� �������� �ʰ� ���߰� ����
		populator.setContinueOnError(false);
		
		// populator�� ����
		DatabasePopulatorUtils.execute(populator, dataSource);
	}
	
	
	@Test
	public void getUserTest() {
		/***Given***/
		String userid = "brown";

		/***When***/
		UserVo userVo = userDao.selectUser(userid);

		/***Then***/
		assertEquals("����", userVo.getUsernm());
	}

	// ��ü ����� ��ȸ �׽�Ʈ
	@Test
	public void selectAllUserTest() {
		/***Given***/
		
		/***When***/
		List<UserVo> userList = userDao.selectAllUser();

		/***Then***/
		assertEquals(16, userList.size());
	}
	
	// ����¡ ó���Ͽ� ����� ���� ��ȸ
	@Test
	public void selectPagingUserTest() {
		
		/***Given***/
		PageVo page = new PageVo(2, 5);
		
		/***When***/
		List<UserVo> userList = userDao.selectPagingUser(page);
		
		/***Then***/
		assertEquals(5, userList.size());
	}
	
	@Test
	public void selectAllUserCntTest() {
		/***Given***/

		/***When***/
		int userCnt = userDao.selectAllUserCnt();

		/***Then***/
		assertEquals(16, userCnt);
	}
	@Test
	public void modifyUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit","�������", "dditPass", new Date(),	"���߿�n", "������ �߱� �߾ӷ�79", "4��", "34940", "brown.png", "uuid-generated-filename.png");
		
		/***When***/
		int updateCnt = userDao.modifyUser(userVo);

		/***Then***/
		assertEquals(1, updateCnt);
	}
	
	@Test
	public void registUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit_n", "�������", "dditPass", new Date(), 
										"���߿�_m", "������ �߱� �߾ӷ�79", "4��", "34940", "brown.png", "uuid-generated-filename.png");

		/***When***/
		int insertCnt = userDao.insertUser(userVo);
		

		/***Then***/
		assertEquals(1, insertCnt);
	}
	
	@Test
	public void deleteUserTest() {
		/***Given***/
		String userid = "ddit_n";
		
		/***When***/
		int deleteCnt = userDao.deleteUser(userid);

		/***Then***/
		assertEquals(1, deleteCnt);
	}
}
