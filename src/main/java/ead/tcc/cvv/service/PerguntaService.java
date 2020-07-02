package ead.tcc.cvv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ead.tcc.cvv.model.Pergunta;
import ead.tcc.cvv.repository.PerguntaRepository;

@Service
public class PerguntaService implements PerguntaInterface{
	
	//Conectamos ao repositório de pergunta
	@Autowired
	private PerguntaRepository perguntaRepository;

	@Override
	public List<Pergunta> getAllPerguntas() {
		return this.perguntaRepository.findAll();
	}

	@Override
	public void savePergunta(Pergunta pergunta) {
		this.perguntaRepository.save(pergunta);
		
	}

	@Override
	public Pergunta getPergunta(long id) {
		Optional<Pergunta> optional = perguntaRepository.findById(id);
		Pergunta pergunta = null;
		if(optional.isPresent()) {
			pergunta = optional.get();
		} else {
			throw new RuntimeException("Pergunta não encontrada!");
		}
		
		return pergunta;
	}

	@Override
	public void deletePergunta(long id) {
		this.perguntaRepository.deleteById(id);
	}
	
	@Override
	public long countPerguntas() {
		return this.perguntaRepository.count();
	}

}
