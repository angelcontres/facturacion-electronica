/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "EMPRESA")
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByEmpRuc", query = "SELECT e FROM Empresa e WHERE e.empRuc = :empRuc"),
    @NamedQuery(name = "Empresa.findByEmpNombre", query = "SELECT e FROM Empresa e WHERE e.empNombre = :empNombre"),
    @NamedQuery(name = "Empresa.findByEmpDireccion", query = "SELECT e FROM Empresa e WHERE e.empDireccion = :empDireccion"),
    @NamedQuery(name = "Empresa.findByEmpTelefono", query = "SELECT e FROM Empresa e WHERE e.empTelefono = :empTelefono"),
    @NamedQuery(name = "Empresa.findByEmpEstado", query = "SELECT e FROM Empresa e WHERE e.empEstado = :empEstado")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "EMP_RUC")
    private String empRuc;
    @Column(name = "EMP_NOMBRE")
    private String empNombre;
    @Column(name = "EMP_DIRECCION")
    private String empDireccion;
    @Column(name = "EMP_TELEFONO")
    private String empTelefono;
    @Column(name = "EMP_ESTADO")
    private String empEstado;

    public Empresa() {
    }

    public Empresa(String empRuc) {
        this.empRuc = empRuc;
    }

    public Empresa(String empRuc, String empNombre, String empDireccion, String empTelefono, String empEstado) {
        this.empRuc = empRuc;
        this.empNombre = empNombre;
        this.empDireccion = empDireccion;
        this.empTelefono = empTelefono;
        this.empEstado = empEstado;
    }
    
    

    public String getEmpRuc() {
        return empRuc;
    }

    public void setEmpRuc(String empRuc) {
        this.empRuc = empRuc;
    }

    public String getEmpNombre() {
        return empNombre;
    }

    public void setEmpNombre(String empNombre) {
        this.empNombre = empNombre;
    }

    public String getEmpDireccion() {
        return empDireccion;
    }

    public void setEmpDireccion(String empDireccion) {
        this.empDireccion = empDireccion;
    }

    public String getEmpTelefono() {
        return empTelefono;
    }

    public void setEmpTelefono(String empTelefono) {
        this.empTelefono = empTelefono;
    }

    public String getEmpEstado() {
        return empEstado;
    }

    public void setEmpEstado(String empEstado) {
        this.empEstado = empEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empRuc != null ? empRuc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.empRuc == null && other.empRuc != null) || (this.empRuc != null && !this.empRuc.equals(other.empRuc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Empresa[ empRuc=" + empRuc + " ]";
    }
    
}
