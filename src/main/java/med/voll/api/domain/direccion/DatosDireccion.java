package med.voll.api.domain.direccion;

import jakarta.validation.constraints.NotBlank;

public record DatosDireccion(

        // REALIZAMOS LA RESPECTIVA VALIDACION CON LA ETIQUETA @NotBlank
        // (QUE VALIDA QUE NO LLEGUE LA INFORMACION NULL O EN BLANCO.
        @NotBlank
        String calle,
        @NotBlank
        String distrito,
        @NotBlank
        String  ciudad,
        @NotBlank
        String numero,
        @NotBlank
        String complemento
) {
}
