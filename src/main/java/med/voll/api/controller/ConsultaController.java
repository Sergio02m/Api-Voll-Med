package med.voll.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultasService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@Controller // cuando manejamos @Controller en vez de @RestController debemo implementar ademas @ResponseBody, que es implementado por @Rescontroller
@ResponseBody
@RequestMapping("/consultas")// ruta url
@SecurityRequirement(name = "bearer-key") // requerimientos de seguridad(en donde podremos agregar el token cunado estemos acediendo desde la interfaz de usuario de api desde OpenAPI)
public class ConsultaController {

    @Autowired
    private AgendaDeConsultasService agendaDeConsultasService;

    @PostMapping
    @Transactional
    @Operation(summary = "Agendas de consulta")
    public ResponseEntity agendar (@RequestBody @Valid DatosAgendarConsulta datos){
        System.out.println(datos);
        var response = agendaDeConsultasService.agendar(datos);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Obtiene el listado de consultas")
    public ResponseEntity<Page<DatosDetalleConsulta>> listar(@PageableDefault(size = 10, sort = {"data"}) Pageable paginacion) {
        var response = agendaDeConsultasService.consultar(paginacion);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Transactional
    @Operation(
            summary = "cancela una consulta de la agenda",
            description = "requiere motivo"
            )
    public ResponseEntity cancelar(@RequestBody @Valid DatosCancelamientoConsulta datosCancelamientoConsulta ){
        agendaDeConsultasService.cancelar(datosCancelamientoConsulta);
        return ResponseEntity.noContent().build();
    }
}
