package Controllers;

import Components.Tables;
import Models.Employee;
import Models.RoleConstants;
import Models.StatusConstants;
import Repositories.EmployeeRepository;
import Views.frmPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

public class EmployeeController extends MouseAdapter implements ActionListener {

    EmployeeRepository repotitory;
    frmPrincipal view;
    DefaultTableModel tableModel;
    String currentEmployeeId = "";
    int currentIndex = -1;

    public EmployeeController(EmployeeRepository repotitory, frmPrincipal view) {
        this.repotitory = repotitory;
        this.view = view;
        this.view.btnRegistrarEmpleado.addActionListener(this);
        this.view.btnCancelarEmpleado.addActionListener(this);
        this.view.btnModificadoEmpleado.addActionListener(this);
        this.view.jtEmpleados.addMouseListener(this);
        ListEmployees();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnRegistrarEmpleado) {
            String name = view.txtNombreEmpelado.getText();
            String username = view.txtUsuarioEmpleado.getText();
            String passwd = view.txtPasswordEmpleado.getText();
            String role = RoleConstants.ADMINISTRATOR;
            if (name.equals("") || username.equals("") || passwd.equals("")) {
                JOptionPane.showMessageDialog(view, "Complete todos los campos", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Employee emp = new Employee();
                emp.setName(name);
                emp.setUsername(username);
                emp.setPasswd(passwd);
                emp.setRole(role);
                try {
                    int result = this.repotitory.CreateEmployee(emp);
                    if (result == 1) {
                        resetForm();
                        ListEmployees();
                        JOptionPane.showMessageDialog(view, "Usuario registrado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Ocurrio un error en el registro", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == view.btnCancelarEmpleado) {
            resetForm();
        } else if (e.getSource() == view.btnModificadoEmpleado) {
            if (!currentEmployeeId.equals("") && currentIndex != -1) {
                Employee emp = new Employee();
                emp.setEmployee_id(currentEmployeeId);
                emp.setName(view.txtNombreEmpelado.getText());
                emp.setUsername(view.txtUsuarioEmpleado.getText());
                emp.setRole(RoleConstants.ADMINISTRATOR);
                try {
                    int result = this.repotitory.UpdateEmployee(emp);
                    if (result == 1) {
                        resetForm();
                        ListEmployees();
                        JOptionPane.showMessageDialog(view, "Usuario actualizado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Ocurrio un error en la actualización", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void ListEmployees() {
        try {
            Tables color = new Tables();
            view.jtEmpleados.setDefaultRenderer(view.jtEmpleados.getColumnClass(0), color);
            tableModel = (DefaultTableModel) this.view.jtEmpleados.getModel();
            List<Employee> employees = repotitory.ToList();
            tableModel.setRowCount(0);
            Object[] row = new Object[5];
            for (int i = 0; i < employees.size(); i++) {
                row[0] = employees.get(i).getEmployee_id();
                row[1] = employees.get(i).getName();
                row[2] = employees.get(i).getUsername();
                row[3] = employees.get(i).getRole();
                row[4] = employees.get(i).isStatus() ? StatusConstants.Activo : StatusConstants.Inactivo;
                tableModel.addRow(row);
            }
            this.view.jtEmpleados.setModel(tableModel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getSource() == view.jtEmpleados
                && e.getButton() == MouseEvent.BUTTON1) {
            currentIndex = this.view.jtEmpleados.rowAtPoint(e.getPoint());
            if (currentIndex != -1) {
                currentEmployeeId = view.jtEmpleados.getValueAt(currentIndex, 0).toString();
                this.view.txtNombreEmpelado.setText(view.jtEmpleados.getValueAt(currentIndex, 1).toString());
                this.view.txtUsuarioEmpleado.setText(view.jtEmpleados.getValueAt(currentIndex, 2).toString());
                this.view.txtPasswordEmpleado.setEnabled(false);
                this.view.btnRegistrarEmpleado.setEnabled(false);
                this.view.btnModificadoEmpleado.setEnabled(true);
                this.view.btnCancelarEmpleado.setEnabled(true);
            }
        }
        if (e.getSource() == view.jtEmpleados
                && !currentEmployeeId.equals("")
                && e.getButton() == MouseEvent.BUTTON3) {
            showPopup(e, e.getX(), e.getY());
        }
    }

    void showPopup(MouseEvent e, int x, int y) {
        int index = this.view.jtEmpleados.rowAtPoint(e.getPoint());
        JPopupMenu popup = new JPopupMenu();
        JMenuItem eliminarOpt = new JMenuItem("Eliminar");
        JMenuItem activarOpt = new JMenuItem("Activar");
        if (index != currentIndex) {
            eliminarOpt.setEnabled(false);
            activarOpt.setEnabled(false);
        }
        boolean currentStatus = Boolean.parseBoolean( this.view.jtEmpleados.getValueAt(index, 4).toString());
        if(currentStatus){
           activarOpt.setEnabled(false);
        }
        activarOpt.addActionListener((ActionEvent e1) -> {
            try {
                int result = repotitory.ActivateEmployee(currentEmployeeId);
                if(result == 1){
                    this.ListEmployees();
                } else {
                   JOptionPane.showMessageDialog(view, "Ocurrio un error en la eliminación", "Aviso", JOptionPane.WARNING_MESSAGE); 
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, ex.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
            }
        });
        eliminarOpt.addActionListener((ActionEvent event) -> {
            try {
                int result = repotitory.DeleteEmployee(currentEmployeeId);
                if (result == 1) {
                    tableModel = (DefaultTableModel) view.jtEmpleados.getModel();
                    tableModel.removeRow(currentIndex);
                    view.jtEmpleados.setModel(tableModel);
                    currentIndex = -1;
                    currentEmployeeId = "";
                    resetForm();
                    JOptionPane.showMessageDialog(view, "Usuario eliminado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "Ocurrio un error en la eliminación", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, ex.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
            }
        });
        popup.add(activarOpt);
        popup.add(eliminarOpt);
        popup.show(view.jtEmpleados, x, y);
    }

    public void resetForm() {
        view.txtNombreEmpelado.setText("");
        view.txtUsuarioEmpleado.setText("");
        view.txtPasswordEmpleado.setText("");
        view.txtPasswordEmpleado.setEnabled(true);
        view.btnRegistrarEmpleado.setEnabled(true);
        view.btnModificadoEmpleado.setEnabled(false);
    }

}
