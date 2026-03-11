package mx.edu.cecyte.api_visitas.controller;

import jakarta.validation.Valid;
import mx.edu.cecyte.api_visitas.entity.Visitante;
import mx.edu.cecyte.api_visitas.service.VisitanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitantes")
public class VisitanteController {

    private final VisitanteService visitanteService;

    public VisitanteController(VisitanteService visitanteService) {
        this.visitanteService = visitanteService;
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
    public ResponseEntity<Visitante> registrarVisitante(@Valid @RequestBody Visitante visitante) {
        Visitante nuevo = visitanteService.registrar(visitante);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVisitante(@PathVariable Long id) {
        visitanteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}