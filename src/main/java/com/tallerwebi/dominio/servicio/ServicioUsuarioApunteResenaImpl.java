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
import java.util.List;



@Service("servicioUsuarioApunteResena")
@Transactional
public class ServicioUsuarioApunteResenaImpl implements ServicioUsuarioApunteResena {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResena;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ServicioUsuarioApunteResenaImpl(RepositorioUsuarioApunteResena repositorioUsuarioApunteResena){
        this.repositorioUsuarioApunteResena = repositorioUsuarioApunteResena;
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
            return true;

        }

    }

    @Override
    public List<Resena> obtenerLista(Long idApunte) {
        return repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(idApunte);
    }

    @Override
    public void eliminarRelacion(Long idResena, Long idApunte, Long idUsuario) {

        repositorioUsuarioApunteResena.eliminarRelacionDeUsuarioApunteResena(idResena, idApunte, idUsuario);
    }
}
