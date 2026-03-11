package mx.edu.cecyte.api_visitas.repository;

import mx.edu.cecyte.api_visitas.entity.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    // No necesitas escribir nada aquí adentro por ahora.
    // JpaRepository ya trae todos los métodos básicos (save, findAll, findById).
}