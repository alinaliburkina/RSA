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
	
	//Writes public and private keys 
	public void writeToFile(String fileName){
		
		if(fileName.equals("sk.txt") || fileName.equals("pk.txt")){
		
		 String text = "Hello world";
	        BufferedWriter output = null;
	        try {
	            File file = new File(fileName);
	            output = new BufferedWriter(new FileWriter(file));
	            output.write(text);
	        } catch ( IOException e ) {
	            e.printStackTrace();
	        } finally {
	          if ( output != null ) {
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

		System.out.println(rsa.generateProbablePrimeNumber());

	}

}
