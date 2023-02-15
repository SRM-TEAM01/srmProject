package com.team01.webapp.model;

import java.sql.Date;

import lombok.Data;

@Data
public class HR {
	private String srNo;
	private int userNo;
	private int taskNo;
	private Date hrStartDate;
	private Date hrEndDate;
	private String hrLeader;
	
	private String userNm;
	private String userDpNm;
	private String userJbps;
	private String userTelNo;
	private String taskNm;
}
