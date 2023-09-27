package med.voll.api.infra.errores;

// clase de validacion de integridad para agenda de consultas
public class ValidacionDeIntegridad extends RuntimeException {
    public ValidacionDeIntegridad(String s) {
        super(s);
    }
}
