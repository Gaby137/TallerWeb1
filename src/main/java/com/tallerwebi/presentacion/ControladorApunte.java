package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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
    public ModelAndView publicar(@Valid DatosApunte datosApunte, BindingResult result, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuario");

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("usuario", datosApunte);
            return new ModelAndView("altaApunte", model);
        } else {
            try {
                servicioUsuarioApunteResena.registrarApunte(datosApunte, usuario);
                return new ModelAndView("redirect:/misApuntes");
            } catch (Exception e) {
                ModelMap model = new ModelMap();
                model.put("usuario", datosApunte);
                model.put("error", e.getMessage());
                return new ModelAndView("altaApunte", model);
            }
        }

    }

    @RequestMapping(path = "/editarApunte/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();

        Apunte apunteAEditar = servicioApunte.obtenerPorId(id);

        if (apunteAEditar != null) {
            modelo.put("apunte", apunteAEditar);
            return new ModelAndView("editarApunte", modelo);
        } else {
            modelo.put("mensaje", "El apunte no se encontró");
            return new ModelAndView("editarApunte", modelo);
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

        servicioUsuarioApunte.eliminarApunte(id);

        return new ModelAndView("apunteEliminado", modelo);
    }
    @RequestMapping(path = "/misApuntes", method = RequestMethod.GET)
    public ModelAndView misApuntes(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Apunte> apuntesComprados = servicioUsuarioApunteResena.obtenerApuntesComprados(usuario);
        List<Apunte> apuntesCreados = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);


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
        Usuario usuarioVendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(id);

        model.put("apunte", apunte);
        model.put("usuarioVendedor", usuarioVendedor);

        List<Resena> resenas = servicioUsuarioApunteResena.obtenerLista(id);
        model.put("resenas", resenas);

        TipoDeAcceso tipoDeAcceso = servicioUsuarioApunte.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(usuario.getId(), apunte.getId());

        if (TipoDeAcceso.LEER.equals(tipoDeAcceso)) {
            model.put("tipoDeAcceso", true);
        } else {
            model.put("tipoDeAcceso", false);
        }

        if (TipoDeAcceso.LEER.equals(tipoDeAcceso) || TipoDeAcceso.EDITAR.equals(tipoDeAcceso)){
            model.put("pdfComprado", true);
        } else {
            model.put("pdfComprado", false);
        }

        boolean hayResena = servicioUsuarioApunteResena.existeResena(usuario.getId(), id);
        model.put("hayResena", hayResena);
        return new ModelAndView("apunte-detalle", model);
    }

    @RequestMapping(path = "/apuntesEnVenta", method = RequestMethod.GET)
    public ModelAndView apuntesDeOtrosUsuarios(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Apunte> todosLosApuntes = servicioUsuarioApunte.obtenerTodosLosApuntes(usuario.getId());
        List<Apunte> apuntesCreadosPorElUsuario = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);
        List<Apunte> apuntesCompradosPorElUsuario = servicioUsuarioApunteResena.obtenerApuntesComprados(usuario);


        model.put("apuntes", todosLosApuntes);
        model.put("apuntesCreados", apuntesCreadosPorElUsuario);
        model.put("apuntesComprados", apuntesCompradosPorElUsuario);
        model.put("title", "Apuntes");
        return new ModelAndView("apuntesEnVenta", model);
    }

    @RequestMapping(path = "/perfilUsuario/{id}", method = RequestMethod.GET)
    public ModelAndView verPerfilUsuario(@PathVariable("id") Long id, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");

        Usuario usuario = servicioUsuario.obtenerPorId(id);

        List<Apunte> apuntesCreados = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);
        List<Apunte> apuntesCompradosPorUsuarioActual = servicioUsuarioApunteResena.obtenerApuntesComprados(usuarioActual);

        model.put("apuntesCreados", apuntesCreados);
        model.put("apuntesCompradosPorUsuarioActual", apuntesCompradosPorUsuarioActual);
        model.put("usuarioActual", usuarioActual);
        model.put("usuario", usuario);
        return new ModelAndView("perfilUsuario", model);
    }


    @RequestMapping(path = "/comprarApunte/{id}", method = RequestMethod.GET)
    public ModelAndView comprarApunte(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario comprador = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);

        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());

        boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

        if (compraExitosa) {
            return getDetalleApunteConListadoDeSusResenas(id, request, session);
        } else {
            model.put("error", "Error al realizar la compra");
            return new ModelAndView("apuntesEnVenta", model);
        }
    }

    @RequestMapping(path = "/comprarApuntePorPerfil/{id}", method = RequestMethod.GET)
    public ModelAndView comprarApuntePorPerfil(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario comprador = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);

        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());

        boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

        if (compraExitosa) {
            return getDetalleApunteConListadoDeSusResenas(id, request, session);
        } else {
            model.put("error", "Error al realizar la compra");
            return new ModelAndView("perfilUsuario", model);
        }
    }
    @RequestMapping(path = "/comprarApunteEnElHome/{id}", method = RequestMethod.GET)
    public ModelAndView comprarApunteEnElHome(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelMap model = new ModelMap();

        Usuario comprador = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);

        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());

        boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

        if (compraExitosa) {
            return getDetalleApunteConListadoDeSusResenas(id, request, session);
        } else {
            redirectAttributes.addFlashAttribute("error", "No fue posible comprar el apunte");
            return new ModelAndView("redirect:/home");
        }
    }

}
