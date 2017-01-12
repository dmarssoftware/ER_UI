package com.EntityResolution.Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * This class executes the shell script on the remote server
 * Requires the jSch java library
 * @author Saket kumar
 *
 */

public class Executejar{

	private static String USERNAME ="hduser"; // username for remote host
	private static String PASSWORD ="Admin123"; // password of the remote host
	private static String host = "172.18.100.103"; // remote host address
	private static int port=22;

	public static boolean executeFile(String scriptFileName)
	{
		boolean isSuccess = false;
		List<String> result = new ArrayList<String>();
		try
		{
			JSch jsch = new JSch();
			Session session = jsch.getSession(USERNAME, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(PASSWORD);
			session.connect();
			
			System.out.println("connected...");
			
			//create the excution channel over the session
			ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
			// Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
			InputStream in = channelExec.getInputStream();
			// Set the command that you want to execute
			// In our case its the remote shell script
			channelExec.setCommand(scriptFileName);
			// Execute the command
			channelExec.connect();
			
			// Read the output from the input stream we set above
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			//Read each line from the buffered reader and add it to result list
			// You can also simple print the result here
			while ((line = reader.readLine()) != null)
			{
				result.add(line);
			}
			//retrieve the exit status of the remote command corresponding to this channel
			int exitStatus = channelExec.getExitStatus();
			//Safely disconnect channel and disconnect session. If not done then it may cause resource leak
			//channelExec.disconnect();
			//session.disconnect();
			if(exitStatus < 0){
				 System.out.println("Done, but exit status not set!");
				 isSuccess = false;
			}
			else if(exitStatus > 0){
				 System.out.println("Done, but with error!");
				 isSuccess = false;
			}
			else{
				System.out.println("Done!");
				isSuccess = true;
			}
			channelExec.disconnect();
			session.disconnect();
		}
		catch(Exception e){
			System.err.println("Error: " + e);
			isSuccess = false;
		}
		
		System.out.println("result is   "+result);
		return isSuccess;
	}

	
}
