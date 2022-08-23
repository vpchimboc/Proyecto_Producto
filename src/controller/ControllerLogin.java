/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import model.Usuario;
import model.UsuarioJpaController;
import proyectoproducto.ManagerFactory;
import view.ViewAdministration;
import view.ViewLogin;

/**
 *
 * @author 59399
 */
public class ControllerLogin {

    private UsuarioJpaController modelo;
    private ViewLogin vista;
    private ManagerFactory manager;

    public ControllerLogin(ManagerFactory manager, UsuarioJpaController modelo, ViewLogin vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.manager = manager;
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        iniciarControl();
    }

    public void iniciarControl() {
        vista.getBtnEntrar().addActionListener(l -> controlLogin());
        vista.getBtnCerrar().addActionListener(l -> cerrar());
    }

    public void cerrar() {
        System.exit(0);
    }

    public void controlLogin() {
        String user = vista.getTxtUsuario().getText();
        String clave = new String(vista.getTxtClave().getPassword());
        try {
            if (!"".equals(user) && !"".equals(clave)) {
                Usuario usuario = modelo.buscarByCredenciales(user, clave);
                if (usuario != null) {
                    Resouces.success("Usuario", "Correcto");
                    ViewAdministration vistaAdministrador = new ViewAdministration();
                    this.vista.dispose();
                    ControllerAdministrador controllerAdministrador = new ControllerAdministrador(usuario, vistaAdministrador, manager);
                    
                } else {
                    Resouces.error("Usuario", "Incorrecto");
                }
            } else {
                Resouces.warning("Atención", "Rellene bien la información");
            }
        } catch (PersistenceException e) {
            Resouces.error("No existe conexión", "Con la Base de Datos");
        }

    }
}
