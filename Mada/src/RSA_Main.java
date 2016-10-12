import java.math.BigInteger;
import java.util.Random;

public class RSA_Main {

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

	//Generates n
	private BigInteger generateN() {
		BigInteger q = primeNumber();
		BigInteger p = primeNumber();
		if (!q.equals(p))
			return p.multiply(q);
		return null;

	}

	public static void main(String[] args) {
		RSA_Main rsa = new RSA_Main();
		BigInteger primeNumber;

		System.out.println(rsa.generateProbablePrimeNumber());

	}

}
