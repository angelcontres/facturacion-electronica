/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.PrintException;
import modelos.Detallefactura;
import modelos.TicketDatos;

/**
 *
 * @author usuario
 */


/**
 * Clase especialista en formatear e imprimir tickets.
 * Recibe los datos en un DTO y se encarga del resto.
 */
public class ServicioImpresion {

    // (Puedes mover este formato a una clase de utilidades)
    private final DecimalFormat formatoDosDecimales = new DecimalFormat("0.00");

    
    
    /**
     * Genera el contenido de texto plano del ticket basado en los datos.
     * @param datos El DTO con toda la información.
     * @return Un String listo para imprimir.
     */
    public String generarContenidoTicket(TicketDatos datos) {
        // Ajusta el ancho (ej. 40 caracteres)
        StringBuilder ticket = new StringBuilder();
        String separador = "----------------------------------------\n"; 

        // Encabezado de la empresa
        ticket.append("         MI NEGOCIO S.A.\n");
        ticket.append("       RUC: 1234567890001\n");
        ticket.append("      Av. Siempre Viva 123\n");
        ticket.append("       Telf: 0991234567\n");
        ticket.append(separador);
        
        // Datos de la factura
        ticket.append(String.format("Factura: %s\n", datos.getNumeroFactura()));
        ticket.append(String.format("Fecha: %s\n", datos.getFecha()));
        ticket.append(separador);
        
        // Datos del cliente
        ticket.append(String.format("Cliente: %s\n", datos.getCliente().getCliNombres() + " " + datos.getCliente().getCliApellidos()));
        ticket.append(String.format("RUC/CI:  %s\n", datos.getCliente().getCliCedula()));
        ticket.append(String.format("Direcc:  %s\n", datos.getCliente().getCliDireccion()));
        ticket.append(separador);

        // Cabecera de detalles
        ticket.append(String.format("%-5s %-19s %6s %7s\n", "Cant", "Descripcion", "P.Unit", "Total")); 
        ticket.append(separador);

        // Detalles (iteramos sobre las entidades JPA)
        for (Detallefactura det : datos.getDetalles()) {
            String nombreProd = det.getProdNombre();
            if (nombreProd.length() > 19) {
                nombreProd = nombreProd.substring(0, 19); // Truncar
            }
            
            String cant = String.format("%-5s", det.getCantidad()); // BigInteger a String
            String pvp = String.format("%6s", formatoDosDecimales.format(det.getProdPvp()));
            String total = String.format("%7s", formatoDosDecimales.format(det.getTotal()));
            
            ticket.append(String.format("%s %-19s %s %s\n", cant, nombreProd, pvp, total));
        }
        ticket.append(separador);
        
        // Totales (Alineados a la derecha)
        ticket.append(String.format("%30s %s\n", "SUBTOTAL 15%:", formatoDosDecimales.format(datos.getSubtotal())));
        ticket.append(String.format("%30s %s\n", "SUBTOTAL 0%:", formatoDosDecimales.format(datos.getSubtotal0())));
        ticket.append(String.format("%30s %s\n", "IVA 15%:", formatoDosDecimales.format(datos.getIva())));
        ticket.append(String.format("%30s %s\n", "DESCUENTO:", formatoDosDecimales.format(datos.getDescuento())));
        ticket.append(String.format("%30s %s\n", "TOTAL:", formatoDosDecimales.format(datos.getTotal())));
        ticket.append(separador);
        ticket.append("\n\n");
        ticket.append("        GRACIAS POR SU COMPRA\n");
        ticket.append("\n\n\n");
        
        return ticket.toString();
    }

    /**
     * Envía el ticket a la impresora.
     * @return true si tuvo éxito, false si no encontró la impresora o falló.
     */
    public boolean imprimir(String nombreImpresora, String contenido, byte[] comandosCorte) {
        try {
            PrintService impresora = buscarImpresora(nombreImpresora);
            if (impresora == null) {
                System.err.println("Impresora no encontrada: " + nombreImpresora);
                return false; // Fracaso: impresora no existe
            }

            DocPrintJob job = impresora.createPrintJob();
            
            // Unimos el texto con los comandos de corte
            byte[] textoBytes = contenido.getBytes("CP437"); // Encoding para tickets
            byte[] ticketCompleto = new byte[textoBytes.length + comandosCorte.length];
            System.arraycopy(textoBytes, 0, ticketCompleto, 0, textoBytes.length);
            System.arraycopy(comandosCorte, 0, ticketCompleto, textoBytes.length, comandosCorte.length);
            
            Doc doc = new SimpleDoc(ticketCompleto, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            job.print(doc, null);
            
            return true; // Éxito

        } catch (PrintException | IOException e) {
            System.err.println("Error de impresión: " + e.getMessage());
            e.printStackTrace();
            return false; // Fracaso
        }
    }
    
    /** Busca un PrintService por su nombre exacto. */
    private PrintService buscarImpresora(String nombreImpresora) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(nombreImpresora)) {
                return service;
            }
        }
        return null;
    }

    /**
     * Abre un diálogo "Guardar como..." para guardar el ticket como .txt.
     */
    public void guardarTicketComoTxt(String contenidoTicket, String numeroFactura, Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Ticket como .txt");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de Texto (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        String nombreArchivo = "Factura-" + numeroFactura.replace("-", "") + ".txt";
        fileChooser.setInitialFileName(nombreArchivo);

        File archivo = fileChooser.showSaveDialog(ownerWindow);

        if (archivo != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write(contenidoTicket);
                // Nota: Los mensajes de éxito/error se manejan en el controlador
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo: " + e.getMessage());
                // Lanzamos una RuntimeException para que el controlador la muestre
                throw new RuntimeException("Error al guardar el archivo: " + e.getMessage(), e);
            }
        }
    }
}
