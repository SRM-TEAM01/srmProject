<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<script>
  		$(document).ready(function(){
  			console.log("시작할때");
 
  			readComment();
  		});

  		let qCountCmnt = 0;
  		
  		function readComment(){
  			console.log("aa");
  			let qstnNo ='${qstn.qstnNo}';
  			console.log(qstnNo);
  			$.ajax({
		    	url:"read/comment",
		        type:"get",
		        data: 'qstnNo='+qstnNo,
		        success:function(data){
		        	console.log(data);
		        	$.each(data, function(index, item){
		        		let comment = '<hr/>';
		        		comment += '<div id="readCmnt">';
		        		comment += 	'<div class="d-flex px-2 flex-row align-items-center justify-content-between">';
		        		comment += 		'<div>';
						comment += 			'<h6 style="color: #406882"><b>'+item.userNm+'</b></h6>';
						comment += 			'<h6>'+item.qstnCmntDate+'</h6>';
						comment +=		'</div>';
						comment += 		'<div>';
						comment += 			'<input type="hidden" id="qstnCmntNo" value="'+item.qstnCmntNo+'"/>';
						comment += 			'<a id="updateToggle'+item.qstnCmntNo+'" onclick="updateCButton('+item.qstnCmntNo+')">수정</a> | <a onclick="deleteComment('+item.qstnCmntNo+')">삭제</a> |';
						comment += 		'</div>';
						comment += 	'</div>';
						comment += 	'<textarea id="commentContent'+item.qstnCmntNo+'" disabled="disabled" style="border: none; resize:none; width:90%">'+item.qstnCmntCn+'</textarea>';
						comment +='</div>'; 
		        		$('#qComment').append(comment); 
		        	});
		        	
		         }
		    })
		
  		};
  		
  		
		function writeComment(){
			console.log("댓글달기 실행");
			let content = $('#qnaCmntCn').val();
			let qcwriterNo = '${sessionScope.loginUser.userNo}';
			/* let qcwriter = '${sessionScope.loginUser.userNm}'; */
			let qstnNo = '${qstn.qstnNo}';
			
			let data = {userNo: qcwriterNo, qstnNo: qstnNo, qstnCmntCn: content};
			console.log(data);
			$.ajax({
				url: "write/comment",
				method: "post",
				data: JSON.stringify(data),
				contentType: "application/json; charset=UTF-8"
			}).done((item) => {
				console.log(item);
				
				let comment = '<hr/>';
        		comment += '<div class="d-flex px-2 flex-row align-items-center justify-content-between">';
        		comment += 	'<div>'
				comment += 		'<span style="color: #406882; margin-right:20px;"><b>'+item.userNm+'</b></span>';
				comment += 		'<span>'+item.qstnCmntDate+'</span>';
				comment +=	'</div>'
				comment += 	'<div>'
				comment += 		'<input type="hidden" id="qstnCmntNo" value="'+item.qstnCmntNo+'"/>'
				comment += 		'<a id="updateToggle'+item.qstnCmntNo+'" onclick="updateCButton('+item.qstnCmntNo+')">수정</a> | <a onclick="deleteComment('+item.qstnCmntNo+')">삭제</a> |';
				comment += 	'</div>';
				comment +='</div>';
				comment += '<textarea id="commentContent'+item.qstnCmntNo+'" disabled="disabled" style="border: none; resize:none; width:90%">'+item.qstnCmntCn+'</textarea>';
				
        		$('#qComment').append(comment); 
        		console.log(qCountCmnt);
        		$('#cmntCount').html('댓글('+(qCountCmnt+1)+')');
        		qCountCmnt += 1;
        		$('#qnaCmntCn').val('');
				
			});
		}
		
		function updateCButton(i){
			console.log(i);
			$('#commentContent'+i).removeAttr("disabled");
			$('#updateToggle'+i).html("변경");
			$('#updateToggle'+i).attr('onclick', 'udpateComplete('+i+')');
			
		};

		function udpateComplete(i){
			//댓글 수정
			console.log("댓글수정 ajax");
			console.log(i);
			let content = $('#commentContent'+i).val();
		 	let qstnCmntNo = i;
			let data = {qstnCmntNo: qstnCmntNo, qstnCmntCn: content};  
			$.ajax({
				url: "update/comment",
				method: "post",
				data: JSON.stringify(data),
				contentType: "application/json; charset=UTF-8"
			}).done((data) => {
				$('#commentContent'+i).attr('disabled','disabled');
				$('#updateToggle'+i).html("수정");
				$('#updateToggle'+i).attr('onclick', 'updateCButton('+i+')');
				console.log(data);
			}); 
		};
		
		function deleteComment(i){
			//댓글 삭제
			console.log("댓글삭제"+i);
			let qstnCmntNo = i;
			$.ajax({
				url: "delete/comment",
				method: "get",
				data: 'qstnCmntNo='+qstnCmntNo,
			}).done((data) => {
				console.log("성공");
				$('#cmntCount').html('댓글('+(qCountCmnt-1)+')');
        		qCountCmnt -= 1;
				$('#qComment').empty(); 
				readComment();
				
			});
		};
		
		function updateQstn(qstnNo){
			$("#mainQstnMenu").removeClass("d-sm-flex");
			$("#mainQstnMenu").hide();
			$("#qstnMenu").show();
			$("#mainQstn").attr("class","col-lg-7");
			$("#miniView").attr("class","col-lg-5");
			let sysNo = $('#sysNo').val();
			
			$.ajax({ 
				url: "${pageContext.request.contextPath}/qna/"+sysNo+"/update?qstnNo="+qstnNo,
				method: "GET",
				dataType : "html"
			}).done((data)=>{
				$('#miniView').html(data);
			});
		}
		
		function deleteQstn(qstnNo){
	    	$("#modalHeader").html("<b>Qna삭제</b>");
	    	$("#modalBody").html("<h4>삭제하시겠습니까?</h4>"); 
	    	$("#qstnModal").modal('show');
	    	let sysNo = $('#sysNo').val();
	    	$('#qstnSubmit').on("click", function(){
	    		$('#qstnForm').attr("action", "${pageContext.request.contextPath}/qna/"+sysNo+"/delete/"+qstnNo);
	    		$('#qstnForm').submit(); 
	    	});
		}
	</script>

    <!-- 메인 컨테이너 Container Fluid-->
 	<div class="d-sm-flex align-items-end justify-content-between">
		<div class="bg-primary px-3 py-2" style="border-top-left-radius:10px; border-top-right-radius:10px;">
			<h6 class="mb-0 text-white">상세보기</h6>
		</div>
      	<ol class="breadcrumb">
        	<li class="breadcrumb-item">Q&A</li>
           	<li class="breadcrumb-item active" aria-current="page">목록보기</li>
        </ol>
    </div>
    
    <!-- Row -->
    <div class="card mb-4 p-4 detail">
    	<div class="card-header">
       		<div class="d-flex flex-row align-items-center justify-content-between">
            	<div><h6 class="m-0 font-weight-bold text-primary mb-3">문의사항 No.${qstn.qstnNo}</h6></div>
                <div>조회수  ${qstn.qstnInqCnt}</div>
            </div>
            <div>
           		<h4 class="m-0 font-weight-bold text-primary textDetail">${qstn.qstnTtl}</h4>
           	</div>
           	<hr/>
        </div>   
        <div class="mx-3 p-3 d-flex flex-column">
       		<form id="qstnForm" method="post">
        		<div class="row">
         			<div class="form-group col-sm-2 ">
         				<label class="col-form-label">작성자</label>
         			</div>
	         		<div class="col-sm-10 mt-2">
	         			${qstn.userNm}
	         		</div>
        		</div>
           		<div class="row mb-2">
             		<div class="form-group col-sm-2  d-flex align-text-center">
             			<label class="col-form-label">작성일</label>
             		</div>
             		<div class="col-sm-10  mt-2">
             			${qstn.qstnWrtDate}
             		</div>
           		</div>
           		<div class="row mb-2">
             		<div class="form-group col-sm-2 ">
             			<label class="col-form-label">내용</label>
             		</div>
             		<div class="col-sm-10  mt-2 textDetail" id="qstnCn" style="border: 1px solid black; height: 150px; overflow-y:scroll;"  >
             			${qstn.qstnCn}
             		</div>
           		</div>
           		<div class="row mb-2">
             		<div class="form-group col-sm-2 ">
             			<label class="col-form-label">첨부파일</label>
             		</div>
            			<div class="col-sm-10">
            				<c:forEach var="qstnFile" items="${qstnFile}">
            					<div><a href="${pageContext.request.contextPath}/${sessionScope.loginUser.sysNo}/file?qstnFileNo=${qstnFile.qstnFileNo}">${qstnFile.qstnFileActlNm}</a></div>
            				</c:forEach>
            			</div>
           		</div>
           		<div class="text-right"> 
           			<input type="hidden" id="sysNo" name="sysNo" value="${qstn.sysNo}"/>
           			<input type="hidden" id="qstnNo" name="qstnNo" value="${qstn.qstnNo}"/>
           			<button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/qna/${qstn.sysNo}/list'">목록</button>
           			<c:if test="${qstn.userNo eq sessionScope.loginUser.userNo}">
           				<input type="button" class="btn btn-primary" value="수정" onclick="updateQstn(${qstn.qstnNo})"/>
           				<button type="button" class="btn btn-primary" onclick="deleteQstn(${qstn.qstnNo})">삭제</button>
           			</c:if>
           			
           		</div>
			</form>
		</div>
	</div>
                    			
	<!-- 댓글 -->
	<div class="card  p-4">
 		<div id="cmntCount" class="mx-3 mb-2">댓글(${qstn.countCmnt})</div>
 		<div class="mx-3 p-1  justify-content-between" >
	  		<div class="row">
	  			<div class="col-sm-2 form-group" id="qnaComentWriter">
	  				${sessionScope.loginUser.userNm}
	  			</div>
	  			<div class="col-sm-8  form-group">
	  				<textarea style="width: 100%" class="form-control" id="qnaCmntCn"></textarea>
	  			</div>
	  			<div class="col-sm-2 text-right">
	  				<button type="button" class="btn btn-primary btn-sm" onclick="writeComment();">등록하기</button>
	  			</div>
	    	</div>
		</div>
		<div class="px-4" id="qComment"></div>
	</div>  
</html>