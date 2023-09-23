package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Apunte;
import com.tallerwebi.dominio.ServicioApunte;
import com.tallerwebi.dominio.excepcion.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.ValidationException;

@Controller
public class ControladorApunte {
    private ServicioApunte servicioApunte;

    @Autowired
    public ControladorApunte(ServicioApunte servicioApunte){
        this.servicioApunte = servicioApunte;
    }

    @RequestMapping(path = "/subirApunte", method = RequestMethod.POST)
    public ModelAndView publicar(@ModelAttribute("datosApunte") DatosApunte datosApunte) {
        ModelMap modelo = new ModelMap();
        if( servicioApunte.registrar(datosApunte)){
            return new ModelAndView("apuntePublicadoExito", modelo);
        } else {
            modelo.put("error", "Por favor complete todos los campos");
            return new ModelAndView("subirApunte", modelo);
        }


    }

    @RequestMapping(path = "/editarApunte/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();

        // Implementar la lógica para obtener el apunte que deseas editar
        Apunte apunteAEditar = servicioApunte.obtenerPorId(id);

        // Verificar si el apunte existe
        if (apunteAEditar != null) {
            modelo.addAttribute("apunte", apunteAEditar);
            return new ModelAndView("editarApunte", modelo);
        } else {
            modelo.addAttribute("mensaje", "El apunte no se encontró");
            return new ModelAndView("error", modelo);
        }
    }

    @RequestMapping(path = "/guardarEdicion", method = RequestMethod.POST)
    public ModelAndView guardarEdicion(@ModelAttribute("apunte") Apunte apunte) {
        ModelMap modelo = new ModelMap();

        // Implementar la lógica para guardar la edición del apunte
        servicioApunte.actualizar(apunte);

        modelo.addAttribute("mensaje", "Apunte editado exitosamente");
        return new ModelAndView("mensajeExito", modelo);
    }

    @RequestMapping(path = "/eliminarApunte", method = RequestMethod.POST)
    public ModelAndView eliminar(@ModelAttribute("apunte") Apunte apunte) {

        ModelMap modelo = new ModelMap();

        servicioApunte.eliminar(apunte);

        return new ModelAndView("apunteEliminado", modelo);
    }
}
