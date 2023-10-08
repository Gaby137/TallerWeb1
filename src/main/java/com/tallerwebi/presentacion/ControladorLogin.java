package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicio.ServicioLogin;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
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
    public ModelAndView registrarme(@Valid Usuario usuario, BindingResult result) {

        if (result.hasErrors()) {
            ModelAndView model = nuevoUsuario();
            model.addObject("usuario", usuario);
            return model;
        } else {
            ModelAndView successModelAndView = new ModelAndView("redirect:/login");
            try {
                servicioLogin.registrar(usuario);
            } catch (UsuarioExistente e) {
                // En caso de un usuario existente, puedes agregar un mensaje de error al modelo y redirigir nuevamente al formulario
                ModelAndView model = nuevoUsuario(); // Reutiliza el método nuevoUsuario()
                model.addObject("usuario", usuario);
                model.addObject("error", "El usuario ya existe");
                return model;
            } catch (Exception e) {
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
        model.put("usuario", new Usuario());
        return new ModelAndView("nuevo-usuario", model);
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView irAHome() {
        return new ModelAndView("home");
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }


}