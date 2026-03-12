package mx.edu.cecyte.api_visitas.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    private String password;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
