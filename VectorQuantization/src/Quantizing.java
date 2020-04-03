import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Quantizing {

	int codeBookSize;
	int vectorHeight;
	int vectorWidth;
	int imageWidth;
	int imageHeight;
	int vectorSize;
	ArrayList<ArrayList<Integer>> vectors = new ArrayList<>();
	ArrayList<ArrayList<Integer>> splitted = new ArrayList<>();
	static ArrayList<Matrix> matrix = new ArrayList<>();
	ArrayList<String> finalCode = new ArrayList<String>();
	String decompressedImage[][] = null;

	public Quantizing(int vectorHeight, int vectorWidth, int vectorSize,
			int codeBookSize) {
		this.vectorSize = vectorSize;
		this.vectorHeight = vectorHeight;
		this.vectorWidth = vectorWidth;
		this.codeBookSize = codeBookSize;
		imageHeight = 512;
		imageWidth = 512;
	}

	public static void setAverageCode() {
		int numberOfSymbols = matrix.size() - 1;
		int numberOfBits = (int) (Math.log(numberOfSymbols) / Math.log(2) + 1);
		for (int i = 0; i <= numberOfSymbols; i++) {
			String str = Integer.toBinaryString(i);
			String s = "";
			if (str.length() < numberOfBits) {
				for (int j = 0; j < numberOfBits - str.length(); j++)
					s += "0";
			}
			str = s + Integer.toBinaryString(i);
			matrix.get(i).averageCode = str;
		}
	}

	private int[][] convertImageToMatrix(String imagePath) throws IOException {
		BufferedImage source = ImageIO.read(new File(imagePath));

		int[][] array = new int[source.getWidth()][source.getHeight()];
		imageWidth = source.getWidth();
		imageHeight = source.getHeight();

		for (int i = 0; i < source.getWidth(); i++) {
			// int counter = 0;
			for (int j = 0; j < source.getHeight(); j++) {
				array[i][j] = source.getRGB(i, j) & 255;
			}
		}
		return array;
	}

	public void makeBlocks(String path) throws IOException {
		int[][] image = convertImageToMatrix(path);
		for (int i = 0; i < imageWidth; i += vectorWidth) {
			for (int j = 0; j < imageHeight; j += vectorHeight) {
				ArrayList<Integer> newBlock = new ArrayList<Integer>();
				for (int x = j; x < j + vectorWidth; x++) {
					for (int y = i; y < i + vectorHeight; y++) {
						if (x >= imageWidth || y >= imageHeight) {
							newBlock.add(image[0][0]);
						} else {
							newBlock.add(image[x][y]);
						}
					}
				}
				vectors.add(newBlock);
			}
		}
	}

	public ArrayList<Integer> getAverageVector(ArrayList<ArrayList<Integer>> arr) {
		ArrayList<Integer> averageVector = new ArrayList<Integer>();
		for (int i = 0; i < vectorSize; i++) {
			int sum = 0;
			int average = 0;
			for (int j = 0; j < arr.size(); j++) {
				int num = arr.get(j).get(i);
				sum += num;
			}
			average = sum / arr.size();
			averageVector.add(average);
		}
		return averageVector;
	}

	public void split(ArrayList<Integer> arr) {
		ArrayList<Integer> newArr = new ArrayList<Integer>();
		for (int i = 0; i < arr.size(); i++) {
			newArr.add(arr.get(i) + 1);
		}
		splitted.add(newArr);
		newArr = new ArrayList<Integer>();
		for (int i = 0; i < arr.size(); i++) {
			newArr.add(arr.get(i) - 1);
		}
		splitted.add(newArr);
	}

	public ArrayList<Integer> getDifference(ArrayList<Integer> arr) {
		ArrayList<Integer> diff = new ArrayList<Integer>();
		for (int i = 0; i < vectors.size(); i++) {
			int difference = 0;
			for (int counter = 0; counter < vectorSize; counter++) {
				difference += Math.abs(vectors.get(i).get(counter)
						- arr.get(counter));

			}
			diff.add(difference);
		}
		return diff;
	}

	public void getNearest(ArrayList<ArrayList<Integer>> arr) {
		matrix.clear();
		ArrayList<ArrayList<Integer>> allDif = new ArrayList<>();
		for (int i = 0; i < arr.size(); i++) {
			// bt7wl kl vector b3d splitting l difference
			ArrayList<Integer> difference = getDifference(arr.get(i));
			allDif.add(difference);
		}
		for (int j = 0; j < vectors.size(); j++) {
			int index = 0;
			int min = allDif.get(0).get(j);
			for (int i = 1; i < allDif.size(); i++) {
				if (allDif.get(i).get(j) < min) {
					min = allDif.get(i).get(j);
					index = i;
				}
			}

			boolean check = false;
			for (int k = 0; k < matrix.size(); k++) {
				if (matrix.get(k).arr == arr.get(index)) {
					matrix.get(k).nearst.add(vectors.get(j));
					check = true;
				}
			}
			if (check == false) {
				Matrix m = new Matrix();
				m.arr = arr.get(index);
				m.nearst.add(vectors.get(j));
				matrix.add(m);
			}

		}
		for (int i = 0; i < matrix.size(); i++) {
			matrix.get(i).setAverage(vectorWidth, vectorHeight, vectorHeight,
					codeBookSize);
		}
		// vectors.get(j) nearst to arr . get(i)
	}

	public String[][] getCompressedImage() {
		int size = imageWidth / vectorWidth;
		String[][] deco = new String[size][size];
		for (int i = 0; i < vectors.size(); i++) {
			for (int j = 0; j < matrix.size(); j++) {
				for (int k = 0; k < matrix.get(j).nearst.size(); k++) {
					if (vectors.get(i) == matrix.get(j).nearst.get(k)) {
						finalCode.add(matrix.get(j).averageCode);
					}
				}
			}
		}

		int k = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				deco[j][i] = finalCode.get(k);
				k++;
			}
		}
		return deco;
	}

	public void readDecompressedImage(String path) throws IOException {
		matrix.clear();
		int size = imageWidth / vectorWidth;
		ArrayList<String> allData = new ArrayList<String>();
		decompressedImage = new String[size][size];
		File f = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String st;
		while ((st = br.readLine()) != null)
			allData.add(st);
		int count = 0;
		for (int i = 0; i < allData.size(); i++) {
			if (allData.get(i).equals("587")) {
				count = i;
			}
		}
		String inpNew = new String();
		for (int i = 0; i < count; i++) {
			inpNew += allData.get(i);
		}

		String inp[] = inpNew.split(" ");

		int k = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				decompressedImage[i][j] = inp[k];
				k++;
			}
		}
		boolean check = false;
		for (int i = count + 1; i < allData.size(); i++) {
			inpNew = new String();
			inpNew = allData.get(i);
			String arr[] = inpNew.split(" ");
			Matrix m = new Matrix();
			ArrayList<Integer> newMat = new ArrayList<>();
			for (int j = 0; j < arr.length; j++)
				newMat.add(Integer.parseInt(arr[j]));

			m.averageVector = newMat;
			i++;
			inpNew = new String();
			inpNew = allData.get(i);
			String arr2[] = inpNew.split(" ");
			m.averageCode = arr2[0];
			matrix.add(m);
		}
	}

	public void printCodeBook() {
		for (int i = 0; i < matrix.size(); i++) {
			System.out.println(matrix.get(i).averageVector);
			System.out.println(matrix.get(i).averageCode);
		}
	}

	public void printDecompressedImage() {
		int size = imageWidth / vectorWidth;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(decompressedImage[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void writeDecompressedImage(String[][] decompressedImage2)
			throws IOException {
		FileWriter out = new FileWriter("compressed.txt");
		File file = new File("E:/VectorQuantization");
		int size = imageWidth / vectorWidth;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				out.write(decompressedImage2[i][j] + " ");
			}
			out.write("\n");
		}
		out.write("587");
		out.write("\n");
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).averageVector.size(); j++)
				out.write(matrix.get(i).averageVector.get(j).toString() + " ");
			out.write("\n");
			out.write(matrix.get(i).averageCode);
			out.write("\n");
		}
		out.close();
	}

	public void compression(String path) throws Exception {
		makeBlocks(path);
		ArrayList<Integer> averageVector = getAverageVector(vectors);
		split(averageVector); //splitted --> 2
		getNearest(splitted); //matrix arr->nearst->average  2->nearst

		while (matrix.size() != codeBookSize) {
			splitted.clear();
			for (int i = 0; i < matrix.size(); i++) {
				split(matrix.get(i).averageVector);
			}
			getNearest(splitted);
		}

		int counter = 0;
		while (counter < 10) {
			splitted.clear();
			for (int i = 0; i < matrix.size(); i++) {
				splitted.add(matrix.get(i).averageVector);
			}
			getNearest(splitted);
			counter++;
		}
		setAverageCode();
		decompressedImage = getCompressedImage();
		writeDecompressedImage(decompressedImage);

	}

	private ArrayList<Integer> search(String x) {
		for (int i = 0; i < matrix.size(); i++) {
			if (matrix.get(i).averageCode.equals(x)) {
				return matrix.get(i).averageVector;
			}
		}
		return null;
	}

	public void decompression(String path) throws Exception {
		matrix.clear();
		vectors.clear();
		decompressedImage = null;
		readDecompressedImage(path);
		ArrayList<int[][]> image = new ArrayList<>();
		int finalImage[][] = new int[imageWidth][imageHeight];
		for (int m = 0; m < decompressedImage.length; m++) {
			for (int c = 0; c < decompressedImage.length; c++) {
				String xo = decompressedImage[c][m];
				ArrayList<Integer> newBlock = search(xo);
				int image2[][] = new int[vectorWidth][vectorHeight];
				int k = 0;
				for (int i = 0; i < vectorWidth; i++) {
					for (int j = 0; j < vectorHeight; j++) {
						image2[i][j] = newBlock.get(k);
						k++;
					}
				}
				image.add(image2);
			}
		}

		int count1 = 0, count2 = 0, count3 = 0, count4 = 0;
		for (int i = 0; i < image.size();) {
			for (int x = 0; x < imageWidth; x += imageWidth) {
				for (int y = 0; y < imageHeight; y += imageHeight) {
					int temp[][] = image.get(i);
					i++;
					for (int a = x; a < x + vectorWidth; a++) {
						for (int b = y; b < y + vectorHeight; b++) {
							finalImage[count1][count2] = temp[a][b];
							count2++;
						}
						count1++;
						count4++;
						count2 = count3;
						if (count4 % (decompressedImage.length * 2) == 0
								&& count4 != 0) {
							count1 = 0;
							count3 += vectorWidth;
							count2 = count3;
						}
					}
				}
			}
		}

		writeImage(finalImage, imageHeight, imageWidth);
	}

	public static void writeImage(int image[][], int width, int height) {
		File fileout = new File("decompressed.png");
		BufferedImage image2 = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image2.setRGB(x, y, (image[x][y] << 16) | (image[x][y] << 8)
						| (image[x][y]));
			}
		}
		try {
			ImageIO.write(image2, "jpg", fileout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
