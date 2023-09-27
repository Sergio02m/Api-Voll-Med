package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.paciente.DatosListadoPaciente;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;


    //metodo para recivir los datos enviados por el usuario(post medicos)
    @PostMapping
    @Transactional
    public void registrarpaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente){
        pacienteRepository.save(new Paciente(datosRegistroPaciente));
    }

    // Metodo para listar medico con retorno de datos especificados.
    @GetMapping
    public List<DatosListadoPaciente> listadopacientes(){
        return pacienteRepository.findAll().stream().map(DatosListadoPaciente::new).toList();
    }
}
