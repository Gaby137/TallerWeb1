package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service("servicioUsuarioApunteResena")
@Transactional
public class ServicioUsuarioApunteResenaImpl implements ServicioUsuarioApunteResena {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResena;
    private RepositorioUsuarioApunte repositorioUsuarioApunte;
    private ServicioApunte servicioApunte;
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunte servicioUsuarioApunte;

    @Autowired
    public ServicioUsuarioApunteResenaImpl(RepositorioUsuarioApunteResena repositorioUsuarioApunteResena, RepositorioUsuarioApunte repositorioUsuarioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte) {
        this.repositorioUsuarioApunteResena = repositorioUsuarioApunteResena;
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;

    }

    @Override
    public boolean registrar(Usuario usuario, Apunte apunte, Resena resena) {

        if (repositorioUsuarioApunteResena.existeResenaConApunteYUsuario(usuario.getId(), apunte.getId())) {
            return false;
        } else {

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

    @Override
    public List<Apunte> obtenerMejoresApuntes(Long usuarioId) {
        List<Apunte> todosLosApuntes = servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuarioId);
        List<Apunte> mejoresApuntes = new ArrayList<>();

        for (Apunte apunte : todosLosApuntes) {
            double promedioPuntaje = calcularPromedioPuntajeResenas(apunte.getId());
            if (promedioPuntaje >= 4.0) {
                mejoresApuntes.add(apunte);
            }
        }

        return mejoresApuntes;
    }

    @Override
    public double calcularPromedioPuntajeResenasPorUsuario(Long usuarioId) {
        List<UsuarioApunte> usuarioApuntes = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuarioId);

        if (usuarioApuntes.isEmpty()) {
            return 0.0;
        }

        double totalPromedioPuntajeApuntes = 0.0;
        int totalApuntes = 0;

        for (UsuarioApunte usuarioApunte : usuarioApuntes) {
            if (usuarioApunte.getTipoDeAcceso()==TipoDeAcceso.EDITAR) {
                Apunte apunte = usuarioApunte.getApunte();
                if (apunte != null) {
                    totalPromedioPuntajeApuntes += calcularPromedioPuntajeResenas(apunte.getId());
                    totalApuntes++;
                }
            }
        }

        if (totalApuntes == 0) {
            return 0.0;
        }

        return totalPromedioPuntajeApuntes / totalApuntes;
    }
    @Override
    public List<Usuario> obtenerUsuariosDestacados(Long usuarioId) {
        List<Usuario> otrosUsuarios = servicioUsuario.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId);

        Map<Usuario, Double> usuariosConPromedios = new LinkedHashMap<>();

        for (Usuario otroUsuario : otrosUsuarios) {
            double promedioPuntaje = calcularPromedioPuntajeResenasPorUsuario(otroUsuario.getId());
            usuariosConPromedios.put(otroUsuario, promedioPuntaje);
        }

        List<Usuario> usuariosDestacados = new ArrayList<>(usuariosConPromedios.keySet());

        usuariosDestacados.sort((usuario1, usuario2) -> Double.compare(usuariosConPromedios.get(usuario2), usuariosConPromedios.get(usuario1)));

        int numeroUsuariosAMostrar = 6;
        return usuariosDestacados.subList(0, Math.min(numeroUsuariosAMostrar, usuariosDestacados.size()));
    }

    @Override
    public List<UsuarioApunte> obtenerApuntesComprados(Usuario usuario) {
        List<UsuarioApunte> apuntes = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuario.getId());
        List<UsuarioApunte> apuntesComprados = new ArrayList<>();

        for (UsuarioApunte apunte : apuntes) {
            if (apunte.getTipoDeAcceso() == TipoDeAcceso.LEER) {
                apuntesComprados.add(apunte);
            }
        }

        return apuntesComprados;
    }

    @Override
    public List<UsuarioApunte> obtenerApuntesCreados(Usuario usuario) {
        List<UsuarioApunte> apuntes = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuario.getId());
        List<UsuarioApunte> apuntesCreados = new ArrayList<>();

        for (UsuarioApunte apunte : apuntes) {
            if (apunte.getTipoDeAcceso() == TipoDeAcceso.EDITAR) {
                apuntesCreados.add(apunte);
            }
        }

        return apuntesCreados;
    }

    public List<UsuarioApunte> obtenerApuntesCreadosYVerSiPuedeComprar(Usuario usuario, Usuario usuarioActual) {
        List<UsuarioApunte> apuntesCreados = obtenerApuntesCreados(usuario);

        List<UsuarioApunte> apuntesComprados = obtenerApuntesComprados(usuarioActual);

        List<Long> idsApuntesComprados = new ArrayList<>();
        for (UsuarioApunte apunte : apuntesComprados) {
            idsApuntesComprados.add(apunte.getApunte().getId());
        }

        for (UsuarioApunte apunte : apuntesCreados) {
            Long apunteId = apunte.getApunte().getId();
            apunte.getApunte().setSePuedeComprar(!idsApuntesComprados.contains(apunteId));
        }

        return apuntesCreados;
    }

    @Override
    public boolean existeResena(Long idUsuario, Long idApunte) {
        return repositorioUsuarioApunteResena.existeResenaConApunteYUsuario(idUsuario, idApunte);
    }


}

