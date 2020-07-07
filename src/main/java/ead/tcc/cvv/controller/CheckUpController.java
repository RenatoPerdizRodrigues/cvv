package ead.tcc.cvv.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.model.Config;
import ead.tcc.cvv.model.DetalhesUsuario;
import ead.tcc.cvv.service.CheckUpService;
import ead.tcc.cvv.service.ConfigService;
import ead.tcc.cvv.model.Pergunta;
import ead.tcc.cvv.service.PerguntaService;

import ead.tcc.cvv.model.Resposta;
import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.service.RespostaService;
import ead.tcc.cvv.service.UsuarioService;

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
	
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/checkups")
	public String checkups(Model model) {
		model.addAttribute("listaCheckUps", checkUpService.getAllCheckUps());
		
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
				
		return "checkups/index";
	}
	
	@GetMapping("/checkups/{usuario_id}")
	public String checkups(@PathVariable (value = "usuario_id") long usuario_id, Model model) {
		List<CheckUp> checkUpList = checkUpService.getCheckUpUsuario(usuario_id);
		model.addAttribute("listaCheckUps", checkUpList);
		
		//Loopamos a lista de check-ups para arrumar as datas
		for (int i = 0; i < checkUpList.size(); i++) {
			String data = checkUpList.get(i).getData_checkup();
			String split[] = data.split("-");
			
			checkUpList.get(i).setData_checkup(split[2] + "/" + split[1] + "/" + split[0]);
			
		}
		
		model.addAttribute("usuario", usuarioService.getUsuario(usuario_id));
		
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
				
		return "checkups/index";
	}
	
	@GetMapping("/checkups/mapa/{cidade}")
	public String mapa(@PathVariable (value = "cidade") String cidade, Model model) {
		
		//Listamos os usuários de SP
		List<Usuario> usuariosList = usuarioService.findByCidade("SAO PAULO");
		model.addAttribute("listaUsuarios", usuariosList);
		
		//Loopamos os usuários para pegar o último check up deste usuário
		for (int i = 0; i < usuariosList.size(); i++) {
			long id= usuariosList.get(i).getId();
			CheckUp last = checkUpService.getLastCheckUpUsuario(id);
		}
		
		model.addAttribute("latitudeCentral", "-23.54");
		model.addAttribute("longitudeCentral", "-46.63");
		model.addAttribute("cidade", "SAO PAULO");
				
		return "checkups/mapa";
	}
	
	@PostMapping("/checkups/mapacidade")
	public String mapaCidade(final HttpServletRequest request, Model model) {
		
		String[] infos = request.getParameter("cidade").split("&");
		
		model.addAttribute("latitudeCentral", infos[0]);
		model.addAttribute("longitudeCentral", infos[1]);
		model.addAttribute("cidade", infos[2]);
		
		model.addAttribute("listaUsuarios", usuarioService.findByCidade(infos[2]));
				
		return "checkups/mapa";
	}
	
	@GetMapping("/checkups/create")
	public String create(Model model) {
		//Montamos objeto de CheckUp
		CheckUp checkup = new CheckUp();
		model.addAttribute("checkup",checkup);
		
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
		
		long id;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DetalhesUsuario) {
			id= ((DetalhesUsuario)principal).getUserId();
		} else {
			id = 1;
		}
		
		checkup.setUsuario_id(id);
		
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
