package com.team01.webapp.request.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.team01.webapp.model.Request;
import com.team01.webapp.model.RequestAjax;
import com.team01.webapp.model.RequestFilter;
import com.team01.webapp.model.RequestList;
import com.team01.webapp.model.SR;
import com.team01.webapp.model.SRStts;
import com.team01.webapp.model.SRType;
import com.team01.webapp.model.SrFile;
import com.team01.webapp.model.System;
import com.team01.webapp.model.Users;
import com.team01.webapp.request.dao.IRequestRepository;
import com.team01.webapp.util.Pager;

@Service
public class RequestService implements IRequestService{
	
	@Autowired
	IRequestRepository requestRepository;
	
	/**
	 * system 넘버 가져오기
	 */
	@Override
	public String getSysNo(int userNo) {
		return requestRepository.selectSysNo(userNo);
	}
	
	@Override
	@Transactional
	public RequestFilter getFilterList(RequestFilter requestFilter) {
		// 사용할 리스트 선언
		List<System> systemList = new ArrayList<>();
		List<SRType> srTypeList = new ArrayList<>();
		List<SRStts> srSttsList = new ArrayList<>();
		List<Users> userOgdpList = new ArrayList<>();
		List<SR> srDevDpList = new ArrayList<>();
		
		// 시스템 구분 리스트
		systemList = requestRepository.selectSysNmList();
		requestFilter.setSysNmList(systemList);
		
		// 업무 구분 리스트
		srTypeList = requestRepository.selectSrType();
		requestFilter.setSrTypeList(srTypeList);
		
		// 진행 상태 리스트
		srSttsList = requestRepository.selectSttsNmList();
		requestFilter.setSrSttsList(srSttsList);
		
		//요청자 소속사 리스트
		userOgdpList = requestRepository.selectUserOgdpList();
		requestFilter.setUserOgdpList(userOgdpList);
		
		//개발 부서 리스트
		srDevDpList = requestRepository.selectSrDevDpList();
		requestFilter.setSrDevDpList(srDevDpList);
		
		return requestFilter;
	}
	
	
	@Override
	public Pager returnPage(String pageNo, Pager pager, RequestAjax requestAjax) {
		int totalListNum = requestRepository.selectTotalRequestCount(requestAjax);
		int pagerNo = Integer.parseInt(pageNo);
		pager = new Pager(7, 5, totalListNum, pagerNo);
		return pager;
	}
	
	
	@Override
	@Transactional
	public List<RequestList> getRequestList(Pager pager, RequestAjax requestAjax) {
		int start = (pager.getPageNo()-1)* pager.getRowsPerPage()+1;
		int end = pager.getPageNo() * pager.getRowsPerPage();
		
		requestAjax.setStart(start);
		requestAjax.setEnd(end);
		List<RequestList> requestLists = requestRepository.selectRequestList(requestAjax);
		return requestLists;
	}
	
	/**
	 * SR 작성
	 */
	@Override
	@Transactional
	public String writeRequest(SR sr) {
		String srSysNo = sr.getSysNo(); 
		String sysNo = "%"+srSysNo+"%";
		String maxSrNo = requestRepository.selectMaxSrNo(sysNo);
		String srNo = "";
		String number = "";
		int srSeq = 0;
		if(maxSrNo == null) {
			srNo = srSysNo+"-SR-00001";
		}else {
			srSeq = Integer.parseInt(requestRepository.selectMaxSrNo(sysNo))+1;
			number = String.format("%05d", srSeq);
			srNo = srSysNo+"-SR-"+number;
		}
		sr.setSrNo(srNo);
		
		int rows = requestRepository.insertRequest(sr);
		return srNo;
		
	}
	
	@Override
	public int requestFileUpload(SrFile srFile) {
		return requestRepository.insertRequestFileUpload(srFile);
	}
	
	/**
	 * SR 상세조회 
	 */
	@Override
	public Request getRequestDetail(String srNo) {
		return requestRepository.selectRequestDetail(srNo);
	}
	@Override
	public List<MultipartFile> selectRequestFileDetail(String srNo) {
		List<MultipartFile> requestFile = requestRepository.selectRequestFileDetail(srNo);
		return requestFile;
	}

	@Override
	public SrFile selectFileDownload(String srFileNo) {
		SrFile srFile = requestRepository.selectFileDownload(srFileNo);
		return srFile;
	}

	@Override
	@Transactional
	public int updateRequest(SR sr) {
		int rows = requestRepository.updateRequest(sr);
		return rows;
	}

	//업데이트시 파일 삭제
	@Override
	public int deleteExistingFile(String srFilePhysNm) {
		return requestRepository.deleteExistingFile(srFilePhysNm);
	}

	@Override
	public int deleteRequest(String srNo) {
		return requestRepository.deleteRequest(srNo);
	}

	@Override
	public List<RequestList> getRequestExcelList(List<String> requestArr) {
		RequestList requestSr = null;
		ArrayList<RequestList> requestList = new ArrayList<>();
		for(int i=0; i<requestArr.size(); i++) {
			requestSr = requestRepository.selectRequestSr(requestArr.get(i));
			requestList.add(requestSr);
		}
		
		return requestList;
	}

	

	
	
}
