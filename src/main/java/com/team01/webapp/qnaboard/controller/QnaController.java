package com.team01.webapp.qnaboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team01.webapp.qnaboard.dto.Qnaboard;
import com.team01.webapp.qnaboard.service.QnaboardService;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/qna")
public class QnaController {
	
	@Autowired
	private QnaboardService qnaboardService;
	
	@GetMapping("/list")
	public String getList(Model model) {
		List<Qnaboard> list = qnaboardService.getList();
		model.addAttribute("qnalist", list);
		log.info("qna목록보기");
		return "qnaboard/qnalist";
	}
	
	
}
