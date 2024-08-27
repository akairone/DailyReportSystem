package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

	private final ReportRepository reportRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
		this.reportRepository = reportRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// 日報保存
	@Transactional
	public List<Report> save(Report report) {
		return reportRepository.findAll();

	}

	// 従業員更新
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

	public Report findById(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	// 従業員削除
	@Transactional
	public ErrorKinds delete(int id, ContentDetail contentDetail) {
		return null;

	}

	// 従業員一覧表示処理
	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	// 1件を検索
	public Report findById(String id) {
		// findByIdで検索
		Optional<Report> option = reportRepository.findById(id);
		// 取得できなかった場合はnullを返す
		Report report = option.orElse(null);
		return report;
	}

}
