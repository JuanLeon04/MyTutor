package uis.mytutor.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.mytutor.modelo.Horario;

public interface HorarioRepositorio extends JpaRepository<Horario, Long> {
}
