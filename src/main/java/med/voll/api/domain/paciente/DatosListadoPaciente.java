package med.voll.api.domain.paciente;

public record DatosListadoPaciente(
        String nombre,
        String documento,
        String telefono,
        String email,
        String ciudad
) {
    public DatosListadoPaciente(Paciente paciente){
        this(
                paciente.getNombre(),
                paciente.getDocumento(),
                paciente.getTelefono(),
                paciente.getEmail(),
                paciente.getDireccion().getCiudad()
        );
    }

}
