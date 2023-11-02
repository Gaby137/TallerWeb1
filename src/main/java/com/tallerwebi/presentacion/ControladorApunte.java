package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Controller
public class ControladorApunte {
    private ServicioApunte servicioApunte;
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;

    @Autowired
    public ControladorApunte(ServicioApunte servicioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuarioApunteResena servicioUsuarioApunteResena, ServicioUsuario servicioUsuario){
        this.servicioApunte = servicioApunte;
        this.servicioUsuario = servicioUsuario;
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

        servicioApunte.eliminar(id);

        return new ModelAndView("apunteEliminado", modelo);
    }
    @RequestMapping(path = "/misApuntes", method = RequestMethod.GET)
    public ModelAndView misApuntes(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<UsuarioApunte> apuntesComprados = servicioUsuarioApunteResena.obtenerApuntesComprados(usuario);
        List<UsuarioApunte> apuntesCreados = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);


        model.put("apuntesComprados", apuntesComprados);
        model.put("apuntesCreados", apuntesCreados);
        model.put("title", "Mis Apuntes");
        return new ModelAndView("misApuntes", model);
    }
    @RequestMapping(path = "/detalleApunte/{id}", method = RequestMethod.GET)
    public ModelAndView getDetalleApunteConListadoDeSusResenas(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);
        request.getSession().setAttribute("idApunte", apunte.getId());

        model.put("apunte", apunte);

        List<Resena> resenas = servicioUsuarioApunteResena.obtenerLista(id);
        model.put("resenas", resenas);

        TipoDeAcceso tipoDeAcceso = servicioUsuarioApunte.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(usuario.getId(), apunte.getId());
        model.put("tipoDeAcceso", tipoDeAcceso);

        return new ModelAndView("apunte-detalle", model);
    }

    @RequestMapping(path = "/apuntesEnVenta", method = RequestMethod.GET)
    public ModelAndView apuntesDeOtrosUsuarios(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Apunte> apuntesDeOtrosUsuarios = servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuario.getId());

        model.put("apuntes", apuntesDeOtrosUsuarios);
        model.put("title", "Apuntes");
        model.put("puntos", "Usted tiene " + usuario.getPuntos() + " puntos");
        return new ModelAndView("apuntesEnVenta", model);
    }

    @RequestMapping(path = "/perfilUsuario/{id}", method = RequestMethod.GET)
    public ModelAndView verPerfilUsuario(@PathVariable("id") Long id, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");

        Usuario usuario = servicioUsuario.obtenerPorId(id);

        List<UsuarioApunte> apuntesCreados = servicioUsuarioApunteResena.obtenerApuntesCreadosYVerSiPuedeComprar(usuario, usuarioActual);

        model.put("apuntesCreados", apuntesCreados);
        model.put("usuarioActual", usuarioActual);
        model.put("usuario", usuario);
        return new ModelAndView("perfilUsuario", model);
    }

    @RequestMapping(path = "/comprarApunte/{id}", method = RequestMethod.GET)
    public ModelAndView comprarApunte(@PathVariable("id") Long id, HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario comprador = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);

        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());

        boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

        if (compraExitosa) {
            model.put("mensaje", "Compra exitosa");
            model.put("puntos", "Usted tiene " + comprador.getPuntos() + " puntos");

            List<Apunte> apuntesDeOtrosUsuarios = servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(comprador.getId());
            model.put("apuntes", apuntesDeOtrosUsuarios);

            return new ModelAndView("apuntesEnVenta", model);
        } else {
            model.put("error", "Error al realizar la compra");
            return new ModelAndView("apuntesEnVenta", model);
        }
    }
}
