/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "DETALLEFACTURA")
@NamedQueries({
    @NamedQuery(name = "Detallefactura.findAll", query = "SELECT d FROM Detallefactura d"),
    @NamedQuery(name = "Detallefactura.findByDetId", query = "SELECT d FROM Detallefactura d WHERE d.detId = :detId"),
    @NamedQuery(name = "Detallefactura.findByProdNombre", query = "SELECT d FROM Detallefactura d WHERE d.prodNombre = :prodNombre"),
    @NamedQuery(name = "Detallefactura.findByCantidad", query = "SELECT d FROM Detallefactura d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallefactura.findByProdPvp", query = "SELECT d FROM Detallefactura d WHERE d.prodPvp = :prodPvp"),
    @NamedQuery(name = "Detallefactura.findByIva", query = "SELECT d FROM Detallefactura d WHERE d.iva = :iva"),
    @NamedQuery(name = "Detallefactura.findByTotal", query = "SELECT d FROM Detallefactura d WHERE d.total = :total")})
public class Detallefactura implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detallefactura_seq")
    @SequenceGenerator(name = "detallefactura_seq", sequenceName = "DETALLEFACTURA_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "DET_ID")
    private BigDecimal detId;
    @Column(name = "PROD_NOMBRE")
    private String prodNombre;
    @Column(name = "CANTIDAD")
    private BigInteger cantidad;
    @Column(name = "PROD_PVP")
    private BigDecimal prodPvp;
    @Column(name = "IVA")
    private BigDecimal iva;
    @Column(name = "TOTAL")
    private BigDecimal total;
    @JoinColumn(name = "FAC_ID", referencedColumnName = "FAC_ID")
    @ManyToOne
    private Factura facId;
    @JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID")
    @ManyToOne
    private Producto prodId;

    public Detallefactura() {
    }

    public Detallefactura(BigDecimal detId) {
        this.detId = detId;
    }

    public BigDecimal getDetId() {
        return detId;
    }

    public void setDetId(BigDecimal detId) {
        this.detId = detId;
    }

    public String getProdNombre() {
        return prodNombre;
    }

    public void setProdNombre(String prodNombre) {
        this.prodNombre = prodNombre;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getProdPvp() {
        return prodPvp;
    }

    public void setProdPvp(BigDecimal prodPvp) {
        this.prodPvp = prodPvp;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Factura getFacId() {
        return facId;
    }

    public void setFacId(Factura facId) {
        this.facId = facId;
    }

    public Producto getProdId() {
        return prodId;
    }

    public void setProdId(Producto prodId) {
        this.prodId = prodId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detId != null ? detId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallefactura)) {
            return false;
        }
        Detallefactura other = (Detallefactura) object;
        if ((this.detId == null && other.detId != null) || (this.detId != null && !this.detId.equals(other.detId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Detallefactura[ detId=" + detId + " ]";
    }
    
    public BigDecimal sumartotal(BigInteger cantidad, BigDecimal precio) {
        // Usamos el método .multiply() y convertimos 'cantidad'
        return precio.multiply(new BigDecimal(cantidad));
    }
    
    public BigDecimal calcularIva(BigDecimal iva, BigDecimal producto){
        return iva.multiply(producto);
    }
    /**
    * Calcula el subtotal de una fila (Cantidad * Precio).
    * Esta es una función de ayuda para el controlador.
    * * @param cantidad La cantidad de productos (puede ser null).
    * @param precio El precio unitario (puede ser null).
    * @return El subtotal (cantidad * precio), o BigDecimal.ZERO si algo es null.
    */
    public BigDecimal calcularSubtotalFila(BigInteger cantidad, BigDecimal precio) {
        // Manejar nulos para evitar un error (NullPointerException)
        if (cantidad == null || precio == null) {
            return BigDecimal.ZERO;
        }

        // Convertir la cantidad BigInteger a BigDecimal para poder multiplicar
        BigDecimal bigDecimalCantidad = new BigDecimal(cantidad);

        // Realizar la multiplicación y devolver el resultado
        return precio.multiply(bigDecimalCantidad);
    }
}
