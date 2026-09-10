package com.reto3.productos.rest;

import com.reto3.productos.exception.ProductoInvalidoException;
import com.reto3.productos.model.Producto;
import com.reto3.productos.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone la gestion de productos como API REST.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto request) {
        Producto creado = productoService.crearProducto(
                request.getCodigo(), request.getNombre(), request.getPrecio(), request.getStock());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @ExceptionHandler(ProductoInvalidoException.class)
    public ResponseEntity<String> manejarProductoInvalido(ProductoInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
