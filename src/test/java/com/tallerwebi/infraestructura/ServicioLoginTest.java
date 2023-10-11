package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.servicio.ServicioLoginImpl;
import com.tallerwebi.dominio.servicio.ServicioMateriaImpl;
import com.tallerwebi.dominio.servicio.ServicioUsuarioImpl;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class ServicioLoginTest {
    private ServicioLoginImpl servicioLogin;
    private RepositorioUsuario repositorioUsuarioMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        servicioLogin = new ServicioLoginImpl(repositorioUsuarioMock);
    }
}
