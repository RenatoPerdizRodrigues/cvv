package ead.tcc.cvv.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Entity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ead.tcc.cvv.model.Pergunta;
import ead.tcc.cvv.model.Resposta;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long>{
	@Query(value="SELECT id, texto, score, pergunta_id FROM respostas WHERE pergunta_id = :PerguntaId",nativeQuery=true)
	public List<Resposta> allByPergunta(@Param("PerguntaId") long id);
}
