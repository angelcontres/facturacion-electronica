/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import controladoresjpa.ClienteJpaController;
import controladoresjpa.EmpresaJpaController;
import controladoresjpa.PerfilJpaController;
import controladoresjpa.ProductoJpaController;
import controladoresjpa.UsuarioJpaController;

/**
 *
 * @author usuario
 */
// Factory para crear controladores JPA

public class JpaControllerFactory {
    
    public static ClienteJpaController getClienteJpaController() {
        return new ClienteJpaController(JPAUtil.getEntityManagerFactory());
    }
    
    public static EmpresaJpaController getEmpresaJpaController(){
        return new EmpresaJpaController(JPAUtil.getEntityManagerFactory());
    }
    
    public static PerfilJpaController getPerfilJpaController(){
        return new PerfilJpaController(JPAUtil.getEntityManagerFactory());
    }
    
    public static ProductoJpaController getProductoJpaController(){
        return new ProductoJpaController(JPAUtil.getEntityManagerFactory());
    }
    
    public static UsuarioJpaController getUsuarioJpaController(){
        return new UsuarioJpaController(JPAUtil.getEntityManagerFactory());
    }
    
}