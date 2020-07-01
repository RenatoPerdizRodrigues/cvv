package ead.tcc.cvv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ead.tcc.cvv.model.Resposta;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long>{

}
