package uis.mytutor.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.mytutor.modelo.Resena;

public interface ResenaRepositorio extends JpaRepository<Resena, Long> {
}
