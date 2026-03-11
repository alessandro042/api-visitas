package mx.edu.cecyte.api_visitas.security;

import mx.edu.cecyte.api_visitas.entity.Usuario;
import mx.edu.cecyte.api_visitas.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias a través del constructor (Buena práctica en Spring)
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscamos al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado en la base de datos"));

        // Retornamos un objeto "User" propio de Spring Security para que él haga la
        // validación
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                new ArrayList<>() // Aquí irían los roles (ej. ADMIN, USER), pero para el prototipo lo dejamos
                                  // vacío
        );
    }
}