
package com.techacademy.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {



    // ID
    @Id
    @Column(length = 10)
    @NotEmpty
    @Length(max = 10)
    private int id;

    @ManyToOne
    Employee employee;

    //日付
    @Column(nullable = false)
    @NotEmpty
    private LocalDateTime reportDate;

    // タイトル
    @Column(length = 100,nullable = false)
    @NotEmpty
    @Length(max = 100)
    private String title;

    // 内容
    @Column(nullable = false)
    @NotEmpty
    private String content;

    // 社員番号
    @Column(length = 10, nullable = false)
    @NotEmpty
    @Length(max = 10)
    private String employeeCode;


    // 削除フラグ(論理削除を行うため)
    @Column(columnDefinition="TINYINT", nullable = false)
    private boolean deleteFlg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}