package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;

import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.ContentDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

	private final ReportService reportService;

	@Autowired
	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	// 日報一覧画面
	@GetMapping
	public String list(Model model) {

		model.addAttribute("listSize", reportService.findAll().size());
		model.addAttribute("reportList", reportService.findAll());

		return "reports/list";
	}

	// 日報詳細画面
	@GetMapping(value = "/{id}/")
	public String detail(@PathVariable String id, Model model) {

		model.addAttribute("report", reportService.findById(id));
		return "reports/detail";
	}

	// 日報更新画面
	@GetMapping(value = "/{id}/update")
	public String getupdate(@PathVariable String id, Model model, Report report) {
		if (id == null) {
			model.addAttribute("report", report);
		} else {

			model.addAttribute("report", reportService.findById(id));
		}

		return "reports/update";
	}

	// 日報更新処理
	@PostMapping(value = "/update")
	public String postupdate(@Validated Report report, BindingResult res, Model model) {



		if (res.hasErrors()) {
			return getupdate(null, model, report);
		}

		ErrorKinds result = reportService.update(report);

		if (ErrorMessage.contains(result)) {
			model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
			return getupdate(null, model, report);
		}



		return "redirect:/reports";
	}

	// 日報新規登録画面
	@GetMapping(value = "/add")
	public String create(@ModelAttribute Report report) {

		return "reports/new";
	}

	// 日報新規登録処理
	@PostMapping(value = "/add")
	public String add(@Validated Report report, BindingResult res, Model model) {


		// 入力チェック
		if (res.hasErrors()) {
			return create(report);
		}


		try {
			ErrorKinds result = reportService.save(report);

			if (ErrorMessage.contains(result)) {
				model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
				return create(report);
			}

		} catch (DataIntegrityViolationException e) {
			model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
					ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
			return create(report);
		}

		return "redirect:/reports";
	}

	// 日報削除処理
	@PostMapping(value = "/{code}/delete")
	public String delete(@PathVariable String id, @AuthenticationPrincipal ContentDetail contentDetail, Model model) {

		ErrorKinds result = reportService.delete(id, contentDetail);

		if (ErrorMessage.contains(result)) {
			model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
			model.addAttribute("report", reportService.findById(id));
			return detail(id, model);
		}

		return "redirect:/reports";
	}

}
