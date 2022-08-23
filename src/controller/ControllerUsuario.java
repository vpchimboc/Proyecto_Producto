/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Dimension;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.Persona;
import model.PersonaJpaController;
import model.Usuario;
import model.UsuarioJpaController;
import model.exceptions.NonexistentEntityException;
import proyectoproducto.ManagerFactory;
import view.interna.ViewUsuarios;

/**
 *
 * @author 59399
 */
public class ControllerUsuario {
    //Creamos un objeto para los metodos con la base de datos

    private UsuarioJpaController modeloUsuario;
    //Creamos un objeto de la vista
    private ViewUsuarios vista;
    //Para la unidad de persisitencia a la base de datos
    private ManagerFactory manager;
    //Creamos un objeto de la clase modelo tabla para cargar los datos a la tabla
    private ModeloTablaUsuarios modelotabla;
    //Creamos un objeto del escritorio principal
    private JDesktopPane panelEscritorio;
    //Creamos un objeto de la clase usuario
    private Usuario usuario;
    //Para escuchar los elementos seleccionados del modelo de la tabla
    private ListSelectionModel listaUsuariosModel;

    public ControllerUsuario(UsuarioJpaController modeloUsuario, ViewUsuarios vista, ManagerFactory manager, JDesktopPane panelEscritorio) {
        this.modeloUsuario = modeloUsuario;
        this.manager = manager;
        this.panelEscritorio = panelEscritorio;
        //Inicializamos el modelo tabla
        modelotabla = new ModeloTablaUsuarios();
        //Asignamos datos al modelo
        modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
        //Controlamos si la ventana interna ya existe
        if (ControllerAdministrador.vu == null) {
            ControllerAdministrador.vu = new ViewUsuarios();
            this.panelEscritorio.add(ControllerAdministrador.vu);
            this.vista = ControllerAdministrador.vu;
            //Agregamos los datos en la tabla
            this.vista.getJtableUsuario().setModel(modelotabla);
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            ControllerAdministrador.vu.show();
            //Iniciar eventos
            iniciarControl();
            cargarCombobox();
        } else {
            ControllerAdministrador.vu.show();
        }
    }

    public void iniciarControl() {
        //Eventos botones
        this.vista.getBtnGuardar().addActionListener(l -> guadarUsuario());
        this.vista.getBtnEditar().addActionListener(l -> editarUsuario());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarUsuario());
        this.vista.getBtnLimpiar().addActionListener(l -> limpiar());
        this.vista.getBtnLimpCriterio().addActionListener(l -> limpiarBuscador());
        this.vista.getBtnBuscar().addActionListener(l -> buscarUsuario());
        this.vista.getjCheckMostrar().addActionListener(l -> buscarUsuario());
        //Eventos tabla
        this.vista.getJtableUsuario().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuariosModel = this.vista.getJtableUsuario().getSelectionModel();
        listaUsuariosModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    usuarioSelecionado();
                }
            }

        });
        //Control Botones de incio
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
    }
     public void cargarCombobox() {
        try {
            Vector v = new Vector();
            v.addAll(new PersonaJpaController(manager.getEntityManagerFactory()).findPersonaEntities());
            this.vista.getjComboPersonas().setModel(new DefaultComboBoxModel(v));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Capturando errores cargarCombobox()");
        }
    }
//Metodo para cargar datos a la vista cuando se selecciona un elemento de la tabla

    public void usuarioSelecionado() {
        if (this.vista.getJtableUsuario().getSelectedRow() != -1) {
            usuario = modelotabla.getFilas().get(this.vista.getJtableUsuario().getSelectedRow());
            this.vista.getTxtUsuario().setText(usuario.getUsuario());
            this.vista.getTxtClave().setText(usuario.getClave());
            this.vista.getjComboPersonas().setSelectedItem(usuario.getIdpersona());
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnGuardar().setEnabled(false);
        }

    }

    public void limpiarBuscador() {
        this.vista.getTxtCriterio().setText("");
        modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
        modelotabla.fireTableDataChanged();
    }

    public void limpiar() {
        this.vista.getTxtUsuario().setText("");
        this.vista.getTxtClave().setText("");
        usuario = null;
        this.vista.getjComboPersonas().setSelectedIndex(0);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnGuardar().setEnabled(true);
        this.vista.getJtableUsuario().getSelectionModel().clearSelection();
    }

    public void buscarUsuario() {
        if (this.vista.getjCheckMostrar().isSelected()) {
            modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
            modelotabla.fireTableDataChanged();
        } else {
            if (!this.vista.getTxtCriterio().getText().equals("")) {
                modelotabla.setFilas(modeloUsuario.buscarUsuario(this.vista.getTxtCriterio().getText()));
                modelotabla.fireTableDataChanged();
            } else {
                modelotabla.setFilas(modeloUsuario.findUsuarioEntities());
                modelotabla.fireTableDataChanged();
            }
        }
    }

    public void guadarUsuario() {
        usuario = new Usuario();
        usuario.setUsuario(this.vista.getTxtUsuario().getText());
        usuario.setClave(new String(this.vista.getTxtClave().getPassword()));
        usuario.setIdpersona((Persona) this.vista.getjComboPersonas().getSelectedItem());
        modeloUsuario.create(usuario);
        modelotabla.agregar(usuario);
        Resouces.success("Atención!!", "Usuario creado correctamente");
        limpiar();
    }

    public void editarUsuario() {
        if (usuario != null) {
            usuario.setUsuario(this.vista.getTxtUsuario().getText());
            usuario.setClave(new String(this.vista.getTxtClave().getPassword()));
            usuario.setIdpersona((Persona) this.vista.getjComboPersonas().getSelectedItem());
            try {
                modeloUsuario.edit(usuario);
            } catch (Exception ex) {
                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
            modelotabla.eliminar(usuario);
            modelotabla.actualizar(usuario);
            Resouces.success("Atención!!", "Usuario editada correctamente");
            limpiar();
        }
    }

    public void eliminarUsuario() {
        if (usuario != null) {
            try {
                modeloUsuario.destroy(usuario.getIdusuario());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

            modelotabla.eliminar(usuario);
            Resouces.success("Atención!!", "Usuario eliminada correctamente");
            limpiar();

        }
    }

}
