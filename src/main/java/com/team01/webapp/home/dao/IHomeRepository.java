package com.team01.webapp.home.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IHomeRepository {

	System selectSysInfo(int userNo);

}