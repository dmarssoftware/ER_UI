package com.EntityResolution.Util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ShellExecuter{

	private static String USERNAME ="hadoop"; 
	private static String PASSWORD ="Admin123"; 
	private static String host = "172.18.100.102"; 
	private static int port=22;

	public static boolean executeFile(String scriptFileName)
	{
		boolean isSuccess = false;
		String scriptName = "/home/hadoop/update_files.sh ";
		scriptFileName = scriptName + scriptFileName ;
		System.out.println("Execute --" + scriptFileName);
		try
		{
			JSch jsch = new JSch();
			Session session = jsch.getSession(USERNAME, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(PASSWORD);
			session.connect();
			System.out.println("connected...");
			ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
			channelExec.setCommand("sh "+scriptFileName);
			channelExec.connect();
			int exitStatus = channelExec.getExitStatus();
			channelExec.disconnect();
			session.disconnect();
			
			if(exitStatus < 0){
				System.out.println(exitStatus);
				 System.out.println("Done, but exit status not set!");
				 isSuccess = true;
			}
			else if(exitStatus > 0){
				System.out.println(exitStatus);
				System.out.println("Done, but with error!");
				 isSuccess = true;
			}
			else{
				System.out.println(exitStatus);
				System.out.println("Done!");
				isSuccess = true;
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			isSuccess = false;
			
		}
		
		return isSuccess;
	}
}
