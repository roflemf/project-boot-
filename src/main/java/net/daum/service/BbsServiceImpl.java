package net.daum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.daum.dao.BbsDAO;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Service//Control과 연결하는 중간 매개체 역할
public class BbsServiceImpl implements BbsService {
	
	@Autowired
	private BbsDAO bbsDao;

	@Override
	public void insertBbs(BbsVO b) {
		bbsDao.insertBbs(b);
	}

	@Override
	public int getRowCount() {
		return this.bbsDao.getRowCount();
	}

	@Override
	public List<BbsVO> getBbsList(PageVO p) {
		
		return bbsDao.getBbsList(p);
	}
	
	
	
	

}
