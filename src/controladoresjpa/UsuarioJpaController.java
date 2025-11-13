package controladoresjpa;

import controladoresjpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelos.Usuario;

public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuario = em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuario.getUsrId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("El usuario con id " + id + " no existe.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsrId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El usuario con id " + id + " no existe.", enfe);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
        /**
     * Busca usuario por nombre y contraseña (para login)
     */
    public Usuario login(String username, String password) {
        EntityManager em = getEntityManager();
        try {
            System.out.println("🔐 Intentando login - Usuario: " + username);

            // Usar JPQL con los nombres correctos de tu entidad
            Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.usrUsuario = :username");
            query.setParameter("username", username);

            List<Usuario> usuarios = query.getResultList();

            System.out.println("📊 Usuarios encontrados: " + usuarios.size());

            if (usuarios.isEmpty()) {
                System.out.println("❌ Usuario no encontrado: " + username);
                return null;
            }

            Usuario usuario = usuarios.get(0);
            System.out.println("✅ Usuario encontrado: " + usuario.getUsrNombres());
            System.out.println("🔐 Contraseña en BD: '" + usuario.getUsrClave() + "'");
            System.out.println("🔐 Contraseña ingresada: '" + password + "'");

            // Verificar contraseña
            if (password != null && password.equals(usuario.getUsrClave())) {
                System.out.println("🎉 CONTRASEÑA CORRECTA - Login exitoso!");

                // IMPORTANTE: Forzar la carga del perfil (puede estar en modo Lazy)
                if (usuario.getPerId() != null) {
                    // Esto fuerza a Hibernate a cargar los datos del perfil
                    usuario.getPerId().getPerDescripcion();
                    System.out.println("👤 Rol: " + usuario.getPerId().getPerDescripcion());
                } else {
                    System.out.println("⚠️ Usuario sin perfil asignado");
                }

                return usuario;
            } else {
                System.out.println("❌ CONTRASEÑA INCORRECTA");
                return null;
            }

        } catch (Exception e) {
            System.out.println("💥 Error en login: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}
