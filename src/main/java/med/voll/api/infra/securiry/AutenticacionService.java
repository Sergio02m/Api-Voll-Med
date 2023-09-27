package med.voll.api.infra.securiry;

import med.voll.api.domain.usuarios.UsuarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Esta clase es la que va a tener la lógica de autenticación dentro de mi aplicación. Ahora como
// siempre, si yo deseo decirle a Spring: “por favor Spring escanea esta clase, porque es un servicio
// que yo quiero abastecer para mi aplicación con @Service

// UserDetailsService es una interfaz propia de Spring que Spring utiliza para efectuar su login

@Service
public class AutenticacionService implements UserDetailsService {

    @Autowired
    private UsuarioRespository usuarioRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRespository.findByLogin(username);
    }

}
