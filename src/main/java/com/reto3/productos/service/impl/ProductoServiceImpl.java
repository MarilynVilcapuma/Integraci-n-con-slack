package com.reto3.productos.service.impl;

import com.reto3.productos.exception.ProductoInvalidoException;
import com.reto3.productos.model.Producto;
import com.reto3.productos.repository.ProductoRepository;
import com.reto3.productos.service.ProductoService;
import org.springframework.stereotype.Service;

/**
 * Implementacion de ProductoService.
 * Depende de {@link ProductoRepository} para persistir los productos creados;
 * esa dependencia se simula con Mockito en las pruebas unitarias para aislar
 * esta clase de la base de datos real (PostgreSQL / Neon).
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto crearProducto(String codigo, String nombre, double precio, int stock) {
        if (!validarNombre(nombre)) {
            throw new ProductoInvalidoException("El nombre del producto no es valido: " + nombre);
        }
        if (!validarPrecio(precio)) {
            throw new ProductoInvalidoException("El precio del producto no es valido: " + precio);
        }
        if (!validarStock(stock)) {
            throw new ProductoInvalidoException("El stock del producto no es valido: " + stock);
        }

        Producto producto = new Producto(codigo, nombre, precio, stock);
        return productoRepository.save(producto);
    }

    @Override
    public boolean validarNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    @Override
    public boolean validarPrecio(double precio) {
        return precio > 0;
    }

    @Override
    public boolean validarStock(int stock) {
        return stock >= 0;
    }

    @Override
    public double calcularPrecioFinal(double precio, double descuentoPorcentaje) {
        if (descuentoPorcentaje < 0 || descuentoPorcentaje > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100: " + descuentoPorcentaje);
        }
        return precio - (precio * descuentoPorcentaje / 100);
    }

    @Override
    public boolean tieneStockDisponible(int stock) {
        return stock > 0;
    }
}
