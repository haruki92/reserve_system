package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Reserve;
import com.example.demo.model.User;
import com.example.demo.repository.ReserveRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SecurityController {
	private final UserRepository userRepository;
	private final ReserveRepository reserveRepository;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String showList(Authentication loginUser, Model model) {
		model.addAttribute("username", loginUser.getName());
		model.addAttribute("authority", loginUser.getAuthorities());
		Optional<User> user = userRepository.findByUsername(loginUser.getName());

		//		findByUsernameでユーザー情報を取得できるが、get()しないとThymeleafに値を渡せない
		model.addAttribute("user", user.get());

		//		ログイン者のIDを条件に予約情報を取得してセット
		List<Reserve> reserves = reserveRepository.findReservesByUser_id(user.get().getId());
		model.addAttribute("reserve", reserves);

		if (reserves.isEmpty()) {
			System.err.println(user.get().getUsername() + " さんの予約情報はありません");
		}

		return "user";
	}

	@GetMapping("/admin/list")
	public String showAdminList(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "list";
	}

	@GetMapping("/register")
	public String register(@ModelAttribute("user") User user) {
		return "register";
	}

	@PostMapping("/register")
	public String process(@Validated @ModelAttribute("user") User user, BindingResult result) {
		//		エラーがある場合はerrorパラメータを持ってregisterへ戻る
		if (result.hasErrors()) {
			return "register";
		}

		//		パスワードをセット
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		//		管理者の場合
		if (user.isAdmin()) {
			user.setAuthority(Authority.ADMIN); // authorityをadminに
		} else {
			//		そうでない場合は
			user.setAuthority(Authority.USER); // authorityをuserに
		}

		//		ユーザー情報を保存
		userRepository.save(user);

		//		リダイレクト
		return "redirect:/login?register";

	}
}