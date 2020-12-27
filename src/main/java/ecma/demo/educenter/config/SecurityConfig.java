package ecma.demo.educenter.config;


import ecma.demo.educenter.entity.enums.RoleName;
import ecma.demo.educenter.security.AuthService;
import ecma.demo.educenter.security.JwtAuthenticationEntryPoint;
import ecma.demo.educenter.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthService authService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(AuthService authService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.authService = authService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(authService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
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
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v2/**",
                        "/csrf",
                        "/webjars/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/groups").hasAnyAuthority("ADMIN", "DIRECTOR")
                .antMatchers(HttpMethod.GET, "/api/groups", "/api/menu").hasAnyAuthority("ADMIN", "DIRECTOR", "TEACHER")
                .antMatchers(HttpMethod.GET, "/api/groups/teacher").hasAuthority("DIRECTOR")
                .antMatchers(HttpMethod.PATCH, "/api/groups", "/api/groups/closeOrReopen").hasAnyAuthority("ADMIN", "DIRECTOR")
                .antMatchers(HttpMethod.POST, "/api/student").hasAnyAuthority("ADMIN", "DIRECTOR", "TEACHER")
                .antMatchers(HttpMethod.GET, "/api/student", "/api/student/search").hasAnyAuthority("ADMIN", "DIRECTOR")
                .antMatchers(HttpMethod.PATCH, "/api/student/payment").hasAnyAuthority("ADMIN", "DIRECTOR")
                .antMatchers(HttpMethod.DELETE, "/api/student").hasAnyAuthority("ADMIN", "DIRECTOR")
                .antMatchers(HttpMethod.GET, "/api/subject").hasAnyAuthority("ADMIN", "DIRECTOR")
                .antMatchers(HttpMethod.POST, "/api/user").hasAuthority("DIRECTOR")
                .antMatchers(HttpMethod.GET, "/api/user/me").hasAnyAuthority("ADMIN", "DIRECTOR", "TEACHER")
                .antMatchers(HttpMethod.GET, "/api/user").hasAnyAuthority("ADMIN", "DIRECTOR")
                .antMatchers(HttpMethod.PATCH, "/api/user/disable").hasAuthority("DIRECTOR")
                .anyRequest().authenticated();

//         Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        @formatter:off
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }
}
