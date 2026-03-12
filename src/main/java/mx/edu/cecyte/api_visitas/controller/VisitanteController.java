package mx.edu.cecyte.api_visitas.controller;

import jakarta.validation.Valid;
import mx.edu.cecyte.api_visitas.entity.Visitante;
import mx.edu.cecyte.api_visitas.service.AuditService;
import mx.edu.cecyte.api_visitas.service.VisitanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitantes")
public class VisitanteController {

    private final VisitanteService visitanteService;
    private final AuditService auditService;

    public VisitanteController(VisitanteService visitanteService, AuditService auditService) {
        this.visitanteService = visitanteService;
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<Visitante>> obtenerVisitantes() {
        return ResponseEntity.ok(visitanteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visitante> obtenerVisitantePorId(@PathVariable Long id) {
        return ResponseEntity.ok(visitanteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Visitante> registrarVisitante(@Valid @RequestBody Visitante visitante, Authentication auth) {
        Visitante nuevo = visitanteService.registrar(visitante);
        auditService.registrar(auth.getName(), "REGISTRAR_VISITANTE",
                "Visitante registrado: " + nuevo.getNombreCompleto() + " (ID " + nuevo.getId() + ")");
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visitante> actualizarVisitante(@PathVariable Long id,
            @Valid @RequestBody Visitante visitante, Authentication auth) {
        Visitante actualizado = visitanteService.actualizar(id, visitante);
        auditService.registrar(auth.getName(), "ACTUALIZAR_VISITANTE",
                "Visitante actualizado: " + actualizado.getNombreCompleto() + " (ID " + id + ")");
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/salida")
    public ResponseEntity<Visitante> registrarSalida(@PathVariable Long id, Authentication auth) {
        Visitante v = visitanteService.registrarSalida(id);
        auditService.registrar(auth.getName(), "SALIDA_VISITANTE",
                "Salida registrada: " + v.getNombreCompleto() + " (ID " + id + ")");
        return ResponseEntity.ok(v);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVisitante(@PathVariable Long id, Authentication auth) {
        String nombre = visitanteService.buscarPorId(id).getNombreCompleto();
        visitanteService.eliminar(id);
        auditService.registrar(auth.getName(), "ELIMINAR_VISITANTE",
                "Visitante eliminado: " + nombre + " (ID " + id + ")");
        return ResponseEntity.noContent().build();
    }
}