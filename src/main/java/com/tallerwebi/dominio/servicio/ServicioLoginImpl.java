package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Rol;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

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
    public void registrar(DatosRegistro usuario, MultipartFile fotoPerfil) throws UsuarioExistente, IOException {
        Usuario usuarioEncontrado = servicioLoginDao.buscarUsuario(usuario.getEmail());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

        File uploadDirectory = new File(uploadDir);
        if (uploadDirectory.exists()) {

            File imageFile = new File(uploadDirectory, fotoPerfil.getOriginalFilename());
            fotoPerfil.transferTo(imageFile);

            if (fotoPerfil.getOriginalFilename() !=null ){
                Usuario u1 = new Usuario(usuario.getNombre(),usuario.getApellido(),100, usuario.getEmail(),usuario.getPassword(),Rol.USUARIO,false, fotoPerfil.getOriginalFilename(),new Date(),new Date());
                servicioLoginDao.guardar(u1);
            }

        }

    }

    public String almacenarFotoDePerfil(MultipartFile fotoPerfil) throws IOException {

                // Obtén una ubicación de almacenamiento o un servicio de almacenamiento en la nube
                // y guarda la imagen allí.

                // Por ejemplo, si deseas guardarla en el sistema de archivos del servidor:
                String nombreArchivo = UUID.randomUUID().toString() + "_" + fotoPerfil.getOriginalFilename();
                String rutaAlmacenamiento = "/resources/core/img/";
                String rutaCompleta = rutaAlmacenamiento + nombreArchivo;

                Path rutaArchivo = Paths.get(rutaCompleta);
                Files.write(rutaArchivo, fotoPerfil.getBytes());

                // Devuelve la URL completa de la imagen para guardarla en la entidad Usuario
                return "/resources/core/img/" + nombreArchivo;

    }

}

