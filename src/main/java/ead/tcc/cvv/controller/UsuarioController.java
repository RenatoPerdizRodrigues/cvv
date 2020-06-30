package ead.tcc.cvv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.service.UsuarioService;

@Controller
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;

    //Display a list of employees
	@GetMapping("/")
	public String viewHomePage(Model model) {
		model.addAttribute("listEmployees", usuarioService.getAllUsuarios());
		return "index";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("usuario",usuario);
		return "usuarios/create";
	}

	@PostMapping("/store")
	public String store(@ModelAttribute("usuario") Usuario usuario) {
		usuarioService.saveUsuario(usuario);
		return "redirect:/";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(value = "id") long id, Model model) {
		Usuario usuario = usuarioService.getUsuario(id);
		
		model.addAttribute("usuario",usuario);
		
		return "edit";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable (value = "id") long id) {
		this.usuarioService.deleteUsuario(id);;
		
		return "redirect:/";
	}
}
