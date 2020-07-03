package ead.tcc.cvv.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.model.Config;
import ead.tcc.cvv.service.CheckUpService;
import ead.tcc.cvv.service.ConfigService;
import ead.tcc.cvv.model.Pergunta;
import ead.tcc.cvv.service.PerguntaService;

import ead.tcc.cvv.model.Resposta;
import ead.tcc.cvv.service.RespostaService;

@Controller
public class CheckUpController {
	@Autowired
	private CheckUpService checkUpService;
	
	@Autowired
	private PerguntaService perguntaService;
	
	@Autowired
	private RespostaService respostaService;
	
	@Autowired
	private ConfigService configService;

	@GetMapping("/checkups")
	public String checkups(Model model) {
		model.addAttribute("listaCheckUps", checkUpService.getAllCheckUps());
		return "checkups/index";
	}
	
	@GetMapping("/checkups/{usuario_id}")
	public String checkups(@PathVariable (value = "usuario_id") long usuario_id, Model model) {
		model.addAttribute("listaCheckUps", checkUpService.getCheckUpUsuario(usuario_id));
		return "checkups/index";
	}
	
	@GetMapping("/checkups/create")
	public String create(Model model) {
		CheckUp checkup = new CheckUp();
		model.addAttribute("checkup",checkup);
		
		//Capturamos todas as perguntas
		model.addAttribute("listaPerguntas", perguntaService.getAllPerguntas());
		
		//Capturamos todas as respostas
		model.addAttribute("listaRespostas", respostaService.getAllRespostas());
		
		return "checkups/create";
	}

	@PostMapping("/checkups/store")
	public String store(@ModelAttribute("checkups") CheckUp checkup, final HttpServletRequest request, Model model) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		checkup.setData_checkup(dateFormat.format(date));
		
		checkup.setUsuario_id(1);
		
		long soma = 0;
		for(long i = 0; i < perguntaService.countPerguntas(); i++) {
			soma = soma + Long.parseLong(request.getParameter(Long.toString(i)));
		}

		checkup.setScore(soma);
		
		checkUpService.saveCheckUp(checkup);
		
		return "redirect:/checkups/resultado/" + soma;
	}
	
	@GetMapping("checkups/resultado/{soma}")
	public String resultado(@PathVariable(value = "soma") long soma, Model model) {
		//Analisamos o score final e retornamos a mensagem adequada
		Config config = configService.getConfig(1);
		
		long scoreGrave = config.getPontuacao_grave();
		long scoreMedio = config.getPontuacao_media();
		long scoreBrando = config.getPontuacao_branda();
		
		if(soma < scoreBrando) {
			model.addAttribute("mensagem", config.getMensagem_branda());
		} else if(soma >= scoreBrando && soma < scoreGrave) {
			model.addAttribute("mensagem", config.getMensagem_media());
		} else if(soma >= scoreGrave) {
			model.addAttribute("mensagem", config.getMensagem_grave());
		} else {
			model.addAttribute("mensagem", "Score não computado!");
		}
		
		model.addAttribute("score", soma);
		
		return "checkups/resultado";
	}
	
	@GetMapping("checkups/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.checkUpService.deleteCheckUp(id);
	
		return "redirect:/checkups";
	}
}
