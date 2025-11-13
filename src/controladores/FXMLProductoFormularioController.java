package controladores;

import controladoresjpa.ProductoJpaController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import modelos.Perfil;
import modelos.Producto;
import util.JPAUtil;

public class FXMLProductoFormularioController implements Initializable {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecioCompra;
    @FXML private TextField txtPvpMenor;
    @FXML private TextField txtPvpMayor;
    @FXML private TextField txtStock;
    @FXML private CheckBox chkAplicaIVA;
    @FXML private CheckBox chkEstado;
    @FXML private ImageView imgProducto;
    @FXML private Button btnSeleccionarImagen;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Producto productoEdicion;
    private boolean esEdicion = false;
    private Stage stage;
    private byte[] imagenBytes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración inicial
    }

    @FXML
    private void accBtnSeleccionarImagen(ActionEvent event) {
        seleccionarImagen();
    }

    @FXML
    private void accBtnGuardar(ActionEvent event) {
        guardarProducto();
    }

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        
        cerrarVentana();
    }

    /**
     * Abre un diálogo para seleccionar imagen
     */
    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen del producto");
        
        // Filtros para tipos de imagen
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Archivos de imagen", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"
        );
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                // Convertir imagen a byte[]
                convertirImagenABytes(file);
                
                // Mostrar imagen en el ImageView
                Image image = new Image(file.toURI().toString());
                imgProducto.setImage(image);
                
            } catch (IOException e) {
                mod_general.mod.showError("Error al cargar la imagen: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Convierte un archivo de imagen a array de bytes
     */
    private void convertirImagenABytes(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            imagenBytes = new byte[(int) file.length()];
            fileInputStream.read(imagenBytes);
        }
    }

    /**
     * Guarda el producto en la base de datos
     */
    private void guardarProducto() {
        if (!validarCampos(txtCodigo.getText().trim(),
                txtNombre.getText().trim(), 
                txtPrecioCompra.getText().trim(), 
                txtPvpMayor.getText().trim(), 
                txtPvpMenor.getText().trim(), 
                txtStock.getText().trim() 
        )) {
            return;
        }

        try {
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            ProductoJpaController jpaProducto = new ProductoJpaController(emf);

            if (esEdicion) {
                actualizarProducto(jpaProducto);
                mod_general.mod.showConfirmacion("Producto actualizado exitosamente");
            } else {
                crearNuevoProducto(jpaProducto);
                mod_general.mod.showConfirmacion("Producto creado exitosamente");
            }

            cerrarVentana();

        } catch (Exception e) {
            mod_general.mod.showError("Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crea un nuevo producto
     */
    private void crearNuevoProducto(ProductoJpaController jpaProducto) throws Exception {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setProdCod(txtCodigo.getText().trim());
        nuevoProducto.setProdNombre(txtNombre.getText().trim());
        nuevoProducto.setProdPreciocompra(new java.math.BigDecimal(txtPrecioCompra.getText().trim()));
        nuevoProducto.setProdPvpxmenor(new java.math.BigDecimal(txtPvpMenor.getText().trim()));
        nuevoProducto.setProdPvpxmayor(new java.math.BigDecimal(txtPvpMayor.getText().trim()));
        nuevoProducto.setProdStock(new java.math.BigDecimal(txtStock.getText().trim()));
        nuevoProducto.setProdAplicaiva(chkAplicaIVA.isSelected() ? (short) 1 : (short) 0);
        nuevoProducto.setProdEstado(chkEstado.isSelected() ? "A" : "I");
        
        // ✅ Guardar la imagen
        if (imagenBytes != null) {
            nuevoProducto.setPodImagen(imagenBytes);
        }

        jpaProducto.crear(nuevoProducto);
    }

    /**
     * Actualiza un producto existente
     */
    private void actualizarProducto(ProductoJpaController jpaProducto) throws Exception {
        productoEdicion.setProdCod(txtCodigo.getText().trim());
        productoEdicion.setProdNombre(txtNombre.getText().trim());
        productoEdicion.setProdPreciocompra(new java.math.BigDecimal(txtPrecioCompra.getText().trim()));
        productoEdicion.setProdPvpxmenor(new java.math.BigDecimal(txtPvpMenor.getText().trim()));
        productoEdicion.setProdPvpxmayor(new java.math.BigDecimal(txtPvpMayor.getText().trim()));
        productoEdicion.setProdStock(new java.math.BigDecimal(txtStock.getText().trim()));
        productoEdicion.setProdAplicaiva(chkAplicaIVA.isSelected() ? (short) 1 : (short) 0);
        productoEdicion.setProdEstado(chkEstado.isSelected() ? "A" : "I");
        
        // ✅ Actualizar imagen si se seleccionó una nueva
        if (imagenBytes != null) {
            productoEdicion.setPodImagen(imagenBytes);
        }

        jpaProducto.editar(productoEdicion);
    }

    /**
     * Carga la imagen desde bytes al ImageView
     */
    private void cargarImagenDesdeBytes(byte[] imagenBytes) {
        if (imagenBytes != null) {
            Image image = new Image(new java.io.ByteArrayInputStream(imagenBytes));
            imgProducto.setImage(image);
        }
    }

    /**
     * Valida los campos del formulario
     */
    private boolean validarCampos(String codigo, String nombre, String precioCompra, String pvpMenor, String pvpMayor, String stock) {

        // 1. Validar campos vacíos
        if (codigo.trim().isEmpty() || nombre.trim().isEmpty() || precioCompra.trim().isEmpty() ||
            pvpMenor.trim().isEmpty() || pvpMayor.trim().isEmpty() || stock.trim().isEmpty()) {

            //("Error de Validación", "Todos los campos son obligatorios. Por favor, llene toda la información.");
            mod_general.mod.showError("Todos los campos son obligatorios. Por favor, llene toda la información.");
            return false;
        }

        // 2. Validar campos numéricos (Precios)
        double pCompra, pMenor, pMayor;
        try {
            pCompra = Double.parseDouble(precioCompra);
            pMenor = Double.parseDouble(pvpMenor);
            pMayor = Double.parseDouble(pvpMayor);
        } catch (NumberFormatException e) {
            
            //mostrarAlerta("Error de Formato", "Los campos de precio (Compra, PVP Menor, PVP Mayor) deben ser números válidos (ej: 10.50).");
            mod_general.mod.showError("Los campos de precio (Compra, PVP Menor, PVP Mayor) deben ser números válidos (ej: 10.50).");
            return false;
        }

        // 3. Validar campo numérico (Stock)
        int stockNum;
        try {
            stockNum = Integer.parseInt(stock);
        } catch (NumberFormatException e) {
            //mostrarAlerta("Error de Formato", "El campo 'Stock' debe ser un número entero (ej: 50).");
            mod_general.mod.showError("El campo 'Stock' debe ser un número entero (ej: 50).");
            return false;
        }

        // 4. Validar números positivos
        if (pCompra <= 0 || pMenor <= 0 || pMayor <= 0) {
            
            //mostrarAlerta("Error de Validación", "Todos los precios deben ser valores positivos (mayores que cero).");
            mod_general.mod.showError("Todos los precios deben ser valores positivos (mayores que cero).");
            return false;
        }

        // 5. Validar stock no negativo
        if (stockNum < 0) {
            
            //mostrarAlerta("Error de Validación", "El stock no puede ser un número negativo.");
            mod_general.mod.showError("El stock no puede ser un número negativo.");
            return false;
        }

        // --- (Para posible implementacion mas adelante insanamente waza brio ashdjasb d) Validación de Lógica de Negocio ---
        //
        /*
        if (pMenor < pCompra) {
            mostrarAlerta("Error de Lógica", "El 'PVP Menor' no puede ser más bajo que el 'Precio de Compra'.");
            return false;
        }

        if (pMayor < pMenor) {
            mostrarAlerta("Error de Lógica", "El 'PVP Mayor' no puede ser más bajo que el 'PVP Menor'.");
            return false;
        }
        */

        // Si todas las validaciones pasan...
        return true;
    }

    private void cerrarVentana() {
        if (stage != null) {
            stage.close();
        } else if (btnCancelar != null && btnCancelar.getScene() != null && btnCancelar.getScene().getWindow() != null) {
            stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void prepararParaNuevoProducto() {
        this.esEdicion = false;
        this.productoEdicion = null;
        this.imagenBytes = null;
        limpiarCampos();
        btnGuardar.setText("Guardar");
    }

    public void cargarProductoParaEdicion(Producto producto) {
        if (producto != null) {
            this.esEdicion = true;
            this.productoEdicion = producto;
            this.imagenBytes = producto.getPodImagen();
            cargarDatosProducto();
            btnGuardar.setText("Actualizar");
        }
    }

    private void cargarDatosProducto() {
        if (productoEdicion != null) {
            txtCodigo.setText(productoEdicion.getProdCod());
            txtNombre.setText(productoEdicion.getProdNombre());
            txtPrecioCompra.setText(productoEdicion.getProdPreciocompra().toString());
            txtPvpMenor.setText(productoEdicion.getProdPvpxmenor().toString());
            txtPvpMayor.setText(productoEdicion.getProdPvpxmayor().toString());
            txtStock.setText(productoEdicion.getProdStock().toString());
            chkAplicaIVA.setSelected(productoEdicion.getProdAplicaiva() == 1);
            chkEstado.setSelected("A".equals(productoEdicion.getProdEstado()));
            
            // Cargar imagen si existe
            cargarImagenDesdeBytes(productoEdicion.getPodImagen());
        }
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecioCompra.clear();
        txtPvpMenor.clear();
        txtPvpMayor.clear();
        txtStock.clear();
        chkAplicaIVA.setSelected(false);
        chkEstado.setSelected(true);
        imgProducto.setImage(null);
        imagenBytes = null;
    }

}