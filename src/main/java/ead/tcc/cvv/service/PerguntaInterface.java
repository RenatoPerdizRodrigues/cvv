package ead.tcc.cvv.service;

import java.util.List;

import ead.tcc.cvv.model.Pergunta;

//Interface de Pergunta
public interface PerguntaInterface {
	//Método de listagem de perguntas
	List<Pergunta> getAllPerguntas();
	
	//Método de salvar perguntas
	void savePergunta(Pergunta pergunta);
	
	//Método de pegar pergunta por ID
	Pergunta getPergunta(long id);
	
	//Método de deletar pergunta
	void deletePergunta(long id);
}
