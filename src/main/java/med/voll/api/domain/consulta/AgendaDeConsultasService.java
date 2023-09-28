package med.voll.api.domain.consulta;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.consulta.validacionesCancelarConsulta.ValidadorCancelamientoDeConsulta;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultasService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired // para que Spring Boot sepa dónde inyectar el valor de ese repositorio, nosotros tenemos que colocar la anotación @Autowired.Si nosotros no colocamos esta anotación, el valor de este atributo va a ser simplemente nulo.
    private ConsultaRepository consultaRepository;

    @Autowired // Creamos una lista y con la anotacion Spring automáticamente sabe que todos los elementos que estén implementando la interfaz ValidadorDeConsultas van a ser inyectados dentro de esta lista y van a encontrarse disponibles.
    List<ValidadorDeConsultas> validadores;

    @Autowired
    List<ValidadorCancelamientoDeConsulta> ValidadoresCancelamiento;
    public DatosDetalleConsulta agendar(DatosAgendarConsulta datosAgendarConsulta){

            // el findById retorna un optional. Nosotros del opcional usamos el método isPresent(findById(datosAgendarConsulta.idPaciente()).isPresent())
        if(!pacienteRepository.existsById(datosAgendarConsulta.idPaciente())){
            throw new ValidacionDeIntegridad("El id del paciente no fue encontrado");
        }

            //  existsById acá directamente estamos usando datos de ID médicos que me retornan un booleano. En caso que es true en caso de que se encuentren y es falso en caso de que no se encuentren
        if(datosAgendarConsulta.idMedico() != null && !medicoRepository.existsById(datosAgendarConsulta.idMedico())){
            throw new ValidacionDeIntegridad("El id del medico no fue encontrado");
        }

        // aca inyectamos los validaciones
        // Vamos a aplicar un forEach para recorrer todos los elementos que se encuentran dentro de esa lista.
        // Entonces esa lista va a estar conformada por cada uno de los validadores que nosotros
        // creamos y vamos a utilizar un arrow function para utilizar el método validar pasándole los
        // datos que estamos recibiendo como parámetro.
        // automáticamente se encarga de enviar esos datos a cada uno de esos validadores y verificar si cumple
        // con las condiciones deseadas para la clínica

        validadores.forEach(v->v.validar(datosAgendarConsulta));


        var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();

        var medico = selecionarMedico(datosAgendarConsulta);


        if(medico==null){
            throw new ValidacionDeIntegridad("no existen medicos disponibles para este horario y especialidad");
        }
        var consulta = new Consulta(medico, paciente, datosAgendarConsulta.fecha());
        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    // metodo para cancelar citas agendadas
    public void cancelar(DatosCancelamientoConsulta datosCancelamientoConsulta){
        if(!consultaRepository.existsById(datosCancelamientoConsulta.idConsulta())){
            throw new ValidationException("Id de la consulta informada no existe");
        }
        ValidadoresCancelamiento.forEach(v -> v.validar(datosCancelamientoConsulta));

        var consulta = consultaRepository.getReferenceById(datosCancelamientoConsulta.idConsulta());
        consulta.cancelar(datosCancelamientoConsulta.motivo());
    }


    // Metodo para seleccionar un Medico que sea aleatorio cuando no se indica en ajendar consulta y que este disponiblr
    private Medico selecionarMedico(DatosAgendarConsulta datosAgendarConsulta) {
        if (datosAgendarConsulta.idMedico()!=null){
            return medicoRepository.getReferenceById(datosAgendarConsulta.idMedico());
        }
        if (datosAgendarConsulta.especialidad()== null){
            throw  new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
        }

        return medicoRepository.selecionarMedicoConEspecialidadEnFecha(datosAgendarConsulta.especialidad(), datosAgendarConsulta.fecha());
    }

    public Page<DatosDetalleConsulta> consultar(Pageable paginacion) {
        return consultaRepository.findAll(paginacion).map(DatosDetalleConsulta::new);

    }
}
