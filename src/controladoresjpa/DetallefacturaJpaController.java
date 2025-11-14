/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladoresjpa;

import modelos.Detallefactura;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import util.JPAUtil;

/**
 *
 * @author usuario
 */
public class DetallefacturaJpaController implements Serializable {

    public DetallefacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallefactura detallefactura) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(detallefactura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallefactura detallefactura) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            detallefactura = em.merge(detallefactura);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = detallefactura.getDetId();
                if (findDetallefactura(id) == null) {
                    throw new Exception("The detallefactura with id " + id + " no longer exists.");
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
            Detallefactura detallefactura;
            try {
                detallefactura = em.getReference(Detallefactura.class, id);
                detallefactura.getDetId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The detallefactura with id " + id + " no longer exists.", enfe);
            }
            em.remove(detallefactura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallefactura> findDetallefacturaEntities() {
        return findDetallefacturaEntities(true, -1, -1);
    }

    public List<Detallefactura> findDetallefacturaEntities(int maxResults, int firstResult) {
        return findDetallefacturaEntities(false, maxResults, firstResult);
    }

    private List<Detallefactura> findDetallefacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallefactura.class));
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

    public Detallefactura findDetallefactura(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallefactura.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallefacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallefactura> rt = cq.from(Detallefactura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    // Métodos específicos usando NamedQueries
    public List<Detallefactura> findDetallefacturaByFactura(BigDecimal facId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT d FROM Detallefactura d WHERE d.facId.facId = :facId");
            q.setParameter("facId", facId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Detallefactura> findDetallefacturaByProducto(BigDecimal prodId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT d FROM Detallefactura d WHERE d.prodId.prodId = :prodId");
            q.setParameter("prodId", prodId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Detallefactura> findDetallefacturaByNombreProducto(String prodNombre) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Detallefactura.findByProdNombre");
            q.setParameter("prodNombre", prodNombre);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public BigDecimal getTotalVentasPorProducto(BigDecimal prodId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT SUM(d.total) FROM Detallefactura d WHERE d.prodId.prodId = :prodId");
            q.setParameter("prodId", prodId);
            Object result = q.getSingleResult();
            return result != null ? (BigDecimal) result : BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    public List<Object[]> getProductosMasVendidos() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery(
                "SELECT d.prodId, d.prodNombre, SUM(d.cantidad), SUM(d.total) " +
                "FROM Detallefactura d " +
                "GROUP BY d.prodId, d.prodNombre " +
                "ORDER BY SUM(d.cantidad) DESC"
            );
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Collection<Detallefactura> getDetallesporId(BigDecimal facId) {
        EntityManager em = null;
        Collection<Detallefactura> detalles = new ArrayList<>();

        try {
            em = JPAUtil.getEntityManagerFactory().createEntityManager();

            // Consulta para obtener todos los detalles de una factura específica
            String jpql = "SELECT d FROM Detallefactura d " +
                         "LEFT JOIN FETCH d.prodId " +
                         "WHERE d.facId.facId = :facId " +
                         "ORDER BY d.detId";

            TypedQuery<Detallefactura> query = em.createQuery(jpql, Detallefactura.class);
            query.setParameter("facId", facId);

            detalles = query.getResultList();

        } catch (Exception e) {
            System.err.println("Error al obtener detalles por ID de factura: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return detalles;
    }
    
}