package com.dmm.task.model;

import java.util.Calendar;
import java.util.Date;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class DateModel {
	@GetMapping("/main")
	public String main(Model model) {
		Date date = new Date();
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		cl.add(Calendar.MONTH, 1);
		cl.add(Calendar.MONTH, -1);
		Date d1 = cl.getTime();
		//model.addAttribute("month", sdf.format(cl.getTime()));
		model.addAttribute("month", d1);
		return "main";
	}
}
