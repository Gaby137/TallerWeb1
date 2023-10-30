package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service("servicioUsuarioApunteResena")
@Transactional
public class ServicioUsuarioApunteResenaImpl implements ServicioUsuarioApunteResena {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResena;
    private ServicioApunte servicioApunte;
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunte servicioUsuarioApunte;

    @Autowired
    public ServicioUsuarioApunteResenaImpl(RepositorioUsuarioApunteResena repositorioUsuarioApunteResena, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte){
        this.repositorioUsuarioApunteResena = repositorioUsuarioApunteResena;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;

    }

    @Override
    public boolean registrar(Usuario usuario, Apunte apunte, Resena resena) {

        if(repositorioUsuarioApunteResena.existeResenaConApunteYUsuario(usuario.getId(), apunte.getId())) {
            return false;
        }else {

            UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
            resena.setCreated_at(new Date());

            usuario.setPuntos(usuario.getPuntos() + 10);

            servicioUsuario.actualizar(usuario);

            usuarioApunteResena.setResena(resena);

            usuarioApunteResena.setUsuario(usuario);

            usuarioApunteResena.setApunte(apunte);

            repositorioUsuarioApunteResena.guardar(usuarioApunteResena);

            dar100PuntosAlUsuarioPorBuenasResenas(apunte.getId());

            return true;
        }
    }
    @Override
    public List<Resena> obtenerLista(Long idApunte) {
        return repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(idApunte);
    }

    @Override
    public boolean dar100PuntosAlUsuarioPorBuenasResenas(Long idApunte) {
        Apunte apunte = servicioApunte.obtenerPorId(idApunte);
        List<Resena> resenas = repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(idApunte);

        if (!apunte.isCienPuntosPorBuenPromedioDeResenas()){
          if (resenas.size() >= 5) {
              double totalPuntaje = 0.0;
              for (Resena resena : resenas) {
                  totalPuntaje += resena.getCantidadDeEstrellas();
              }
              double promedio = totalPuntaje / resenas.size();

              if (promedio >= 4.5) {
                  Usuario usuario = servicioUsuarioApunte.obtenerVendedorPorApunte(idApunte);
                  usuario.setPuntos(usuario.getPuntos() + 100);
                  servicioUsuario.actualizar(usuario);
                  apunte.setCienPuntosPorBuenPromedioDeResenas(true);
                  return true;
              }
          }
        }
        return false;
}
    @Override
    public double calcularPromedioPuntajeResenas(Long apunteId) {
        List<Resena> resenas = repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(apunteId);

        if (resenas.isEmpty()) {
            return 0.0;
        }

        double totalPuntaje = 0.0;
        for (Resena resena : resenas) {
            totalPuntaje += resena.getCantidadDeEstrellas();
        }

        return totalPuntaje / resenas.size();
    }

}

