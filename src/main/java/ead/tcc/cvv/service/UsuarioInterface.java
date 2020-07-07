package ead.tcc.cvv.service;

import ead.tcc.cvv.model.Usuario;
import java.util.List;

//Interface de Usuário
public interface UsuarioInterface {
	//Método de listagem de usuários
	List<Usuario> getAllUsuarios();
	
	//Método de salvar usuário
	void saveUsuario(Usuario usuario);
	
	//Método de pegar usuário por ID
	Usuario getUsuario(long id);
	
	//Método de deletar usuário
	void deleteUsuario(long id);
	
	//Pega usuário por cidade
	List<Usuario> findByCidade(String cidade);
	
	//Pega usuário por e-mail
	Usuario findByEmail(String email);
}
