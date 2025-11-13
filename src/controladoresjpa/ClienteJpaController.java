/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladoresjpa;

import controladoresjpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import modelos.Cliente;

/**
 *
 * @author usuario
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cliente = em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = cliente.getCliId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getCliId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    // BÚSQUEDA EN TIEMPO REAL - OPTIMIZADA PARA TECLEO CONSTANTE
    public List<Cliente> buscarClientesEnTiempoReal(String textoBusqueda, int limiteResultados) {
        EntityManager em = getEntityManager();
        try {
            if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
                return obtenerUltimosClientes(limiteResultados);
            }
            
            String jpql = "SELECT c FROM Cliente c WHERE " +
                         "(LOWER(c.cliCedula) LIKE LOWER(:texto) OR " +
                         "LOWER(c.cliNombres) LIKE LOWER(:texto) OR " +
                         "LOWER(c.cliApellidos) LIKE LOWER(:texto) OR " +
                         "LOWER(CONCAT(c.cliNombres, ' ', c.cliApellidos)) LIKE LOWER(:texto)) " +
                         "AND c.cliEstado = 'A' " +
                         "ORDER BY " +
                         "CASE WHEN c.cliCedula = :textoExacto THEN 0 " +
                         "WHEN c.cliCedula LIKE :textoInicio THEN 1 " +
                         "WHEN CONCAT(c.cliNombres, ' ', c.cliApellidos) LIKE :textoInicio THEN 2 " +
                         "ELSE 3 END, " +
                         "c.cliNombres, c.cliApellidos";
            
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("texto", "%" + textoBusqueda + "%");
            query.setParameter("textoExacto", textoBusqueda);
            query.setParameter("textoInicio", textoBusqueda + "%");
            query.setMaxResults(limiteResultados);
            
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }

    // Búsqueda por cédula en tiempo real
    public List<Cliente> buscarPorCedulaEnTiempoReal(String cedula, int limite) {
        EntityManager em = getEntityManager();
        try {
            if (cedula == null || cedula.trim().isEmpty()) {
                return obtenerUltimosClientes(limite);
            }
            
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.cliCedula LIKE :cedula AND c.cliEstado = 'A' " +
                "ORDER BY CASE WHEN c.cliCedula = :cedulaExacta THEN 0 ELSE 1 END, c.cliCedula", 
                Cliente.class
            );
            query.setParameter("cedula", cedula + "%");
            query.setParameter("cedulaExacta", cedula);
            query.setMaxResults(limite);
            
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }

    // Búsqueda por nombre en tiempo real
    public List<Cliente> buscarPorNombreEnTiempoReal(String nombre, int limite) {
        EntityManager em = getEntityManager();
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return obtenerUltimosClientes(limite);
            }
            
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE " +
                "(LOWER(c.cliNombres) LIKE LOWER(:nombre) OR " +
                "LOWER(c.cliApellidos) LIKE LOWER(:nombre) OR " +
                "LOWER(CONCAT(c.cliNombres, ' ', c.cliApellidos)) LIKE LOWER(:nombre)) " +
                "AND c.cliEstado = 'A' " +
                "ORDER BY " +
                "CASE WHEN CONCAT(c.cliNombres, ' ', c.cliApellidos) LIKE :nombreInicio THEN 0 ELSE 1 END, " +
                "c.cliNombres, c.cliApellidos", 
                Cliente.class
            );
            query.setParameter("nombre", "%" + nombre + "%");
            query.setParameter("nombreInicio", nombre + "%");
            query.setMaxResults(limite);
            
            return query.getResultList();
            
        } finally {
            em.close();
        }
    }

    // Búsqueda inteligente que detecta automáticamente si es cédula o nombre
    public List<Cliente> busquedaInteligente(String texto, int limite) {
        if (texto == null || texto.trim().isEmpty()) {
            return obtenerUltimosClientes(limite);
        }
        
        // Detectar si es probablemente una cédula (solo números)
        if (texto.matches("\\d+")) {
            return buscarPorCedulaEnTiempoReal(texto, limite);
        } else {
            return buscarPorNombreEnTiempoReal(texto, limite);
        }
    }

    // Obtener últimos clientes (para cuando no hay búsqueda)
    private List<Cliente> obtenerUltimosClientes(int limite) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.cliEstado = 'A' " +
                "ORDER BY c.cliId DESC", 
                Cliente.class
            );
            query.setMaxResults(limite);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Búsqueda individual por cédula exacta
    public Cliente buscarClientePorCedulaExacta(String cedula) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT c FROM Cliente c WHERE c.cliCedula = :cedula");
            q.setParameter("cedula", cedula);
            List<Cliente> resultados = q.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    // Búsqueda por nombre (tanto nombres como apellidos)
    public List<Cliente> buscarClientePorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery(
                "SELECT c FROM Cliente c WHERE LOWER(c.cliNombres) LIKE LOWER(:nombre) OR LOWER(c.cliApellidos) LIKE LOWER(:nombre) ORDER BY c.cliNombres"
            );
            q.setParameter("nombre", "%" + nombre + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // Verificar si una cédula existe rápidamente
    public boolean existeCedula(String cedula) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.cliCedula = :cedula");
            query.setParameter("cedula", cedula);
            Long count = (Long) query.getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // FUNCIÓN PARA BUSCAR CLIENTE POR CÉDULA Y NOMBRE (usando Criteria API)
    public List<Cliente> buscarClientePorCedulaNombre(String cedula, String nombre) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
            Root<Cliente> cliente = cq.from(Cliente.class);
            
            // Crear predicados dinámicos
            Predicate predicate = cb.conjunction();
            
            if (cedula != null && !cedula.trim().isEmpty()) {
                predicate = cb.and(predicate, 
                    cb.like(cb.lower(cliente.get("cliCedula")), "%" + cedula.toLowerCase() + "%"));
            }
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                Predicate nombrePredicate = cb.or(
                    cb.like(cb.lower(cliente.get("cliNombres")), "%" + nombre.toLowerCase() + "%"),
                    cb.like(cb.lower(cliente.get("cliApellidos")), "%" + nombre.toLowerCase() + "%")
                );
                predicate = cb.and(predicate, nombrePredicate);
            }
            
            cq.where(predicate);
            cq.orderBy(cb.asc(cliente.get("cliNombres")));
            
            Query q = em.createQuery(cq);
            return q.getResultList();
            
        } finally {
            em.close();
        }
    }
}