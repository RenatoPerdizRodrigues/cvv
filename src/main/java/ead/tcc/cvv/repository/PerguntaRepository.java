package ead.tcc.cvv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ead.tcc.cvv.model.Pergunta;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long>{

}
