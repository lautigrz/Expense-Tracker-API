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
import java.util.List;
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

			List<CategoryEntity> categories = List.of(
					new CategoryEntity(null, ECategory.COMIDA, "restaurant", "#34d399","#e0f2f1"),
					new CategoryEntity(null, ECategory.TRANSPORTE, "directions_bus", "#3b82f6","#dbeafe"),
					new CategoryEntity(null, ECategory.COMBUSTIBLE, "local_gas_station", "#FFA726","#FF7043"),

					new CategoryEntity(null, ECategory.SERVICIOS, "receipt_long", "#2bc52d","#C5FAC7"),
					new CategoryEntity(null, ECategory.ROPA, "checkroom", "#6765FC","#C6C5FA"),
					new CategoryEntity(null, ECategory.COMPRAS, "shopping_bag", "#f97316","#ffedd5"),
					new CategoryEntity(null, ECategory.SALUD, "health_and_safety", "#FF0D0D","#FFB3B3"),
					new CategoryEntity(null, ECategory.EDUCACION, "school", "#38D8FF","#D1F6FF"),
					new CategoryEntity(null, ECategory.OCIO, "celebration", "#929411","#FDFFCC"),
					new CategoryEntity(null, ECategory.VIAJES, "flight_takeoff", "#2B9EFF","#CCE7FF"),
					new CategoryEntity(null, ECategory.MASCOTAS, "pets", "#FFBF2B","#FFF4D4"),
					new CategoryEntity(null, ECategory.SEGUROS, "verified_user", "FF4624","FA9E8E"),
					new CategoryEntity(null, ECategory.IMPUESTOS, "account_balance", "#FF9F33","#FFEAD4"),
					new CategoryEntity(null, ECategory.OTROS, "category", "#C7DFFF","#3388FF")
			);

			for (CategoryEntity c : categories) {
				categoryService.createCategory(c);
			}

			UserEntity user = UserEntity.builder()
					.username("lauti")
					.password(passwordEncoder.encode("12345"))
					.build();

			if (user.getExpenses() == null) {
				user.setExpenses(new ArrayList<>());
			}
			userRepository.save(user);


			// Crear algunos gastos con fechas distintas
			ExpenseEntity expense1 = ExpenseEntity.builder()
					.description("Supermercado")
					.amount(50.0)
					.expenseDate(LocalDate.now().minusDays(2)) // hace 2 días
					.category(categoryService.findByCategory("COMIDA"))
					.user(user)
					.build();

			ExpenseEntity expense2 = ExpenseEntity.builder()
					.description("Transporte")
					.amount(20.0)
					.expenseDate(LocalDate.now().minusWeeks(1)) // hace 1 semana
					.category(categoryService.findByCategory("TRANSPORTE"))
					.user(user)
					.build();

			ExpenseEntity expense3 = ExpenseEntity.builder()
					.description("Ropa")
					.amount(100.0)
					.expenseDate(LocalDate.now().minusMonths(1)) // hace 1 mes
					.category(categoryService.findByCategory("ROPA"))
					.user(user)
					.build();

            ExpenseEntity expense4 = ExpenseEntity.builder()
                    .description("Cena con amigos")
                    .amount(75.0)
                    .expenseDate(LocalDate.now()) // hoy
                    .category(categoryService.findByCategory("OCIO"))
                    .user(user)
                    .build();

            ExpenseEntity expense5 = ExpenseEntity.builder()
                    .description("Netflix")
                    .amount(15.0)
                    .expenseDate(LocalDate.now().minusDays(10)) // hace 10 días
                    .category(categoryService.findByCategory("SERVICIOS"))
                    .user(user)
                    .build();

            ExpenseEntity expense6 = ExpenseEntity.builder()
                    .description("Gimnasio")
                    .amount(40.0)
                    .expenseDate(LocalDate.now().minusMonths(2)) // hace 2 meses
                    .category(categoryService.findByCategory("OTROS"))
                    .user(user)
                    .build();

            ExpenseEntity expense7 = ExpenseEntity.builder()
                    .description("Vacaciones")
                    .amount(500.0)
                    .expenseDate(LocalDate.now().minusYears(1)) // hace 1 año
                    .category(categoryService.findByCategory("VIAJES"))
                    .user(user)
                    .build();


            user.getExpenses().addAll(
                    List.of(expense1, expense2, expense3, expense4, expense5, expense6, expense7)
            );

			// Guardar usuario de nuevo para que cascade ALL persista los gastos
			userRepository.save(user);



		};
	}

}
