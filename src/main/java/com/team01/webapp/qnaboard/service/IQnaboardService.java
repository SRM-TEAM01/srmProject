package com.team01.webapp.qnaboard.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.team01.webapp.model.QSTN;
import com.team01.webapp.model.QSTNComment;
import com.team01.webapp.model.QSTNFile;
import com.team01.webapp.util.Pager;


public interface IQnaboardService {
	
	public List<QSTN> getQstnList(@Param("pager") Pager pager, @Param("qstn") QSTN qstn);
	public Pager returnPage(@Param("int") int pageNo, @Param("pager") Pager pager, @Param("qstn") QSTN qstn);
	
	public QSTN getDetail(int qstnNo);

	public QSTNComment writeComment(QSTNComment qComment);
	public List<QSTNComment> getCommentList(int qstnNo);
	public void updateComment(QSTNComment qComment);
	public void deleteComment(int qstnCmntNo);
	public int writeQSTN(QSTN qstn);
	
	public int countComment(int qstnNo);
	public List<MultipartFile> getQstnFileDetail(int qstnNo);
	public QSTNFile selectFiledownload(int qstnNo);
	public void qstnFileUpload(QSTNFile qstnFile);
	public int changeQstn(QSTN qstn);
	public int changeQstnFile(QSTNFile qstnFile);
	public int EraseExistingFile(String qstnFilePhysNm);
	
	public int eraseQstn(int qstnNo);
	

	
	

	
}
