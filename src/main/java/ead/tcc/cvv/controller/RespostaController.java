package ead.tcc.cvv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.Resposta;
import ead.tcc.cvv.service.RespostaService;

@Controller
public class RespostaController {
	@Autowired
	private RespostaService respostaService;

	@GetMapping("/respostas/{id}")
	public String respostas(@PathVariable(value = "id") long id, Model model) {
		model.addAttribute("listaRespostas", respostaService.allByPergunta(id));
		model.addAttribute("cod_pergunta", id);
		
		Resposta resposta = new Resposta();
		model.addAttribute("resposta", resposta);
		
		return "respostas/index";
	}
	
	@PostMapping("/respostas/store")
	public String store(@ModelAttribute("resposta") Resposta resposta) {
		respostaService.saveResposta(resposta);
		return "redirect:/respostas/" + resposta.getPergunta_id();
	}
	
	@GetMapping("respostas/delete/{id}/{pergunta_id}")
	public String delete(@PathVariable (value = "id") long id, @PathVariable (value = "pergunta_id") long pergunta_id) {
		this.respostaService.deleteResposta(id);
	
		return "redirect:/respostas/" + pergunta_id;
	}
}
