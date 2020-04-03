import java.util.ArrayList;

public class Matrix {
	public ArrayList<Integer> arr = new ArrayList<>();
	public ArrayList<ArrayList<Integer>> nearst = new ArrayList<>();
	public ArrayList<Integer> averageVector = new ArrayList<>();
	String averageCode;
	

	public void setAverage (int vectorWidth , int vectorHeight , int vectorSize , int codeBookSize) {
		Quantizing c = new Quantizing(vectorWidth,vectorHeight,arr.size(),codeBookSize);
		averageVector = c.getAverageVector(nearst);
		return;
	}
}
