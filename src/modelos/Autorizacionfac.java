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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "AUTORIZACIONFAC")
@NamedQueries({
    @NamedQuery(name = "Autorizacionfac.findAll", query = "SELECT a FROM Autorizacionfac a"),
    @NamedQuery(name = "Autorizacionfac.findByAutId", query = "SELECT a FROM Autorizacionfac a WHERE a.autId = :autId"),
    @NamedQuery(name = "Autorizacionfac.findByAutAutsri", query = "SELECT a FROM Autorizacionfac a WHERE a.autAutsri = :autAutsri"),
    @NamedQuery(name = "Autorizacionfac.findByAutDesde", query = "SELECT a FROM Autorizacionfac a WHERE a.autDesde = :autDesde"),
    @NamedQuery(name = "Autorizacionfac.findByAutHasta", query = "SELECT a FROM Autorizacionfac a WHERE a.autHasta = :autHasta"),
    @NamedQuery(name = "Autorizacionfac.findByAutEstado", query = "SELECT a FROM Autorizacionfac a WHERE a.autEstado = :autEstado")})
public class Autorizacionfac implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autorizacionfac_seq")
    @SequenceGenerator(name = "autorizacionfac_seq", sequenceName = "AUROTIZACIONFAC_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "AUT_ID")
    private BigDecimal autId;
    @Column(name = "AUT_AUTSRI")
    private String autAutsri;
    @Column(name = "AUT_DESDE")
    private BigInteger autDesde;
    @Column(name = "AUT_HASTA")
    private BigInteger autHasta;
    @Column(name = "AUT_ESTADO")
    private String autEstado;

    public Autorizacionfac() {
    }

    public Autorizacionfac(BigDecimal autId) {
        this.autId = autId;
    }

    public BigDecimal getAutId() {
        return autId;
    }

    public void setAutId(BigDecimal autId) {
        this.autId = autId;
    }

    public String getAutAutsri() {
        return autAutsri;
    }

    public void setAutAutsri(String autAutsri) {
        this.autAutsri = autAutsri;
    }

    public BigInteger getAutDesde() {
        return autDesde;
    }

    public void setAutDesde(BigInteger autDesde) {
        this.autDesde = autDesde;
    }

    public BigInteger getAutHasta() {
        return autHasta;
    }

    public void setAutHasta(BigInteger autHasta) {
        this.autHasta = autHasta;
    }

    public String getAutEstado() {
        return autEstado;
    }

    public void setAutEstado(String autEstado) {
        this.autEstado = autEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (autId != null ? autId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Autorizacionfac)) {
            return false;
        }
        Autorizacionfac other = (Autorizacionfac) object;
        if ((this.autId == null && other.autId != null) || (this.autId != null && !this.autId.equals(other.autId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Autorizacionfac[ autId=" + autId + " ]";
    }
    
}
