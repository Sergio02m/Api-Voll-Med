package med.voll.api.infra.securiry;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Clase encargad de hacer el filtro al momento de hacer Request

// Component es el estereotipo más genérico de Spring para definir simplemente un componente de
// Spring. Spring precisa hacer el escaneo en la clase para incluirlo en su contexto. Service,
// repository y controller son estereotipos basados en componente, explicándolo en otras formas.
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRespository usuarioRespository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("El filtro esta siendo llamado");
        // Realizamos el filtro para que este pueda acceder a los controller y retornar los valores.
        // obtener el token del header

        // nombre del Header es siempre Authorization

        var autoHeader = request.getHeader("Authorization");
        if (autoHeader != null){
            System.out.println("Validamos que token no es Null");
            // si solo queremos manejar el Token, entonces reemplazamos el Bearer por ""(vacio)
            var token = autoHeader.replace("Bearer ", "");
            System.out.println(token);
            var nombreUsuario = tokenService.getSubject(token);
            if (nombreUsuario != null){
                // token valido
                var usuario = usuarioRespository.findByLogin(nombreUsuario);
                var autentication = new UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities()); // forzamos un inicio de sesion
                SecurityContextHolder.getContext().setAuthentication(autentication);
            }
        }
        filterChain.doFilter(request, response);

    }
}
