package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosApunte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("servicioApunte")
@Transactional
public class ServicioApunteImpl implements ServicioApunte{
    private RepositorioApunte repositorioApunte;
    @Autowired
    public ServicioApunteImpl(RepositorioApunte repositorioApunte){
        this.repositorioApunte = repositorioApunte;
    }
    @Override
    public List<Apunte> obtenerApuntes() {
        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();
        apunte1.setId(1L);
        apunte1.setNombre("Guía TP - PW2");
        apunte1.setDescripcion("Guía de trabajos prácticos de PW2");
        apunte2.setId(2L);
        apunte2.setNombre("Resumen - 1er Parcial BD1");
        apunte2.setDescripcion("Resumen para el primer parcial de BD1");
        List<Apunte> listHard = new ArrayList<>();
        listHard.add(apunte1);
        listHard.add(apunte2);
        listHard.add(apunte1);
        listHard.add(apunte2);
        listHard.add(apunte1);
        listHard.add(apunte2);
        return listHard;
    }

    @Override
    public boolean registrar(DatosApunte datosApunte) {
        boolean result;
        if (datosApunte.getPathArchivo() == null || datosApunte.getPathArchivo().isEmpty() ||
                datosApunte.getNombre() == null || datosApunte.getNombre().isEmpty() ||
                datosApunte.getDescripcion() == null || datosApunte.getDescripcion().isEmpty()) {

            result = false;
        }else {
            Apunte apunte = new Apunte(datosApunte.getPathArchivo(), datosApunte.getNombre(), datosApunte.getDescripcion(), new Date(), new Date());

            repositorioApunte.registrarApunte(apunte);
            result = true;
        }
       return result;

    }

    @Override
    public Apunte obtenerPorId(Long id) {

        return repositorioApunte.obtenerApunte(id);
    }

    @Override
    public boolean actualizar(Apunte apunte) {
        boolean result;
        if (apunte.getPathArchivo() == null || apunte.getPathArchivo().isEmpty() ||
                apunte.getNombre() == null || apunte.getNombre().isEmpty() ||
                apunte.getDescripcion() == null || apunte.getDescripcion().isEmpty()) {

            result = false;
        }else {

            repositorioApunte.modificarApunte(apunte);
            result = true;
        }
        return result;
    }

    @Override
    public void eliminar(Long id) {
        Apunte apunte = repositorioApunte.obtenerApunte(id);

        repositorioApunte.eliminarApunte(apunte);
    }
}
