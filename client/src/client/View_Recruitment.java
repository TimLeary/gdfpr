package client;


import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import pkginterface.DepartmentWorker;
import pkginterface.Job;
import pkginterface.NewEmployee;
import pkginterface.Department;

public class View_Recruitment extends JDialog implements ActionListener, DocumentListener, FocusListener
{
      
    private JTextField taEmpFirstName = new JTextField(14);
    private JTextField taEmpLastName = new JTextField(15);
    private JTextField taEmpEmail = new JTextField(10);
    private JFormattedTextField taEmpPhone = new JFormattedTextField();
    
    private JLabel lbNewEmpMaxSalary = new JLabel("Max: ------ ");
    
    private JButton btOk = new JButton("Ok");
    private JButton btCancel = new JButton("Mégse");
    
    private JSpinner spnNewEmpSalary = new JSpinner(new SpinnerNumberModel(0, null, null, 0));
    
    
    private JComboBox jcDepList = new JComboBox();
    private JComboBox jcJobList = new JComboBox();
    
    private Department depChosen;
    private NewEmployee newEmployee = new NewEmployee();

    public View_Recruitment(JFrame fParent, String DepSelected) {
        super(fParent, "Új belépő felvétele", true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            abortRecruit();}
        });
        setSize(880, 310);
        setMinimumSize(new Dimension(880, 332));
        setLocationRelativeTo(this);
        setLayout(new BorderLayout(20, 20));
        if(jcDepList.getItemCount()==0)
          for (Object deps: Model.loadListDepts()) {
            if(!((Department)deps).getJobList().isEmpty())
              jcDepList.addItem(deps);
          }

        jcDepList.addActionListener(this);
        jcJobList.setPrototypeDisplayValue("Sooooooooooooooooo long job title");
        jcJobList.addActionListener(this);
        SetSalaryModel(spnNewEmpSalary, 0, 10);
        spnNewEmpSalary.setPreferredSize(new Dimension(100, 20));
        spnNewEmpSalary.setEnabled(false);
        btOk.addActionListener(this);
        btCancel.addActionListener(this);
        taEmpFirstName.getDocument().addDocumentListener(this);
        taEmpLastName.getDocument().addDocumentListener(this);
        taEmpEmail.getDocument().addDocumentListener(this);
        taEmpPhone.getDocument().addDocumentListener(this);
        
//felső panel - feliratok
        JPanel pnFormLabel=new JPanel();
        pnFormLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        pnFormLabel.setLayout(new GridLayout(1, 3, 20, 20));
        pnFormLabel.add(new JLabel("Dolgozó neve, elérhetősége:", SwingConstants.CENTER));
        pnFormLabel.add(new JLabel("Dolgozó munkaköre:", SwingConstants.CENTER));
        pnFormLabel.add(new JLabel("Dolgozó fizetése:", SwingConstants.CENTER));
        add(pnFormLabel, BorderLayout.PAGE_START);
        
//bal panel - dolgozó neve elérhetősége
        JPanel pnLeft=new JPanel();
        pnLeft.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JPanel pnSubLeft1=new JPanel();
        JPanel pnSubLeft2=new JPanel();
        pnLeft.setLayout(new BorderLayout());
        pnSubLeft1.setLayout(new GridLayout(4, 1, 20, 20));
        pnSubLeft2.setLayout(new GridLayout(4, 1, 20, 20));
        pnSubLeft1.add(new JLabel("Keresztnév: "));
        pnSubLeft2.add(taEmpFirstName);
        pnSubLeft1.add(new JLabel("Vezetéknév: "));
        pnSubLeft2.add(taEmpLastName);
        pnSubLeft1.add(new JLabel("E-mail cím: "));
        JPanel pnEmailPanel = new JPanel();
        pnEmailPanel.setLayout(new BorderLayout());
        taEmpEmail.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        pnEmailPanel.add(taEmpEmail, BorderLayout.LINE_START);
        pnEmailPanel.add(new JLabel("@corporatemail.net"), BorderLayout.LINE_END);
        pnSubLeft2.add(pnEmailPanel);
        pnSubLeft1.add(new JLabel("Telefonszám: "));
        taEmpPhone.setColumns(8);
        //taEmpPhone.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                try {
                  taEmpPhone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.####.####")));
                  } catch (java.text.ParseException ex) {
                  ex.printStackTrace();
                }
        JPanel pnPhonePanel= new JPanel();
        pnPhonePanel.setLayout(new BorderLayout());
        pnPhonePanel.add(taEmpPhone, BorderLayout.LINE_END);
        pnSubLeft2.add(pnPhonePanel);
        pnLeft.add(pnSubLeft1, BorderLayout.LINE_START);
        pnLeft.add(pnSubLeft2, BorderLayout.LINE_END);
        
