package com.team01.webapp.alarm.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.team01.webapp.alarm.service.IAlarmService;
import com.team01.webapp.model.Alarm;
import com.team01.webapp.model.Users;

@Controller
@RequestMapping("/alarm")
public class AlarmController extends TextWebSocketHandler {
	
	@Autowired
	IAlarmService alarmService;
	
	/**
	 * @author : 황건희
	 * @param session		알림 데이터를 가져오기 위한 유저의 정보
	 * @param model			View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@GetMapping("list")
	public String alarmList(HttpSession session, Model model) {
		//알림 수
		int userNo = (Integer) session.getAttribute("userNo");
		Alarm alarm = new Alarm();
		alarm.setUserNo(userNo);
		alarm.setUserType((String)session.getAttribute("userType"));
		model.addAttribute("userType",alarm.getUserType());
		if(alarm.getUserType().equals("관리자")) {
			Users loginUser = alarmService.selectLoginUser(userNo);
			alarm.setSysNo("%"+loginUser.getSysNo()+"%");
			model.addAttribute("sysNo",alarm.getSysNo());
		}else {			
			alarm.setSysNo("%"+(String)session.getAttribute("sysNo")+"%");
			model.addAttribute("sysNo",alarm.getSysNo());
		}
		//알림 리스트
		List<Alarm> alarmList = alarmService.selectAlarmList(alarm);
		int alarmCnt = alarmService.selectAlarmCount(alarm);
		model.addAttribute("alarmCnt",alarmCnt);
		model.addAttribute("alarmList",alarmList);
		return "alarm/list";
	}
	
	/**
	 * @author : 황건희
	 * @param alarm		해당 카테고리에 대한 알람 정보
	 * @param model		View로 데이터 전달을 위한 Model 객체 주입
	 * @return
	 */
	@PostMapping(value="categoryAlarm", produces="application/json; charset=UTF-8")
	public String selectCategoryAlarm(@RequestBody Alarm alarm, Model model) {
		List<Alarm> categoryAlarm = alarmService.selectCategoryAlarm(alarm);
		model.addAttribute("categoryAlarm",categoryAlarm);
		return "alarm/categoryList";
	}
	
	/**
	 * @author : 황건희
	 * @param alarm		읽은 알람에 대한 정보
	 * @return
	 */
	@PostMapping(value="updateAlarmCheck")
	public String updateAlarmCheck(@RequestBody Alarm alarm) {
		int alarmNo = alarm.getAlarmNo();
		alarmService.updateCheck(alarmNo);
		
		return "redirect:/alarm/list";
	}
	
	/**
	 * @author : 황건희
	 * @param alarm		알람 삭제
	 * @return
	 */
	@PostMapping(value="delete", produces="application/json; charset=UTF-8")
	public String deleteAlarm(@RequestBody Alarm alarm) {
		int alarmNo = alarm.getAlarmNo();
		alarmService.deleteAlarm(alarmNo);
		
		return "redirect:/alarm/list";
	}
	
	
}


