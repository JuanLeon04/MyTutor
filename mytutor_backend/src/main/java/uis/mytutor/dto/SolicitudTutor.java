package uis.mytutor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudTutor {

    private String bio;
    private Double precioHora;
    private String experiencia;
}
