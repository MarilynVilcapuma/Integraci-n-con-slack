package com.reto3.productos.service;

import com.reto3.productos.exception.ProductoInvalidoException;
import com.reto3.productos.model.Producto;

/**
 * Logica de negocio de gestion de productos.
 */
public interface ProductoService {

    /**
     * Crea y registra un producto luego de validar nombre, precio y stock.
     *
     * @throws ProductoInvalidoException si algun dato del producto es invalido
     */
    Producto crearProducto(String codigo, String nombre, double precio, int stock);

    /**
     * Un nombre es valido si no es nulo y contiene al menos un caracter no en blanco.
     */
    boolean validarNombre(String nombre);

    /**
     * Un precio es valido si es estrictamente mayor que cero.
     */
    boolean validarPrecio(double precio);

    /**
     * Un stock es valido si es mayor o igual a cero (no se permiten valores negativos).
     */
    boolean validarStock(int stock);

    /**
     * Calcula el precio final aplicando un descuento porcentual (0-100).
     */
    double calcularPrecioFinal(double precio, double descuentoPorcentaje);

    /**
     * Un producto tiene stock disponible si su cantidad es mayor que cero.
     */
    boolean tieneStockDisponible(int stock);
}
