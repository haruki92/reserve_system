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
		user.setIncome(2);
		user.setIndustry(3);
		user.setCreated_at(LocalDateTime.now());
		user.setUpdated_at(LocalDateTime.now());
		user.setDelete_flag(0);

		var haruki = new User();
		haruki.setUsername("haruki");
		haruki.setPassword(passwordEncoder.encode("haruki"));
		haruki.setAuthority(Authority.USER);
		haruki.setAdmin(false);
		haruki.setEmail("example@example.com");
		haruki.setPhone("08062487125");
		haruki.setGender(0);
		haruki.setIncome(2);
		haruki.setIndustry(3);
		haruki.setCreated_at(LocalDateTime.now());
		haruki.setUpdated_at(LocalDateTime.now());
		haruki.setDelete_flag(0);

		var reserve = new Reserve();
		reserve.setUser_id(user);
		reserve.setReserveDate(LocalDate.of(2023, 2, 11));
		reserve.setReserveTime(LocalTime.of(3, 00));
		reserve.setRemarks("備考欄に記入した内容。こんにちは。よろしくお願いします。ありがとうございました。500文字まで。");
		reserve.setChangeFlag(0);
		reserve.setDeleteFlag(0);
		reserve.setCreatedAt(LocalDateTime.now());
		reserve.setUpdatedAt(LocalDateTime.now());

		var testReserve = new Reserve();
		testReserve.setUser_id(user);
		testReserve.setReserveDate(LocalDate.of(2023, 5, 5));
		testReserve.setReserveTime(LocalTime.of(1, 11));
		testReserve.setRemarks("うんこ");
		testReserve.setChangeFlag(1);
		testReserve.setDeleteFlag(1);
		testReserve.setCreatedAt(LocalDateTime.now());
		testReserve.setUpdatedAt(LocalDateTime.now());
		//		ユーザが存在しない場合、登録する
		if (userRepository.findByUsername(haruki.getUsername()).isEmpty()) {
			userRepository.save(user);
			userRepository.save(haruki);
			//		予約情報が存在しない場合、登録する
			if (reserveRepository.findReservesByUser_id(haruki.getId()).isEmpty()) {
				reserveRepository.save(reserve);
				reserveRepository.save(testReserve);
			}
		}

	}
}
