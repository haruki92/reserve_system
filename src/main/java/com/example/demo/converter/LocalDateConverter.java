package com.example.demo.converter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

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

	public static java.util.Date localDateToDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
