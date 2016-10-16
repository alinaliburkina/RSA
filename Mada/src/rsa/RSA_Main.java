package rsa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class RSA_Main {

	private String m_string;

	// Generates a prossible prime number
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

	public int fiOfN(BigInteger n) {
		return n.intValueExact() - 1;
	}

	// Generates E which belongs to Z of Fi
	private int generateE(BigInteger n) {
		boolean flag = false;
		int i = fiOfN(n);
		int index = i - 1;
		while (flag && i > 1) {
			int[] array = euclidAlgorithm(index, n);
			// if ggT(index,n)==1
			if (array[0] == 1) {
				flag = true;
				return index;
			}
			i--;
		}

		return 1;
	}

	// Calculates d by using of euclid-algorithm
	private int generateD(BigInteger n, int e) {

		return euclidAlgorithm(e, n)[2];
	}

	// Calculates by using of Euclid algorithm ggT(e,n) and
	// Bezout-coefficient and returns them in the array. Position one is for
	// ggT(e,n), position2 and position3 for coefficients.

	private int[] euclidAlgorithm(int e, BigInteger n) {

		// 1st Initialization
		int a = e;
		int b = n.intValueExact();
		int x0 = 1;
		int x1 = 0;
		int y0 = 0;
		int y1 = 1;
		int q = a / b;
		int r = a % b;

		// 2nd Loop
		while (b != 0) {
			q = a / b;
			r = a % b;
			a = b;
			b = r;
			x0 = x1;
			y0 = y1;
			x1 = x0 - q * x1;
			y1 = y0 - q * y1;
		}

		int[] array = new int[3];
		array[0] = a;
		array[1] = x0;
		array[2] = x1;
		return array;
	}

	public int fastExponention(int x, BigInteger n, int e) {
		String binaryE = Integer.toBinaryString(e);
		int i = Integer.toBinaryString(e).length() - 1;
		int[] binaryEArray = new int[i + 1];
		for (int j = 0; j < binaryEArray.length; j++) {
			binaryEArray[j] = Integer.valueOf(binaryE.substring(j, j + 1));
		}
		int h = 1;
		int k = x;
		int index = 0;
		while (i >= 0) {
			if (binaryEArray[index] == 1) {
				h = h * k % n.intValueExact();
				k = k * k % n.intValueExact();
				i = i - 1;
			}
			index++;
		}
		return h;

	}

	// Writes to sk.txt file keypair (n,d)
	public void saveSecretKey(String keyPair) {
		writeToFile("sk.txt", keyPair);
	}

	// Writes to pk.txt file keypair (e,d)
	public void savePublicKey(String keyPair) {
		writeToFile("pk.txt", keyPair);
	}

	// Reads string to be decrypted from file
	public void readFile() {
		try (BufferedReader br = new BufferedReader(new FileReader("text.txt"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			m_string = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Writes public and private keys
	public void writeToFile(String fileName, String keyPair) {

		if (fileName.equals("sk.txt") || fileName.equals("pk.txt")) {

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

	}

	public static void main(String[] args) {
		RSA_Main rsa = new RSA_Main();
		BigInteger primeNumber;
		String binaryE = "01234";

		System.out.println(Integer.valueOf(binaryE.substring(1, 2)));

	}

}
