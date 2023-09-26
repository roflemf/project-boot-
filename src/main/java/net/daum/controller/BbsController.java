package net.daum.controller;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oreilly.servlet.MultipartRequest;

import net.daum.service.BbsService;
import net.daum.vo.BbsVO;
import net.daum.vo.PageVO;

@Controller //스프링MVC에서 컨트롤러로 인식함
public class BbsController {
	
	@Autowired
	private BbsService bbsService;
	
	//자료실 글쓰기 폼
	@GetMapping("/bbs_write") //bbs_write라는 매핑주소 등록
	public ModelAndView bbs_write(HttpServletRequest request) {
		//페이징에서 내가 본 페이지 번호로 바로 이동하는 책갈피기능
		int page=1;
		if(request.getParameter("page") !=null) {
			page=Integer.parseInt(request.getParameter("page"));
			//get으로 전달된 쪽번호가 있는 경우 숫자로 변경해 저장
		}
		
		ModelAndView wm = new ModelAndView();
		wm.addObject("page", page); //페이징 책갈피 기능때문에 page 키이름에 쪽번호 저장
		wm.setViewName("bbs/bbs_write");//뷰리졸브(뷰페이지)경로는 /WEB-INF/views/bbs/bbs/_write.jsp
		return wm;
	}//bbs_write()
	
	//자료실 저장
	@PostMapping("/bbs_write_ok")
	public String bbs_write_ok(BbsVO b, HttpServletRequest request) 
	throws Exception{
		String saveFolder = request.getRealPath("upload");//이진파일 업로드 서버 경로
		int fileSize = 5*1024*1024; //이진파일 업로드 최대크기
		MultipartRequest multi = null; //이진파일 업로드 참조변수
		
		multi = new MultipartRequest(request, saveFolder, fileSize, "UTF-8");
		
		String bbs_name = multi.getParameter("bbs_name");
		String bbs_title = multi.getParameter("bbs_title");
		String bbs_pwd = multi.getParameter("bbs_pwd");
		String bbs_cont = multi.getParameter("bbs_cont");
		
		File upFile = multi.getFile("bbs_file"); //첨부한 이진파일 가져온다
		
		if(upFile !=null) { //첨부한 파일 있는 경우 실행
			String fileName = upFile.getName();//첨부한 파일명 구함
			Calendar c= Calendar.getInstance(); 
			int year = c.get(Calendar.YEAR); //년도값
			int month = c.get(Calendar.MONTH)+1; //월값, +1한 이유; 1월이 0으로 반환되기 떄문
			int date = c.get(Calendar.DATE); //일값
			
			String homedir = saveFolder+"/" + year+"-"+month +"-"+date; //오늘날짜 폴더 경로 저장
			File path01 = new File(homedir);
			
			if(!(path01.exists())) {//오늘날짜 폴더 경로 없다면
				path01.mkdir();//오늘날짜 폴더 생성
			}
			Random r = new Random();//난수 발생 시키는 클래스
			int random = r.nextInt(100000000);//0이상 1억 미만 사이의 정수 숫자 난수 발생
			
			int index = fileName.lastIndexOf(".");
			//.위치번호를 맨오른쪽부터 찾아서 가장 먼저 나오는 . 위치번호를 맨왼쪽부터 카운터해서 구함
			//첫문자는 0부터 시작
			String fileExtendsion = fileName.substring(index+1); 
			//.이후부터 마지막 문자까지 구함
			//즉 첨부파일 확장자만 구함
			String refileName = "bbs" + year+month+date+random+"."+fileExtendsion;
			//새로운 첨부파일명 구함
			String fileDBName = "/"+year+"-"+month+"-"+date+"/"+refileName;
			//데이터베이스에 저장될 레코드 값
			upFile.renameTo(new File(homedir+"/"+refileName));
			//새롭게 생성된 폴더에 변경된 파일명으로 실제 업로드
			
			b.setBbs_file(fileDBName);//데이터 베이스에 저장될 레코드값
		}else {//첨부파일 없는 경우
			String fileDBName = "";
			b.setBbs_file(fileDBName);
		}//if else
		
		b.setBbs_name(bbs_name); b.setBbs_title(bbs_title);
		b.setBbs_pwd(bbs_pwd); b.setBbs_cont(bbs_cont);
		
		this.bbsService.insertBbs(b); //자료실 저장
			
		
		return "redirect:/bbs_list";//자료실 목록 보기 매핑주소로 이동
	}///bbs_write_ok()
	
	//페이징과 검색기능이 되는 자료실 목록
	@RequestMapping("/bbs_list")
	public ModelAndView bbs_list (HttpServletRequest request, PageVO p) {
		
		int page=1;
		int limit = 10; //한페이지에 보여지는 목록개수를 10개로함
		if(request.getParameter("page")!=null) {
			page=Integer.parseInt(request.getParameter("page"));
		}
		
		int totalCount = this.bbsService.getRowCount();//검색전 총 레코드 개수
		
		p.setStartrow((page-1)*10+1);//시작행 번호
		p.setEndrow(p.getStartrow()+limit-1);//끝행번호
		
		
		List<BbsVO> blist = this.bbsService.getBbsList(p); //검색전 목록
		
		//총 페이지수
	      int maxpage=(int)((double)totalCount/limit+0.95);
	      //시작페이지(1,11,21 ..)
	      int startpage=(((int)((double)page/10+0.9))-1)*10+1;
	      //현재 페이지에 보여질 마지막 페이지(10,20 ..)
	      int endpage=maxpage;
	      if(endpage>startpage+10-1) endpage=startpage+10-1;
	      
	    ModelAndView listM = new ModelAndView("bbs/bbs_list");
	    //생성자 인자값으로 뷰페이지 경로 설정
	    listM.addObject("blist", blist); //blist 키이름에 목록 저장
	    listM.addObject("page", page); 
	    listM.addObject("startpage", startpage); //시작페이지
	    listM.addObject("endpage", endpage); //마지막페이지
	    listM.addObject("maxpage", maxpage); //최대 페이지
	    listM.addObject("totalCount", totalCount); //검색전후 레코드 개수
	    
		return listM;
		
		
	}//bbs_list()
}
