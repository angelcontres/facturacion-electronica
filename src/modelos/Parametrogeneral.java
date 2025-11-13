/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "PARAMETROGENERAL")
@NamedQueries({
    @NamedQuery(name = "Parametrogeneral.findAll", query = "SELECT p FROM Parametrogeneral p"),
    @NamedQuery(name = "Parametrogeneral.findByIdparametro", query = "SELECT p FROM Parametrogeneral p WHERE p.idparametro = :idparametro"),
    @NamedQuery(name = "Parametrogeneral.findByClave", query = "SELECT p FROM Parametrogeneral p WHERE p.clave = :clave"),
    @NamedQuery(name = "Parametrogeneral.findByDescripcion", query = "SELECT p FROM Parametrogeneral p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Parametrogeneral.findByValornumerico", query = "SELECT p FROM Parametrogeneral p WHERE p.valornumerico = :valornumerico"),
    @NamedQuery(name = "Parametrogeneral.findByEstado", query = "SELECT p FROM Parametrogeneral p WHERE p.estado = :estado")})
public class Parametrogeneral implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parametrogeneral_seq")
    @SequenceGenerator(name = "parametrogeneral_seq", sequenceName = "PARAMETROGENERAL_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDPARAMETRO")
    private BigDecimal idparametro;
    @Basic(optional = false)
    @Column(name = "CLAVE")
    private String clave;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "VALORNUMERICO")
    private BigDecimal valornumerico;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private short estado;

    public Parametrogeneral() {
    }

    public Parametrogeneral(BigDecimal idparametro) {
        this.idparametro = idparametro;
    }

    public Parametrogeneral(BigDecimal idparametro, String clave, BigDecimal valornumerico, short estado) {
        this.idparametro = idparametro;
        this.clave = clave;
        this.valornumerico = valornumerico;
        this.estado = estado;
    }

    public BigDecimal getIdparametro() {
        return idparametro;
    }

    public void setIdparametro(BigDecimal idparametro) {
        this.idparametro = idparametro;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValornumerico() {
        return valornumerico;
    }

    public void setValornumerico(BigDecimal valornumerico) {
        this.valornumerico = valornumerico;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idparametro != null ? idparametro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametrogeneral)) {
            return false;
        }
        Parametrogeneral other = (Parametrogeneral) object;
        if ((this.idparametro == null && other.idparametro != null) || (this.idparametro != null && !this.idparametro.equals(other.idparametro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Parametrogeneral[ idparametro=" + idparametro + " ]";
    }
    
}
