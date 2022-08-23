/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import static controller.ControllerAdministrador.vp;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.Persona;
import model.PersonaJpaController;
import model.exceptions.NonexistentEntityException;
import proyectoproducto.ManagerFactory;
import view.interna.ViewPersonas;

/**
 *
 * @author 59399
 */
public class ControllerPersona {

    //Creamos un objeto para los metodos con la base de datos
    private PersonaJpaController modeloPersona;
    //Creamos un objeto de la vista
    private ViewPersonas vista;
    //Para la unidad de persisitencia a la base de datos
    private ManagerFactory manager;
    //Creamos un objeto de la clase modelo tabla para cargar los datos a la tabla
    private ModeloTablaPersona modelotabla;
    //Creamos un objeto del escritorio principal
    private JDesktopPane panelEscritorio;
    //Creamos un objeto de la clase persona
    private Persona persona;
    //Para escuchar los elementos seleccionados del modelo de la tabla
    private ListSelectionModel listaPersonasModel;

    public ControllerPersona(PersonaJpaController modeloPersona, ViewPersonas vista, ManagerFactory manager, JDesktopPane panelEscritorio) {
        this.modeloPersona = modeloPersona;
        this.manager = manager;
        this.panelEscritorio = panelEscritorio;
        //Inicializamos el modelo tabla
        modelotabla = new ModeloTablaPersona();
        //Asignamos datos al modelo
        modelotabla.setFilas(modeloPersona.findPersonaEntities());
        //Controlamos si la ventana interna ya existe
        if (ControllerAdministrador.vp == null) {
            ControllerAdministrador.vp = new ViewPersonas();
            this.panelEscritorio.add(vp);
            this.vista = ControllerAdministrador.vp;
            //Agregamos los datos en la tabla
            this.vista.getJtablePersonas().setModel(modelotabla);
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            ControllerAdministrador.vp.show();
            //Iniciar eventos
            iniciarControl();
        } else {
            ControllerAdministrador.vp.show();
        }
    }

    public void iniciarControl() {
        //Eventos botones
        this.vista.getBtnGuardar().addActionListener(l -> guadarPersona());
        this.vista.getBtnEditar().addActionListener(l -> editarPersona());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarPersona());
        this.vista.getBtnLimpiar().addActionListener(l -> limpiar());
        this.vista.getBtnLimpCriterio().addActionListener(l -> limpiarBuscador());
        this.vista.getBtnBuscar().addActionListener(l -> buscarPersona());
        this.vista.getjCheckMostrar().addActionListener(l -> buscarPersona());
        //Eventos tabla
        this.vista.getJtablePersonas().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPersonasModel = this.vista.getJtablePersonas().getSelectionModel();
        listaPersonasModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    personaSeleccionada();
                }
            }

        });
        //Control Botones de incio
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
    }
//Metodo para cargar datos a la vista cuando se selecciona un elemento de la tabla

    public void personaSeleccionada() {
        if (this.vista.getJtablePersonas().getSelectedRow() != -1) {
            persona = modelotabla.getFilas().get(this.vista.getJtablePersonas().getSelectedRow());
            this.vista.getTxtNombre().setText(persona.getNombre());
            this.vista.getTxtApellido().setText(persona.getApellido());
            this.vista.getTxtCedula().setText(persona.getCedula());
            this.vista.getTxtCelular().setText(persona.getCelular());
            this.vista.getTxtCorreo().setText(persona.getCorreo());
            this.vista.getTxtDireccion().setText(persona.getDireccion());
            //control de botones
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnGuardar().setEnabled(false);
        }

    }

    public void limpiarBuscador() {
        this.vista.getTxtCriterio().setText("");
        modelotabla.setFilas(modeloPersona.findPersonaEntities());
        modelotabla.fireTableDataChanged();
    }

    public void limpiar() {
        this.vista.getTxtNombre().setText("");
        this.vista.getTxtApellido().setText("");
        this.vista.getTxtCedula().setText("");
        this.vista.getTxtCelular().setText("");
        this.vista.getTxtCorreo().setText("");
        this.vista.getTxtDireccion().setText("");
        persona = null;
        //control de botones
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnGuardar().setEnabled(true);
        this.vista.getJtablePersonas().getSelectionModel().clearSelection();
    }

    public void buscarPersona() {
        if (this.vista.getjCheckMostrar().isSelected()) {
            modelotabla.setFilas(modeloPersona.findPersonaEntities());
            modelotabla.fireTableDataChanged();
        } else {
            if (!this.vista.getTxtCriterio().getText().equals("")) {
                modelotabla.setFilas(modeloPersona.buscarPersona(this.vista.getTxtCriterio().getText()));
                modelotabla.fireTableDataChanged();
            } else {
                modelotabla.setFilas(modeloPersona.findPersonaEntities());
                modelotabla.fireTableDataChanged();
            }
        }
    }

    public void guadarPersona() {
        persona = new Persona();
        persona.setNombre(this.vista.getTxtNombre().getText());
        persona.setApellido(this.vista.getTxtApellido().getText());
        persona.setCedula(this.vista.getTxtCedula().getText());
        persona.setCelular(this.vista.getTxtCelular().getText());
        persona.setCorreo(this.vista.getTxtCorreo().getText());
        persona.setDireccion(this.vista.getTxtDireccion().getText());
        modeloPersona.create(persona);
        modelotabla.agregar(persona);
        Resouces.success("Atención!!", "Persona creada correctamente");
        limpiar();
    }

    public void editarPersona() {
        if (persona != null) {
            persona.setNombre(this.vista.getTxtNombre().getText());
            persona.setApellido(this.vista.getTxtApellido().getText());
            persona.setCedula(this.vista.getTxtCedula().getText());
            persona.setCelular(this.vista.getTxtCelular().getText());
            persona.setCorreo(this.vista.getTxtCorreo().getText());
            persona.setDireccion(this.vista.getTxtDireccion().getText());
            try {
                modeloPersona.edit(persona);
                modelotabla.eliminar(persona);
                modelotabla.actualizar(persona);
            } catch (Exception ex) {
                Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
            Resouces.success("Atención!!", "Persona editada correctamente");
            limpiar();
        }
    }

    public void eliminarPersona() {
        if (persona != null) {
            try {
                modeloPersona.destroy(persona.getIdpersona());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
            modelotabla.eliminar(persona);
            Resouces.success("Atención!!", "Persona eliminada correctamente");
            limpiar();

        }
    }
}
