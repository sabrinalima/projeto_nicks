package com.yank.nicks.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@RequestMapping("/welcome")
	public String welcomepage() {
		WebsiteCrawler websiteCrawl = new WebsiteCrawler();
		
		websiteCrawl.obtendoNomeCpfNicks();	
		return "OK";
	}
}
