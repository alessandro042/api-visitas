package mx.edu.cecyte.api_visitas.repository;

import mx.edu.cecyte.api_visitas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Boot detecta "findBy" + "Username" y crea el SQL automáticamente
    // Retorna un Optional para evitar errores si el usuario no existe
    Optional<Usuario> findByUsername(String username);
}