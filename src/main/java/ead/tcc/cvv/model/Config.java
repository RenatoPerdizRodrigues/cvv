package ead.tcc.cvv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="configs")
public class Config {
	//Atributos com marcadores de seus valores na tabela a ser gerada
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "tempo_lembrete", columnDefinition = "int default 15")
	private String tempo_lembrete;
	
	@Column(name = "mensagem_grave", columnDefinition = "text default 'Seus sintomas aparentam ser graves e é recomendado que você procure um médico!'")
	private String mensagem_grave;
	
	@Column(name = "mensagem_media", columnDefinition = "text default 'É recomendado que você fique de olho nos seus sintomas!'")
	private String mensagem_media;
	
	@Column(name = "mensagem_branda", columnDefinition = "text default 'Seu sintomas não são graves, mas todo cuidado é pouco!'")
	private String mensagem_branda;
	
	@Column(name = "pontuacao_grave", columnDefinition = "int default 85")
	private Long pontuacao_grave;
	
	@Column(name = "pontuacao_media", columnDefinition = "int default 50")
	private Long pontuacao_media;

	@Column(name = "pontuacao_branda", columnDefinition = "int default 30")
	private Long pontuacao_branda;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTempo_lembrete() {
		return tempo_lembrete;
	}

	public void setTempo_lembrete(String tempo_lembrete) {
		this.tempo_lembrete = tempo_lembrete;
	}

	public String getMensagem_grave() {
		return mensagem_grave;
	}

	public void setMensagem_grave(String mensagem_grave) {
		this.mensagem_grave = mensagem_grave;
	}

	public String getMensagem_media() {
		return mensagem_media;
	}

	public void setMensagem_media(String mensagem_media) {
		this.mensagem_media = mensagem_media;
	}

	public String getMensagem_branda() {
		return mensagem_branda;
	}

	public void setMensagem_branda(String mensagem_branda) {
		this.mensagem_branda = mensagem_branda;
	}

	public Long getPontuacao_grave() {
		return pontuacao_grave;
	}

	public void setPontuacao_grave(Long pontuacao_grave) {
		this.pontuacao_grave = pontuacao_grave;
	}

	public Long getPontuacao_media() {
		return pontuacao_media;
	}

	public void setPontuacao_media(Long pontuacao_media) {
		this.pontuacao_media = pontuacao_media;
	}

	public Long getPontuacao_branda() {
		return pontuacao_branda;
	}

	public void setPontuacao_branda(Long pontuacao_branda) {
		this.pontuacao_branda = pontuacao_branda;
	}
	
	
}
