package ead.tcc.cvv.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.model.Config;
import ead.tcc.cvv.repository.ConfigRepository;

@Service
public class ConfigService implements ConfigInterface{
	//Conectamos ao repositório de config
	@Autowired
	private ConfigRepository configRepository;
	
	@Override
	public void saveConfig(Config config) {
		this.configRepository.save(config);
	}
	
	@Override
	public Config getConfig(long id) {
		Optional<Config> optional = configRepository.findById(id);
		Config config = null;
		if(optional.isPresent()) {
			config = optional.get();
		} else {
			throw new RuntimeException("Sistema precisa ser configurado!");
		}
		
		return config;
	}
}
