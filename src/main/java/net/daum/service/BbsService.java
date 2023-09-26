package net.daum.service;

import java.util.List;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

public interface BbsService {

	void insertBbs(BbsVO b);

	int getRowCount();

	List<BbsVO> getBbsList(PageVO p);

}
