package mx.edu.cecyte.api_visitas.service;

import mx.edu.cecyte.api_visitas.entity.Visitante;
import mx.edu.cecyte.api_visitas.repository.VisitanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VisitanteService {

    private final VisitanteRepository visitanteRepository;

    public VisitanteService(VisitanteRepository visitanteRepository) {
        this.visitanteRepository = visitanteRepository;
    }

    public List<Visitante> listarTodos() {
        return visitanteRepository.findAll();
    }

    public Visitante buscarPorId(Long id) {
        return visitanteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Visitante con ID " + id + " no encontrado"));
    }

    public Visitante registrar(Visitante visitante) {
        return visitanteRepository.save(visitante);
    }

    public void eliminar(Long id) {
        if (!visitanteRepository.existsById(id)) {
            throw new NoSuchElementException("Visitante con ID " + id + " no encontrado");
        }
        visitanteRepository.deleteById(id);
    }
}
