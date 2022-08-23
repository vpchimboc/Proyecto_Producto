/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoproducto;

import controller.ControllerLogin;
import java.sql.SQLException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import model.UsuarioJpaController;
import view.ViewLogin;

/**
 *
 * @author 59399
 */
public class ProyectoProducto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
            ManagerFactory manager = new ManagerFactory();
            UsuarioJpaController modeloUsuario = new UsuarioJpaController(manager.getEntityManagerFactory());
            ViewLogin vista = new ViewLogin();
            ControllerLogin controladorUsuario = new ControllerLogin(manager, modeloUsuario, vista);
    }

}
