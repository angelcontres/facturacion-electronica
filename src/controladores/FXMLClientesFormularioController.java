/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.ClienteJpaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelos.Cliente;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLClientesFormularioController implements Initializable {

    @FXML
    private Label lbl_nombre;
    @FXML
    private TextField txtCliNombre;
    @FXML
    private Label lblCedula;
    @FXML
    private TextField txtCedula;
    @FXML
    private Label lblApellidos;
    @FXML
    private TextField txtCliApellidos;
    @FXML
    private Label lblApellidos1;
    @FXML
    private Label lblTelefono;
    @FXML
    private TextField txtCliTelefono;
    @FXML
    private Label lblCorreo;
    @FXML
    private TextField txtCliCorreo;
    @FXML
    private CheckBox cbEstado;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextArea txtADireccion;

    private Cliente clienteEdicion;
    private boolean esEdicion = false;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mod_general.Mod_General.configurarNavegacion(this.txtCedula,this.txtCliNombre, txtCliApellidos, txtADireccion,txtCliTelefono, txtCliTelefono, txtCliCorreo, btnGuardar, btnCancelar);
    }    

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        System.out.println("Cancelar");
        cerrarVentana();
    }

    @FXML
    private void accBtnGuardar(ActionEvent event) {
        System.out.println("Grabar");
        String sCedula = txtCedula.getText().trim();
        String sNombre = txtCliNombre.getText().trim();
        String sApellidos = txtCliApellidos.getText().trim();
        String sDireccion = txtADireccion.getText().trim();
        String sTelefono = txtCliTelefono.getText().trim();
        String sCorreo = txtCliCorreo.getText().trim();
        
        boolean estado = cbEstado.isSelected();
        String sEstado = estado ? "A" : "F";
        
        if(!validar(sCedula, sNombre, sApellidos, sDireccion, sTelefono, sCorreo)){
            return;
        }
        
        EntityManagerFactory emf = null;
        try {
            // Crear EntityManagerFactory para esta operación
            emf = Persistence.createEntityManagerFactory("FacturacionPU");
            ClienteJpaController jpaCliente = new ClienteJpaController(emf); // ← AQUÍ ESTÁ EL CAMBIO
            
            if (esEdicion) {
                // Actualizar cliente existente
                actualizarCliente(jpaCliente, sNombre, sApellidos, sDireccion, sTelefono, sCorreo, sEstado);
                mod_general.mod.showConfirmacion("Cliente " + sNombre + " ha sido actualizado exitosamente");
            } else {
                // Crear nuevo cliente
                Cliente nuevoCliente = new Cliente(sCedula, sNombre, sApellidos, sDireccion, sTelefono, sCorreo, sEstado);
                jpaCliente.create(nuevoCliente);
                mod_general.mod.showConfirmacion("Cliente " + sNombre + " ha sido creado exitosamente");
            }
            
            cerrarVentana();
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al guardar cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar EntityManagerFactory
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }

    private boolean validar(String sCedula, String sNombre, String sApellidos, String sDireccion, String sTelefono, String sCorreo) {
        if(sCedula.isEmpty() && !esEdicion){
            mod_general.mod.showError("El campo cédula está vacío");
            txtCedula.requestFocus();
            return false;
        }        
        if(sNombre.isEmpty()){
            mod_general.mod.showError("El campo nombre está vacío");
            txtCliNombre.requestFocus();
            return false;
        }
        if(sApellidos.isEmpty()){
            mod_general.mod.showError("El campo apellidos está vacío");
            txtCliApellidos.requestFocus();
            return false;
        }
        if(sDireccion.isEmpty()){
            mod_general.mod.showError("El campo dirección está vacío");
            txtADireccion.requestFocus();
            return false;
        }
        if(sTelefono.isEmpty()){
            mod_general.mod.showError("El campo teléfono está vacío");
            txtCliTelefono.requestFocus();
            return false;
        }
        if(sCorreo.isEmpty()){
            mod_general.mod.showError("El campo correo está vacío");
            txtCliCorreo.requestFocus();
            return false;
        }
        
        // Validar formato de correo básico
        if (!sCorreo.contains("@") || !sCorreo.contains(".")) {
            mod_general.mod.showError("El formato del correo electrónico no es válido");
            txtCliCorreo.requestFocus();
            return false;
        }
        
        return true;
    }

    private void limpiarCampos() {
        txtADireccion.clear();
        txtCedula.clear();
        txtCliApellidos.clear();
        txtCliCorreo.clear();
        txtCliNombre.clear();
        txtCliTelefono.clear();
        cbEstado.setSelected(true);
    }

    /**
     * Método para recuperar cliente por ID (para edición)
     */
    void fun_recuperarClientexId(Cliente cliente) {
        if (cliente != null) {
            this.clienteEdicion = cliente;
            this.esEdicion = true;
            cargarDatosCliente();
        }
    }

    /**
     * Prepara el formulario para nuevo cliente
     */
    void nuevoCliente() {
        this.esEdicion = false;
        this.clienteEdicion = null;
        limpiarCampos();
        txtCedula.setDisable(false);
        btnGuardar.setText("Guardar");
        actualizarTituloVentana("Nuevo Cliente");
    }

    /**
     * Carga los datos del cliente para edición
     */
    void cargarClienteParaEdicion(Cliente cliente) {
        if (cliente != null) {
            this.clienteEdicion = cliente;
            this.esEdicion = true;
            cargarDatosCliente();
            txtCedula.setDisable(true); // No permitir editar cédula
            btnGuardar.setText("Actualizar");
            actualizarTituloVentana("Editar Cliente - " + cliente.getCliNombres());
        }
    }

    /**
     * Prepara el formulario para nuevo cliente (alternativo)
     */
    void prepararParaNuevoCliente() {
        nuevoCliente();
    }
    
    /**
     * Carga los datos del cliente en los campos del formulario
     */
    private void cargarDatosCliente() {
        if (clienteEdicion != null) {
            txtCedula.setText(clienteEdicion.getCliCedula());
            txtCliNombre.setText(clienteEdicion.getCliNombres());
            txtCliApellidos.setText(clienteEdicion.getCliApellidos());
            txtADireccion.setText(clienteEdicion.getCliDireccion());
            txtCliTelefono.setText(clienteEdicion.getCliTelefono());
            txtCliCorreo.setText(clienteEdicion.getCliCorreo());
            
            // Configurar estado
            if ("A".equals(clienteEdicion.getCliEstado())) {
                cbEstado.setSelected(true);
            } else {
                cbEstado.setSelected(false);
            }
        }
    }
    
    /**
     * Actualiza un cliente existente
     */
    private void actualizarCliente(ClienteJpaController jpaCliente, String nombre, String apellidos, 
                                 String direccion, String telefono, String correo, String estado) throws Exception {
        clienteEdicion.setCliNombres(nombre);
        clienteEdicion.setCliApellidos(apellidos);
        clienteEdicion.setCliDireccion(direccion);
        clienteEdicion.setCliTelefono(telefono);
        clienteEdicion.setCliCorreo(correo);
        clienteEdicion.setCliEstado(estado);
        
        jpaCliente.edit(clienteEdicion);
    }
    
    /**
     * Actualiza el título de la ventana de forma segura
     */
    private void actualizarTituloVentana(String titulo) {
        // Usar Platform.runLater para asegurar que la UI esté lista
        Platform.runLater(() -> {
            if (stage != null) {
                stage.setTitle(titulo);
            } else if (btnGuardar != null && btnGuardar.getScene() != null && btnGuardar.getScene().getWindow() != null) {
                stage = (Stage) btnGuardar.getScene().getWindow();
                stage.setTitle(titulo);
            }
        });
    }
    
    /**
     * Cierra la ventana de forma segura
     */
    private void cerrarVentana() {
        if (stage != null) {
            stage.close();
        } else if (btnCancelar != null && btnCancelar.getScene() != null && btnCancelar.getScene().getWindow() != null) {
            stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
    }
    
    /**
     * Método para establecer el stage desde el controlador padre
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}