/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.ProductoJpaController;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import modelos.Cliente;
import modelos.Perfil;
import modelos.Producto;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLProductosController implements Initializable {

    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colCompra;
    @FXML
    private TableColumn<?, ?> colPvpMenor;
    @FXML
    private TableColumn<?, ?> colPvpMayor;
    @FXML
    private TableColumn<?, ?> colStock;
    @FXML
    private TableColumn<?, ?> colIva;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<Producto> tblProductos;
    private ProductoJpaController jpaProducto;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        jpaProducto = new ProductoJpaController(emf);
        configurarTabla();
        cargarDatos();
        
    }    

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        System.out.println("Cancelar");
    }

    @FXML
    private void accBtnAgregar(ActionEvent event) {
        System.out.println("Agregar");
        abrirModal();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("prodCod"));
        colCompra.setCellValueFactory(new PropertyValueFactory<>("prodPreciocompra"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("prodEstado"));
        colId.setCellValueFactory(new PropertyValueFactory<>("prodId"));
        colIva.setCellValueFactory(new PropertyValueFactory<>("prodAplicaiva"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("prodNombre"));
        colPvpMayor.setCellValueFactory(new PropertyValueFactory<>("prodPvpxmayor"));
        colPvpMenor.setCellValueFactory(new PropertyValueFactory<>("prodPvpxmenor"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("prodStock"));
    }

    private void cargarDatos() {
        try {
            List<Producto> productos = jpaProducto.listarTodos();
            tblProductos.getItems().setAll(productos);
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void abrirModalProducto(Producto p){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLProductoFormulario.fxml"));
            //loader.setResources(I18N.getBundle());
            Parent root = loader.load();

            FXMLProductoFormularioController controlador = loader.getController();
            controlador.cargarProductoParaEdicion(p);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLProductoFormulario.fxml"));
            //loader.setResources(I18N.getBundle());
            Parent root = loader.load();

            FXMLProductoFormularioController controlador = loader.getController();
            controlador.prepararParaNuevoProducto();

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

    @FXML
    private void onMouseClick(MouseEvent event) {
        try {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                Producto objProducto = tblProductos.getSelectionModel().getSelectedItem();
                if(objProducto != null){
                    abrirModalProducto(objProducto);
                }
            }
        } catch(Exception e){
            System.out.println("Error al seleccionar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
}
