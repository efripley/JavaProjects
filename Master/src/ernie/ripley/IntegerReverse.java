package ernie.ripley;

import java.util.Comparator;

public class IntegerReverse implements Comparator<Integer>{

	@Override
	public int compare(Integer a, Integer b){
		return -a.compareTo(b);
	}

}
