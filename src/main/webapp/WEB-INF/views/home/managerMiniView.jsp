<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div>
        <div class="card shadow mb-4">
          <div class="card-header d-sm-flex justify-content-between align-items-center">
          	<h6 class="font-weight-bold text-primary"><img src="${pageContext.request.contextPath}/resources/images/gantt.png" style="width:30px; margin-right:10px;">SR 진행현황</h6>
          	<a class="btn btn-sm btn-primary">SR 진척관리 확인<i class="fa-solid fa-caret-right m-1"></i></a>
          </div>
          <div class="card-body pt-0">
            <div class="d-flex">
			   <div class="d-flex align-items-center">
				   <div class="devStage px-5 py-3">
				   
				   		<div class="d-sm-flex justify-content-between align-items-center">
	                      	<h6 class="text-center" style="border-bottom: 2px solid #263b65; padding:0 10px;">SR No.JHJ-SR-00001 <span class="badge badge-warning m-1" >개발(개선)</span> </h6>
	                      	<span><i class="fa-solid fa-user-tie m-1"></i> 관련 시스템: JHJ시스템</span>
	                  	</div>
	                  	
	                  	<div class="d-sm-flex justify-content-start align-items-center mb-2">
	                      	<p><i class="fa-solid fa-quote-left fa-2xs mr-1 ml-2"></i>시스템 점검 부탁드립니다.<i class="fa-solid fa-quote-right fa-2xs ml-1"></i></p>
	                  	</div>
	                  	
	                  	<div class="d-flex align-items-center mb-4">
		                  	<!-- S한 단계 -->
		                  	<div class="d-flex flex-column">
					   			<div class="circle plan"><i class="fa-sharp fa-regular fa-pen-to-square"></i><p>요청</p></div>
					   			<span class="badge badge-secondary dateBadge">요청일자 <br> 2023/02/12</span> 
		                  	</div>
					   		<i class="fa-solid fa-caret-right"></i>	<!-- 화살표 -->
							<!-- E한 단계 -->
							
							<div class="d-flex flex-column">
								<div class="circle execute current"><i class="fa-solid fa-magnifying-glass"></i><p>검토</p></div> 
								<span class="dateBadge"></span>
							</div>
							<!-- 실행 단계, 현재 실행 중인 업무 -->
							<i class="fa-solid fa-caret-right"></i>
							<div class="d-flex flex-column">
								<div class="circle evaluate"><i class="fa-solid fa-file-circle-check"></i></i><p>접수</p></div> 
								<span class="dateBadge"></span>
							</div>
							<i class="fa-solid fa-caret-right"></i>
							<div class="d-flex flex-column">
								<div class="circle evaluate"><i class="fa-solid fa-laptop-code"></i><p>개발중</p></div> 
								<span class="badge badge-secondary dateBadge">2023/03/01 ~<br>2023/03/26</span> 
							</div>
							<i class="fa-solid fa-caret-right"></i>
							<div class="d-flex flex-column">
								<div class="circle evaluate"><i class="fa-solid fa-magnifying-glass-arrow-right"></i><p>완료요청</p></div>
								<span class="dateBadge"></span><!-- 평가 단계 -->
							</div>
							<i class="fa-solid fa-caret-right"></i>
							<div class="d-flex flex-column">
								<div class="circle evaluate"><i class="fa-solid fa-square-check"></i><p>개발완료</p></div>
								<span class="badge badge-secondary dateBadge">완료예정일<br>2023/04/01</span> 
							</div>
						</div>
						 <!-- 특수한 경우 -->
						<!-- <i class="fa-solid fa-caret-right"></i>
						<div class="circle evaluate"><i class="fa-solid fa-file-circle-minus"></i><p>반려</p></div> 평가 단계
						<i class="fa-solid fa-caret-right"></i>
						<div class="circle evaluate"><i class="fa-solid fa-file-circle-exclamation"></i><p>재검토</p></div> 평가 단계 -->
						
					   <div class="d-flex justify-content-between p-3" style="background-color:#EAECF4; border-radius:5px;">
					  		<span><i class="fa-solid fa-user-tie m-1"></i> 관련 시스템: JHJ시스템</span>
					  		<span><i class="fa-solid fa-user-tie m-1"></i> 신청자: 김모씨</span>
					  		<span><i class="fa-solid fa-file-invoice-dollar m-1"></i> 예산: 3000만원</span>
					   </div>
					   
				   </div>
						
			   </div>
			   <div id="devInfo" class="px-3">
			   			
	   			<div class="devTable p-2">
                  	<div class="d-sm-flex justify-content-between align-items-start">
	                    <h6 class="card-title card-title-dash"> <i class="fa-solid fa-users-gear mx-1"></i>개발 1팀</h6>
	                  </div>
                  
                  <div class="table p-2" style="width: 500px;">
                    <table class="table select-table">
                      <thead>
                        <tr>
                          <th>리더여부</th>
                          <th>성명</th>
                          <th>직무</th>
                          <th>상태</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>
                            <span class="badge badge-warning" style="font-size:100%">Leader</span>
                          </td>
                          <td>
                            <h6>황건희</h6>
                            <small>2023.03.16~2023.03.26</small>
                          </td>
                          <td>
                            <div>
                               <p class="badge badge-success" style="font-size:100%;">설계</p>
                             </div>
                          </td>
                          <td><p class="badge" style="font-size:100%;">대기</p></td>
                        </tr>
                         <tr>
                          <td>
                            <span class="badge badge-secondary" style="font-size:100%">Dev</span>
                          </td>
                          <td>
                            <h6>김건지</h6>
                            <small>2023.03.16~2023.03.26</small>
                          </td>
                          <td>
                            <div>
                               <p class="badge badge-success" style="font-size:100%;">설계</p>
                             </div>
                          </td>
                          <td><p class="badge" style="font-size:100%;">대기</p></td>
                        </tr>
                        <tr>
                          <td>
                            <span class="badge badge-secondary" style="font-size:100%">Dev</span>
                          </td>
                          <td>
                            <h6>정항준</h6>
                            <small>2023.03.16~2023.03.26</small>
                          </td>
                          <td>
                            <div>
                               <p class="badge badge-success" style="font-size:100%;">설계</p>
                             </div>
                          </td>
                          <td><p class="badge" style="font-size:100%;">대기</p></td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
			   </div>
			</div>
          </div>
        </div>
	</div>
  <script>
  </script>