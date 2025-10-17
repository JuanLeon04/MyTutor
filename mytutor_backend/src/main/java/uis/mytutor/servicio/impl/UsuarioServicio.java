package uis.mytutor.servicio.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uis.mytutor.dto.SolicitudRegistro;
import uis.mytutor.dto.UsuarioDTO;
import uis.mytutor.modelo.Usuario;
import uis.mytutor.repositorio.UsuarioRepositorio;
import uis.mytutor.servicio.interfaz.IUsuarioServicio;

import java.util.List;

@Service
@Transactional
public class UsuarioServicio implements IUsuarioServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Escencial para el DTO
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setFotoPerfil(usuario.getFotoPerfil());
        dto.setActivo(usuario.isActivo());
        return dto;
    }

    // Convertir la solicitud en un usuaro real con la contraseña cifrada
    public Usuario registroToUsuario(SolicitudRegistro solicitud) {
        if (solicitud == null) {
            return null;
        }
        Usuario usr = new Usuario();
        usr.setNombre(solicitud.getNombre());
        usr.setApellido(solicitud.getApellido());
        usr.setCorreo(solicitud.getCorreo());
        usr.setTelefono(solicitud.getTelefono());
        usr.setFotoPerfil(solicitud.getFotoPerfil());
        usr.setActivo(true);
        usr.setNombreUsuario(solicitud.getNombreUsuario());
        usr.setPassword(passwordEncoder.encode(solicitud.getPassword()));
        return usr;
    }

    // Listar todos los usuarios
    @Override
    public List<UsuarioDTO> getUsuarios() {
        return usuarioRepositorio.findAll()
                .stream()
                .map(UsuarioServicio::toDTO) // convierte cada Usuario en UsuarioDTO
                .toList();
    }

    // Obtener usuario por ID
    @Override
    public UsuarioDTO getUsuarioById(Long id){
        return toDTO(usuarioRepositorio.findById(id).orElse(null));
    }

    // Obtener entidad completa (uso interno del backend)
    @Override
    public Usuario getUsuarioEntityById(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    // Obtener el propio usuario
    @Override
    public Usuario getMyUsuario(Usuario usuarioActual){
        return getUsuarioEntityById(usuarioActual.getId());
    }

    // Crear usuario desde el registro
    public UsuarioDTO register(SolicitudRegistro solicitud){
        if (usuarioRepositorio.existsByNombreUsuario(solicitud.getNombreUsuario())) {
            return null;
        }
        Usuario usuario = registroToUsuario(solicitud);
        return toDTO(usuarioRepositorio.save(usuario));
    }

    // Actualizar usuario
    public UsuarioDTO updateUsuario(Usuario usuario, Usuario usuarioActual, String rol){
        boolean puedeActualizar = rol.equals("ADMIN");
        Long id;
        if (usuario.getId()==null){
            puedeActualizar = true;
            id = usuarioActual.getId();
        }else{
            id = usuario.getId();
        }
        Usuario existe = getUsuarioEntityById(id);
        if(existe!=null && puedeActualizar){
            existe.setNombre(usuario.getNombre());
            existe.setApellido(usuario.getApellido());
            existe.setCorreo(usuario.getCorreo());
            existe.setTelefono(usuario.getTelefono());
            existe.setFotoPerfil(usuario.getFotoPerfil());
            existe.setActivo(usuario.isActivo());
            existe.setNombreUsuario(usuario.getNombreUsuario());
            existe.setPassword(usuario.getPassword());
            return toDTO(usuarioRepositorio.save(existe));
        }
        return null;
    }

    // Borrar usuario
    public boolean deleteUsuarioById(Long id){
        Usuario usuario = getUsuarioEntityById(id); // lanza EntityNotFoundException si no existe
        if (usuario != null){
            usuarioRepositorio.deleteById(id);
            return true;
        }
        return false;
    }

    // Borrarse a si mismo
    public boolean deleteUsuario(Usuario nombreUsuario){
        if (nombreUsuario != null) {
            return deleteUsuarioById(nombreUsuario.getId());
        }
        return false;
    }

}
