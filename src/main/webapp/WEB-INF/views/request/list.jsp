<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  	<%@include file="/WEB-INF/views/common/head.jsp" %>
	<style>
	.modal{
		z-index:1050;
	}
	
	.col-lg-7 .requsetTtl{
		width: 90px;
		overflow: hidden;
		text-overflow: ellipsis;
		display:block;
	}	
    .col-lg-12 .requsetTtl{
    	width: 400px;
		overflow: hidden;
		text-overflow: ellipsis;
		display:block;
    }
    .srNo{
		width: 110px;
		overflow: hidden;
		text-overflow: ellipsis;
		display:block;
	}	
   	.sysNm{
   		width: 110px;
		overflow: hidden;
		text-overflow: ellipsis;
		display:block;
   	}
	.col-lg-7 .userOgdp{
		width: 70px;
		overflow: hidden;
		text-overflow: ellipsis;
		display:block;
	}
	.col-lg-7 .userOgdpColumn{
		display: none;
	}
	
    .col-lg-12 .userOgdp{
		width: 70px;
		display:block;
	}
	.srStts{
		width: 50px;
		display:block;
	}
	#requestList > .table th, 
	#requestList > .table td {
    	padding: 0.5rem;
    }
    
    .ajaxDetail span {
	  	white-space:normal !important;
	  	font-size:14px;
	 }
	 #colNo2 label,
	 #colNo2 input {
	  	white-space:normal !important;
	  	font-size:14px;
	 }
	  #colNo2 label{
	  	font-weight: 700 !important;
	  }
	  .on {
         background-color: rgba(0, 0, 0, 0.075);
      }
    
	</style>
  	
	<script>
	  	$(document).ready(function () {
		    $('#simple-date4 .input-daterange').datepicker({        
		        format: 'yyyy/mm/dd',        
		        autoclose: true,     
		        todayHighlight: true,   
		        todayBtn: 'linked',
		      });  
	  	});
	  	
  	</script>
  	
</head>

<body id="page-top">

<!-- 메시지 모달 -->
 <div class="modal" id="requestModal" tabindex="-1" role="dialog" aria-labelledby="requestModalLabel" aria-hidden="true">
   <div class="modal-dialog" role="document" style="max-width:450px;">
     <div class="modal-content">
       <div class="modal-header bg-primary">
         	<h5 class="modal-title" id="requestModalLabel"> 
	          	<img src="${pageContext.request.contextPath}/resources/images/logoOnly.png" style="width:20px;">
	        	<small class="text-white"><b id="modalHeadText">알림</b></small>
	        </h5>
         <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
           <span aria-hidden="true">&times;</span>
         </button>
       </div>
       <div class="modal-body justify-content-center text-center p-5">
	       <div class="d-flex align-items-center">
		       <div id="iconWrapper" class="mr-4">
			       	<i class="fas fa-exclamation-triangle" style="font-size:3rem; color:#FFA426;"></i>
		       </div>
		       <div id="dialogWrapper" class="text-left">
			     <h5 id="modalBodyText">요청이 처리되었습니다.</h5>
		       </div>
	       </div>
       </div>
       <div class="modal-footer justify-content-center text-center">
         <button type="button" id="modalcloseBtn" class="btn btn-outline-primary mx-2"  data-dismiss="modal" >확인</button>
       </div>
     </div>
   </div>
 </div>
