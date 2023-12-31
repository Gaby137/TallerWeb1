package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioApunteImpl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioApunteTest {
    private ServicioApunteImpl servicioApunte;
    private RepositorioApunte repositorioApunteMock;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
 
        repositorioApunteMock = mock(RepositorioApunte.class);
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        servicioApunte = new ServicioApunteImpl(repositorioApunteMock, repositorioUsuarioApunteMock);


    }

    @Test
    public void testObtenerApuntePoId() {
        // Configuración de un id de ejemplo
        Long idEjemplo = 1L;

        // Configuración del comportamiento del repositorio simulado
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20,  new Date(), new Date());
        apunteMock.setId(idEjemplo);
        when(repositorioApunteMock.obtenerApunte(idEjemplo)).thenReturn(apunteMock);

        // Ejecución de la prueba
        Apunte resultado = servicioApunte.obtenerPorId(idEjemplo);

        // Verificación
        assertNotNull(resultado);
        assertEquals(apunteMock, resultado);
    }

    @Test
    public void testActualizarApunteFuncionamientoABML() {
        // Configuración de un apunte de ejemplo
        Apunte apunteEjemplo = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());

        // Ejecución de la prueba
        servicioApunte.actualizar(apunteEjemplo);

        // Verificación
        verify(repositorioApunteMock).modificarApunte(apunteEjemplo); // Verifica que se llamó al método del repositorio
    }

    @Test
    public void testEliminarApunteFuncionamientoABML() {
        Long idApunte = 1L;
        Apunte apunteEjemplo = new Apunte();

        // Configurar el comportamiento del repositorio para devolver el apunte de ejemplo
        when(repositorioApunteMock.obtenerApunte(idApunte)).thenReturn(apunteEjemplo);

        // Llamada al método que queremos probar
        servicioApunte.eliminar(idApunte);

        // Verificar que se llamó al método del repositorio para eliminar el apunte
        verify(repositorioApunteMock).eliminarApunte(apunteEjemplo);
    }

    @Transactional
    @Rollback
    @Test
    public void deberiaDevolverLosApuntesCreadosEnLaUltimaSemana() {
        Apunte apunte1 = new Apunte();
        apunte1.setNombre("Nombre Apunte 1");
        Apunte apunte2 = new Apunte();
        apunte2.setNombre("Nombre Apunte 2");
        Apunte apunte3 = new Apunte();
        apunte3.setNombre("Nombre Apunte 3");

        apunte1.setCreated_at(new Date("2023/05/05"));
        apunte2.setCreated_at(new Date());
        apunte3.setCreated_at(new Date());

        List <Apunte> listadoResp = new ArrayList<>();
        listadoResp.add(apunte2);
        listadoResp.add(apunte3);

        when(repositorioApunteMock.obtenerApuntesEntreFechas(any(Date.class), any(Date.class))).thenReturn(listadoResp);

        List<Apunte> novedadesObtenidas = servicioApunte.obtenerApuntesNovedades();

        assertEquals(2, novedadesObtenidas.size());

    }


}
