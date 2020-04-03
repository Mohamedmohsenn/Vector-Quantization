import java.*;
import java.util.Scanner;
public class Main {
	public static void main(String args[]) throws Exception {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter vector width : ");
		int vectorWidth = in.nextInt();
		
		System.out.println("Enter vector height: ");
		int vectorHeight = in.nextInt();
		
		
		System.out.println("Enter vector size: ");
		int vectorSize = in.nextInt();
		
		System.out.println("Enter code book size: ");
		int codeBookSize = in.nextInt();
		
		
		
		Quantizing c = new Quantizing(vectorWidth,vectorHeight,vectorSize,codeBookSize);
		c.compression("E:/VectorQuantization/mohsen.png");
		c.decompression("compressed.txt");
		
	}
}
