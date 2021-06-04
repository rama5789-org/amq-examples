package com.xtrim.helloworld;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@RequestMapping("/hello")
	public String sayHello() {
		String response = "Hello, Application is running on JBOSS EAP 7.2 !!!";
		System.out.println("LOG:: " + response);
		return response;
	}
	
	@RequestMapping("/ping")
	public String sayPong() {
		return "pong";
	}

}
