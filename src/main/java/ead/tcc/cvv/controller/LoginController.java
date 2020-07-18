package ead.tcc.cvv.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {
	@GetMapping(value= {"/login", "/"})
	public String create(Principal user) {
		
		if (user != null) {
			return "redirect:/checkups/create";			
		}
		
		//Se usuário não estiver logado, retornamos a página de login
		return "login/login";
	}
	
	//Caso falhe o login!
	@GetMapping(value= {"/login/erro"})
	public String erro(RedirectAttributes red, Principal user) {
		
		if (user != null) {
			return "redirect:/checkups/create";			
		}
		
		red.addFlashAttribute("error", "Credenciais não encontradas!");
		
		return "redirect:/";
		
	}
}
