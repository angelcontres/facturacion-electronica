/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.EmpresaJpaController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
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
import javax.persistence.Persistence;
import modelos.Cliente;
import modelos.Empresa;
import org.eclipse.persistence.internal.helper.JPAClassLoaderHolder;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLEmpresasController implements Initializable {

    @FXML
    private TextField txtBuscarEmpresa;
    @FXML
    private TableView<Empresa> tblEmpresas;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colDireccion;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> Estado;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAgregar;
    
    private ObservableList listEmpresa = FXCollections.observableArrayList();
    private EmpresaJpaController jpaEmpresa;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        this.jpaEmpresa = new EmpresaJpaController(emf);
        configurarTabla();
        cargarDatos();
        
    }    

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        
    }

    @FXML
    private void accBtnAgregar(ActionEvent event) {
        abrirModal();
    }
    
    
    
    
    private void abrirModal(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLEmpresFormulario.fxml"));
            //loader.setResources(I18N.getBundle());
            Parent root = loader.load();

            FXMLEmpresFormularioController controlador = loader.getController();
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
            List<Empresa> clientes = jpaEmpresa.findEmpresaEntities();
            tblEmpresas.getItems().setAll(clientes);
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("empRuc"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("empNombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("empDireccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("empTelefono"));
        Estado.setCellValueFactory(new PropertyValueFactory<>("empEstado"));    
    }

    private void abrirModalEmpresa(Empresa em){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLEmpresFormulario.fxml"));
            //loader.setResources(I18N.getBundle());
            Parent root = loader.load();

            FXMLEmpresFormularioController controlador = loader.getController();
            controlador.fun_recuperarClientexId(em);

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

    @FXML
    private void accMouseClick(MouseEvent event) {
        try {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                Empresa empresa = tblEmpresas.getSelectionModel().getSelectedItem();
                if(empresa != null){
                    abrirModalEmpresa(empresa);
                }
            }
        } catch(Exception e){
            System.out.println("Error al seleccionar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
}
