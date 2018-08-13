package com.softserve.edu.bookinglite.config;

import com.softserve.edu.bookinglite.security.JwtAuthenticationEntryPoint;
import com.softserve.edu.bookinglite.security.JwtAuthorizationFilter;
import com.softserve.edu.bookinglite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/api/login","/api/register","/api/registrationconfirm").permitAll()
                .antMatchers("/api/login","/api/register","/api/registrationconfirm","/api/test").permitAll()
                .antMatchers(HttpMethod.POST,"/api/property","property/*/photo","/property/*/apartment").hasAuthority("ROLE_OWNER")
                .antMatchers(HttpMethod.PUT,"/apartment/*","/property/*").hasAuthority("ROLE_OWNER")
                .antMatchers(HttpMethod.DELETE,"/photo/*").hasAuthority("ROLE_OWNER")
                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthorizationFilter,UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }




}
