package com.realtech.AptechBank;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title="The SoftCodeMath Academy Bank App",
				description = "Backend Rest APIs for Aptech Bank",
				version = "v1.0",
				contact= @Contact(
						name= "Ridwan Ilelaboye",
						email = "ilelaboyeridwan@gmail.com"

				)

				

		)
)
public class AptechBankApplication {
	public static void main(String[] args) {
		SpringApplication.run(AptechBankApplication.class, args);
	}
}