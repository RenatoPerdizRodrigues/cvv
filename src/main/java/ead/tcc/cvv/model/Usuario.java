package ead.tcc.cvv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="usuarios")
public class Usuario {
	//Atributos com marcadores de seus valores na tabela a ser gerada
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "nome")
	@NotNull(message="Nome não deve ser nulo!")
	@Size(min=3,max=55,message="Nome deve ter entre 3 e 55 caracteres!")
	private String nome;

	@Column(name = "email")
	@NotNull(message="E-mail não deve ser nulo!")
	@Size(min=3,max=55,message="E-mail deve ter entre 3 e 55 caracteres!")
	@Email(message="E-mail deve ser válido!")
	private String email;
	
	@Column(name = "senha", unique = true)
	@NotNull(message="Senha deve ser preenchida!")
	@NotBlank(message="Senha deve ser preenchida!")
	private String senha;
	
	@Column(name = "cpf")
	@NotNull(message="CPF deve ser preenchido!")
	@NotBlank(message="CPF deve ser preenchido!")
	@Size(min=11,max=11,message="CPF deve ter 11 caracteres!")
	private String cpf;
	
	@Column(name = "cidade")
	@NotNull
	@NotBlank
	@Size(min=3,max=55)
	private String cidade;
	
	@Column(name = "uf")
	@NotNull
	@NotBlank
	@Size(min=2,max=55)
	private String uf;
	
	@Column(name = "endereco")
	@NotNull
	@NotBlank
	@Size(min=3,max=255)
	private String endereco;
	
	@Column(name = "latitude")
	@NotNull
	@NotBlank
	@Size(min=3,max=55)
	private String latitude;
	
	@Column(name = "longitude")
	@NotNull
	@NotBlank
	@Size(min=3,max=55)
	private String longitude;
	
	@Column(name = "papel")
	private String papel;
	
	@Column(name = "recebe_lembrete")
	private String recebe_lembrete;
	

	@Column(name = "token_senha")
	private String token_senha;
	
	//Coluna para guardar o score
	private long score = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}
	
	public long getScore() {
		return this.score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getToken_senha() {
		return token_senha;
	}

	public void setToken_senha(String token_senha) {
		this.token_senha = token_senha;
	}

	public String getRecebe_lembrete() {
		return recebe_lembrete;
	}

	public void setRecebe_lembrete(String recebe_lembrete) {
		this.recebe_lembrete = recebe_lembrete;
	}
	
}
