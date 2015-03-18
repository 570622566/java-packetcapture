import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import jpcap.*;
import jpcap.packet.*;
public class PacketGrabber implements PacketReceiver {
	private int FrameNumber;
	private int Interface;
	private String Wrapper;
	public PacketGrabber(int in, int fn){
		FrameNumber = fn;
		Interface = in;
		Wrapper = "";
	}
	public void receivePacket(Packet packet) {
		String[] pac = packet.toString().split(" ");
		String[] link = packet.datalink.toString().split(" ");
		
		Wrapper += "------------------------------------------------------------------------------------\n";
		Wrapper += "Frame "+FrameNumber+": "+packet.len+" bytes on interface "+ Interface+"\n";
		
		Wrapper += "Ethernet II : "; 
		Wrapper += "MAC Source = "+link[1].split("->")[0]+", ";
		Wrapper +=  "MAC Destination = "+link[1].split("->")[1]+"";
		Wrapper +=  "\n";
		Wrapper += "Internet Protocol: ";
		if(!link[1].split("->")[1].equalsIgnoreCase("ff:ff:ff:ff:ff:ff")){
			Wrapper += "IP Source = "+pac[1].split("->")[0].replace("/", "")+", ";
			Wrapper += "IP Destination = "+pac[1].split("->")[1].replace("/", "");
		}else{
			Wrapper +=  "Broadcast";
		}
		Wrapper +=  "\n";
		Wrapper += "Protocol: ";
		if(!pac[0].equalsIgnoreCase("arp") && !pac[0].equalsIgnoreCase("rarp")){
			int protocol = Integer.parseInt(pac[2].replace(")","").replace("(","/").split("/")[1]);
			Wrapper += "" + getProtocol(protocol);
			if(pac.length>10){
				Wrapper += ", ";
				Wrapper += "Port Source = "+pac[10]+", ";
				Wrapper += "Port Destination = "+pac[12];
				Wrapper +=  " ";
			}
		}else{
			Wrapper +=  packet;
		}
		
		/*
		 For more information
		 for(int i=3;i<pac.length;i++){
			System.out.print(pac[i].replace("(", " = ").replace(")",","));
			System.out.print(" ");
		}
		*/
		
		Wrapper +=  "\n";
		
		try{
			SaveOutput(Wrapper);
		}catch(Exception e){}
	}
	
	public void SaveOutput(String A) throws IOException{		  
		  String text = A;
		  System.out.println(text);
		  PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("writer.txt", true)));
		  out.println(text);
		  out.close(); 
	}
	
	private String getProtocol(int num){
		String[] protocol = {"HOPOPT","ICMP","IGMP","GGP","IPv4","ST","TCP",
							 "CBT","EGP","IGP","BBN-RCC-MON","NVP-II","PUP",
							 "ARGUS","EMCON","XNET","CHAOS","UDP","MUX","DCN-MEAS","HMP"};
		if(num<=20) return protocol[num];
		else return ""+num;
	}
	
	
}
