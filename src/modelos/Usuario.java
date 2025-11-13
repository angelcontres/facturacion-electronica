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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "USUARIO")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByUsrId", query = "SELECT u FROM Usuario u WHERE u.usrId = :usrId"),
    @NamedQuery(name = "Usuario.findByUsrNombres", query = "SELECT u FROM Usuario u WHERE u.usrNombres = :usrNombres"),
    @NamedQuery(name = "Usuario.findByUsrUsuario", query = "SELECT u FROM Usuario u WHERE u.usrUsuario = :usrUsuario"),
    @NamedQuery(name = "Usuario.findByUsrEstado", query = "SELECT u FROM Usuario u WHERE u.usrEstado = :usrEstado")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "USUARIO_SEQ", allocationSize = 1)
    @Column(name = "USR_ID")
    @Basic(optional = false)
    private BigDecimal usrId;
    @Column(name = "USR_NOMBRES")
    private String usrNombres;
    @Basic(optional = false)
    @Column(name = "USR_USUARIO")
    private String usrUsuario;
    @Basic(optional = false)
    @Lob
    @Column(name = "USR_CLAVE")
    private String usrClave;
    @Column(name = "USR_ESTADO")
    private String usrEstado;
    @OneToMany(mappedBy = "audUsrIdModifica")
    private Collection<Factura> facturaCollection;
    @OneToMany(mappedBy = "audUsrIdCrea")
    private Collection<Factura> facturaCollection1;
    @JoinColumn(name = "PER_ID", referencedColumnName = "PER_ID")
    @ManyToOne
    private Perfil perId;

    public Usuario() {
    }

    public Usuario(BigDecimal usrId) {
        this.usrId = usrId;
    }

    public Usuario(BigDecimal usrId, String usrUsuario, String usrClave) {
        this.usrId = usrId;
        this.usrUsuario = usrUsuario;
        this.usrClave = usrClave;
    }

    public BigDecimal getUsrId() {
        return usrId;
    }

    public void setUsrId(BigDecimal usrId) {
        this.usrId = usrId;
    }

    public String getUsrNombres() {
        return usrNombres;
    }

    public void setUsrNombres(String usrNombres) {
        this.usrNombres = usrNombres;
    }

    public String getUsrUsuario() {
        return usrUsuario;
    }

    public void setUsrUsuario(String usrUsuario) {
        this.usrUsuario = usrUsuario;
    }

    public String getUsrClave() {
        return usrClave;
    }

    public void setUsrClave(String usrClave) {
        this.usrClave = usrClave;
    }

    public String getUsrEstado() {
        return usrEstado;
    }

    public void setUsrEstado(String usrEstado) {
        this.usrEstado = usrEstado;
    }

    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    public Collection<Factura> getFacturaCollection1() {
        return facturaCollection1;
    }

    public void setFacturaCollection1(Collection<Factura> facturaCollection1) {
        this.facturaCollection1 = facturaCollection1;
    }

    public Perfil getPerId() {
        return perId;
    }

    public void setPerId(Perfil perId) {
        this.perId = perId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usrId != null ? usrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usrId == null && other.usrId != null) || (this.usrId != null && !this.usrId.equals(other.usrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Usuario[ usrId=" + usrId + " ]";
    }
    
}