//középső panel - részleg, munkakör
        JPanel pnCenter=new JPanel();
        pnCenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        JPanel pnSubCenter=new JPanel();
        pnCenter.setLayout(new BorderLayout(20, 20));
        pnSubCenter.setLayout(new GridLayout(4, 2, 0, 20));
        pnSubCenter.add(new JLabel("Részleg neve: ", SwingConstants.RIGHT));
        pnSubCenter.add(jcDepList);
        pnSubCenter.add(new JLabel(""));
        pnSubCenter.add(new JLabel(""));
        pnSubCenter.add(new JLabel("Munkakör: ", SwingConstants.RIGHT));
        pnSubCenter.add(jcJobList);
        pnSubCenter.add(new JLabel(""));
        pnSubCenter.add(new JLabel(""));

//jobb panel - fizetés
        JPanel pnRight=new JPanel();
        JPanel pnSubRight1=new JPanel();
        JPanel pnSubRight2=new JPanel();
        pnRight.setLayout(new BorderLayout());
        pnSubRight1.setLayout(new GridLayout(4, 1, 20, 20));
        pnSubRight2.setLayout(new GridLayout(4, 1, 20, 20));
        pnSubRight1.add(lbNewEmpMaxSalary, BorderLayout.CENTER);
        pnSubRight2.add(spnNewEmpSalary, BorderLayout.CENTER);
        pnSubRight1.add(new JLabel(""), BorderLayout.CENTER);
        pnSubRight2.add(new JLabel(""), BorderLayout.CENTER);
        pnSubRight1.add(new JLabel(""), BorderLayout.CENTER);
        pnSubRight2.add(new JLabel(""), BorderLayout.CENTER);
        pnSubRight1.add(new JLabel(""), BorderLayout.CENTER);
        pnSubRight2.add(new JLabel(""), BorderLayout.CENTER);
        pnRight.add(pnSubRight1, BorderLayout.LINE_START);
        pnRight.add(pnSubRight2, BorderLayout.LINE_END);
        
        add(pnLeft, BorderLayout.LINE_START);
        add(pnCenter, BorderLayout.LINE_END);
        pnCenter.add(pnRight, BorderLayout.LINE_END);
        pnCenter.add(pnSubCenter, BorderLayout.LINE_START);
        
