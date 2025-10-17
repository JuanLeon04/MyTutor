package uis.mytutor.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.mytutor.modelo.Materia;

public interface MateriaRepositorio extends JpaRepository<Materia, Long> {
}
