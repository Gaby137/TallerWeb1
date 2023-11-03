package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Rol;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario servicioLoginDao;

    @Value("${uploadDir}")
    private String uploadDir;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario servicioLoginDao){
        this.servicioLoginDao = servicioLoginDao;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return servicioLoginDao.buscarUsuario(email, password);
    }

    @Override
    public void registrar(DatosRegistro usuario) throws UsuarioExistente, IOException {
        Usuario usuarioEncontrado = servicioLoginDao.buscarUsuario(usuario.getEmail());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

        File uploadDirectory = new File(uploadDir);
        if (uploadDirectory.exists()) {

            File imageFile = new File(uploadDirectory, usuario.getFotoPerfil().getOriginalFilename());
            usuario.getFotoPerfil().transferTo(imageFile);

            if (usuario.getFotoPerfil().getOriginalFilename() !=null ){
                Usuario u1 = new Usuario(usuario.getNombre(),usuario.getApellido(),100, usuario.getEmail(),usuario.getPassword(),Rol.USUARIO,false, usuario.getFotoPerfil().getOriginalFilename(), usuario.getLatitud(), usuario.getLongitud(), new Date(),new Date());
                servicioLoginDao.guardar(u1);
            }

        }

    }

}


