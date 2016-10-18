package rsa;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import org.w3c.dom.Text;

public class RSA_Main {

	private String m_string;

	// Generates a possible prime number
	private BigInteger generateProbablePrimeNumber() {
		return BigInteger.probablePrime(1000, new Random());
	}

	// Checks if a generated number is a prime number and returns a prime
	// number.
	// In the way of failure calls itself to make the next check
	private BigInteger primeNumber() {
		BigInteger bigInt = generateProbablePrimeNumber();
		if (bigInt.isProbablePrime(20))
			return bigInt;
		else {
			return primeNumber();
		}
	}

	// Generates n
	private BigInteger generateN() {
		BigInteger q = primeNumber();
		BigInteger p = primeNumber();
		if (!q.equals(p))
			return p.multiply(q);
		return null;

	}

	public BigInteger fiOfN(BigInteger n) {
		return n.subtract(BigInteger.ONE);
		// return Math.abs(n.intValue()) - 1;
	}

	// Generates E which belongs to Z of Fi
	private BigInteger generateE(BigInteger n) {
		boolean flag = false;
		BigInteger foOfN = fiOfN(n);
		BigInteger index = foOfN.subtract(BigInteger.ONE);
		while (!flag && index.compareTo(BigInteger.ONE) > 0) {
			BigInteger[] array = euclidAlgorithm(index, foOfN);
			// if ggT(index,fiOfN)==1
			// array[0]==1
			if (array[0].equals(BigInteger.ONE)) {
				flag = true;
				return index;
			}

			// index--;
			index = index.subtract(BigInteger.ONE);
		}

		return BigInteger.ONE;
	}

	// Calculates d by using of euclid-algorithm
	// d = y0 y0 is at the position 2
	private BigInteger generateD(BigInteger n, BigInteger e) {

		return euclidAlgorithm(e, n)[2];
	}

	// Calculates by using of Euclid algorithm ggT(e,n) and
	// Bezout-coefficient and returns them in the array. Position one is for
	// ggT(e,n), position2 and position3 for coefficients.

	private BigInteger[] euclidAlgorithm(BigInteger e, BigInteger n) {

		// 1st Initialization
		BigInteger a = e;
		BigInteger b = n;
		BigInteger x0 = BigInteger.ONE;
		BigInteger x1 = BigInteger.ZERO;
		BigInteger y0 = BigInteger.ZERO;
		BigInteger y1 = BigInteger.ONE;
		BigInteger q = a.divide(b);
		BigInteger r = a.mod(b);
		BigInteger x0tmp = x0;
		BigInteger y0tmp = y0;

		// 2nd Loop
		// b!=0
		while (b.compareTo(BigInteger.ZERO) != 0) {
			q = a.divide(b);
			r = a.mod(b);
			a = b;
			b = r;
			x0tmp = x0;
			y0tmp = y0;
			x0 = x1;
			y0 = y1;
			x1 = x0tmp.subtract(q.multiply(x1));
			y1 = y0tmp.subtract(q.multiply(y1));
		}

		BigInteger[] array = new BigInteger[3];
		array[0] = a;
		array[1] = x0;
		array[2] = y0.abs();
		return array;
	}

	   public  int fastExponention(BigInteger x, BigInteger e, BigInteger m) {

	        //String binaryE = e.toString(2);
	        String binaryE = e.toString(2);
	        int i = binaryE.length()-1;
	        int h = 1;
	        int k = x.intValue();

	        while(i>=0){
	            if(binaryE.charAt(i)=='1'){
	                h = (h * k) % m.intValue();
	            }
	            k = (int)((Math.pow(k,2))% m.intValue());
	            i = i-1;
	        }

	        return h;

	    }


	// Writes to sk.txt file keypair (n,d)
	public void saveSecretKey(BigInteger n, BigInteger d) {
		String keyPair = "(" + n + "," + d + ")";
		writeToFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataOwnKeyPairs\\sk.txt", keyPair);
	}

	// Writes to pk.txt file keypair (e,d)
	public void savePublicKey(BigInteger n, BigInteger e) {
		String keyPair = "(" + n + "," + e + ")";
		writeToFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataOwnKeyPairs\\pk.txt", keyPair);
	}

	// Reads string to be decrypted from file
	// Please give tha path in form of "C:\\ ..."
	public String readFile(String path) {
		String string = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			string = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

	// Deserializes a keyPair from sk.txt pair and returns an String array.
	// Elemet 0 is N and lement1 is D.
	public String[] deserealizer(String keyPair) {
		// keyPair: (5146541481464164317,456456546563966576387643)
		String deserealizedString = keyPair.trim().substring(1, keyPair.length() - 2);
		return deserealizedString.split(",");

	}

	// Writes public and private keys
	public void writeToFile(String fileName, String keyPair) {

		BufferedWriter output = null;
		try {
			File file = new File(fileName);
			output = new BufferedWriter(new FileWriter(file));
			output.write(keyPair);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) {
		RSA_Main rsa = new RSA_Main();


		String textTXT = rsa.readFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataGiven\\chiffre.txt");
		String DString = rsa.readFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataGiven\\sk.txt");
		String string = rsa.readFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataOwnKeyPairs\\pk.txt");
		BigInteger N = new BigInteger(rsa.deserealizer(DString)[0]);
//		BigInteger E = new BigInteger(rsa.deserealizer(string)[1]);
		BigInteger D = new BigInteger(rsa.deserealizer(DString)[1]);
		
		String[] stringArrayText = textTXT.split(",");
		for (int i = 0; i < stringArrayText.length-1; i++) {
			System.out.print(rsa.fastExponention(new BigInteger(stringArrayText[i]), D, N)+" ");
		}
		
//		int[] askiiArrayTextTXT = new int[textTXT.length()];
//		int[] encrtedTextTXT = new int[textTXT.length()];
//		StringBuilder ecryptedTextTXTString = new StringBuilder();
//		
//		// ************************encrypt text.txt***************************************
//		for (int i = 0; i < askiiArrayTextTXT.length - 1; i++) {
//			askiiArrayTextTXT[i] = textTXT.charAt(i);
//			encrtedTextTXT[i] = rsa.fastExponention(askiiArrayTextTXT[i], E, N);
//			ecryptedTextTXTString.append(Math.abs(encrtedTextTXT[i]) + ",");
//
//			System.out.print(Math.abs(encrtedTextTXT[i]) + " ");
//		}
//		rsa.writeToFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataOwnKeyPairs\\chiffre.txt",
//				ecryptedTextTXTString.toString());
//
//		// *******************************************************************************
//		
//		System.out.println(); 
//		
//		
//		// decrypt text.txt
//		String chiffreTXTString = rsa.readFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataOwnKeyPairs\\chiffre.txt");
//
//		String[] chiffreTXTStringArray = chiffreTXTString.split(",");
//
//		for (int i = 0; i < chiffreTXTStringArray.length - 1; i++) {
//			System.out.print(rsa.fastExponention(Integer.parseInt(chiffreTXTStringArray[i]), D, N) + " ");
//		}
		
		
	}

}
