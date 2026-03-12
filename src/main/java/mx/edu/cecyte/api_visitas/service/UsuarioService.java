package mx.edu.cecyte.api_visitas.service;

import mx.edu.cecyte.api_visitas.dto.UsuarioRequest;
import mx.edu.cecyte.api_visitas.entity.Usuario;
import mx.edu.cecyte.api_visitas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario con ID " + id + " no encontrado"));
    }

    public Usuario crear(UsuarioRequest req) {
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria al crear un usuario");
        }
        Usuario u = new Usuario();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setNombreCompleto(req.getNombreCompleto());
        u.setRol(req.getRol());
        return usuarioRepository.save(u);
    }

    public Usuario actualizar(Long id, UsuarioRequest req) {
        Usuario u = buscarPorId(id);
        u.setUsername(req.getUsername());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        u.setNombreCompleto(req.getNombreCompleto());
        u.setRol(req.getRol());
        return usuarioRepository.save(u);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario con ID " + id + " no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
