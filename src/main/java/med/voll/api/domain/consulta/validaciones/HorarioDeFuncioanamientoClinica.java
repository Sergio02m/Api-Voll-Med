package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class HorarioDeFuncioanamientoClinica implements ValidadorDeConsultas{

    // Metodo para validar que las citas sean agendadas en el horario estipulado

    public void validar(DatosAgendarConsulta datosAgendarConsulta){

        var domingo = DayOfWeek.SUNDAY.equals(datosAgendarConsulta.fecha().getDayOfWeek());// valida que el dia de agendamiento no sea domingo
        var antesDeApertura = datosAgendarConsulta.fecha().getHour()<7;
        var despuesDeCierre = datosAgendarConsulta.fecha().getHour()>19;

        if (domingo || antesDeApertura || despuesDeCierre){
            throw new ValidationException("El horario de atención de la clinica es de lunes a sabado de 07:00 a 19:00 horas");
        }

    }
}
