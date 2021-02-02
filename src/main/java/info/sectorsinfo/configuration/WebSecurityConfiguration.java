package info.sectorsinfo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private DataSource dataSource;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
      .dataSource(dataSource)
      .passwordEncoder(passwordEncoder())
      .usersByUsernameQuery("select login, password, enabled from user where login=?")
      .authoritiesByUsernameQuery("select login, role from user where login=?");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers("/login", "/").permitAll()
      .antMatchers("/sector", "/list").hasAuthority("USER")
      .antMatchers("/", "/**").permitAll()
      .antMatchers("/error").permitAll()
      .anyRequest().authenticated()
      .and().formLogin();

    http
      .logout().permitAll().logoutSuccessUrl("/");
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(4);
  }
}
