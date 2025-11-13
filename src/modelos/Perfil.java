/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "PERFIL")
@NamedQueries({
    @NamedQuery(name = "Perfil.findAll", query = "SELECT p FROM Perfil p"),
    @NamedQuery(name = "Perfil.findByPerId", query = "SELECT p FROM Perfil p WHERE p.perId = :perId"),
    @NamedQuery(name = "Perfil.findByPerDescripcion", query = "SELECT p FROM Perfil p WHERE p.perDescripcion = :perDescripcion"),
    @NamedQuery(name = "Perfil.findByPerEstado", query = "SELECT p FROM Perfil p WHERE p.perEstado = :perEstado")})
public class Perfil implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
     @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfil_seq")
    @SequenceGenerator(name = "perfil_seq", sequenceName = "PERFIL_SEQ", allocationSize = 1)
    @Column(name = "PER_ID")
    @Basic(optional = false)
    private BigDecimal perId;
    @Column(name = "PER_DESCRIPCION")
    private String perDescripcion;
    @Column(name = "PER_ESTADO")
    private String perEstado;
    @OneToMany(mappedBy = "perId")
    private Collection<Usuario> usuarioCollection;

    public Perfil() {
    }

    public Perfil(BigDecimal perId) {
        this.perId = perId;
    }

    public BigDecimal getPerId() {
        return perId;
    }

    public void setPerId(BigDecimal perId) {
        this.perId = perId;
    }

    public String getPerDescripcion() {
        return perDescripcion;
    }

    public void setPerDescripcion(String perDescripcion) {
        this.perDescripcion = perDescripcion;
    }

    public String getPerEstado() {
        return perEstado;
    }

    public void setPerEstado(String perEstado) {
        this.perEstado = perEstado;
    }

    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perId != null ? perId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
        if ((this.perId == null && other.perId != null) || (this.perId != null && !this.perId.equals(other.perId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.perDescripcion;
    }
    
}
