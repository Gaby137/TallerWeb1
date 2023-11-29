package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import com.tallerwebi.dominio.servicio.ServicioLogin;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuario servicioUsuario;
    private ServicioApunte servicioApunte;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin, ServicioUsuarioApunteResena servicioUsuarioApunteResena, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte) {
        this.servicioLogin = servicioLogin;
        this.servicioUsuario = servicioUsuario;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioApunte = servicioApunte;
    }


    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("usuario", usuarioBuscado);
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            return new ModelAndView("redirect:/home");
        } else {
            model.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", model);
    }



    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@Valid DatosRegistro usuario, BindingResult result) {

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("usuario", usuario);
            return new ModelAndView("nuevo-usuario", model);
        } else {
            ModelAndView successModelAndView = new ModelAndView("redirect:/login");
            try {
                servicioLogin.registrar(usuario);
            }catch (RuntimeException e) {
                ModelAndView model = nuevoUsuario();
                model.addObject("usuario", usuario);
                model.addObject("error", "El código de creador no corresponde a ningún usuario");
                return model;
            }catch (UsuarioExistente e) {
                // En caso de un usuario existente, puedes agregar un mensaje de error al modelo y redirigir nuevamente al formulario
                ModelAndView model = nuevoUsuario(); // Reutiliza el método nuevoUsuario()
                model.addObject("usuario", usuario);
                model.addObject("error", "El usuario ya existe");
                return model;
            } catch (IOException e) {
                // En caso de otros errores, también puedes agregar un mensaje de error al modelo y redirigir nuevamente al formulario
                ModelAndView model = nuevoUsuario(); // Reutiliza el método nuevoUsuario()
                model.addObject("usuario", usuario);
                model.addObject("error", "Error al registrar el nuevo usuario");
                return model;
            }
            return successModelAndView;
        }
    }


    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("datosRegistro", new DatosRegistro());
        return new ModelAndView("nuevo-usuario", model);
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView home(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String errorAlComprarApunteDesdeElHome = (String) model.getAttribute("error");

        if (usuario != null) {
            List<Apunte> mejoresApuntes = servicioUsuarioApunteResena.obtenerMejoresApuntes(usuario.getId());
            List<Apunte> apuntesCompradosPorElUsuario = servicioUsuarioApunteResena.obtenerApuntesComprados(usuario);
            List<Apunte> apuntesCreadosPorElUsuario = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);
            List<Usuario> usuariosDestacados = servicioUsuarioApunteResena.obtenerUsuariosDestacados(usuario.getId());
            List<Apunte> apuntesNovedades = servicioApunte.obtenerApuntesNovedades();

            for (Apunte apunte : mejoresApuntes) {
                double promedioPuntajeResenas = servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte.getId());
                String promedioFormateado = String.format(Locale.US, "%.1f", promedioPuntajeResenas);
                apunte.setPromedioResenas(Double.parseDouble(promedioFormateado));
            }

            model.put("usuario", usuario);
            model.put("usuariosDestacados", usuariosDestacados);
            model.put("apuntesComprados", apuntesCompradosPorElUsuario);
            model.put("apuntesCreados", apuntesCreadosPorElUsuario);
            model.put("apuntes", mejoresApuntes);
            model.put("novedades", apuntesNovedades);
            model.put("title", "Apuntes Destacados");

            if (errorAlComprarApunteDesdeElHome != null) {
                model.addAttribute("error", errorAlComprarApunteDesdeElHome);
            }
            return new ModelAndView("home", model);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/cerrarSesion", method = RequestMethod.GET)
    public ModelAndView cerrarSesion(HttpSession session, HttpServletRequest request) {
        session.removeAttribute("usuario");
        session.removeAttribute("ROL");
        request.getSession().invalidate();

        return new ModelAndView("redirect:/login");
    }


}