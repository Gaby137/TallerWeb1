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
public class ServicioApunteImpl implements ServicioApunte {
    private RepositorioApunte repositorioApunte;

    @Autowired
    public ServicioApunteImpl(RepositorioApunte repositorioApunte) {
        this.repositorioApunte = repositorioApunte;
    }

    @Override
    public List<Apunte> obtenerApuntes() {
        return repositorioApunte.obtenerApuntes();
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
