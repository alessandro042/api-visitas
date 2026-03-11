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
            Usuario admin = new Usuario();
            admin.setUsername("recepcion_admin");
            admin.setPassword(passwordEncoder.encode("Cecyte2026*"));

            usuarioRepository.save(admin);
            log.info("Usuario de prueba creado: recepcion_admin");
        }
    }
}