package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
@Entity
public class Reserve {
	/*
	 * 予約ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/*
	 * 予約日
	 */
	private LocalDate reserve_date;

	/*
	 * 予約時間
	 */
	private LocalTime reserve_time;

	/*
	 * ユーザID
	 * Userエンティティのidと関連付け
	 * ユーザ１ 対 予約多
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user_id;

	/*
	 * 予約変更許可フラグ 初期値 = 0
	 * 予約日前日になったら予約変更不可 = 1
	 */
	private Integer arrow_flag;

	/*
	 * 予約取消フラグ 初期値 = 0
	 * 予約取消 = 1
	 */
	private Integer delete_flag;

	/*
	 * 登録日時
	 */
	@CreatedDate
	private LocalDateTime created_at;

	/*
	 * 更新日時
	 */
	@UpdateTimestamp
	private LocalDateTime updated_at;

	/*
	 * 備考
	 * 500文字まで
	 */
	@Size(max = 500)
	private String remarks;
}
