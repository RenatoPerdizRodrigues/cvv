package ead.tcc.cvv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.service.CheckUpService;

@Controller
public class CheckUpController {
	@Autowired
	private CheckUpService checkUpService;

	@GetMapping("/checkups")
	public String checkups(Model model) {
		model.addAttribute("listaCheckUps", checkUpService.getAllCheckUps());
		return "checkups/index";
	}
	
	@GetMapping("/checkups/create")
	public String create(Model model) {
		CheckUp checkup = new CheckUp();
		model.addAttribute("checkup",checkup);
		return "checkups/create";
	}

	@PostMapping("/checkups/store")
	public String store(@ModelAttribute("checkups") CheckUp checkup) {
		checkUpService.saveCheckUp(checkup);
		return "redirect:/checkups";
	}
	
	@GetMapping("checkups/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.checkUpService.deleteCheckUp(id);
	
		return "redirect:/checkups";
	}
}
