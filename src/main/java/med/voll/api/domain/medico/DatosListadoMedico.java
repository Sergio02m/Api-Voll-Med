package med.voll.api.domain.medico;

import med.voll.api.domain.paciente.Paciente;

public record DatosListadoMedico(

        Long id,
        String nombre,
        String documento,
        String especialidad,
        String email

) {
    public DatosListadoMedico(Medico medico){
        this(
                medico.getId(),
                medico.getNombre(),
                medico.getDocumento(),
                medico.getEspecialidad().toString(),
                medico.getEmail()
        );
    }

}
