package ead.tcc.cvv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="check_ups")
public class CheckUp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "usuario_id")
	private long usuario_id;

	@Column(name = "score")
	private long score;
	
	@Column(name = "data_checkup")
	private String data_checkup;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(long usuario_id) {
		this.usuario_id = usuario_id;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getData_checkup() {
		return data_checkup;
	}

	public void setData_checkup(String data_checkup) {
		this.data_checkup = data_checkup;
	}
	
	
}
