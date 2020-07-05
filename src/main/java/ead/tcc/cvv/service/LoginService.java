package ead.tcc.cvv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ead.tcc.cvv.model.LoginUser;
import ead.tcc.cvv.model.Usuario;
import ead.tcc.cvv.repository.UsuarioRepository;

public class LoginService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario user = repo.findByUsername(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("User 404");
		}
		// TODO Auto-generated method stub
		return new LoginUser(user);
	}

}
