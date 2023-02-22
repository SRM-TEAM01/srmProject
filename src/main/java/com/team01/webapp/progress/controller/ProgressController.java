package com.team01.webapp.progress.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.team01.webapp.model.HR;
import com.team01.webapp.model.Progress;
import com.team01.webapp.model.ProgressDetail;
import com.team01.webapp.model.ProgressFile;
import com.team01.webapp.model.ProgressFilter;
import com.team01.webapp.model.ProgressType;
import com.team01.webapp.model.SrFile;
import com.team01.webapp.model.SrProgressAjax;
import com.team01.webapp.model.SrProgressList;
import com.team01.webapp.model.Task;
import com.team01.webapp.model.ThArr;
import com.team01.webapp.progress.service.IProgressService;
import com.team01.webapp.util.Pager;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class ProgressController {
	
	@Autowired
	private IProgressService progressService;
	
	/**
	 * 리스트 된 필터링 불러오기
	 * 
	 * @author					김태희
	 * @param pageNo			클라이언트가 보낸 페이지 번호 정보 저장
	 * @param progressfilter	클라이언트가 보낸 필터링 하기 위한 데이터 정보 저장
	 * @param model				View로 데이터 전달을 위한 Model 객체 주입
	 * @return					progress/list 로 return
	 */
	@RequestMapping(value="/progress/list/{pageNo}", method = RequestMethod.GET)
	public String progressList(@PathVariable int pageNo, ProgressFilter progressfilter, Model model) {
		
		progressfilter = progressService.filterList(progressfilter);
		
		model.addAttribute("progressFilter", progressfilter);
		
		return "progress/list";
	}
	
	/**
	 * 진척 관리 상세 정보 조회
	 * 
	 * @author				김태희
	 * @param srNo			클라이언트가 보낸 SR 번호 정보 저장
	 * @param model			View로 데이터 전달을 위한 Model 객체 주입
	 * @return				progress/detail 로 return
	 */
	@RequestMapping(value="/progress/detail/{srNo}", method = RequestMethod.GET)
	public String progressDetail(@PathVariable String srNo, Model model) {
		
		ProgressDetail progressdetail = progressService.selectDetail(srNo);
		
		model.addAttribute("progressDetail", progressdetail);
		
		return "progress/detail";
	}
	
	/**
	 * progress 리스트 불러오기
	 * 
	 * @author					김태희
	 * @param pageNo			클라이언트가 보낸 Page 번호 정보 저장
	 * @param srProgressAjax	클라이언트가 보낸 필터링 후 데이터 정보 저장
	 * @param model				View로 데이터 전달을 위한 Model 객체 주입
	 * @param pager				클라이언트가 보낸 pager 데이터 정보 저장
	 * @return					progress/progressListView 로 return
	 */
	@RequestMapping(value="progress/list/progressajax/{pageNo}", produces="application/json; charset=UTF-8")
	public String progressAjax(@PathVariable String pageNo, @RequestBody SrProgressAjax srProgressAjax, Model model, Pager pager) {
		
		log.info("pageNo " + pageNo);
		log.info("srProgressAjax " + srProgressAjax);
		
		pager = progressService.returnPage(pageNo, pager, srProgressAjax);
		
		log.info(pager);
		
		List<SrProgressList> list = progressService.ProgressList(pager, srProgressAjax);
		
		model.addAttribute("ProgressList", list);
		model.addAttribute("pager", pager);
		
		log.info(pager);
		
		return "progress/progressListView";
	}
	
	/**
	 * 상세 뷰에서 파일 다운로드
	 * 
	 * @author				김태희
	 * @param srFileNo		클라이언트가 보낸 srFile 번호 정보 저장
	 * @param userAgent		User-Agent header 정보 저장
	 * @param response		HttpServletResponse 객체 주입
	 * @throws Exception	예외 처리
	 */
	@RequestMapping(value="progress/detail/filedownload", method = RequestMethod.GET)
	public void filedownload(int srFileNo, @RequestHeader("User-Agent") String userAgent, HttpServletResponse response) throws Exception {
		SrFile srFile = progressService.getSrFile(srFileNo);
		
		String originalName = srFile.getSrFileActlNm();
		String savedName = srFile.getSrFilePhysNm();
		String contentType = srFile.getSrFileExtnNm();
		
		// originalName이 한글이 포함되어 있을 경우, 브라우저별로 한글을 인코딩
		if(userAgent.contains("Trident") || userAgent.contains("MSIE")) {
			originalName = URLEncoder.encode(originalName, "UTF-8");
		} else {
			originalName = new String(originalName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		// 응답 헤더 설정
		response.setHeader("Content-Disposition", "attachment; filename=\"" + originalName + "\"");
		response.setContentType(contentType);
		
		// 응답 바디에 파일 데이터 싣기
		String filePath = "C:/Temp/uploadfiles/" + savedName;
		
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
	
	/**
	 * SR인적자원관리 리스트 불러오기
	 * 
	 * @author			김태희
	 * @param hr		클라이언트가 보낸 hr 정보 저장
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return			progress/humanResourceList 로 return
	 */
	@RequestMapping(value="progress/detail/progressajax/1", produces="application/json; charset=UTF-8")
	public String humanResourceAjax(@RequestBody HR hr, Model model) {
		
		List<Task> taskList = progressService.taskList();
		model.addAttribute("taskList", taskList);
		
		String srNo = hr.getSrNo();
		model.addAttribute("srNo", srNo);
		
		List<HR> hrList = progressService.humanResourceList(srNo);
		model.addAttribute("hrList", hrList);
		
		List<HR> developerList = progressService.developerList(hrList.get(0).getUserDpNm(), srNo);
		model.addAttribute("developerList", developerList);
		
		return "progress/humanResourceList";
	}
	
	/**
	 * SR인적자원 추가
	 * 
	 * @author			김태희
	 * @param srNo		클라이언트가 보낸 sr 번호 정보 저장
	 * @param thArr		클라이언트가 보낸 thArr 정보 저장
	 * @return			progress/detail 로 리다이렉트
	 */
	@RequestMapping(value="progress/detail/developerinsert/{srNo}", produces="application/json; charset=UTF-8")
	public String developerinsert(@PathVariable String srNo, @RequestBody ThArr thArr) {
		
		log.info(thArr.getThArr().size());
		
		progressService.developerInsert(thArr);
		
		return "redirect:/progress/detail/" + srNo;
	}
	
	/**
	 * SR인적자원 수정폼 불러오기
	 * 
	 * @author			김태희
	 * @param hr		클라이언트가 보낸 hr 정보 저장
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return			progress/humanResourceUpdateView 로 return
	 */
	@RequestMapping(value="progress/detail/developerUpdateView", produces="application/json; charset=UTF-8")
	public String developerUpdateView(@RequestBody HR hr, Model model) {
		Date sysdate = new Date();
		
		HR developer = progressService.developer(hr.getSrNo(), hr.getUserNo());
		
		boolean startresult = developer.getHrStartDate().after(sysdate);
		boolean endresult = developer.getHrEndDate().after(sysdate);
		
		model.addAttribute("developer", developer);
		model.addAttribute("startresult", startresult);
		model.addAttribute("endresult", endresult);
		
		return "progress/humanResourceUpdateView";
	}
	
	/**
	 * SR인적자원 수정
	 * 
	 * @author		김태희
	 * @param hr	클라이언트가 보낸 hr 정보 저장
	 * @return		progress/detail 로 리다이렉트
	 */
	@RequestMapping(value="progress/detail/developerUpdate", produces="application/json; charset=UTF-8")
	public String developerUpdate(@RequestBody HR hr) {
		
		progressService.developerUpdate(hr);
		
		return "redirect:/progress/detail/" + hr.getSrNo();
	}
	
	/**
	 * SR인적자원 삭제
	 * 
	 * @author		김태희
	 * @param hr	클라이언트가 보낸 hr 정보 저장
	 * @return		progress/detail 로 리다이렉트
	 */
	@RequestMapping(value="progress/detail/developerDelete", produces="application/json; charset=UTF-8")
	public String developerDelete(@RequestBody HR hr) {
		
		log.info(hr.getSrNo());
		log.info(hr.getUserNo());

		progressService.developerDelete(hr.getSrNo(), hr.getUserNo());
		
		return "redirect:/progress/detail/" + hr.getSrNo();
	}
	
	/**
	 * SR 진척율 리스트 불러오기
	 * 
	 * @author			김태희
	 * @param hr		클라이언트가 보낸 hr 정보 저장
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return			progress/progressRateList 로 return
	 */
	@RequestMapping(value="progress/detail/progressajax/2", produces="application/json; charset=UTF-8")
	public String Progressrate(@RequestBody HR hr, Model model) {
		
		List<Progress> progressRateList = progressService.progressRateList(hr.getSrNo());
		
		model.addAttribute("progressRateList", progressRateList);
		
		log.info(progressRateList);
		
		return "progress/progressRateList";
	}
	
	@RequestMapping(value="progress/detail/progressRateAdd/{progNo}", method=RequestMethod.POST)
	public String ProgressRateAdd(@PathVariable int progNo, Model model) {
		
		Progress progress = progressService.progressRate(progNo);
		
		model.addAttribute("progress", progress);
		
		return "progress/progressRateAdd";
	}

	@RequestMapping(value="progress/detail/progressRate/update", method=RequestMethod.POST)
	public String ProgressRateUpdate(Progress progress) throws IOException {
		// 첨부 파일이 있는지 확인
		List<MultipartFile> mfList = progress.getProgressattach();
		
		log.info(mfList);
		if(mfList != null && !mfList.isEmpty()) {
			for(int i=0; i<mfList.size(); i++) {
				// 파일의 원래 이름
				progress.setProgFileActlNm(mfList.get(i).getOriginalFilename());
				
				// 파일의 저장 이름
				String progFilePhysNm = new Date().getTime() + "-" + mfList.get(i).getOriginalFilename();
				progress.setProgFilePhysNm(progFilePhysNm);
				
				// 파일의 타입 설정
				progress.setProgFileExtnNm(mfList.get(i).getContentType());
				
				// 서버 파일 시스템에 파일로 저장
				String filePath = "C:/OTI/uploadfiles/" + progress.getSrNo() + "/" + progFilePhysNm;
				File dir = new File(filePath);
				
				// 폴더가 없다면 생성한다
				if(!dir.exists()) {
					try {
						Files.createDirectories(Paths.get(filePath));
						log.info("폴더 생성 완료");
						mfList.get(i).transferTo(dir);
					} catch (Exception e) {
						log.info("생성 실패 : " + filePath);
					}
				} else {
					mfList.get(i).transferTo(dir);
				}
				
				progressService.writeProgressRateFile(progress);
			}
		}
		
		progressService.updateProgressRate(progress);
		
		return "redirect:/progress/detail/" + progress.getSrNo();
	}
	
	@RequestMapping(value="progress/detail/progressFiledownload/{srNo}", method = RequestMethod.GET)
	public void progressFiledownload(String progFileNo, @PathVariable String srNo, @RequestHeader("User-Agent") String userAgent, HttpServletResponse response) throws Exception {
		ProgressFile progressFile = progressService.getProgressFile(progFileNo);
		
		String originalName = progressFile.getProgFileActlNm();
		String savedName = progressFile.getProgFilePhysNm();
		String contentType = progressFile.getProgFileExtnNm();
		
		// originalName이 한글이 포함되어 있을 경우, 브라우저별로 한글을 인코딩
		if(userAgent.contains("Trident") || userAgent.contains("MSIE")) {
			originalName = URLEncoder.encode(originalName, "UTF-8");
		} else {
			originalName = new String(originalName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		// 응답 헤더 설정
		response.setHeader("Content-Disposition", "attachment; filename=\"" + originalName + "\"");
		response.setContentType(contentType);
		
		// 응답 바디에 파일 데이터 싣기
		String filePath = "C:/OTI/uploadfiles/" + srNo + "/" + savedName;
		
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
	
	
	@RequestMapping(value="progress/detail/progressajax/3", produces="application/json; charset=UTF-8")
	public String ProgresssFileList(@RequestBody HR hr, Model model) {
		
		List<ProgressFile> progressFileList = progressService.progressfileList(hr.getSrNo());
		
		model.addAttribute("progressFileList", progressFileList);
		model.addAttribute("srNo", hr.getSrNo());
		
		log.info(progressFileList);
		
		return "progress/progressFileList";
	}
	
	@RequestMapping(value="progress/detail/progressFileAdd/{srNo}", method=RequestMethod.GET)
	public String ProgressFileAdd(@PathVariable String srNo, Model model) {
		List<ProgressType> progressTypeList = progressService.getProgressTypeList();
		
		model.addAttribute("srNo", srNo);
		model.addAttribute("progressTypeList", progressTypeList);
		
		return "progress/progressFileAdd";
	}
	
	@RequestMapping(value="progress/detail/progressFile/add", method=RequestMethod.POST)
	public String ProgressFileAdd(Progress progress) throws IOException {
		// 첨부 파일이 있는지 확인
		List<MultipartFile> mfList = progress.getProgressattach();
		
		Progress progNo = progressService.getProgNo(progress.getProgTypeNo(), progress.getSrNo());
		
		progress.setProgNo(progNo.getProgNo());
		
		log.info(progress);
		
		if(mfList != null && !mfList.isEmpty()) {
			for(int i=0; i<mfList.size(); i++) {
				// 파일의 원래 이름
				progress.setProgFileActlNm(mfList.get(i).getOriginalFilename());
				
				// 파일의 저장 이름
				String progFilePhysNm = new Date().getTime() + "-" + mfList.get(i).getOriginalFilename();
				progress.setProgFilePhysNm(progFilePhysNm);
				
				// 파일의 타입 설정
				progress.setProgFileExtnNm(mfList.get(i).getContentType());
				
				// 서버 파일 시스템에 파일로 저장
				String filePath = "C:/OTI/uploadfiles/" + progress.getSrNo() + "/" + progFilePhysNm;
				File dir = new File(filePath);
				
				// 폴더가 없다면 생성한다
				if(!dir.exists()) {
					try {
						Files.createDirectories(Paths.get(filePath));
						log.info("폴더 생성 완료");
						mfList.get(i).transferTo(dir);
					} catch (Exception e) {
						log.info("생성 실패 : " + filePath);
					}
				} else {
					mfList.get(i).transferTo(dir);
				}
				
				progressService.writeProgressRateFile(progress);
			}
		}
		
		return "redirect:/progress/detail/" + progress.getSrNo();
	}
	
	@RequestMapping(value="progress/detail/progressFileRemove", produces="application/json; charset=UTF-8")
	public String ProgressFileRemove(@RequestBody Progress progress) {
		
		for(int i=0; i<progress.getProgressFile().size(); i++) {
			String filePath = "C:/OTI/uploadfiles/" + progress.getSrNo() + "/" + progress.getProgressFile().get(i).getProgFilePhysNm();
			
			File file = new File(filePath);
			
			if(file.exists()) {
				if(file.delete()) {
					log.info("파일 삭제 성공");
					progressService.removeProgressFiles(progress.getProgressFile().get(i).getProgFileNo());
				} else {
					log.info("파일 삭제 실패");
				}
			}
		}
		
		
		return "redirect:/progress/detail/" + progress.getSrNo();
	}
}
