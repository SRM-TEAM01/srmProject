package com.team01.webapp.qnaboard.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.team01.webapp.model.QSTN;
import com.team01.webapp.model.QSTNComment;
import com.team01.webapp.model.QSTNFile;
import com.team01.webapp.qnaboard.service.IQnaboardService;
import com.team01.webapp.util.AlarmInfo;
import com.team01.webapp.util.Pager;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/qna")
public class QnaController {
	
	@Autowired
	private IQnaboardService qnaboardService;
	
	@Autowired
	AlarmInfo alarmInfo;
	

	/**QnA목록 보기
	 * @author 			정홍주
	 * @param sysNo 	소속된 시스템 
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입	
	 * @return
	 */
	@GetMapping("/{sysNo}/list")
	public String getQnaList(@PathVariable String sysNo, HttpSession session, Model model){
		//알림 수 및 리스트
		alarmInfo.info(session, model);
		model.addAttribute("session",sysNo);
		return "qnaboard/qnalist";
	}
	
	
	/**메인화면에서 상세보기
	 * @author			정홍주
	 * @param sysNo		소속된 시스템
	 * @param qstnNo	조회할 문의글의 번호
	 * @param session	알림을 띄우기 위함
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@GetMapping("/{sysNo}/list/{qstnNo}")
	public String getQnaList(@PathVariable String sysNo, @PathVariable int qstnNo, HttpSession session, Model model){
		//알림 수 및 리스트
		alarmInfo.info(session, model);
		model.addAttribute("session",sysNo);
		model.addAttribute("qstnNo", qstnNo);
		model.addAttribute("command","detail");
		return "qnaboard/qnalist";
	}
	
	/**Qna조건 리스트
	 * @author 			정홍주
	 * @param pageNo	조회할 페이지 번호
	 * @param sysNo 	소속된 시스템
	 * @param qstn		조건을 넘길 객체
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @param pager		페이징 처리
	 * @return
	 */
	@PostMapping(value="/{sysNo}/filter/{pageNo}", produces="application/json; charset=UTF-8")
	public String getQnaList(@PathVariable int pageNo, @PathVariable String sysNo, @RequestBody QSTN qstn,  Model model, Pager pager) {
		pager = qnaboardService.returnPage(pageNo,pager,qstn);
		List<QSTN> qnalist = qnaboardService.getQstnList(pager,qstn);
		model.addAttribute("qnalist", qnalist); 
		model.addAttribute("session",sysNo);
		model.addAttribute("pager",pager);
		
		return "qnaboard/ajaxList";
	}
	
	
	/**	QNA상세보기
	 * @author			정홍주
	 * @param qstnNo	선택된 Q&A번호
	 * @param sysNo 	소속된 시스템
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return			qnaboard/qnadetail.jsp 리턴
	 */
	@GetMapping("/{sysNo}/view/{qstnNo}")
	public String getQnaDetail(@PathVariable int qstnNo, @PathVariable String sysNo, Model model) {
		QSTN qstn = qnaboardService.getDetail(qstnNo);
		
		List<MultipartFile> qstnFile = qnaboardService.getQstnFileDetail(qstnNo);
		//댓글 목록
		List<QSTNComment> qnaClist = qnaboardService.getCommentList(qstnNo);
		int countQstnComment = qnaboardService.countComment(qstnNo);
		model.addAttribute("qstn", qstn);
		model.addAttribute("qstnFile", qstnFile);
		model.addAttribute("qnaClist",qnaClist);
		model.addAttribute("countQstnComment", countQstnComment);
		model.addAttribute("session", sysNo);
		
		return "qnaboard/qnadetail";
	}
	
