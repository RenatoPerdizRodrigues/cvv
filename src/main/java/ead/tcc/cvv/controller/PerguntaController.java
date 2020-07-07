package ead.tcc.cvv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ead.tcc.cvv.model.DetalhesUsuario;
import ead.tcc.cvv.model.Pergunta;
import ead.tcc.cvv.model.Resposta;
import ead.tcc.cvv.service.PerguntaService;
import ead.tcc.cvv.service.RespostaService;

@Controller
public class PerguntaController {
	@Autowired
	private PerguntaService perguntaService;
	
	@Autowired
	private RespostaService respostaService;

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
	public String store(@ModelAttribute("pergunta") Pergunta pergunta, RedirectAttributes red) {
		perguntaService.savePergunta(pergunta);
		
		red.addFlashAttribute("success", "Usuário editado com sucesso!");
		
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
	public String delete(@PathVariable (value = "id") long id, RedirectAttributes red) {
		

		List<Resposta> respostas = this.respostaService.allByPergunta(id);
		if(respostas.size() > 0) {
			red.addFlashAttribute("error", "Esta pergunta possui respostas atreladas! As delete primeiro.");
			return "redirect:/perguntas";
		}
		
		this.perguntaService.deletePergunta(id);
	
		return "redirect:/perguntas";
	}
}
