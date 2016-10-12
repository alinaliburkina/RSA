import java.math.BigInteger;
import java.util.Random;

public class RSA_Main {

	private BigInteger generateProbablePrimeNumber() {
		return BigInteger.probablePrime(1000, new Random());
	}

	private BigInteger primeNumber() {
		BigInteger bigInt = generateProbablePrimeNumber();
		if (bigInt.isProbablePrime(20))
			return bigInt;
		else {
			return primeNumber();
		}
	}

	private BigInteger generateN() {
		BigInteger q = primeNumber();
		BigInteger p = primeNumber();
		if (q.equals(p))
			generateN();
		return p.multiply(q);
	}

	public static void main(String[] args) {
		RSA_Main rsa = new RSA_Main();
		BigInteger primeNumber;

		System.out.println(rsa.primeNumber());

	}

}
