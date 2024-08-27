
package com.techacademy.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {

	@ManyToOne()
	@JoinColumn(name = "employee_code", referencedColumnName = "code")
	Employee employee;

	// ID
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 10)
	private int id;

	// 日付
	@Column(nullable = false)
	@NotNull
	private LocalDateTime reportDate;

	// タイトル
	@Column(length = 100, nullable = false)
	@NotEmpty
	@Length(max = 100)
	private String title;

	// 内容
	@Column(nullable = false)
	@NotEmpty
	private String content;


	// 削除フラグ(論理削除を行うため)
	@Column(columnDefinition = "TINYINT", nullable = false)
	private boolean deleteFlg;

	// 登録日時
	@Column(nullable = false)
	private LocalDateTime createdAt;

	// 更新日時
	@Column(nullable = false)
	private LocalDateTime updatedAt;

}