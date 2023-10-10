package net.daum.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Repository
public class AdminBbsDAOImpl implements AdminBbsDAO{

	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private AdminBbsRepository adminBbsRepo;

	@Override
	public int getListCount(PageVO p) {
		
		return this.sqlSession.selectOne("abbs_count", p);
	}//관리자 자료실 검색전후 레코드 개수

	@Override
	public List<BbsVO> getAdminBbsList(PageVO p) {
		
		return sqlSession.selectList("abbs_list", p);
	}//관리자 자료실 검색 전후 목록

	@Override
	public void adminInsertBbs(BbsVO b) {
		
		//this.sqlSession.insert("abbs_in", b); 
		//mybatis 에서 insert()메서드는 레코드를 저장
		//abbs_in은 admin_bbs.xml에서 설정할 유일한 아이디명
		//b객체에 들어있는 내용; 글쓴이, 글제목, 내용, 비번, 첨부파일
		
		System.out.println("\n ==============> JPA로 관리자 자료실 저장");
		int bbs_no = this.sqlSession.selectOne("bbsNoSeq_Find"); //시퀀스로부터 번호값 구함
				
		b.setBbs_no(bbs_no); //자료실 번호 저장
		b.setBbs_ref(bbs_no); //글 그룹번호 저장
		
		this.adminBbsRepo.save(b); //JPA로 저장
	}//관리자 자료실 저장

	@Override
	public BbsVO getAdminBbsCont(int no) {
		
		//return sqlSession.selectOne("abbs_cont", no);
		//abbs_cont; mybatis에서 설정할 유일한 아이디명, mybatis에서 selectOne(); 단 한개의 레코드값만 반환
		
		System.out.println("\n =====================> JPA로 관리자 자료실 상세정보 보기");
		BbsVO bc = this.adminBbsRepo.getReferenceById(no); 
		//번호에 해당하는 레코드를 검색해서 엔티티빈 타입으로 반환
		return bc;
	}//관리자 자료실 상세정보와 수정폼

	@Transactional
	@Override
	public void adminUpdateBbs(BbsVO b) {
		
		//sqlSession.update("abbs_edit", b);//mybatis에서 updqte()메서드는 레코드 수정
		
		System.out.println("\n ===================================> JPA로 관리자 자료실 수정");
		this.adminBbsRepo.adminEditBbs(b.getBbs_name(), b.getBbs_title(), b.getBbs_cont(), 
									   b.getBbs_file(), b.getBbs_no());
	}//관리자 자료실 수정완료

	@Override
	public void adminBbsDel(int no) {
		
		//this.sqlSession.delete("abbs_del", no);
		//mybatis에서 delete()메서드는 레코드를 삭제한다.
		//abbs_del; 유일한 delete 아이디명
		
		System.out.println(" \n =========================> JPA로 관리자 자료실 삭제");
		adminBbsRepo.deleteById(no); //this. 생략, 번호 기준 삭제
	}//관리자 자료실 삭제
	
	
}
