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
import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(path = "/misApuntes", method = RequestMethod.GET)
    public ModelAndView misApuntes() {
        ModelMap model = new ModelMap();

        //List<Apunte> resultApuntes = servicioApunte.getApuntesByProprietary(1234567890L);
        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();
        apunte1.setNombre("Guía TP - PW2");
        apunte1.setDescripcion("Guía de trabajos prácticos de PW2");
        apunte2.setNombre("Resumen - 1er Parcial BD1");
        apunte2.setDescripcion("Resumen para el primer parcial de BD1");
        List<Apunte> listHard = new ArrayList<>();
        listHard.add(apunte1);
        listHard.add(apunte2);
        listHard.add(apunte1);
        listHard.add(apunte2);
        listHard.add(apunte1);
        listHard.add(apunte2);


        model.put("apuntes", listHard);
        return new ModelAndView("misApuntes", model);
    }
}
