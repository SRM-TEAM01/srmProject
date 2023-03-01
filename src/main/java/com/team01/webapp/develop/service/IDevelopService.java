package com.team01.webapp.develop.service;

import java.util.List;

import com.team01.webapp.model.DevelopFilter;
import com.team01.webapp.model.Examine;
import com.team01.webapp.model.ExamineList;
import com.team01.webapp.model.HR;
import com.team01.webapp.model.Progress;
import com.team01.webapp.model.SR;
import com.team01.webapp.model.SrFile;
import com.team01.webapp.model.DevelopDto;
import com.team01.webapp.model.Users;
import com.team01.webapp.util.Pager;

public interface IDevelopService {
	public List<DevelopDto> getDevelopList(Pager pager, DevelopDto developDto);
	public Pager returnPage(int pageNo, Pager pager, DevelopDto developDto);
	public DevelopFilter filterList(DevelopFilter developFilter);
	
	public DevelopDto getDetail(String srNo);
	
	public List<Users> getDevelopList();
	public int updateDevelopSr(DevelopDto srDevelop);
	public List<Users> selectDeveloperList(String userDpNm, int userNo, String hrStartDate, String hrEndDate);
	
	public List<Users> selectDevName(int userNo);
	public int insertHrList(List<HR> listHR);
	public SrFile getSrFile(int srFileNo);
	
}
