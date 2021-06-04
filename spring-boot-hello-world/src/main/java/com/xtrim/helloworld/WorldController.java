package com.xtrim.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/world")
public class WorldController {

	@GetMapping("")
	String getWorld() {
		return "Welcome to Planet Earth!";
	}
	
	@GetMapping("/galaxy")
	String getGalaxy() {
		return "Welcome to Galaxy Milkyway!";
	}
}
