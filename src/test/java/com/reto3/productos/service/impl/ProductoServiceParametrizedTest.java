package com.reto3.productos.service.impl;

import com.reto3.productos.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas parametrizadas (JUnit 5 @ParameterizedTest + @CsvSource).
 * Cada prueba reutiliza una misma estructura para validar multiples escenarios
 * (validos, invalidos y limite) sin duplicar codigo de prueba.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceParametrizedTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @ParameterizedTest(name = "precio={0} -> valido={1}")
    @DisplayName("validarPrecio debe evaluar correctamente valores validos, invalidos y limite")
    @CsvSource({
            "10,    true",
            "50,    true",
            "0.01,  true",
            "0,     false",
            "-10,   false"
    })
    void validarPrecio_conDistintosValores(double precio, boolean esperado) {
        assertEquals(esperado, productoService.validarPrecio(precio));
    }

    @ParameterizedTest(name = "stock={0} -> valido={1}")
    @DisplayName("validarStock debe evaluar correctamente valores validos, invalidos y limite")
    @CsvSource({
            "0,   true",
            "1,   true",
            "50,  true",
            "-1,  false",
            "-100, false"
    })
    void validarStock_conDistintosValores(int stock, boolean esperado) {
        assertEquals(esperado, productoService.validarStock(stock));
    }

    @ParameterizedTest(name = "stock={0} -> tieneStock={1}")
    @DisplayName("tieneStockDisponible debe evaluar correctamente valores validos y limite")
    @CsvSource({
            "10,  true",
            "1,   true",
            "0,   false",
            "-5,  false"
    })
    void tieneStockDisponible_conDistintosValores(int stock, boolean esperado) {
        assertEquals(esperado, productoService.tieneStockDisponible(stock));
    }

    @ParameterizedTest(name = "precio={0}, descuento={1} -> final={2}")
    @DisplayName("calcularPrecioFinal debe aplicar correctamente distintos porcentajes de descuento")
    @CsvSource({
            "100.0, 10,  90.0",
            "100.0, 0,   100.0",
            "100.0, 100, 0.0",
            "200.0, 50,  100.0",
            "50.0,  20,  40.0"
    })
    void calcularPrecioFinal_conDistintosDescuentos(double precio, double descuento, double esperado) {
        assertEquals(esperado, productoService.calcularPrecioFinal(precio, descuento), 0.0001);
    }
}
