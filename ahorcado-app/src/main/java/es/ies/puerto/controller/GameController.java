package es.ies.puerto.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.ies.puerto.PrincipalApplication;
import es.ies.puerto.controller.abstractas.AbstractController;
import es.ies.puerto.model.Palabra;
import es.ies.puerto.model.Palabras;
import es.ies.puerto.model.Usuario;
import es.ies.puerto.model.Usuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author santiago
 * @version 1.0.0
 */
public class GameController extends AbstractController {
    Palabras palabras;

    Usuario usuario;

    Usuarios usuarios;

    int oportunidades;

    @FXML
    Text nivel;

    @FXML
    Text textPalabra;

    Palabra palabra;

    @FXML
    TextField palabraText;

    @FXML
    Text mensajeText;

    List<Palabra> palabrasList;

    List<String> dificultades;

    List<String> letrasPista;

    @FXML
    Rectangle arm1;

    @FXML
    Rectangle arm2;

    @FXML
    Rectangle leg1;

    @FXML
    Rectangle leg2;

    @FXML
    Rectangle body;

    @FXML
    Circle head;

    List<Rectangle> listaRectangulos;

    /**
     * Metodo que coge de la base de datos la dificultad
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public GameController() throws ClassNotFoundException, SQLException {
        usuarios = new Usuarios();
        dificultades = new ArrayList<>(Arrays.asList("facil", "medio", "dificil"));
        usuario = usuarios.leer();

    }

    /**
     * Metodo que genera al inicar la pantalla
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void initialize() throws ClassNotFoundException, SQLException {
        palabras = new Palabras(nivel.getText());
        palabrasList = palabras.getFacil();
        palabra = palabrasList.get((int) (Math.random() * palabrasList.size()));
        letrasPista = palabra.letras(palabra);
        String mensaje = "";
        mensaje += palabras.ahorcado("", palabra);
        textPalabra.setText(mensaje);
        oportunidades = 6;
        letrasPista(letrasPista);
        listaRectangulos = new ArrayList<>(Arrays.asList(arm1, arm2, body, leg1, leg2));
    }

    /**
     * Metodo para el ahorcado
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws InterruptedException
     */
    @FXML
    protected void ahorcado() throws ClassNotFoundException, SQLException, InterruptedException {

        if (oportunidades > 0) {
            String textoInicio = textPalabra.getText();
            String mensaje = palabras.ahorcado(palabraText.getText(), palabra);
            mensaje = palabras.comparacion(mensaje, textPalabra.getText());
            textPalabra.setText(mensaje);
            if (mensaje.equals(palabra.getPalabraString())) {
                mensajeText.setText("Felicidades ganaste");
                cambiarDificultad();
                return;
            }
            if (!acertarOhNo(textoInicio, textPalabra.getText())) {
                mensajeText.setText("Muy bien");

                if (acertarOhNo(textPalabra.getText(), palabra.getPalabraString())) {
                    mensajeText.setText("Felicidades ganaste");
                    cambiarDificultad();
                }
                return;
            }
            oportunidades--;
            muñeco();
            mensajeText.setText(" Le quedan " + oportunidades + " oportunidades ");
            return;
        }
        mensajeText.setText("No dispone de más intentos");
        textPalabra.setText(palabra.getPalabraString());
    }

    /**
     * Metodo que comprueba el estado del ahorcado
     * @return
     */
    public boolean comprobar() {
        if (mensajeText.getText().equals("Felicidades ganaste")) {
            return true;
        }
        return false;
    }

    /**
     * Metodo que genera otro ahorcado
     * @param original
     * @param despues
     * @return
     */
    public boolean acertarOhNo(String original, String despues) {
        return original.equals(despues);
    }

    /**
     * Metodo que genera al azar la dificultad del ahorcado
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void cambiarDificultad() throws ClassNotFoundException, SQLException {
        int posicion = dificultades.indexOf(nivel.getText());
        if (posicion == dificultades.size() - 1) {
            nivel.setText(dificultades.get(0));
            initialize();
            return;
        }
        nivel.setText(dificultades.get(posicion + 1));
        initialize();
        restaurarMuñeco();
    }

    /**
     * Metodo que cambia a la pantalla anterior
     */
    @FXML
    protected void goBack() {
        try {

            FXMLLoader loader = new FXMLLoader(PrincipalApplication.class.getResource("perfil.fxml"));
            Parent root = loader.load();

            PerfilController registroController = loader.getController();
            registroController.setPropertiesIdioma(this.getPropertiesIdioma());

            Stage stage = (Stage) textPalabra.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que te genera una pista
     * @param letrasPista
     */
    public void letrasPista(List<String> letrasPista) {
        for (int i = 0; i < letrasPista.size(); i++) {
            String mensaje = palabras.ahorcado(letrasPista.get(i), palabra);
            mensaje = palabras.comparacion(mensaje, textPalabra.getText());
            textPalabra.setText(mensaje);
        }
    }

    public void muñeco() {
        if (!head.isVisible()) {
            head.setVisible(true);
            return;
        }
        for (int i = 0; i < listaRectangulos.size(); i++) {
            if (!listaRectangulos.get(i).isVisible()) {
                listaRectangulos.get(i).setVisible(true);
                return;
            }
        }
    }

    public void restaurarMuñeco() {
        if (head.isVisible()) {
            head.setVisible(false);
        }
        for (int i = 0; i < listaRectangulos.size(); i++) {
            if (listaRectangulos.get(i).isVisible()) {
                listaRectangulos.get(i).setVisible(false);
            }
        }
    }
}
