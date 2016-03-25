package SocketAWT;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
class SocketClinet_Send extends java.lang.Thread{
	private int flag_send=0;
	private JTextField Massage;
	private int port = 8868;//�n�ʱ���port
	private String address="127.0.0.1";
	private String ColorType = "red";
	private JButton[] setColor;
	private JButton SendMeg;
	public SocketClinet_Send(String address,int port,JTextField Massage,JButton[] setColor,JButton SendMeg){
		this.address = address;
		this.port = port;
		this.Massage = Massage;
		this.setColor = setColor;
		this.SendMeg = SendMeg;
		SendMeg.addActionListener(new AL());
		for(int i=0;i<3;i++) this.setColor[i].addActionListener(new AL());
	}
	public void run(){
		try{
			Massage.addKeyListener(new KeyLis());
			Socket s=new Socket(address,port);
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
			if(e.getSource()==setColor[0]){
				ColorType = "red";
			}
			if(e.getSource()==setColor[1]){
				ColorType = "blue";
			}
			if(e.getSource()==setColor[2]){
				ColorType = "green";
			}
			if(e.getSource()==SendMeg){
				flag_send=1;
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
	private TextPane Massage_rev;
	private boolean work = false;
	private int port = 8765;// �n������port
	private String address = "127.0.0.1";
	private JButton Host;
	public SocketClinet_Recv(String address, int port,TextPane Massage_rev,JButton Host){
		this.port = port;
		this.address = address;
		this.Massage_rev = Massage_rev;
		this.Host = Host;
	}
	public void run(){
		byte buff[] = new byte[1024];
		while(true){
			try{
				Socket s = new Socket(address,port);			
				InputStream in=s.getInputStream();
				Massage_rev.appendToPane("���A���s�����\!\n",Color.BLACK);
				Massage_rev.appendToPane("�}�l��ƶǿ�...\n",Color.BLACK);
				int n;
				while(true){
					String[] data_res = new String[2];
					n=in.read(buff);
					String data = new String(buff,0,n);
					try{
						data_res = data.split(",");
						switch(data_res[0]){
							case "red":	Massage_rev.appendToPane("Server: "+data_res[1]+"\n", Color.RED);break;
							case "blue": Massage_rev.appendToPane("Server: "+data_res[1]+"\n", Color.BLUE);break;
							case "green": Massage_rev.appendToPane("Server: "+data_res[1]+"\n", Color.GREEN);break;
						}
					}catch(ArrayIndexOutOfBoundsException AIO){
						Massage_rev.appendToPane("Server: \n", Color.RED);
					}
					
					System.out.print("Received from server: ");
					System.out.print(new String(buff,0,n));
					try{
						sleep((int)(100*Math.random()));
					}catch(InterruptedException IE){
						System.out.println("Error:"+IE);
						continue;
					}
				}
			}catch(ConnectException ce){
				Massage_rev.appendToPane("���A���_�u!\n", Color.RED);
				Host.setEnabled(true);
				return;
			}catch(IOException ioe){
				System.out.println("Error:"+ioe);
				continue;
			}
		}
		
	}
}
class SocketServer_Recv extends java.lang.Thread{
	private ServerSocket server;
	private TextPane Massage_rev;
	private JButton Join;
	public SocketServer_Recv(int port,TextPane Massage_rev,JButton Join){
		this.Massage_rev = Massage_rev;
		this.Join = Join;
		try{
			server = new ServerSocket(port);
		}catch(IOException ioe){
			System.out.println("Error:"+ioe);
		}
	}
	public void run(){
		byte buff[] = new byte[1024];
		while(true){
			Socket s = null;
			try{
				System.out.println(server);
				s = server.accept();
				Massage_rev.appendToPane("�Ȥ�ݳs�����\!\n",Color.BLACK);
				Massage_rev.appendToPane("�}�l��ƶǿ�...\n",Color.BLACK);
				InputStream in=s.getInputStream();
				int n;
				while(true){
					n=in.read(buff);
					String data = new String(buff,0,n);
					String[] data_res = new String[2];
					try{
						data_res = data.split(",");
						switch(data_res[0]){
							case "red":	Massage_rev.appendToPane("Client: "+data_res[1]+"\n", Color.RED);break;
							case "blue": Massage_rev.appendToPane("Client: "+data_res[1]+"\n", Color.BLUE);break;
							case "green": Massage_rev.appendToPane("Client: "+data_res[1]+"\n", Color.GREEN);break;
						}
					}catch(ArrayIndexOutOfBoundsException AIO){
						Massage_rev.appendToPane("Client: \n", Color.RED);
					}
					try{
						sleep((int)(100*Math.random()));
					}catch(InterruptedException IE){
						System.out.println("Error:"+IE);
						continue;
					}
				}
			}catch(SocketException se){
				Massage_rev.appendToPane("�Ȥ���_�u!\n", Color.RED);
				Massage_rev.appendToPane("���A�����ݳs����...\n",Color.BLACK);
			}catch(IOException e){	
				System.out.println("Error(RECV):"+e);
			}
		}
	}
}
class SocketServer_Send extends java.lang.Thread{
	private int flag_send=0;
	private JTextField Massage;
	private int port = 8765;//�n�ʱ���port
	private String ColorType = "red";
	private JButton[] setColor;
	private JButton SendMeg;
	public SocketServer_Send(int port,JTextField Massage,JButton[] setColor,JButton SendMeg){
		this.port = port;
		this.Massage = Massage;
		this.setColor = setColor;
		this.SendMeg = SendMeg;
		SendMeg.addActionListener(new AL());
		for(int i=0;i<3;i++) this.setColor[i].addActionListener(new AL());
	}
	public void run(){
		try{
			Massage.addKeyListener(new KeyLis());
			ServerSocket svs = new ServerSocket(port);
			Socket s = svs.accept();
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
			if(e.getSource()==setColor[0]){
				ColorType = "red";
			}
			if(e.getSource()==setColor[1]){
				ColorType = "blue";
			}
			if(e.getSource()==setColor[2]){
				ColorType = "green";
			}
			if(e.getSource()==SendMeg){
				flag_send=1;
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

class TextPane extends JTextPane{
	public TextPane(){
		super();
	}
	public void appendToPane(String msg, Color c){
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = getDocument().getLength();
        setCaretPosition(len);
        setCharacterAttributes(aset, false);
        replaceSelection(msg);
    }

}
class GuiBulider implements ActionListener{
	JFrame demo = new JFrame("JAVA Socket AWT");
	JTextField AddressBox = new JTextField("127.0.0.1");
	JButton Host = new JButton("�D��");
	JButton Join = new JButton("�s��");
	JScrollPane Message_Panel;
	TextPane Message_Get = new TextPane();
	JButton[] setColor = new JButton[3];
	JTextField Message = new JTextField();
	JButton Message_Send = new JButton("�o�e");
	private final int Cport = 8868;//Client �o�e
	private final int Sport = 8765;//Server �o�e
	public GuiBulider(){
		try{
			InetAddress adr = InetAddress.getLocalHost();
		}catch(Exception e){
			System.out.println("Error:" + e);
		}
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setLayout(null);
		
		AddressBox.setBounds(20,40,320,30);
		
		Host.setBounds(360,40,100,30);
		Host.addActionListener(this);
		
		Join.setBounds(480,40,100,30);
		Join.addActionListener(this);
		
		Message_Panel = new JScrollPane(Message_Get);
		Message_Panel.setBounds(20,90,560,200);
		
		setColor[0] = new JButton("��r�]������");
		setColor[0].setBounds(20,300,175,30);
		setColor[1] = new JButton("��r�]���Ŧ�");
		setColor[1].setBounds(215,300,170,30);
		setColor[2] = new JButton("��r�]�����");
		setColor[2].setBounds(405,300,175,30);
		
		Message.setBounds(20,350,440,30);
		Message_Send.setBounds(480,350,100,30);
		
		demo.add(AddressBox);
		demo.add(Host);
		demo.add(Join);
		demo.add(Message_Panel);
		demo.add(setColor[0]);
		demo.add(setColor[1]);
		demo.add(setColor[2]);
		demo.add(Message);
		demo.add(Message_Send);
		
		
		demo.pack();
		demo.setSize(600,450);
		demo.setResizable(false);
		demo.setVisible(true);
	}
	public void actionPerformed(ActionEvent e){
        if(e.getSource()==Host){
			if(Join.isEnabled()){
				(new SocketServer_Recv(Cport,Message_Get,Join)).start();
				(new SocketServer_Send(Sport,Message,setColor,Message_Send)).start();
				Message_Get.appendToPane("���A�����ݳs����...\n",Color.BLACK);
				Join.setEnabled(false);
			}
		}
		if(e.getSource()==Join){
			if(Host.isEnabled()){
				(new SocketClinet_Send(AddressBox.getText(),Cport,Message,setColor,Message_Send)).start();
				(new SocketClinet_Recv(AddressBox.getText(),Sport,Message_Get,Host)).start();
				Message_Get.appendToPane("�s�����A����...\n",Color.BLACK);
				Host.setEnabled(false);
			}
		}
    }
}
public class Main {
	public static void main(String args[]){
		new GuiBulider();
	}
}



