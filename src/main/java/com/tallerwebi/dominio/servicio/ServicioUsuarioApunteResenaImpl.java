package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.presentacion.DatosApunte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service("servicioUsuarioApunteResena")
@Transactional
public class ServicioUsuarioApunteResenaImpl implements ServicioUsuarioApunteResena {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResena;
    private RepositorioApunte repositorioApunte;
    private RepositorioUsuarioApunte repositorioUsuarioApunte;
    private ServicioApunte servicioApunte;
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunte servicioUsuarioApunte;

    @Autowired
    public ServicioUsuarioApunteResenaImpl(RepositorioUsuarioApunteResena repositorioUsuarioApunteResena, RepositorioApunte repositorioApunte, RepositorioUsuarioApunte repositorioUsuarioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte) {
        this.repositorioUsuarioApunteResena = repositorioUsuarioApunteResena;
        this.repositorioApunte = repositorioApunte;
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;

    }

    @Override
    public boolean registrarResena(Usuario usuario, Apunte apunte, Resena resena) {

        if (repositorioUsuarioApunteResena.existeResenaConApunteYUsuario(usuario.getId(), apunte.getId())) {
            return false;
        } else {

            UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
            resena.setCreated_at(new Date());
            usuario.setPuntos(usuario.getPuntos() + 3);
            servicioUsuario.actualizar(usuario);
            usuarioApunteResena.setResena(resena);
            usuarioApunteResena.setUsuario(usuario);
            usuarioApunteResena.setApunte(apunte);
            repositorioUsuarioApunteResena.guardar(usuarioApunteResena);
            dar100PuntosAlUsuarioPorBuenasResenas(apunte.getId());
            List<Resena> resenasCreadas = obtenerResenasPorIdDeUsuario(usuario.getId());
            if (resenasCreadas.size() % 5 == 0) {
                darPuntosAlUsuarioPorParticipacionContinua(usuario);
            }
            return true;
        }
    }

    @Override
    public void registrarApunte(DatosApunte datosApunte, Usuario usuario) {
        Apunte apunte = new Apunte(datosApunte.getPathArchivo(), datosApunte.getNombre(), datosApunte.getDescripcion(), datosApunte.getPrecio(), new Date(), new Date());
        UsuarioApunte usuarioApunte = new UsuarioApunte();
        usuarioApunte.setApunte(apunte);
        usuarioApunte.setUsuario(usuario);
        usuarioApunte.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        repositorioApunte.registrarApunte(apunte);
        repositorioUsuarioApunte.registrar(usuarioApunte);
        List<UsuarioApunte> apuntesCreados = obtenerApuntesCreados(usuario);
        if (apuntesCreados.size() % 5 == 0) {
            darPuntosAlUsuarioPorParticipacionContinua(usuario);
        }
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

    public void darPuntosAlUsuarioPorParticipacionContinua(Usuario usuario) {
        List<Resena> resenas = obtenerResenasPorIdDeUsuario(usuario.getId());
        List<UsuarioApunte> apuntes = obtenerApuntesCreados(usuario);

        int puntosApuntes = apuntes.size() / 5 * 25;

        int puntosResenas = resenas.size() / 5 * 25;

        int puntosTotales=puntosResenas+puntosApuntes;

        if (puntosTotales > 25) {
            puntosTotales = 25;
        }

        usuario.setPuntos(usuario.getPuntos() + puntosTotales);

        servicioUsuario.actualizar(usuario);
    }

    @Override
    public List<Resena> obtenerLista(Long idApunte) {
        return repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(idApunte);
    }
    @Override
    public List<Resena> obtenerResenasPorIdDeUsuario(Long idUsuario){
        return repositorioUsuarioApunteResena.obtenerResenasPorIdUsuario(idUsuario);
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

        Comparator<Apunte> porPromedioPuntaje = (apunte1, apunte2) -> {
            double promedioPuntaje1 = calcularPromedioPuntajeResenas(apunte1.getId());
            double promedioPuntaje2 = calcularPromedioPuntajeResenas(apunte2.getId());
            return Double.compare(promedioPuntaje2, promedioPuntaje1);
        };

        todosLosApuntes.sort(porPromedioPuntaje);

        for (Apunte apunte : todosLosApuntes) {
            double promedioPuntaje = calcularPromedioPuntajeResenas(apunte.getId());
            if (promedioPuntaje >= 4.0) {
                mejoresApuntes.add(apunte);
            }
        }

        mejoresApuntes = mejoresApuntes.subList(0, Math.min(mejoresApuntes.size(), 6));

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

        double promedioTotal=totalPromedioPuntajeApuntes / totalApuntes;
        return promedioTotal;
    }
    @Override
    public List<Usuario> obtenerUsuariosDestacados(Long usuarioId) {
        List<Usuario> otrosUsuarios = servicioUsuario.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId);

        Map<Usuario, Double> usuariosConPromedios = new LinkedHashMap<>();

        for (Usuario otroUsuario : otrosUsuarios) {
            double promedioPuntaje = calcularPromedioPuntajeResenasPorUsuario(otroUsuario.getId());
            if (promedioPuntaje >= 4.0) {
                usuariosConPromedios.put(otroUsuario, promedioPuntaje);
            }
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

    @Override
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

