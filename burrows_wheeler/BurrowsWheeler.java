import edu.princeton.cs.algs4.*;
import java.util.Arrays;
public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode()
	{
		String s = BinaryStdIn.readString();
		int length = s.length();
		CircularSuffixArray csa = new CircularSuffixArray(s);
		int first = 0;
		for (int i = 0; i < length; i++) {
			if (csa.index(i) == 0) first = i;
		}
		BinaryStdOut.write(first);
		for (int i = 0; i < length; i++) {
			int oriIndex = csa.index(i);
			int ouputIndex = oriIndex == 0 ? length - 1 : oriIndex - 1;
			BinaryStdOut.write(s.charAt(ouputIndex));
		}
		BinaryStdOut.close();
	}

	private static class Pair implements Comparable<Pair>
	{
		private int value;
		private char key;
		Pair(int value, char key)
		{
			this.value = value;
			this.key = key;
		}

		public int value()
		{	return value;	}
		
		public int compareTo(Pair that)
		{	return this.key - that.key;	}
	}
		

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode()
	{
		int first = BinaryStdIn.readInt();
		String s = BinaryStdIn.readString();
		int length = s.length();
		int[] next = new int[length];
		char[] sOri = s.toCharArray();
		char[] sSort = Arrays.copyOf(sOri, length);
		Arrays.sort(sSort);

		Pair[] p = new Pair[length];
		for (int i = 0; i < length; i++)
			p[i] = new Pair(i, sOri[i]);
		Arrays.sort(p);
		for (int i = 0; i < length; i++)
			next[i] = p[i].value();
	//	// 防止多个next指向同一个元素
	//	boolean[] mark = new boolean[length];
	//	for (boolean i : mark)
	//		i = false;
	//	// construct next array.
	//	for (int i = 0; i < length; i++)
	//		for (int j = 0 ; j < length; j++) {
	//			if (sOri[j] == sSort[i] && mark[j] == false) {
	//				next[i] = j;
	//				mark[j] = true;	
	//				break;
	//			}
	//		}
//		for (char i : sSort)
//			StdOut.print(i + " ");
//		StdOut.println();
		//StdOut.println("first" + first);
		for (int i = 0, cur = first; i < length; i++) {
			BinaryStdOut.write(sSort[cur]);
			//StdOut.printf("%c", sSort[cur]);
			cur = next[cur];
		}
		BinaryStdOut.close();
			
	}

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args)
	{
		if (args[0].equals("-")) encode();
		else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
	}
}
