import java.util.Scanner;

public class Tester {
	
	// tanasak janpan
	public static void main(String[] args){
		PacketCapture pro = new PacketCapture();
		boolean stay = true;
		Scanner in = new Scanner(System.in);
		
		do{
			System.out.print("command >> ");
			String[] command = in.nextLine().split(" ");
			if(pro.validCommand(command)){
				System.out.println(pro.getMessage());
			}else{
				System.out.println("invalid command!");
			}
		}while(stay);
	}
}
