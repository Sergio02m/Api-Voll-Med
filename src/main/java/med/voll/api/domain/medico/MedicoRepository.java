package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // metodo para retornar solo medicos activos
    Page<Medico> findByActivoTrue(Pageable paginacion);

    @Query("""
            select m from Medico m 
            where m.activo= 1 and
            m.especialidad=:especialidad and
            m.id not in(
            select c.medico.id from Consulta c
            c.data=fecha
            )
            order by rand()
            limit 1
            """)
    Medico selecionarMedicoConEspecialidadEnFecha(Especialidad especialidad, LocalDate fecha);
}
