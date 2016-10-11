package ernie.ripley;

import static kiss.API.*;

//Test driven design
//Methods for testing: only add new tests: test falures and truths

public class App{
	
	public class Primes{
		boolean isPrime(int x){
			for(int factor = 2; factor <= (int)(sqrt(x) + 1.0); ++factor)
				if(x % factor == 0) return false;
			return true;
		}
	}
	
	void testSmallPrimes(){
		Primes primes = new Primes();
		assert primes.isPrime(2) == true;
		assert primes.isPrime(3) == true;
		assert primes.isPrime(5) == true;
	}
	
	void testSmallComposites(){
		Primes primes = new Primes();
		assert primes.isPrime(4) == false;
		assert primes.isPrime(6) == false;
	}

	void testBigPrimes(){
		Primes primes = new Primes();
		double t0 = time();
		assert primes.isPrime(1_000_003);
		double smallTime = time() - t0;
		
		double t1 = time();
		assert primes.isPrime(1_000_000_007) == true;
		double bigTime = time() - t1;
		
		assert (bigTime < 100 * smallTime);
	}
	
	void run(){
	}
}
