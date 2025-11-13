/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "FACTURA")
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findByFacId", query = "SELECT f FROM Factura f WHERE f.facId = :facId"),
    @NamedQuery(name = "Factura.findByFacNumero", query = "SELECT f FROM Factura f WHERE f.facNumero = :facNumero"),
    @NamedQuery(name = "Factura.findByFacFecha", query = "SELECT f FROM Factura f WHERE f.facFecha = :facFecha"),
    @NamedQuery(name = "Factura.findByFacSubtotal", query = "SELECT f FROM Factura f WHERE f.facSubtotal = :facSubtotal"),
    @NamedQuery(name = "Factura.findByFacSubtotalcero", query = "SELECT f FROM Factura f WHERE f.facSubtotalcero = :facSubtotalcero"),
    @NamedQuery(name = "Factura.findByFacIva", query = "SELECT f FROM Factura f WHERE f.facIva = :facIva"),
    @NamedQuery(name = "Factura.findByFacDescuento", query = "SELECT f FROM Factura f WHERE f.facDescuento = :facDescuento"),
    @NamedQuery(name = "Factura.findByFacTotal", query = "SELECT f FROM Factura f WHERE f.facTotal = :facTotal"),
    @NamedQuery(name = "Factura.findByFeClave", query = "SELECT f FROM Factura f WHERE f.feClave = :feClave"),
    @NamedQuery(name = "Factura.findByFeAutorizacion", query = "SELECT f FROM Factura f WHERE f.feAutorizacion = :feAutorizacion"),
    @NamedQuery(name = "Factura.findByFeFechaAut", query = "SELECT f FROM Factura f WHERE f.feFechaAut = :feFechaAut"),
    @NamedQuery(name = "Factura.findByAudMacCrea", query = "SELECT f FROM Factura f WHERE f.audMacCrea = :audMacCrea"),
    @NamedQuery(name = "Factura.findByAudFechaCreacion", query = "SELECT f FROM Factura f WHERE f.audFechaCreacion = :audFechaCreacion"),
    @NamedQuery(name = "Factura.findByAudHoraCreacion", query = "SELECT f FROM Factura f WHERE f.audHoraCreacion = :audHoraCreacion"),
    @NamedQuery(name = "Factura.findByAudMacModifica", query = "SELECT f FROM Factura f WHERE f.audMacModifica = :audMacModifica"),
    @NamedQuery(name = "Factura.findByAudFechaModificacion", query = "SELECT f FROM Factura f WHERE f.audFechaModificacion = :audFechaModificacion"),
    @NamedQuery(name = "Factura.findByAudHoraModificacion", query = "SELECT f FROM Factura f WHERE f.audHoraModificacion = :audHoraModificacion"),
    @NamedQuery(name = "Factura.findByFacEstado", query = "SELECT f FROM Factura f WHERE f.facEstado = :facEstado")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factura_seq")
    @SequenceGenerator(name = "factura_seq", sequenceName = "FACTURA_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "FAC_ID")
    private BigDecimal facId;
    @Basic(optional = false)
    @Column(name = "FAC_NUMERO")
    private String facNumero;
    @Column(name = "FAC_FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date facFecha;
    @Basic(optional = false)
    @Column(name = "FAC_SUBTOTAL")
    private BigDecimal facSubtotal;
    @Basic(optional = false)
    @Column(name = "FAC_SUBTOTALCERO")
    private BigDecimal facSubtotalcero;
    @Basic(optional = false)
    @Column(name = "FAC_IVA")
    private BigDecimal facIva;
    @Basic(optional = false)
    @Column(name = "FAC_DESCUENTO")
    private BigDecimal facDescuento;
    @Basic(optional = false)
    @Column(name = "FAC_TOTAL")
    private BigDecimal facTotal;
    @Column(name = "FE_CLAVE")
    private String feClave;
    @Column(name = "FE_AUTORIZACION")
    private String feAutorizacion;
    @Column(name = "FE_FECHA_AUT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feFechaAut;
    @Column(name = "AUD_MAC_CREA")
    private String audMacCrea;
    @Column(name = "AUD_FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date audFechaCreacion;
    @Column(name = "AUD_HORA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date audHoraCreacion;
    @Column(name = "AUD_MAC_MODIFICA")
    private String audMacModifica;
    @Column(name = "AUD_FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date audFechaModificacion;
    @Column(name = "AUD_HORA_MODIFICACION")
    private String audHoraModificacion;
    @Column(name = "FAC_ESTADO")
    private String facEstado;
    @OneToMany(mappedBy = "facId")
    private Collection<Detallefactura> detallefacturaCollection;
    @JoinColumn(name = "CLI_ID", referencedColumnName = "CLI_ID")
    @ManyToOne
    private Cliente cliId;
    @JoinColumn(name = "AUD_USR_ID_MODIFICA", referencedColumnName = "USR_ID")
    @ManyToOne
    private Usuario audUsrIdModifica;
    @JoinColumn(name = "AUD_USR_ID_CREA", referencedColumnName = "USR_ID")
    @ManyToOne
    private Usuario audUsrIdCrea;

    public Factura() {
    }

    public Factura(BigDecimal facId) {
        this.facId = facId;
    }

    public Factura(BigDecimal facId, String facNumero, BigDecimal facSubtotal, BigDecimal facSubtotalcero, BigDecimal facIva, BigDecimal facDescuento, BigDecimal facTotal) {
        this.facId = facId;
        this.facNumero = facNumero;
        this.facSubtotal = facSubtotal;
        this.facSubtotalcero = facSubtotalcero;
        this.facIva = facIva;
        this.facDescuento = facDescuento;
        this.facTotal = facTotal;
    }

    public BigDecimal getFacId() {
        return facId;
    }

    public void setFacId(BigDecimal facId) {
        this.facId = facId;
    }

    public String getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(String facNumero) {
        this.facNumero = facNumero;
    }

    public Date getFacFecha() {
        return facFecha;
    }

    public void setFacFecha(Date facFecha) {
        this.facFecha = facFecha;
    }

    public BigDecimal getFacSubtotal() {
        return facSubtotal;
    }

    public void setFacSubtotal(BigDecimal facSubtotal) {
        this.facSubtotal = facSubtotal;
    }

    public BigDecimal getFacSubtotalcero() {
        return facSubtotalcero;
    }

    public void setFacSubtotalcero(BigDecimal facSubtotalcero) {
        this.facSubtotalcero = facSubtotalcero;
    }

    public BigDecimal getFacIva() {
        return facIva;
    }

    public void setFacIva(BigDecimal facIva) {
        this.facIva = facIva;
    }

    public BigDecimal getFacDescuento() {
        return facDescuento;
    }

    public void setFacDescuento(BigDecimal facDescuento) {
        this.facDescuento = facDescuento;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public String getFeClave() {
        return feClave;
    }

    public void setFeClave(String feClave) {
        this.feClave = feClave;
    }

    public String getFeAutorizacion() {
        return feAutorizacion;
    }

    public void setFeAutorizacion(String feAutorizacion) {
        this.feAutorizacion = feAutorizacion;
    }

    public Date getFeFechaAut() {
        return feFechaAut;
    }

    public void setFeFechaAut(Date feFechaAut) {
        this.feFechaAut = feFechaAut;
    }

    public String getAudMacCrea() {
        return audMacCrea;
    }

    public void setAudMacCrea(String audMacCrea) {
        this.audMacCrea = audMacCrea;
    }

    public Date getAudFechaCreacion() {
        return audFechaCreacion;
    }

    public void setAudFechaCreacion(Date audFechaCreacion) {
        this.audFechaCreacion = audFechaCreacion;
    }

    public Date getAudHoraCreacion() {
        return audHoraCreacion;
    }

    public void setAudHoraCreacion(Date audHoraCreacion) {
        this.audHoraCreacion = audHoraCreacion;
    }

    public String getAudMacModifica() {
        return audMacModifica;
    }

    public void setAudMacModifica(String audMacModifica) {
        this.audMacModifica = audMacModifica;
    }

    public Date getAudFechaModificacion() {
        return audFechaModificacion;
    }

    public void setAudFechaModificacion(Date audFechaModificacion) {
        this.audFechaModificacion = audFechaModificacion;
    }

    public String getAudHoraModificacion() {
        return audHoraModificacion;
    }

    public void setAudHoraModificacion(String audHoraModificacion) {
        this.audHoraModificacion = audHoraModificacion;
    }

    public String getFacEstado() {
        return facEstado;
    }

    public void setFacEstado(String facEstado) {
        this.facEstado = facEstado;
    }

    public Collection<Detallefactura> getDetallefacturaCollection() {
        return detallefacturaCollection;
    }

    public void setDetallefacturaCollection(Collection<Detallefactura> detallefacturaCollection) {
        this.detallefacturaCollection = detallefacturaCollection;
    }

    public Cliente getCliId() {
        return cliId;
    }

    public void setCliId(Cliente cliId) {
        this.cliId = cliId;
    }

    public Usuario getAudUsrIdModifica() {
        return audUsrIdModifica;
    }

    public void setAudUsrIdModifica(Usuario audUsrIdModifica) {
        this.audUsrIdModifica = audUsrIdModifica;
    }

    public Usuario getAudUsrIdCrea() {
        return audUsrIdCrea;
    }

    public void setAudUsrIdCrea(Usuario audUsrIdCrea) {
        this.audUsrIdCrea = audUsrIdCrea;
    }
    
    public Double sumarProductos(double subtotal, double subtotal0, double iva, double descuento){
        
        return subtotal + subtotal0 + iva - descuento;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facId != null ? facId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.facId == null && other.facId != null) || (this.facId != null && !this.facId.equals(other.facId))) {
            return false;
        }
        return true;
    }    
    
    @Override
    public String toString() {
        return "modelos.Factura[ facId=" + facId + " ]";
    }
    
}
