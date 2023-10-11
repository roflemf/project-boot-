package net.daum.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.daum.pwdconv.PwdChange;
import net.daum.service.MemberService;
import net.daum.vo.MemberVO;
import net.daum.vo.ZipcodeVO;
import net.daum.vo.ZipcodeVO2;

@Controller
public class MemberController {//사용자 회원관리
	
	@Autowired
	private MemberService memberService;
	
	//로그인 폼
	@GetMapping("/member_login")
	public ModelAndView member_Login() {
		return new ModelAndView("member/member_Login"); 
		//생성자 인자값으로 뷰리졸브 경로를 설정 => 
		// 	/WEB-INF/views/member/member_Login.jsp
		
	}
	
	//회원가입 폼
	@RequestMapping("/member_join")
	public ModelAndView member_join() {
		String[] phone = {"010", "011", "019"};
		String[] email = {"gmail", "naver.com", "daum.net",
						"nate.com", "직접입력"};
		ModelAndView jm = new ModelAndView();
		jm.addObject("phone", phone);
		jm.addObject("email", email);
		jm.setViewName("member/member_Join");//뷰페이지 경로 설정
		return jm;
	}//member_join()
	
	@PostMapping("/member_idcheck")
	public ModelAndView member_idcheck(String id, HttpServletResponse response)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		//웹브라우저에 출력되는 문자와 태그, 언어코딩 타입 출력
		PrintWriter out = response.getWriter();
		
		MemberVO db_id =this.memberService.idCheck(id); 
		//오라클로부터 아이디 중복검색
		
		/* 문제) 
		 * member.xml 매퍼 mybatis xml파일을 만드시고 유일아이디명을 m_idcheck로 해서 아이디를 기준으로 member테이블로부터
		 * 모든 필드를 검색해서 아이디 중복검색되게 만들어 보자.
		 * 
		 */
		
