/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladores.FXMLClientesFormularioController;
import controladoresjpa.ClienteJpaController;
import controladoresjpa.ProductoJpaController;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BigIntegerStringConverter;
import javax.persistence.EntityManagerFactory;
import modelos.Cliente;
import modelos.Detallefactura;
import modelos.Producto;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLFacturaController implements Initializable {

    @FXML
    private AnchorPane ap_factura;
    @FXML
    private TableView<Detallefactura> tbl_detallefactura;
    @FXML
    private TableColumn<?, ?> col_ID;
    @FXML
    private TableColumn<Detallefactura, String> col_codigo;
    @FXML
    private TableColumn<Detallefactura, Void> col_buscar;
    @FXML
    private TableColumn<?, ?> col_descripcion;
    @FXML
    private TableColumn<Detallefactura, BigInteger> col_cantidad;    
    @FXML
    private TableColumn<Detallefactura, BigDecimal> col_pvp;
    @FXML
    private TableColumn<?, ?> col_aplicaiva;
    @FXML
    private TableColumn<Detallefactura, BigDecimal> col_iva;
    @FXML
    private TableColumn<Detallefactura, BigDecimal> col_total;
    @FXML
    private TextField txt_factura;
    @FXML
    private TextField txt_fecha;
    @FXML
    private TextField txt_documento;
    @FXML
    private TextField txt_nombres;
    @FXML
    private TextField txt_apellidos;
    @FXML
    private TextField txt_telefono;
    @FXML
    private TextField txt_correo;
    @FXML
    private TextField txt_direccion;
    @FXML
    private TextField txt_subtotal;
    @FXML
    private TextField txt_subtotal0;
    @FXML
    private TextField txt_iva;
    @FXML
    private TextField txt_descuento;
    @FXML
    private TextField txt_total;
    @FXML
    private Button btn_grabar;
    @FXML
    private Button btn_anular;
    @FXML
    private Button btn_imprimir;
    @FXML
    private Button btn_cancelar;

    private ClienteJpaController gestorCliente;
    
    private ProductoJpaController gestorProducto;
    
    private final DecimalFormat formatoDosDecimales = new DecimalFormat("0.00");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        obtenerFecha();
        mod_general.Mod_General.configurarNavegacion(this.txt_documento, txt_nombres,
                txt_apellidos, txt_telefono, 
                txt_correo, txt_direccion, btn_grabar, btn_anular, btn_imprimir, btn_cancelar );
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        this.gestorCliente = new ClienteJpaController(emf);
        this.gestorProducto = new ProductoJpaController(emf);
        configurarTablaDetalleFactura();
        addFila();
    }    
    
    private void addFila(){
        Detallefactura det = new Detallefactura();
        
        tbl_detallefactura.getItems().add(det);
        
    }
    
    private void configurarTablaDetalleFactura() {
        tbl_detallefactura.setEditable(true);
        //Configuracion de la tabla y sus columnas como producto nombre cantidad total
        col_ID.setCellValueFactory(new PropertyValueFactory<>("prodId"));
        col_codigo.setCellValueFactory(data -> {
            Producto producto = data.getValue().getProdId();
            String codigo = (producto != null) ? producto.getProdCod(): "";
            return new SimpleStringProperty(codigo);
        });
        col_codigo.setCellFactory(TextFieldTableCell.forTableColumn());
        
        col_buscar.setEditable(true);
        col_cantidad.setEditable(true);
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_cantidad.setCellFactory(TextFieldTableCell.forTableColumn(new BigIntegerStringConverter()));        //col_cantidad.setCellFactory(crearFabricaDeCeldasFormateada(formatoDosDecimales));
        
        col_descripcion.setCellValueFactory(new PropertyValueFactory<>("prodNombre"));
        
        col_aplicaiva.setCellValueFactory(new PropertyValueFactory<>("prodAplicaiva"));

        col_pvp.setCellValueFactory(new PropertyValueFactory<>("prodPvp"));
        
//        col_pvp.setCellFactory(crearFabricaDeCeldasFormateada(formatoDosDecimales));
        // --- COLUMNA PVP (PRECIO) COMO COMBOBOX EDITABLE ---
        col_pvp.setCellValueFactory(new PropertyValueFactory<>("prodPvp"));

        // 1. Usamos un ComboBoxTableCell
        col_pvp.setCellFactory(param -> new ComboBoxTableCell<Detallefactura, BigDecimal>() {
            
            // 2. Este método se llama para "dibujar" la celda
            @Override
            public void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // 3. Obtener el producto de la fila actual
                    Detallefactura detalle = getTableView().getItems().get(getIndex());
                    Producto producto = detalle.getProdId();

                    if (producto != null) {
                        // 4. Crear la lista de precios SOLO para ese producto
                        ObservableList<BigDecimal> listaDePrecios = FXCollections.observableArrayList(
                            producto.getProdPvpxmenor(),
                            producto.getProdPvpxmayor()
                            // Aquí puedes añadir más precios si los tienes (ej. p.getProdPvpEspecial())
                        );
                        
                        // 5. Asignar la lista al ComboBox de esta celda
                        this.getItems().setAll(listaDePrecios);
                        
                        // 6. (Opcional) Formatear cómo se ve el precio
                        this.setConverter(new StringConverter<BigDecimal>() {
                            @Override
                            public String toString(BigDecimal object) {
                                return (object == null) ? "0.00" : formatoDosDecimales.format(object);
                            }
                            @Override
                            public BigDecimal fromString(String string) {
                                try {
                                    return new BigDecimal(string);
                                } catch (Exception e) {
                                    return BigDecimal.ZERO;
                                }
                            }
                        });
                    } else {
                        // No hay producto, no mostrar nada
                        this.getItems().clear();
                    }
                    
                    // 7. Mostrar el texto formateado
                    setText((item == null) ? "" : formatoDosDecimales.format(item));
                }
            }
        });

        // 8. ¡Definir qué pasa cuando el usuario SELECCIONA un nuevo precio!
        col_pvp.setOnEditCommit(event -> {
            Detallefactura det = event.getRowValue();
            BigDecimal nuevoPrecio = event.getNewValue();

            if (nuevoPrecio != null && det.getCantidad() != null) {
                det.setProdPvp(nuevoPrecio); // Asignar el nuevo precio
                
                // Recalcular IVA y Total con el nuevo precio
                det.setIva(det.calcularIva(new BigDecimal("0.15"), nuevoPrecio));
                det.setTotal(det.sumartotal(det.getCantidad(), nuevoPrecio));

                tbl_detallefactura.refresh();
                
                // (Recuerda recalcular tus totales generales aquí)
                // sumarTotalesGenerales();
            }
        });
        
        col_iva.setCellValueFactory(new PropertyValueFactory<>("iva"));
        col_iva.setCellFactory(crearFabricaDeCeldasFormateada(formatoDosDecimales));
        
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        col_total.setCellFactory(crearFabricaDeCeldasFormateada(formatoDosDecimales));
        
        col_codigo.setOnEditCommit(evento -> {
            // Obtiene la fila (Detallefactura) que se está editando
            Detallefactura det = evento.getRowValue();
            String nuevoCodigo = evento.getNewValue();

            if (nuevoCodigo == null || nuevoCodigo.trim().isEmpty()) {
                det.setProdId(null); // Limpiar el producto si el código está vacío
                tbl_detallefactura.refresh();
                return;
            }

            // 1. Buscar el producto en la base de datos
            Producto objProd = gestorProducto.buscarPorCodigo(nuevoCodigo);

            if (objProd != null) {
                // 2. ¡Esta es la línea clave! Asignas el objeto Producto completo al detalle
                det.setProdId(objProd);
                det.setProdNombre(objProd.getProdNombre());
                // 3. (Recomendado) Asignar una cantidad por defecto si es una fila nueva
                if (det.getCantidad() == null) {
                    det.setCantidad(new BigInteger("1")); // O 1, dependiendo del tipo de dato
                }
                det.setProdPvp(objProd.getProdPvpxmenor());
                det.setIva(det.calcularIva(new BigDecimal("0.15"), det.getProdId().getProdPvpxmenor()));
                // 4. (Recomendado) Reactiva tu lógica para actualizar totales
                det.setTotal(det.sumartotal(det.getCantidad(), det.getProdId().getProdPvpxmenor()));
                // det.actualizarTotales(); // Necesitarás un método en tu entidad Detallefactura
                // sumarTotales(); // Un método en este controlador para sumar todos los detalles
                
                tbl_detallefactura.refresh(); // Refresca la tabla para mostrar los nuevos datos

                // 5. (Recomendado) Reactiva tu lógica para añadir una nueva fila
                //agregarFilasiEsUltima(evento.getTablePosition().getRow());
                addFila();
            } else {
                // Si el producto no existe, limpiamos la referencia
                det.setProdId(null);
                det.setCantidad(null);
                tbl_detallefactura.refresh();

                // Muestra tu mensaje de error
                // General.Mod_general.fun_mensajeInformacion("No existe el producto", this.getClass());

                // (Opcional) Limpiar el código que escribió el usuario
                // det.getProdId().setProdCod(""); // Esto no funcionará, necesitas un campo temporal
            }
        });
        
        col_buscar.setCellFactory(col -> {
              // 3. Crea la celda que contendrá el botón
            TableCell<Detallefactura, Void> cell = new TableCell<>() {
                // 4. Crea el botón (una sola vez por celda)
                private final Button btn = new Button("Buscar");
                {
                    // 5. Dale una acción al botón
                    btn.setOnAction(event -> {

                        // ¡CLAVE! Obtener el índice de la fila actual
                        int filaActual = getIndex(); 

                        // Si necesitas los datos (aunque ya no es necesario aquí)
                        // Detallefactura detalle = getTableView().getItems().get(filaActual);

                        // Llamar al método con el índice
                        abrirModalBuscar(filaActual); 
                    });
                }

                // 6. Este método decide si mostrar el botón o no
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null); // Fila vacía = no hay botón
                    } else {
                        setGraphic(btn);  // Fila con datos = sí hay botón
                    }
                }
            };
            return cell;
        });
        
        col_cantidad.setOnEditCommit(event -> {
            Detallefactura det = event.getRowValue();
            BigInteger nuevaCantidad = event.getNewValue();

            // Validar que la cantidad sea válida (ej. mayor a cero)
            if (nuevaCantidad != null && nuevaCantidad.compareTo(BigInteger.ZERO) > 0) {
                det.setCantidad(nuevaCantidad);

                // Recalcular el total de la fila (SOLO si ya hay un producto)
                if (det.getProdId() != null) {
                    // Usamos los mismos métodos que usaste en col_codigo
                    det.setProdPvp(det.getProdId().getProdPvpxmenor());
                    det.setIva(det.calcularIva(new BigDecimal("0.15"), det.getProdId().getProdPvpxmenor()));
                    det.setTotal(det.sumartotal(nuevaCantidad, det.getProdId().getProdPvpxmenor()));

                    tbl_detallefactura.refresh(); // Refrescar la fila

                    // Aquí deberías llamar a un método que sume los totales
                    // de TODAS las filas y actualice los TextField de abajo (txt_total, txt_iva, etc.)
                    // sumarTotalesGenerales(); 
                }
            } else {
                // Si no es válido (ej. 0 o texto), revierte al valor anterior
                det.setCantidad(event.getOldValue());
                tbl_detallefactura.refresh();
                // (Aquí podrías mostrar un mensaje de error)
            }
        });
        
        
    }
    
    
    /**
    * Crea un CellFactory genérico para formatear números en una TableColumn.
    * @param <S> El tipo del objeto de la fila (Ej: Detallefactura)
    * @param <T> El tipo del valor de la celda (Debe ser un subtipo de Number, como BigDecimal o BigInteger)
    * @param format El DecimalFormat que se usará para el texto.
    * @return Un Callback listo para usarse en setCellFactory.
    */
    private <S, T extends Number> Callback<TableColumn<S, T>, TableCell<S, T>> crearFabricaDeCeldasFormateada(DecimalFormat format) {
        return column -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Aquí está la lógica de formato
                    setText(format.format(item));
                }
            }
        };
    }
    
    public void actualizarDetalle(Detallefactura detalle, Producto producto){
        
    }
    /**
    * Este método PÚBLICO es llamado desde FXMLBuscarProductosController
    * para devolver el producto seleccionado.
    * @param fila El índice de la fila que se debe actualizar
    * @param objProd El Producto seleccionado en el modal
    */
    public void actualizarProductoEnFila(int fila, Producto objProd) {
        if (objProd == null) return;

        // 1. Obtener el detalle de la fila específica
        Detallefactura det = tbl_detallefactura.getItems().get(fila);

        // 2. Asignar el producto y calcular todo (¡Igual que en col_codigo!)
        det.setProdId(objProd);
        det.setProdNombre(objProd.getProdNombre());

        // 3. Asignar cantidad 1 por defecto si no tiene
        if (det.getCantidad() == null) {
            det.setCantidad(new BigInteger("1"));
        }

        det.setProdPvp(objProd.getProdPvpxmenor());
        det.setIva(det.calcularIva(new BigDecimal("0.15"), objProd.getProdPvpxmenor()));
        det.setTotal(det.sumartotal(det.getCantidad(), objProd.getProdPvpxmenor()));

        // 4. Refrescar la tabla
        tbl_detallefactura.refresh();

        // 5. Si la fila actualizada era la última, añade una nueva fila vacía
        if (fila == tbl_detallefactura.getItems().size() - 1) {
            addFila();
        }

        // 6. (Recomendado) Mover el foco a la columna cantidad de esa fila
        //    para que el usuario pueda editarla rápidamente.
        tbl_detallefactura.requestFocus();
        tbl_detallefactura.getSelectionModel().select(fila);
        tbl_detallefactura.edit(fila, col_cantidad); 
    }
    
    public void obtenerFecha(){
        // Fecha actual
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaActual = LocalDate.now();
        txt_fecha.setText(fechaActual.format(formato));
        txt_fecha.setEditable(false); 
    }
    
    @FXML
    private void acc_grabar(ActionEvent event) {
        
    }

    @FXML
    private void acc_anular(ActionEvent event) {
        
    }

    @FXML
    private void acc_imprimir(ActionEvent event) {
        
    }

    @FXML
    private void acc_cancelar(ActionEvent event) {
        
    }

    @FXML
    private void buscarCliente(KeyEvent event) {
        autocompletarCliente(event);
    }
    
    private void autocompletarCliente(KeyEvent event) {
        String cedula = txt_documento.getText().trim();

        if (cedula.length() >= 9) {
            new Thread(() -> {
                try {
                    // Pequeña pausa para no buscar demasiado rápido
                    Thread.sleep(300);

                    // Buscar cliente
                    List<Cliente> resultados = gestorCliente.buscarPorCedulaEnTiempoReal(cedula, 5);

                    Platform.runLater(() -> {
                        if (!resultados.isEmpty()) {
                            // Tomar el primer resultado y autocompletar
                            Cliente cliente = resultados.get(0);
                            txt_nombres.setText(cliente.getCliNombres());
                            txt_apellidos.setText(cliente.getCliApellidos());
                            txt_telefono.setText(cliente.getCliTelefono() != null ? cliente.getCliTelefono() : "");
                            txt_correo.setText(cliente.getCliCorreo() != null ? cliente.getCliCorreo() : "");
                            txt_direccion.setText(cliente.getCliDireccion() != null ? cliente.getCliDireccion() : "");
                            //txt_documento.requestFocus();
                        } else {
                            // Limpiar campos si no encuentra
                            limpiarCamposCliente();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else if (cedula.isEmpty()) {
            limpiarCamposCliente();
        }
    }

    private void limpiarCamposCliente() {
        txt_nombres.clear();
        txt_apellidos.clear();
        txt_telefono.clear();
        txt_correo.clear();
        txt_direccion.clear();
    } 
    
    private void abrirModalBuscar(int fila) { // <-- AHORA ACEPTA LA FILA
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLBuscarProductos.fxml"));
            Parent root = loader.load();

            // 1. Obtener el controlador del modal
            FXMLBuscarProductosController controlador = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Buscar Productos");

            // 2. ¡CLAVE! Pasar los datos al modal antes de mostrarlo
            controlador.setFacturaController(this); // Le pasas ESTE controlador
            controlador.setFilaDestino(fila);       // Le pasas el NÚMERO de fila
            controlador.setStage(stage);            // Le pasas el stage para que se pueda cerrar

            stage.showAndWait();

            // 3. (Opcional) Al cerrar el modal, refrescar la tabla
            tbl_detallefactura.refresh();

        } catch (Exception e){
            System.out.println("Error al abrir modal: " + e.getMessage());
            e.printStackTrace();
            // mod_general.mod.showError("No se pudo abrir el formulario de cliente");
        }
    }
    
}
