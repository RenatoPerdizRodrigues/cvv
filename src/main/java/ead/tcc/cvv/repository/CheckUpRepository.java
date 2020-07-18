package ead.tcc.cvv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ead.tcc.cvv.model.CheckUp;
import ead.tcc.cvv.model.Usuario;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Entity;

@Repository
public interface CheckUpRepository extends JpaRepository<CheckUp, Long>{
	@Query(value="SELECT id, score, higiene, mascara, isolamento, data_checkup, usuario_id FROM check_ups WHERE usuario_id = :UsuarioId",nativeQuery=true)
	public List<CheckUp> allByUsuario(@Param("UsuarioId") long id);
	
	@Query(value="SELECT id, score, higiene, mascara, isolamento, data_checkup, usuario_id FROM check_ups WHERE usuario_id = :UsuarioId ORDER BY data_checkup ASC LIMIT 1",nativeQuery=true)
	public Optional<CheckUp> lastlByUsuario(@Param("UsuarioId") long id);
	
	@Query(value="SELECT id, score, higiene, mascara, isolamento, data_checkup, usuario_id FROM check_ups WHERE data_checkup <= DATE_SUB(CURDATE(), INTERVAL :DataConfig DAY) GROUP BY usuario_id",nativeQuery=true)
	public List<CheckUp> getLembretes(@Param("DataConfig") String dataConfig);
}
