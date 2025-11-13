/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import controladoresjpa.UsuarioJpaController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelos.Usuario;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.GestureEvent;
import javax.persistence.EntityManagerFactory;
import mod_general.mod;
import util.JPAUtil;

/**
 * FXML Controller class
 *
 * @author usuario
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private Label lblṔassword;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblUser;
    @FXML
    private TextField txtUser;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnSalir;
    
//    private UsuarioJpaController gestorUsuario = new UsuarioJpaController();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // Navegación entre campos
        mod_general.Mod_General.configurarNavegacion(txtUser, txtPassword, btnLogin);

    }
    /**
     * Funcion que recibe por parametros el evento del boton click
     * Sirve para inciar sesion en el aplicativo
     * Una vez que se valida se permite entrar al aplicativo a la pantalla principal
     * @parametro event
     */
    @FXML
    private void accBtnLogin(ActionEvent event) {
        String user = txtUser.getText();
        String password = txtPassword.getText();
        if (validar(user, password)) {
            
            return;
        }
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        UsuarioJpaController gestorUsuario = new UsuarioJpaController(emf);
        Usuario usuario = gestorUsuario.login(user, password);

        if (usuario == null) {
            System.out.println("El usuario o la contraseña son incorrectos");
            mod_general.mod.showError("El usuario o la contraseña son incorrectos");
            limpiezadecampos();
            return;
        }
        mod_general.mod.userActual = usuario;
        // Usuario encontrado, se puede iniciar sesión
        System.out.println("Login exitoso: " + usuario.getUsrNombres());
        funAbrirVentana("/vista/FXMLPrincipal.fxml", event);
        
    }
    
    /**
     * Funcion que recibe el evento click
     * sirve para entrar a la pantalla de crear cuenta para poder entrar al aplicativo
     * @param event 
     * 
     */
    @FXML
    private void accBtnSignUp(ActionEvent event) {
        System.out.println("Entrando a la ventana para crear cuenta");
        funAbrirVentana("/vista/FXMLUsuarioFormulario.fxml", event);
        
    }

    private boolean validar(String user, String password) {
        if (user.isEmpty()) {
            System.out.println("El usuario esta vacio");
            mod_general.mod.showError("El usuario esta vacio");
            limpiezadecampos();
            return true;
        }
        if (password.isEmpty()) {
            System.out.println("La contraseña esta vacia");
            mod_general.mod.showError("La contraseña esta vacia");
            limpiezadecampos();
            return true;
        }
        return false;
    }

    private void funAbrirVentana(String direccion, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(direccion));
            //loader.setResources(ResourceBundle.getBundle("resources.messages_en"));
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

    
    private void limpiezadecampos() {
        this.txtPassword.clear();
        this.txtUser.clear();
    }

    @FXML
    private void accBtnSalir(ActionEvent event) {
        System.out.println("Salir");
        Platform.exit();
    }

}
