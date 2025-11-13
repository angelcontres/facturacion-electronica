/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.PerfilJpaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import mod_general.mod;
import modelos.Perfil;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLPerfilFormularioController implements Initializable {

    @FXML
    private TextField txtDescripcion;
    @FXML
    private CheckBox chbxEstado;
    @FXML
    private Button btnGrabar;
    @FXML
    private Button btnCancelar;

    private Perfil perfilEdicion;
    private boolean esEdicion = false;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar navegación si es necesario
        mod_general.Mod_General.configurarNavegacion(txtDescripcion, btnGrabar, btnCancelar);
    }

    @FXML
    private void accBtnGrabar(ActionEvent event) {
        String descripcion = txtDescripcion.getText().trim();
        boolean estado = chbxEstado.isSelected();
        String estadoStr = estado ? "A" : "F";

        if (!validarCampos(descripcion)) {
            return;
        }

        guardarPerfil(descripcion, estadoStr);
    }
    @FXML
    private void accBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    /**
     * Carga un perfil existente para edición
     */
    void fun_recuperarClientexId(Perfil perfil) {
        if (perfil != null) {
            this.perfilEdicion = perfil;
            this.esEdicion = true;
            cargarDatosPerfil();
            btnGrabar.setText("Actualizar");
            actualizarTituloVentana("Editar Perfil");
        }
    }

    /**
     * Prepara el formulario para nuevo perfil
     */
    void fun_nuevoCliente() {
        this.esEdicion = false;
        this.perfilEdicion = null;
        limpiarCampos();
        btnGrabar.setText("Guardar");
        actualizarTituloVentana("Nuevo Perfil");
    }

    /**
     * Valida los campos del formulario
     */
    private boolean validarCampos(String descripcion) {
        if (descripcion.isEmpty()) {
            mod.showError("La descripción del perfil es obligatoria");
            txtDescripcion.requestFocus();
            return false;
        }

        if (descripcion.length() < 3) {
            mod.showError("La descripción debe tener al menos 3 caracteres");
            txtDescripcion.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Guarda o actualiza el perfil en la base de datos
     */
    private void guardarPerfil(String descripcion, String estado) {
        EntityManagerFactory emf = null;
        try {
            emf = JPAUtil.getEntityManagerFactory();
            PerfilJpaController jpaPerfil = new PerfilJpaController(emf);

            if (esEdicion) {
                // Actualizar perfil existente
                actualizarPerfil(jpaPerfil, descripcion, estado);
                mod.showConfirmacion("Perfil actualizado exitosamente");
            } else {
                // Crear nuevo perfil
                Perfil nuevoPerfil = new Perfil();
                nuevoPerfil.setPerDescripcion(descripcion); // Asumiendo que se llama setPerNombre
                nuevoPerfil.setPerEstado(estado);
                
                jpaPerfil.create(nuevoPerfil);
                mod.showConfirmacion("Perfil creado exitosamente");
            }

            cerrarVentana();

        } catch (Exception e) {
            mod.showError("Error al guardar perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un perfil existente
     */
    private void actualizarPerfil(PerfilJpaController jpaPerfil, String descripcion, String estado) throws Exception {
        // Actualizar los campos del perfil existente
        // Ajusta estos métodos según los nombres reales de tu entidad Perfil
        perfilEdicion.setPerDescripcion(descripcion);
        perfilEdicion.setPerEstado(estado);

        jpaPerfil.edit(perfilEdicion);
    }

    /**
     * Carga los datos del perfil en los campos del formulario
     */
    private void cargarDatosPerfil() {
        if (perfilEdicion != null) {
            txtDescripcion.setText(perfilEdicion.getPerDescripcion()); // Asumiendo getPerNombre()
            
            // Configurar estado
            if ("A".equals(perfilEdicion.getPerEstado())) {
                chbxEstado.setSelected(true);
            } else {
                chbxEstado.setSelected(false);
            }
        }
    }

    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarCampos() {
        txtDescripcion.clear();
        chbxEstado.setSelected(true); // Por defecto activo
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
        } else if (btnGrabar != null && btnGrabar.getScene() != null && btnGrabar.getScene().getWindow() != null) {
            stage = (Stage) btnGrabar.getScene().getWindow();
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
     * Métodos alternativos para consistencia con otros controladores
     */
    public void prepararParaNuevoPerfil() {
        fun_nuevoCliente();
    }

    public void cargarPerfilParaEdicion(Perfil perfil) {
        fun_recuperarClientexId(perfil);
    }
}