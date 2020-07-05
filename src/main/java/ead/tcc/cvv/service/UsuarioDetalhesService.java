package ead.tcc.cvv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ead.tcc.cvv.model.DetalhesUsuario;
import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.repository.UsuarioRepository;

@Service
public class UsuarioDetalhesService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> user = usuarioRepository.findByEmail(username);
		
		user.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
		
		return user.map(DetalhesUsuario::new).get();
	}

}
