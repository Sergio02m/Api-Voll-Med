package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.DatosAgendarConsulta;

//dentro de las interfaces nosotros vamos a colocar métodos abstractos. La interface es una clase
//abstracta que son métodos que no tienen cuerpo, simplemente sirven para indicar cuál va a ser la firma
// de los métodos que van a ser implementados dentro de las clases que implementen en esa interface
//DONDE SERA IMPLEMENTADA EN CADA UNA DE LAS VALIDACIONES QUE CREAMOS PUESTO TODAS SEGUEN UN PATRON
//COMO LO SON EL METODO Y LA FIRMA.
public interface ValidadorDeConsultas {
    public void validar(DatosAgendarConsulta datosAgendarConsulta);
}
