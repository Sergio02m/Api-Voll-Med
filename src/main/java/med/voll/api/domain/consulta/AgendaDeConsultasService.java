package med.voll.api.domain.consulta;

import jakarta.validation.constraints.Null;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultasService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired // para que Spring Boot sepa dónde inyectar el valor de ese repositorio, nosotros tenemos que colocar la anotación @Autowired.Si nosotros no colocamos esta anotación, el valor de este atributo va a ser simplemente nulo.
    private ConsultaRepository consultaRepository;
    public void agendar(DatosAgendarConsulta datosAgendarConsulta){

            // el findById retorna un optional. Nosotros del opcional usamos el método isPresent
        if(pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("Este id para el paciente no fue encontrado");
        }

            //  existsById acá directamente estamos usando datos de ID médicos que me retornan un booleano. En caso que es true en caso de que se encuentren y es falso en caso de que no se encuentren
        if(datosAgendarConsulta.idMedico() != null && medicoRepository.existsById(datosAgendarConsulta.idMedico())){
            throw new ValidacionDeIntegridad("El medico no fue encontrado");
        }
        var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();

        var medico = selecionarMedico(datosAgendarConsulta);

        var consulta = new Consulta(null,medico, paciente, datosAgendarConsulta.fecha());
        consultaRepository.save(consulta);
    }

    // Metodo para seleccionar un Medico qque sea aleatorio cuando no se indica en ajendar consulta y que este disponiblr
    private Medico selecionarMedico(DatosAgendarConsulta datosAgendarConsulta) {
        if (datosAgendarConsulta.idMedico()!=null){
            return medicoRepository.getReferenceById(datosAgendarConsulta.idMedico());
        }
        if (datosAgendarConsulta.especialidad()== null){
            throw  new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
        }

        return medicoRepository.selecionarMedicoConEspecialidadEnFecha(datosAgendarConsulta.especialidad(),datosAgendarConsulta.fecha());
    }
}
