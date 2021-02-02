package kr.or.ddit.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.repository.UserDao;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Resource(name="userDao")
	private UserDao userDao;
	
	public UserServiceImpl() {}
	
	public UserServiceImpl(UserDao userDao) {
		this.userDao=userDao;
	}
	@Override
	public UserVo selectUser(String userid) {
		return userDao.selectUser(userid);
	}
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<UserVo> selectAllUser() {
		return userDao.selectAllUser();
	}

	@Override
	public List<UserVo> selectPagingUser(PageVo vo) {
		return userDao.selectPagingUser(vo);
	}

	@Override
	public int selectAllUserCnt() {
		return userDao.selectAllUserCnt();
	}

	@Override
	public int modifyUser(UserVo userVo) {
		return userDao.modifyUser(userVo);
	}

	@Override
	public int insertUser(UserVo userVo) {
		return userDao.insertUser(userVo);
	}

	@Override
	public int deleteUser(String userid) {
		return userDao.deleteUser(userid);
	}
	
}
