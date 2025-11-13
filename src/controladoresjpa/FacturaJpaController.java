/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladoresjpa;

import modelos.Factura;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author usuario
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            factura = em.merge(factura);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = factura.getFacId();
                if (findFactura(id) == null) {
                    throw new Exception("The factura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getFacId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The factura with id " + id + " no longer exists.", enfe);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Factura findFactura(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    // Métodos específicos usando NamedQueries
    public List<Factura> findFacturaByNumero(String facNumero) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Factura.findByFacNumero");
            q.setParameter("facNumero", facNumero);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Factura> findFacturaByEstado(String facEstado) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Factura.findByFacEstado");
            q.setParameter("facEstado", facEstado);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Factura> findFacturaByCliente(Object cliId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT f FROM Factura f WHERE f.cliId = :cliId");
            q.setParameter("cliId", cliId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Factura> findFacturaByFecha(Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT f FROM Factura f WHERE f.facFecha BETWEEN :fechaInicio AND :fechaFin");
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}