package ead.tcc.cvv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {
	@GetMapping(value= {"/login", "/"})
	public String create() {
		
		//Se usuário não estiver logado, retornamos a página de login
		return "login/login";
		
		//Se estiver, voltamos para a 
	}
	
	//Caso falhe o login!
	@GetMapping(value= {"/login/erro"})
	public String erro(RedirectAttributes red) {
		
		red.addFlashAttribute("error", "Credenciais não encontradas!");
		
		return "redirect:/";
		
	}
}
