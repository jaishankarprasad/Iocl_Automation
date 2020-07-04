
package de.re.easymodbus.modbusserver;

import java.io.IOException;

class ListenerThread extends Thread 
{
	ModbusServer easyModbusTCPServer;
	public ListenerThread(ModbusServer easyModbusTCPServer)
	{
		this.easyModbusTCPServer = easyModbusTCPServer;
	}
	
	  public void run()
	    {		  
		  java.net.ServerSocket serverSocket = null;
		try {
             	serverSocket = new java.net.ServerSocket(easyModbusTCPServer.getPort());

	    
	        while (easyModbusTCPServer.getServerRunning() & !this.isInterrupted())
	        {	
                    java.net.Socket socket = serverSocket.accept();
                    (new ClientConnectionThread(socket, easyModbusTCPServer)).start();
	        }
		} catch (IOException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if (serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

}
