package ead.tcc.cvv.service;

import java.util.List;

import ead.tcc.cvv.model.CheckUp;

//Interface de CheckUp
public interface CheckUpInterface {
	//Método de listagem de checkups
	List<CheckUp> getAllCheckUps();
	
	//Método de salvar checkups
	void saveCheckUp(CheckUp checkup);
	
	//Método de pegar checkup por ID
	CheckUp getCheckUp(long id);
	
	//Método de deletar checkup
	void deleteCheckUp(long id);
	
	//Pegar check-up por usuário
	List<CheckUp> getCheckUpUsuario(long id);
	
	//Pegar check-up por usuário, mas só o último
	long getLastCheckUpUsuario(long id);
	
	
}
