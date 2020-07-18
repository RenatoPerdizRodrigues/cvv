package ead.tcc.cvv.controller;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import ead.tcc.cvv.config.EmailConfiguration;
import ead.tcc.cvv.model.DetalhesUsuario;
import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.service.UsuarioService;

@Controller
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmailConfiguration emailConfiguration;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    //Mostramos os usuários
	@GetMapping("/usuarios")
	public String viewHomePage(Model model) {
		model.addAttribute("listaUsuarios", usuarioService.getAllUsuarios());
		model.addAttribute("total_usuarios", usuarioService.getAllUsuarios().size());
		
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
	
	//Retornamos a view de usuários
	@GetMapping("/cadastrar")
	public String create(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario",usuario);
		return "usuarios/create";
	}
	
	//Página de recuperação de senha
	@GetMapping("/recuperar")
	public String recuperar() {
		return "usuarios/recuperar";
	}
	
	//Envia e-mail de recuperaçaõ
	@PostMapping("/enviaEmail")
	public String enviaEmail(final HttpServletRequest request) {

		Usuario usuario = usuarioService.findByEmail(request.getParameter("email"));
		if(usuario != null) {
			//Fazemos o token de recuperação
			String token = passwordEncoder.encode(Long.toString(System.currentTimeMillis()));
			token = token.replace("/", "");
			token = token.replace(".", "");

			usuario.setTokenSenha(token);
			usuarioService.saveUsuario(usuario);
			
			token = "http://localhost:8080/reset/" + token;
			
			//Envio de e-mail
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost(this.emailConfiguration.getHost());
			mailSender.setPort(this.emailConfiguration.getPort());
			mailSender.setUsername(this.emailConfiguration.getUsername());
			mailSender.setPassword(this.emailConfiguration.getPassword());
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			String htmlMsg = "Foi solicitada uma recuperação de senha para seu usuário no Sistema CVV. <a href=\"" + token + "\">Clique aqui</a> para resetar!";
			try {
				helper.setText(htmlMsg, true);
				helper.setTo(usuario.getEmail());
				helper.setSubject("CVV - Recuperação de Senha");
				helper.setFrom("rebsunderline@hotmail.com");
				mailSender.send(mimeMessage);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return "redirect:/";		
	}
	
	//Recuperação de senha
	@GetMapping("/reset/{token}")
	public String resetSenha(Model model, @PathVariable(value = "token") String token, RedirectAttributes red) {
		Usuario usuario = usuarioService.findByTokenSenha(token);
		
		if(usuario == null) {
			red.addFlashAttribute("warning", "Token incorreto");
			return "redirect:/";
		}
		
		model.addAttribute("usuario",usuario);
		model.addAttribute("token",token);
		return "usuarios/senha";
	}
	
	//Resetar apenas a senha
	@PostMapping("/resetsenha")
	public String resetsenha(Usuario usuario, BindingResult bindingResult, final HttpServletRequest request, RedirectAttributes red) {
		
		Usuario user = usuarioService.findByTokenSenha(request.getParameter("token"));
				
		if(user == null) {
			red.addFlashAttribute("warning", "Token incorreto");
			return "redirect:/";
		}
		
		String senha = passwordEncoder.encode(request.getParameter("senha"));
		user.setSenha(senha);
		user.setTokenSenha(null);
		
		usuarioService.saveUsuario(user);
		
		red.addFlashAttribute("success", "Senha resetada com sucesso!");
		return "redirect:/";
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
		
		//Caso seja criação, precisa ter todos os campos e todos devem ser válidos
		if (bindingResult.hasErrors() && metodo.equals("criacao")) {
			bindingResult.rejectValue("uf", "error.uf", "Por favor, verifique o endereço novamente para alterar as informações do formulário!");
			return "usuarios/create";
		}
		
		//Verificamos se o e-mail é único
		Usuario user_mail = usuarioService.findByEmail(request.getParameter("email"));
		
		if(metodo.equals("criacao") && user_mail instanceof Usuario && user_mail.getEmail().equals(request.getParameter("email"))) {
			bindingResult.rejectValue("email", "error.usuario", "Este e-mail já está cadastrado!");
			bindingResult.rejectValue("uf", "error.uf", "Por favor, verifique o endereço novamente para alterar as informações do formulário!");
			
			return "usuarios/create";
		}
		
		//Colocamos a cidade em maíuscula
		usuario.setCidade(request.getParameter("cidade").toUpperCase());
		
		
		
		//Definimos a senha usando nosso encoder BCrypt
		if(request.getParameter("senha") != "") {
			String senha = passwordEncoder.encode(request.getParameter("senha"));
			usuario.setSenha(senha);
		} else {
			Usuario usuario_original = usuarioService.getUsuario(Long.parseLong(request.getParameter("id")));
			usuario.setSenha(usuario_original.getSenha());
		}
		
		//Definimos o papel, que é sempre USUARIO para um cadastro normal
		//Mas não mexemos na permissão caso seja edição
		if(metodo.equals("criacao")) {
			usuario.setPapel("ROLE_USER");
		}
		
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
			red.addFlashAttribute("success", "Você se cadastrou com sucesso! Por favor, se logue utilizand o e-mail e a senha definidos.");
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
