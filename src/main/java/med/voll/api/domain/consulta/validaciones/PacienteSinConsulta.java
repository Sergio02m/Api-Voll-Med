package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class PacienteSinConsulta implements ValidadorDeConsultas{

    //con @Autowire ahora Spring sabe que el repositorio de consulta se debe inyectar dentro de la clase de
    // pacienteSinConsulta, y sabe que esta clase de pacienteSinConsulta se encuentra disponible donde
    // quiera ser inyectada
    @Autowired
    private ConsultaRepository consultaRepository;

    // metodo para validar que no se asigne 2 citas al paciente en el mismo dia
    public void validar(DatosAgendarConsulta datosAgendarConsulta){

        var primerHorario = datosAgendarConsulta.fecha().withHour(7);
        var ultimohorario = datosAgendarConsulta.fecha().withHour(18);

        var pacienteConConsulta = consultaRepository.existsByPacienteIdAndDataBetween(datosAgendarConsulta.idPaciente(),primerHorario, ultimohorario);

        if(pacienteConConsulta){
            throw new ValidationException("El paciente ya tiene una consulta para ese dia");
        }
    }
}
