package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.presentacion.DatosApunte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.io.File;
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
    private ServicioAdministrador servicioAdministrador;

    @Autowired
    public ServicioUsuarioApunteResenaImpl(RepositorioUsuarioApunteResena repositorioUsuarioApunteResena, RepositorioApunte repositorioApunte, RepositorioUsuarioApunte repositorioUsuarioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte, ServicioAdministrador servicioAdministrador) {
        this.repositorioUsuarioApunteResena = repositorioUsuarioApunteResena;
        this.repositorioApunte = repositorioApunte;
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;
        this.servicioAdministrador = servicioAdministrador;

    }

    @Override
    public boolean registrarResena(Usuario usuario, Apunte apunte, Resena resena) {

        if (this.existeResena(usuario.getId(), apunte.getId())) {
            return false;
        } else {

            UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
            resena.setCreated_at(new Date());
            servicioUsuario.actualizar(usuario);
            usuarioApunteResena.setResena(resena);
            usuarioApunteResena.setUsuario(usuario);
            usuarioApunteResena.setApunte(apunte);
            repositorioUsuarioApunteResena.guardar(usuarioApunteResena);
            dar100PuntosAlUsuarioPorBuenasResenas(apunte.getId());
            darPuntosAlUsuarioPorParticipacionContinua(usuario);

        }
        return true;
    }

    @Override
    public void registrarApunte(DatosApunte datosApunte, Usuario usuario) {
        File uploadDirectory = new File("src/main/webapp/resources/core/pdf/");
        if (uploadDirectory.exists()) {
            if (datosApunte.getPathArchivo() != null){
                File pdfFile = new File(uploadDirectory, datosApunte.getPathArchivo().getOriginalFilename());
                try {
                    datosApunte.getPathArchivo().transferTo(pdfFile);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                Apunte apunte = new Apunte(datosApunte.getPathArchivo().getOriginalFilename(), datosApunte.getNombre(), datosApunte.getDescripcion(), datosApunte.getPrecio(), new Date(), new Date());
                Materia materia = servicioAdministrador.obtenerMateria(datosApunte.getIdMateria());
                apunte.setMateria(materia);
                UsuarioApunte usuarioApunte = new UsuarioApunte();
                usuarioApunte.setApunte(apunte);
                usuarioApunte.setUsuario(usuario);
                usuarioApunte.setTipoDeAcceso(TipoDeAcceso.EDITAR);
                apunte.setActivo(true);
                repositorioApunte.registrarApunte(apunte);
                repositorioUsuarioApunte.registrar(usuarioApunte);
                darPuntosAlUsuarioPorParticipacionContinua(usuario);

            }
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
    @Override
    @Transactional
    public void darPuntosAlUsuarioPorParticipacionContinua(Usuario usuario) {
        List<Resena> resenas = obtenerResenasPorIdDeUsuario(usuario.getId());
        List<Apunte> apuntes = obtenerApuntesCreados(usuario);

        boolean flag10Resenas = usuario.getFlagsDeParticipacionContinua().contains("FLAG_10_RESENAS");
        boolean flag20Resenas = usuario.getFlagsDeParticipacionContinua().contains("FLAG_20_RESENAS");
        boolean flag30Resenas = usuario.getFlagsDeParticipacionContinua().contains("FLAG_30_RESENAS");
        boolean flag40Resenas = usuario.getFlagsDeParticipacionContinua().contains("FLAG_40_RESENAS");
        boolean flag50Resenas = usuario.getFlagsDeParticipacionContinua().contains("FLAG_50_RESENAS");
        boolean flag100Resenas = usuario.getFlagsDeParticipacionContinua().contains("FLAG_100_RESENAS");

        if (resenas.size() == 10 && !flag10Resenas) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_10_RESENAS");
            usuario.setPuntos(usuario.getPuntos() + 25);
        }

        if (resenas.size() == 20 && !flag20Resenas) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_20_RESENAS");
            usuario.setPuntos(usuario.getPuntos() + 30);
        }

        if (resenas.size() == 30 && !flag30Resenas) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_30_RESENAS");
            usuario.setPuntos(usuario.getPuntos() + 35);
        }

        if (resenas.size() == 40 && !flag40Resenas) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_40_RESENAS");
            usuario.setPuntos(usuario.getPuntos() + 40);
        }

        if (resenas.size() == 50 && !flag50Resenas) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_50_RESENAS");
            usuario.setPuntos(usuario.getPuntos() + 50);
        }
        if (resenas.size() == 100 && !flag100Resenas) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_100_RESENAS");
            usuario.setPuntos(usuario.getPuntos() + 100);
        }

        boolean flag10Apuntes = usuario.getFlagsDeParticipacionContinua().contains("FLAG_10_APUNTES");
        boolean flag20Apuntes = usuario.getFlagsDeParticipacionContinua().contains("FLAG_20_APUNTES");
        boolean flag30Apuntes = usuario.getFlagsDeParticipacionContinua().contains("FLAG_30_APUNTES");
        boolean flag40Apuntes = usuario.getFlagsDeParticipacionContinua().contains("FLAG_40_APUNTES");
        boolean flag50Apuntes = usuario.getFlagsDeParticipacionContinua().contains("FLAG_50_APUNTES");
        boolean flag100Apuntes = usuario.getFlagsDeParticipacionContinua().contains("FLAG_100_APUNTES");

        if (apuntes.size() == 10 && !flag10Apuntes) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_10_APUNTES");
            usuario.setPuntos(usuario.getPuntos() + 35);
        }

        if (apuntes.size() == 20 && !flag20Apuntes) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_20_APUNTES");
            usuario.setPuntos(usuario.getPuntos() + 45);
        }

        if (apuntes.size() == 30 && !flag30Apuntes) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_30_APUNTES");
            usuario.setPuntos(usuario.getPuntos() + 55);
        }

        if (apuntes.size() == 40 && !flag40Apuntes) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_40_APUNTES");
            usuario.setPuntos(usuario.getPuntos() + 70);
        }

        if (apuntes.size() == 50 && !flag50Apuntes) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_50_APUNTES");
            usuario.setPuntos(usuario.getPuntos() + 100);
        }

        if (apuntes.size() == 100 && !flag100Apuntes) {
            usuario.getFlagsDeParticipacionContinua().add("FLAG_100_APUNTES");
            usuario.setPuntos(usuario.getPuntos() + 200);
        }

        servicioUsuario.actualizar(usuario);
    }


    @Override
    public List<Resena> obtenerListaDeResenasPorIdApunte(Long idApunte) {
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
        List<Apunte> todosLosApuntes = servicioUsuarioApunte.obtenerTodosLosApuntes(usuarioId);
        List<Apunte> mejoresApuntes = new ArrayList<>();

        Comparator<Apunte> porPromedioPuntaje = (apunte1, apunte2) -> {
            double promedioPuntaje1 = calcularPromedioPuntajeResenas(apunte1.getId());
            double promedioPuntaje2 = calcularPromedioPuntajeResenas(apunte2.getId());
            return Double.compare(promedioPuntaje2, promedioPuntaje1);
        };

        todosLosApuntes.sort(porPromedioPuntaje);

        for (Apunte apunte : todosLosApuntes) {
            if (apunte.isActivo()) {
                List<Resena> resenas = repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(apunte.getId());
                if (resenas.size() >= 5) {
                    double promedioPuntaje = calcularPromedioPuntajeResenas(apunte.getId());
                    if (promedioPuntaje >= 4.0) {
                        mejoresApuntes.add(apunte);
                    }
                }
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
                if (apunte != null && apunte.isActivo()) {
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
            List<Apunte> apuntes = obtenerApuntesCreados(otroUsuario);
            if (apuntes.size() >= 5) {
                double promedioPuntaje = calcularPromedioPuntajeResenasPorUsuario(otroUsuario.getId());
                if (promedioPuntaje >= 4.0) {
                    usuariosConPromedios.put(otroUsuario, promedioPuntaje);
                }
            }
        }

        List<Usuario> usuariosDestacados = new ArrayList<>(usuariosConPromedios.keySet());

        usuariosDestacados.sort((usuario1, usuario2) -> Double.compare(usuariosConPromedios.get(usuario2), usuariosConPromedios.get(usuario1)));

        return usuariosDestacados;
    }

    @Override
    public List<Apunte> obtenerApuntesComprados(Usuario usuario) {
        List<UsuarioApunte> apuntes = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuario.getId());
        List<Apunte> apuntesComprados = new ArrayList<>();

        for (UsuarioApunte apunte : apuntes) {
            if (apunte.getTipoDeAcceso() == TipoDeAcceso.LEER) {
                apuntesComprados.add(apunte.getApunte());
            }
        }

        return apuntesComprados;
    }

    @Override
    public List<Apunte> obtenerApuntesCreados(Usuario usuario) {
        List<UsuarioApunte> apuntes = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuario.getId());
        List<Apunte> apuntesCreados = new ArrayList<>();

        for (UsuarioApunte apunte : apuntes) {
            if (apunte.getTipoDeAcceso() == TipoDeAcceso.EDITAR) {
                apuntesCreados.add(apunte.getApunte());
            }
        }

        return apuntesCreados;
    }

    //SIN USO
    /*@Override
    public List<Apunte> obtenerApuntesCreadosYVerSiPuedeComprar(Usuario usuario, Usuario usuarioActual) {
        List<Apunte> apuntesCreados = obtenerApuntesCreados(usuario);

        List<Apunte> apuntesComprados = obtenerApuntesComprados(usuarioActual);

        List<Long> idsApuntesComprados = new ArrayList<>();
        for (Apunte apunte : apuntesComprados) {
            idsApuntesComprados.add(apunte.getId());
        }

        for (Apunte apunte : apuntesCreados) {
            Long apunteId = apunte.getId();
            apunte.setSePuedeComprar(!idsApuntesComprados.contains(apunteId));
        }

        return apuntesCreados;
    }*/

    @Override
    public boolean existeResena(Long idUsuario, Long idApunte) {
        return repositorioUsuarioApunteResena.existeResenaConApunteYUsuario(idUsuario, idApunte).size()>0;
    }

    @Override
    public Resena obtenerResenasPorIdDeUsuarioYApunte(Long idUsuario, Long idApunte){
        return repositorioUsuarioApunteResena.obtenerResenaPorIdUsuarioYApunte(idUsuario, idApunte);
    }

}

