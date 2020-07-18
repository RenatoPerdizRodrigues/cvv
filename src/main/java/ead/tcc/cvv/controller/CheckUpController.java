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
		List<CheckUp> checkUpList = checkUpService.getAllCheckUps();
		
		model.addAttribute("listaCheckUps", checkUpList);
		model.addAttribute("total_checkups", checkUpList.size());
		
		int total_checkups = 0;
		
		//Loopamos a lista de check-ups para arrumar as datas
		for (int i = 0; i < checkUpList.size(); i++) {
			String data = checkUpList.get(i).getData_checkup();
			String split[] = data.split("-");
			
			total_checkups++;
			
			checkUpList.get(i).setData_checkup(split[2] + "/" + split[1] + "/" + split[0]);
			
		}
		
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
		
		int total_checkups = 0;
		
		//Loopamos a lista de check-ups para arrumar as datas
		for (int i = 0; i < checkUpList.size(); i++) {
			String data = checkUpList.get(i).getData_checkup();
			String split[] = data.split("-");
			
			total_checkups++;
			
			checkUpList.get(i).setData_checkup(split[2] + "/" + split[1] + "/" + split[0]);
			
		}
		
		model.addAttribute("total_checkups", total_checkups);
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
	
	@GetMapping("/checkups/mapa")
	public String mapa(Model model) {
		
		//Listamos os usuários de SP
		List<Usuario> usuariosList = usuarioService.findByCidade("SÃO PAULO");
		model.addAttribute("listaUsuarios", usuariosList);
		
		model.addAttribute("latitudeCentral", "-23.54");
		model.addAttribute("longitudeCentral", "-46.63");
		model.addAttribute("cidade", "SÃO PAULO");
				
		List<Usuario> listaUser = usuarioService.findByCidade("SÃO PAULO");
		
		//Pegamos o score do último checkup de cada usuário!
		for(int i = 0; i < listaUser.size(); i++) {
			Usuario loop = listaUser.get(i);
			long score = checkUpService.getLastCheckUpUsuario(loop.getId());
			listaUser.get(i).setScore(score);
		}
		
		model.addAttribute("listaUsuarios", listaUser);
		
		//Pegamos quais os valores são considerados brandos, médios e graves
		Config config = configService.getConfig(1);
		model.addAttribute("pontuacao_branda", config.getPontuacao_branda());
		model.addAttribute("pontuacao_media", config.getPontuacao_media());
		model.addAttribute("pontuacao_grave", config.getPontuacao_grave());
		
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
						
		return "checkups/mapa";
	}
	
	@PostMapping("/checkups/mapacidade")
	public String mapaCidade(final HttpServletRequest request, Model model) {
		
		String[] infos = request.getParameter("cidade").split("&");
		
		model.addAttribute("latitudeCentral", infos[0]);
		model.addAttribute("longitudeCentral", infos[1]);
		model.addAttribute("cidade", infos[2]);
		
		List<Usuario> listaUser = usuarioService.findByCidade(infos[2]);
		
		//Pegamos o score do último checkup de cada usuário!
		for(int i = 0; i < listaUser.size(); i++) {
			Usuario loop = listaUser.get(i);
			long score = checkUpService.getLastCheckUpUsuario(loop.getId());
			listaUser.get(i).setScore(score);
		}
		
		model.addAttribute("listaUsuarios", listaUser);
		
		//Pegamos quais os valores são considerados brandos, médios e graves
		Config config = configService.getConfig(1);
		model.addAttribute("pontuacao_branda", config.getPontuacao_branda());
		model.addAttribute("pontuacao_media", config.getPontuacao_media());
		model.addAttribute("pontuacao_grave", config.getPontuacao_grave());
		
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
	
	@GetMapping("/checkups/informacoes")
	public String informacoes(Model model) {
		
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
		
		return "checkups/informacoes";
	}

	@PostMapping("/checkups/store")
	public String store(@ModelAttribute("checkups") CheckUp checkup, final HttpServletRequest request, Model model) {
		
		//Formatamos a data
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		checkup.setData_checkup(dateFormat.format(date));
		
		long id;
		
		//Pergamos o usuário logado para salvar seu ID
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DetalhesUsuario) {
			id= ((DetalhesUsuario)principal).getUserId();
		} else {
			id = 1;
		}
		
		//Definimos o ID do usuário		
		checkup.setUsuario_id(id);
		
		//Rodamos as respostas do questionário para somar seu resultado final
		long soma = 0;
		for(long i = 0; i < perguntaService.countPerguntas(); i++) {
			soma = soma + Long.parseLong(request.getParameter(Long.toString(i)));
		}

		//Declarmaos sua soma
		checkup.setScore(soma);
		
		//Savamos o objeto Check Up
		checkUpService.saveCheckUp(checkup);
		
		return "redirect:/resultado/" + soma;
	}
	
	@GetMapping("resultado/{soma}")
	public String resultado(@PathVariable(value = "soma") long soma, Model model) {
		//Analisamos o score final e retornamos a mensagem adequada
		Config config = configService.getConfig(1);
		
		long scoreGrave = config.getPontuacao_grave();
		long scoreMedio = config.getPontuacao_media();
		long scoreBrando = config.getPontuacao_branda();
				
		if(soma <= scoreBrando && soma > (long) 0) {
			model.addAttribute("mensagem", config.getMensagem_branda());
		} else if(soma == 0) {
			model.addAttribute("mensagem", "Parabéns, você não apresenta sintomas! Não deixe de se prevenir!");
		} else if(soma >= scoreBrando && soma < scoreGrave) {
			model.addAttribute("mensagem", config.getMensagem_media());
		} else if(soma >= scoreGrave) {
			model.addAttribute("mensagem", config.getMensagem_grave());
		} else {
			model.addAttribute("mensagem", "Score não computado!");
		}
		
		model.addAttribute("score", soma);
		
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
		
		return "checkups/resultado";
	}
	
	@GetMapping("checkups/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.checkUpService.deleteCheckUp(id);
	
		return "redirect:/checkups";
	}
}
