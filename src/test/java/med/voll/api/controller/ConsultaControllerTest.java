package med.voll.api.controller;


import med.voll.api.domain.consulta.AgendaDeConsultasService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.domain.medico.Especialidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest // Acá vamos a trabajar con @SpringBootTest que es una anotación que nos permite trabajar con todos los componentes dentro del contexto de Spring, entonces acá nosotros podemos utilizar repositorios, los servicios y los controladores.
@AutoConfigureMockMvc // Esta anotación se encarga de configurar todos los componentes necesarios para realizar una simulación de una petición para ese controlador.
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@SuppressWarnings("all")

class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc; // Simula un servidor

    @Autowired
    private JacksonTester<DatosAgendarConsulta> agendarConsultaJacksonTester; //Esta clase toma un objeto que es del tipo Java y lo va a transformar en un objeto del tipo JSON

    @Autowired
    private JacksonTester<DatosDetalleConsulta> detalleConsultaJacksonTester;

    @MockBean // Con esto, le decimos a Spring que cuando necesite inyectar este objeto en el controlador, debe aplicar el mock y no inyectar una agendaDeConsultaService real.
    private AgendaDeConsultasService agendaDeConsultaService;

    @Test
    @DisplayName("Deberia retornar estado 400 cuando los datos ingresados sean invalidos")
    @WithMockUser //recordando que nosotros teníamos que enviar el token antes de realizar cualquier petición, entonces nosotros tenemos que también simular el envío de ese token dentro de nuestra petición. Entonces para eso nosotros vamos a utilizar la anotación @WithMockUser y tenemos que importar la librería.
    void agendarEscenario1() throws Exception {

        // given // when
        // vamos a desempeñar un una petición del tipo post en la dirección “consultas” y tenemos que retornar una respuesta. Entonces, esta vez el estado esperado va a ser el estado 400, que es equivalente a BAD_REQUEST.
        var response = mvc.perform(post("/consultas")).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Debera retornar estado http 200 cuando los datos ingresados sean validos")
    @WithMockUser
    void agendarEscenario2() throws Exception {
        // given
        var fecha= LocalDateTime.now().plusHours(1) ;  //  la fecha que tenemos que colocar es una fecha con una hora de anticipación, haciendo la especificación de que la consulta que se va a realizar va a ser una hora después del momento actual.
        var especialidad = Especialidad.CARDIOLOGIA;
        var datos = new DatosDetalleConsulta(null, 2l, 5l, fecha);

        // when

        when(agendaDeConsultaService.agendar(any())).thenReturn(datos);

        // vamos a desempeñar un una petición del tipo post en la dirección “consultas” y tenemos que retornar una respuesta. Entonces, esta vez el estado esperado va a ser el estado 200, que es equivalente a OK.
        var response = mvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON) // SE ESPECIFICA EL TIPO DE CONTENIDO, EN ESTE CASO FORMATO JSON
                .content(agendarConsultaJacksonTester.write(new DatosAgendarConsulta(2l, 5l, especialidad, fecha)).getJson())
        ).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = detalleConsultaJacksonTester.write(datos).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }


}