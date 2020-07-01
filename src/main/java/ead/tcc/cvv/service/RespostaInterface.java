package ead.tcc.cvv.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import ead.tcc.cvv.model.Resposta;

@Service
public interface RespostaInterface {
	//Método de listagem de respostas
	List<Resposta> getAllRespostas();
	
	//Método de salvar respostas
	void saveResposta(Resposta resposta);
	
	//Método de pegar resposta por ID
	Resposta getResposta(long id);
	
	//Método de deletar resposta
	void deleteResposta(long id);
	
	//Método de pegar resposta por ID de pergunta
	List<Resposta> allByPergunta(long id);
	
}