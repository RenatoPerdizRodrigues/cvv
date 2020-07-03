package ead.tcc.cvv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ead.tcc.cvv.model.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long>{

}
