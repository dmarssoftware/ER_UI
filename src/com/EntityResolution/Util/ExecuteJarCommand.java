package com.EntityResolution.Util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ExecuteJarCommand {

	public static boolean executeSparkCommand(String command){
		boolean isSuccessstatus = false;
		int exitStatus = 0 ;
		String user = "hduser";
		String password = "Admin123";
		String host = "172.18.100.103";
		try{
			int port=22;
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			Channel channel=session.openChannel("exec"); 
			((ChannelExec)channel).setCommand(command);

			channel.connect();
			channel.run();
			exitStatus = channel.getExitStatus();
			channel.disconnect();
			session.disconnect();
			if(exitStatus < 0){
				System.out.println("done , but exit status not set");
				isSuccessstatus= false;
			}
			else if(exitStatus > 0){
				System.out.println("done, with error!!");
				isSuccessstatus=false;
			}
			else{
				System.out.println("done, success");
				isSuccessstatus=true;
			}
			channel.disconnect();
			session.disconnect();

		}
		catch(Exception ex){
			ex.printStackTrace();
			isSuccessstatus = false;

		}

		return isSuccessstatus;
	}

/*	public static void main(String args[]){
		ExecuteJarCommand ob = new ExecuteJarCommand ();
		String command = "spark-submit address_segmentation.py hdfs://hadoop-master:54310/EntityR/input_files/ hdfs://hadoop-master:54310/ER/nivedita_addr_segment spark://hadoop-master:7077 3";
		ob.executeSparkCommand(command);
		
	}*/

}
