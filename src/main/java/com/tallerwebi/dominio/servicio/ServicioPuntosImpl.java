/*package com.tallerwebi.dominio.servicio;


import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioPuntos")
@Transactional
public class ServicioPuntosImpl implements ServicioPuntos{

    private ServicioApunte servicioApunte;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;
    private ServicioUsuario servicioUsuario;
    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResena;
    @Autowired
    public ServicioPuntosImpl(RepositorioUsuarioApunteResena repositorioUsuarioApunteResena, ServicioUsuarioApunteResena servicioUsuarioApunteResena, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte) {
        this.repositorioUsuarioApunteResena = repositorioUsuarioApunteResena;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;

    }


    @Override
    public boolean dar100PuntosAlUsuarioPorBuenasResenas(Long idApunte) {
        Apunte apunte = servicioApunte.obtenerPorId(idApunte);
        List<Resena> resenas = repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(idApunte);

        if (!apunte.isCienPuntosPorBuenPromedioDeResenas()) {
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
    public void darPuntosAlUsuarioPorParticipacionContinua(Usuario usuario) {
        List<Resena> resenas = repositorioUsuarioApunteResena.obtenerResenasPorIdUsuario(usuario.getId());
        List<UsuarioApunte> apuntes = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        int resenasSize = resenas.size();
        int apuntesSize = apuntes.size();

        int totalPuntosGanados = 0;

        while (resenasSize == 5 && apuntesSize == 5) {
            int puntosGanados = 50;
            totalPuntosGanados += puntosGanados;

            resenas.subList(0, 5).clear();
            apuntes.subList(0, 5).clear();

            resenasSize = resenas.size();
            apuntesSize = apuntes.size();
        }

        usuario.setPuntos(usuario.getPuntos() + totalPuntosGanados);
        servicioUsuario.actualizar(usuario);
    }

}*/
