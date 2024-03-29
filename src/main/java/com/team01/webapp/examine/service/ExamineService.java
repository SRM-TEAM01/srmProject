package com.team01.webapp.examine.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team01.webapp.examine.dao.IExamineRepository;
import com.team01.webapp.model.Examine;
import com.team01.webapp.model.ExamineFilter;
import com.team01.webapp.model.ExamineList;
import com.team01.webapp.model.SRStts;
import com.team01.webapp.model.SrFile;
import com.team01.webapp.model.System;
import com.team01.webapp.model.Users;
import com.team01.webapp.util.Pager;

@Service
public class ExamineService implements IExamineService {
	
	@Autowired
	IExamineRepository examineRepository;
	
	@Override
	public int getTotalRows() {
		int rows = examineRepository.selectExamineListAllCount();
		return 0;
	}


	/**
	 * SR 검토 리스트 가져오기
	 * @author : 황건희
	 * @param examinelist	SR 검토 리스트 가져오기
	 * @param pager			페이징 처리
	 * @return
	 */
	@Override
	public List<Examine> getExamineList(Pager pager,ExamineList examinelist) {
		
		examinelist.setStartRowNo(pager.getStartRowNo());
		examinelist.setEndRowNo(pager.getEndRowNo());
		List<Examine> examineList = examineRepository.selectExaminelist(examinelist);
		
		return examineList;
	}

	/**
	 * SR 검토 검색 조건에 대한 리스트 
	 * @author : 황건희
	 * @param examineFilter	SR 검토 검색 조건에 대해 해당 리스트 가져오기
	 * @return
	 */
	@Override
	public ExamineFilter filterList(ExamineFilter examineFilter) {
		
		List<SRStts> srSttsList = new ArrayList<>();
		List<System> sysNmList = new ArrayList<>();
		List<Users> userOgdpList = new ArrayList<>();
		List<Users> userDpList = new ArrayList<>();
		String userOgdp = "전체";
		//요청 진행상태
		srSttsList = examineRepository.selectSrSttsList();
		examineFilter.setSrSttsList(srSttsList);
		
		//관련 시스템
		sysNmList = examineRepository.selectSysNmList();
		examineFilter.setSysNmList(sysNmList);
		
		//등록자 소속 
		userOgdpList = examineRepository.selectUserOgdpList();
		examineFilter.setUserOgdpList(userOgdpList);	
		
		//등록자 소속 부서
		userDpList = examineRepository.selectUserDpList(userOgdp);
		examineFilter.setUserDpList(userDpList);
		
		return examineFilter;
	}
	
	/**
	 * SR 검토 목록 페이지 처리
	 * @author : 황건희
	 * @param examinelist	SR 검토 목록 페이지에 따른 리스트 가져오기
	 * @param pageNo		SR 검토 목록 페이지 위치
	 * @param pager			페이징 처리
	 * @return
	 */
	@Override
	public Pager returnPage(int pageNo, Pager pager, ExamineList examinelist) {
		int totalListNum = (int) examineRepository.selectTotalExamineCount(examinelist);
		pager = new Pager(10,5,totalListNum,pageNo);
		
		return pager;
	}


	@Override
	public Examine getExamine(String srNo) {
		Examine examine = examineRepository.selectExamine(srNo);
		
		return examine;
	}
	
	@Override
	public List<MultipartFile> selectExamineFileList(String srNo){
		
		List<MultipartFile> examineFileList = examineRepository.selectExamineFileList(srNo);
		
		return examineFileList;
	}
	
	@Override
	public SrFile selectFileDownload(int srFileNo) {
		
		SrFile srFile = examineRepository.selectExamineFileDownload(srFileNo);
		
		return srFile;
	}
	

	@Override
	public void updateExamine(Examine examine) {
		examineRepository.updateExamine(examine);
		
	}


	@Override
	public void updateExamineProcessing(ExamineList examinelist) {
		if(examinelist.getSrNo() != null) {			
			examineRepository.updateExamineProcessing(examinelist);
		}
		
	}
	
	//로그인 유저 정보 가져오기
	@Override
	public Users selectLoginUser(int userNo) {
		
		Users loginUser = examineRepository.selectLoginUser(userNo);
		
		return loginUser;
	}
	
	//엑셀 다운로드
	@Override
	public List<Examine> getExamineExcelList(List<String> examineArr){
		Examine examine = null;
		ArrayList<Examine> examineList = new ArrayList<>();
		for(int i=0; i<examineArr.size(); i++) {
			examine = examineRepository.selectExamine(examineArr.get(i));
			examineList.add(examine);
		}
		return examineList;
		
	}
	
	@Override
	public ExamineFilter selectUserDpList(String userOgdp){
		List<SRStts> srSttsList = new ArrayList<>();
		List<System> sysNmList = new ArrayList<>();
		List<Users> userOgdpList = new ArrayList<>();
		List<Users> userDpList = new ArrayList<>();
		
		ExamineFilter examineFilter = new ExamineFilter();
		//요청 진행상태
		srSttsList = examineRepository.selectSrSttsList();
		examineFilter.setSrSttsList(srSttsList);
		
		//관련 시스템
		sysNmList = examineRepository.selectSysNmList();
		examineFilter.setSysNmList(sysNmList);
		
		//등록자 소속 
		userOgdpList = examineRepository.selectUserOgdpList();
		examineFilter.setUserOgdpList(userOgdpList);	
		userDpList = examineRepository.selectUserDpList(userOgdp);
				
		examineFilter.setUserDpList(userDpList);
		return examineFilter;
	}

}
