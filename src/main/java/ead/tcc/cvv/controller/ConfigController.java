package ead.tcc.cvv.controller;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.config.EmailConfiguration;
import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.model.Config;
import ead.tcc.cvv.model.DetalhesUsuario;
import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.repository.CheckUpRepository;
import ead.tcc.cvv.service.CheckUpService;
import ead.tcc.cvv.service.ConfigService;
import ead.tcc.cvv.service.UsuarioService;

@Controller
public class ConfigController {
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmailConfiguration emailConfiguration;
	
	@GetMapping("/configs")
	public String edit(Model model) {
		Config config = configService.getConfig(1);
		
		model.addAttribute("config",config);
		
		//Verificamos permissão do admin
		boolean admin = false;
		long id;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DetalhesUsuario) {
			id= ((DetalhesUsuario)principal).getUserId();
			admin = ((DetalhesUsuario)principal).getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		} else {
			id = 1;
		}
		
		//Passamos id e permissões de usuário
		model.addAttribute("usuario_id", id);
		model.addAttribute("admin",admin);
		
		return "configs/create";
	}
	
	@PostMapping("/configs/store")
	public String store(@ModelAttribute("config") Config config) {
		configService.saveConfig(config);
		return "redirect:/configs";
	}
}
