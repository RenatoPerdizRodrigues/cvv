package ead.tcc.cvv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import ead.tcc.cvv.model.Resposta;
import ead.tcc.cvv.repository.RespostaRepository;

public class RespostaService implements RespostaInterface{
	//Conectamos ao repositório de resposta
	@Autowired
	private RespostaRepository respostaRepository;

	@Override
	public List<Resposta> getAllRespostas() {
		return this.respostaRepository.findAll();
	}

	@Override
	public void saveResposta(Resposta resposta) {
		this.respostaRepository.save(resposta);
		
	}

	@Override
	public Resposta getResposta(long id) {
		Optional<Resposta> optional = respostaRepository.findById(id);
		Resposta resposta = null;
		if(optional.isPresent()) {
			resposta = optional.get();
		} else {
			throw new RuntimeException("Resposta não encontrada!");
		}
		
		return resposta;
	}

	@Override
	public void deleteResposta(long id) {
		this.respostaRepository.deleteById(id);
	}
}
