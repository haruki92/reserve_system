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

	/**
	 * 予約を変更するメソッド
	 * @param 変更後の予約情報
	 * @param ユーザ情報
	 */
	public void change(Reserve reserve, User user) {
		Reserve rs = reserveRepository.findNotDeletedReserve(user.getId()).get();
		rs.setReserveDate(reserve.getReserveDate());
		rs.setReserveTime(reserve.getReserveTime());
		rs.setRemarks(reserve.getRemarks());
		rs.setChangeFlag(1);
		rs.setUpdatedAt(LocalDateTime.now());
		reserveRepository.save(rs);
	}

	//	TODO 予約キャンセルするメソッドを実装
	/**
	 * 予約をキャンセル・削除するメソッド
	 * @param ユーザ情報
	 */
	public void delete(User user) {
		Reserve rs = reserveRepository.findNotDeletedReserve(user.getId()).get();
		rs.setDeleteFlag(1);
		rs.setUpdatedAt(LocalDateTime.now());
		reserveRepository.save(rs);
	}
}
