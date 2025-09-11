package com.expanse_tracker;

import com.expanse_tracker.models.*;
import com.expanse_tracker.repository.CategoryRepository;
import com.expanse_tracker.repository.UserRepository;
import com.expanse_tracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@SpringBootApplication
public class ApiExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiExpenseTrackerApplication.class, args);
	}


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CategoryService categoryService;

	@Bean
	CommandLineRunner init(){
		return args -> {

			UserEntity user = UserEntity.builder()
					.username("lauti")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(RoleEntity.builder().name(ERole.valueOf(ERole.USER.name())).build()))
					.build();
			// Inicializar la lista después
			if (user.getExpenses() == null) {
				user.setExpenses(new ArrayList<>());
			}
			userRepository.save(user);


			// Crear algunos gastos con fechas distintas
			ExpenseEntity expense1 = ExpenseEntity.builder()
					.description("Supermercado")
					.amount(50.0)
					.expenseDate(LocalDate.now().minusDays(2)) // hace 2 días
					.category(categoryService.findByCategory("COMESTIBLES"))
					.user(user)
					.build();

			ExpenseEntity expense2 = ExpenseEntity.builder()
					.description("Transporte")
					.amount(20.0)
					.expenseDate(LocalDate.now().minusWeeks(1)) // hace 1 semana
					.category(categoryService.findByCategory("SALUD"))
					.user(user)
					.build();

			ExpenseEntity expense3 = ExpenseEntity.builder()
					.description("Ropa")
					.amount(100.0)
					.expenseDate(LocalDate.now().minusMonths(1)) // hace 1 mes
					.category(categoryService.findByCategory("ROPA"))
					.user(user)
					.build();

			// Agregar gastos al usuario
			user.getExpenses().add(expense1);
			user.getExpenses().add(expense2);
			user.getExpenses().add(expense3);

			// Guardar usuario de nuevo para que cascade ALL persista los gastos
			userRepository.save(user);



		};
	}

}
