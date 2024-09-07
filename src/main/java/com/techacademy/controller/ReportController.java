package com.techacademy.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;
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
	public String detail(@PathVariable int id, Model model) {

		model.addAttribute("report", reportService.findById(id));
		return "reports/detail";

	}

	// 日報更新画面
	@GetMapping(value = "/{id}/update")
	public String getUpdate(@PathVariable("id") Integer id, Model model, Report report) {
		if (id == null) {
			model.addAttribute("report",report);
		} else {
		model.addAttribute("report", reportService.findById(id));
		}
		return "reports/update";

	}

	// 日報更新処理
	@PostMapping(value = "/{id}/update")
	public String postUpdate( @Validated Report report, BindingResult res, Model model) {

		if (res.hasErrors()) {
			model.addAttribute("report", report);
			return getUpdate(null, model, report);
		}
		report.setEmployee(report.getEmployee());
		ErrorKinds result = reportService.update(report);

		List<Report> existingReports = reportService.findByEmpAndDate(report.getEmployee(), report.getReportDate());
		if (!existingReports.isEmpty()) {
			for (Report existingReport : existingReports) {

				if (existingReport.getId() != report.getId()) {
					model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
							ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
					return getUpdate(null, model, report);
				}
			}
		}

		return "redirect:/reports";
	}

	// 日報新規登録画面
	@GetMapping(value = "/add")
	public String create(@ModelAttribute Report report, @AuthenticationPrincipal UserDetail ud) {

		report.setEmployee(ud.getEmployee());
//			model.addAttribute("report", report); @ModelAttributeのアノテーションが付いているのでわざわざrequestスコープに登録する必要はありませんでした。
		return "reports/new";
	}

	// 日報新規登録処理
	@PostMapping(value = "/add")
	public String add(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail ud,
			Model model) {

		// 入力チェック
		if (res.hasErrors()) {
			return create(report, ud);
		}

		try {
			report.setEmployee(ud.getEmployee());

			List<Report> existingReports = reportService.findByEmpAndDate(ud.getEmployee(), report.getReportDate());
			if (!existingReports.isEmpty()) {
				model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
						ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
				return create(report, ud);

			}
			ErrorKinds result = reportService.save(report);

		} catch (DataIntegrityViolationException e) {

			return create(report, ud);
		}

		return "redirect:/reports";
	}

	// 日報削除処理
	@PostMapping(value = "/{id}/delete")
	public String delete(@PathVariable int id, @AuthenticationPrincipal ContentDetail contentDetail, Model model) {

		ErrorKinds result = reportService.delete(id, contentDetail);

		if (ErrorMessage.contains(result)) {
			model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
			model.addAttribute("report", reportService.findById(id));
			return detail(id, model);
		}

		return "redirect:/reports";
	}

}
