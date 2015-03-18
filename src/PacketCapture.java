import jpcap.*;
public class PacketCapture {
	private String message; 
	private NetworkInterface[] NETWORK_INTERFACES;
	private JpcapCaptor CAP;
	private CaptureThread WORKER;
	private int INDEX = 0;
	private int COUNTER = 0;
	private int PORT = -1;
	private int FrameNumber = 1;
	private boolean CaptureState = false;
	
	public PacketCapture(){
		
	}
	
	public void ListNetworkInterfaces(){
		NETWORK_INTERFACES = JpcapCaptor.getDeviceList();
		for(int i=0;i<NETWORK_INTERFACES.length;i++){
			System.out.println("");
			System.out.println("------Interface "+ i +" info-------");
			System.out.println("Interface Number: "+i);
			System.out.println("Description: "+ NETWORK_INTERFACES[i].name + "("+NETWORK_INTERFACES[i].description+")");
			System.out.println("Datalink Name: "+ NETWORK_INTERFACES[i].datalink_name + "("+NETWORK_INTERFACES[i].datalink_description+")");
			System.out.print("MAC address: ");
			byte[] R = NETWORK_INTERFACES[i].mac_address;
			for (int A=0;A<NETWORK_INTERFACES.length;A++){
				System.out.print(Integer.toHexString(R[A] & 0xff)+":");
			}
			System.out.println("");
			
			for (NetworkInterfaceAddress INTF : NETWORK_INTERFACES[i].addresses){
				System.out.println("IP Address: "+INTF.address);
				System.out.println("Subnet Mask: "+INTF.subnet);
				System.out.println("Broadcast Address: "+INTF.broadcast);
			}
			
			COUNTER++;
		}
	}
	
	public void ChooseInterface(int interf){
		INDEX = interf;
	}
	
	public void CapturePackets(){
		System.out.println("Now Capturing on Interface "+INDEX+"...");
		try{
					
			CAP = JpcapCaptor.openDevice(NETWORK_INTERFACES[INDEX], 65535, false, 20);
			if(PORT!=-1) CAP.setFilter("port "+PORT, true);
			while(CaptureState){
				CAP.processPacket(1, new PacketGrabber(INDEX, FrameNumber));
				FrameNumber++;
			}
				CAP.close();
			}catch(Exception X){
				System.out.println("error");
		}
	}
	
	public boolean validCommand(String[] command){
		boolean valid = false;
		try{
			if(command[0].equalsIgnoreCase("list")){
				
				ListNetworkInterfaces();
				message = "";
				valid = true;
			}else if(command[0].equalsIgnoreCase("select")){
				ChooseInterface(Integer.parseInt(command[1]));
				message = "choosing interface successfully";
				valid = true;
			}else if(command[0].equalsIgnoreCase("capture")){
					CaptureState = true;
					CapturePackets();
					message = "";
					valid = true;
			}else if(command[0].equalsIgnoreCase("filter") && command[1].equalsIgnoreCase("port") ){
				PORT = Integer.parseInt(command[2]);
				message = "you have set filter to port "+command[2];
				valid = true;
			}else{
				
			}
		}catch(Exception e){}
		
		return valid;
	}
	
	public String getMessage(){
		return message;
	}
	
}
