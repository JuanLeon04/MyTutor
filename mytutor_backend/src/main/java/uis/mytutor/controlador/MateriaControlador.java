package uis.mytutor.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uis.mytutor.modelo.Materia;
import uis.mytutor.servicio.impl.MateriaServicio;

import java.util.List;

@RestController
@RequestMapping("/api/materia")
@Tag(name = "Materia", description = "EndPoints sobre las Materias")
public class MateriaControlador {

    @Autowired
    MateriaServicio materiaServicio;

    @Operation(summary = "Listar todos las materias")
    @GetMapping("/list")
    public List<Materia> getMaterias() {
        return materiaServicio.getMaterias();
    }

    @Operation(summary = "Obtener una materia por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Materia> obtenerMateriaPorId(@PathVariable Long id) {
        Materia obj = materiaServicio.getMateriaById(id);
        if (obj == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Crear una materia (Solo rol ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Materia> crearMateria(@RequestBody Materia materia) {
        Materia obj = materiaServicio.addMateria(materia);
        if (obj == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Modificar una materia (Solo rol ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public ResponseEntity<Materia> editarMateria(@RequestBody Materia materia) {
        Materia obj = materiaServicio.updateMateria(materia);
        if (obj == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(obj);
    }

    @Operation(summary = "Borrar una materia por id (Solo rol ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuarioPorId(@PathVariable Long id) {
        boolean borrado = materiaServicio.deleteMateriaById(id);
        if (borrado) {
            return ResponseEntity.ok().build(); // borrado exitoso
        }
        return ResponseEntity.notFound().build(); // no existía
    }
}