		int re =-1;//중복아이디가 없는 경우 반환값
		if(db_id != null) {//중복 아이디가 있는 경우
			re=1;
			
		}
		out.println(re);//값 반환 가능
		
		
		return null;
	}//member_idcheck
	

	
	//우편검색 공지창
	@GetMapping("/zip_find")
	public ModelAndView zip_find() {
		ModelAndView zm = new ModelAndView("member/zip_Find");
		return zm;
	}//zip_find()
	
	//우편주소 검색결과
	@PostMapping("/zip_find_ok")
	public ModelAndView zip_find_ok(String dong) {
		List<ZipcodeVO> zlist = this.memberService.zipFind("%"+dong+"%");
		
		List<ZipcodeVO2> zlist2 = new ArrayList<>();
		
		for(ZipcodeVO z:zlist) {
			ZipcodeVO2 z2 = new ZipcodeVO2();
			
			z2.setZipcode(z.getZipcode()); //우편번호 저장
			z2.setAddr(z.getSido()+" " + z.getGugun()+" " + z.getGil());
			//시도 구군 길(도로명주소)
			
			zlist2.add(z2); //컬렉션 추가
		}
		ModelAndView zm = new ModelAndView("member/zip_Find");
		zm.addObject("zipcodelist", zlist2); //zipcodelist키이름에 가공한 주소목록 저장
		zm.addObject("dong", dong);
		return zm;
		
	}//zip_find_ok()
	
	
	//회원저장
	@RequestMapping("/member_join_ok")
	public ModelAndView member_join_ok(MemberVO m) {
		/* MemberVO m이라고 하면 member_Join.jsp의 네임파라미터 이름과 빈클래스 변수명이 같으면
		 * m에 입력한 회원정보가 저장되어 있다.
 		 * 
		 */
		m.setMem_pwd(PwdChange.getPassWordToXEMD5String(m.getMem_pwd()));//비번 암호화
		this.memberService.insertMember(m); //회원저장
		/* 문제) 탈퇴 사유인 mem_delcont,탈퇴날짜인 mem_deldate만 빼고 나머지는 저장되게 만들어보자
		 * 가입회원의 경우는 mem_state=1 이다. 탈퇴회원은 2로한다. 
		 * mybatis 매퍼태그 유일 아이디명은 m_in으로 한다.
		 * 
		 */
		return new ModelAndView("redirect:/member_login");
		//생성자 인자값에 redirect:/가 들어가면 원하는 매핑주소로 이동시킴
	}///member_join_ok
	
	//비번찾기 공지창
	@RequestMapping("/pwd_find")
	public ModelAndView pwd_find() {
		return new ModelAndView("member/pwd_find"); 
		//생성자 인자값으로 뷰페이지 경로 설정
		// /WEB-INF/views/member/pwd_find.jsp
	}//pwd_find.jsp
	
	//비번찾기 결과
	@RequestMapping("pwd_find_ok")
	public ModelAndView pwd_find_ok(@RequestParam("pwd_id") String pwd_id,
				String pwd_name, HttpServletResponse response, MemberVO m)
						throws Exception{
		/* @RequestMapping("pwd_find_ok") = request.getParameter("pwd_id") 같은역할
		 */
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		m.setMem_id(pwd_id); m.setMem_name(pwd_name);
		MemberVO pm = this.memberService.pwdMember(m);
		/* 문제)
		 * 아이디와 회원이름 기준 회원정보를 오라클로부터 검색하는 pwdMember()메서드 작성
		 * member.xml에 설정하는 유일 아이디명; p_find
		 * 
		 */
		if(pm == null) {
			out.println("<script>");
			out.println("alert('회원으로 검색되지 않습니다!\\n 올바른 회원정보를 입력하세요!');");
			out.println("history.go(-1)");
			out.println("</script>");
		}else {
//			System.out.println("오라클로부터 검색된 회원 : " + pm.getMem_name());
			
			Random r = new Random();
			int pwd_random = r.nextInt(100000);//0이상 십만 미만 사이의 정수 숫자 난수 발생
			String ran_pwd = Integer.toString(pwd_random); //정수 숫자를 문자열로 변경
			m.setMem_pwd(PwdChange.getPassWordToXEMD5String(ran_pwd)); //임시비번 암호화
			
			this.memberService.updatePwd(m); //암호화 된 임시비번으로 수정
			
			ModelAndView fm = new ModelAndView("member/pwd_find_ok");
			fm.addObject("pwd_ran", ran_pwd);
			
			return fm;
		}
		return null;
	}//pwd_find_ok()
	
	
	//로그인 인증 처리
	@PostMapping("/member_login_ok")
	public ModelAndView member_login_ok(String login_id,String login_pwd, HttpServletResponse response,
			HttpSession session) throws Exception{
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		MemberVO m = this.memberService.loginCheck(login_id);
		/* 문제)
		 * 아이디와 mem_state=1 가입회원인 경우만 로그인 인증 처리를 한다.
		 * m_loginCheck; member.xml 에서 설정할 유일 아이디명은 
		 * 단위테스트인 개발자 테스트까지 마무리
		 * 
		 */
		
		if(m == null) {
			out.println("<script>");
	         out.println("alert('가입 안된 회원입니다!');");
	         out.println("history.back();");
	         out.println("</script>");
		}else {
			if(!m.getMem_pwd().equals(PwdChange.getPassWordToXEMD5String(login_pwd))) {
			out.println("<script>");
			out.println("alert('비번이 다릅니다!');");
			out.println("history.go(-1);");
			out.println("</script>");
			}else {
				session.setAttribute("id", login_id); //세션 id 키이름에 아이디 저장
				return new ModelAndView("redirect:/member_login");
			}
		}
		
		return null;
	}//member_login_ok();
	
	//로그아웃
	@PostMapping("/member_logout")
	public ModelAndView member_logout(HttpServletResponse response, 
			HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		session.invalidate();//세션 만료(로그아웃)
		
		out.println("<script>");
		out.println("alert('로그아웃 되었습니다!');");
		out.println("location='member_login';");
		out.println("</script>");
		
		return null;
				
	}//member_logout()
	
	//회원정보수정
	@GetMapping("/member_edit")
	public ModelAndView member_edit(HttpServletResponse response, HttpSession session)
	throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		String id = (String)session.getAttribute("id"); //세션 아이디를 구함
		
		if(isLogin(response,session)) {//==true 생략됌
			
			String[] phone = {"010", "011", "019"};
			String[] email = {"gmail", "naver.com", "daum.net",
							"nate.com", "직접입력"};
			
			MemberVO em = this.memberService.getMember(id); //아이디에 해당하는 회원정보 읽어옴
			/* 문제)
			 * 아이디를 기준으로 member 테이블로부터 회원정보 검색
			 * member_Info; member.xml에서 설정할 유일 아이디명
			 * 개발자 테스트 단위까지 하기.
			 * 
			 */
			
			ModelAndView m = new ModelAndView("member/member_Edit");
			m.addObject("em", em);
			m.addObject("phone", phone);
			m.addObject("email", email);
			
			return m;
		}
		return null;
	}//member_edit();
	
	
	//정보수정 완료
	@RequestMapping("/member_update_ok")
	public ModelAndView member_update_ok(MemberVO m, HttpServletResponse response,
			HttpSession session) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String id = (String)session.getAttribute("id");
		
		if(isLogin(response, session)) {
			m.setMem_id(id);
			m.setMem_pwd(PwdChange.getPassWordToXEMD5String(m.getMem_pwd()));//정식 비번
	         //암호화
			
			this.memberService.updateMember(m);// 회원정보 수정
			out.println("<script>");
			out.println("alert('정보 수정했습니다!');");
			out.println("location='member_edit';");
			out.println("</script>");
		}
		return null;
	}//member_update_ok()
	
	//반복적인 코드 줄이기
	public static boolean isLogin(HttpServletResponse response, HttpSession session)
	throws Exception{
		PrintWriter out = response.getWriter();
		String id = (String)session.getAttribute("id");
		
		if(id == null) {
			out.println("<script>");
			out.println("alert('다시 로그인하세요!');");
			out.println("location='member_login';");
			out.println("<script>");
			
			return false;
		}
		return true;
	}
}