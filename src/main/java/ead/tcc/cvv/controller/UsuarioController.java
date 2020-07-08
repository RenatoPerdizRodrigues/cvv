package ead.tcc.cvv.controller;

import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String store(@Valid Usuario usuario, BindingResult bindingResult, final HttpServletRequest request, RedirectAttributes red) {

		//Verificamos se é cadastro ou edição
		String metodo = request.getParameter("metodo");
		
		//Caso seja edição, validamos mas deixamos passar a senha vazia e e-mail repetido
		if(metodo.equals("edicao") && bindingResult.hasErrors()) {
			
			System.out.println(bindingResult.getErrorCount());
			
			if(bindingResult.getErrorCount() == 1 && bindingResult.getFieldError("senha") != null) {
				//Tem erro apenas na senha
			} else {
				// Tem outros erros!
				return "usuarios/edit";
			}
		}
		
		//Caso seja criação, precisa ter todos os campos e e-mail único
		if (bindingResult.hasErrors() && metodo.equals("criacao")) {
			return "usuarios/create";
		}
		
		Usuario user_mail = usuarioService.findByEmail(request.getParameter("email"));
		
		if(metodo.equals("criacao") && user_mail instanceof Usuario && user_mail.getEmail().equals(request.getParameter("email"))) {
			bindingResult.rejectValue("email", "error.usuario", "Este e-mail já está cadastrado!");
			return "usuarios/create";
		}
		
		//Colocamos a cidade em maíuscula
		usuario.setCidade(request.getParameter("cidade").toUpperCase().replaceAll("[^a-zA-Z0-9]", ""));
		
		
		
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
		
		
		if(metodo.equals("edicao")) {
			red.addFlashAttribute("success", "Usuário editado com sucesso!");
			String id = request.getParameter("id");
			return "redirect:/edit/" + id;
		} else {
			red.addFlashAttribute("success", "Usuário cadastrado com sucesso!");
			return "redirect:/login";
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
