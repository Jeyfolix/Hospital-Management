import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class PatientManagements extends JFrame implements ActionListener{
    private Image background;
    PatientManagements(){

        background = new ImageIcon("C:\\Users\\User\\Documents\\Downloads\\Desktop\\istockphoto-532523451-1024x1024.jpg").getImage(); // Change the path accordingly

        setSize(1540,850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new ImagePanel());

       
        JMenuBar mb=new JMenuBar();


        JMenu newInformation=new JMenu("Patient Information");
        newInformation.setForeground(Color.BLUE);
        mb.add(newInformation);

        JMenuItem demIn=new JMenuItem("Demogratic detailes");
        demIn.setBackground(Color.WHITE);
        demIn.addActionListener(this);
        newInformation.add(demIn);
        
        

        JMenuItem medicalIn=new JMenuItem("Medical History");
        medicalIn.setBackground(Color.WHITE);
        medicalIn.addActionListener(this);
        newInformation.add(medicalIn);

        JMenuItem insuranceIn=new JMenuItem("Insurance information");
        insuranceIn.setBackground(Color.WHITE);
        insuranceIn.addActionListener(this);
        newInformation.add(insuranceIn);

        JMenuItem currentIn=new JMenuItem("Current medical record");
        currentIn.setBackground(Color.WHITE);
        currentIn.addActionListener(this);
        newInformation.add(currentIn);

        JMenu appoint=new JMenu("Appointment Scheduling");
        appoint.setForeground(Color.RED);
        mb.add(appoint);

        JMenuItem book=new JMenuItem( "Appointment Booking");
        book.setBackground(Color.WHITE);
        book.addActionListener(this);
        appoint.add(book);

        JMenuItem provider=new JMenuItem("Multi_provider Scheduling");
        provider.setBackground(Color.WHITE);
        provider.addActionListener(this);
        appoint.add(provider);

        JMenuItem calender=new JMenuItem("Appointment Calender");
        calender.setBackground(Color.WHITE);
        calender.addActionListener(this);
        appoint.add(calender);

        JMenu bill=new JMenu("Billing and Finance");
        bill.setForeground(Color.BLUE);
        mb.add(bill);

        JMenuItem claime=new JMenuItem( "Insurance claime");
        claime.setBackground(Color.WHITE);
        claime.addActionListener(this);
        bill.add(claime);

        JMenuItem pbilling=new JMenuItem("Patient Billing");
        pbilling.setBackground(Color.WHITE);
        pbilling.addActionListener(this);
        bill.add(pbilling);

        JMenuItem report=new JMenuItem("Financial Report");
        report.setBackground(Color.WHITE);
        report.addActionListener(this);
        bill.add(report);

        JMenu preport=new JMenu("Reporting and Analytics");
        preport.setForeground(Color.RED);
        mb.add(preport);

        JMenuItem clinic=new JMenuItem( "Clinical Report");
        clinic.setBackground(Color.WHITE);
        clinic.addActionListener(this);
        preport.add(clinic);

        JMenuItem operate=new JMenuItem("Operation Report");
        operate.setBackground(Color.WHITE);
        operate.addActionListener(this);
        preport.add(operate);

        JMenu update=new JMenu("Update Detailes");
        update.setForeground(Color.BLUE);
        mb.add(update);

        JMenuItem pupdate=new JMenuItem( "Update Patient Information");
        pupdate.setBackground(Color.WHITE);
        pupdate.addActionListener(this);
        update.add(pupdate);
        
        JMenu utility=new JMenu("Utility");
        utility.setForeground(Color.RED);

        mb.add(utility);

        JMenuItem notpad=new JMenuItem( "Notepad");
        notpad.setBackground(Color.WHITE);
        notpad.addActionListener(this);
        utility.add(notpad);

        JMenuItem calc=new JMenuItem("Calculator");
        calc.setBackground(Color.WHITE);
        calc.addActionListener(this);
        utility.add(calc);


        JMenu exit=new JMenu("Exit");
        exit.setForeground(Color.RED);
        mb.add(exit);

        JMenuItem ex=new JMenuItem( "Exit");
        ex.setBackground(Color.WHITE);
        ex.addActionListener(this);
        exit.add(ex);

        setJMenuBar(mb);
        setVisible(true);

    }
    private class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }



    public void actionPerformed(ActionEvent ae){
        String msg=ae.getActionCommand();
        if(msg.equals("Exit")){
            setVisible(false);
        }
        else if(msg.equals("Calculator")){
            
        }else if(msg.equals("Notepad")){
            
                }else if(msg.equals("Demogratic detailes")){
                    setVisible(true);
                    new PatientDetailes();
                    

                }else if(msg.equals("")){
                    

                }else if(msg.equals("")){
                    

                }else if(msg.equals("")){
                    

                
            }else if(msg.equals("")){
                

            }else if(msg.equals("")){
                

            
        }else if(msg.equals("")){
            

        }


            }
    public static void main(String[] args) {
        new PatientManagements();
    }
    
}


