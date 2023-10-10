package net.daum.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "member")//member테이블 이름 지정
@EqualsAndHashCode//equals(), hashcode(), canEqual() 메서드 자동제공
public class MemberVO {
	
	@Id //기본키
	private String mem_id;//회원아이디
	
	private String mem_pwd;//비번
	private String mem_name;//회원이름
	private String mem_zip;//우편번호
	private String mem_zip2;//우편번호
	private String mem_addr;//주소
	private String mem_addr2;//나머지 주소
	private String mem_phone01;//첫번째 폰번호
	private String mem_phone02;
	private String mem_phone03;
	private String mail_id;//메일 아이디
	private String mail_domain;//메일 도메인
	
	@CreationTimestamp//회원가입시점 날짜 기록(mybatis에서는 실행 X, 하이버네이트 특별기능)
	private Timestamp mem_date; //가입날짜
	
	private int mem_state; //가입회원이면 1, 탈퇴회원이면2
	private String mem_delcont; //탈퇴사유
	
	private Timestamp mem_deldate; //탈퇴날짜

}
