package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class HorarioDeAnticipacion implements ValidadorDeConsultas{


    // Metodo para validar que la cita se agende 30 minutos antes de la consulta
    public void validar(DatosAgendarConsulta datosAgendarConsulta){
        var ahora = LocalDateTime.now();
        var horaDeConsulta=datosAgendarConsulta.fecha();

        var diferenciaDe30Min = Duration.between(ahora,horaDeConsulta).toMinutes()<30;

        if(diferenciaDe30Min){
            throw new ValidationException("Las consultas deben programarsen con almenos 30 minutos de anticipación");
        }
    }
}
