package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeModel;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import pkginterface.AuthInterface;
import pkginterface.User;
import pkginterface.DepartmentWorker;
import pkginterface.ModifySalaryEmployee;


public class View extends JFrame implements ActionListener, TreeSelectionListener, MouseListener, FocusListener{
    private JPanel pnLogin = new JPanel();
    private JPanel pnSalaryIncrease = new JPanel();
    private JLabel lbUsername = new JLabel("Felhasználónév: ");
    private JTextField tfUsername = new JTextField(15);
    private JPasswordField pfPassword = new JPasswordField(15);
    private JLabel lbPassword = new JLabel("Jelszó: ");
    private JButton btLogin = new JButton("Bejelentkezés");
    private User user;

    protected JTree treeDepEmpName=new JTree();
    private DepartmentWorker actworker = null;
    private JLabel lbempInfo= new JLabel("<html><br><br>Dolgozó neve: " +"<br>Részleg neve: "+"<br><br>Dolgozó jelenlegi fizetése: "+"<br></html>");
    private JLabel lbIncreaseMax= new JLabel("<html>Fizetés módosítása <br>erre az összegre:<br>(Min: -- , Max: --)</html>");
    private JButton btChangeSalary=new JButton("<html><br>Fizetés módosítása<br><br></html>");
    private JSpinner spnIncreaseRate=new JSpinner(new SpinnerNumberModel(0, 0, 0, 0));
    private final int spinnerIncrement = 10;
    private JMenuItem miAdd=new JMenuItem("Új alkalmazott felvétele", KeyEvent.VK_A);
    private JMenuItem miAdd_popup=new JMenuItem("Új alkalmazott felvétele", KeyEvent.VK_A);
    private JMenuItem miLogOff=new JMenuItem("Kijelentkezés", KeyEvent.VK_K);

