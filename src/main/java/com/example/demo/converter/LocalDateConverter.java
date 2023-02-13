package com.example.demo.converter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

	/*
	 * LocalDateオブジェクトからjava.sql.Dateオブジェクトに変換する
	 */
	@Override
	public Date convertToDatabaseColumn(LocalDate date) {
		return date == null ? null : Date.valueOf(date);
	}

	/*
	 * java.sql.DateオブジェクトからLocalDateオブジェクトに変換する
	 */
	@Override
	public LocalDate convertToEntityAttribute(Date value) {
		return value == null ? null : value.toLocalDate();
	}

	/**
	 * LocalDateオブジェクトからjava.util.Dateに変換する
	 * @param LocalDateオブジェクト
	 */
	public static java.util.Date localDateToDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * java.util.DateオブジェクトからLocalDateに変換する
	 * @param java.util.Dateオブジェクト
	 */
	public static LocalDate dateToLocalDate(final java.util.Date date) {
		LocalDateTime localDateTime = new Timestamp(date.getTime()).toLocalDateTime();
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}

	/**
	 * StringからLocalDateへ変換する
	 * @param 日付の文字列
	 * @param フォーマットパターン yyyy年MM月dd日 など
	 * @return LocalDate型の日付
	 */
	public static LocalDate stringToLocalDate(String date, String format) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
	}
}
