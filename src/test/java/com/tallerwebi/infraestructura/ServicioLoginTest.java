package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.servicio.ServicioLoginImpl;
import com.tallerwebi.dominio.servicio.ServicioMateriaImpl;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.presentacion.DatosRegistro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServicioLoginTest {
    private ServicioLoginImpl servicioLogin;
    private RepositorioUsuario repositorioUsuarioMock;
    private ServicioUsuario servicioUsuarioMock;
    private String uploadDir="src/main/webapp/resources/core/img/";

    @BeforeEach
    public void init() throws IOException {
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioLogin = new ServicioLoginImpl(repositorioUsuarioMock, servicioUsuarioMock);
    }

    @Test
    public void queGenereUnCodigoDeCreadorConUnaLongitudDe6SiExisteCodigoRetornaFalse() {
        when(servicioUsuarioMock.existeCodigoCreadorEnLaBaseDeDatos(anyString())).thenReturn(false);

        String codigoDeCreadorGenerado = servicioLogin.generarCodigoCreador();

        assertNotNull(codigoDeCreadorGenerado);
        assertEquals(6, codigoDeCreadorGenerado.length());

        verify(servicioUsuarioMock, times(1)).existeCodigoCreadorEnLaBaseDeDatos(codigoDeCreadorGenerado);
    }

    @Test
    public void queNoGenereUnCodigoSiExisteCodigoRetornaTrueYVuelvaAGenerarOtro() {
        when(servicioUsuarioMock.existeCodigoCreadorEnLaBaseDeDatos(anyString())).thenReturn(true, false);

        String codigoDeCreadorGenerado = servicioLogin.generarCodigoCreador();

        assertNotNull(codigoDeCreadorGenerado);

        verify(servicioUsuarioMock, times(2)).existeCodigoCreadorEnLaBaseDeDatos(anyString());
    }


   /* @Test
    public void queAlRegistrarseUnUsuarioSeLeAsigneUnCodigoDeCreador() throws IOException, UsuarioExistente {
        File uploadDirectoryMock = mock(File.class);

        when(uploadDirectoryMock.exists()).thenReturn(true);

        when(new File(eq(uploadDir))).thenReturn(uploadDirectoryMock);

        MockMultipartFile archivoSimulado = mock(MockMultipartFile.class);

        doNothing().when(archivoSimulado).transferTo(any(File.class));

        when(archivoSimulado.getOriginalFilename()).thenReturn("nombreDeArchivo.jpg");

        when(repositorioUsuarioMock.buscarUsuario(anyString())).thenReturn(null);

        when(servicioUsuarioMock.existeCodigoCreadorEnLaBaseDeDatos(anyString())).thenReturn(false);

        servicioLogin.generarCodigoCreador();

        Usuario usuarioRegistrado = new Usuario();

        doNothing().when(repositorioUsuarioMock).guardar(eq(usuarioRegistrado));

        servicioLogin.registrar(new DatosRegistro("Tomas", "Cernik", "sdadsads", "sadasdasd", archivoSimulado));

        verify(repositorioUsuarioMock, times(1)).guardar(eq(usuarioRegistrado));
        verify(servicioUsuarioMock, times(1)).existeCodigoCreadorEnLaBaseDeDatos(anyString());
        verify(servicioUsuarioMock, times(0)).actualizar(any(Usuario.class));
    }*/
}
