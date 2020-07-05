package ead.tcc.cvv.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.DetalhesUsuario;
import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.service.UsuarioService;

@Controller
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    //Display a list of employees
	@GetMapping("/")
	public String viewHomePage(Model model) {
		model.addAttribute("listaUsuarios", usuarioService.getAllUsuarios());
		
		String username;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DetalhesUsuario) {
		  username = ((DetalhesUsuario)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		System.out.println("User " + ((DetalhesUsuario)principal).getAuthorities());
		
		model.addAttribute("username", username);


		return "usuarios/index";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario",usuario);
		return "usuarios/create";
	}

	@PostMapping("/store")
	public String store(@ModelAttribute("usuario") Usuario usuario, final HttpServletRequest request) {
		
		//Definimos a senha usando nosso encoder BCrypt
		String senha = passwordEncoder.encode(request.getParameter("senha"));
		usuario.setSenha(senha);
		
		//Definimos o papel, que é sempre USUARIO para um cadastro normal
		usuario.setPapel("USUARIO,");
		
		usuarioService.saveUsuario(usuario);
		return "redirect:/";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		Usuario usuario = usuarioService.getUsuario(id);
		
		model.addAttribute("usuario",usuario);
		
		return "usuarios/edit";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.usuarioService.deleteUsuario(id);
		
		return "redirect:/";
	}
}
