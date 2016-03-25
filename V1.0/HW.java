package HW;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
class SocketClinet_Send extends java.lang.Thread{
	private int flag_send=0;
	private JTextField Massage;
	private int port = 8868;//要監控的port
	private String address="127.0.0.1";
	private String ColorType = "red";
	private JButton RedSet;
	private JButton BlueSet;
	private JButton GreenSet;
	public SocketClinet_Send(String address,int port,JTextField Massage,JButton RedSet,JButton BlueSet,JButton GreenSet){
		this.address = address;
		this.port = port;
		this.Massage = Massage;
		this.RedSet = RedSet;
		this.RedSet.addActionListener(new AL());
		this.BlueSet = BlueSet;
		this.BlueSet.addActionListener(new AL());
		this.GreenSet = GreenSet;
		this.GreenSet.addActionListener(new AL());
	}
	public void run(){
		try{
			Massage.addKeyListener(new KeyLis());
			Socket s=new Socket(address,port);
			System.out.println("Connected with server for sending successfully!!");
			System.out.println("Data transfering...");
			OutputStream out=s.getOutputStream();
			String str;
			while(true){
				if(flag_send==1){
				   str=ColorType+","+Massage.getText();
				   out.write(str.getBytes());
				   flag_send=0;
				   System.out.print("Send:"+str);
				}
				sleep((int)(100*Math.random())); 
			}
		}catch(Exception e){
			System.out.println("Error:"+e);
		}
		
	}
	class AL implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==RedSet){
				ColorType = "red";
			}
			if(e.getSource()==BlueSet){
				ColorType = "blue";
			}
			if(e.getSource()==GreenSet){
				ColorType = "green";
			}
		}
	}
	class KeyLis extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode()==KeyEvent.VK_ENTER){ 
				flag_send=1;
			}
		}
	}
}
class SocketClinet_Recv extends java.lang.Thread{
	private ServerSocket server;
	private JLabel Massage_rev;
	private JTextField Massage;
	private boolean work = false;
	private int port = 8765;// 要接收的port
	private String address = "127.0.0.1";
	public SocketClinet_Recv(String address, int port,JLabel Massage_rev,JTextField Massage){
		this.port = port;
		this.address = address;
		this.Massage = Massage; 
		this.Massage_rev = Massage_rev;
	}
	public void run(){
		byte buff[] = new byte[1024];
		try{
			Socket s=new Socket(address,port);
			System.out.println("Connected with server for receiving successfully!!");
			InputStream in=s.getInputStream();
			int n;
			while(true){
				n=in.read(buff);
				String data = new String(buff,0,n);
				String[] data_res = data.split(",");
				switch(data_res[0]){
					case "red":	Massage_rev.setForeground(Color.RED);break;
					case "blue": Massage_rev.setForeground(Color.BLUE);break;
					case "green": Massage_rev.setForeground(Color.GREEN);break;
				}
				Massage_rev.setText("Server: "+data_res[1]);
				System.out.print("Received from server: ");
				System.out.print(new String(buff,0,n));
            sleep((int)(100*Math.random())); 
         }
      }
      catch(Exception e)
      {
         System.out.println("Error:"+e);
      }
   }
}
class SocketServer_Recv extends java.lang.Thread{
	private ServerSocket server;
	private JLabel Massage_rev;
	private JTextField Massage;
	private boolean work = false;
	private int port = 8868;// 要接收的port
	public SocketServer_Recv(int port,JLabel Massage_rev,JTextField Massage){
		this.port = port;
		this.Massage = Massage; 
		this.Massage_rev = Massage_rev;
	}
	public void run(){
		byte buff[] = new byte[1024];
		try{
			ServerSocket svs = new ServerSocket(port);
			Socket s=svs.accept();
			System.out.println("Clinet connecting for receiving successfully!!");
			System.out.println("Data transfering...");
			InputStream in=s.getInputStream();
			int n;
			while(true){
				n=in.read(buff);
				String data = new String(buff,0,n);
				String[] data_res = data.split(",");
				switch(data_res[0]){
					case "red":	Massage_rev.setForeground(Color.RED);break;
					case "blue": Massage_rev.setForeground(Color.BLUE);break;
					case "green": Massage_rev.setForeground(Color.GREEN);break;
				}
				Massage_rev.setText("Client: "+data_res[1]);
				System.out.print("Received from Client: ");
				System.out.print(new String(buff,0,n));
				sleep((int)(100*Math.random())); 
			}
		}catch(Exception e){
			System.out.println("Error:"+e);
		}
	}
	
}
class SocketServer_Send extends java.lang.Thread{
	private int flag_send=0;
	private JTextField Massage;
	private int port = 8765;//要監控的port
	private String ColorType = "red";
	private JButton RedSet;
	private JButton BlueSet;
	private JButton GreenSet;
	public SocketServer_Send(int port,JTextField Massage,JButton RedSet,JButton BlueSet,JButton GreenSet){
		this.port = port;
		this.Massage = Massage;
		this.RedSet = RedSet;
		this.RedSet.addActionListener(new AL());
		this.BlueSet = BlueSet;
		this.BlueSet.addActionListener(new AL());
		this.GreenSet = GreenSet;
		this.GreenSet.addActionListener(new AL());
	}
	public void run(){
		try{
			Massage.addKeyListener(new KeyLis());
			ServerSocket svs = new ServerSocket(port);
			Socket s = svs.accept();
			System.out.println("Clinet connecting for sending successfully!!");
			System.out.println("Data transfering...");
			OutputStream out = s.getOutputStream();
			String str;
			while(true){
				if(flag_send == 1){
					str = ColorType+","+Massage.getText();
					out.write(str.getBytes());
					flag_send = 0;
					System.out.print("Send:"+str);
				}
				sleep((int)(100*Math.random())); 
			}
		}
		catch(Exception e){
			System.out.println("Error:"+e);
		}
	}
	class AL implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==RedSet){
				ColorType = "red";
			}
			if(e.getSource()==BlueSet){
				ColorType = "blue";
			}
			if(e.getSource()==GreenSet){
				ColorType = "green";
			}
		}
	}
	class KeyLis extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode()==KeyEvent.VK_ENTER){ 
				flag_send=1;
			}
		}
	}
}

