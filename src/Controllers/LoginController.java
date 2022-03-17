package Controllers;

import Models.Employee;
import Repositories.EmployeeRepository;
import Views.frmLogin;
import Views.frmPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginController implements ActionListener {

    private EmployeeRepository repository;
    private frmLogin view;

    public LoginController(EmployeeRepository repository, frmLogin view) {
        this.repository = repository;
        this.view = view;
        this.view.btnIngresar.addActionListener(this);
        this.view.btnCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == this.view.btnIngresar) {
            String usuario = view.txtUsuario.getText();
            String passwd = view.txtPasswd.getText();
            if (!usuario.equals("") && !passwd.equals("")) {
                try {
                    Employee emp = repository.Login(usuario, passwd);
                    if (emp != null) {
                        new frmPrincipal().setVisible(true);
                        view.dispose();
                    } else {
                        System.out.println("El usuario no existe");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Complete todos los campos", "Validación", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (source == this.view.btnCancelar) {
            this.view.dispose();
        }
    }

}
