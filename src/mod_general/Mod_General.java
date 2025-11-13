/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mod_general;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author usuario
 */
public class Mod_General {

    static int gestorBD = 2;
    public static String str_nombreBD;
    
    /**
     * Configura una lista de campos para que ENTER y TAB muevan el foco al siguiente campo,
     * y SHIFT+TAB mueva el foco al campo anterior.
     *
     * @param campos Lista de campos de texto en orden
     */
    public static void configurarNavegacion(Node... componentes) {
        for (int i = 0; i < componentes.length; i++) {
            final int index = i;
            Node actual = componentes[i];

            actual.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                    int siguienteIndex;
                    if (event.isShiftDown()) {
                        // Retroceder
                        siguienteIndex = (index - 1 + componentes.length) % componentes.length;
                    } else {
                        // Avanzar
                        siguienteIndex = (index + 1) % componentes.length;
                    }

                    Node siguiente = componentes[siguienteIndex];
                    siguiente.requestFocus();

                    // Si es botón y presionaron ENTER, activarlo
                    if (siguiente instanceof javafx.scene.control.Button && event.getCode() == KeyCode.ENTER) {
                        ((javafx.scene.control.Button) siguiente).fire();
                    }

                    event.consume();
                }
            });
        }
    }
}
    
    

