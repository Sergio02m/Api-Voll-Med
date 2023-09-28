package med.voll.api.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice actúa como una especie de proxy para todos nuestros
// controllers, por algo está como rest controller, para interceptar las
// llamadas en caso suceda algún tipo de excepción.
@RestControllerAdvice
public class TratadorDeErrores {

    // 1. Para tratar errores por busqueda de MEDICOS URL
    // @ExceptionHandler lo que yo le voy a decir es el tipo de excepción
    // que yo quiero tratar. En este caso es esta de aquí: EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();
    }

    // 2. Para tratar errores por formulario de registro con campos en blanco
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }
    // tratar errores ValidacionDeIntegridad
    @ExceptionHandler(ValidacionDeIntegridad.class)
    public ResponseEntity errorHandlerValidacionesDeIntegridad(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // tratar errores ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity errorHandlerValidacionesDeNegocio(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    //  Si yo quiero intervenir aquí y personalizar el tipo de respuesta que
    //  quiero dar a mi cliente, necesito Un DTO,
    //  y como ese DTO va a ser usado solo aquí, a este nivel, yo lo voy a
    //  crear internamente aquí.

    private record DatosErrorValidacion(String campo,String error){
        public DatosErrorValidacion(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }

    }

}
