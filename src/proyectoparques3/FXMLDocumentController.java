
package proyectoparques3;

import com.mysql.cj.protocol.Resultset;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonCerrar;
    @FXML
    private Button buscar;
    @FXML
    private TextField textParque;
    @FXML
    private TextField textComunidad;
    @FXML
    private TextField textExtension;
    @FXML
    private TextField textNombre;
    @FXML
    private Label conectado;
    
    private Connection conn;
    private PreparedStatement ps;
    
    private String name;
    private double extension;
    private int idComunidad, id;

      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conectarDB();
    } 
    
    private void conectarDB(){
        String rut = "jdbc:mysql://localhost:3306/parques?serverTimezone=UTC";
        
        try {
            conn = DriverManager.getConnection(rut,"root","root");
            if(conn != null){
                conectado.setText("CONECTADO");
            }
        }catch(SQLException ex){
            System.out.println("ERROR SQL: " + ex.getMessage());
            System.out.println("Hubo un problema al intentar conectarse "
                                            + "con la base de datos parques");
            
        }catch(Exception es){
            System.out.println("ERROR EXCEPTION: " + es.getMessage());
        }        
    }
    
    private void desconectarDB(){        
        try {
            if(conn != null){
                conn.close();
                conectado.setText("DESCONECTADO");
            }
        }catch(SQLException ex){
            System.out.println("ERROR SQL: " + ex.getMessage());
        }catch(Exception es){
            System.out.println("ERROR EXCEPTION: " + es.getMessage());
        }
        
    }
    
    private void recogerDatos(){
        id = Integer.parseInt(textParque.getText());
        name = textNombre.getText();
        extension = Double.parseDouble(textExtension.getText());
        idComunidad = Integer.parseInt(textComunidad.getText());            
    }
    
    private void consultaEdicion(){
        int rows;
        String actualizacion = "update parque set nombre = ?, extension = ?, idComunidad = ? where id = ?";
        try {
            ps = conn.prepareStatement(actualizacion);
            
            ps.setString(1, name);
            ps.setDouble(2, extension);
            ps.setInt(3, idComunidad);
            ps.setInt(4, id);
            
            rows = ps.executeUpdate();
            
            if(rows != 0){
               mostrarConfirmacion(); 
            }
            
        }catch(SQLException ex){
            System.out.println("ERROR STATEMENT: " + ex.getMessage());
        }
    }
    
    private void consultaSelect(){
        String seleccion = "SELECT nombre,extension,idComunidad FROM parque WHERE id = ?";
        try{
            ps = conn.prepareStatement(seleccion);
            ps.setInt(1,Integer.parseInt(textParque.getText()));
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
            
                name = rs.getString("nombre");
                extension = rs.getDouble("extension");
                idComunidad = rs.getInt("idComunidad");

                textNombre.setText(name);
                textExtension.setText(String.valueOf(extension));
                textComunidad.setText(String.valueOf(idComunidad));
            
            }
            
        }catch(SQLException ex){
            System.out.println("ERROR SQL CONSULTA: " + ex.getMessage());
        }
    }
//    
//    private void consultaSelect2(){
//        int opcion = 0;
//        if(textNombre.getText().isEmpty()){
//            opcion = 1;
//        }
//        if(textNombre.getText().isEmpty() && textExtension.getText().isEmpty()){
//            
//        }
//    }
    
    private void mostrarConfirmacion(){
        Alert alertConfirm = new Alert(Alert.AlertType.INFORMATION);
        alertConfirm.setTitle("ACTUALIZACIÓN DB PARQUES");
        alertConfirm.setHeaderText(null);
        alertConfirm.setContentText("Operación realizada con exito....");
        
        alertConfirm.showAndWait();
    }

    @FXML
    private void accionGuardar(ActionEvent event) {
        recogerDatos();
        consultaEdicion();
    }

    @FXML
    private void accionCerrar(ActionEvent event) {
        desconectarDB();
    }

    @FXML
    private void accionBuscar(ActionEvent event) {
        consultaSelect();
    }
    
}
