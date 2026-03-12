package mx.edu.cecyte.api_visitas.dto;

import mx.edu.cecyte.api_visitas.entity.Usuario;

public class UsuarioResponse {

    private Long id;
    private String username;
    private String nombreCompleto;
    private String rol;

    public UsuarioResponse(Usuario u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.nombreCompleto = u.getNombreCompleto();
        this.rol = u.getRol();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getRol() { return rol; }
}
