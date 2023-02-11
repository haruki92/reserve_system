package com.example.demo.converter;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// エンティティのLocalTime型のフィールドに対して自動で適用される
@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {

	/*
	 * LocalTimeオブジェクトからjava.sql.Timeオブジェクトに変換する
	 */
	@Override
	public Time convertToDatabaseColumn(LocalTime time) {
		return time == null ? null : Time.valueOf(time);
	}

	/*
	 * java.sql.TimeオブジェクトからTimeオブジェクトに変換する
	 */
	@Override
	public LocalTime convertToEntityAttribute(Time value) {
		return value == null ? null : value.toLocalTime();
	}

}
