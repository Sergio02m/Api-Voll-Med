package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacienteActivo implements ValidadorDeConsultas{

    @Autowired
    private PacienteRepository pacienteRepository;

    // Metodo para validar que el paciente este activo en el sistema.
    public void validar(DatosAgendarConsulta datosAgendarConsulta){

        if(datosAgendarConsulta.idPaciente() == null){
            return;
        }
        var pacienteActivo = pacienteRepository.findActivoById(datosAgendarConsulta.idPaciente());

        if(!pacienteActivo){
            throw new ValidationException("No se permite ajendar citas con pacientes inactivos en el sistema");
        }

    }
}
