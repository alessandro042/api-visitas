package mx.edu.cecyte.api_visitas.service;

import mx.edu.cecyte.api_visitas.entity.AuditLog;
import mx.edu.cecyte.api_visitas.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void registrar(String usuario, String accion, String detalle) {
        AuditLog log = new AuditLog();
        log.setUsuario(usuario);
        log.setAccion(accion);
        log.setDetalle(detalle);
        repository.save(log);
    }

    public List<AuditLog> listarTodos() {
        return repository.findAllByOrderByTimestampDesc();
    }
}
