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
@Table(name = "CLIENTE")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByCliId", query = "SELECT c FROM Cliente c WHERE c.cliId = :cliId"),
    @NamedQuery(name = "Cliente.findByCliCedula", query = "SELECT c FROM Cliente c WHERE c.cliCedula = :cliCedula"),
    @NamedQuery(name = "Cliente.findByCliNombres", query = "SELECT c FROM Cliente c WHERE c.cliNombres = :cliNombres"),
    @NamedQuery(name = "Cliente.findByCliApellidos", query = "SELECT c FROM Cliente c WHERE c.cliApellidos = :cliApellidos"),
    @NamedQuery(name = "Cliente.findByCliDireccion", query = "SELECT c FROM Cliente c WHERE c.cliDireccion = :cliDireccion"),
    @NamedQuery(name = "Cliente.findByCliTelefono", query = "SELECT c FROM Cliente c WHERE c.cliTelefono = :cliTelefono"),
    @NamedQuery(name = "Cliente.findByCliCorreo", query = "SELECT c FROM Cliente c WHERE c.cliCorreo = :cliCorreo"),
    @NamedQuery(name = "Cliente.findByCliEstado", query = "SELECT c FROM Cliente c WHERE c.cliEstado = :cliEstado")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name = "cliente_seq", sequenceName = "CLIENTE_SEQ", allocationSize = 1)
    @Column(name = "CLI_ID")
    private BigDecimal cliId;
    @Column(name = "CLI_CEDULA")
    private String cliCedula;
    @Column(name = "CLI_NOMBRES")
    private String cliNombres;
    @Column(name = "CLI_APELLIDOS")
    private String cliApellidos;
    @Column(name = "CLI_DIRECCION")
    private String cliDireccion;
    @Column(name = "CLI_TELEFONO")
    private String cliTelefono;
    @Column(name = "CLI_CORREO")
    private String cliCorreo;
    @Column(name = "CLI_ESTADO")
    private String cliEstado;
    @OneToMany(mappedBy = "cliId")
    private Collection<Factura> facturaCollection;

    public Cliente() {
    }

    public Cliente(BigDecimal cliId) {
        this.cliId = cliId;
    }

    public Cliente(String cliCedula, String cliNombres, String cliApellidos, String cliDireccion, String cliTelefono, String cliCorreo, String cliEstado) {
//        this.cliId = cliId;
        this.cliCedula = cliCedula;
        this.cliNombres = cliNombres;
        this.cliApellidos = cliApellidos;
        this.cliDireccion = cliDireccion;
        this.cliTelefono = cliTelefono;
        this.cliCorreo = cliCorreo;
        this.cliEstado = cliEstado;
//        this.facturaCollection = facturaCollection;
    }
    
    public BigDecimal getCliId() {
        return cliId;
    }

    public void setCliId(BigDecimal cliId) {
        this.cliId = cliId;
    }

    public String getCliCedula() {
        return cliCedula;
    }

    public void setCliCedula(String cliCedula) {
        this.cliCedula = cliCedula;
    }

    public String getCliNombres() {
        return cliNombres;
    }

    public void setCliNombres(String cliNombres) {
        this.cliNombres = cliNombres;
    }

    public String getCliApellidos() {
        return cliApellidos;
    }

    public void setCliApellidos(String cliApellidos) {
        this.cliApellidos = cliApellidos;
    }

    public String getCliDireccion() {
        return cliDireccion;
    }

    public void setCliDireccion(String cliDireccion) {
        this.cliDireccion = cliDireccion;
    }

    public String getCliTelefono() {
        return cliTelefono;
    }

    public void setCliTelefono(String cliTelefono) {
        this.cliTelefono = cliTelefono;
    }

    public String getCliCorreo() {
        return cliCorreo;
    }

    public void setCliCorreo(String cliCorreo) {
        this.cliCorreo = cliCorreo;
    }

    public String getCliEstado() {
        return cliEstado;
    }

    public void setCliEstado(String cliEstado) {
        this.cliEstado = cliEstado;
    }

    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cliId != null ? cliId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.cliId == null && other.cliId != null) || (this.cliId != null && !this.cliId.equals(other.cliId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Cliente[ cliId=" + cliId + " ]";
    }
    
}
