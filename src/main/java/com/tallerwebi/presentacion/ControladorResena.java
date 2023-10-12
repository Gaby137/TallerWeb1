package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioResena;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorResena {

    private ServicioResena servicioResena;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorResena(ServicioResena servicioResena, ServicioUsuario servicioUsuario) {
        this.servicioResena = servicioResena;
        this.servicioUsuario=servicioUsuario;
    }

    @RequestMapping(path = "/formulario-alta-resena", method = RequestMethod.GET)
    public ModelAndView irAFormularioAlta(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("usuario");
        model.put("usuario", usuario);
        model.put("resena", new Resena());
        return new ModelAndView("formulario-alta-resena", model);
    }

    @RequestMapping(path = "/guardarResena", method = RequestMethod.POST)
    public ModelAndView guardarResena(@ModelAttribute("resena") Resena resena) {
        ModelMap model = new ModelMap();

        if (resena != null) {
            UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();

            usuarioApunteResena.setResena(resena);
            resena.setUsuarioResenaApunte(usuarioApunteResena);

            Usuario usuario = usuarioApunteResena.getUsuario();

            if (usuario != null) {

                usuario.setPuntos(usuario.getPuntos() + 10);

                servicioUsuario.actualizar(usuario);

                servicioResena.guardar(resena);

                return listarResenas();
            } else {
                model.put("mensaje", "Usuario asociado a la reseña es nulo");
            }
        } else {
            model.put("mensaje", "Reseña es nula");
        }

        return new ModelAndView("formulario-alta-resena", model);
    }
        @RequestMapping(path = "/apunte-detalle", method = RequestMethod.GET)
    public ModelAndView listarResenas() {
        ModelMap model = new ModelMap();
        List<Resena> resenas = servicioResena.listar();
        model.put("resenas", resenas);
        return new ModelAndView("apunte-detalle", model);
    }

    @RequestMapping(path = "/borrarResena/{id}", method = RequestMethod.GET)
    public ModelAndView borrar(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();
        try {
            servicioResena.borrar(id);
            modelo.put("mensaje", "Reseña borrada exitosamente");

        } catch (Exception e) {
            modelo.put("error", "Error al intentar borrar la reseña");
        }
        return new ModelAndView("redirect:/apunte-detalle", modelo);
    }

}

