package ead.tcc.cvv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ead.tcc.cvv.model.CheckUp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Entity;

@Repository
public interface CheckUpRepository extends JpaRepository<CheckUp, Long>{
	
}
