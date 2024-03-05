package com.swtp4.backend;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.swtp4.backend.services.ApplicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {


//	public static void main(String[] args) {
//		SpringApplication.run(BackendApplication.class, args);
//	}

	public static void main(String[] args) {
		var ctx = SpringApplication.run(BackendApplication.class, args);
		ApplicationService applicationService = ctx.getBean(ApplicationService.class);
		System.out.println(applicationService.getApplicationsByStatus("open"));
	}

}