    public View() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 320);
        setLayout(new BorderLayout());
        setLocationRelativeTo(this);
        buildLogin();
        setVisible(true);
    }
    
    
    
    private void buildLogin() {
        setTitle("Bejelentkezés");
        Dimension d = new Dimension(520, 320);
        pnLogin.setSize(d);
        pnLogin.setMaximumSize(d);
        pnLogin.setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        tfUsername.setSize(50, 24);
        pfPassword.setSize(50, 24);
        
        btLogin.addActionListener(this);
        
        gbc.gridx=0;
        gbc.gridy=0;
        pnLogin.add(lbUsername, gbc);
        gbc.gridx=1;
        pnLogin.add(tfUsername, gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        pnLogin.add(lbPassword, gbc);
        gbc.gridx=1;
        pnLogin.add(pfPassword, gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridwidth=2;
        pnLogin.add(btLogin, gbc);
        
        this.add(pnLogin);
        btLogin.getRootPane().setDefaultButton(btLogin);

        this.revalidate();
    }
    
    private void buildSurface() {
        setTitle("HR nyilvántartás");
        pnSalaryIncrease.setSize(500, 320);

        this.remove(pnLogin);
        btLogin.removeActionListener(this);

        JMenuBar menuBar = new JMenuBar();
        JMenu mAcc = new JMenu("Fiók");
        mAcc.setMnemonic(KeyEvent.VK_F);
        JMenu mEdit = new JMenu("Szerkesztés");
        mEdit.setMnemonic(KeyEvent.VK_E);

        miLogOff.addActionListener(this);
        mAcc.add(miLogOff);
        menuBar.add(mAcc);

        if(user.getAcl().equals("manager")) {
            miAdd.addActionListener(this);

            mEdit.add(miAdd);
            menuBar.add(mEdit);
        }
        setJMenuBar(menuBar);

        if (!(pnSalaryIncrease.isMinimumSizeSet())) {
          pnSalaryIncrease.setMinimumSize(new Dimension(500, 320));
          pnSalaryIncrease.setLayout(new BorderLayout());
          buildTree();
          JPanel pnSalary = buildPnSalaryInfo();
          pnSalaryIncrease.add(pnSalary, BorderLayout.EAST);
        }
        this.add(pnSalaryIncrease);
        btChangeSalary.addActionListener(this);
        btChangeSalary.getRootPane().setDefaultButton(btChangeSalary);


        this.revalidate();
    }
    
     private JPanel buildPnSalaryInfo() { //a jobb oldali panel lenne
        JPanel pnSal = new JPanel();
        pnSal.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        pnSal.setPreferredSize(new Dimension(300, HEIGHT));
        pnSal.setLayout(new BorderLayout(20, 20));
        pnSal.add(lbempInfo, BorderLayout.NORTH);
        JPanel kisSouthGrid = new JPanel(new BorderLayout(0, 20));
        kisSouthGrid.add(lbIncreaseMax, BorderLayout.WEST);
        spnIncreaseRate.setFont(new Font("Tahoma", 1, 14));
        spnIncreaseRate.setPreferredSize(new Dimension(100, HEIGHT));
        spnIncreaseRate.setEnabled(false);
        kisSouthGrid.add(spnIncreaseRate, BorderLayout.EAST);
        btChangeSalary.setEnabled(false);
        kisSouthGrid.add(btChangeSalary, BorderLayout.SOUTH);
        pnSal.add(kisSouthGrid, BorderLayout.SOUTH);
        return pnSal;
    }

    private void buildTree(){
        DefaultTreeModel treeModel = Model.getEmployeeTreeModel();
        treeDepEmpName.setModel(treeModel);
        treeDepEmpName.addTreeSelectionListener(this);
        treeDepEmpName.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeDepEmpName.addMouseListener(this);
        treeDepEmpName.setRootVisible(false);
        treeDepEmpName.setExpandsSelectedPaths(true);
        JScrollPane spTree=new JScrollPane(treeDepEmpName);
        pnSalaryIncrease.add(spTree, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btLogin) {
            System.out.println("Login");
            String username = tfUsername.getText();
            String password = String.copyValueOf(pfPassword.getPassword());
            
            try {
                AuthInterface auth = (AuthInterface) Naming.lookup("rmi://localhost:1099/auth");
                user = auth.login(username, password);
                if(user != null) {
                    this.buildSurface();
                }

            } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                ex.printStackTrace();
            }
        }
      if (e.getSource()==miLogOff){
        this.setJMenuBar(null);
        pfPassword.setText("");
        this.remove(pnSalaryIncrease);
        btChangeSalary.removeActionListener(this);
        miLogOff.removeActionListener(this);
        if(user.getAcl().equals("manager"))
          miAdd.removeActionListener(this);

        this.buildLogin();
      }
      
      if(e.getSource() == btChangeSalary) {
        salaryIncreaseAction();
      }
      
      if (e.getSource() == miAdd || e.getSource() == miAdd_popup){
        miAdd_popup.removeActionListener(this);

          if (treeDepEmpName.getLastSelectedPathComponent() != null)
          {
            if (((DefaultMutableTreeNode)treeDepEmpName.getLastSelectedPathComponent()).isLeaf()){
              // employee is chosen
                new View_Recruitment(this, ((DepartmentWorker)((DefaultMutableTreeNode) treeDepEmpName.getLastSelectedPathComponent()).getUserObject()).getDepartmentName());
            } else // dept is chosen
              new View_Recruitment(this, ((DefaultMutableTreeNode)treeDepEmpName.getLastSelectedPathComponent()).toString());
          }
          else // nothing is chosen
            new View_Recruitment(this, null);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        
        if (selectedNode == null || !selectedNode.isLeaf()){
            lbempInfo.setText("<html><br><br>Dolgozó neve: "+"<br>Részleg neve: "+"<br><br>Dolgozó jelenlegi fizetése: "+"<br></html>");
            lbIncreaseMax.setText("<html>Fizetés módosítása <br>erre az összegre:<br>(Min: -- , Max: --)</html>");
            btChangeSalary.setEnabled(false);
            spnIncreaseRate.setModel(new SpinnerNumberModel(0, 0, 0, 0));
            spnIncreaseRate.setEnabled(false);
            return;
        }
        
        Object nodeInfo = selectedNode.getUserObject();
        if (nodeInfo instanceof DepartmentWorker){
            actworker = (DepartmentWorker) nodeInfo;
            ModifySalaryEmployee salaryModel = new ModifySalaryEmployee(actworker);
            int max = (int) Math.min(salaryModel.getSalary() * 1.05, Math.max(salaryModel.getSalary(), salaryModel.getMaxSalary()));

            lbempInfo.setText("<html><br><br>Dolgozó neve: "+
                    actworker.getWorkerName()+"<br>Részleg neve: "+
                    actworker.getDepartmentName()+"<br><br>Dolgozó jelenlegi fizetése: "+
                    (int)salaryModel.getSalary()+"<br></html>");
          lbIncreaseMax.setText("<html>Fizetés módosítása <br>erre az összegre:<br>(Min: "+
                    +(int)salaryModel.getMinSalary() + ", Max: "+
                  max + ")</html>");
            spinnerSet((int)salaryModel.getSalary(), (int)salaryModel.getMinSalary(), max, spinnerIncrement);
            btChangeSalary.setEnabled(true);
            spnIncreaseRate.setEnabled(true);
        }
    }

      public void spinnerSet(int actSalary, int minSalary, int maxSalary , int increment){
        spnIncreaseRate.setModel(new SpinnerNumberModel(actSalary, minSalary, maxSalary, increment));
        JFormattedTextField txt = ((JSpinner.NumberEditor) spnIncreaseRate.getEditor()).getTextField();

        txt.addFocusListener(this);

        txt.addKeyListener(new KeyAdapter() {
              @Override
              public void keyTyped (KeyEvent e){ //hogy ne lehessen betűt begépelni
                if (e.getKeyChar()==KeyEvent.VK_ENTER)
                  btChangeSalary.requestFocus();
                if (e.getKeyChar()<'0' || '9'<e.getKeyChar())
                  e.consume();
              }
        });
      }

  private void salaryIncreaseAction() {
        try {
            spnIncreaseRate.commitEdit();
        } catch (java.text.ParseException /*| NumberFormatException */ exc) {
            JOptionPane.showMessageDialog(this, "Érvénytelen fizetés.", "Hiba", JOptionPane.ERROR_MESSAGE);
        }

        ModifySalaryEmployee salaryModel = new ModifySalaryEmployee(actworker);
        int newSalary = (Integer) spnIncreaseRate.getValue();
        
        
        System.out.println(newSalary);

        if (newSalary == salaryModel.getSalary()) {
            JOptionPane.showMessageDialog(this, "A jelenlegi fizetést adta meg. Nincs módosítás", "Jelenlegi fizetés", JOptionPane.WARNING_MESSAGE);
        } else {
          salaryModel.updateSalary(newSalary);
          lbempInfo.setText("<html><br><br>Dolgozó neve: "
                  + actworker.getWorkerName() + "<br>Részleg neve: "
                  + actworker.getDepartmentName() + "<br><br>Dolgozó jelenlegi fizetése: "
                  + (int)salaryModel.getSalary() + "<br></html>");

          //Ha pl elég közel van a fizu a menedzserhez
          int max = (int) Math.min(salaryModel.getSalary() * 1.05, Math.max(salaryModel.getSalary(), salaryModel.getMaxSalary()));

          lbIncreaseMax.setText("<html>Fizetés módosítása <br>erre az összegre:<br>(Min: "
                  + (int) salaryModel.getMinSalary() + ", Max: "
                  + max + ")</html>");

          spinnerSet((int)salaryModel.getSalary(), (int)salaryModel.getMinSalary(), max, spinnerIncrement);
        }
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    if (SwingUtilities.isRightMouseButton(e)) {
      if (user.getAcl().equals("manager")) {
        int row = treeDepEmpName.getClosestRowForLocation(e.getX(), e.getY());
        treeDepEmpName.setSelectionRow(row);

        JPopupMenu popup = new JPopupMenu();
        miAdd_popup.addActionListener(this);
        popup.add(miAdd_popup);
        popup.show(e.getComponent(), e.getX(), e.getY());
      } else {
        e.consume();
      }
    }
  }

  @Override
  public void focusGained(FocusEvent e)
  {
  }

  @Override
  public void focusLost(FocusEvent e)
  {
    if(!((JFormattedTextField)e.getSource()).isEditValid())
    {
      btChangeSalary.setEnabled(false);
      JOptionPane.showMessageDialog(this, "Érvénytelen fizetést adott meg!", "Hiba", JOptionPane.ERROR_MESSAGE);
      btChangeSalary.setEnabled(true);
    }
  }

  @Override
  public void mousePressed(MouseEvent e)
  {

  }

  @Override
  public void mouseReleased(MouseEvent e)
  {

  }

  @Override
  public void mouseEntered(MouseEvent e)
  {

  }

  @Override
  public void mouseExited(MouseEvent e)
  {

  }
}
