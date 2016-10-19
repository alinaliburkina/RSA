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

/**
 * @author Alina Liburkina, Biraveenmaks Ponnu, Raphael Zumbrunnen
 */

public class RSA_Main {

	private BigInteger q;
	private BigInteger p;

	// Generates a prime number
	private BigInteger primeNumber() {
		return new BigInteger(1024, 100, new Random());
	}

	// Generates n
	private BigInteger generateN() {
		q = primeNumber();
		p = primeNumber();
		if (!q.equals(p))
			return p.multiply(q);
		return null;

	}

	public BigInteger phiOfN() {
		return (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
	}

	// Generates E which belongs to Z of phi
	public BigInteger generateE(BigInteger phi) {

		boolean found = false;
		BigInteger k = phi.subtract(BigInteger.ONE);

		while (!found) {

			if ((euclidAlgorithm(k, phi)[0]).compareTo(BigInteger.ONE) == 0) {
				return k;
			}

			k = k.subtract(BigInteger.ONE);
		}

		return null;
	}

	// Calculates d by using of euclid-algorithm
	// d = y0 y0 is at the position 2
	private BigInteger generateD(BigInteger n, BigInteger e) {

		return euclidAlgorithm(e, n)[2];
	}

	// Calculates by using of Euclid algorithm ggT(e,n) and
	// Bezout-coefficient and returns them in the array. Position one is for
	// ggT(e,n), position2 and position3 for coefficients.

	private BigInteger[] euclidAlgorithm(BigInteger phiOfn, BigInteger e) {

		// 1st Initialization
		BigInteger a = e;
		BigInteger b = phiOfn;
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

		// Konntrolliere ob d kleiner als O ist. Wenn ja, d = d + phi
		if (y0.compareTo(BigInteger.ZERO) == -1) {
			y0 = y0.add(e);
		}
		// Konntrolliere ob d gösser als phi ist. Wenn ja, d = d - phi
		if (y0.compareTo(e) == 1) {
			y0 = y0.subtract(e);
		}

		BigInteger[] array = new BigInteger[3];
		array[0] = a;
		array[1] = x0;
		array[2] = y0;
		return array;
	}

	public BigInteger fastExponention(BigInteger x, BigInteger e, BigInteger m) {

		// String binaryE = e.toString(2);
		String binaryE = e.toString(2);
		int i = binaryE.length() - 1;
		BigInteger h = BigInteger.ONE;
		BigInteger k = x;

		while (i >= 0) {
			if (binaryE.charAt(i) == '1') {
				h = h.multiply(k).mod(m);
			}
			k = k.multiply(k).mod(m);
			i = i - 1;
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

	public void encryptFile(String pathToWriteSecretKey, String pathToWritePublicKey, String pathToReadText,
			String pathToWriteTo) {

		BigInteger N = generateN();
		BigInteger E = generateE(N);
		BigInteger D = generateD(N, E);

		String keyPairSecret = "(" + N.toString() + "," + D.toString() + ")";
		String keyPairPublic = "(" + N.toString() + "," + E.toString() + ")";

		writeToFile(pathToWriteSecretKey, keyPairSecret);
		writeToFile(pathToWriteSecretKey, keyPairPublic);

		String textTXT = readFile(pathToReadText);

		int[] askiiArrayTextTXT = new int[textTXT.length()];
		BigInteger[] encrtedTextTXT = new BigInteger[textTXT.length()];
		StringBuilder ecryptedTextTXTString = new StringBuilder();

		for (int i = 0; i < askiiArrayTextTXT.length - 1; i++) {
			askiiArrayTextTXT[i] = textTXT.charAt(i);
			encrtedTextTXT[i] = fastExponention(BigInteger.valueOf(askiiArrayTextTXT[i]), E, N);
			ecryptedTextTXTString.append(encrtedTextTXT[i] + ",");

		}
		writeToFile(pathToWriteTo, ecryptedTextTXTString.toString());

	}

	// Decrypts a given text chiffre.txt with a given secret key sk.txt and
	// writes the result into a file text-d
	public void decryptFile(String pathToReadKey, String pathToReadEcryptedText, String pathToWriteTo) {
		String skString = readFile(pathToReadKey);
		BigInteger N = new BigInteger(deserealizer(skString)[0]);
		BigInteger D = new BigInteger(deserealizer(skString)[1]);

		String chiffreTXTString = readFile(pathToReadEcryptedText);

		String[] chiffreTXTStringArray = chiffreTXTString.split(",");
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < chiffreTXTStringArray.length - 1; i++) {

			sb.append((char) fastExponention(new BigInteger(chiffreTXTStringArray[i]), D, N).intValue() + ",");
		}
		String[] strArrTmp = sb.toString().split(",");
		StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < strArrTmp.length; i++) {
			sb2.append(strArrTmp[i]);
		}
		writeToFile(pathToWriteTo, sb2.toString());
	}

	public static void main(String[] args) {
		RSA_Main rsa = new RSA_Main();
	
		String pathGiven ="C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataGiven\\";
		String pathOwn ="C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataOwnKeyPairs\\";
		// rsa.decryptFile("C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataGiven\\sk.txt",
		// "C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataGiven\\chiffre.txt",
		// "C:\\Users\\Alina\\git\\RSA_Mada\\Mada\\src\\dataGiven\\text-d.txt");

	
		rsa.encryptFile(pathOwn+"sk.txt", pathOwn+"pk.txt", pathOwn+"text.txt", pathOwn+"chiffre.txt");
		rsa.decryptFile(pathOwn+"sk.txt", pathOwn+"chiffre.txt", pathOwn+"text-d.txt");

	}

}
