package uis.mytutor.servicio.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uis.mytutor.dto.SolicitudTutor;
import uis.mytutor.modelo.Tutor;
import uis.mytutor.modelo.Usuario;
import uis.mytutor.repositorio.TutorRepositorio;
import uis.mytutor.servicio.interfaz.ITutorServicio;

import java.util.List;

@Service
@Transactional
public class TutorServicio implements ITutorServicio {

    @Autowired
    private TutorRepositorio tutorRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    public Tutor solicitudToTutor(SolicitudTutor solicitud, Usuario usuarioActual) {
        // Este método ya usa un Usuario administrado por JPA
        Usuario usr = usuarioServicio.getUsuarioEntityById(usuarioActual.getId());

        Tutor tutor = new Tutor();
        tutor.setBio(solicitud.getBio());
        tutor.setExperiencia(solicitud.getExperiencia());
        tutor.setPrecioHora(solicitud.getPrecioHora());
        tutor.setUsuario(usr);

        return tutor;
    }

    @Override
    public boolean esTutor(Long idUsuario) {
        return tutorRepositorio.findByUsuario_Id(idUsuario).isPresent();
    }

    @Override
    public List<Tutor> getTutores(){
        return tutorRepositorio.findAll();
    }

    @Override
    public Tutor getTutorById(Long id){
        return tutorRepositorio.findById(id).orElse(null);
    }

    @Override
    public Tutor getTutorByIdUsuario(Long idUsuario){
        return tutorRepositorio.findByUsuario_Id(idUsuario).orElse(null);
    }

    @Override
    public Tutor addTutor(SolicitudTutor solicitud, Usuario usuarioActual){
        Tutor nuevoTutor = solicitudToTutor(solicitud, usuarioActual);
        return tutorRepositorio.save(nuevoTutor);
    }

    // Actualizar tutor (Lo hace el propio tutor, por eso se simplifica)
    @Override
    public Tutor updateTutor(SolicitudTutor solicitud, Usuario usuarioActual){
        Tutor tutor = getTutorByIdUsuario(usuarioActual.getId());
        if (tutor != null) {
            tutor.setBio(solicitud.getBio());
            tutor.setExperiencia(solicitud.getExperiencia());
            tutor.setPrecioHora(solicitud.getPrecioHora());
            return tutorRepositorio.save(tutor);
        }
        return null;
    }

    @Override
    public boolean deleteTutorById(Long id){
        Tutor tutor = getTutorById(id);
        if (tutor != null) {
            tutorRepositorio.deleteById(id);
            return true;
        }
        return false;
    }

    // Que un tutor borre su propio perfil
    @Override
    public boolean deletePropioTutor(Usuario usuarioActual){
        if (usuarioActual != null) {
            Tutor tutor = getTutorByIdUsuario(usuarioActual.getId());;
            return deleteTutorById(tutor.getId());
        }
        return false;
    }

}
