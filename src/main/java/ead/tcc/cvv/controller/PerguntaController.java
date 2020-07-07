package ead.tcc.cvv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.DetalhesUsuario;
import ead.tcc.cvv.model.Pergunta;
import ead.tcc.cvv.service.PerguntaService;

@Controller
public class PerguntaController {
	@Autowired
	private PerguntaService perguntaService;

	@GetMapping("/perguntas")
	public String perguntas(Model model) {
		model.addAttribute("listaPerguntas", perguntaService.getAllPerguntas());
		
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
				
		return "perguntas/index";
	}
	
	@GetMapping("/perguntas/create")
	public String create(Model model) {
		Pergunta pergunta = new Pergunta();
		model.addAttribute("pergunta",pergunta);
		return "perguntas/create";
	}

	@PostMapping("/perguntas/store")
	public String store(@ModelAttribute("pergunta") Pergunta pergunta) {
		perguntaService.savePergunta(pergunta);
		return "redirect:/perguntas";
	}
	
	@GetMapping("perguntas/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		Pergunta pergunta = perguntaService.getPergunta(id);
		
		model.addAttribute("pergunta",pergunta);
		
		//Verificamos permissão do admin
		boolean admin = false;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DetalhesUsuario) {
			admin = ((DetalhesUsuario)principal).getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		}
		
		//Passamos id e permissões de usuário
		model.addAttribute("usuario_id", id);
		model.addAttribute("admin",admin);
		
		return "perguntas/edit";
	}
	
	@GetMapping("perguntas/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.perguntaService.deletePergunta(id);
	
		return "redirect:/perguntas";
	}
}
