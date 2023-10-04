package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorApunte {
    private ServicioApunte servicioApunte;

    @Autowired
    public ControladorApunte(ServicioApunte servicioApunte){
        this.servicioApunte = servicioApunte;
    }

    @RequestMapping(path = "/formulario-alta-apunte", method = RequestMethod.GET)
    public ModelAndView apunte() {
        ModelMap model = new ModelMap();
        model.put("datosApunte", new DatosApunte());
        return new ModelAndView("altaApunte", model);
    }

    @RequestMapping(path = "/subirApunte", method = RequestMethod.POST)
    public ModelAndView publicar(@ModelAttribute("datosApunte") DatosApunte datosApunte) {

        if( servicioApunte.registrar(datosApunte)){
            return misApuntes();
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
            return misApuntes();
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
    public ModelAndView misApuntes() {
        ModelMap model = new ModelMap();

        List<Apunte> resultApuntes = servicioApunte.obtenerApuntes();

        model.put("apuntes", resultApuntes);
        return new ModelAndView("misApuntes", model);
    }
}
