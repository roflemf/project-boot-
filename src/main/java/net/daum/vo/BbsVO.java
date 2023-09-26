package net.daum.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter //getter() 메서드 자동제공
@Setter //setter() 메서드 자동제공
@ToString //toString() 메서드 자동제공
@Entity//엔티티빈 JPA
@Table(name="bbs") //bbs테이블명 지정
@EqualsAndHashCode(of="bbs_no")//equals(), hashcode(),canEqual()메서드 자동 생성
public class BbsVO {//자료실 엔티티빈 클래스
	
	@Id //기본키 지정 => 식별키
	private int bbs_no; //자료실 번호 =>JPA를 통해서 bbs_no컬럼이 생성 되고 primary key로 설정됌 (DB설계)
	
	private String bbs_name; //글쓴이
	private String bbs_title; //글제목
	private String bbs_pwd; //비번
	
	@Column(length = 4000) //컬럼 크기를 4000으로 설정
	private String bbs_cont;//내용
	
	private String bbs_file; //첨부 파일경로와 파일명
	private int bbs_hit; //조회수
	
	//계단형 계층형 자료실을 만들기 위해서 필요한 것 =>관리자 답변기능
	private int bbs_ref;//글 그룹 번호 => 원본글과 답변글을 묶어주는 기능
	private int bbs_step;//원본글과 답변글을 구분하는 번호값이면서 몇번쨰 답변글인가를 알려줌. 
						 //원본글이면 0, 첫번째 답변글이면1, 두번째 답변글이면 2
	private int bbs_level; //답변글 정렬순서
	
	@CreationTimestamp //등록시점의 날짜값 기록 (orm 하이버네이트의 기능)
					   //mybatis에서는 작동 X. jpa 를 통한 하이버네이트에서만 작동.
	private Timestamp bbs_date; //글 등록 날짜
	
	
	 
}
