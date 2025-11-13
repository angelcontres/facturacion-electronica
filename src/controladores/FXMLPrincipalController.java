package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FXMLPrincipalController implements Initializable {

    @FXML
    private Button btnAdministracion;
    @FXML
    private Button btnProceso;
    @FXML
    private Button btnReportes;

    @FXML
    private VBox subMenuAdmin;
    @FXML
    private VBox subMenuProceso;
    @FXML
    private VBox subMenuReportes;

    @FXML
    private Label lblNombreUsuario;
    @FXML
    private Label lblRolUsuario;
    @FXML
    private Label lblFecha;

    @FXML
    private Button btnGestionClientes;
    @FXML
    private Button btnGestionUsuarios;
    @FXML
    private VBox dataPane;
    @FXML
    private Button btnGestionPerfiles;
    @FXML
    private Button btnGestionEmpresas;
    @FXML
    private Button btnGestionProductos;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set user info
        lblNombreUsuario.setText(mod_general.mod.userActual.getUsrUsuario());
        lblRolUsuario.setText(mod_general.mod.userActual.getPerId().getPerDescripcion()); // ejemplo estático, puedes mapear el perfil
    }

    // Toggle helper para cerrar otros submenus y abrir solo el seleccionado
    private void toggleSubMenu(VBox subMenu) {
        boolean isCurrentlyVisible = subMenu.isVisible(); // guardar estado actual

        // Cerrar todos los submenús
        subMenuAdmin.setVisible(false);
        subMenuAdmin.setManaged(false);
        subMenuProceso.setVisible(false);
        subMenuProceso.setManaged(false);
        subMenuReportes.setVisible(false);
        subMenuReportes.setManaged(false);

        // Si el que se clicó estaba cerrado, abrirlo
        if (!isCurrentlyVisible) {
            subMenu.setVisible(true);
            subMenu.setManaged(true);
        }
    }


    @FXML
    private void abrirSubMenuAdministracion(ActionEvent event) {
        toggleSubMenu(subMenuAdmin);
    }

    @FXML
    private void abrirSubMenuProceso(ActionEvent event) {
        toggleSubMenu(subMenuProceso);
    }

    @FXML
    private void abrirSubMenuReportes(ActionEvent event) {
        toggleSubMenu(subMenuReportes);
    }

    @FXML
    private void accBtnGestionClientes(ActionEvent event) {
        System.out.println("Abrir Gestión de Clientes");
        // Aquí carga la vista correspondiente al centro
        cargarPagina("/vista/FXMLClientes.fxml");
    }

    @FXML
    private void accBtnGestionUsuarios(ActionEvent event) {
        System.out.println("Abrir Gestión de Usuarios");
        // Aquí carga la vista correspondiente al centro
        cargarPagina("/vista/FXMLUsuarios.fxml");
    }
    
    public void cargarPagina(String ruta){
        try {
            FXMLLoader load = new FXMLLoader(getClass().getResource(ruta));
            this.dataPane.getChildren().clear();
            this.dataPane.getChildren().add(load.load());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" " + e.getMessage());
        }
    }

    @FXML
    private void accBtnGestionPerfiles(ActionEvent event) {
        //funcion de accion que escucha, para iniciar la pantalla de Perfiles
        System.out.println("Boton para abrir pantalla de perfiles");
        cargarPagina("/vista/FXMLPerfiles.fxml");
    }

    @FXML
    private void accBtnEmpresas(ActionEvent event) {
        System.out.println("Abriendo Pantalla de Empresas");
        cargarPagina("/vista/FXMLEmpresas.fxml");
    }

    @FXML
    private void accBtnProductos(ActionEvent event) {
        System.out.println("Abriendo Pantalla de Productos");
        cargarPagina("/vista/FXMLProductos.fxml");
    }

    @FXML
    private void accBtnFactura(ActionEvent event) {
        System.out.println("Abriendo pantalla Factura");
        cargarPagina("/vista/FXMLFactura.fxml");
    }
    
}
