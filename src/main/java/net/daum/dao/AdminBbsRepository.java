package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.BbsVO;

public interface AdminBbsRepository extends JpaRepository<BbsVO, Integer> {
	
	@Modifying //@Query 애노테이션; select 검색쿼리만 가능하지만 
	//애노테이션을 이용해서 DML(insert,update,delete)문 작업처리도 가능
	@Query("update BbsVO b set b.bbs_name=?1, b.bbs_title=?2, b.bbs_cont=?3"
			+", b.bbs_file=?4 where b.bbs_no=?5")
	//?1은 첫번째로 전달되어지는 인자값, ?2는 두번째로 전달되어지는 인자값
	//JPQL(Java Persistance Query Language; JPA를 사용하는 쿼리문)
	//JPQL문에서는 실제 테이블명 대신 엔티티빈 클래스명을 사용, 
	//실제 컬럼명 대신 엔티티빈 클래스의 속성명인 변수명을 사용
	public void adminEditBbs(String name, String title, String cont, String fileName,
			int bno);
	

}
