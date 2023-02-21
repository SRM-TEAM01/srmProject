package com.team01.webapp.progress.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team01.webapp.model.DeveloperSR;
import com.team01.webapp.model.HR;
import com.team01.webapp.model.Progress;
import com.team01.webapp.model.ProgressDetail;
import com.team01.webapp.model.ProgressFile;
import com.team01.webapp.model.ProgressFilter;
import com.team01.webapp.model.SRStts;
import com.team01.webapp.model.SRType;
import com.team01.webapp.model.SrFile;
import com.team01.webapp.model.SrProgressAjax;
import com.team01.webapp.model.SrProgressList;
import com.team01.webapp.model.System;
import com.team01.webapp.model.Task;
import com.team01.webapp.model.ThArr;
import com.team01.webapp.progress.dao.IProgressRepository;
import com.team01.webapp.util.Pager;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProgressService implements IProgressService {
	
	@Autowired
	private IProgressRepository progressRepository;
	
	/**
	 * 필터링 하기 위한 데이터 저장
	 * 
	 * @author					김태희
	 * @param progressfilter 	클라이언트가 보낸 필터링 하기 위한 데이터 정보 저장
	 * @return					ProgressFilter 객체 return
	 */
	@Override
	public ProgressFilter filterList(ProgressFilter progressfilter) {
		// 사용할 리스트 선언
		List<System> systemList = new ArrayList<>();
		List<SRType> srTypeList = new ArrayList<>();
		List<SRStts> srSttsList = new ArrayList<>();
		
		// 시스템 구분 리스트
		systemList = progressRepository.selectSysNmList();
		progressfilter.setSysNmList(systemList);
		
		// 업무 구분 리스트
		srTypeList = progressRepository.selectSrType();
		progressfilter.setSrTypeList(srTypeList);
		
		// 진행 상태 리스트
		srSttsList = progressRepository.selectSttsNmList();
		progressfilter.setSrSttsList(srSttsList);
		
		return progressfilter;
	}
	
	/**
	 * 페이지 정보 저장
	 * 
	 * @author					김태희
	 * @param pageNo			클라이언트가 보낸 Page 번호 정보 저장
	 * @param pager				클라이언트가 보낸 pager 데이터 정보 저장
	 * @param srProgressAjax	클라이언트가 보낸 필터링 후 데이터 정보 저장
	 * @return					Pager 객체 return
	 */
	@Override
	public Pager returnPage(String pageNo, Pager pager, SrProgressAjax srProgressAjax) {
		// Pager
		int totalListNum = (int) progressRepository.selectTotalProgressCount(srProgressAjax);
		
		int pagerNo = Integer.parseInt(pageNo);
		pager = new Pager(10, 5, totalListNum, pagerNo);
		
		return pager;
	}
	
	/**
	 * 페이징 처리된 SrProgress 리스트 정보 저장
	 * 
	 * @author					김태희
	 * @param pager				클라이언트가 보낸 pager 객체 정보 저장
	 * @param srProgressAjax	클라이언트가 보낸 필터링 후 데이터 정보 저장
	 * @return					List<SrProgressList> 리스트 return
	 */
	@Override
	public List<SrProgressList> ProgressList(Pager pager, SrProgressAjax srProgressAjax) {
		int end = pager.getPageNo() * pager.getRowsPerPage();
		int start = (pager.getPageNo()-1)* pager.getRowsPerPage()+1;
		
		srProgressAjax.setEnd(end);
		srProgressAjax.setStart(start);
		
		return progressRepository.selectProgressList(srProgressAjax);
	}
	
	/**
	 * Progress 세부 정보 저장
	 * 
	 * @author			김태희
	 * @param srNo		클라이언트가 보낸 sr 번호 정보 저장
	 * @return			ProgressDetail 객체 return
	 */
	@Override
	public ProgressDetail selectDetail(String srNo) {
		
		ProgressDetail progressDetail = progressRepository.selectProgressRequester(srNo);
		
		// 개발자 정보 담아주기
		ProgressDetail progressDetailDeveloper = progressRepository.selectProgessdeveloper(srNo);
		progressDetail.setDpNm(progressDetailDeveloper.getDpNm());
		progressDetail.setDeveloperNm(progressDetailDeveloper.getDeveloperNm());
		progressDetail.setSrStartDate(progressDetailDeveloper.getSrStartDate());
		progressDetail.setSrEndDate(progressDetailDeveloper.getSrEndDate());
		
		// 파일 정보 담아주기
		List<SrFile> srFile = progressRepository.selectSrFileList(srNo);
		progressDetail.setSrFile(srFile);
		
		return progressDetail;
	}
	
	/**
	 * 파일 데이터 정보 저장
	 * 
	 * @author				김태희
	 * @param srFileNo		클라이언트가 보낸 srFile 번호 정보 저장
	 * @return				SRFile 객체 return
	 */
	@Override
	public SrFile getSrFile(int srFileNo) {
		return progressRepository.selectSrFile(srFileNo);
	}
	
	/**
	 * SR 인적 자원 정보 리스트 저장
	 * 
	 * @author			김태희
	 * @param srNo		클라이언트가 보낸 sr 번호 정보 저장
	 * @return			List<HR> 리스트 return
	 */
	@Override
	public List<HR> humanResourceList(String srNo) {
		return progressRepository.selectHumanResourceList(srNo);
	}

	/**
	 * SR 개발자 리스트 저장
	 * 
	 * @author				김태희
	 * @param userDpNm		클라이언트가 보낸 userDpNm 유저 부서 이름 정보 저장
	 * @param srNo			클라이언트가 보낸 sr 번호 정보 저장
	 * @return				List<HR> 리스트 return
	 */
	@Override
	public List<HR> developerList(String userDpNm, String srNo) {
		
		List<HR> developerList = progressRepository.selectDeveloperList(userDpNm, srNo);
		
		for(int i=0; i<developerList.size(); i++) {
			List<DeveloperSR> srList = progressRepository.selectDeveloperSR(developerList.get(i).getUserNo());
			log.info(srList);
			
			developerList.get(i).setSrList(srList);
		}
		
		return developerList;
	}
	
	/**
	 * 담당업무 리스트 저장
	 * 
	 * @author		김태희
	 * @return		List<Task> 리스트 return
	 */
	@Override
	public List<Task> taskList() {
		return progressRepository.selectTaskList();
	}
	
	/**
	 * SR인적 자원 추가
	 * 
	 * @author			김태희
	 * @param thArr		클라이언트가 보낸 ThArr 정보 저장
	 */
	@Override
	public void developerInsert(ThArr thArr) {
		progressRepository.insertDeveloper(thArr);
	}
	
	/**
	 * SR인적 자원 수정뷰 저장
	 * 
	 * @author			김태희
	 * @param srNo		클라이언트가 보낸 sr 번호 정보 저장
	 * @param userNo	클라이언트가 보낸 user 번호 정보 저장
	 * @return			
	 */
	@Override
	public HR developer(String srNo, int userNo) {
		return progressRepository.selectDeveloper(srNo, userNo);
	}
	
	/**
	 * SR인적 자원 수정
	 * 
	 * @author			김태희
	 * @param hr		클라이언트가 보낸 hr 정보 저장
	 */
	@Override
	public void developerUpdate(HR hr) {
		progressRepository.updateDeveloper(hr);
	}
	
	/**
	 * SR인적 자원 삭제
	 * 
	 * @author			김태희
	 * @param srNo		클라이언트가 보낸 sr 번호 정보 저장
	 * @param userNo	클라이언트가 보낸 user 번호 정보 저장
	 */
	@Override
	public void developerDelete(String srNo, int userNo) {
		progressRepository.deleteDeveloper(srNo, userNo);
	}

	/**
	 * SR진척율 리스트 저장
	 * 
	 * @author			김태희
	 * @param srNo		클라이언트가 보낸 sr 번호 정보 저장
	 * @return			List<Progress> 리스트 return
	 */
	@Override
	public List<Progress> progressRateList(String srNo) {
		List<Progress> progressList = progressRepository.selectProgressRateList(srNo);
		
		for(int i=0; i<progressList.size(); i++) {
			progressList.get(i).setProgressFile(progressRepository.selectProgressRateFile(progressList.get(i).getProgNo()));
		}
		
		return progressList;
	}

	@Override
	public Progress progressRate(int progNo) {
		return progressRepository.selectProgressRate(progNo);
	}

	@Override
	public void writeProgressRateFile(Progress progress) {
		progressRepository.insertProgressRateFile(progress);
	}

	@Override
	public void updateProgressRate(Progress progress) {
		progressRepository.updateProgressRate(progress);
	}

	@Override
	public ProgressFile getProgressFile(String progFileNo) {
		return progressRepository.selectProgressFile(progFileNo);
	}

}
