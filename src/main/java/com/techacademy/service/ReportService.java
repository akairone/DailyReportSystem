package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReportService {

	private final ReportRepository reportRepository;
	@Autowired
	public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
		this.reportRepository = reportRepository;
	}


	// 日報保存
	@Transactional
	public ErrorKinds save(Report report) {
		//return reportRepository.findAll();
		LocalDateTime now = LocalDateTime.now();
		report.setCreatedAt(now);
		report.setUpdatedAt(now);

		reportRepository.save(report);
		return ErrorKinds.SUCCESS;


	}

	// 日報更新
	@Transactional
	public ErrorKinds update(Report report) {

		Report rep = findById(report.getId());

		report.setDeleteFlg(false);

		LocalDateTime now = LocalDateTime.now();
		report.setCreatedAt(rep.getCreatedAt());
		report.setUpdatedAt(now);

		reportRepository.save(report);
		return ErrorKinds.SUCCESS;
	}



	// 日報削除
	@Transactional
	public ErrorKinds delete(int id, ContentDetail contentDetail) {

		Report report = findById(id);
		LocalDateTime now = LocalDateTime.now();
		report.setUpdatedAt(now);
		report.setDeleteFlg(true);
		return ErrorKinds.SUCCESS;

	}

	// 日報一覧表示処理
	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	// 1件を検索
	public Report findById(int id) {
		// findByIdで検索
		Optional<Report> option = reportRepository.findById(id);
		// 取得できなかった場合はnullを返す
		Report report = option.orElse(null);
		return report;
	}

	// 従業員と日付が一致する日報取得
	public List<Report> findByEmpAndDate(Employee employee, LocalDate reportDate ) {
		return reportRepository.findByEmployeeAndReportDate(employee, reportDate);
	}




//	// 表示中の従業員と日付が一致する日報取得
//		public List<Report> findByDisAndDate(Integer id, LocalDate reportDate ) {
//			return reportRepository.findByIdAndReportDate(id, reportDate);
//		}

}
