<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자료실 목록</title>
<link rel= "stylesheet" type="text/css" href="./css/board.css" >
</head>
<body>

<form method="get" action="bbs_list">



<div id="bList_wrap">
      <h2 class="bList_title">사용자 자료실 목록</h2>
      <div class="bList_count">글개수: ${totalCount}</div>
      <table id="bList_t">
         <tr>
            <th width="6%" height="26">번호</th>
            <th width="50%">제목</th>
            <th width="14%">작성자</th>
            <th width="17%">작성일</th>
            <th width="14%">조회수</th>
         </tr>

         <c:if test="${!empty blist}">
            <c:forEach var="b" items="${blist}">
               <tr>
                  <td align="center">
                  <c:if test="${b.bbs_step==0}"> 
                  <%--원본글일때만 글그룹번호 출력 --%>
                  ${b.bbs_ref}
                  </c:if>
                  </td>
                  <td>
                  <c:if test="${b.bbs_step !=0}"> 
                  <%--답변글일때만 실행: 계단형 계층형 자료실 --%>
                   <c:forEach begin="1" end="${b.bbs_step}" step="1">
                   &nbsp;<%--한칸의 빈공백 처리 --%>
                   </c:forEach>
                   <img src="images/AnswerLine.gif" ><%--답변글 이미지 --%>
                   </c:if>
                  <a
                     href="bbs_cont?bbs_no=${b.bbs_no}&state=cont&page=${page}">
                        ${b.bbs_title}</a></td>
                  <td align="center">${b.bbs_name}</td>
                  <td align="center">${fn:substring(b.bbs_date,0,10)}</td>
                  <%-- 0이상 10미만 사이의 년월일만 반환 --%>
                  <td align="center">${b.bbs_hit}</td>
               </tr>
            </c:forEach>
         </c:if>

         <c:if test="${empty blist}">
            <tr>
               <th colspan="5">자료실 목록이 없습니다.</th>
            </tr>
         </c:if>
      </table>

      <%--페이징(쪽나누기)--%>
      <div id="bList_paging">
         <%--검색전 페이징 --%>
         <c:if test="${(empty find_field)&&(empty find_name)}">
            <c:if test="${page <=1}">
   [이전]&nbsp;
   </c:if>
            <c:if test="${page >1}">
               <a href="bbs_list?page=${page-1}">[이전]</a>&nbsp;
   </c:if>

            <%--쪽번호 출력부분 --%>
            <c:forEach var="a" begin="${startpage}" end="${endpage}" step="1">
               <c:if test="${a == page}"><${a}></c:if>

               <c:if test="${a != page}">
                  <a href="bbs_list?page=${a}">[${a}]</a>&nbsp;
    </c:if>
            </c:forEach>

            <c:if test="${page>=maxpage}">[다음]</c:if>
            <c:if test="${page<maxpage}">
               <a href="bbs_list?page=${page+1}">[다음]</a>
            </c:if>
         </c:if>

         <%-- 검색후 페이징(쪽나누기) --%>
         <c:if test="${(!empty find_field) || (!empty find_name)}">
            <c:if test="${page <=1}">
   [이전]&nbsp;
   </c:if>
            <c:if test="${page >1}">
               <a href="bbs_list?page=${page-1}&find_field=${find_field}&find_name=${find_name}">[이전]</a>&nbsp;
               <%--검색이후 페이징목록 유지 --%>
   </c:if>

            <%--쪽번호 출력부분 --%>
            <c:forEach var="a" begin="${startpage}" end="${endpage}" step="1">
               <c:if test="${a == page}"><${a}></c:if>

               <c:if test="${a != page}">
                  <a href="bbs_list?page=${a}&find_field=${find_field}&find_name=${find_name}">[${a}]</a>&nbsp;
    </c:if>
            </c:forEach>

            <c:if test="${page>=maxpage}">[다음]</c:if>
            <c:if test="${page<maxpage}">
               <a href="bbs_list?page=${page+1}&find_field=${find_field}&find_name=${find_name}">[다음]</a>
            </c:if>
         </c:if>
      </div>

      <div id="bList_menu">
         <input type="button" value="글쓰기"
            onclick="location='bbs_write?page=${page}';" />
         <c:if test="${(!empty find_field)&& (!empty find_name)}">
          <input type="button" value="전체목록"
          onclick="location='bbs_list?page=${page}';" >
          <%-- c:if; get으로 전달한 쪽번호만 전달하면 책갈피 기능 구현  --%>
         </c:if>
      </div>

	<%--검색 폼 추가 --%>
	<div id="bFind_wrap">
		<select name="find_field">
		 <option value="bbs_title"
		 	<c:if test="${find_field == 'bbs_title'}"> ${'selected'} </c:if>>
		 	<%--find_field가 bbs_cont와 같다면 해당목록을 선택되게 함 --%>
		 	글 제목</option>
		  <option value="bbs_cont"
		   <c:if test="${find_field =='bbs_cont'}">${'selected'}
		   </c:if>>글내용</option>
		  </select> <input type="search" name="find_name" id="find_name"
		  size="16" value="${find_name}" >
		  <input type="submit" value="검색">
	  </div>
   </div>
</form>
</body>
</html>