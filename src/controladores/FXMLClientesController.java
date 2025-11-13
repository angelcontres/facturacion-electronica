/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.ClienteJpaController;
import java.math.BigDecimal;
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
import javax.persistence.Persistence;
import modelos.Cliente;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLClientesController implements Initializable {

    @FXML
    private TextField txtBuscarClientet;
    @FXML
    private TableView<Cliente> tblClientes;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colNombres;
    @FXML
    private TableColumn<?, ?> colApellidos;
    @FXML
    private TableColumn<?, ?> colCorreo;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;
    private EntityManagerFactory emf;
    
    private ClienteJpaController clienteJpaController;

    private Cliente c;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        this.clienteJpaController = new ClienteJpaController(emf);
        configurarColumnasTabla();
        cargarClientes();        
    }    

    @FXML
    private void accBtnAgregar(ActionEvent event) {
        System.out.println("Abriendo Pantalla para agregar");
        abrirModalCliente(null); // Cambié c por null para nuevo cliente
    }

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        System.out.println("Cancelar");
        cerrarRecursos();
    }
    
    private void abrirModalCliente(Cliente cliente){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLClientesFormulario.fxml"));
            Parent root = loader.load();
            
            FXMLClientesFormularioController controlador = loader.getController();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            
            // Pasar el stage al controlador antes de configurar el modo
            controlador.setStage(stage);
            
            if (cliente != null) {
                controlador.cargarClienteParaEdicion(cliente);
                stage.setTitle("Editar Cliente");
            } else {
                controlador.prepararParaNuevoCliente();
                stage.setTitle("Nuevo Cliente");
            }
            
            stage.showAndWait();
            
            // Actualizar tabla después de cerrar el modal
            actualizarTablaClientes();
            
        } catch (Exception e){
            System.out.println("Error al abrir modal: " + e.getMessage());
            e.printStackTrace();
            mod_general.mod.showError("No se pudo abrir el formulario de cliente");
        }
    }

    @FXML
    private void accClickMouse(MouseEvent event) {
        try {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                Cliente cliente = tblClientes.getSelectionModel().getSelectedItem();
                if(cliente != null){
                    abrirModalCliente(cliente);
                }
            }
        } catch(Exception e){
            System.out.println("Error al seleccionar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void actualizarTablaClientes() {
        try {
            // Si el emf está cerrado o es null, crear uno nuevo
            if (emf == null || !emf.isOpen()) {
                emf = Persistence.createEntityManagerFactory("FacturacionPU");
            }
            
            ClienteJpaController jpaCliente = new ClienteJpaController(emf);
            List<Cliente> clientes = jpaCliente.findClienteEntities();
            tblClientes.getItems().setAll(clientes); // Usar setAll en lugar de clear + addAll
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al actualizar la lista de clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void configurarColumnasTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("cliCedula"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("cliNombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("cliApellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("cliCorreo"));
    }
    
    private void cargarClientes() {
        try {
            List<Cliente> clientes = clienteJpaController.findClienteEntities();
            tblClientes.getItems().setAll(clientes);
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void cerrarRecursos() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}