package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Controller
public class ControladorApunte {
    private ServicioApunte servicioApunte;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;

    @Autowired
    public ControladorApunte(ServicioApunte servicioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuarioApunteResena servicioUsuarioApunteResena){
        this.servicioApunte = servicioApunte;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;
    }

    @RequestMapping(path = "/formulario-alta-apunte", method = RequestMethod.GET)
    public ModelAndView apunte() {
        ModelMap model = new ModelMap();
        model.put("datosApunte", new DatosApunte());
        model.put("title", "Nuevo Apunte");
        return new ModelAndView("altaApunte", model);
    }

    @RequestMapping(path = "/subirApunte", method = RequestMethod.POST)
    public ModelAndView publicar(@ModelAttribute("datosApunte") DatosApunte datosApunte, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuario");
        if( servicioApunte.registrar(datosApunte, usuario)){
            return new ModelAndView("redirect:/misApuntes");
        } else {
            ModelMap model = new ModelMap();
            model.put("error", "Por favor complete todos los campos");
            return new ModelAndView("altaApunte", model);
        }
    }

    @RequestMapping(path = "/editarApunte/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();

        // Implementar la lógica para obtener el apunte que deseas editar
        Apunte apunteAEditar = servicioApunte.obtenerPorId(id);

        // Verificar si el apunte existe
        if (apunteAEditar != null) {
            modelo.put("apunte", apunteAEditar);
            return new ModelAndView("editarApunte", modelo);
        } else {
            modelo.put("mensaje", "El apunte no se encontró");
            return new ModelAndView("error", modelo);
        }
    }

    @RequestMapping(path = "/guardarEdicion", method = RequestMethod.POST)
    public ModelAndView guardarEdicion(@ModelAttribute("apunte") Apunte apunte) {

        if(servicioApunte.actualizar(apunte)){
            return new ModelAndView("redirect:/misApuntes");
        } else {
            ModelMap model = new ModelMap();
            model.put("error", "Por favor complete todos los campos");
            return new ModelAndView("editarApunte", model);
        }
    }

    @RequestMapping(path = "/eliminarApunte/{id}", method = RequestMethod.GET)
    public ModelAndView eliminar(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();

        // Implementar la lógica para obtener el apunte que deseas editar
        servicioApunte.eliminar(id);

        return new ModelAndView("apunteEliminado", modelo);
    }
    @RequestMapping(path = "/misApuntes", method = RequestMethod.GET)
    public ModelAndView misApuntes(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("usuario");

        //List<Apunte> resultApuntes = servicioApunte.obtenerApuntes();
        List<Apunte> resultApuntes = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuario.getId());

        model.put("apuntes", resultApuntes);
        model.put("title", "Mis Apuntes");
        return new ModelAndView("misApuntes", model);
    }

    @RequestMapping(path = "/detalleApunte/{id}", method = RequestMethod.GET)
    public ModelAndView getDetalleApunteConListadoDeSusResenas(@PathVariable("id") Long id, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Apunte apunte = servicioApunte.obtenerPorId(id);
        request.getSession().setAttribute("idApunte", apunte.getId());

        model.put("apunte", apunte);

        List<Resena> resenas = servicioUsuarioApunteResena.obtenerLista(id);
        model.put("resenas", resenas);
        return new ModelAndView("apunte-detalle", model);
    }

    @RequestMapping(path = "/apuntes", method = RequestMethod.GET)
    public ModelAndView apuntesDeOtrosUsuarios(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Apunte> apuntesDeOtrosUsuarios = servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuario.getId());

        model.put("apuntes", apuntesDeOtrosUsuarios);
        model.put("title", "Apuntes");
        return new ModelAndView("apuntes", model);
    }

    @RequestMapping(path = "/comprarApunte/{id}", method = RequestMethod.GET)
    public ModelAndView comprarApunte(@PathVariable("id") Long id, HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);

        boolean compraExitosa = servicioUsuarioApunte.comprarApunte(usuario, apunte);

        if (compraExitosa) {
            model.put("mensaje", "Compra exitosa");

            List<Apunte> apuntesDeOtrosUsuarios = servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuario.getId());
            model.put("apuntes", apuntesDeOtrosUsuarios);

            return new ModelAndView("apuntes", model);
        } else {
            model.put("error", "Error al realizar la compra");
            return new ModelAndView("apuntes", model);
        }
    }
}
