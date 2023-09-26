package net.daum.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Repository //spring mvc에서 modeldao로 인식하게 해줌 
public class BbsDAOImpl implements BbsDAO {//spring MVC에서 모델 dao로 인식함 -> 자료실
	
	@Autowired
	private SqlSession sqlSession; //mybatis 쿼리문 수행 sqlSession 의존성(DI) 주입
	
	@Autowired
	private BbsRepository bbsRepo; //사용자 자료실 JPA를 실행하기 위한 자동 의존성 주입

	@Override
	public void insertBbs(BbsVO b) {
		//this.sqlSession.insert("bbs_in",b); //mybatis에서 insert()는 레코드를 저장시킨다.
		//bbs_in은 bbs.xml에서 설정할 유일한 아이디명이다.
		
		int bbs_no = this.sqlSession.selectOne("bbsNoSeq_Find");//시퀀스 번호값 구함
		//mybatis에서 selectOne()메서드는 한개의 레코드값만 반환
		//bbsNoSeq_Find는 bbs.xml에서 설정할 유일한 아이디명
		
		System.out.println("\n=========================> JPA로 자료실 저장");
		b.setBbs_no(bbs_no);//번호값 저장
		b.setBbs_ref(bbs_no);//글 그룹번호를 저장
		
		this.bbsRepo.save(b); //JPA로 하이버네이트 통해 자료실 저장
	}//자료실 저장

	@Override
	public int getRowCount() {
	
		return this.sqlSession.selectOne("bbs_count");
	}//검색 전후 총 레코드 개수

	@Override
	public List<BbsVO> getBbsList(PageVO p) {
		return this.sqlSession.selectList("bbs_list", p);
		//mybatis에서 selectList()메서드는 하나 이상의 레코드를 검색해 컬렉션 List로 변환
	}//검색 전후 목록

}
