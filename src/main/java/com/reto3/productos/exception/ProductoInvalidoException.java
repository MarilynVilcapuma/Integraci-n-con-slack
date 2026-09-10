package com.reto3.productos.exception;

/**
 * Se lanza cuando los datos de un producto no cumplen las reglas de validacion
 * (nombre, precio o stock invalidos).
 */
public class ProductoInvalidoException extends RuntimeException {

    public ProductoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
