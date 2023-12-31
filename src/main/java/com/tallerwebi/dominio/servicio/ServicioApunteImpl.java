package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service("servicioApunte")
@Transactional
public class ServicioApunteImpl implements ServicioApunte {
    private RepositorioApunte repositorioApunte;
    private RepositorioUsuarioApunte repositorioUsuarioApunte;

    @Autowired
    public ServicioApunteImpl(RepositorioApunte repositorioApunte, RepositorioUsuarioApunte repositorioUsuarioApunte) {
        this.repositorioApunte = repositorioApunte;
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
    }
    @Override
    public List<Apunte> obtenerApuntes() {
        return repositorioApunte.obtenerApuntes();
    }

    @Override
    public Apunte obtenerApuntePorIdResena(Long idResena) {
        return repositorioApunte.obtenerApuntePorIdResena(idResena);
    }

    
    @Override
    public List<UsuarioApunteResena> getListadoDeResenasConSusUsuariosPorIdApunte(Long idApunte) {
        return this.repositorioApunte.getListadoDeResenasConSusUsuariosPorIdApunte(idApunte);
    };

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

    @Override
    public List<Apunte> obtenerApuntesNovedades() {
        Date now = new Date();
        LocalDate haceUnaSemana = LocalDate.now().minusDays(7);
        Date dateHaceUnaSemana = Date.from(haceUnaSemana.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return repositorioApunte.obtenerApuntesEntreFechas(dateHaceUnaSemana, now);
    }

    @Override
    public Materia obtenerMateriaPorIdDeApunte(Long id){
        return repositorioApunte.obtenerMateriaPorIdApunte(id);
    }
}
