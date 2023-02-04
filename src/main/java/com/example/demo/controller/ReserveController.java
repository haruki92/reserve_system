package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Reserve;
import com.example.demo.repository.ReserveRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReserveController {
	private final ReserveRepository reserveRepository;

	/*
	 * 予約ページからのPOSTリクエスト
	 * 予約情報：日付(date)、時間(time、備考欄
	 */
	@PostMapping("/reserve")
	public String reserve(@Validated @ModelAttribute("reserve") Reserve reserve,
			BindingResult result) {

		//		エラーがある場合は、元の画面に戻る
		if (result.hasErrors()) {
			return "/reserve";
		}
		reserve.setReserve_date(reserve.getReserve_date()); // 日付をセット
		reserve.setReserve_Time(reserve.getReserve_Time()); // 時刻をセット
		reserve.setRemarks(reserve.getRemarks()); // 備考欄をセット
		reserve.setUser_id(reserve.getUser_id()); // 会員IDをセット

		reserveRepository.save(reserve); // 予約情報を保存する

		return "redirect:/user?reserve";
	}

	@GetMapping("/admin/reserve_list")
	public String showAdminList(Model model) {
		model.addAttribute("reserves", reserveRepository.findAll());
		return "reserve_list";
	}
}
