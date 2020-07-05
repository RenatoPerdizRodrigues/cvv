package ead.tcc.cvv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
	@GetMapping(value= {"/", "/login"})
	public String create() {
		return "login/login";
	}
}
