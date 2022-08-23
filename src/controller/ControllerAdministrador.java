/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.PersonaJpaController;
import model.ProductoJpaController;
import model.Usuario;
import model.UsuarioJpaController;
import proyectoproducto.ManagerFactory;
import view.ViewAdministration;
import view.interna.ViewPersonas;
import view.interna.ViewProductos;
import view.interna.ViewUsuarios;

/**
 *
 * @author 59399
 */
public class ControllerAdministrador extends javax.swing.JFrame {

    private Usuario usuario;
    private ViewAdministration vista;
    private ManagerFactory manager;

    public ControllerAdministrador(Usuario usuario, ViewAdministration vista, ManagerFactory manager) {
        this.usuario = usuario;
        this.vista = vista;
        this.manager = manager;
        this.vista.setVisible(true);
        this.vista.setExtendedState(MAXIMIZED_BOTH);
        iniciarControl();
    }

    public void iniciarControl() {
        this.vista.getMenuPersona().addActionListener(l -> cargarVistaPersona());
        this.vista.getMenuUsuario().addActionListener(l -> cargarVistaUsuario());
        this.vista.getMenuProductos().addActionListener(l -> cargarVistaProducto());
    }
    public static ViewPersonas vp = null;

    public void cargarVistaPersona() {
        new ControllerPersona(new PersonaJpaController(manager.getEntityManagerFactory()), vp, manager, vista.getPanelEscritorio());
    }
    public static ViewProductos vpr = null;

    public void cargarVistaProducto() {
        new ControllerProducto(new ProductoJpaController(manager.getEntityManagerFactory()), vpr, manager, vista.getPanelEscritorio());
    }
    public static ViewUsuarios vu = null;

    public void cargarVistaUsuario() {
        new ControllerUsuario(new UsuarioJpaController(manager.getEntityManagerFactory()), vu, manager, vista.getPanelEscritorio());
    }
}
