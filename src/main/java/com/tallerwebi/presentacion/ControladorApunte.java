package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.ApunteYaCompradoException;
import com.tallerwebi.dominio.excepcion.PuntosInsuficientesException;
import com.tallerwebi.dominio.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ControladorApunte {
    private ServicioApunte servicioApunte;
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;
    private ServicioAdministrador servicioAdministrador;
    private ControladorLogin controladorLogin;

    @Autowired
    public ControladorApunte(ServicioApunte servicioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuarioApunteResena servicioUsuarioApunteResena, ServicioUsuario servicioUsuario, ServicioAdministrador servicioAdministrador, ControladorLogin controladorLogin){
        this.servicioApunte = servicioApunte;
        this.servicioUsuario = servicioUsuario;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;
        this.servicioAdministrador = servicioAdministrador;
        this.controladorLogin = controladorLogin;
    }

    @RequestMapping(path = "/formulario-alta-apunte", method = RequestMethod.GET)
    public ModelAndView apunte() {
        ModelMap model = new ModelMap();
        List<Carrera> listadoCarrera = servicioAdministrador.listadoCarreras();
        model.put("listaCarreras", listadoCarrera);
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

    @RequestMapping(path = "/eliminarApunte/{id}", method = RequestMethod.POST)
    public ModelAndView eliminar(@PathVariable("id") Long id, HttpSession session) {
        ModelMap modelo = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (servicioUsuarioApunte.existeRelacionUsuarioApunteEditar(usuario.getId(), id)) {
            servicioUsuarioApunte.eliminarApunte(id);
            return new ModelAndView("apunteEliminado", modelo);
        }else{
            modelo.put("error", "El usuario esta intentando borrar un apunte que no le pertenece.");
        }
        return new ModelAndView("redirect:/detalleApunte/" + id, modelo);

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

        List<Resena> resenas = servicioUsuarioApunteResena.obtenerListaDeResenasPorIdApunte(id);
        model.put("resenas", resenas);

        List<Resena> resenasDelUsuarioActual = servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuario(usuario.getId());
        model.put("resenasDelUsuarioActual", resenasDelUsuarioActual);

        List<Apunte> apuntesCompradosPorElUsuario = servicioUsuarioApunteResena.obtenerApuntesComprados(usuario);
        model.put("apuntesCompradosPorUsuarioActual", apuntesCompradosPorElUsuario);

        List<Apunte> apuntesCreadosPorUsuarioActual = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);
        model.put("apuntesCreadosPorUsuarioActual", apuntesCreadosPorUsuarioActual);

        double promedioPuntajeResenas = servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(id);
        model.put("promedioDeResenas", promedioPuntajeResenas);

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
        List<Apunte> apuntesCreadosPorUsuarioActual = servicioUsuarioApunteResena.obtenerApuntesCreados(usuarioActual);

        model.put("apuntesCreados", apuntesCreados);
        model.put("apuntesCompradosPorUsuarioActual", apuntesCompradosPorUsuarioActual);
        model.put("apuntesCreadosPorUsuarioActual", apuntesCreadosPorUsuarioActual);
        model.put("usuarioActual", usuarioActual);
        model.put("usuario", usuario);
        return new ModelAndView("perfilUsuario", model);
    }


    @RequestMapping(path = "/comprarApunte/{id}", method = RequestMethod.POST)
    public ModelAndView comprarApunte(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session) throws PuntosInsuficientesException, ApunteYaCompradoException {
        ModelMap model = new ModelMap();

        Usuario comprador = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);

        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());


        try {
            boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

            if (compraExitosa) {
                return getDetalleApunteConListadoDeSusResenas(id, request, session);
            } else {
                ModelAndView apuntesEnVenta = apuntesDeOtrosUsuarios(session);
                apuntesEnVenta.getModelMap().put("error", "Error al realizar la compra");
                return apuntesEnVenta;
            }
        } catch (ApunteYaCompradoException e){
            ModelAndView apuntesEnVenta = apuntesDeOtrosUsuarios(session);
            apuntesEnVenta.getModelMap().put("error", "Ya tenes este apunte comprado");
            return apuntesEnVenta;

        } catch (PuntosInsuficientesException e){
            ModelAndView apuntesEnVenta = apuntesDeOtrosUsuarios(session);
            apuntesEnVenta.getModelMap().put("error", "Puntos insuficientes para adquirir el apunte");
            return apuntesEnVenta;

        } catch (Exception e){
            ModelAndView apuntesEnVenta = apuntesDeOtrosUsuarios(session);
            apuntesEnVenta.getModelMap().put("error", "Error inesperado al intentar comprar el apunte");
            return apuntesEnVenta;
        }
    }

    @RequestMapping(path = "/comprarApunteEnDetalleApunte/{id}", method = RequestMethod.POST)
    public ModelAndView comprarApunteEnDetalleApunte(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session) throws PuntosInsuficientesException, ApunteYaCompradoException {
        ModelMap model = new ModelMap();

        Usuario comprador = (Usuario) session.getAttribute("usuario");

        Apunte apunte = servicioApunte.obtenerPorId(id);

        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());

        try {
            boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

            if (compraExitosa) {
                return getDetalleApunteConListadoDeSusResenas(id, request, session);
            } else {
                ModelAndView detalleApunte = getDetalleApunteConListadoDeSusResenas(apunte.getId(), request, session);
                detalleApunte.getModelMap().put("error", "Error al realizar la compra");
                return detalleApunte;
            }
        }catch (ApunteYaCompradoException e){
            ModelAndView detalleApunte = getDetalleApunteConListadoDeSusResenas(apunte.getId(), request, session);
            detalleApunte.getModelMap().put("error", "Ya tenes este apunte comprado");
            return detalleApunte;
        }
        catch (PuntosInsuficientesException e){
            ModelAndView detalleApunte = getDetalleApunteConListadoDeSusResenas(apunte.getId(), request, session);
            detalleApunte.getModelMap().put("error", "Puntos insuficientes para adquirir el apunte");
            return detalleApunte;

        } catch (Exception e){
            ModelAndView detalleApunte = getDetalleApunteConListadoDeSusResenas(apunte.getId(), request, session);
            detalleApunte.getModelMap().put("error", "Error inesperado al intentar comprar el apunte");
            return detalleApunte;
        }
    }

    @RequestMapping(path = "/comprarApuntePorPerfil/{id}", method = RequestMethod.POST)
    public ModelAndView comprarApuntePorPerfil(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario comprador = (Usuario) session.getAttribute("usuario");
        Apunte apunte = servicioApunte.obtenerPorId(id);
        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());

        try {
            boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

            if (compraExitosa) {
                return getDetalleApunteConListadoDeSusResenas(id, request, session);
            } else {
                ModelAndView perfilUsuarioView = verPerfilUsuario(vendedor.getId(), session);
                perfilUsuarioView.getModelMap().put("error", "Error al realizar la compra");
                return perfilUsuarioView;
            }

        } catch (ApunteYaCompradoException e){
            ModelAndView perfilUsuarioView = verPerfilUsuario(vendedor.getId(), session);
            perfilUsuarioView.getModelMap().put("error", "Ya tenes este apunte comprado");
            return perfilUsuarioView;

        } catch (PuntosInsuficientesException e){
            ModelAndView perfilUsuarioView = verPerfilUsuario(vendedor.getId(), session);
            perfilUsuarioView.getModelMap().put("error", "Puntos insuficientes para adquirir el apunte");
            return perfilUsuarioView;

        } catch (Exception e){
            ModelAndView perfilUsuarioView = verPerfilUsuario(vendedor.getId(), session);
            perfilUsuarioView.getModelMap().put("error", "Error inesperado al intentar comprar el apunte");
            return perfilUsuarioView;
        }

    }
    @RequestMapping(path = "/comprarApunteEnElHome/{id}", method = RequestMethod.POST)
    public ModelAndView comprarApunteEnElHome(@PathVariable("id") Long id, HttpServletRequest request, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario comprador = (Usuario) session.getAttribute("usuario");
        Apunte apunte = servicioApunte.obtenerPorId(id);
        Usuario vendedor = servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId());

        try {

            boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

            if (compraExitosa) {
                return getDetalleApunteConListadoDeSusResenas(id, request, session);
            } else {
                ModelAndView homeView = controladorLogin.home(session);
                homeView.getModelMap().put("error", "Error al realizar la compra");
                return homeView;
            }
        } catch (PuntosInsuficientesException e) {
            ModelAndView homeView = controladorLogin.home(session);
            homeView.getModelMap().put("error", "Puntos insuficientes para comprar el apunte");
            return homeView;

        } catch (ApunteYaCompradoException e) {
            ModelAndView homeView = controladorLogin.home(session);
            homeView.getModelMap().put("error", "Ya teenes este apunte comprado");
            return homeView;

        } catch (Exception e) {
            ModelAndView homeView = controladorLogin.home(session);
            homeView.getModelMap().put("error", "Error inesperado al realizar la compra");
            return homeView;
        }
    }

}
