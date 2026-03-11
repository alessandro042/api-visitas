package mx.edu.cecyte.api_visitas.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la solicitud de autenticación.
 *
 * Usar un DTO en lugar de un Map<String, String> nos permite:
 * 1. Aplicar validaciones declarativas con @NotBlank.
 * 2. Tener un contrato explícito y bien documentado de la API.
 * 3. Desacoplar la representación externa (JSON) de las entidades internas.
 */
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Getters y Setters explícitos (sin Lombok en DTOs para máxima transparencia)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
