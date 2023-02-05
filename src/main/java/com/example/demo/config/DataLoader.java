package com.example.demo.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Reserve;
import com.example.demo.model.User;
import com.example.demo.repository.ReserveRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;
import lombok.var;

@RequiredArgsConstructor // finalなフィールドを初期化する
@Component // 部品（コンポーネント）クラスであることを示す
public class DataLoader implements ApplicationRunner {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ReserveRepository reserveRepository;

	//	Beanの実行に使用される
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//		名前が "admin", パスワードが "password" のユーザを用意する
		var user = new User();
		user.setUsername("admin");
		user.setPassword(passwordEncoder.encode("password"));
		user.setAuthority(Authority.ADMIN);
		user.setAdmin(true);
		user.setEmail("example@example.com");
		user.setPhone("08062487125");
		user.setGender(0);
		user.setIncome(500);
		user.setIndustry("製造業");
		user.setCreated_at(LocalDate.now());
		user.setUpdated_at(LocalDate.now());
		user.setDelete_flag(0);
		//		ユーザが存在しない場合、登録する
		if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
			userRepository.save(user);
		}

		var reserve = new Reserve();
		reserve.setUser_id(user);
		reserve.setReserve_date(LocalDate.of(2023, 2, 11));
		reserve.setReserve_time(LocalTime.of(3, 15));
		reserve.setRemarks("備考欄に記入した内容。こんにちは。よろしくお願いします。ありがとうございました。500文字まで。");
		reserve.setArrow_flag(0);
		reserve.setDelete_flag(0);
		reserve.setCreated_at(LocalDateTime.now());
		reserve.setUpdated_at(LocalDateTime.now());
		//		予約情報が存在しない場合、登録する
		if (reserveRepository.findReservesByUser_id(user.getId()).isEmpty()) {
			reserveRepository.save(reserve);
		}

	}
}
