package com.reto3.productos.service.impl;

import com.reto3.productos.exception.ProductoInvalidoException;
import com.reto3.productos.model.Producto;
import com.reto3.productos.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de ProductoServiceImpl.
 * ProductoRepository se simula con Mockito (@Mock) para aislar la logica de negocio
 * de la base de datos real (PostgreSQL / Neon), siguiendo el patron @Mock + @InjectMocks.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private final String codigo = "P001";
    private final String nombre = "Teclado mecanico";

    // ---------- Casos exitosos ----------

    @Test
    @DisplayName("crearProducto con datos validos debe guardarlo mediante el repositorio")
    void crearProducto_conDatosValidos_debeCrearYGuardarProducto() {
        Producto esperado = new Producto(codigo, nombre, 100.0, 10);
        when(productoRepository.save(any(Producto.class))).thenReturn(esperado);

        Producto resultado = productoService.crearProducto(codigo, nombre, 100.0, 10);

        assertEquals(esperado, resultado);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("crearProducto no debe invocar al repositorio si los datos son invalidos")
    void crearProducto_conDatosInvalidos_noDebeInvocarRepositorio() {
        assertThrows(ProductoInvalidoException.class,
                () -> productoService.crearProducto(codigo, "", 100.0, 10));

        verify(productoRepository, never()).save(any(Producto.class));
    }

    // ---------- Casos invalidos: nombre ----------

    @Test
    @DisplayName("crearProducto con nombre vacio debe lanzar ProductoInvalidoException")
    void crearProducto_conNombreVacio_debeLanzarExcepcion() {
        assertThrows(ProductoInvalidoException.class,
                () -> productoService.crearProducto(codigo, "   ", 50.0, 5));
    }

    @Test
    @DisplayName("validarNombre con nombre nulo debe retornar false")
    void validarNombre_conNombreNulo_debeRetornarFalse() {
        assertFalse(productoService.validarNombre(null));
    }

    @Test
    @DisplayName("validarNombre con nombre valido debe retornar true")
    void validarNombre_conNombreValido_debeRetornarTrue() {
        assertTrue(productoService.validarNombre(nombre));
    }

    // ---------- Casos invalidos: precio ----------

    @Test
    @DisplayName("crearProducto con precio negativo debe lanzar ProductoInvalidoException")
    void crearProducto_conPrecioNegativo_debeLanzarExcepcion() {
        assertThrows(ProductoInvalidoException.class,
                () -> productoService.crearProducto(codigo, nombre, -10.0, 5));
    }

    @Test
    @DisplayName("crearProducto con precio igual a cero debe lanzar ProductoInvalidoException")
    void crearProducto_conPrecioCero_debeLanzarExcepcion() {
        assertThrows(ProductoInvalidoException.class,
                () -> productoService.crearProducto(codigo, nombre, 0.0, 5));
    }

    // ---------- Casos invalidos / limite: stock ----------

    @Test
    @DisplayName("crearProducto con stock negativo debe lanzar ProductoInvalidoException")
    void crearProducto_conStockNegativo_debeLanzarExcepcion() {
        assertThrows(ProductoInvalidoException.class,
                () -> productoService.crearProducto(codigo, nombre, 50.0, -1));
    }

    @Test
    @DisplayName("validarStock con stock igual a cero (caso limite) debe retornar true")
    void validarStock_conStockCero_debeRetornarTrue() {
        assertTrue(productoService.validarStock(0));
    }

    // ---------- Disponibilidad de stock ----------

    @Test
    @DisplayName("tieneStockDisponible con stock positivo debe retornar true")
    void tieneStockDisponible_conStockPositivo_debeRetornarTrue() {
        assertTrue(productoService.tieneStockDisponible(10));
    }

    @Test
    @DisplayName("tieneStockDisponible con stock cero (caso limite) debe retornar false")
    void tieneStockDisponible_conStockCero_debeRetornarFalse() {
        assertFalse(productoService.tieneStockDisponible(0));
    }

    // ---------- Calculo de precio final ----------

    @Test
    @DisplayName("calcularPrecioFinal con descuento valido debe calcular correctamente")
    void calcularPrecioFinal_conDescuentoValido_debeCalcularCorrectamente() {
        double resultado = productoService.calcularPrecioFinal(100.0, 20);
        assertEquals(80.0, resultado);
    }

    @Test
    @DisplayName("calcularPrecioFinal con descuento fuera de rango debe lanzar IllegalArgumentException")
    void calcularPrecioFinal_conDescuentoFueraDeRango_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> productoService.calcularPrecioFinal(100.0, 150));
    }
}
