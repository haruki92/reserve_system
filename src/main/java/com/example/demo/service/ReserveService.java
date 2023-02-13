package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Reserve;
import com.example.demo.model.User;
import com.example.demo.repository.ReserveRepository;

@Transactional
@Service
public class ReserveService {

	@Autowired
	ReserveRepository reserveRepository;

	/**
	 * 予約情報を予約可能か判別して登録するメソッド
	 * @param 予約情報
	 * @return 予約情報
	 */
	public Reserve reserve(Reserve reserve, User user) {

		reserve.setUser_id(user);
		reserve.setChangeFlag(0);
		reserve.setDeleteFlag(0);
		reserve.setCreatedAt(LocalDateTime.now());
		reserve.setUpdatedAt(LocalDateTime.now());
		reserveRepository.save(reserve);

		return reserve;
	}

	//	TODO 予約変更するメソッドを実装
	public void change() {

	}

	//	TODO 予約キャンセルするメソッドを実装
	public void cancel() {

	}
}
