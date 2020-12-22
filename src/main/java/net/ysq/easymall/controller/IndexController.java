package net.ysq.easymall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author	passerbyYSQ
 * @date	2020-11-30 16:34:37
 */
@Controller
public class IndexController {
	
	@GetMapping("/")
	public String index() {
		return "redirect:/product/list"; 
	}
	
}
