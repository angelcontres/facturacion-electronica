/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.EmpresaJpaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelos.Empresa;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLEmpresFormularioController implements Initializable {

    @FXML
    private TextField txtRuc;
    @FXML
    private TextField txtNombreEmpresa;
    @FXML
    private TextArea txtADireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private CheckBox chboxEstado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;
    
    private Stage stage;
    private Empresa empresaEdicion;
    private boolean esEdicion = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar navegación si es necesario
        mod_general.Mod_General.configurarNavegacion(
            txtRuc, txtNombreEmpresa, txtADireccion, txtTelefono, 
            btnAgregar, btnCancelar
        );
    }    

    @FXML
    private void accBtnAgregar(ActionEvent event) {
        String sRuc = txtRuc.getText().trim();
        String eNombre = txtNombreEmpresa.getText().trim();
        String eDireccion = txtADireccion.getText().trim();
        String eTelefono = txtTelefono.getText().trim();
        boolean estado = chboxEstado.isSelected();
        String sEstado = estado ? "A" : "F";
        
        // Validar antes de guardar
        if (!validar(sRuc, eNombre, eDireccion, eTelefono)) {
            return;
        }
        
        guardarEmpresa(sRuc, eNombre, eDireccion, eTelefono, sEstado);
    }

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }    

    /**
     * Prepara el formulario para nueva empresa
     */
    public void fun_nuevoCliente() {
        this.esEdicion = false;
        this.empresaEdicion = null;
        limpiarDatos();
        txtRuc.setDisable(false);
        btnAgregar.setText("Guardar");
        actualizarTituloVentana("Nueva Empresa");
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    public void limpiarDatos(){
        txtRuc.clear();
        txtNombreEmpresa.clear();
        txtADireccion.clear();
        txtTelefono.clear();
        chboxEstado.setSelected(true);
    }

    /**
     * Carga una empresa existente para edición
     */
    void fun_recuperarClientexId(Empresa empresa) {
        if (empresa != null) {
            this.empresaEdicion = empresa;
            this.esEdicion = true;
            cargarDatosEmpresa();
            txtRuc.setDisable(true); // No editar RUC
            btnAgregar.setText("Actualizar");
            actualizarTituloVentana("Editar Empresa - " + empresa.getEmpNombre());
        }
    }
    
    /**
     * Valida los campos del formulario
     */
    public boolean validar(String eRuc, String eNombre, String eDireccion, String eTelefono){
        if (eRuc.isEmpty()) {
            mod_general.mod.showError("El campo RUC es obligatorio");
            txtRuc.requestFocus();
            return false;
        }
        
        if (eRuc.length() != 13) {
            mod_general.mod.showError("El RUC debe tener 13 dígitos");
            txtRuc.requestFocus();
            return false;
        }
        
        if (!eRuc.matches("\\d+")) {
            mod_general.mod.showError("El RUC solo debe contener números");
            txtRuc.requestFocus();
            return false;
        }
        
        if (eNombre.isEmpty()) {
            mod_general.mod.showError("El nombre de la empresa es obligatorio");
            txtNombreEmpresa.requestFocus();
            return false;
        }
        
        if (eDireccion.isEmpty()) {
            mod_general.mod.showError("La dirección es obligatoria");
            txtADireccion.requestFocus();
            return false;
        }
        
        if (eTelefono.isEmpty()) {
            mod_general.mod.showError("El teléfono es obligatorio");
            txtTelefono.requestFocus();
            return false;
        }
        
        if (!eTelefono.matches("\\d+")) {
            mod_general.mod.showError("El teléfono solo debe contener números");
            txtTelefono.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Guarda o actualiza la empresa en la base de datos
     */
    private void guardarEmpresa(String ruc, String nombre, String direccion, String telefono, String estado) {
        EntityManagerFactory emf = null;
        try {
            emf = JPAUtil.getEntityManagerFactory();
            EmpresaJpaController jpaEmpresa = new EmpresaJpaController(emf);
            
            if (esEdicion) {
                // Actualizar empresa existente
                actualizarEmpresa(jpaEmpresa, nombre, direccion, telefono, estado);
                mod_general.mod.showConfirmacion("Empresa " + nombre + " actualizada exitosamente");
            } else {
                // Crear nueva empresa
                Empresa nuevaEmpresa = new Empresa(ruc, nombre, direccion, telefono, estado);
                jpaEmpresa.create(nuevaEmpresa);
                mod_general.mod.showConfirmacion("Empresa " + nombre + " creada exitosamente");
            }
            
            cerrarVentana();
            
        } catch (Exception e) {
            mod_general.mod.showError("Error al guardar empresa: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza los datos de una empresa existente
     */
    private void actualizarEmpresa(EmpresaJpaController jpaEmpresa, String nombre, 
                                 String direccion, String telefono, String estado) throws Exception {
        empresaEdicion.setEmpNombre(nombre);
        empresaEdicion.setEmpDireccion(direccion);
        empresaEdicion.setEmpTelefono(telefono);
        empresaEdicion.setEmpEstado(estado);
        
        jpaEmpresa.edit(empresaEdicion);
    }
    
    /**
     * Carga los datos de la empresa en los campos del formulario
     */
    private void cargarDatosEmpresa() {
        if (empresaEdicion != null) {
            txtRuc.setText(empresaEdicion.getEmpRuc());
            txtNombreEmpresa.setText(empresaEdicion.getEmpNombre());
            txtADireccion.setText(empresaEdicion.getEmpDireccion());
            txtTelefono.setText(empresaEdicion.getEmpTelefono());
            
            // Configurar estado
            if ("A".equals(empresaEdicion.getEmpEstado())) {
                chboxEstado.setSelected(true);
            } else {
                chboxEstado.setSelected(false);
            }
        }
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
     * Actualiza el título de la ventana
     */
    private void actualizarTituloVentana(String titulo) {
        if (stage != null) {
            stage.setTitle(titulo);
        }
    }
    
    /**
     * Establece el stage desde el controlador padre
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Método alternativo para nuevo cliente (consistencia con otros controladores)
     */
    public void prepararParaNuevaEmpresa() {
        fun_nuevoCliente();
    }
    
    /**
     * Método alternativo para edición (consistencia con otros controladores)
     */
    public void cargarEmpresaParaEdicion(Empresa empresa) {
        fun_recuperarClientexId(empresa);
    }
}