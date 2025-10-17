package uis.mytutor.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import uis.mytutor.modelo.Reserva;

public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {
}
