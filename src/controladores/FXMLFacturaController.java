/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladores.FXMLClientesFormularioController;
import controladoresjpa.ClienteJpaController;
import controladoresjpa.DetallefacturaJpaController;
import controladoresjpa.FacturaJpaController;
import controladoresjpa.ProductoJpaController;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BigIntegerStringConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import modelos.Cliente;
import modelos.Detallefactura;
import modelos.Factura;
import modelos.Producto;
import modelos.TicketDatos;
import util.JPAUtil;
import util.ServicioImpresion;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLFacturaController implements Initializable {

    @FXML
    private VBox ap_factura;
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
    
    private DetallefacturaJpaController gestorDet;
    
    private final DecimalFormat formatoDosDecimales = new DecimalFormat("0.00");
    @FXML
    private Button btn_anadirProducto;
    @FXML
    private Button btn_eliminarDetalle;
    
    private final ServicioImpresion servicioImpresion = new ServicioImpresion();
    
    private FacturaJpaController gestorFactura;
    private BigDecimal idFacturaGuardada;
    @FXML
    private CheckBox chboxConsumidorFInal;
    private Cliente clienteConsumidorFinal; // Para guardar el cliente 99999
    
    private BigDecimal totalSubtotalConIVA;
    private BigDecimal totalSubtotalCero;
    private BigDecimal totalIVA;
    private BigDecimal totalDescuento;
    private BigDecimal totalGeneral;
    
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
        this.gestorFactura = new FacturaJpaController(emf); 
        this.gestorDet = new DetallefacturaJpaController(emf);
        btn_imprimir.setDisable(true); 
        
        cargarClienteConsumidorFinal();

        // 2. Añadimos la acción
        chboxConsumidorFInal.setOnAction(event -> {
            if (chboxConsumidorFInal.isSelected()) {
                // Si el usuario lo MARCA
                cargarDatosConsumidorFinal();
            } else {
                // Si el usuario lo DESMARCA
                limpiarCamposCliente();
                habilitarCamposCliente(true);
            }
        });
        
        cargarNumeroFacturaPreview();
        configurarTablaDetalleFactura();
        
        addFila();
        calcularTotalesGenerales();
        
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            
            // Obtiene el texto que *resultaría* después del cambio
            String nuevoTexto = change.getControlNewText();

            // 2. Comprueba las dos condiciones:
            //    - Que el nuevo texto tenga 10 caracteres o menos
            //    - Que el nuevo texto SÓLO contenga dígitos (usando una expresión regular)
            if (nuevoTexto.length() <= 10 && nuevoTexto.matches("\\d*")) {
                // Si cumple, acepta el cambio
                return change;
            }
            
            // 3. Si no cumple (largo > 10 o no es dígito), rechaza el cambio
            return null;
        };
        TextFormatter<String> formatter = new TextFormatter<>(filtro);
        
        txt_documento.setTextFormatter(formatter);
    }    
    
    /**
     * Busca en la BD al cliente "Consumidor Final" (9999...)
     * y lo guarda en memoria para usarlo rápidamente.
     */
    private void cargarClienteConsumidorFinal() {
        // Hacemos esto en un hilo para no bloquear la UI
        new Thread(() -> {
            try {
                // (Usa el JpaController o un EM)
                this.clienteConsumidorFinal = gestorCliente.buscarClientePorCedulaExacta("9999999999");
            } catch (Exception e) {
                this.clienteConsumidorFinal = null;
                // Si no existe, la app debe mostrar un error
                Platform.runLater(() -> {
                    mod_general.mod.showError(
                        "Error Crítico: El cliente '9999999999' (Consumidor Final) no existe en la base de datos.");
                });
            }
        }).start();
    }

    /**
     * Rellena los TextFields con los datos del Consumidor Final
     * y los bloquea.
     */
    private void cargarDatosConsumidorFinal() {
        if (this.clienteConsumidorFinal != null) {
            txt_documento.setText(clienteConsumidorFinal.getCliCedula());
            txt_nombres.setText(clienteConsumidorFinal.getCliNombres());
            txt_apellidos.setText(clienteConsumidorFinal.getCliApellidos());
            txt_telefono.setText(clienteConsumidorFinal.getCliTelefono());
            txt_correo.setText(clienteConsumidorFinal.getCliCorreo());
            txt_direccion.setText(clienteConsumidorFinal.getCliDireccion());
            
            // Bloqueamos los campos
            habilitarCamposCliente(false);
        } else {
            // Si el cliente 999 no se cargó, desmarca la casilla
            mod_general.mod.showError(
                "No se pudo cargar el Consumidor Final. Revise la base de datos.");
            chboxConsumidorFInal.setSelected(false);
        }
    }
    
    /**
     * Habilita o deshabilita todos los campos de texto del cliente.
     * @param habilitar true para habilitar, false para deshabilitar.
     */
    private void habilitarCamposCliente(boolean habilitar) {
        txt_documento.setDisable(!habilitar);
        txt_nombres.setDisable(!habilitar);
        txt_apellidos.setDisable(!habilitar);
        txt_telefono.setDisable(!habilitar);
        txt_correo.setDisable(!habilitar);
        txt_direccion.setDisable(!habilitar);
    }
    
    
    /**
    * Añade una nueva fila vacía a la tabla, 
    * solo si la última fila actual ya tiene un producto.
    */
   private void addFila() {
       ObservableList<Detallefactura> items = tbl_detallefactura.getItems();

       // 1. Revisar si la tabla está vacía
       if (items.isEmpty()) {
           tbl_detallefactura.getItems().add(new Detallefactura());
           return; // Añade la primera fila y termina
       }

       // 2. Si no está vacía, obtener la última fila
       Detallefactura ultimaFila = items.get(items.size() - 1);

       // 3. Revisar si la última fila ya tiene un producto
       //    (Si no tiene producto, significa que YA es la fila "nueva")
       if (ultimaFila.getProdId() != null) {
           // La última fila SÍ tiene un producto, entonces añadimos una nueva vacía
           tbl_detallefactura.getItems().add(new Detallefactura());
       }

       // Si la última fila NO tiene producto (está vacía), no hace nada.
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
                                if (string == null || string.trim().isEmpty()) {
                                    return BigDecimal.ZERO;
                                }
                                try {
                                    // Estandariza la entrada: reemplaza comas por puntos
                                    String textoEstandar = string.replace(',', '.');
                                    return new BigDecimal(textoEstandar);
                                } catch (Exception e) {
                                    return BigDecimal.ZERO;
                                }
                            }
                        });
                    } else {
                        // No hay producto, no mostrar nada
                        this.getItems().clear();
                    }
                    
                    // Mostrar el texto formateado
                    setText((item == null) ? "" : formatoDosDecimales.format(item));
                }
            }
        });

        // Definir qué pasa cuando el usuario SELECCIONA un nuevo precio
        col_pvp.setOnEditCommit(event -> {
            Detallefactura det = event.getRowValue();
            BigDecimal nuevoPrecio = event.getNewValue();

            // Validar que tengamos todo
            if (nuevoPrecio != null && det.getCantidad() != null && det.getProdId() != null) {

                det.setProdPvp(nuevoPrecio); // Asignar el nuevo precio

                // 1. Calcular subtotal de fila (usando el método de tu entidad)
                BigDecimal subtotalFila = det.sumartotal(det.getCantidad(), nuevoPrecio);
                det.setTotal(subtotalFila);

                // 2. Revisar si aplica IVA (leyendo "en vivo" del producto)
                Short aplicaIva = det.getProdId().getProdAplicaiva(); 

                if (aplicaIva != null && aplicaIva == 1) {
                    // 3. Calcular IVA basado en el subtotal de la fila
                    det.setIva(det.calcularIva(new BigDecimal("0.15"), subtotalFila));
                } else {
                    det.setIva(BigDecimal.ZERO);
                }

                tbl_detallefactura.refresh();
                calcularTotalesGenerales();
            }
        });
        
        col_iva.setCellValueFactory(new PropertyValueFactory<>("iva"));
        col_iva.setCellFactory(crearFabricaDeCeldasFormateada(formatoDosDecimales));
        
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        col_total.setCellFactory(crearFabricaDeCeldasFormateada(formatoDosDecimales));
        
        col_codigo.setOnEditCommit(evento -> {
            Detallefactura det = evento.getRowValue();
            String nuevoCodigo = evento.getNewValue();

            // ... (Tu código para limpiar la fila si el código es vacío) ...
            if (nuevoCodigo == null || nuevoCodigo.trim().isEmpty()) {
                det.setProdId(null);
                det.setCantidad(null);
                det.setProdPvp(null);
                det.setIva(null);
                det.setTotal(null);
                // ¡No hay setProdAplicaiva que limpiar!
                tbl_detallefactura.refresh();
                calcularTotalesGenerales(); 
                return;
            }

            Producto objProd = gestorProducto.buscarPorCodigo(nuevoCodigo);

            if (objProd != null) {
                det.setProdId(objProd);
                det.setProdNombre(objProd.getProdNombre());

                if (det.getCantidad() == null) {
                    det.setCantidad(new BigInteger("1"));
                }

                det.setProdPvp(objProd.getProdPvpxmenor());


                // 1. Calcular el subtotal de la fila (Cant * PVP)
                BigDecimal subtotalFila = det.calcularSubtotalFila(det.getCantidad(), det.getProdPvp());
                det.setTotal(subtotalFila); // 'total' es el subtotal de la fila
                
                // 2. Calcular IVA (SOLO si aplica)
                // Se lee directo del producto
                Short aplicaIva = objProd.getProdAplicaiva(); 

                if (aplicaIva != null && aplicaIva == 1) {
                    det.setIva(det.calcularIva(new BigDecimal("0.15"), subtotalFila));
                } else {
                    det.setIva(BigDecimal.ZERO); // No aplica IVA
                }



                tbl_detallefactura.refresh();
                addFila();
                calcularTotalesGenerales();

            } else {
                // ... (tu código para limpiar si no se encuentra el producto) ...
                det.setProdId(null);
                det.setCantidad(null);
                det.setProdPvp(null);
                det.setIva(null);
                det.setTotal(null);

                tbl_detallefactura.refresh();
                calcularTotalesGenerales(); 
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
                        calcularTotalesGenerales();
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

            if (nuevaCantidad != null && nuevaCantidad.compareTo(BigInteger.ZERO) > 0) {
                det.setCantidad(nuevaCantidad);

                if (det.getProdId() != null) {

                    // 1. Calcular subtotal de fila (usando el precio que YA está en la fila)
                    BigDecimal subtotalFila = det.sumartotal(nuevaCantidad, det.getProdPvp());
                    det.setTotal(subtotalFila);

                    // 2. Revisar si aplica IVA
                    Short aplicaIva = det.getProdId().getProdAplicaiva(); 

                    if (aplicaIva != null && aplicaIva == 1) {
                        // 3. Calcular IVA basado en el subtotal
                        det.setIva(det.calcularIva(new BigDecimal("0.15"), subtotalFila));
                    } else {
                        det.setIva(BigDecimal.ZERO);
                    }

                    tbl_detallefactura.refresh(); 
                    calcularTotalesGenerales();
                }
            } else {
                // Revertir si la cantidad es inválida (ej. 0 o texto)
                det.setCantidad(event.getOldValue());
                tbl_detallefactura.refresh();
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

        // --- 1. Validaciones Previas ---
        List<Detallefactura> detallefac = tbl_detallefactura.getItems().stream()
                .filter(d -> d.getProdId() != null) // Filtra solo filas con productos
                .toList();

        if (txt_documento.getText().isEmpty() || detallefac.isEmpty()) {
            mod_general.mod.showError(
                    "Complete los datos del cliente y al menos un producto.");
            return;
        }

        // --- 2. Iniciar EntityManager y Transacción ---
        EntityManager em = null;
        String facNumero = null;

        try {
            em = JPAUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin(); // ⭐️ ¡CLAVE 1: Iniciar la transacción de JPA!

            // --- 3. Obtener/Validar el Cliente (Modo JPA) ---
            Cliente cliente;
            try {
                TypedQuery<Cliente> query = em.createNamedQuery("Cliente.findByCliCedula", Cliente.class);
                query.setParameter("cliCedula", txt_documento.getText());
                cliente = query.getSingleResult(); // Lanza excepción si no lo encuentra
            } catch (NoResultException e) {
                // --- CLIENTE NO ENCONTRADO, PROCEDEMOS A CREARLO ---

                // 2. Validamos que los campos del nuevo cliente estén llenos
                if (txt_nombres.getText().isEmpty() || txt_apellidos.getText().isEmpty()) {
                    // Si no hay nombre/apellido, no podemos crear. Detenemos la transacción.
                    throw new Exception("Cliente no existe. Por favor, complete los campos Nombres y Apellidos para registrarlo.");
                }

                // 3. Creamos el nuevo objeto Cliente
                cliente = new Cliente();
                
                // 4. Llenamos el objeto con los datos de los TextFields
                cliente.setCliCedula(txt_documento.getText().trim());
                cliente.setCliNombres(txt_nombres.getText().trim());
                cliente.setCliApellidos(txt_apellidos.getText().trim());
                cliente.setCliTelefono(txt_telefono.getText().trim());
                cliente.setCliCorreo(txt_correo.getText().trim());
                cliente.setCliDireccion(txt_direccion.getText().trim());
                // (Si tienes un estado por defecto, ej. "A" por Activo, ponlo aquí)
                cliente.setCliEstado("A"); 

                // 5. Persistimos el nuevo cliente DENTRO de la transacción actual
                em.persist(cliente);
            }

            // --- 4. Generar Número de Factura (Modo PROCEDURE de Oracle) ---
        
            // ⭐️ ¡CLAVE 2: Bloquear la fila de correlativos (Sintaxis ORACLE)!
            // (Esto está bien y es necesario para evitar números duplicados)
            try {
                em.createNativeQuery("SELECT SECUENCIA FROM CORRELATIVOS WHERE MOVIMIENTO='FACTURA' AND TERMINAL=1 FOR UPDATE")
                    .getSingleResult(); // Esto bloquea la fila hasta el commit/rollback

            } catch (NoResultException e) {
                 throw new Exception("No se encontró el correlativo de factura. Configure la tabla CORRELATIVOS.");
            }

            // Llamar al PROCEDURE (Sintaxis correcta)!
            try {
                // 1. Crear la consulta al procedimiento por su nombre
                StoredProcedureQuery query = em.createStoredProcedureQuery("sp_generaNumFac");

                // 2. Registrar los parámetros IN (Entrada)
                query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN); // p_movimiento
                query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN); // p_terminal

                // 3. Registrar el parámetro OUT (Salida)
                query.registerStoredProcedureParameter(3, String.class, ParameterMode.OUT); // p_numero_factura

                // 4. Setear los valores de ENTRADA
                query.setParameter(1, "FACTURA");
                query.setParameter(2, 1);

                // 5. Ejecutar el procedimiento
                query.execute();

                // 6. Obtener el valor del parámetro de SALIDA
                facNumero = (String) query.getOutputParameterValue(3);

            } catch (Exception e) {
                // Si esto falla, puede ser por permisos (GRANT EXECUTE)
                throw new Exception("Error al ejecutar el PROCEDIMIENTO sp_generaNumFac. Revise los permisos.", e);
            }

            // Incrementar el correlativo (Modo JPA)!
            // Tu SP solo LEE el secuencial+1, pero no lo actualiza.
            // Por lo tanto, este UPDATE sigue siendo necesario y es correcto.
            em.createNativeQuery("UPDATE CORRELATIVOS SET SECUENCIA = SECUENCIA + 1 WHERE MOVIMIENTO='FACTURA' AND TERMINAL=1")
                .executeUpdate();

            // --- Fin de la sección de bloqueo y generación de número ---

            if (facNumero == null || facNumero.isEmpty()) {
                 throw new Exception("El número de factura generado es nulo o vacío.");
            }

            // ... (El resto del código para buscar cliente, crear Factura y Detalle
            //      sigue exactamente igual que en la respuesta anterior) ...
            if (facNumero == null || facNumero.isEmpty()) {
                 throw new Exception("El número de factura generado es nulo o vacío.");
            }

            // --- 5. Crear la Cabecera de Factura (Modo JPA) ---
            Factura factura = new Factura();

            // (Asumo que tu entidad Factura tiene estos setters)
            factura.setFacNumero(facNumero);
            factura.setFacFecha(new java.util.Date()); // O usa @PrePersist en la entidad
            factura.setCliId(cliente); // ⭐️ ¡Se asigna el OBJETO, no el ID!
            factura.setFacSubtotal(this.totalSubtotalConIVA);
            factura.setFacSubtotalcero(this.totalSubtotalCero);
            factura.setFacIva(this.totalIVA);
            factura.setFacDescuento(this.totalDescuento);
            factura.setFacTotal(this.totalGeneral);
            factura.setFacEstado("A");

            // (Si tienes la relación bidireccional, prepara la colección)
            // factura.setDetallefacturaCollection(new ArrayList<>());

            // --- 6. Preparar los Detalles (Modo JPA) ---
            for (Detallefactura det : detallefac) {

                // ⭐️ ¡CLAVE 5: Enlazar el detalle con la cabecera!
                det.setFacId(factura); // Asigna el objeto Factura al detalle

                // (Si es bidireccional)
                // factura.getDetallefacturaCollection().add(det);

                // Persistimos el detalle
                em.persist(det); 
            }
            
            // --- 7. Guardar la Cabecera ---
            // (Si no es bidireccional con Cascade.ALL, persistimos la factura ahora)
            em.persist(factura);

            // ⭐️ ¡CLAVE 6: Éxito! Confirmar la Transacción
            em.getTransaction().commit(); 
            this.idFacturaGuardada = factura.getFacId();
            mod_general.mod.showConfirmacion(
                    "Factura grabada correctamente (JPA). Número: " + facNumero);

            btn_imprimir.setDisable(false);
            
            //fun_limpiar(); // Limpia la interfaz
            cargarNumeroFacturaPreview();
        } catch (Exception e) {
            // ⭐️ ¡CLAVE 7: Error! Revertir la Transacción
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Deshace TODOS los cambios
            }

            mod_general.mod.showError("Error al grabar factura: " + e.getMessage());
            e.printStackTrace(); // Útil para depurar en la consola

        } finally {
            // ⭐️ ¡CLAVE 8: Cerrar el EntityManager!
            if (em != null) {
                em.close();
            }
        }
        
    }

    private void fun_limpiar() {
        // Limpia cabecera
        txt_documento.clear();
        limpiarCamposCliente(); // Tu método para limpiar datos del cliente
        txt_factura.clear(); // Limpia el número de factura si lo estabas mostrando

        // Limpia la tabla (items de la GUI)
        tbl_detallefactura.getItems().clear(); 

        // Añade la primera fila vacía para empezar de nuevo
        addFila(); 

        // Recalcula los totales (se pondrán en 0.00)
        calcularTotalesGenerales(); 
    }

    @FXML
    private void acc_anular(ActionEvent event) {
        //Anular
    }

    @FXML
    private void acc_imprimir(ActionEvent event) {

        // --- 1. Definir Constantes de Impresión ---
        // ⚠️ ¡CAMBIA ESTO por el nombre exacto de tu impresora!
        String NOMBRE_IMPRESORA = "EPSON TM-T88v ReceiptE4"; 
        // Comando de corte para la mayoría de impresoras (Epson)
        byte[] COMANDOS_CORTE = new byte[] { 0x1D, 0x56, 0x42, 0x00 }; 

        // --- 2. Validación (Lógica del Controlador) ---
        if (this.idFacturaGuardada == null) {
            mod_general.mod.showError(
                "No hay una factura guardada para imprimir. Grabe la factura primero.");
            return;
        }

        // --- 3. Rescatar Datos (Lógica del Controlador) ---
        EntityManager em = null;
        TicketDatos datos = new TicketDatos();

        try {
            em = JPAUtil.getEntityManagerFactory().createEntityManager();

            // "Rescatamos" la entidad completa de la BD
            Factura factura = gestorFactura.findFacturaWithDetails(this.idFacturaGuardada);
            if (factura == null) {
                throw new Exception("Factura no encontrada en la BD.");
            }
            
            // --- 4. "Empaquetar" los datos en el DTO ---
            datos.setNumeroFactura(factura.getFacNumero());
            datos.setDetalles(gestorDet.getDetallesporId(idFacturaGuardada));
            // Formatear la fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            datos.setFecha(sdf.format(factura.getFacFecha()));
           
            datos.setCliente(factura.getCliId());
         
            // Totales (vienen directos de la entidad Factura)
            datos.setSubtotal(factura.getFacSubtotal());
            datos.setSubtotal0(factura.getFacSubtotalcero());
            datos.setIva(factura.getFacIva());
            datos.setDescuento(factura.getFacDescuento());
            datos.setTotal(factura.getFacTotal());

        } catch (Exception e) {
            mod_general.mod.showError("Error al rescatar datos: " + e.getMessage());
            e.printStackTrace();
            if (em != null) em.close();
            return; // Salimos si no pudimos leer los datos
        } finally {
            if (em != null) em.close();
        }

        // 5 Generar el contenido
        String contenidoTicket = servicioImpresion.generarContenidoTicket(datos);

        // (Opcional: Imprimir en consola para depurar)
        System.out.println("--- INICIO TICKET ---");
        System.out.println(contenidoTicket);
        System.out.println("--- FIN TICKET ---");

        // 5.2. Intentar imprimir
        boolean impresionExitosa = servicioImpresion.imprimir(NOMBRE_IMPRESORA, contenidoTicket, COMANDOS_CORTE);

        // --- 6. Manejar el Resultado (Lógica del Controlador) ---
        if (impresionExitosa) {
            mod_general.mod.showError("Ticket enviado a la impresora.");
        } else {
            // La impresora falló, preguntamos para guardar
            boolean guardar = mostrarDialogoConfirmacion(
            "¿Desea guardar el ticket como un archivo de texto (.txt)?"
            );

            if (guardar) {
                try {
                    // Obtenemos la ventana actual (Stage) desde cualquier control
                    Window ventana = txt_factura.getScene().getWindow();

                    // Le pasamos el control al servicio para guardar
                    servicioImpresion.guardarTicketComoTxt(contenidoTicket, datos.getNumeroFactura(), ventana);

                    mod_general.mod.showConfirmacion("Ticket guardado exitosamente.");
                } catch (Exception e) {
                    mod_general.mod.showError("Error al guardar el ticket: " + e.getMessage());
                }
            }
        }
        fun_limpiar();
    }

    @FXML
    private void acc_cancelar(ActionEvent event) {
        limpiarCamposCliente();
        ap_factura.setVisible(false);
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
        txt_documento.clear(); // <-- Asegúrate de que esto esté aquí
        txt_nombres.clear();
        txt_apellidos.clear();
        txt_telefono.clear();
        txt_correo.clear();
        txt_direccion.clear();
        
        // ⭐️ Desmarca el CheckBox si se limpia manualmente
        if (chboxConsumidorFInal.isSelected()) {
            chboxConsumidorFInal.setSelected(false);
        }
        
        // Y nos aseguramos de que los campos estén habilitados
        habilitarCamposCliente(true);
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
    
    /**
     * Calcula los totales generales de la factura (subtotales, iva, descuento y total)
     * y los muestra en los campos de texto correspondientes.
     */
    private void calcularTotalesGenerales() {
        BigDecimal subtotalConIVA = BigDecimal.ZERO;
        BigDecimal subtotalCero = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;

        // 1. El descuento siempre será CERO
        BigDecimal descuento = BigDecimal.ZERO; 

        BigDecimal totalGeneral = BigDecimal.ZERO;

        List<Detallefactura> detallesValidos = tbl_detallefactura.getItems().stream()
                .filter(det -> det.getProdId() != null && det.getTotal() != null)
                .toList();

        for (Detallefactura det : detallesValidos) {
            BigDecimal totalFila = det.getTotal(); // Este es el Subtotal (Cant * PVP)

            // ¡RIESGO! Esto consulta el estado ACTUAL del producto.
            Short aplicaIva = det.getProdId().getProdAplicaiva(); 

            if (aplicaIva != null && aplicaIva == 1) {
                // Producto SÍ aplica IVA
                BigDecimal ivaFila = (det.getIva() != null) ? det.getIva() : BigDecimal.ZERO;
                subtotalConIVA = subtotalConIVA.add(totalFila);
                totalIVA = totalIVA.add(ivaFila);
            } else {
                // Producto NO aplica IVA (Subtotal 0%)
                subtotalCero = subtotalCero.add(totalFila);
            }
        }

        // 2. Ya no leemos txt_descuento. Se queda en CERO.

        // 3. Calcular el total general
        totalGeneral = subtotalConIVA.add(subtotalCero).add(totalIVA).subtract(descuento);

        // 4. Setear los valores en los TextFields (formateados)
        txt_subtotal.setText(formatoDosDecimales.format(subtotalConIVA));
        txt_subtotal0.setText(formatoDosDecimales.format(subtotalCero));
        txt_iva.setText(formatoDosDecimales.format(totalIVA));

        // Actualizamos el campo de descuento para que MUESTRE 0.00 o 0,00
        txt_descuento.setText(formatoDosDecimales.format(descuento)); 

        txt_total.setText(formatoDosDecimales.format(totalGeneral));
        
        // 5. Guardar los objetos BigDecimal para usarlos al grabar
        this.totalSubtotalConIVA = subtotalConIVA;
        this.totalSubtotalCero = subtotalCero;
        this.totalIVA = totalIVA;
        this.totalDescuento = descuento;
        this.totalGeneral = totalGeneral;
        
    }

    
    /**
    * Carga el número de factura (como vista previa) en un hilo separado
    * para no congelar la interfaz de usuario al iniciar la pantalla.
    */
   private void cargarNumeroFacturaPreview() {

       // Hacemos esto en un hilo nuevo para no congelar la UI
       new Thread(() -> {
           EntityManager em = null;
           String previewNumero = "Error al cargar"; // Mensaje por si falla

           try {
               em = JPAUtil.getEntityManagerFactory().createEntityManager();

               // 1. Crear la consulta al procedimiento (igual que en grabar)
               StoredProcedureQuery query = em.createStoredProcedureQuery("sp_generaNumFac");

               // 2. Registrar los parámetros
               query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN); // p_movimiento
               query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN); // p_terminal
               query.registerStoredProcedureParameter(3, String.class, ParameterMode.OUT); // p_numero_factura

               // 3. Setear los valores de ENTRADA
               query.setParameter(1, "FACTURA");
               query.setParameter(2, 1);

               // 4. Ejecutar el procedimiento
               query.execute();

               // 5. Obtener el valor de SALIDA
               previewNumero = (String) query.getOutputParameterValue(3);

           } catch (Exception e) {
               // Si falla (ej. no hay correlativo), el texto mostrará "Error al cargar"
               System.err.println("Error al cargar preview de factura: " + e.getMessage());
               e.printStackTrace();
           } finally {
               if (em != null) {
                   em.close(); // Cerramos el EntityManager de esta lectura
               }
           }

           // 6. Volver al Hilo de JavaFX para actualizar el TextField
           // (No puedes tocar la UI desde el hilo de fondo directamente)
           String finalNumero = previewNumero;
           Platform.runLater(() -> {
               txt_factura.setText(finalNumero);
               // (Opcional) Hacemos el campo no editable si solo es de muestra
               txt_factura.setEditable(false); 
           });

       }).start(); // Inicia el hilo
   }


   /**
    * Muestra un diálogo de confirmación (Sí/No) y espera la respuesta del usuario.
    * @param mensaje El texto principal que verá el usuario.
    * @return true si el usuario presiona "Sí" (o Aceptar), false si presiona "No" o cierra la ventana.
    */
   private boolean mostrarDialogoConfirmacion(String mensaje) {

       // 1. Crear el Alert de tipo CONFIRMATION
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("Confirmar Acción");
       alert.setHeaderText("Impresora no encontrada o falló.");
       alert.setContentText(mensaje);

       // (Opcional) Puedes cambiar el texto de los botones si lo deseas
       // ButtonType botonSi = new ButtonType("Sí, Guardar");
       // ButtonType botonNo = new ButtonType("No, Cancelar");
       // alert.getButtonTypes().setAll(botonSi, botonNo);

       // 2. Mostrar el diálogo Y ESPERAR a que el usuario haga clic
       Optional<ButtonType> resultado = alert.showAndWait();

       // 3. Devolver 'true' SOLO si el usuario presionó el botón OK (o "Sí")
       return resultado.isPresent() && resultado.get() == ButtonType.OK;

       // Si cambiaste los botones, usa:
       // return resultado.isPresent() && resultado.get() == botonSi;
   }
}
