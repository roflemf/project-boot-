package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.daum.vo.MemberVO;
																//primarykey 타입
public interface MemberRepository extends JpaRepository<MemberVO, String>{

}
