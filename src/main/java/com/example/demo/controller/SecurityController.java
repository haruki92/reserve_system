package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

		//		ログイン者のIDを条件に予約情報を取得できた時にセット
		Optional<Reserve> reserve = reserveRepository.findReservesByUser_id(user.get().getId());
		if (!reserve.isEmpty()) {
			model.addAttribute("reserve", reserve.get());
		}

		return "user";
	}

	@GetMapping("/register")
	public String register(@ModelAttribute("user") User user) {
		return "register";
	}

	@GetMapping("/confirm")
	public String getConfirm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		//		性別が未選択の場合、"選択しない"をセット
		if (user.getGender() == null) {
			user.setGender(0);
		}
		model.addAttribute(user);

		return "confirm";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated @ModelAttribute("user") User user, BindingResult result,
			Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "register";
		}
		HttpSession session = request.getSession();
		session.setAttribute("user", user);

		model.addAttribute("user", user);
		return "redirect:/confirm";
	}

	@PostMapping("/register")
	public String process(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		//		パスワードを暗号化してセット
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		//		その他ユーザ情報をセット
		user.setCreated_at(LocalDateTime.now());
		user.setUpdated_at(LocalDateTime.now());
		user.setDelete_flag(0);

		//		管理者の場合
		if (user.isAdmin()) {
			user.setAuthority(Authority.ADMIN); // authorityをadminに
		} else {
			//		そうでない場合は
			user.setAuthority(Authority.USER); // authorityをuserに
		}

		//		ユーザー情報を保存
		userRepository.save(user);

		System.err.println("登録が完了しました");
		//		リダイレクト
		return "redirect:/login?register";

	}
}