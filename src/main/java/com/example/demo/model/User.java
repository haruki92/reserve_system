package com.example.demo.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import com.example.demo.util.Authority;

import lombok.Data;

@Data
@Entity
public class User {
    /*
     * 顧客ID（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
     * ユーザ名
     */
    @NotBlank
    @Size(min = 2, max = 40)
    private String username;

    /*
     * パスワード
     */
    @NotBlank
    @Size(min = 6, max = 64)
    private String password;

    /*
     * 権限
     */
    private Authority authority;

    /*
     * 管理者
     */
    private boolean admin;

    /*
     * メールアドレス
     */
    @NotBlank
    @Email
    @Size(max = 254)
    private String email;

    /*
     * 電話番号
     */
    @NotBlank
    @Pattern(regexp = "[0-9-]*")
    private String phone;

    /*
     * 性別
     */
    private Integer gender;

    /*
     * 年収
     */
    private Integer income;

    /*
     * 業種
     */
    private String industry;

    /*
     * 登録日時
     * Entityが生成され、保存される時に時間が自動で保存される
     */
    @CreatedDate
    private LocalDate created_at;

    /*
     * 更新日時
     * updateTimestampはテーブルの内容に変更がない場合はupdateしない
     * LastModifiedDateはEntityの値を変更する時に時間が自動で保存される
     */
    @UpdateTimestamp
    private LocalDate updated_at;

    /*
     * 削除フラグ
     */
    private Integer delete_flag;
}
