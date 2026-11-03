package com.url_shortener_sb;

import com.url_shortener_sb.entity.Role;
import com.url_shortener_sb.repository.RoleRepository;
import com.url_shortener_sb.utils.Roles;
import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UrlShortenerSbApplication  {

	// Inject RoleRepository via constructor or field
//	@Autowired
//	private RoleRepository roleRepository;

	public static void main(String[] args) {

		SpringApplication.run(UrlShortenerSbApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return  new ModelMapper();
	}

//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("Initializing roles...");
//
//		// Initialize roles if they don't exist
//		if (roleRepository.findByName(Roles.ADMIN) == null) {
//			System.out.println("Admin role not found, saving...");
//			roleRepository.save(new Role(Roles.ADMIN));
//		}
//
//		if (roleRepository.findByName(Roles.USER) == null) {
//			System.out.println("User role not found, saving...");
//			roleRepository.save(new Role(Roles.USER));
//		}
//	}



}
