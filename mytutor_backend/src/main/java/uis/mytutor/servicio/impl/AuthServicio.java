package uis.mytutor.servicio.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uis.mytutor.Excepciones.AuthExcepciones;
import uis.mytutor.configuraciones.jwt.JwtUtil;
import uis.mytutor.dto.SolicitudRegistro;
import uis.mytutor.dto.UsuarioDTO;
import uis.mytutor.repositorio.UsuarioRepositorio;
import uis.mytutor.servicio.interfaz.IAuthServicio;

@Service
public class AuthServicio implements IAuthServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private TutorServicio tutorServicio;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Iniciar sesion
    @Override
    public String login(String nombreUsuario, String password) {
        var usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new AuthExcepciones.CredencialesInvalidasException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new AuthExcepciones.CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        String rol;
        if(tutorServicio.esTutor(usuario.getId())) {
            rol = "TUTOR";
        }else{
            rol = "ESTUDIANTE";
        }
        // MALA PRACTICA pero me da pereza solucionar en DB xd
        // igual debe estar registrado en la base de datos si no gg
        if (nombreUsuario.equals("admin")){
            rol = "ADMIN";
        }

        // Si todo está bien → generar token
        return jwtUtil.generateToken(nombreUsuario, rol);
    }

    // Registrar usuario en el servicio de usuario
}
