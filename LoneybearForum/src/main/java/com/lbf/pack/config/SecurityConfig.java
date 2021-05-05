package com.lbf.pack.config;

import com.lbf.pack.interceptor.CustomUrlIntercepter;
import com.lbf.pack.service.CustomUserDetailService;
import com.lbf.pack.serviceImpl.CustomUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomAuthenticationSuccessHandler successHandler;
    @Autowired
    CustomAuthenticationFailHandler failHandler;
    @Autowired
    CustomUserDetailService customUserDetailService;

//    密码加密，暂时没用
    @Bean
    PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    VerifycodeCustomAuthenticationProvider myAuthenticationProvider() {
        VerifycodeCustomAuthenticationProvider myAuthenticationProvider = new VerifycodeCustomAuthenticationProvider();
        myAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myAuthenticationProvider.setUserDetailsService(customUserDetailService);
        return myAuthenticationProvider;
    }



    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager manager = new ProviderManager(Arrays.asList(myAuthenticationProvider()));
        return manager;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());

    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("3 > 2 > 1 > 0");
        return hierarchy;
    }
//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        userDetailsService(customUserDetailService);
//        manager.createUser(User.withUsername("javaboy").password("123").roles("admin").build());
//        return manager;
//    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**","/images/**","/fonts/**","/swiper-master/**","/static/**","/signup/**","/reset/**","/defaultKaptcha");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        http
                .exceptionHandling()
                    .accessDeniedHandler(new CustomAccessDeniedHandler())   //认证异常处理
                    .authenticationEntryPoint(new CustomNotAuthencated())
                .and()
                .authorizeRequests()
                    .antMatchers("/userdata").hasAuthority("1")
                    .antMatchers("/zoneadmin/**").hasAuthority("2")

                    .antMatchers("/admin/**").hasAuthority("2")
                    .antMatchers("/senior/**").hasAuthority("3")
                    .antMatchers("/zone/**/manage").hasAuthority("2")
                    .antMatchers("/**").permitAll()



                .anyRequest()
                    .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .loginProcessingUrl("/verify")
                    .successHandler(successHandler)
                    .failureHandler(failHandler)


                .and()
//                .defaultSuccessUrl("/homepage")
                .csrf().disable()
                .logout();
//                .permitAll();
//                .and()

    }



}
