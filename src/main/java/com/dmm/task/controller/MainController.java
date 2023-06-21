package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.TaskForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class MainController {
	@GetMapping("/main")
	//@PreAuthorize("hasRole('ROLE_ADMIN')") // 追記 ROLE_ADMINのユーザのみアクセスを許可
	public String main(Model model, @AuthenticationPrincipal AccountUserDetails user) {
		
		List<List<LocalDate>> month = new ArrayList<>();
		List<LocalDate> week = new ArrayList<>();
		
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		
		LocalDate day;
		day = LocalDate.now();
		day = LocalDate.of(day.getYear(),day.getMonthValue(), 1);
		
		DayOfWeek w = day.getDayOfWeek();
		day = day.minusDays(w.getValue());
		
		for(int i = 6; i <= day.lengthOfMonth(); i++) {
			//最終週の翌月分をDayOfWeekの値を使って計算し、6．で生成したリストへ格納し、最後に1．で生成したリストへ格納する
			w = day.getDayOfWeek();
			
			if(w == DayOfWeek.SATURDAY){
				month.add(week);
				week.add(day);
				day = day.plusDays(1);
				week = new ArrayList<>();
			}
			week.add(day);
			day = day.plusDays(1);
		}
		
		w = day.getDayOfWeek();
		for(int i = 1; i <= 7 - w.getValue(); i++) {
			
			day = day.plusDays(1);
			week.add(day);
		}
		
		month.add(week);
		week = new ArrayList<>();
		
		List<Tasks> list;
		if(user.admin) {
			list = repo.findAllByDateBetween();
		} else {
			list = repo.findByDateBetween();
		}
		//list = repo.findAllByDateBetween(Sort.by(Sort.Direction.DESC, "id"));
		for(Tasks t : list) {
			tasks.add(t.getDate(), t);
		}
		model.addAttribute("prev", day.minusMonths(1));
		model.addAttribute("next", day.plusMonths(1));
		
		model.addAttribute("matrix", month);
		model.addAttribute("tasks", tasks);	
		
		return "main";
	}

	/**
	   * タスクの新規作成画面.
	   * @param model モデル
	   * @param date 追加対象日
	   * @return
	   */
	
	 @GetMapping("/main/create/{date}")
	 public String create(Model model, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		 
		 return "create";
	 }
	 
	@Autowired
		private TasksRepository repo;
	@PostMapping("/main/create")
	public String create(Model model, TaskForm form, @AuthenticationPrincipal AccountUserDetails user) {

	    Tasks task = new Tasks();

	    task.setName(user.getName());
	    task.setTitle(form.getTitle());
	    task.setText(form.getText());
	    task.setDate(form.getDate());
	    task.setDone(form.isDone());

	    repo.save(task);
	     
	    return "redirect:/main";
	}

	@GetMapping("/main/edit/{id}")
	public String edit(Model model, @PathVariable Integer id) {
		
		Tasks task = repo.getById(id);	
		model.addAttribute("task", task);
		return "edit";
	}
	
	@PostMapping("main/edit/{id}")
	public String update(Model model, @PathVariable Integer id, TaskForm form, @AuthenticationPrincipal AccountUserDetails user) {
		Tasks task = repo.getById(id);
		
		task.setName(user.getName());
	    task.setTitle(form.getTitle());
	    task.setText(form.getText());
	    task.setDate(form.getDate());
	    task.setDone(form.isDone());

	    repo.save(task);
		return "redirect:/main";
		
	}
	
	@PostMapping("/main/delete/{id}")
	public String delete(@PathVariable Integer id) {
		repo.deleteById(id);
		return "redirect:/main";
	}
	
}
