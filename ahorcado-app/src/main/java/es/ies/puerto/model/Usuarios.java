package es.ies.puerto.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author santiago
 * @version 1.0.0
 */
public class Usuarios {
    private List<Usuario> usuarios;
    String path = "jdbc:sqlite:src/main/resources/db/Usuario.db";

    private Bbdd bbdd;

    /**
     * Metodo que crea Usuarios en la base de datos
     * 
     * @throws SQLException
     */
    public Usuarios() throws SQLException {
        try {
            bbdd = new Bbdd(path);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        usuarios = bbdd.getAllData(path);
        bbdd.closeConnection();
    }

    /**
     * Metodo que inicia sesion mediante nombre y password en la clase de Usuario
     * 
     * @param nombre
     * @param password
     * @return
     */
    public boolean iniciarSesion(String nombre, String password) {
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        if (password == null || password.isBlank()) {
            return false;
        }
        Usuario buscado = new Usuario(password, nombre);
        for (Usuario usuario : usuarios) {
            if (usuario.equals(buscado)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que busca Email
     * 
     * @param email
     * @return
     */
    public boolean findEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que agrega un Email al usuario
     * 
     * @param email
     * @return
     */
    public Usuario darUsuarioPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Metodo que recibe el Usuario en la base de datos
     * 
     * @param nombre
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Usuario recibirUsuario(String nombre) throws SQLException, ClassNotFoundException {
        bbdd = new Bbdd(path);
        return bbdd.findUsuario(nombre);
    }

    /**
     * Metodo que elimina el usuario
     * 
     * @param usuario
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean remove(Usuario usuario) throws SQLException, ClassNotFoundException {
        bbdd = new Bbdd(path);
        usuarios = bbdd.getAllData(path);
        if (usuarios.remove(usuario)) {
            try {
                bbdd.deleteData(usuario.getId());
                bbdd.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * Metodo que escribe el usuario en el .txt
     * 
     * @param usuario
     * @throws IOException
     */
    public void escribir(Usuario usuario) throws IOException {
        String path = "src/main/resources/es/ies/puerto/usuario.txt";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(usuario.toString());
        }
    }

    /**
     * Metodo que lee el usuario en el .txt
     * 
     * @return
     */
    public Usuario leer() {
        String path = "src/main/resources/es/ies/puerto/usuario.txt";
        File file = new File(path);
        Usuario usuario = new Usuario();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(",");
                int id = Integer.parseInt(array[3]);
                String nombre = array[0];
                String contrasenia = array[1];
                String email = array[2];
                usuario = new Usuario(id, contrasenia, email, nombre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    /**
     * Metodo que actualiza el usuario en el .txt
     * 
     * @param usuario
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean update(Usuario usuario) throws SQLException, ClassNotFoundException {
        bbdd = new Bbdd(path);
        usuarios = bbdd.getAllData(path);
        try {
            bbdd.updateData(usuario);
            bbdd.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Metodo que agrega el usuario en el .txt
     * 
     * @param usuario
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean aniadir(Usuario usuario) throws SQLException, ClassNotFoundException {
        bbdd = new Bbdd(path);
        try {
            bbdd.insertData(usuario);
            usuarios = bbdd.getAllData(path);
            bbdd.closeConnection();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Constructor de Usuarios que agrega la lista de Usuarios, el path y la base de
     * datos
     * 
     * @param usuarios
     * @param path
     * @param bbdd
     */
    public Usuarios(List<Usuario> usuarios, String path, Bbdd bbdd) {
        this.usuarios = usuarios;
        this.path = path;
        this.bbdd = bbdd;
    }

    // Getters y Setters
    public List<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bbdd getBbdd() {
        return this.bbdd;
    }

    public void setBbdd(Bbdd bbdd) {
        this.bbdd = bbdd;
    }

    public Usuarios usuarios(List<Usuario> usuarios) {
        setUsuarios(usuarios);
        return this;
    }

    public Usuarios path(String path) {
        setPath(path);
        return this;
    }

    public Usuarios bbdd(Bbdd bbdd) {
        setBbdd(bbdd);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarios, path, bbdd);
    }

    @Override
    public String toString() {
        return "" + getUsuarios();
    }

}
