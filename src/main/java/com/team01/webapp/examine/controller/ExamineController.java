package com.team01.webapp.examine.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.team01.webapp.alarm.service.IAlarmService;
import com.team01.webapp.examine.service.IExamineService;
import com.team01.webapp.model.Examine;
import com.team01.webapp.model.ExamineFilter;
import com.team01.webapp.model.ExamineList;
import com.team01.webapp.model.SrFile;
import com.team01.webapp.model.Users;
import com.team01.webapp.util.AlarmInfo;
import com.team01.webapp.util.Pager;


@Controller
@RequestMapping("/examine")
public class ExamineController {

	@Autowired
	IExamineService examineService;
	
	@Autowired
	IAlarmService alarmService;
	
	@Autowired
	AlarmInfo alarmInfo;
	
	/**
	 * SR요청에 대한 필터링 
	 * @author : 황건희
	 * @param examineFilter SR요청에 대한 검색 조건
	 * @param model	View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@GetMapping(value="/list")
	public String getExamineList(ExamineFilter examineFilter , HttpSession session,Model model) {
		
		examineFilter = examineService.filterList(examineFilter);
		
		int userNo = (Integer) session.getAttribute("userNo");
		
		//로그인 유저 정보 가져오기
		Users loginUser = examineService.selectLoginUser(userNo);
		model.addAttribute("loginUser",loginUser);
		
		//알림 수 및 리스트
		alarmInfo.info(session, model); 
		model.addAttribute("examineFilter",examineFilter);
		
		return "examine/list";
	}
	
	/**
	 * @author : 황건희
	 * @param srNo				상세조회시 필요한 srNo
	 * @param examineFilter		검색 필터
	 * @param session			알림 데이터에 필요한 session
	 * @param model				View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@GetMapping(value="/list/{srNo}")
	public String getExamineDetail(@PathVariable String srNo, ExamineFilter examineFilter , HttpSession session,Model model) {
		
		examineFilter = examineService.filterList(examineFilter);
		
		int userNo = (Integer) session.getAttribute("userNo");
		
		//로그인 유저 정보 가져오기
		Users loginUser = examineService.selectLoginUser(userNo);
		model.addAttribute("loginUser",loginUser);
		
		//알림 수 및 리스트
		alarmInfo.info(session, model); 
		model.addAttribute("examineFilter",examineFilter);
		model.addAttribute("srNo",srNo);
		model.addAttribute("command","detail");
		return "examine/list";
	}
	
	
	/**
	 * SR 요청에 대한 필터링 후 리스트 가져오기
	 * @author : 황건희
	 * @param pageNo		SR검토 목록 페이지 위치
	 * @param examineList	SR검토 리스트
	 * @param pager			페이지 처리	
	 * @param model			View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@PostMapping(value="/filter/{pageNo}", produces="application/json; charset=UTF-8")
	public String getExamineFilter(@PathVariable int pageNo, @RequestBody ExamineList examineList, Model model, Pager pager) {
		pager = examineService.returnPage(pageNo,pager,examineList);
		
		List<Examine> list = examineService.getExamineList(pager, examineList);
		
		model.addAttribute("examine",list);
		model.addAttribute("pager",pager);

		return "examine/ajaxList";
	}
	
	/**
	 * SR 검토 상세 조회
	 * @author : 황건희
	 * @param examineFilter 필터링 후 SR 검토 리스트 가져오기
	 * @param model
	 * @return
	 */
	@GetMapping(value="/detail/{srNo}")
	public String getExamineDetail(@PathVariable String srNo, HttpSession session, Model model) {
		
		//로그인 유저 정보 가져오기
		int userNo = (Integer) session.getAttribute("userNo");
		Users loginUser = examineService.selectLoginUser(userNo);
		model.addAttribute("loginUser",loginUser);
		
		Examine examine = examineService.getExamine(srNo);
		List<MultipartFile> examineFileList = examineService.selectExamineFileList(srNo);
		model.addAttribute("examine",examine);
		model.addAttribute("examineFileList",examineFileList);
		
		
		return "examine/detailView";
	}
	
	/**
	 * SR 검토 상태 변경
	 * @author : 황건희
	 * @param examine	detailView.jsp에서 요청 검토 처리
	 * @return
	 */
	@PostMapping(value="/detail", produces="application/json; charset=UTF-8")
	public String updateExamine(@RequestBody Examine examine,HttpSession session,Model model)throws Exception {
		
		String srNo = examine.getSrNo();
		Examine selectExamine = examineService.getExamine(srNo);
		if(selectExamine.getSttsNm().equals(examine.getSttsNm())) {
			return "redirect:/examine/detail/"+srNo;
		}else {			
			examineService.updateExamine(examine);
		}
				
		alarmService.insertAlarm(srNo,session);
		
		
		return "redirect:/examine/detail/"+srNo;
	}
	
