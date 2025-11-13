/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mod_general;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import modelos.Usuario;

/**
 *
 * @author usuario
 */
public class mod {
    
    public static  Usuario userActual = null;
    
    public static void showError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText("❌ Error");
        ajustarTamaño(alert);
        alert.showAndWait();
    }

    public static void showConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setTitle("Confirmación");
        alert.setHeaderText("✅ Confirmación");
        ajustarTamaño(alert);
        alert.showAndWait();
    }

    // Ajuste opcional para evitar que los textos largos se corten
    private static void ajustarTamaño(Alert alert) {
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    }
}
