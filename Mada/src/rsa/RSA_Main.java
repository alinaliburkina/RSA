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
		BigInteger i = fiOfN(n);
		BigInteger index = i.subtract(BigInteger.ONE);
		while (flag && index.compareTo(BigInteger.ONE) > 0) {
			BigInteger[] array = euclidAlgorithm(index, i);
			// if ggT(index,fiOfN)==1
			//array[0]==1
			if (array[0].compareTo(BigInteger.ONE)==0) {
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

		// 2nd Loop
		// b!=0
		while (b.compareTo(BigInteger.ZERO) != 0) {
			q = a.divide(b);
			r = a.mod(b);
			a = b;
			b = r;
			x0 = x1;
			y0 = y1;
			x1 = x0.subtract(q.multiply(x1));
			y1 = y0.subtract(q.multiply(y1));
		}

		BigInteger[] array = new BigInteger[3];
		array[0] = a;
		array[1] = x0;
		array[2] = y0;
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
				h = h * k % n.intValue();
				k = k * k % n.intValue();
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
		BigInteger N = rsa.generateN();
		BigInteger E = rsa.generateE(N);
		BigInteger D = rsa.generateD(E,N);
	
		BigInteger[] eucAlg = rsa.euclidAlgorithm(rsa.primeNumber(), N);
		System.out.println(eucAlg[2]);

	}

}
