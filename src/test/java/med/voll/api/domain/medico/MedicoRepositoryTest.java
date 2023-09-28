package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;

// cuando nosotros estamos utilizando base de datos de prueba, tenemos que separar la base de datos de
// producción de la base de datos de book o para prueba, ya que podemos tener conflictos y precisamente
// la base de prueba es para realizar diferentes tests que pueden tener valores equivocados o valores
// que podemos incluso borrar la base de datos

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //indicando que la base de datos que yo voy a utilizar va a ser una base de datos externa y que no voy a reemplazar la base de datos que estoy utilizando previamente.
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    MedicoRepository medicoRepository;

    //Spring framework nos permite utilizar el TestEntityManager, que él se va a autoinstanciar únicamente para realizar las pruebas. Entonces solamente para ambientes de prueba, nosotros vamos a tener este TestEntityManager que es un gerenciador que cuando nosotros ejecutemos la prueba él va a estar activo.
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Debe retornar nulo cuando el medico se encuentre en consulta con otro paciente en ese horario")
    void selecionarMedicoConEspecialidadEnFechaEscenario1() {

        // given(Dado un conjunto de valores)
        var proximoLunes10H = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);

        var medico= registrarMedico("Jose Martinez", "Jose@mail.com", "1096513285", Especialidad.CARDIOLOGIA);
        var paciente = registrarPaciente("Maria Perez", "Antonoi@mail.com", "108651478");
        registrarConsulta(medico,paciente,proximoLunes10H);

        // when(Cuandose realiza una accion)
        var medicoLibre = medicoRepository.selecionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA,proximoLunes10H);

        // then(Comparamos el valor recivido con el resultado deseado)
        assertThat(medicoLibre).isNull();

    }

    @Test
    @DisplayName("Debe retornar un medico cuando realice la consulta en la base de datos en ese horario")
    void selecionarMedicoConEspecialidadEnFechaEscenario2() {

        // given
        var proximoLunes10H = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);

        var medico= registrarMedico("Jose Martinez", "Jose@mail.com", "1096513285", Especialidad.CARDIOLOGIA);

        // when
        var medicoLibre = medicoRepository.selecionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA,proximoLunes10H);

        // then
        assertThat(medicoLibre).isEqualTo(medico);

    }

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha) {
        em.persist(new Consulta(null, medico, paciente, fecha, null));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad) {
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad));
        em.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento) {
        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        em.persist(paciente);
        return paciente;
    }

    private DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad especialidad) {
        return new DatosRegistroMedico(
                nombre,
                email,
                "61999999999",
                documento,
                especialidad,
                datosDireccion()
        );
    }

    private DatosRegistroPaciente datosPaciente(String nombre, String email, String documento) {
        return new DatosRegistroPaciente(
                nombre,
                email,
                "61999999999",
                documento,
                datosDireccion()
        );
    }

    private DatosDireccion datosDireccion() {
        return new DatosDireccion(
                " loca",
                "azul",
                "acapulpo",
                "321",
                "12"
        );
    }
}