//Alsó panel - gombok
        JPanel pnButtons=new JPanel();
        pnButtons.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
        pnButtons.setLayout(new GridLayout(1, 4, 20, 20));
        pnButtons.add(new JLabel(""));
        pnButtons.add(new JLabel(""));
        pnButtons.add(btOk);
        pnButtons.add(btCancel);
        add(pnButtons, BorderLayout.PAGE_END);

      if (DepSelected ==null)
        jcDepList.setSelectedItem(null);
      else
      {
        int i = 0;

        while(!jcDepList.getItemAt(i).toString().equals(DepSelected) && i<jcDepList.getItemCount())
          i++;

        jcDepList.setSelectedIndex(i);
      }

      btOk.getRootPane().setDefaultButton(btOk);

      setVisible(true);
    }

    public void SetSalaryModel(JSpinner spinner, int defaultSalary, int increment) {
        spinner.setModel(new SpinnerNumberModel(defaultSalary, 0,defaultSalary*2, increment));
        JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();

      txt.addFocusListener(this);

      txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { //hogy ne lehessen betűt begépelni
              if (e.getKeyChar()==KeyEvent.VK_ENTER)
                btCancel.requestFocus();
              if (e.getKeyChar()<'0' || '9'<e.getKeyChar())
                e.consume();
                }});
    }
    
    public void abortRecruit(){
      int answer=JOptionPane.showConfirmDialog(this, "Félbehagyja felvételt?", "Kilépés megerősítése", JOptionPane.YES_NO_OPTION);
      if (answer==JOptionPane.YES_OPTION){
        this.dispose();
      }
    }
    
    public void fillEmployeeData(){
        newEmployee.setFirstName(taEmpFirstName.getText());
        newEmployee.setLastName(taEmpLastName.getText());
        newEmployee.setEmail(taEmpEmail.getText());
        newEmployee.setPhoneNumber(taEmpPhone.getText());
        newEmployee.setDepId(depChosen.getId());
        newEmployee.setJobId((((Job)jcJobList.getSelectedItem()).getId()));
        newEmployee.setManagerId(depChosen.getManagerId());
        newEmployee.setSalary((double)((int)spnNewEmpSalary.getValue()));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource()==jcDepList && jcDepList.getSelectedItem()!=null){
        depChosen=(Department) jcDepList.getSelectedItem();
        jcJobList.removeAllItems();
        if(jcJobList.getItemCount()==0)
          for (Job jobs : depChosen.getJobList()) {
            jcJobList.addItem(jobs);
          }
        btOk.setEnabled(empDataIsFilled());
      }
      if (e.getSource()==jcJobList) {
        newEmployee.setMaxSalary();
        lbNewEmpMaxSalary.setText("Max: " + newEmployee.getMaxSalary());

        SetSalaryModel(spnNewEmpSalary, newEmployee.getMaxSalary()/2, 10);

        spnNewEmpSalary.setEnabled(true);

        btOk.setEnabled(empDataIsFilled());
      }
      if (e.getSource()==btCancel){
        abortRecruit();
      }
      if (e.getSource()==btOk) {
        try {
                spnNewEmpSalary.commitEdit();
        } catch (java.text.ParseException /*| NumberFormatException */ exc) {
            JOptionPane.showMessageDialog(this, "Érvénytelen fizetés.", "Hiba", JOptionPane.ERROR_MESSAGE);
        }
          fillEmployeeData();
          newEmployee.setEmpId();

          // refresh tree
          ((View)this.getParent()).treeDepEmpName.setModel(new Model().treeModel(new DepartmentWorker(newEmployee.getEmpId(), depChosen.getName(), newEmployee.getFirstName() + " " + newEmployee.getLastName())));

          // open the chosen dept in tree
          int i = 0;
          String s = ((DefaultMutableTreeNode)(((View)this.getParent()).treeDepEmpName.getModel().getRoot())).getChildAt(i).toString();

          while(!depChosen.getName().equals(s))
          {
            i++;
            s=((DefaultMutableTreeNode)(((View)this.getParent()).treeDepEmpName.getModel().getRoot())).getChildAt(i).toString();
          }

          ((View)this.getParent()).treeDepEmpName.expandRow(i);

          // save to SQL table
        if (newEmployee.save())
        {
          JOptionPane.showMessageDialog(this, "A dolgozó adatait rögzítettük.", "Sikeres felvétel", JOptionPane.INFORMATION_MESSAGE);

          // select the new employee in tree
          ((View)this.getParent()).treeDepEmpName.setSelectionRow(i+1);
          while(!(newEmployee.getFirstName() + " " + newEmployee.getLastName()).equals(((View)this.getParent()).treeDepEmpName.getLastSelectedPathComponent().toString()))
          {
            i++;
            ((View)this.getParent()).treeDepEmpName.setSelectionRow(i);
          }

          this.dispose();
        }
        else
          JOptionPane.showMessageDialog(this, "A dolgozó adatait nem rögzítettük.", "Sikertelen felvétel", JOptionPane.INFORMATION_MESSAGE);
      }
    }

  @Override
  public void insertUpdate(DocumentEvent e) {
    btOk.setEnabled(empDataIsFilled());
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    btOk.setEnabled(empDataIsFilled());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    btOk.setEnabled(empDataIsFilled());
  }
  
  public boolean empDataIsFilled() {
    if (!(taEmpFirstName.getText().equals("")) && !(taEmpLastName.getText().equals("")) 
            && !(taEmpEmail.getText().equals("")) && !(taEmpPhone.getText().trim().length()!=13) &&
            !(jcDepList.getSelectedItem()==null) && !(jcJobList.getSelectedItem()==null)){
       return true;
     }
     else {
       return false;
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
      btOk.setEnabled(false);
      JOptionPane.showMessageDialog(this, "Érvénytelen fizetést adott meg!", "Hiba", JOptionPane.ERROR_MESSAGE);
      btOk.setEnabled(empDataIsFilled());
    }
  }
}
