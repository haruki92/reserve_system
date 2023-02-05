package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
	//	会員IDを条件に予約情報を取得する　第一引数を渡すときは?1, 第二引数は?2 
	@Query("SELECT r FROM Reserve r, User u WHERE r.user_id.id = ?1")
	Optional<Reserve> findReservesByUser_id(int id);
}
