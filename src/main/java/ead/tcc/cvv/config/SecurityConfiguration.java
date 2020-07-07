package ead.tcc.cvv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ead.tcc.cvv.service.UsuarioDetalhesService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	UsuarioDetalhesService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			//Rotas de admin
			.antMatchers("/error").permitAll()
			.antMatchers("/usuarios").hasRole("ADMIN")
			.antMatchers("/delete/{id}").hasRole("ADMIN")
			.antMatchers("/checkups").hasRole("ADMIN")
			.antMatchers("/checkups/delete/{id}").hasRole("ADMIN")
			.antMatchers("/configs").hasRole("ADMIN")
			.antMatchers("/config/store").hasRole("ADMIN")
			.antMatchers("/perguntas").hasRole("ADMIN")
			.antMatchers("/perguntas/create").hasRole("ADMIN")
			.antMatchers("/perguntas/store").hasRole("ADMIN")
			.antMatchers("/perguntas/edit/{id}").hasRole("ADMIN")
			.antMatchers("/perguntas/delete/{id}").hasRole("ADMIN")
			.antMatchers("/respostas/{id}").hasRole("ADMIN")
			.antMatchers("/respostas/store").hasRole("ADMIN")
			.antMatchers("/respostas/delete/{id}/{pergunta_id}").hasRole("ADMIN")
			
			//Rotas para ambos usuário e admin
			.antMatchers("/edit/id").hasAnyRole("ADMIN", "USER")
			.antMatchers("/checkups/{usuario_id}").hasAnyRole("ADMIN", "USER")
			.antMatchers("/checkups/mapa").hasAnyRole("ADMIN", "USER")
			.antMatchers("/checkups/mapacidade").hasAnyRole("ADMIN", "USER")
			.antMatchers("/checkups/create").hasAnyRole("ADMIN", "USER")
			.antMatchers("/checkups/store").hasAnyRole("ADMIN", "USER")
			.antMatchers("/checkups/resultado/soma").hasAnyRole("ADMIN", "USER")
			
			//Rotas para todos os usuários deslogados
			.antMatchers("/home").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/cadastrar").permitAll()
			.antMatchers("/store").permitAll()
			.and().formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/checkups/create", true)
				.failureUrl("/login/erro");
		
		http.csrf()
			.ignoringAntMatchers("/logout");
		
		http.logout().logoutSuccessUrl("/login");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	

	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}

}
