package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
	//	会員IDを条件に予約情報を取得する
	@Query("SELECT r FROM Reserve r, User u WHERE r.user_id.id = ?1")
	List<Reserve> findReservesByUser_id(int id);
}