class GuiBulider implements ActionListener{
	JFrame demo;
	JButton Host;
	JButton Join;
	JButton RedSet;
	JButton BlueSet;
	JButton GreenSet;
	JLabel Massage_Lab;
	JTextField Massage;
	JLabel Massage_Get;
	private boolean Connect = false;
	private final int Cport = 8868;//Client 發送
	private final int Sport = 8765;//Server 發送
	private final String address = "127.0.0.1";// 連線的ip
	public GuiBulider(){
		demo = new JFrame();
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setLayout(new GridLayout(4,1));
		
		JPanel Option = new JPanel(new GridLayout(1,2));
		Host = new JButton("主機");
		Host.addActionListener(this);
		Join = new JButton("加入");
		Join.addActionListener(this);
		Option.add(Host);
		Option.add(Join);
		
		JPanel Color = new JPanel(new GridLayout(1,3));
		RedSet = new JButton("文字設為紅色");
		BlueSet = new JButton("文字設為藍色");
		GreenSet = new JButton("文字設為綠色");
		Color.add(RedSet);
		Color.add(BlueSet);
		Color.add(GreenSet);
		
		JPanel Keyin = new JPanel(new GridLayout(1,2));
		Massage_Lab = new JLabel("輸入訊息:");
		Massage = new JTextField();
		Massage.addActionListener(this);
		
		Keyin.add(Massage_Lab);
		Keyin.add(Massage);
	
		Massage_Get = new JLabel();
		
		demo.add(Option);
		demo.add(Color);
		demo.add(Keyin);
		demo.add(Massage_Get);
		
		demo.pack();
		demo.setSize(600,600);
		demo.setResizable(false);
		demo.setVisible(true);
	}
	public void actionPerformed(ActionEvent e){
        if(e.getSource()==Host){
			if(!Connect){
				(new SocketServer_Recv(Cport,Massage_Get,Massage)).start();
				(new SocketServer_Send(Sport,Massage,RedSet,BlueSet,GreenSet)).start();
				Join.setEnabled(false);
				Connect = true;
			}
		}
		if(e.getSource()==Join){
			if(!Connect){
				(new SocketClinet_Send(address,Cport,Massage,RedSet,BlueSet,GreenSet)).start();
				(new SocketClinet_Recv(address,Sport,Massage_Get,Massage)).start();
				Host.setEnabled(false);
				Connect = true;
			}
		}
    }
}
public class HW {
	public static void main(String args[]){
		new GuiBulider();
	}
}



