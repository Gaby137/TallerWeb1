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
import java.security.SecureRandom;
import java.util.Date;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario servicioLoginDao;
    private ServicioUsuario servicioUsuario;

    @Value("${uploadDir}")
    private String uploadDir;


    @Autowired
    public ServicioLoginImpl(RepositorioUsuario servicioLoginDao, ServicioUsuario servicioUsuario) {
        this.servicioLoginDao = servicioLoginDao;
        this.servicioUsuario = servicioUsuario;
    }

    @Override
    public Usuario consultarUsuario(String email, String password) {
        return servicioLoginDao.buscarUsuario(email, password);
    }

    @Override
    public void registrar(DatosRegistro usuario) throws UsuarioExistente, IOException {
        Usuario usuarioEncontrado = servicioLoginDao.buscarUsuario(usuario.getEmail());
        if (usuarioEncontrado != null) {
            throw new UsuarioExistente();
        }

        File uploadDirectory = new File(uploadDir);
        if (uploadDirectory.exists()) {

            File imageFile = new File(uploadDirectory, usuario.getFotoPerfil().getOriginalFilename());
            usuario.getFotoPerfil().transferTo(imageFile);

            if (usuario.getFotoPerfil().getOriginalFilename() != null) {
                Usuario u1 = new Usuario(usuario.getNombre(), usuario.getApellido(), 100, usuario.getEmail(), usuario.getPassword(), Rol.USUARIO, false, usuario.getFotoPerfil().getOriginalFilename(), usuario.getLatitud(), usuario.getLongitud(), usuario.getCodigoDeCreador(), new Date(), new Date());
                String codigoCreador = generarCodigoCreador();
                u1.setCodigoDeCreador(codigoCreador);
                servicioLoginDao.guardar(u1);

                if (usuario.getCodigoDeCreador() != null && !usuario.getCodigoDeCreador().isEmpty()) {
                    if (servicioUsuario.existeCodigoCreadorEnLaBaseDeDatos(usuario.getCodigoDeCreador())) {
                        Usuario usuarioDelCodigo = servicioUsuario.buscarUsuarioPorCodigoDeCreador(usuario.getCodigoDeCreador());

                        usuarioDelCodigo.setPuntos(usuarioDelCodigo.getPuntos()+50);
                        servicioUsuario.actualizar(usuarioDelCodigo);

                        u1.setPuntos(u1.getPuntos()+50);
                        servicioUsuario.actualizar(u1);
                    } else {
                        throw new RuntimeException("El código de creador no corresponde a ningún usuario");
                    }
                }
            }
            }

    }

    @Override
    public String generarCodigoCreador() {
        String CARACTERES_VALIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int LONGITUD_CODIGO = 6;

        SecureRandom random = new SecureRandom();
        String nuevoCodigoCreador;

        do {
            StringBuilder codigoCreador = new StringBuilder();
            for (int i = 0; i < LONGITUD_CODIGO; i++) {
                int indiceCaracter = random.nextInt(CARACTERES_VALIDOS.length());
                char caracter = CARACTERES_VALIDOS.charAt(indiceCaracter);
                codigoCreador.append(caracter);
            }
            nuevoCodigoCreador = codigoCreador.toString();
        } while (servicioUsuario.existeCodigoCreadorEnLaBaseDeDatos(nuevoCodigoCreador));

        return nuevoCodigoCreador;
}
}


