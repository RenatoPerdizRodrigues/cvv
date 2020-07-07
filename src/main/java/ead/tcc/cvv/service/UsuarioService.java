package ead.tcc.cvv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.repository.UsuarioRepository;

@Service
public class UsuarioService implements UsuarioInterface {
	
	//Conectamos ao repositório de usuário
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Implementamos as funções da inteface para realizarmos seu CRUD
	public List<Usuario> getAllUsuarios(){
		return this.usuarioRepository.findAll();
	}
	
	public void saveUsuario(Usuario usuario) {
		this.usuarioRepository.save(usuario);
	}
	
	public Usuario getUsuario(long id) {
		Optional<Usuario> optional = usuarioRepository.findById(id);
		Usuario usuario = null;
		if(optional.isPresent()) {
			usuario= optional.get();
		} else {
			throw new RuntimeException("Usuário não encontrado!");
		}
		
		return usuario;
	}
	
	public void deleteUsuario(long id) {
		this.usuarioRepository.deleteById(id);
	}

	public List<Usuario> findByCidade(String cidade) {
		return this.usuarioRepository.findByCidade(cidade);
	}
	
	public Usuario findByEmail(String email) {
		Optional<Usuario> optional = usuarioRepository.findByEmail(email);
		Usuario usuario = null;
		if(optional.isPresent()) {
			usuario= optional.get();
		} else {
			return null;
		}
		
		return usuario;
	}
}
