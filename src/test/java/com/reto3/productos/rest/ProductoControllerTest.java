package com.reto3.productos.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto3.productos.exception.ProductoInvalidoException;
import com.reto3.productos.model.Producto;
import com.reto3.productos.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba de la capa REST aislada (WebMvcTest): el servicio se simula con Mockito,
 * por lo que no se realiza ninguna conexion a la base de datos real durante la prueba.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crear_conDatosValidos_debeRetornar201() throws Exception {
        Producto producto = new Producto("P001", "Teclado", 100.0, 10);
        when(productoService.crearProducto(anyString(), anyString(), anyDouble(), anyInt()))
                .thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(producto)));
    }

    @Test
    void crear_conDatosInvalidos_debeRetornar400() throws Exception {
        when(productoService.crearProducto(anyString(), any(), anyDouble(), anyInt()))
                .thenThrow(new ProductoInvalidoException("El nombre del producto no es valido: "));

        Producto invalido = new Producto("P002", "", 100.0, 10);

        mockMvc.perform(post("/api/productos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }
}
