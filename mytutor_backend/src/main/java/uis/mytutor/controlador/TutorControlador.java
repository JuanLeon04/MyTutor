package uis.mytutor.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uis.mytutor.dto.SolicitudTutor;
import uis.mytutor.modelo.Tutor;
import uis.mytutor.modelo.Usuario;
import uis.mytutor.servicio.impl.TutorServicio;

import java.util.List;

@RestController
@RequestMapping("/api/tutor")
@Tag(name = "Tutor", description = "EndPoints sobre los tutores")
public class TutorControlador {

    @Autowired
    TutorServicio tutorServicio;

    @Operation(summary = "Listar todos los Tutores")
    @GetMapping("/list")
    public List<Tutor> getTutores() {
        return tutorServicio.getTutores();
    }

    @Operation(summary = "Obtener un tutor por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Tutor> obtenerTutorPorId(@PathVariable Long id) {
        Tutor obj = tutorServicio.getTutorById(id);
        if (obj == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Hacer que un Usuario sea tutor (Solo rol Estudiante)")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    @PostMapping()
    public ResponseEntity<Tutor> crearTutor(@RequestBody SolicitudTutor solicitud, Authentication authentication) {
        Usuario usuarioActual = (Usuario) authentication.getPrincipal(); // lo que pusiste en el filtro

        Tutor obj = tutorServicio.addTutor(solicitud, usuarioActual);
        if (obj == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Actualizar propio perfil de tutor (Solo rol Tutor)")
    @PreAuthorize("hasRole('TUTOR')")
    @PutMapping() //actualizar
    public ResponseEntity<Tutor> actualizarTutor(@RequestBody SolicitudTutor solicitud, Authentication authentication) {

        Usuario usuarioActual = (Usuario) authentication.getPrincipal(); // lo que pusiste en el filtro
        String rol = authentication.getAuthorities().iterator().next().getAuthority(); // ej. ROLE_TUTOR

        Tutor obj = tutorServicio.updateTutor(solicitud, usuarioActual);
        if (obj == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Borrar Tutor por ID (Solo rol ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTutorPorId(@PathVariable Long id) {
        boolean borrado = tutorServicio.deleteTutorById(id);
        if (borrado) {
            return ResponseEntity.ok().build(); // borrado exitoso
        }
        return ResponseEntity.notFound().build(); // no existía
    }

    @Operation(summary = "Permite que un usuario borre su perfil de tutor (Solo rol Tutor)")
    @PreAuthorize("hasRole('TUTOR')")
    @DeleteMapping()
    public ResponseEntity<Void> eliminarElPropioTutor(Authentication authentication) {
        Usuario usuarioActual = (Usuario) authentication.getPrincipal(); // lo que pusiste en el filtro

        boolean borrado = tutorServicio.deletePropioTutor(usuarioActual);
        if (borrado) {
            return ResponseEntity.ok().build(); // borrado exitoso
        }
        return ResponseEntity.notFound().build(); // no existía
    }
}