<!-- 메시지 모달 -->

  <div id="wrapper">
    <!-- Sidebar -->
   	<%@include file="/WEB-INF/views/common/sidebar.jsp" %>

    
    <div id="content-wrapper" class="d-flex flex-column">
      <div id="content">
        
        <!-- TopBar -->
         <%@include file="/WEB-INF/views/common/topbar.jsp" %>


        <!-- 메인 컨테이너 Container Fluid-->
        <div class="container-fluid" id="container-wrapper">
        
          <div class="d-sm-flex align-items-end justify-content-between"  id="srMenu">
          	<div class="bg-primary px-3 py-2" style="border-top-left-radius:10px; border-top-right-radius:10px; width:121px;">
			 	<h6 class="mb-0 text-white">SR 요청관리</h6>
			</div>
            <ol class="breadcrumb">
              <li class="breadcrumb-item"><b>SR 관리</b></li>
              <li class="breadcrumb-item active" aria-current="page">SR 요청관리</li>
            </ol>
          </div>
		
          <!-- Row -->
        <div class="row">
     	<div class="col-lg-12" id="colNo1">
	     	<div class="bg-primary px-3 py-2" style="border-top-left-radius:10px; border-top-right-radius:10px; width:121px; display:none;" id="srMiniMenu">
			 	<h6 class="mb-0 text-white">SR 요청관리</h6>
			</div>
         	<div class="card mb-4 " style="min-height:740px;">
         		<!-- 검색 -->
                <div class="mb-1 mt-5 px-3">
                <form class="navbar-search">
                	<div class="row text-right">
                		<div class="col-4">
                			<div class="form-group row" id="simple-date4" >
	                			<div class="input-daterange input-group input-group-sm">
	                				<label for="start" class="col-sm-3 col-form-label-sm ">조회기간</label>
	                				<div class="col-sm-9 d-flex">
		                    			<input type="text" class="input-sm form-control form-control-sm" name="start" id="dateStart"/>
		                    			<div class="input-group-prepend">
		                    				<span class="input-group-text" style="height:31px; border-radius:0px;">~</span>
		                    			</div>
		                    			<input type="text" class="input-sm form-control form-control-sm" name="end" id="dateEnd"/>
	                				</div>
	                    		</div>
	            			</div>
                		</div>
                		
                		<div class="col-3">
                			<div class="form-group row">
                				<label for="sttsNo" class="col-sm-4 col-form-label-sm" >진행상태</label>
                				<select id="sttsNo" class="form-control form-control-sm col-sm-8" >
									<option selected>전체</option>
									<c:forEach var="item" items="${requestFilter.srSttsList}">
										<option value="${item.sttsNo}">${item.sttsNm}</option>
									</c:forEach>
		                    	</select>
		                	</div>
                		</div>
                		<div class="col-4">
                			<div class="form-group row">
                				<label for="sysNo" class="col-sm-4 col-form-label-sm" id="sysNoLabel">관련시스템</label>
                				<select  id="sysNo" class="form-control form-control-sm col-sm-8">
									<option selected>전체</option>
									<c:forEach var="item" items="${requestFilter.sysNmList}">
										<option value="${item.sysNo}">${item.sysNm}</option>
									</c:forEach>
		                    	</select>
		                	</div>
                		</div>
                		<div class="col-1 text-left px-1">
							<button class="btn btn-outline-warning btn-sm" type="button" onclick="requestFilterReset()" >초기화 </button>
                		</div>
                		
                	</div>
                	<div class="row text-right">
                		<div class="col-4">
                			<div class="form-group row">
                				<label for="userOgdp" class="col-sm-3 col-form-label-sm">등록자소속</label>
                				<div class="col-sm-9">
		                    	<select id="userOgdp" class="form-control form-control-sm">
		                        	<option selected>전체</option>
									<c:forEach var="item" items="${requestFilter.userOgdpList}">
										<option class="text-black" value="${item.userOgdp}">${item.userOgdp}</option>
									</c:forEach>
		                    	</select>
                				</div>
		                	</div>
                		</div>
                		<div class="col-3">
                			<div class="form-group row">
                			<label for="srDevDp" class="col-sm-4 col-form-label-sm">개발부서</label>
		                    	<select id="srDevDp" class="form-control form-control-sm col-sm-8" >
		                        	<option selected>전체</option>
									<c:forEach var="item" items="${requestFilter.srDevDpList}">
										<option value="${item.srDevDp}">${item.srDevDp}</option>
									</c:forEach>
		                    	</select>
		                	</div>
                		</div>
                		<div class="col-4">
		                	<div class="form-group row">
		                		<label for="srKeyWord" class="col-sm-4 col-form-label-sm">키워드</label>
		                    	<input type="text" id="keyword" class="form-control form-control-sm col-sm-8 bg-light" 
		                   		aria-label="Search" placeholder="검색어를 입력하세요" style="border-color: #3f51b5;">
		                  	</div>
                		</div>
                		<div class="col-1 text-left px-1">
							<button class="btn btn-primary btn-sm" type="button" onclick="requestList(1)" > 조회 <i class="fas fa-search fa-sm"></i></button>
                		</div>
                	</div>
                </form>
                </div>
                <hr/>
         		<!-- SR 요청 목록 -->
                <div class="p-3 d-flex flex-row align-items-center justify-content-between">
                   	<h5 class="m-0 font-weight-bold text-primary mb-1">SR 요청 목록</h5>
					<div class="d-sm-flex justify-content-end">
					<c:if test="${sessionScope.loginUser.userType eq '고객사'}">
                 		<a class="btn btn-sm btn-primary mr-1" onclick="getWriteForm()"> 요청등록 </a>
                 	</c:if>
						<button class="btn btn-sm btn-primary" onclick="excelDownload()">엑셀 다운로드</button>
					</div>
				</div>
				
				<!-- 나의 SR 조회 -->
				<input type="hidden" id="userNo" value="${sessionScope.loginUser.userNo}">
				<input type="hidden" id="userType" name="userType" value="${sessionScope.loginUser.userType}">
				<div class="custom-control custom-switch px-5 ml-2" style="width:200px; border-radius:3px; background-color:#eaecf4;">
					  <input type="checkbox" class="custom-control-input" id="searchMySR" onclick="requestList(1)"/>
					  
					  <label class="custom-control-label" for="searchMySR"><span class="text-primary">
					  <c:if test="${sessionScope.loginUser.userType eq '고객사'}">
					  	나의 SR 조회
					  </c:if>
					  <c:if test="${sessionScope.loginUser.userType eq '관리자' or sessionScope.loginUser.userType eq '개발자'}">
					  	담당 SR 조회
					  </c:if>
					  <i class="fas fa-search fa-sm mx-2"></i></span></label>
				</div>
				
				<!-- ajaxList 들어가는 곳 -->
                <div id="ajaxList">
                
                </div>
                
              </div>
          </div>
          
          <div id="colNo2">
          
          </div>
          <c:if test="${!empty command and command eq 'detail'}">
          	<input type="hidden" value="${srNo}" id="detailSrNo">
          </c:if>
          </div>
          <!-- Row -->
          <c:if test="${!empty command and command eq 'detail'}">
	          <script>
	          	$(document).ready(function(){
	          		var srNo = $("#detailSrNo").val();
		  			getSrDetail(srNo);
		          }
	          	);
	          </script>
          </c:if>
			<script>
				
				//오늘 날짜 디폴트로 입력
				$(document).ready(function(){
					getFilterDate();		
				}
				);
				
				function getFilterDate(){
					var todayResult = getTimeStamp();
					document.getElementById('dateEnd').value = todayResult;
					
					var dateStart =getLastYearTimeStamp();
					document.getElementById('dateStart').value = dateStart;
				}
				//오늘 날짜 양식
				function getTimeStamp() {
				  var d = new Date();
	
				  var s =
				    leadingZeros(d.getFullYear(), 4) + '/' +
				    leadingZeros(d.getMonth() + 1, 2) + '/' +
				    leadingZeros(d.getDate(), 2);
	
				  return s;
				}
				//작년 날짜 양식
				function getLastYearTimeStamp() {
					  var d = new Date();
	
					  var s =
					    leadingZeros(d.getFullYear(), 4)-1 + '/' +
					    leadingZeros(d.getMonth() + 1, 2) + '/' +
					    leadingZeros(d.getDate(), 2);
	
					  return s;
					}
	
	
	
				//오늘 날짜 양식 (+두자리)
				function leadingZeros(n, digits) {
				  var zero = '';
				  n = n.toString();
	
				  if (n.length < digits) {
				    for (i = 0; i < digits - n.length; i++)
				      zero += '0';
				  }
				  return zero + n;
				}
				
				$(document).ready(function () {
					var sysNo = 0;
					var sttsNo = 0;
					
					var homeSttsNo = '${homeSttsNo}';
					var homeSysNo = '${homeSysNo}';
					
					var choice = 0;
					var userType = $("#userType").val();
					
					if(homeSttsNo != '') {
						if(homeSysNo != '') {
							sysNo = '${homeSysNo}';
							$("#sysNo").val(sysNo).prop("selected", true);
						}
						sttsNo = homeSttsNo;
						$("#sttsNo").val(sttsNo).prop("selected", true);
						
						if(userType != '고객사') {
							$('#searchMySR').prop('checked', true);
						}
						var sysNoSelect = document.getElementById("sysNo");
						sysNo = sysNoSelect.options[document.getElementById("sysNo").selectedIndex].value;
						choice = 1;
						requestList(1);
						
					} else {
						var sysNoSelect = document.getElementById("sysNo");
						sysNo = sysNoSelect.options[document.getElementById("sysNo").selectedIndex].value;
						var sttsNoSelect = document.getElementById("sttsNo");
						sttsNo = sttsNoSelect.options[document.getElementById("sttsNo").selectedIndex].value;	
					}
					
					var userOgdpSelect = document.getElementById("userOgdp");
					var srDevDpSelect = document.getElementById("srDevDp");
					
					var userOgdp = userOgdpSelect.options[document.getElementById("userOgdp").selectedIndex].value;
			 		var srDevDp = srDevDpSelect.options[document.getElementById("srDevDp").selectedIndex].value;  
			 		
			 		var srRegStartDate = document.getElementById("dateStart").value;
					var srRegEndDate = document.getElementById("dateEnd").value;
					var srTtl = document.getElementById("keyword").value;
					if(srTtl !== "") {
						srTtl = "%" + srTtl + "%";
					}
			 		
					let data = {sysNo : sysNo, sttsNo : sttsNo, userOgdp : userOgdp, srDevDp : srDevDp, srRegStartDate : srRegStartDate, srRegEndDate : srRegEndDate, srTtl : srTtl};
					
					if(choice == 0) {
						$.ajax({
							url : "${pageContext.request.contextPath}/request/filter/1",
							method : "post",
							data : JSON.stringify(data),
							contentType: "application/json; charset=UTF-8"
						}).done((data) => {
							$("#ajaxList").html(data)
						});
					}
										
				});
				
				/* 나의 SR 체크여부 */
				var checkMySR = 0;
				 
				/* 필터링한 리스트 가져오기 */
				function requestList(pageNo) {
					var userNo = $("#userNo").val();
					var userType = $("#userType").val();
					
					if ( $('#searchMySR').prop('checked') ) {
						if(userType == '고객사'){
							checkMySR = '고객사';
							userNo = $("#userNo").val();
						} else if(userType == '관리자'){
							checkMySR = '관리자';
							userNo = $("#userNo").val();
						} else if (userType == '개발자'){
							checkMySR = '개발자';
							userNo = $("#userNo").val();
						}
						
						
					  } else {
						checkMySR = 0;
					  }
					var sysNoSelect = document.getElementById("sysNo");
					var sttsNoSelect = document.getElementById("sttsNo");
					var userOgdpSelect = document.getElementById("userOgdp");
					var srDevDpSelect = document.getElementById("srDevDp");
					
					var sysNo = sysNoSelect.options[document.getElementById("sysNo").selectedIndex].value;
					var sttsNo = sttsNoSelect.options[document.getElementById("sttsNo").selectedIndex].value;
					var userOgdp = userOgdpSelect.options[document.getElementById("userOgdp").selectedIndex].value;
			 		var srDevDp = srDevDpSelect.options[document.getElementById("srDevDp").selectedIndex].value;  
					
			 		var srRegStartDate = document.getElementById("dateStart").value;
					var srRegEndDate = document.getElementById("dateEnd").value;
					var srTtl = document.getElementById("keyword").value;
					
					if(srTtl !== "") {
						srTtl = "%" + srTtl + "%";
					}
					
					let data = {checkMySR: checkMySR, userNo: userNo, userType: userType, sysNo : sysNo, sttsNo : sttsNo, userOgdp : userOgdp, srDevDp : srDevDp, 
								srRegStartDate : srRegStartDate, srRegEndDate : srRegEndDate, srTtl : srTtl};
					
					$.ajax({
						url : "${pageContext.request.contextPath}/request/filter/"+pageNo,
						method : "post",
						data : JSON.stringify(data),
						contentType: "application/json; charset=UTF-8"
					}).done((data) => {
						$("#ajaxList").html(data);
						
					});
				}
				
				/* SR 상세정보 가져오기 */
				function getSrDetail(i){
					let srNo = i;
					$("#srMenu").removeClass("d-sm-flex");
					$("#srMenu").hide();
					$("#srMiniMenu").show();
					$("#colNo1").attr("class","col-lg-7");
					$("#colNo2").attr("class","col-lg-5");
					
					$(".srList").removeClass("on");
					
					$.ajax({
						url : "${pageContext.request.contextPath}/request/detail/"+srNo,
						type : "GET",
						dataType : "html",
						success : function(data) {
							$('#colNo2').html(data);
							var detailOn = "sr-"+i;
							$('#' + detailOn).addClass("on");
						}
					});
					
				}
				
				/* SR 입력폼 가져오기 */
				function getWriteForm(){
					$("#srMenu").removeClass("d-sm-flex");
					$("#srMenu").hide();
					$("#srMiniMenu").show();
					$("#colNo1").attr("class","col-lg-7");
					$("#colNo2").attr("class","col-lg-5");
					
					$.ajax({
						url :"${pageContext.request.contextPath}/request/write",
						type : "GET",
						dataType : "html",
						success : function(data) {
							$('#colNo2').html(data);
						}
					});
				}
				
				/* SR 수정폼 가져오기 */
				function getUpdateForm(srNo){
					$("#srMenu").removeClass("d-sm-flex");
					$("#srMenu").hide();
					$("#srMiniMenu").show();
					$("#colNo1").attr("class","col-lg-7");
					$("#colNo2").attr("class","col-lg-5");
					
					$.ajax({
						url :"${pageContext.request.contextPath}/request/update/"+srNo,
						type : "GET",
						dataType : "html",
						success : function(data) {
							$('#colNo2').html(data);
						}
					});
					
				}
				/* 체크박스 모두 체크하기 */
				function selectAll(selectAll) {
					const checkboxes = document.querySelectorAll('input[name="requestCheck"]');
				  
				  	checkboxes.forEach((checkbox) => {
				    	checkbox.checked = selectAll.checked
				  	})
					
				}
				
				/* 체크박스가 모두 체크되었는지 확인 */
				function checkSelectAll(checkbox)  {
				  const selectall 
				    = document.querySelector('input[name="requestCheck"]');
				  
				  if(checkbox.checked === false)  {
				    selectall.checked = false;
				  }
				}
				
				/* 검색조건 초기화하기 */
				function requestFilterReset(){
					getFilterDate();
					$("#sysNo").val("전체").prop("selected", true);
					$("#userOgdp").val("전체").prop("selected", true);
					$("#sttsNo").val("전체").prop("selected", true);
					$("#sttsNo").val("전체").prop("selected", true);
					$("#srDevDp").val("전체").prop("selected", true);
					$("#keyword").val("");
				}
				
				/* 엑셀 다운로드 */
				function excelDownload() {
					var requestArr = new Array();
					var checkbox = $("input[name=requestCheck]:checked");
					
					// 체크된 체크박스의 값을 가져옴
					checkbox.each(function(i) {
						var tr = checkbox.parent().parent().parent().eq(i);
						var td = tr.children();
						
						if(td.eq(1).text() != 'SR번호') {
							
							var srNo = td.eq(1).text();
							
							requestArr.push(srNo);
						}
					});
					
					var form = document.createElement('form');
					form.setAttribute('method','post');
					form.setAttribute('action', 'excelDownload');
					document.charset = "utf-8";
					
					var hiddenField = document.createElement("input");
					hiddenField.setAttribute('type', 'hidden');
					hiddenField.setAttribute('name', 'requestArr');
					hiddenField.setAttribute('value', requestArr);
					form.appendChild(hiddenField);
					
					document.body.appendChild(form);
					form.submit();
				}
			</script>
          

          <!-- 로그아웃 모달 -->
           <%@include file="/WEB-INF/views/common/logout.jsp" %>
           
      
			
        </div>
        <!---Container Fluid-->
      </div>
      
      <!-- Footer -->
     <%@include file="/WEB-INF/views/common/footer.jsp" %>

    </div>
  </div>
 <%@include file="/WEB-INF/views/common/bottom.jsp" %>
</body>

</html>
