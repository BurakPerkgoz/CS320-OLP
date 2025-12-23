package olp;

import olp.database.Connection;
import olp.utils.AI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		try {
			Connection.init();
		}
		catch (Exception ex) {
			System.out.println("Database connection error : ");
			ex.printStackTrace();
		}
		SpringApplication.run(Main.class, args);
	}
}