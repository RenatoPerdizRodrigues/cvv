package ead.tcc.cvv.service;

import java.util.List;

import ead.tcc.cvv.model.Config;

public interface ConfigInterface {
	//Método de salvar checkups
	void saveConfig(Config checkup);
	
	//Método de pegar checkup por ID
	Config getConfig(long id);
}
