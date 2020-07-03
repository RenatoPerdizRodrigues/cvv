package ead.tcc.cvv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.Config;
import ead.tcc.cvv.service.ConfigService;

@Controller
public class ConfigController {
	@Autowired
	private ConfigService configService;
	
	@GetMapping("/configs")
	public String edit(Model model) {
		Config config = configService.getConfig(1);
		
		model.addAttribute("config",config);
		
		return "configs/create";
	}
	
	@PostMapping("/configs/store")
	public String store(@ModelAttribute("config") Config config) {
		configService.saveConfig(config);
		return "redirect:/configs";
	}
}
