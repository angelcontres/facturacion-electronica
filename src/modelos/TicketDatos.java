/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.math.BigDecimal;
import java.util.Collection;
import modelos.Cliente;
import modelos.Detallefactura;

/**
 *
 * @author usuario
 */


/**
 * DTO (Objeto de Transferencia de Datos).
 * Esta clase es una "caja" simple para enviar todos los datos 
 * al servicio de impresión de una sola vez.
 */
public class TicketDatos {
    
    // Datos de la cabecera
    private String numeroFactura;
    private String fecha; // Puedes usar String si ya está formateada
    
    // Datos del Cliente
    private Cliente cliente;
    
    // Detalles
    private Collection<Detallefactura> detalles;
    
    // Totales (¡Siempre BigDecimal para dinero!)
    private BigDecimal subtotal;
    private BigDecimal subtotal0;
    private BigDecimal iva;
    private BigDecimal descuento;
    private BigDecimal total;

    // --- Getters y Setters (Generados con Alt+Insert) ---

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Collection<Detallefactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<Detallefactura> detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getSubtotal0() {
        return subtotal0;
    }

    public void setSubtotal0(BigDecimal subtotal0) {
        this.subtotal0 = subtotal0;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}