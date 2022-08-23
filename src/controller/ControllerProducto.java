/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.Producto;
import model.ProductoJpaController;
import model.exceptions.NonexistentEntityException;
import proyectoproducto.ManagerFactory;
import view.interna.ViewProductos;

/**
 *
 * @author 59399
 */
public class ControllerProducto {
    //Creamos un objeto para los metodos con la base de datos

    private ProductoJpaController modeloProducto;
    //Creamos un objeto de la vista
    private ViewProductos vista;
    //Para la unidad de persisitencia a la base de datos
    private ManagerFactory manager;
    //Creamos un objeto de la clase modelo tabla para cargar los datos a la tabla
    private ModeloTablaProductos modelotabla;
    //Creamos un objeto del escritorio principal
    private JDesktopPane panelEscritorio;
    //Creamos un objeto de la clase producto
    private Producto producto;
    //Para escuchar los elementos seleccionados del modelo de la tabla
    private ListSelectionModel listaProductosModel;

    public ControllerProducto(ProductoJpaController modeloProducto, ViewProductos vista, ManagerFactory manager, JDesktopPane panelEscritorio) {
        this.modeloProducto = modeloProducto;
        this.manager = manager;
        this.panelEscritorio = panelEscritorio;
        //Inicializamos el modelo tabla
        modelotabla = new ModeloTablaProductos();
        //Asignamos datos al modelo
        modelotabla.setFilas(modeloProducto.findProductoEntities());
        //Controlamos si la ventana interna ya existe
        if (ControllerAdministrador.vpr == null) {
            ControllerAdministrador.vpr = new ViewProductos();
            this.panelEscritorio.add(ControllerAdministrador.vpr);
            this.vista = ControllerAdministrador.vpr;
            //Agregamos los datos en la tabla
            this.vista.getJtableProductos().setModel(modelotabla);
            //Para centar la vista en la ventana
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vista.getSize();
            this.vista.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            ControllerAdministrador.vpr.show();
            //Iniciar eventos
            iniciarControl();
        } else {
            ControllerAdministrador.vpr.show();
        }
    }

    public void iniciarControl() {
        //Eventos botones
        this.vista.getBtnGuardar().addActionListener(l -> guadarProducto());
        this.vista.getBtnEditar().addActionListener(l -> editarProducto());
        this.vista.getBtnEliminar().addActionListener(l -> eliminarProducto());
        this.vista.getBtnLimpiar().addActionListener(l -> limpiar());
        this.vista.getBtnLimpCriterio().addActionListener(l -> limpiarBuscador());
        this.vista.getBtnBuscar().addActionListener(l -> buscarProducto());
        this.vista.getjCheckMostrar().addActionListener(l -> buscarProducto());
        //Eventos tabla
        this.vista.getJtableProductos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProductosModel = this.vista.getJtableProductos().getSelectionModel();
        listaProductosModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    productoSelecionado();
                }
            }

        });
        //Control Botones de incio
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
    }
//Metodo para cargar datos a la vista cuando se selecciona un elemento de la tabla

    public void productoSelecionado() {
        if (this.vista.getJtableProductos().getSelectedRow() != -1) {
            producto = modelotabla.getFilas().get(this.vista.getJtableProductos().getSelectedRow());
            this.vista.getTxtNombre().setText(producto.getNombre());
            this.vista.getTxtPrecio().setText(producto.getPrecio().toString());
            this.vista.getTxtCantidad().setValue(producto.getCantidad());
            this.vista.getBtnEditar().setEnabled(true);
            this.vista.getBtnEliminar().setEnabled(true);
            this.vista.getBtnGuardar().setEnabled(false);
        }

    }

    public void limpiarBuscador() {
        this.vista.getTxtCriterio().setText("");
        modelotabla.setFilas(modeloProducto.findProductoEntities());
        modelotabla.fireTableDataChanged();
    }

    public void limpiar() {
        this.vista.getTxtNombre().setText("");
        this.vista.getTxtPrecio().setText("");
        this.vista.getTxtCantidad().setValue(0);
        producto = null;
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnGuardar().setEnabled(true);
        this.vista.getJtableProductos().getSelectionModel().clearSelection();
    }

    public void buscarProducto() {
        if (this.vista.getjCheckMostrar().isSelected()) {
            modelotabla.setFilas(modeloProducto.findProductoEntities());
            modelotabla.fireTableDataChanged();
        } else {
            if (!this.vista.getTxtCriterio().getText().equals("")) {
                modelotabla.setFilas(modeloProducto.buscarProducto(this.vista.getTxtCriterio().getText()));
                modelotabla.fireTableDataChanged();
            } else {
                modelotabla.setFilas(modeloProducto.findProductoEntities());
                modelotabla.fireTableDataChanged();
            }
        }
    }

    public void guadarProducto() {
        producto = new Producto();
        producto.setNombre(this.vista.getTxtNombre().getText());
        producto.setPrecio(Double.valueOf(this.vista.getTxtPrecio().getText()));
        producto.setCantidad((Integer) this.vista.getTxtCantidad().getValue());
        modeloProducto.create(producto);
        modelotabla.agregar(producto);
        Resouces.success("Atención!!", "Producto creada correctamente");
        limpiar();
    }

    public void editarProducto() {
        if (producto != null) {
            producto.setNombre(this.vista.getTxtNombre().getText());
            producto.setPrecio(Double.valueOf(this.vista.getTxtPrecio().getText()));
            producto.setCantidad((Integer) this.vista.getTxtCantidad().getValue());
            try {
                modeloProducto.edit(producto);
            } catch (Exception ex) {
                Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
            modelotabla.eliminar(producto);
            modelotabla.actualizar(producto);
            Resouces.success("Atención!!", "Producto editada correctamente");
            limpiar();
        }
    }

    public void eliminarProducto() {
        if (producto != null) {
            try {
                modeloProducto.destroy(producto.getIdproducto());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerProducto.class.getName()).log(Level.SEVERE, null, ex);
            }

            modelotabla.eliminar(producto);
            Resouces.success("Atención!!", "Producto eliminada correctamente");
            limpiar();

        }
    }

}
