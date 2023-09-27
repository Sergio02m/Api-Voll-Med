package med.voll.api.domain.medico;

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
