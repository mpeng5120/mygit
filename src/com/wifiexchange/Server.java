/*

锘�*    Copyright 2012 Brayden (headdetect) Lopez
 *    
 *    Dual-licensed under the Educational Community License, Version 2.0 and
 *	the GNU General Public License Version 3 (the "Licenses"); you may
 *	not use this file except in compliance with the Licenses. You may
 *	obtain a copy of the Licenses at
 *
 *		http://www.opensource.org/licenses/ecl2.php
 *		http://www.gnu.org/licenses/gpl-3.0.html
 *
 *		Unless required by applicable law or agreed to in writing
 *	software distributed under the Licenses are distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the Licenses for the specific language governing
 *	permissions and limitations under the Licenses.
 * 
 */

package com.wifiexchange;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.dbutils.Constans;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerChatter.
 */
public class Server implements Runnable {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	/** The client list. */
	private ArrayList<Client> clientList;

	/** The server socket. */
	private ServerSocket mSocket;

	/** The crypto key. */
	//private byte[] cryptoKey;

	/** The shutting down. */
	private boolean shuttingDown;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Instantiates a new server chatter.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Server() throws IOException {
		mSocket = new ServerSocket(Constans.PORT);
		clientList = new ArrayList<Client>();

		/*Random mRand = new SecureRandom();
		cryptoKey = new byte[16];
		mRand.nextBytes(cryptoKey);*/
		
		//System.out.println("Server ->" + new String(cryptoKey));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Checks if is shutting down.
	 * 
	 * @return true, if is shutting down
	 */
	public boolean isShuttingDown() {
		return shuttingDown;
	}

	/**
	 * Sets the shutting down.
	 * 
	 * @param shuttingDown
	 *            the new shutting down
	 */
	public void setShuttingDown(boolean shuttingDown) {
		this.shuttingDown = shuttingDown;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while (!shuttingDown) {
			Socket socket = null;
			Client client = null;
			try {
				socket = this.mSocket.accept();


				//socket.getOutputStream().write(cryptoKey);
				/*PrintWriter writer = new PrintWriter(new BufferedWriter(
					    new OutputStreamWriter(socket.getOutputStream())), true);
				writer.println("鏈嶅姟鍣ㄥ彂杩囨潵鐨�?;*/
				
				client = new Client();
				//client.setKey(cryptoKey);
				new Thread(client).start();
				clientList.add(client);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					
					if (socket != null)
						socket.close();
				} catch (IOException e1) {
					//Please, can i have some more exceptions
					e1.printStackTrace();
				}
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            the message
	 * @throws Exception
	 *             the exception
	 */
	public void sendMessage(String message) throws Exception {
		for (Client client : clientList) {

			if (shuttingDown)
				return;

			//client.sendMessage(message);
		}

	}

	/**
	 * Shutdowns the server.
	 */
	public void shutdown() {
		shuttingDown = true;

		try {
			mSocket.close();
		} catch (IOException e) {
		} finally {
			mSocket = null;
		}

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
