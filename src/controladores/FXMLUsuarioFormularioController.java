/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.PerfilJpaController;
import controladoresjpa.UsuarioJpaController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import mod_general.mod;
import modelos.Perfil;
import modelos.Usuario;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLUsuarioFormularioController implements Initializable {

    @FXML
    private TextField txtSeguridad;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtClave;
    @FXML
    private ComboBox<Perfil> cboxPerfilRol;
    @FXML
    private Button btnGrabar;
    @FXML
    private Button btnCancelar;
    
    private UsuarioJpaController gestorUsuario;
    private PerfilJpaController gestorPerfil;
    private Usuario usuarioEdicion;
    private boolean esEdicion = false;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mod_general.Mod_General.configurarNavegacion(txtNombre, txtUsuario, txtClave, txtSeguridad, cboxPerfilRol, btnGrabar, btnCancelar);
        
        try {
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            this.gestorPerfil = new PerfilJpaController(emf);
            List<Perfil> perfiles = gestorPerfil.findPerfilEntities();
            
            ObservableList<Perfil> observablePerfiles = FXCollections.observableArrayList(perfiles);
            cboxPerfilRol.setItems(observablePerfiles);
            
            // Configurar cómo mostrar los perfiles
            cboxPerfilRol.setCellFactory(param -> new javafx.scene.control.ListCell<Perfil>() {
                @Override
                protected void updateItem(Perfil item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getPerDescripcion());
                    }
                }
            });
            
            cboxPerfilRol.setButtonCell(new javafx.scene.control.ListCell<Perfil>() {
                @Override
                protected void updateItem(Perfil item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Seleccionar Perfil");
                    } else {
                        setText(item.getPerDescripcion());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            mod.showError("Error al cargar los perfiles: " + e.getMessage());
        }
    }

    @FXML
    private void accBtnGrabar(ActionEvent event) {
        System.out.println("Grabar usuario");
        String nombre = txtNombre.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String clave = txtClave.getText().trim();
        String codigoSeguridad = txtSeguridad.getText().trim();
        Perfil perfilSeleccionado = cboxPerfilRol.getValue();
        
        if(!esEdicion && !codigoSeguridad.equals("12345")){
            mod.showError("Código de seguridad incorrecto");
            txtSeguridad.requestFocus();
            return; 
        }
        
        if(!validarCampos(nombre, usuario, clave, perfilSeleccionado)){
            return;
        }
        
        try {
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            UsuarioJpaController gestorUsuario = new UsuarioJpaController(emf);
            
            if (esEdicion) {
                actualizarUsuario(gestorUsuario, nombre, usuario, clave, perfilSeleccionado);
                mod.showConfirmacion("Usuario actualizado exitosamente");
            } else {
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setUsrNombres(nombre);
                nuevoUsuario.setUsrUsuario(usuario);
                nuevoUsuario.setUsrClave(clave);
                nuevoUsuario.setPerId(perfilSeleccionado);
                nuevoUsuario.setUsrEstado("A");
                
                gestorUsuario.create(nuevoUsuario);
                mod.showConfirmacion("Usuario creado exitosamente");
            }
            
            
            if(this.esEdicion){
            cerrarVentana();
            }
            else{
                funAbrirVentana("/vista/FXMLLogin.fxml", event);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mod.showError("Error al guardar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void accBtnCancelar(ActionEvent event) {
        System.out.println("Cancelar");
        if(this.esEdicion){
            cerrarVentana();
        }
        else{
            funAbrirVentana("/vista/FXMLLogin.fxml", event);
        }
    }

    private boolean validarCampos(String nombre, String usuario, String clave, Perfil perfilSeleccionado) {
        if(nombre.isEmpty()){
            mod.showError("El campo nombre está vacío");
            txtNombre.requestFocus();
            return false;
        }
        if(usuario.isEmpty()){
            mod.showError("El campo usuario está vacío");
            txtUsuario.requestFocus();
            return false;            
        }
        if(clave.isEmpty()){
            mod.showError("El campo clave está vacío");
            txtClave.requestFocus();
            return false;
        }
        if(clave.length() < 4){
            mod.showError("La clave debe tener al menos 4 caracteres");
            txtClave.requestFocus();
            return false;
        }
        if(perfilSeleccionado == null){ 
            mod.showError("No ha seleccionado un Rol/Perfil");
            cboxPerfilRol.requestFocus();
            return false;
        }
        return true;
    }
    
    private void actualizarUsuario(UsuarioJpaController gestorUsuario, String nombre, 
                                 String usuario, String clave, Perfil perfil) throws Exception {
        usuarioEdicion.setUsrNombres(nombre);
        usuarioEdicion.setUsrUsuario(usuario);
        usuarioEdicion.setUsrClave(clave);
        usuarioEdicion.setPerId(perfil);
        
        gestorUsuario.edit(usuarioEdicion);
    }
    
    public void prepararParaNuevoUsuario() {
        this.esEdicion = false;
        this.usuarioEdicion = null;
        limpiarCampos();
        txtSeguridad.setDisable(false);
        btnGrabar.setText("Guardar");
        actualizarTituloVentana("Nuevo Usuario");
    }
    
    public void cargarUsuarioParaEdicion(Usuario usuario) {
        if (usuario != null) {
            this.esEdicion = true;
            this.usuarioEdicion = usuario;
            cargarDatosUsuario();
            txtSeguridad.setDisable(false);
            btnGrabar.setText("Actualizar");
            actualizarTituloVentana("Editar Usuario - " + usuario.getUsrNombres());
        }
    }
    
    private void cargarDatosUsuario() {
        if (usuarioEdicion != null) {
            txtNombre.setText(usuarioEdicion.getUsrNombres());
            txtUsuario.setText(usuarioEdicion.getUsrUsuario());
            txtClave.setText(usuarioEdicion.getUsrClave());
            cboxPerfilRol.setValue(usuarioEdicion.getPerId());
        }
    }
    
    private void limpiarCampos() {
        txtNombre.clear();
        txtUsuario.clear();
        txtClave.clear();
        txtSeguridad.clear();
        cboxPerfilRol.setValue(null);
    }
    
    /**
     * Actualiza el título de la ventana de forma segura
     */
    private void actualizarTituloVentana(String titulo) {
        // ✅ CORREGIDO: Usar Platform.runLater y verificar múltiples formas
        Platform.runLater(() -> {
            if (stage != null) {
                stage.setTitle(titulo);
            } else if (btnGrabar != null && btnGrabar.getScene() != null) {
                Stage currentStage = (Stage) btnGrabar.getScene().getWindow();
                if (currentStage != null) {
                    stage = currentStage;
                    stage.setTitle(titulo);
                }
            }
        });
    }
    
    /**
     * Cierra la ventana de forma segura
     */
    private void cerrarVentana() {
        if (stage != null) {
            stage.close();
        } else if (btnCancelar != null && btnCancelar.getScene() != null) {
            Stage currentStage = (Stage) btnCancelar.getScene().getWindow();
            if (currentStage != null) {
                stage = currentStage;
                stage.close();
            }
        }
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
        // ✅ Actualizar título inmediatamente si el stage está disponible
        if (stage != null && usuarioEdicion != null) {
            stage.setTitle("Editar Usuario - " + usuarioEdicion.getUsrNombres());
        } else if (stage != null) {
            stage.setTitle("Nuevo Usuario");
        }
    }
    
    private void funAbrirVentana(String direccion, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(direccion));
            loader.setResources(ResourceBundle.getBundle("resources.messages_en"));
            Pane root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("No se pudo cargar la página: " + direccion);
            e.printStackTrace();
        }
    }
    
    
}