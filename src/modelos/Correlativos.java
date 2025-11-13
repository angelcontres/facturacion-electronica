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
@Table(name = "CORRELATIVOS")
@NamedQueries({
    @NamedQuery(name = "Correlativos.findAll", query = "SELECT c FROM Correlativos c"),
    @NamedQuery(name = "Correlativos.findByCorrelativo", query = "SELECT c FROM Correlativos c WHERE c.correlativo = :correlativo"),
    @NamedQuery(name = "Correlativos.findByMovimiento", query = "SELECT c FROM Correlativos c WHERE c.movimiento = :movimiento"),
    @NamedQuery(name = "Correlativos.findByMes", query = "SELECT c FROM Correlativos c WHERE c.mes = :mes"),
    @NamedQuery(name = "Correlativos.findBySecuencia", query = "SELECT c FROM Correlativos c WHERE c.secuencia = :secuencia"),
    @NamedQuery(name = "Correlativos.findByTerminal", query = "SELECT c FROM Correlativos c WHERE c.terminal = :terminal"),
    @NamedQuery(name = "Correlativos.findByAnio", query = "SELECT c FROM Correlativos c WHERE c.anio = :anio"),
    @NamedQuery(name = "Correlativos.findByEstado", query = "SELECT c FROM Correlativos c WHERE c.estado = :estado"),
    @NamedQuery(name = "Correlativos.findByEstablecimiento", query = "SELECT c FROM Correlativos c WHERE c.establecimiento = :establecimiento"),
    @NamedQuery(name = "Correlativos.findByPuntoEmision", query = "SELECT c FROM Correlativos c WHERE c.puntoEmision = :puntoEmision")})
public class Correlativos implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "correlativos_seq")
    @SequenceGenerator(name = "correlativos_seq", sequenceName = "CORRELATIVOS_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "CORRELATIVO")
    private BigDecimal correlativo;
    @Column(name = "MOVIMIENTO")
    private String movimiento;
    @Column(name = "MES")
    private BigInteger mes;
    @Column(name = "SECUENCIA")
    private Long secuencia;
    @Column(name = "TERMINAL")
    private Long terminal;
    @Column(name = "ANIO")
    private Long anio;
    @Column(name = "ESTADO")
    private Character estado;
    @Column(name = "ESTABLECIMIENTO")
    private String establecimiento;
    @Column(name = "PUNTO_EMISION")
    private String puntoEmision;

    public Correlativos() {
    }

    public Correlativos(BigDecimal correlativo) {
        this.correlativo = correlativo;
    }

    public BigDecimal getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(BigDecimal correlativo) {
        this.correlativo = correlativo;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public BigInteger getMes() {
        return mes;
    }

    public void setMes(BigInteger mes) {
        this.mes = mes;
    }

    public Long getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Long secuencia) {
        this.secuencia = secuencia;
    }

    public Long getTerminal() {
        return terminal;
    }

    public void setTerminal(Long terminal) {
        this.terminal = terminal;
    }

    public Long getAnio() {
        return anio;
    }

    public void setAnio(Long anio) {
        this.anio = anio;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (correlativo != null ? correlativo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Correlativos)) {
            return false;
        }
        Correlativos other = (Correlativos) object;
        if ((this.correlativo == null && other.correlativo != null) || (this.correlativo != null && !this.correlativo.equals(other.correlativo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Correlativos[ correlativo=" + correlativo + " ]";
    }
    
}
