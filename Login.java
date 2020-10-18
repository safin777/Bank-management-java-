import java.lang.*;
import javax.swing.*;
import javax.swing.border.*; //for border 
import java.awt.*; 
import java.awt.event.*; //event perform or action 
import java.sql.*; //sql database connection

public class  Login extends JFrame implements ActionListener  //login class inherit jframe class and implements interface Action
{
	public JPanel panel;  // for panel design
	public JLabel userlabel,passlabel,logolabel;  //label design
	public JTextField userfield;  // for textfield
	public JPasswordField passfield;  //for passwordfield
	public JButton login,forgetPassword;  //for button
	public ImageIcon logo;   //for imageicon adding
	
	
	public Login()
	{
		super("Login Form");  //
		this.setSize(800,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // an option for the close button which exit the application 
		
		Font myFont= new Font("Consolas",Font.PLAIN, 25);  //font selection
		Color fontColor = new Color (41,41,41);
		Color panelColor = new Color (225,225,225); //coloring font in RGB method
		Color buttonColColor = new Color (74,96,132);
		
		
		panel= new JPanel ();
		panel.setLayout(null);
		panel.setBackground(panelColor); //panel backgroung operation
		
		
		logo = new ImageIcon("logo.jpg");
		logolabel = new JLabel(logo);
		logolabel.setBounds(0,-25,800,150);  //bound the location of this label
		panel.add(logolabel);
		
		int labelxAxis=200,labelyAxis=200,labelWidth=200,labelHeight=40;
		int fieldxAxis=400,fieldyAxis=205,fieldWidth=200,fieldHeight=30;
		
		
		userlabel=new JLabel("Bank ID:");
		userlabel.setBounds(labelxAxis,labelyAxis,labelWidth,labelHeight);
		userlabel.setFont(myFont);
		userlabel.setForeground(fontColor);
		panel.add(userlabel);
		
		userfield=new JTextField();
		userfield.setBounds(fieldxAxis,fieldyAxis,fieldWidth,fieldHeight);
		userfield.setFont(myFont);
		panel.add(userfield);
		
		passlabel=new JLabel("Password:");
		passlabel.setBounds(labelxAxis,labelyAxis+100,labelWidth,labelHeight);
		passlabel.setFont(myFont);
		passlabel.setForeground(fontColor);
		panel.add(passlabel);
		
		passfield=new JPasswordField();
		passfield.setBounds(fieldxAxis,fieldyAxis+100,fieldWidth,fieldHeight);
		passfield.setFont(myFont);
		panel.add(passfield);
		
		
		login=new JButton("Login");
		login.setBounds(labelxAxis+100,labelyAxis+200,labelWidth-80,labelHeight);
		login.setFont(myFont);
		login.setForeground(fontColor);
		login.setBackground(buttonColColor);
		login.addActionListener(this);
		panel.add(login);
		
		forgetPassword=new JButton("Forget Password");
		forgetPassword.setBounds(labelxAxis+300,labelyAxis+320,labelWidth+50,labelHeight-10);
		forgetPassword.setFont(myFont);
		forgetPassword.setForeground(fontColor);
		forgetPassword.setBackground(buttonColColor);
		forgetPassword.addActionListener(this);
		panel.add(forgetPassword);
		
		
		this.add(panel);	
	}
	public void actionPerformed(ActionEvent ae) //The actionPerformed method is called when the associated object generates a action
	{ 
		String text = ae.getActionCommand();  //getActionCommand() gives you a String representing the action command.
		if(ae.getSource()== login)  //We can use getSource() to identify the component and execute corresponding lines of code within an action-listener.
		{
			checkLogin();
		}
		if(ae.getSource()== forgetPassword)
		{
			ForgetPassword fp=new ForgetPassword();
			fp.setVisible(true);
			this.setVisible(false);
		}
		
	}
	
	
	
	
	
	public void checkLogin()
	{
		String query = "SELECT `Bank_id`, `Password`, `Status` FROM `login`;";     
        Connection con=null;//for connection
        Statement st = null;//for query execution
		ResultSet rs = null;//to get row by row result from DB
		System.out.println(query);
        try
		{
			Class.forName("com.mysql.jdbc.Driver");//load driver
			System.out.println("driver loaded");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			rs = st.executeQuery(query);//getting result
			System.out.println("results received");
			
			boolean flag = false;			
			while(rs.next())
			{
                String Bank_id = rs.getString("Bank_id");
				String Password = rs.getString("Password");
				int Status = rs.getInt("Status");
				
				if(Bank_id.equals(userfield.getText()) && Password.equals(passfield.getText()))
				{
					flag=true;
					if(Status==1)
					{
						EmployeeInfo ep =new EmployeeInfo(Bank_id);
			            ep.setVisible(true);
			            this.setVisible(false);
					}
					else if(Status==0)
					{
						CustomerInfo ch = new CustomerInfo(Bank_id);
						CustomerBill ch1 = new CustomerBill(Bank_id);
						CustomerTransfer ch2 = new CustomerTransfer(Bank_id);
						CustomerWithdraw ch3 = new CustomerWithdraw(Bank_id);
						ch.setVisible(true);
						this.setVisible(false);
					}
					else if(Status==2)
					{
						AdminInfo ad=new AdminInfo(Bank_id);
						ad.setVisible(true);
			            this.setVisible(false);
					}
				}
			}
			if(!flag)
			{
				JOptionPane.showMessageDialog(this,"Invalid ID or Password"); 
			}
		}
        catch(Exception ex)
		{
			System.out.println("Exception : " +ex.getMessage());
        }
        finally
		{
            try
			{
                if(rs!=null)
					rs.close();

                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex){}
        }
	}
}
