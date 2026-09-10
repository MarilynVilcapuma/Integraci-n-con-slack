package com.reto3.productos.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de la entidad Producto (getters, equals, hashCode y toString).
 */
class ProductoTest {

    @Test
    void getters_debenRetornarLosValoresAsignados() {
        Producto producto = new Producto("P001", "Mouse", 25.5, 15);

        assertEquals("P001", producto.getCodigo());
        assertEquals("Mouse", producto.getNombre());
        assertEquals(25.5, producto.getPrecio());
        assertEquals(15, producto.getStock());
    }

    @Test
    void equals_conMismoCodigo_debeSerIgual() {
        Producto p1 = new Producto("P001", "Mouse", 25.5, 15);
        Producto p2 = new Producto("P001", "Mouse inalambrico", 30.0, 5);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void equals_conCodigoDistinto_noDebeSerIgual() {
        Producto p1 = new Producto("P001", "Mouse", 25.5, 15);
        Producto p2 = new Producto("P002", "Mouse", 25.5, 15);

        assertNotEquals(p1, p2);
    }

    @Test
    void equals_conObjetoDeOtroTipo_noDebeSerIgual() {
        Producto p1 = new Producto("P001", "Mouse", 25.5, 15);
        assertNotEquals(p1, "P001");
    }

    @Test
    void toString_debeContenerLosDatosDelProducto() {
        Producto producto = new Producto("P001", "Mouse", 25.5, 15);
        String texto = producto.toString();

        assertTrue(texto.contains("P001"));
        assertTrue(texto.contains("Mouse"));
    }
}
