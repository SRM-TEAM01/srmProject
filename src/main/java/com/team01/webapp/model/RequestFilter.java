package com.team01.webapp.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestFilter {
	private List<System> SysNmList;
	private List<SRType> SrTypeList;
	private List<SRStts> SrSttsList;
	private List<Users> UserOgdpList;
	private List<SR> SrDevDpList;
}
