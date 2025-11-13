/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "PRODUCTO")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByProdId", query = "SELECT p FROM Producto p WHERE p.prodId = :prodId"),
    @NamedQuery(name = "Producto.findByProdCod", query = "SELECT p FROM Producto p WHERE p.prodCod = :prodCod"),
    @NamedQuery(name = "Producto.findByProdNombre", query = "SELECT p FROM Producto p WHERE p.prodNombre = :prodNombre"),
    @NamedQuery(name = "Producto.findByProdPreciocompra", query = "SELECT p FROM Producto p WHERE p.prodPreciocompra = :prodPreciocompra"),
    @NamedQuery(name = "Producto.findByProdPvpxmenor", query = "SELECT p FROM Producto p WHERE p.prodPvpxmenor = :prodPvpxmenor"),
    @NamedQuery(name = "Producto.findByProdPvpxmayor", query = "SELECT p FROM Producto p WHERE p.prodPvpxmayor = :prodPvpxmayor"),
    @NamedQuery(name = "Producto.findByProdStock", query = "SELECT p FROM Producto p WHERE p.prodStock = :prodStock"),
    @NamedQuery(name = "Producto.findByProdAplicaiva", query = "SELECT p FROM Producto p WHERE p.prodAplicaiva = :prodAplicaiva"),
    @NamedQuery(name = "Producto.findByProdEstado", query = "SELECT p FROM Producto p WHERE p.prodEstado = :prodEstado")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq")
    @SequenceGenerator(name = "producto_seq", sequenceName = "PRODUCTO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "PROD_ID")
    private BigInteger prodId;
    @Column(name = "PROD_COD")
    private String prodCod;
    @Column(name = "PROD_NOMBRE")
    private String prodNombre;
    @Column(name = "PROD_PRECIOCOMPRA")
    private BigDecimal prodPreciocompra;
    @Column(name = "PROD_PVPXMENOR")
    private BigDecimal prodPvpxmenor;
    @Column(name = "PROD_PVPXMAYOR")
    private BigDecimal prodPvpxmayor;
    @Column(name = "PROD_STOCK")
    private BigDecimal prodStock;
    @Column(name = "PROD_APLICAIVA")
    private Short prodAplicaiva;
    @Lob
    @Column(name = "POD_IMAGEN")
    private byte[] podImagen;
    
    
    @Column(name = "PROD_ESTADO")
    private String prodEstado;
    @OneToMany(mappedBy = "prodId")
    private Collection<Detallefactura> detallefacturaCollection;

    public Producto() {
    }

    public Producto(BigInteger prodId) {
        this.prodId = prodId;
    }

    public BigInteger getProdId() {
        return prodId;
    }

    public void setProdId(BigInteger prodId) {
        this.prodId = prodId;
    }

    public String getProdCod() {
        return prodCod;
    }

    public void setProdCod(String prodCod) {
        this.prodCod = prodCod;
    }

    public String getProdNombre() {
        return prodNombre;
    }

    public void setProdNombre(String prodNombre) {
        this.prodNombre = prodNombre;
    }

    public BigDecimal getProdPreciocompra() {
        return prodPreciocompra;
    }

    public void setProdPreciocompra(BigDecimal prodPreciocompra) {
        this.prodPreciocompra = prodPreciocompra;
    }

    public BigDecimal getProdPvpxmenor() {
        return prodPvpxmenor;
    }

    public void setProdPvpxmenor(BigDecimal prodPvpxmenor) {
        this.prodPvpxmenor = prodPvpxmenor;
    }

    public BigDecimal getProdPvpxmayor() {
        return prodPvpxmayor;
    }

    public void setProdPvpxmayor(BigDecimal prodPvpxmayor) {
        this.prodPvpxmayor = prodPvpxmayor;
    }

    public BigDecimal getProdStock() {
        return prodStock;
    }

    public void setProdStock(BigDecimal prodStock) {
        this.prodStock = prodStock;
    }

    public Short getProdAplicaiva() {
        return prodAplicaiva;
    }

    public void setProdAplicaiva(Short prodAplicaiva) {
        this.prodAplicaiva = prodAplicaiva;
    }

    public byte[] getPodImagen() {
        return podImagen;
    }

    public void setPodImagen(byte[] podImagen) {
        this.podImagen = podImagen;
    }

    public String getProdEstado() {
        return prodEstado;
    }

    public void setProdEstado(String prodEstado) {
        this.prodEstado = prodEstado;
    }

    public Collection<Detallefactura> getDetallefacturaCollection() {
        return detallefacturaCollection;
    }

    public void setDetallefacturaCollection(Collection<Detallefactura> detallefacturaCollection) {
        this.detallefacturaCollection = detallefacturaCollection;
    }

    public double precioPorIva(double precio, double iva ){
        return precio * iva;
    }
    

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prodId != null ? prodId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.prodId == null && other.prodId != null) || (this.prodId != null && !this.prodId.equals(other.prodId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Producto[ prodId=" + prodId + " ]";
    }
    
}
