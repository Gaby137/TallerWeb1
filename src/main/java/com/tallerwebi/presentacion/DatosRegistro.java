package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Rol;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class DatosRegistro {
    @NotEmpty(message = "El nombre no puede estar en blanco.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúüÁÉÍÓÚÜ\\s]*$", message = "El nombre solo puede contener letras y espacios.")
    private String nombre;
    @NotEmpty(message = "El apellido no puede estar en blanco.")
   @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
   @Pattern(regexp = "^[a-zA-ZáéíóúüÁÉÍÓÚÜ\\s]*$", message = "El apellido solo puede contener letras y espacios.")
    private String apellido;
    @NotEmpty(message = "El mail no puede estar en blanco.")
     @Pattern(regexp = "^[A-Za-z0-9+_.-]+@alumno\\.unlam\\.edu\\.ar$", message = "El mail debe ser una de la institución")
    private String email;
    @NotEmpty(message = "La contraseña no puede estar en blanco.")
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "La contraseña debe contener al menos una letra mayúscula, una letra minúscula y un dígito.")
    @NotBlank
    private String password;
    private MultipartFile fotoPerfil;

    public DatosRegistro(){

    }
    public DatosRegistro(String nombre, String apellido, String email, String password, MultipartFile fotoPerfil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(MultipartFile fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
