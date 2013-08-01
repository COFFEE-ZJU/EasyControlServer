package coffee;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;


public class EasyControlServer extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTextArea logTA;
	private JTextField logTF;
	private JButton runButton;

	private Queue<MyAction> actionQueue = new LinkedList<MyAction>();
	private Robot robot;
	private ServerThread serverThread;
	
	public EasyControlServer(){
		setTitle("EasyControlServer");
		setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout(0, 0));
		scrollPane = new JScrollPane();
		logTA = new JTextArea();
		
		JViewport viewport = new JViewport();
		viewport.add(logTA);
		scrollPane.setViewport(viewport);
		contentPane.add(scrollPane,BorderLayout.CENTER);
		logTF = new JTextField("运行状态: ");
		contentPane.add(logTF,BorderLayout.NORTH);
		runButton = new JButton();
		contentPane.add(runButton,BorderLayout.EAST);
		
		logTF.setEditable(false);
		logTA.setEditable(false);
		runButton.setText("启动服务");
		runButton.addActionListener(new RunButtonListener());
	}
	
	private void execAction(MyAction act){
		if(!(act.isMouse)){
			if(act.act == Constant.ACTION_DOWN) robot.keyPress(act.btn);
			else if(act.act == Constant.ACTION_UP) robot.keyRelease(act.btn);
		}
		else{
			if(act.act == Constant.ACTION_MOVE){
				Point point = MouseInfo.getPointerInfo().getLocation();
				int oldX = point.x;
				int oldY = point.y;
				robot.mouseMove(oldX+act.distX, oldY+act.distY);
			}
			else if(act.act == Constant.ACTION_DOWN) robot.mousePress(act.btn);
			else if(act.act == Constant.ACTION_UP) robot.mouseRelease(act.btn);
			else if(act.act == Constant.ACTION_ROLL){
				if(act.btn == Constant.ROLL_UP) robot.mouseWheel(-1);
				else if(act.btn == Constant.ROLL_DOWN) robot.mouseWheel(1);
			}
		}
	}
	
	class RunButtonListener implements ActionListener{
		private boolean isRunning = false;
		@Override
		public void actionPerformed(ActionEvent e) {
			if(! isRunning){
				isRunning = true;
				if(serverThread == null){
					serverThread = new ServerThread();
					serverThread.start();
				}
				else{
					serverThread.restart();
					logTA.setText(logTA.getText()+"服务运行中...\n");
				}
				runButton.setText("停止服务");
			}
			else {
				isRunning = false;
				serverThread.pause();
				runButton.setText("启动服务");
				logTA.setText(logTA.getText()+"服务已暂停\n");
			}
		}
	}
	
	class ServerThread extends Thread{
		private boolean isPaused = false;
		private MyAction action;
		public void pause(){
			isPaused = true;
		}
		public void restart(){
			isPaused = false;
			//notify();
		}
		public void run(){
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			try{
				String hostName = InetAddress.getLocalHost().getHostName();
				InetAddress[] ips = InetAddress.getAllByName(hostName);
				
				logTA.setText(logTA.getText()+"本机IP: \n");
				for(int i=0;i<ips.length;i++) 
					logTA.setText(logTA.getText()+ips[i].getHostAddress().toString()+"\n");
				DatagramSocket server = new DatagramSocket(Constant.PORT_NUM);
				DatagramSocket sender = new DatagramSocket();
				
				logTA.setText(logTA.getText()+"服务运行中...\n");
				byte[] buf = new byte[10];
				DatagramPacket packet = new DatagramPacket(buf,buf.length);
				DatagramPacket senderPacket;
				int len;
				while(true){
					if(isPaused){
						Thread.sleep(1000);
						continue;
					}
					server.receive(packet);
					len = packet.getLength();
					System.out.println("len: "+len);
					action = new MyAction(buf,len);
					System.out.println(action.toString());
					if(action.act == Constant.ACTION_HEARTBEAT){
						System.out.println("start to send hb");
						byte[] temp = new MyAction(Constant.ACTION_HEARTBEAT).getBytes();
						InetAddress ip = packet.getAddress();
						System.out.println("ip: "+ip.toString());
						senderPacket = new DatagramPacket(temp,temp.length,ip,Constant.PORT_NUM);
						sender.send(senderPacket);
					}
					else execAction(action);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			System.out.println("Server Stop");
		}
	}
	
	class ExecThread extends Thread{
		public void run(){
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			System.out.println("Service Started!");
			while(true){
				while(! actionQueue.isEmpty()){
					System.out.println("ActionQueue size = "+actionQueue.size());
					execAction(actionQueue.poll());
				}
			}
		}
	}
	
	public static void main(String[] args) {
		EasyControlServer ecs = new EasyControlServer();
		ecs.setVisible(true);
		ecs.setResizable(false);
		//ecs.startServer();
	}

}
