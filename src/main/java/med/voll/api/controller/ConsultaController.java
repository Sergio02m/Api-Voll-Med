package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultasService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller // cuando manejamos @Controller en vez de @RestController debemo implementar ademas @ResponseBody, que es implementado por @Rescontroller
@ResponseBody
@RequestMapping("/consultas")// ruta url
public class ConsultaController {

    @Autowired
    private AgendaDeConsultasService agendaDeConsultasService;


    @PostMapping
    @Transactional
    public ResponseEntity agendar (@RequestBody @Valid DatosAgendarConsulta datos){
        System.out.println(datos);

        agendaDeConsultasService.agendar(datos);
        return ResponseEntity.ok(new DatosDetalleConsulta(null, null, null,null));
    }
}