	/**
	 * @author : 황건희
	 * @param srFileNo		다운로드 받으려는 파일 No
	 * @param userAgent		현재 유저가 사용하고 있는 브라우저 정보
	 * @param response		
	 * @throws Exception
	 */
	@GetMapping("/fileDownload")
	public void download(int srFileNo,@RequestHeader("User-Agent") String userAgent, HttpServletResponse response) throws Exception{
		
		SrFile srFile = examineService.selectFileDownload(srFileNo);
		
		String originalName = srFile.getSrFileActlNm();
		String savedName = srFile.getSrFilePhysNm();
		String contentType = srFile.getSrFileExtnNm();
		
		//originalName이 한글이 포함되어 있을 경우, 브라우저별로 한글을 인코딩하는 방법
		if(userAgent.contains("Trident")|| userAgent.contains("MSIE")) {
			//Trident: IE 11
			//MSIE: IE 10 이하
			originalName = URLEncoder.encode(originalName,"UTF-8");
		}else {
			//Edge, Chrome, Safari
			originalName = new String(originalName.getBytes("UTF-8"),"ISO-8859-1");
		}
		
		//응답 헤더 설정
		response.setHeader("Content-Disposition", "attachmemnt; filename=\""+originalName+"\"");
		response.setContentType(contentType);
		
		//응답 바디에 파일 데이터 실기
		String filePath = "C:/OTI/uploadfiles/request/"+srFile.getSrNo()+"/"+savedName;
		File file = new File(filePath);
		
		if(file.exists()) {
			InputStream is = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			FileCopyUtils.copy(is, os);
			os.flush();
			os.close();
			is.close();
		}
	}
	
	//일괄 처리(검토중) or 일괄처리(접수)
	/**
	 * @author : 황건희
	 * @param examineList		일괄 처리하려는 SR 검토 목록
	 * @param model				View로 데이터 전달을 위한 Model 객체 주입
	 * @param pager				페이징 
	 * @return
	 */
	@PostMapping(value="/processing")
	public String updateExamineProcessing(@RequestBody ExamineList examineList,Model model, Pager pager) {

		examineService.updateExamineProcessing(examineList);
		
		pager = examineService.returnPage(1,pager,examineList);
		
		List<Examine> list = examineService.getExamineList(pager, examineList);
		
		model.addAttribute("examine",list);
		model.addAttribute("pager",pager);
		
		return "examine/ajaxList";
	}
	
	//엑셀 다운로드
	/**
	 * @author : 황건희
	 * @param examineArr		엑셀에 입력할 데이터 정보
	 * @param response			
	 * @throws IOException
	 */
	@PostMapping(value="/excelDownload")
	public void excelDownload(@RequestParam List<String> examineArr, HttpServletResponse response) throws IOException {
		XSSFWorkbook wb=null;
		Sheet sheet=null;
		Row row=null;
		Cell cell=null; 
		wb = new XSSFWorkbook();
		sheet = wb.createSheet("freeBoard");
        
        String[] HeaderList = {"SR 번호", "제목", "관련시스템", "등록자", "소속회사", "부서", "상태", "등록일","중요"};
        
        //첫행   열 이름 표기 
        int cellCount=0;
        row = sheet.createRow(0);
        for(int i=0; i<HeaderList.length; i++) {
    		cell=row.createCell(cellCount++);
    		cell.setCellValue(HeaderList[i]);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		List<Examine> list = examineService.getExamineExcelList(examineArr);
		
		for(int i=0; i<list.size(); i++) {
			row=sheet.createRow(i+1);
			cellCount = 0;
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getSrNo());
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getSrTtl());
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getSysNm());
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getUserNm());
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getUserOgdp());
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getUserDpNm());
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getSttsNm());
			cell=row.createCell(cellCount++);
			String srRegDate = simpleDateFormat.format(list.get(i).getSrRegDate());
			cell.setCellValue(srRegDate);
			cell=row.createCell(cellCount++);
			cell.setCellValue(list.get(i).getSrPry());
		}
		
		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition", "attachment;filename=testlist.xlsx");  //파일이름지정.
		//response OutputStream에 엑셀 작성
		wb.write(response.getOutputStream());
		wb.close();
	}
	
	@PostMapping(value="/selectUserDp")
	public String selectUserDp (@RequestBody Examine examine, Model model) {

		String userOgdp = (String)examine.getUserOgdp();
		ExamineFilter userDpList = examineService.selectUserDpList(userOgdp);
		model.addAttribute("examineFilter",userDpList);
		return "examine/examineUserDpNm";
	}
	
	
}
