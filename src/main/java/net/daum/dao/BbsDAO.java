package net.daum.dao;

import java.util.List;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

public interface BbsDAO {

	void insertBbs(BbsVO b);

	int getRowCount();

	List<BbsVO> getBbsList(PageVO p);

}
