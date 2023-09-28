package med.voll.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;


    // 1.   metodo para recivir los datos enviados por el usuario(post medicos)
    // Usar la clase ResponseEntity, de Spring, para personalizar los retornos de los métodos de
    // una clase Controller
    // usamos ResponseEntity, que es un wrapper para digamos encapsular la respuesta que le
    // vamos a dar a nuestro servidor
    @PostMapping
    @Operation(summary = "Registra un nuevo medico en la base de datos")
    public ResponseEntity <DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
                                                                 UriComponentsBuilder uriComponentsBuilder){
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri(); // crea url para encontrar medico agregado.
        return ResponseEntity.created(url).body(datosRespuestaMedico);

        // retorna 201 Created
        // Retorno URL donde encontrar al medico
        // Get://http:localhost:8080/medicos/id
    }


    // 2.   Metodo para listar medico con retorno de datos especificados.
    //@GetMapping
    // RETORNAMOS LISTAS
    //public List<DatosListadoMedico> listadomedicos(){
    // RETORNAMOS PANINAS(CUANDO NO QUEREMOS MOSTAR MAS QUE CIERTA CANTIDAD DE MEDICOS EN UN PAGINA)
    //return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
    //}


    //@GetMapping                                              // modificar atributos para obciones de listado
   //public Page<DatosListadoMedico> listadomedicos(@PageableDefault(size = 4,sort= "nombre") Pageable paginacion){
        // retornara lista completa de medicos
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);

    @GetMapping                                              // modificar atributos para obciones de listado
    @Operation(summary = "Obtiene el listado de medicos")
    public  ResponseEntity<Page<DatosListadoMedico>>listadomedicos(@PageableDefault(size = 4,sort= "nombre") Pageable paginacion){
        //Retornara solo Medicos activos
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
    }

    // 3.  Metodo para editar medicos

    @PutMapping
    @Transactional // perinte realizar la transaccion despues de ejecutar el método
                    // realizando un commin en la base de datos por ende actualizando la informacion.
    @Operation(summary = "Actualiza los datos de un medico existente")
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento())));

    }

    // 4.  Metodo para eliminar medicos de la base de datos

    //@DeleteMapping("/{id}")
    //@Transactional
    //@Operation(summary = "Elimina un medico registrado")
    //public void eliminarMedicos(@PathVariable Long id){
        //Medico medico = medicoRepository.getReferenceById(id);
        //medicoRepository.delete(medico);
    //}

    // 5.  Metodo para eliminar medicos (delete Logico)
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "inactiva un medico registrado")
    public ResponseEntity inactivarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

    // 6. Metodo para obtener por medio de url (generado en metodo registar)
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene los registros del medico con ID")
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedicos = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(),
                medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedicos);
    }

}
