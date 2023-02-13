package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.converter.LocalDateConverter;
import com.example.demo.model.Reserve;
import com.example.demo.model.User;
import com.example.demo.repository.ReserveRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReserveService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReserveController {
	private final ReserveRepository reserveRepository;
	private final UserRepository userRepository;

	@Autowired
	ReserveService reserveService;

	@GetMapping("/reserve")
	public String showReserve(@ModelAttribute("reserve") Reserve reserve, Model model) {
		LocalDate date = LocalDate.of(2023, 2, 13);

		//		00:00から24:00まで1時間刻みでLocaltimeオブジェクトを作成しリストに格納する
		//		TODO 開始時間と終了時間は店舗側で任意の値に設定できる仕様にする
		List<LocalTime> timelist = Stream.iterate(LocalTime.of(0, 0), t -> t.plusHours(1))
				.limit(24)
				.collect(Collectors.toList());

		//		日時の計算をするためのCalenderクラスのインスタンスを生成
		Calendar calender = Calendar.getInstance();

		//		LocalDateTimeからDateに変換してcalenderにセット
		calender.setTime(LocalDateConverter.localDateToDate(date));

		List<LocalDate> dates = new ArrayList<>();

		//		TODO 10 のところは店舗側で任意の値に設定できる仕様にする
		for (int i = 1; i < 10; i++) { //		LocalDate型で指定した日付から前10日の日付を取得する
			calender.add(Calendar.DATE, -1);
			dates.add(LocalDateConverter.dateToLocalDate(calender.getTime()));
		}
		System.err.println(dates);

		model.addAttribute("timelist", timelist);
		model.addAttribute("dates", dates);
		return "reserve/reserve";
	}

	@GetMapping("/reserve/confirm")
	public String getReserveConfirm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Reserve reserve = (Reserve) session.getAttribute("reserve");
		model.addAttribute("reserve", reserve);

		return "reserve/confirm";
	}

	/*
	 * 予約ページからのPOSTリクエスト
	 * 予約情報：日付(date)、時間(time、備考欄
	 */
	@PostMapping("/reserve/confirm")
	public String reserveConfirm(@Validated @ModelAttribute("reserve") Reserve reserve, BindingResult result,
			Model model, HttpServletRequest request) {

		Optional<Reserve> r = reserveRepository.findIdByReserveDateAndReserveTime(reserve.getReserveDate(),
				reserve.getReserveTime());
		//		予約情報が取得できた場合は既に予約で埋まっている
		if (!r.isEmpty()) {
			System.err.println("予約が埋まっているためエラーが発生");
			return "redirect:/reserve?error";
		}

		if (result.hasErrors()) {
			model.addAttribute("errors");
			return "reserve/reserve";
		}

		HttpSession session = request.getSession();
		session.setAttribute("reserve", reserve);
		model.addAttribute("reserve", reserve);

		return "redirect:/reserve/confirm";
	}

	/*
	 * 予約確認ページからのPOSTリクエスト
	 * 予約情報を登録
	 */
	@PostMapping("/reserve/complete")
	public String complete(Authentication loginUser, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Reserve reserve = (Reserve) session.getAttribute("reserve");
		Optional<User> user = userRepository.findByUsername(loginUser.getName());

		try {
			//		予約情報をDBに登録する
			reserveService.reserve(reserve, user.get());
		} catch (Exception e) {
			// 予約が埋まっている場合はExceptionをスローするためmodelにエラーメッセージを追加して予約画面に戻る
			model.addAttribute("error", e.getMessage());
			System.err.println(e.getMessage());
			return "redirect:/reserve";
		}

		return "reserve/complete";
	}

	@GetMapping("/admin/reserve_list")
	public String showAdminList(Model model) {
		model.addAttribute("reserves", reserveRepository.findAll());
		return "reserve_list";
	}
}
