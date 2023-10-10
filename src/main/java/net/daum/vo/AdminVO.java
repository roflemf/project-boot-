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
@Table(name="admin") //admin 관리자 테이블명 지정
@EqualsAndHashCode(of="admin_id")
//eqauls(), hashCode(), canEqual() 메서드 자동제공
//equals :  두 객체의 내용이 같은지, 동등성(equality) 를 비교하는 연산자
//hashCode : 두 객체가 같은 객체인지, 동일성(identity) 를 비교하는 연산자
//여기서는 세 메서드 이용은 X 테이블과 연결 때문에 이용
public class AdminVO {//관리자 엔티티빈 클래스

	private int admin_no; //번호
	
	@Id //기본키 컬럼(구분키, 식별키로 활용)
	private String admin_id;  //관리자 아이디
	private String admin_pwd; //관리자 비번
	private String admin_name; //관리자 이름
	
	@CreationTimestamp //등록시점 날짜값 기록, mybatis 실행시 구동 X
	private Timestamp admin_date; //등록날짜 -> JPA로 레코드 저장시 실행되어 등록시점 날짜값 기록
	
}
