package mx.edu.cecyte.api_visitas;

import lombok.extern.slf4j.Slf4j;
import mx.edu.cecyte.api_visitas.entity.Usuario;
import mx.edu.cecyte.api_visitas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            crear("admin",          "Admin2026*",   "Administrador del Sistema", "ADMIN");
            crear("recepcion_admin","Cecyte2026*",  "Recepcionista Principal",   "RECEPCIONISTA");
            crear("vigilante01",    "Cecyte2026*",  "Vigilante Turno A",         "VIGILANTE");
            log.info("Usuarios iniciales creados: admin, recepcion_admin, vigilante01");
        }
    }

    private void crear(String username, String password, String nombre, String rol) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setNombreCompleto(nombre);
        u.setRol(rol);
        usuarioRepository.save(u);
    }
}