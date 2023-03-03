package com.team01.webapp.model;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DevelopDto {
	private String srNo;
	private int srCustNo;
	private String sysNo;
	private int sttsNo;
	private String srTtl;
	private String srCn;
	private Date srRegDate;
	private String srPry;
	private String srOpnn;
	private int srBgt;
	private String srDevCn;
	private String srReqSe;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date srDdlnDate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date srStartDate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date srEndDate;
	private String srViewYn;
	private String srStd;
	private String srDevDp;
	
	//등록자 , 소속, 개발부서, 등록날짜, 파일
	private String userNm;
	private String userOgdp;
	private String userDpNm;
	private String sttsNm;
	private String sysNm;
	private String userType;

	private Date srRegStartDate;
	private Date srRegEndDate;
	
	private int seq;
	
	//페이징 처리
	private int startRowNo;
	private int endRowNo;
	
	//첨부파일
	private List<SrFile> srDevelopFile;
}
