package com.example.bookingproject.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    //Configure Security Filter chain - chain (цепочка) of methods , that will control how your security works
    UserDetailsService userDetailsService;
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {this.userDetailsService = userDetailsService;}

    @Bean
    public static PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // отключает защиту от межсайтовой подделки запросов (CSRF). В реальном приложении это может быть небезопасно, и вы должны включить защиту CSRF, если ваше приложение подвержено этому типу атак.
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/home")).permitAll()
                        // nobody can delete ADMIN user (even ADMIN himself)
                        .requestMatchers(new AntPathRequestMatcher("/users/delete/1")).denyAll()
                        .anyRequest().authenticated()) //other URLs are only allowed authenticated users.

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home?successLogin",true)
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")// = ?fail
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );
        return http.build();}
    //Authentication Manager
    public void configure(AuthenticationManagerBuilder builder) throws Exception
    {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}