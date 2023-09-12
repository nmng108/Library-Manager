package org.nmng.library.manager.security;

import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.filter.JwtVerificationFilter;
import org.nmng.library.manager.service.Impl.UserServiceImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final JwtVerificationFilter jwtVerificationFilter;

    public WebSecurityConfiguration(JwtAuthenticationEntryPoint authenticationEntryPoint,
                                    UserServiceImpl userService,
                                    JwtVerificationFilter jwtVerificationFilter
    ) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userDetailsService = userService;
        this.jwtVerificationFilter = jwtVerificationFilter;
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.builder().username("lib").password("1").roles(Role.LIBRARIAN).build());
//        manager.createUser(User.builder().username("patron").password("2").roles("PATRON").build());
//
//        return manager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public FilterRegistrationBean<JwtVerificationFilter> registerJwtFilter(JwtVerificationFilter filter) {
        FilterRegistrationBean<JwtVerificationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);

        return registrationBean;
    }

    @Bean
    public SecurityFilterChain authenticationEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher(AntPathRequestMatcher.antMatcher("/api/auth/**"))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/login")).anonymous()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/register")).anonymous()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/password")).permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(this.jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(this.userDetailsService)
        ;

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain bookManagementEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/books/**")
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers(HttpMethod.POST).hasAnyAuthority(Role.LIBRARIAN)
                        .requestMatchers(HttpMethod.PATCH).hasAnyAuthority(Role.LIBRARIAN)
                        .requestMatchers(HttpMethod.DELETE).hasAnyAuthority(Role.LIBRARIAN)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(this.jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
//                .userDetailsService(this.userDetailsService)
        ;

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain categoryManagementEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/categories/**")
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers(HttpMethod.POST).hasAuthority(Role.LIBRARIAN)
                        .requestMatchers(HttpMethod.PATCH).hasAnyAuthority(Role.LIBRARIAN)
                        .requestMatchers(HttpMethod.DELETE).hasAnyAuthority(Role.LIBRARIAN)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(this.jwtVerificationFilter, AuthorizationFilter.class)
//                .userDetailsService(this.userDetailsService)
        ;

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain requestManagementEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher(AntPathRequestMatcher.antMatcher("/api/requests/**"))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAuthority(Role.LIBRARIAN)
                )
                .addFilterBefore(this.jwtVerificationFilter, AuthorizationFilter.class)
//                .userDetailsService(this.userDetailsService)
        ;

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain patronManagementEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/patrons/**")
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/patrons/*").authenticated()
                        .anyRequest().hasAnyAuthority(Role.LIBRARIAN, Role.ADMIN, Role.ROOT_ADMIN)
                )
                .addFilterBefore(this.jwtVerificationFilter, AuthorizationFilter.class)
//                .userDetailsService(this.userDetailsService)
        ;

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain librarianManagementEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/librarians/**")
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/librarians/{identifiable}")
                            .hasAnyAuthority(Role.LIBRARIAN, Role.ROOT_ADMIN, Role.ADMIN)
                        .anyRequest().hasAnyAuthority(Role.ROOT_ADMIN, Role.ADMIN)
                )
                .addFilterBefore(this.jwtVerificationFilter, AuthorizationFilter.class)
//                .userDetailsService(this.userDetailsService)
        ;

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain userManagementEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/users/**")
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAnyAuthority(Role.ROOT_ADMIN, Role.ADMIN)
                )
                .addFilterBefore(this.jwtVerificationFilter, AuthorizationFilter.class)
//                .userDetailsService(this.userDetailsService)
        ;

        return httpSecurity.build();
    }
}
