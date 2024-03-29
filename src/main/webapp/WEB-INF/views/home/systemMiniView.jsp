<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div>
        <div class="card shadow mb-4">
          <div class="card-header py-3">
           	<div class="table-responsive">
           		<c:if test="${userType == '관리자' || userType == '고객사'}">
	          	 	<c:forEach var="list" items="${system}">
					    <table class="table" >
							<thead class="thead-light">
								  <tr>
								    <th>
								    	<i class="fa-solid fa-chart-pie"></i>
								    	${list.sysNm} 
								    </th>
								  </tr>
							</thead>
						    <tbody>
							      <tr>
							        <td><b>시스템 담당자 :  ${list.managerNm}</b></td>
							      </tr>
							      <tr>
							        <td style="border:0;"><b>시스템 업데이트일자 : ${list.sysUpdtDate}</b></td>
							      </tr>
						     </tbody>
					     </table>
				     </c:forEach>
			     </c:if>
			     <c:if test="${userType == '개발자'}">
			     	<table class="table" >
			     		<thead class="thead-light">
			     			<tr>
			     				<th>
			     					<i class="far fa-fw fa-window-maximize"></i>
			     					나의 업무 통계
			     				</th>
			     			</tr>
			     		</thead>
					    <tbody>
						      <tr>
						        <td><b>부서 : ${loginUser.userDpNm}</b></td>
						      </tr>
						      <tr>
						        <td style="border:0;"><b>직급 : ${loginUser.userJbps}</b></td>
						      </tr>
					     </tbody>
			     	</table>
			     </c:if>
	    	 </div>
          </div>
          <div class="card-body">
            <div class="chart-pie">
              <canvas id="myPieChart" style="height:100px; weight:100px"></canvas>
            </div>
          </div>
        </div>
	</div>

             <script>
             	labelse = [];
             	data = [];
             	sttsNoList = [];
             	ColorList = ['#6e707e', '#ef4f4f', '#a11811', '#406882', '#36b9cc', '#66bb6a', '#ffa426', '#a33bff', '#0a8091', '#78380c'];
             	backgroundColor = [];
             	
             	<c:forEach var="list" items="${donutList}">
             		labelse.push('${list.sttsNm}');
             		data.push(${list.count});
             		backgroundColor.push(ColorList[${list.sttsNo - 1}]);
             	</c:forEach>
             
				var ctx = document.getElementById("myPieChart");
				var myPieChart = new Chart(ctx, {
 				  plugins : [ChartDataLabels],
				  type: 'doughnut',
				  data: {
				    labels: labelse,
				    datasets: [{
				      data: data,
				      backgroundColor: backgroundColor,
				      hoverBorderColor: "rgba(234, 236, 244, 1)",
				    }],
				  },
				  options: {	
				    maintainAspectRatio: false,
				    cutoutPercentage: 60,
				    plugins : {
				    	tooltip: {
					      borderWidth: 1,
					      xPadding: 15,
					      yPadding: 15,
					      displayColors: false,
					      caretPadding: 10,
					      mode : 'point'
					    },
				    	legend: {
	 						position : 'bottom',
					    	labels : {
					    		boxWidth : 10
					    	}
				    	},
				    	datalabels : {
				            backgroundColor: null,
				            borderRadius: 5,
				    		formatter : function (value, context) {
				    			var idx = context.dataIndex;
				    			return context.chart.data.labels[idx] +"\n"+ value + "건";
				    		},
				    		display : 'auto',
				    		font : {
				    			size : '12',
				    		},
				    		color : '#fff',
				    	},
				    },
				  },
				});
				
				document.getElementById("myPieChart").onclick = function(evt) {
					const points = myPieChart.getElementsAtEventForMode(evt, 'index', { intersect: true }, true);
					var i = points[0].index;
					
					sttsNoList = [];
	             	<c:forEach var="list" items="${donutList}">
	             		sttsNoList.push('${list.sttsNo}');
	             	</c:forEach>
	             	
					var sttsNo = sttsNoList[i];
					var sysNo = '${system[0].sysNo}';
					
					var form = document.createElement('form');
					form.setAttribute('method','post');
					form.setAttribute('action', '${pageContext.request.contextPath}/list');
					document.charset = "utf-8";
					
					var hiddenField = document.createElement("input");
					hiddenField.setAttribute('type', 'hidden');
					hiddenField.setAttribute('name', 'sttsNo');
					hiddenField.setAttribute('value', sttsNo);
					form.appendChild(hiddenField);
					var hiddenField2 = document.createElement("input");
					hiddenField2.setAttribute('type', 'hidden');
					hiddenField2.setAttribute('name', 'sysNo');
					hiddenField2.setAttribute('value', sysNo);
					form.appendChild(hiddenField2);
					
					document.body.appendChild(form);
					form.submit();
					
				};
            </script>