package med.voll.api.infra.securiry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration, Entonces Spring con esta anotación al igual que aquí con service, Spring va a venir aquí
// y va a decir: “Esta es una configuración, entonces la voy a escanear, porque en el orden de creación
// de los objetos de Spring, primero se escanean los que están anotados con @Configuration porque se
// sobreentiende que son prerrequisitos para que otros objetos de la aplicación
// puedan ser creados”.

// Para que Spring sepa que esta es una configuración del contexto de seguridad, yo aquí lo que tengo
// que decirle es @EnableWebSecurity.
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    //La gestión de sesiones (sessionManagement) en Spring Security es una forma de controlar
    // cómo se manejan las sesiones durante y después del proceso de autenticación. En el contexto
    // de una API REST, normalmente querrías que tu autenticación sea sin estado (stateless), lo que
    // significa que no se guarda ninguna información de la sesión entre las solicitudes. Esto se
    // puede lograr utilizando tokens de autenticación en cada solicitud en lugar de depender de las
    // sesiones.
    @Bean

    //La gestión de sesiones (sessionManagement) en Spring Security es una forma de controlar cómo se
    // manejan las sesiones durante y después del proceso de autenticación. En el contexto de una
    // API REST, normalmente querrías que tu autenticación sea sin estado (stateless), lo que
    // significa que no se guarda ninguna información de la sesión entre las solicitudes. Esto se
    // puede lograr utilizando tokens de autenticación en cada solicitud en lugar de depender de las
    // sesiones.

    // filtro por defecto de Sspring
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // retornar mi httpSecurity.csrf(). Vemos que tenga la terminación aquí del csrf.
        // El Cross-site request forgery y método de protection. Esto es para evitar suplantación de identidad.
        // pero como no estoy usando autenticación stateful no lo necesito por ahora

        // configuracion para utilizar autenticacion STATELESS
        return httpSecurity.csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests() // autorizamos los request que tengan el siguiente matchers (http, post, "/loging").
                .requestMatchers(HttpMethod.POST,"/login")
                .permitAll()
                .requestMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN") // solo el rol de admin eliminaara medicos
                .requestMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN") // solo el rol de admin eliminaara pacientess
                .anyRequest() // despues todos los request deben ser autenticados.
                .authenticated()
                .and() // para agregar mis filtros antes del filtro por defecto de Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter lo que va a hacer es validar que en efecto el usuario que está iniciando la sesión existe y que ya está autenticado.
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //  @Bean para que esté disponible en mi contexto de Spring
    // metodo que   informa que se realizo un cifrado a nuestra clave.
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
