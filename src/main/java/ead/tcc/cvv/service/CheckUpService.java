package ead.tcc.cvv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.model.Resposta;
import ead.tcc.cvv.repository.CheckUpRepository;

@Service
public class CheckUpService implements CheckUpInterface{
	//Conectamos ao repositório de checkup
	@Autowired
	private CheckUpRepository checkupRepository;

	@Override
	public List<CheckUp> getAllCheckUps() {
		return this.checkupRepository.findAll();
	}

	@Override
	public void saveCheckUp(CheckUp checkup) {
		this.checkupRepository.save(checkup);
	}

	@Override
	public CheckUp getCheckUp(long id) {
		Optional<CheckUp> optional = checkupRepository.findById(id);
		CheckUp checkup = null;
		if(optional.isPresent()) {
			checkup = optional.get();
		} else {
			throw new RuntimeException("Check-Up não encontrado!");
		}
		
		return checkup;
	}
	
	@Override
	public List<CheckUp> getCheckUpUsuario(long id){
		return this.checkupRepository.allByUsuario(id);
	}
	
	@Override
	public CheckUp getLastCheckUpUsuario(long id){
		return this.checkupRepository.lastlByUsuario(id);
	}

	@Override
	public void deleteCheckUp(long id) {
		this.checkupRepository.deleteById(id);
	}
}
