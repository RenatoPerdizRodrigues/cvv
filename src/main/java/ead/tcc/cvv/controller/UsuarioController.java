package ead.tcc.cvv.controller;

import javax.servlet.ServletException;
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
	@GetMapping("/usuarios")
	public String viewHomePage(Model model) {
		model.addAttribute("listaUsuarios", usuarioService.getAllUsuarios());
		
		String username;

		//Verificamos permissão do admin
		boolean admin = false;
		long id;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DetalhesUsuario) {
		  username = ((DetalhesUsuario)principal).getUsername();
		  id= ((DetalhesUsuario)principal).getUserId();
		  admin = ((DetalhesUsuario)principal).getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		} else {
		  username = principal.toString();
			id = 1;
		}
		
		//Passamos id e permissões de usuário
		model.addAttribute("usuario_id", id);
		model.addAttribute("admin",admin);
		model.addAttribute("username", username);


		return "usuarios/index";
	}
	
	@GetMapping("/cadastrar")
	public String create(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario",usuario);
		return "usuarios/create";
	}

	@PostMapping("/store")
	public String store(@ModelAttribute("usuario") Usuario usuario, final HttpServletRequest request) {
		
		//Definimos a senha usando nosso encoder BCrypt
		if(request.getParameter("senha") != "") {
			String senha = passwordEncoder.encode(request.getParameter("senha"));
			usuario.setSenha(senha);
		} else {
			Usuario usuario_original = usuarioService.getUsuario(Long.parseLong(request.getParameter("id")));
			usuario.setSenha(usuario_original.getSenha());
		}
		
		//Definimos o papel, que é sempre USUARIO para um cadastro normal
		usuario.setPapel("ROLE_USER");
		
		usuarioService.saveUsuario(usuario);
		
		try {
			request.login(request.getParameter("email"), request.getParameter("senha"));
	    } catch (ServletException e) {
	        //erro
	    }
		
		//Verificamos se é cadastro ou edição
		String metodo = request.getParameter("metodo");
		
		if(metodo.equals("edicao")) {
			String id = request.getParameter("id");
			return "redirect:/edit/" + id;
		} else {
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		Usuario usuario = usuarioService.getUsuario(id);
		
		model.addAttribute("usuario",usuario);
		model.addAttribute("id",id);
		
		//Verificamos permissão do admin
		boolean admin = false;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DetalhesUsuario) {
			id= ((DetalhesUsuario)principal).getUserId();
			admin = ((DetalhesUsuario)principal).getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		}
		
		//Passamos id e permissões de usuário
		model.addAttribute("usuario_id", id);
		model.addAttribute("admin",admin);
		
		return "usuarios/edit";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.usuarioService.deleteUsuario(id);
		
		return "redirect:/";
	}
}
