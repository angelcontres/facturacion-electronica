/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.UsuarioJpaController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import modelos.Usuario;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLUsuariosController implements Initializable {

    @FXML
    private TableView<Usuario> tblUsuarios;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TableColumn<Usuario, Integer> colID;
    @FXML
    private TableColumn<Usuario, String> colPerfil;
    @FXML
    private TableColumn<Usuario, String> colNombres;
    @FXML
    private TableColumn<Usuario, String> colUsuarios;
    @FXML
    private TableColumn<Usuario, String> colEstado;

    private UsuarioJpaController jpaUsuarios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnasTabla();
        inicializarJPA();
        cargarUsuarios();
    }

    @FXML
    private void accBtnAgregar(ActionEvent event) {
        System.out.println("Agregar usuario...");
        abrirModalUsuario(null); // Para nuevo usuario
    }

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        System.out.println("Cancelar acción");
    }
    
    /**
     * Abre el modal para nuevo usuario o edición
     */
    private void abrirModalUsuario(Usuario usuario){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLUsuarioFormulario.fxml"));
            Parent root = loader.load();

            FXMLUsuarioFormularioController controlador = loader.getController();
            
            Stage stage = new Stage();
            //controlador.setStage(stage);
            
            if (usuario != null) {
                controlador.cargarUsuarioParaEdicion(usuario);
                stage.setTitle("Editar Usuario");
            } else {
                controlador.prepararParaNuevoUsuario();
                stage.setTitle("Nuevo Usuario");
            }
            
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            
            // ✅ Actualizar tabla después de cerrar modal
            cargarUsuarios();
            
        } catch (Exception e){
            System.out.println("Error al abrir modal: " + e.getMessage());
            e.printStackTrace();
            mod_general.mod.showError("No se pudo abrir el formulario de usuario");
        }
    }

    @FXML
    private void onMouseClick(MouseEvent event) {
        try {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                Usuario usuario = tblUsuarios.getSelectionModel().getSelectedItem();
                if(usuario != null){
                    abrirModalUsuario(usuario);
                }
            }
        } catch(Exception e){
            System.out.println("Error al seleccionar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Configura las columnas de la tabla
     */
    private void configurarColumnasTabla() {
        colID.setCellValueFactory(new PropertyValueFactory<>("usrId"));
        colPerfil.setCellValueFactory(new PropertyValueFactory<>("perId"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("usrNombres"));
        colUsuarios.setCellValueFactory(new PropertyValueFactory<>("usrUsuario"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("usrEstado"));
    }
    
    /**
     * Inicializa la conexión JPA
     */
    private void inicializarJPA() {
        try {
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            this.jpaUsuarios = new UsuarioJpaController(emf);
        } catch (Exception e) {
            mod_general.mod.showError("Error al inicializar conexión con la base de datos");
            e.printStackTrace();
        }
    }
    
    /**
     * Carga los usuarios en la tabla
     */
    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = jpaUsuarios.findUsuarioEntities();
            tblUsuarios.getItems().setAll(usuarios); // ✅ Usar setAll en lugar de addAll
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la ventana actual
     */
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
    
    /**
     * Maneja la búsqueda de usuarios (opcional)
     */
    @FXML
    private void accBuscarUsuario(ActionEvent event) {
        // Implementar búsqueda si es necesario
        String textoBusqueda = txtBuscar.getText().trim();
        if (!textoBusqueda.isEmpty()) {
            // Lógica de búsqueda aquí
        }
    }
}
