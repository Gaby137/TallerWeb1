package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ControladorResena {

    private ServicioResena servicioResena;
    private ServicioUsuario servicioUsuario;
    private ServicioApunte servicioApunte;
    private HttpSession session;


    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;

    @Autowired
    public ControladorResena(ServicioResena servicioResena, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuarioApunteResena servicioUsuarioApunteResena, HttpSession session) {
        this.servicioResena = servicioResena;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;
        this.session = session;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;
    }


    @PostMapping("/guardarResena")
    public ResponseEntity guardarResena(@RequestParam String descripcion, @RequestParam String cantidadDeEstrellas) {
        if(descripcion.isBlank()){
            return new ResponseEntity<>("Campo Comentario no puede estar vacio", HttpStatus.BAD_REQUEST);
        }

        if(cantidadDeEstrellas.isBlank()){
            return new ResponseEntity<>("Campo Valoración no puede estar vacio", HttpStatus.BAD_REQUEST);
        } else {
            try {
                Integer valoracion = Integer.parseInt(cantidadDeEstrellas);
                if(valoracion < 1 || valoracion > 5){
                    return new ResponseEntity<>("Campo Valoración tiene que ser un numero entre 1 y 5", HttpStatus.BAD_REQUEST);        
                }
            } catch (NumberFormatException e) {
                return new ResponseEntity<>("Campo Valoración debe ser un número", HttpStatus.BAD_REQUEST);
            }
        }
        
        try {
            Long id = (Long) this.session.getAttribute("idApunte");
            Usuario usuario=(Usuario) this.session.getAttribute("usuario");
            Apunte apunte = this.servicioApunte.obtenerPorId(id);
            Resena resena = new Resena();
            resena.setDescripcion(descripcion);
            resena.setCantidadDeEstrellas(Integer.parseInt(cantidadDeEstrellas));

            if (this.servicioUsuarioApunteResena.registrarResena(usuario, apunte, resena)) {
                return new ResponseEntity<>(resena, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("No deberia nunca devolver esto", HttpStatus.BAD_REQUEST);

    }

    @RequestMapping(path = "/borrarResena/{id}", method = RequestMethod.POST)
    public ModelAndView borrar(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelMap modelo = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Long idApunte = (Long) session.getAttribute("idApunte");

        if (session.getAttribute("usuario") != null){
            Resena resenaExistente = servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuarioYApunte(usuario.getId(), idApunte);

            if (resenaExistente != null && resenaExistente.getId().equals(id)) {
                servicioResena.borrar(id);
                modelo.put("status", "success");
            } else {
                modelo.put("status", "error");
                modelo.put("error", "El usuario no tiene una resena para este apunte o esta intentando borrar una resena que no le pertenece.");
                redirectAttributes.addFlashAttribute("modelo", modelo);
            }

            return new ModelAndView("redirect:/detalleApunte/" + idApunte, modelo);
        }else{
            return new ModelAndView("redirect:/login");
        }

    }

}



