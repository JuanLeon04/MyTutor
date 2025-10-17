package uis.mytutor.servicio.interfaz;

import uis.mytutor.modelo.Materia;

import java.util.List;

public interface IMateriaServicio {

    List<Materia> getMaterias();

    Materia getMateriaById(Long id);

    Materia addMateria(Materia cliente);

    Materia updateMateria(Materia cliente);

    boolean deleteMateriaById(Long id);
}
