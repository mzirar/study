package network;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This program implements a simple server that listens to port 8189 and echoes
 * back all client input.
 */
public class ServerSocketDemo {
	public static void main(String[] args) {
		ServerSocketDemo serverSocketDemo = new ServerSocketDemo();

//		serverSocketDemo.echoServer();

		serverSocketDemo.threadedEchoServer();
	}

	private void threadedEchoServer() {
		try {
			int i = 1;
			ServerSocket s = new ServerSocket(8189);

			while (true) {
				Socket incoming = s.accept();
				System.out.println("Spawning " + i);
				Runnable r = new ThreadedEchoHandler(incoming, i);
				Thread t = new Thread(r);
				t.start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void echoServer() {
		try {
			// establish server socket
			ServerSocket s = new ServerSocket(8189);

			// wait for client connection
			Socket incoming = s.accept();
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);

				out.println("Hello! Enter BYE to exit.");

				// echo client input
				boolean done = false;
				while (!done && in.hasNextLine()) {
					String line = in.nextLine();
					out.println("Echo: " + line);
					if (line.trim().equals("BYE"))
						done = true;
				}
			} finally {
				incoming.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * This class handles the client input for one server socket connection.
 */
class ThreadedEchoHandler implements Runnable

{
	private Socket incoming;
	private int counter;

	/**
	 * Constructs a handler.
	 * 
	 * @param i
	 *            the incoming socket
	 * @param c
	 *            the counter for the handlers (used in prompts)
	 */
	public ThreadedEchoHandler(Socket i, int c) {
		incoming = i;
		counter = c;
	}

	public void run() {
		try {
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);

				out.println("Hello! Enter BYE to exit.");

				// echo client input
				boolean done = false;
				while (!done && in.hasNextLine()) {
					String line = in.nextLine();
					out.println("Echo: " + line);
					if (line.trim().equals("BYE"))
						done = true;
				}
			} finally {
				incoming.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