	/**파일 다운로드
	 * @author 			정홍주
	 * @param qstnNo	Q&A번호
	 * @param userAgent	브라우저 정보
	 * @param response	HttpServletResponse의 객체
	 * @throws Exception
	 */
	@GetMapping("/file")
	public void downloadQnafile(@RequestParam int qstnFileNo, @RequestHeader("User-Agent") String userAgent, HttpServletResponse response) throws Exception{
		
		QSTNFile qstnFile = qnaboardService.selectFiledownload(qstnFileNo);
		String originalName = qstnFile.getQstnFileActlNm();
		String savedName = qstnFile.getQstnFilePhysNm();
		String contentType = qstnFile.getQstnFileExtnNm();
		
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
		String filePath = "C:/OTI/uploadfiles/qstn/"+qstnFile.getQstnNo()+"/"+savedName;
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
	
	/**Q&A 폼 불러오기
	 * @author		정홍주
	 * @param sysNo	시스템 번호
	 * @param model	View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@GetMapping("{sysNo}/write")
	public String writeQna(@PathVariable String sysNo, Model model) {
		model.addAttribute("session",sysNo);
		model.addAttribute("command", "write");
		return "qnaboard/qnawrite";
	}

	/** Q&A 작성하기
	 * @author			정홍주
	 * @param qstn		Q&A객체
	 * @param qstnFile	Q&A파일 객체
	 * @return
	 */
	@PostMapping("{sysNo}/write")
	public String writeQna(QSTN qstn, QSTNFile qstnFile) {
		try {
			int qstnNo = qnaboardService.writeQSTN(qstn);
			//첨부파일 추가
			List<MultipartFile> mf = qstn.getQstnMFile();
			if(mf!=null &&!mf.isEmpty()) {
				for(int i=0; i<mf.size(); i++) {		
					qstnFile.getQstnNo();
					//파일 원래 이름 저장
					qstnFile.setQstnFileActlNm(mf.get(i).getOriginalFilename());
					//파일의 저장 이름 설정
					String qstnFilePhysNm = new Date().getTime()+"-"+mf.get(i).getOriginalFilename();
					qstnFile.setQstnFilePhysNm(qstnFilePhysNm);
					//파일 타입 설정
					String str = mf.get(i).getContentType();
					int beginIndex = str.indexOf("/");
					int endIndex = str.length();
					String type = str.substring(beginIndex,endIndex);
					qstnFile.setQstnFileExtnNm(type);
					qstnFile.setQstnNo(qstn.getQstnNo());		
					//서버 파일 시스템에 파일로 저장
					String filePath = "C:/OTI/uploadfiles/qstn/"+qstnFile.getQstnNo()+"/"+qstnFilePhysNm;
					File file = new File(filePath);
					// 폴더가 없다면 생성
					if(!file.exists()) {
						try {
							Files.createDirectories(Paths.get(filePath));
							mf.get(i).transferTo(file);
						} catch (Exception e) {
							log.info("생성 실패 : " + filePath);
						}
					} else {
						mf.get(i).transferTo(file);
					}
					qnaboardService.qstnFileUpload(qstnFile);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/qna/"+qstn.getSysNo()+"/view/"+qstn.getQstnNo();
	}
	
	/** Qna 수정 폼 띄우기
	 * @author			정홍주
	 * @param qstnNo	수정할 Q&A번호
	 * @param sysNo		접속한 시스템 번호
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@GetMapping("/{sysNo}/update")
	public String updateQna(@RequestParam int qstnNo, @PathVariable String sysNo, Model model) {
		QSTN qstn = qnaboardService.getDetail(qstnNo);
		List<MultipartFile> qstnFile = qnaboardService.getQstnFileDetail(qstnNo);
		model.addAttribute("qstn", qstn);
		model.addAttribute("qstnFile",qstnFile);
		model.addAttribute("command", "update");
		model.addAttribute("session",sysNo);
		return "qnaboard/qnawrite";
	}
	
	/**qna수정하기
	 * @author 			정홍주
	 * @param qstnNo	수정할 Q&A번호
	 * @param qstn		수정한 정보가 들어간 Q&A객체
	 * @param qstnFile	수정한 정보가 들어간 Q&A 파일 객체
	 * @return
	 */
	@PostMapping("/{sysNo}/update")
	public String updateQna(@RequestParam int qstnNo, QSTN qstn, QSTNFile qstnFile) {
		try {
			//기존 파일을 삭제했다면 삭제처리
			List<String> df = qstn.getDeleteFile();
			if(df!=null && !df.isEmpty()) {
				for(int j=0; j<df.size(); j++) {
					String filePath = "C:/OTI/uploadfiles/qstn/" + qstn.getQstnNo() + "/" + df.get(j);
					File file = new File(filePath);
					if(file.exists()) {
						if(file.delete()) {
							qnaboardService.EraseExistingFile(df.get(j));
						} else {
							log.info("파일 삭제 실패");
						}
					}
				}
			}
		
			//첨부 파일 유무 조사
			List<MultipartFile> mf = qstn.getQstnMFile();
			if(mf!=null &&!mf.isEmpty()) {
				for(int i=0; i<mf.size(); i++) {		
					//파일 원래 이름 저장
					qstnFile.setQstnFileActlNm(mf.get(i).getOriginalFilename());
					//파일의 저장 이름 설정
					String qstnFilePhysNm = new Date().getTime()+"-"+mf.get(i).getOriginalFilename();
					qstnFile.setQstnFilePhysNm(qstnFilePhysNm);
					//파일 타입 설정
					String str = mf.get(i).getContentType();
					int beginIndex = str.indexOf("/");
					int endIndex = str.length();
					String type = str.substring(beginIndex,endIndex);
					qstnFile.setQstnFileExtnNm(type);

					//서버 파일 시스템에 파일로 저장
					String filePath = "C:/OTI/uploadfiles/qstn/"+qstnFile.getQstnNo()+"/"+qstnFilePhysNm;
					File file = new File(filePath);
					// 폴더가 없다면 생성한다
					if(!file.exists()) {
						try {
							Files.createDirectories(Paths.get(filePath));
							mf.get(i).transferTo(file);
						} catch (Exception e) {
						log.info("생성 실패 : " + filePath);
						}
					} else {
					mf.get(i).transferTo(file);
					}
					int row = qnaboardService.changeQstnFile(qstnFile);
					if(row == 1) {
						
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		qnaboardService.changeQstn(qstn);
		return "redirect:/qna/"+qstn.getSysNo()+"/view/"+qstn.getQstnNo();
	}
	
	/**Qna삭제
	 * @author			정홍주
	 * @param qstnNo 	삭제할 Q&A번호
	 * @param sysNo		접속한 시스템 번호
	 * @return
	 */
	@PostMapping("/{sysNo}/delete/{qstnNo}")
	public String deleteQstn(@PathVariable int qstnNo, @PathVariable String sysNo) {
		int result = qnaboardService.eraseQstn(qstnNo);
		return  "redirect:/qna/"+sysNo+"/list";
	}
	
	/**Qna댓글읽기
	 * @author			정홍주
	 * @param qstnNo	댓글 달 Q&A번호
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@GetMapping(value="/{sysNo}/read/comment")
	public String readComment(@RequestParam int qstnNo, Model model) {
		List<QSTNComment> list = qnaboardService.getCommentList(qstnNo);
		model.addAttribute("list",list);
		return "qnaboard/qnaCmntList";
	}
	

	/**Qna댓글작성
	 * @author			정홍주
	 * @param qComment	작성한 댓글이 저장된 객체
	 * @return
	 */
	@PostMapping(value="/{sysNo}/write/comment", produces="application/json; charset=UTF-8")
	@ResponseBody
	public QSTNComment writeComment(@RequestBody QSTNComment qComment){
		//RequestBody안에는 json타입으로 있어야 함.
		qComment = qnaboardService.writeComment(qComment);
		return qComment;
	}
	
	/**Qna댓글 수정
	 * @author			정홍주
	 * @param qComment	작성한 댓글이 저장된 객체
	 * @return
	 */
	@PostMapping(value="/{sysNo}/update/comment", produces="application/json; charset=UTF-8")
	@ResponseBody
	public QSTNComment updateComment(@RequestBody QSTNComment qComment) {
		qnaboardService.updateComment(qComment);
		return qComment;
	}
	
	/**Qna댓글삭제
	 * @author				정홍주
	 * @param qstnCmntNo	삭제할 댓글 번호
	 * @return				
	 */
	@GetMapping(value="{sysNo}/delete/comment")
	@ResponseBody
	public int deleteComment(@RequestParam int qstnCmntNo) {
		qnaboardService.deleteComment(qstnCmntNo);
		return qstnCmntNo;
	}
	
}
