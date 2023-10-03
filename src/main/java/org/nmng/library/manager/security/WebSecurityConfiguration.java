package org.nmng.library.manager.security;

import lombok.extern.java.Log;
import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private LoginAuthenticationFilter loginAuthenticationFilter;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserRepository userRepository;

    public WebSecurityConfiguration(JwtAuthenticationEntryPoint authenticationEntryPoint,
                                    UserServiceImpl userService,
                                    UserRepository userRepository,
                                    AuthenticationConfiguration authenticationConfiguration
    ) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userDetailsService = userService;
        this.userRepository = userRepository;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    /**
     * Circular dependence, where this Configuration (and its Beans) must be instantiated before JwtAuthenticationFilter
     */
    @Lazy
    @Autowired
    public void setJwtAuthenticationFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Circular dependence, where this Configuration (and its Beans) must be instantiated before LoginAuthenticationFilter
     */
    @Lazy
    @Autowired
    public void setLoginAuthenticationFilter(LoginAuthenticationFilter loginAuthenticationFilter) {
        this.loginAuthenticationFilter = loginAuthenticationFilter;
    }

    /**
     * Prevent auto-registering JwtAuthenticationFilter to the main Filter chain, which leads to duplicate the filter
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> configureJwtAuthenticationFilter(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);

        return registrationBean;
    }

    /**
     * Prevent auto-registering LoginAuthenticationFilter to the main Filter chain, which leads to duplicate the filter
     */
    @Bean
    public FilterRegistrationBean<LoginAuthenticationFilter> configureLoginAuthenticationFilter(LoginAuthenticationFilter filter) {
        FilterRegistrationBean<LoginAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);

        return registrationBean;
    }

    /**
     * Additional provider for authenticating user with a jwt
     */
    @Bean
    public JwtAuthenticationProvider instantiateJwtAuthenticationProvider() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setUserRepository(this.userRepository);

        return provider;
    }

    /**
     * Declare a global AuthenticationManager bean
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain authenticationEndpointFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher(AntPathRequestMatcher.antMatcher("/api/auth/**"))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(this.authenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/login")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/register")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/password")).permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(this.loginAuthenticationFilter, AuthorizationFilter.class)
//                .addFilterAfter(this.jwtAuthenticationFilter, LoginAuthenticationFilter.class)
//                .authenticationManager(this.authenticationManager)
//                .userDetailsService(this.userDetailsService)
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
//                        .requestMatchers(HttpMethod.POST).hasAnyAuthority(Role.LIBRARIAN)
//                        .requestMatchers(HttpMethod.PATCH).hasAnyAuthority(Role.LIBRARIAN)
//                        .requestMatchers(HttpMethod.DELETE).hasAnyAuthority(Role.LIBRARIAN)
                                .anyRequest().hasAnyAuthority(Role.LIBRARIAN)
                )
                .addFilterAfter(this.jwtAuthenticationFilter, RequestCacheAwareFilter.class)
                .requestCache(cache -> cache.requestCache(new HttpSessionRequestCache()))
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
//                        .requestMatchers(HttpMethod.POST).hasAuthority(Role.LIBRARIAN)
//                        .requestMatchers(HttpMethod.PATCH).hasAnyAuthority(Role.LIBRARIAN)
//                        .requestMatchers(HttpMethod.DELETE).hasAnyAuthority(Role.LIBRARIAN)
                                .anyRequest().hasAnyAuthority(Role.LIBRARIAN)
                )
                .addFilterBefore(this.jwtAuthenticationFilter, AuthorizationFilter.class)
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
                        .requestMatchers(HttpMethod.GET).hasAnyAuthority(Role.LIBRARIAN, Role.ADMIN, Role.ROOT_ADMIN)
                        .anyRequest().hasAuthority(Role.LIBRARIAN)
                )
                .addFilterBefore(this.jwtAuthenticationFilter, AuthorizationFilter.class)
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
                        .requestMatchers(HttpMethod.GET, "/api/patrons/*").hasAnyAuthority(Role.PATRON, Role.LIBRARIAN, Role.ADMIN, Role.ROOT_ADMIN)
                        .anyRequest().hasAnyAuthority(Role.LIBRARIAN, Role.ADMIN, Role.ROOT_ADMIN)
                )
                .addFilterBefore(this.jwtAuthenticationFilter, AuthorizationFilter.class)
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
                .addFilterBefore(this.jwtAuthenticationFilter, AuthorizationFilter.class)
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
                .addFilterBefore(this.jwtAuthenticationFilter, AuthorizationFilter.class)
        ;

        return httpSecurity.build();
    }
}
