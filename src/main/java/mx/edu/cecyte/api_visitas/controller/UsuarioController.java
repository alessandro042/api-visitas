package mx.edu.cecyte.api_visitas.controller;

import jakarta.validation.Valid;
import mx.edu.cecyte.api_visitas.dto.UsuarioRequest;
import mx.edu.cecyte.api_visitas.dto.UsuarioResponse;
import mx.edu.cecyte.api_visitas.service.AuditService;
import mx.edu.cecyte.api_visitas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuditService auditService;

    public UsuarioController(UsuarioService usuarioService, AuditService auditService) {
        this.usuarioService = usuarioService;
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> lista = usuarioService.listarTodos()
                .stream().map(UsuarioResponse::new).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new UsuarioResponse(usuarioService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest req, Authentication auth) {
        UsuarioResponse nuevo = new UsuarioResponse(usuarioService.crear(req));
        auditService.registrar(auth.getName(), "CREAR_USUARIO",
                "Usuario creado: " + req.getUsername() + " [" + req.getRol() + "]");
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody UsuarioRequest req, Authentication auth) {
        UsuarioResponse actualizado = new UsuarioResponse(usuarioService.actualizar(id, req));
        auditService.registrar(auth.getName(), "ACTUALIZAR_USUARIO",
                "Usuario actualizado: " + req.getUsername() + " (ID " + id + ")");
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication auth) {
        String username = usuarioService.buscarPorId(id).getUsername();
        usuarioService.eliminar(id);
        auditService.registrar(auth.getName(), "ELIMINAR_USUARIO",
                "Usuario eliminado: " + username + " (ID " + id + ")");
        return ResponseEntity.noContent().build();
    }
}
