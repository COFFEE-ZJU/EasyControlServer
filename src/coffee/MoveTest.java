package coffee;

public class MoveTest {
	
	public static void main(String[] args){
		MyAction action,action2;
		short x,y;
		x= 5555;y=-5555;
		action = new MyAction(x,y);
		System.out.println("x = "+action.distX+"\ty = "+action.distY);
		
		System.out.println((action.distX / 128));
		System.out.println((action.distX % 128));
		System.out.println((action.distY / 128));
		System.out.println((action.distY % 128));
//	    bytes[3] = (byte)(distX % 256);
//	    bytes[4] = (byte)(distY / 256);
//	    bytes[5] = (byte)(distY % 256);
	    
		System.out.println((action.getBytes()[2]));
		System.out.println((action.getBytes()[3]));
		System.out.println((action.getBytes()[4]));
		System.out.println((action.getBytes()[5]));
		action2 = new MyAction(action.getBytes(), action.getBytes().length);
		System.out.println("x = "+action2.distX+"\ty = "+action2.distY);
	}
}
