package com.EntityResolution.Util;

import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;

public class Margehdfs {
	public static void main(String args[]) {

				try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser("hduser");
			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {
					Configuration conf = new Configuration();
					conf.set("fs.defaultFS", "hdfs://172.25.3.7:54310/");
					conf.set("hadoop.job.ugi", "hduser");
					FileSystem fs = FileSystem.get(conf);
					//marge files
					Path trg = new Path("hdfs://172.25.3.7:54310/part/2.txt");
					List<Path> fileList = new ArrayList<Path>(); 
					FileStatus[] fstatus1 = fs.listStatus(new Path("/part"));
					for(int i=0;i<fstatus1.length;i++){
						if(fstatus1[i].getPath().equals(trg)){
							System.out.println("break...");
						}else{
							fileList.add(fstatus1[i].getPath());
							System.out.println(fstatus1[i].getPath());
						}
					}
					Path [] mergeFile = fileList.toArray(new Path[fileList.size()]);
					fs.concat(trg, mergeFile);
					System.out.println("done");
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
