package ead.tcc.cvv.service;

import java.util.List;

import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.model.Pergunta;

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
}
