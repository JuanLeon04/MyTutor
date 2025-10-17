package uis.mytutor.servicio.interfaz;

import uis.mytutor.dto.SolicitudTutor;
import uis.mytutor.modelo.Tutor;
import uis.mytutor.modelo.Usuario;

import java.util.List;

public interface ITutorServicio {

    boolean esTutor(Long idUsuario);

    List<Tutor> getTutores();

    Tutor getTutorById(Long id);

    Tutor getTutorByIdUsuario(Long idUsuario);

    // Empezar a ser tutor
    Tutor addTutor(SolicitudTutor solicitud, Usuario usuarioActual);

    // Actualizar Tutor
    Tutor updateTutor(SolicitudTutor solicitud, Usuario usuarioActual);

    // Borrar tutor por su id
    boolean deleteTutorById(Long id);

    // Que un tutor borre su propio perfil
    boolean deletePropioTutor(Usuario usuarioActual);
}
