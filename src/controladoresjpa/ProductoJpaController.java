package controladoresjpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import modelos.Producto;

public class ProductoJpaController {

    private EntityManagerFactory emf = null;
    
    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ---------------------- CRUD ----------------------

    public void crear(Producto producto) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al crear producto: " + e.getMessage());
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void editar(Producto producto) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al editar producto: " + e.getMessage());
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void eliminar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null) {
                em.remove(producto);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public Producto buscarPorId(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Producto> listarTodos() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Producto.findAll", Producto.class).getResultList();
        } finally {
            em.close();
        }
    }

    // ---------------------- CONSULTAS PERSONALIZADAS ----------------------

    public List<Producto> buscarPorNombre(String nombre) {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Producto.findByProdNombre", Producto.class)
                     .setParameter("prodNombre", "%" + nombre + "%")
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public Producto buscarPorCodigo(String codigo) {
        EntityManager em = getEntityManager();
        try {
            List<Producto> lista = em.createNamedQuery("Producto.findByProdCod", Producto.class)
                                     .setParameter("prodCod", codigo)
                                     .getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    public List<Producto> listarActivos() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Producto.findByProdEstado", Producto.class)
                     .setParameter("prodEstado", "A") // Asumiendo que 'A' es activo
                     .getResultList();
        } finally {
            em.close();
        }
    }

    // ---------------------- MÉTODOS PARA FACTURACIÓN ----------------------

    /**
     * Busca productos por código o nombre (para búsqueda en tiempo real)
     */
    public List<Producto> buscarProductoPorCodigoONombre(String busqueda, int limite) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Producto p WHERE " +
                         "(LOWER(p.prodCod) LIKE LOWER(:busqueda) OR " +
                         "LOWER(p.prodNombre) LIKE LOWER(:busqueda)) " +
                         "AND p.prodEstado = 'A' " + // Solo productos activos
                         "ORDER BY p.prodNombre";
            
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("busqueda", "%" + busqueda + "%");
            query.setMaxResults(limite);
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca productos con stock disponible
     */
    public List<Producto> buscarConStock(int limite) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Producto p WHERE " +
                         "p.prodStock > 0 AND p.prodEstado = 'A' " +
                         "ORDER BY p.prodNombre";
            
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setMaxResults(limite);
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza el stock después de una venta
     */
    public void actualizarStock(Integer productoId, Integer cantidadVendida) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            
            Producto producto = em.find(Producto.class, productoId);
            if (producto != null && producto.getProdStock() != null) {
                Integer nuevoStock = producto.getProdStock().intValue() - cantidadVendida;
                if (nuevoStock < 0) nuevoStock = 0;
                producto.setProdStock(java.math.BigDecimal.valueOf(nuevoStock));
                em.merge(producto);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Busca productos más vendidos
     */
    public List<Object[]> obtenerProductosMasVendidos(int limite) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT df.prodId, p.prodNombre, SUM(df.cantidad) as totalVendido " +
                         "FROM Detallefactura df " +
                         "JOIN df.prodId p " +
                         "GROUP BY df.prodId, p.prodNombre " +
                         "ORDER BY totalVendido DESC";
            
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setMaxResults(limite);
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Verifica si existe un producto con el mismo código
     */
    public boolean existeCodigo(String codigo) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Producto p WHERE p.prodCod = :codigo", Long.class)
                          .setParameter("codigo", codigo)
                          .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Busqueda en tiempo real para autocompletado
     */
    public List<Producto> buscarEnTiempoReal(String texto, int limite) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Producto p WHERE " +
                         "(LOWER(p.prodCod) LIKE LOWER(:texto) OR " +
                         "LOWER(p.prodNombre) LIKE LOWER(:texto)) " +
                         "AND p.prodEstado = 'A' " +
                         "ORDER BY " +
                         "CASE WHEN p.prodCod = :textoExacto THEN 0 " +
                         "WHEN p.prodCod LIKE :textoInicio THEN 1 " +
                         "WHEN p.prodNombre LIKE :textoInicio THEN 2 " +
                         "ELSE 3 END, " +
                         "p.prodNombre";
            
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("texto", "%" + texto + "%");
            query.setParameter("textoExacto", texto);
            query.setParameter("textoInicio", texto + "%");
            query.setMaxResults(limite);
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ---------------------- MÉTODOS DE APOYO ----------------------

    public void cerrar() {
        if (emf != null) {
            emf.close();
        }
    }
}