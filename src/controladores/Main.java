/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package controladores;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.persistence.Persistence;
import mod_general.Mod_DB;
import util.JPAUtil;

/**
 *
 * @author usuario
 */
public class Main extends Application{

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Persistence.createEntityManagerFactory("FacturacionPU");
            System.out.println("✅ persistence.xml cargado correctamente");
        } catch (Exception e) {
            System.out.println("❌ Error al cargar persistence.xml");
            e.printStackTrace();
        }
        try {
//            Mod_DB conexion = new Mod_DB();
//            
//            if(conexion.conectarBD()){
//                System.out.println("Se ha conectado correctamente");
//                
//            }else{
//                System.out.println("Error al conectar a la base de datos.");
//
//            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLLogin.fxml"));
            loader.setResources(ResourceBundle.getBundle("resources.messages_en"));
            Parent pane = loader.load();
            Scene scene = new Scene(pane);

            stage.setScene(scene);
            stage.setTitle("Facturacion Electronica");

            stage.show();
          
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("" + e.getMessage());
            
        }
    }    
    @Override
    public void stop() throws Exception {
        JPAUtil.close(); // Cerrar EMF cuando la aplicación termina
        super.stop();
    }
        
}
