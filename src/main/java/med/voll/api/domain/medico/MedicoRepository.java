package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import java.time.LocalDateTime;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // metodo para retornar solo medicos activos
    Page<Medico> findByActivoTrue(Pageable paginacion);

    @Query("""
            select m from Medico m
            where m.activo= true\s
            and
            m.especialidad=:especialidad\s
            and
            m.id not in( \s
                select c.medico.id from Consulta c
                where
                c.data=:fecha
            )
            order by rand()
            limit 1
            """)
    Medico selecionarMedicoConEspecialidadEnFecha(Especialidad especialidad, LocalDateTime fecha);

    @Query("""
            select m.activo
            from Medico m
            where m.id=:idMedico
            """)
    Boolean findActivoById(Long idMedico);
}
