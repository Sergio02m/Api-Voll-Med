package med.voll.api.domain.consulta;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.medico.Especialidad;

import java.time.LocalDateTime;

public record DatosAgendarConsulta(

        @NotNull

        Long idPaciente,

        Long idMedico,

        Especialidad especialidad,

        @NotNull
        @Future // para fechas posterior a la fecha actual
        //@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha
) {


}
