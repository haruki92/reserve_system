package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
	//	第一引数を渡すときは?1, 第二引数は?2 
	/*
	 * 会員IDを条件に予約情報を取得する
	 */
	@Query("SELECT r FROM Reserve r, User u WHERE r.user_id.id = ?1")
	Optional<Reserve> findReservesByUser_id(int id);

	/**
	 * 予約日を条件に予約情報を取得
	 * @param 予約日
	 * @return 予約情報
	 */
	Optional<Reserve> findByReserveDate(LocalDate reserveDate);

	/**
	 * 予約日時を条件に予約情報IDを取得する
	 * (取得できる = 予約があるためその日時には予約できない)
	 * @param 日付
	 * @param 時間
	 * @return 予約情報
	 */
	Optional<Reserve> findIdByReserveDateAndReserveTime(LocalDate reserveDate, LocalTime reserveTime);
}
