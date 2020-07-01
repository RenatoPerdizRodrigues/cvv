package ead.tcc.cvv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.Pergunta;
import ead.tcc.cvv.service.PerguntaService;

@Controller
public class PerguntaController {
	@Autowired
	private PerguntaService perguntaService;

	@GetMapping("/perguntas")
	public String perguntas(Model model) {
		model.addAttribute("listaPerguntas", perguntaService.getAllPerguntas());
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
		return "redirect:/teste";
	}
	
	@GetMapping("perguntas/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		Pergunta pergunta = perguntaService.getPergunta(id);
		
		model.addAttribute("pergunta",pergunta);
		
		return "perguntas/edit";
	}
	
	@GetMapping("perguntas/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.perguntaService.deletePergunta(id);
	
		return "redirect:/perguntas";
	}
}
