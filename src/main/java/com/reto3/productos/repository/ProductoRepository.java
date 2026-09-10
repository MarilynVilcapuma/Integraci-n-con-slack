package com.reto3.productos.repository;

import com.reto3.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de persistencia de Producto sobre PostgreSQL (Spring Data JPA).
 * En las pruebas unitarias de la capa de servicio se sustituye por un Mock de Mockito
 * para aislar la logica de negocio de la base de datos real.
 */
public interface ProductoRepository extends JpaRepository<Producto, String> {
}
