package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroMedico(

        // REALIZAMOS LA VALIDACION DE LOS DATOS QUE EVNIA EL CLIENTE, PARA LUEGO REALIZAR LA PERSISTENCIA;
        @NotBlank  // INTERNAMENTE HACE LO MISMO DE @NoyNull
        String nombre,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{4,10}") // EXPRECION REGULAR PARA VALIDACION DE NUMEROS.
        String telefono,

        @NotBlank
        @Pattern(regexp = "\\d{4,10}") // EXPRECION REGULAR PARA VALIDACION DE NUMEROS.
        String documento,

        @NotNull // ETIQUETA OBLICATORIA CUANDO ES UN OBJETO O EN ESTE CASO UN ENUM
        Especialidad especialidad,

        @NotNull // COMO ES UN OBJETO NUNCA LLEGARA EN BLANCO PERO SI NULL
        @Valid // VALIDA LOS DATOS RECIVIDOS
        DatosDireccion direccion
){

}

