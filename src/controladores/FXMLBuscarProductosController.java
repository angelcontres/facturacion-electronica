/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.ProductoJpaController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage; // <-- Importante
import javax.persistence.EntityManagerFactory;
import modelos.Producto;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLBuscarProductosController implements Initializable {

    @FXML
    private TextField txtBuscarProducto;
    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableColumn<Producto, Void> colSeleccion; // Tipo <Producto, Void> para botón
    @FXML
    private TableColumn<Producto, String> colCodigo; // Tipos de dato correctos
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML
    private Button accBtnSalir;
    @FXML
    private Button btnAgregar; // Este botón ahora es opcional

    // Usa ObservableList directamente
    private ObservableList<Producto> listaObservableProductos = FXCollections.observableArrayList();

    private ProductoJpaController gestorProductos;

    // --- Variables para la comunicación ---
    private FXMLFacturaController facturaController;
    private int filaDestino; // La fila de la factura que vamos a actualizar
    private Stage stage; // Referencia a la ventana (Stage) de este modal

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        gestorProductos = new ProductoJpaController(emf);

        // 1. Configurar la tabla
        configurarTabla();

        // 2. Asignar la lista observable a la tabla
        tblProductos.setItems(listaObservableProductos);

        // 3. Cargar datos iniciales
        cargarDatos();

        // 4. Configurar la búsqueda
        configurarBusqueda();
    }

    private void configurarTabla() {
        // Asignar las propiedades del modelo Producto a las columnas
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("prodCod"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("prodNombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("prodPvpxmenor")); // O el precio que quieras mostrar

        // --- Configurar la columna del botón "Seleccionar" ---
        colSeleccion.setCellFactory(col -> new TableCell<Producto, Void>() {
            private final Button btn = new Button("Ok");

            {
                btn.setOnAction(event -> {
                    // 1. Obtener el producto de esta fila
                    Producto productoSeleccionado = getTableView().getItems().get(getIndex());
                    
                    // 2. Enviar el producto de vuelta al controlador de factura
                    enviarProductoDeVuelta(productoSeleccionado);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private void cargarDatos() {
        try {
            // Carga todos los productos en la lista observable
            listaObservableProductos.setAll(gestorProductos.listarTodos());
        } catch (Exception e) {
            mod_general.mod.showError("Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarBusqueda() {
        txtBuscarProducto.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProducto(newValue);
        });
    }

    private void buscarProducto(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            // Si no hay texto, mostrar todos
            listaObservableProductos.setAll(gestorProductos.listarTodos());
        } else {
            // ¡Usa el método que ya tenías!
            // Le pasamos la búsqueda y un límite (ej. 50 productos)
            listaObservableProductos.setAll(
                gestorProductos.buscarProductoPorCodigoONombre(textoBusqueda, 50)
            );
        }
    }

    private void enviarProductoDeVuelta(Producto producto) {
        if (facturaController != null) {
            // Llama al método público en FXMLFacturaController
            facturaController.actualizarProductoEnFila(filaDestino, producto);
            
            // Cierra este modal
            if (stage != null) {
                stage.close();
            }
        }
    }

    // --- Métodos públicos para recibir datos de la Factura ---

    /**
     * Recibe la referencia al controlador de la factura que abrió este modal.
     */
    public void setFacturaController(FXMLFacturaController controller) {
        this.facturaController = controller;
    }

    /**
     * Recibe el índice de la fila que debe ser actualizada.
     */
    public void setFilaDestino(int fila) {
        this.filaDestino = fila;
    }

    /**
     * Recibe la referencia del Stage (ventana) para poder cerrarla.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // --- Acciones de los botones ---

    @FXML
    private void accBtnSalir(ActionEvent event) {
        // Simplemente cierra el modal
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void accBtnAgregar(ActionEvent event) {
        // Este botón agrega el producto que está SELECCIONADO (resaltado)
        Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            enviarProductoDeVuelta(productoSeleccionado);
        } else {
            mod_general.mod.showConfirmacion("Por favor, seleccione un producto de la lista.");
        }
    }
}