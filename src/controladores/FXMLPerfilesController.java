/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.EmpresaJpaController;
import controladoresjpa.PerfilJpaController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelos.Cliente;
import modelos.Empresa;
import modelos.Perfil;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLPerfilesController implements Initializable {

    @FXML
    private TextField txtBuscarPerfil;
    @FXML
    private Label lblPerfil;
    @FXML
    private TableView<Perfil> tblPerfiles;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;
    
    private ObservableList<Perfil> listPerfil = FXCollections.observableArrayList();
    private PerfilJpaController jpaPerfil;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        this.jpaPerfil = new PerfilJpaController(emf);
        
        colId.setCellValueFactory(new PropertyValueFactory<>("perId"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("perDescripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("perEstado"));
        
        // TODO
        cargarDatos();
    }    

    @FXML
    private void accBtnAgregar(ActionEvent event) {
        System.out.println("Agregar");
        abrirModal();
    }

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        System.out.println("Cancelar");
    }
    
    private void abrirModalPerfil(Perfil p){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLPerfilFormulario.fxml"));
            //loader.setResources(I18N.getBundle());
            Parent root = loader.load();

            FXMLPerfilFormularioController controlador = loader.getController();
            controlador.fun_recuperarClientexId(p);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            //ThemeManager.applyCurrentTheme(stage.getScene());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e){
            System.out.println("Error al abrir modal: " + e.getMessage());
        }
    }
    
    private void abrirModal(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLPerfilFormulario.fxml"));
            //loader.setResources(I18N.getBundle());
            Parent root = loader.load();

            FXMLPerfilFormularioController controlador = loader.getController();
            controlador.fun_nuevoCliente();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            //ThemeManager.applyCurrentTheme(stage.getScene());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            cargarDatos();
        } catch(Exception e){
            System.out.println("Error al crear cliente: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            List<Perfil> perfiles = jpaPerfil.findPerfilEntities();
            tblPerfiles.getItems().setAll(perfiles);
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickMouse(MouseEvent event) {
        try {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                Perfil p = tblPerfiles.getSelectionModel().getSelectedItem();
                if(p != null){
                    abrirModalPerfil(p);
                }
            }
        } catch(Exception e){
            System.out.println("Error al seleccionar Perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
