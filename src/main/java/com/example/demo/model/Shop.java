package com.example.demo.model;

import java.sql.Time;
import java.time.DayOfWeek;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/*
 * 店舗側の予約設定に関するエンティティ
 */

@Data
@Entity
public class Shop {

	/*
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/*
	 * 予約可能開始日
	 * 何日前から予約できるかを設定
	 */
	@NotBlank
	private Integer reservable_date;

	/*
	 * 予約開始時間
	 */
	@NotBlank
	private Time start_time;

	/*
	 * 予約終了時間
	 */
	@NotBlank
	private Time end_time;

	/*
	 * 店休日
	 * 店が休みの曜日を設定
	 */
	private DayOfWeek store_holiday;
}
