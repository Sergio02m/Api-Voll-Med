package med.voll.api.domain.consulta;

import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


    Boolean existsByPacienteIdAndDataBetween(Long IdPaciente, LocalDateTime primerHorario, LocalDateTime ultimohorario);

    Boolean existsByMedicoIdAndData(Long IdMedico, LocalDateTime fecha);

}
