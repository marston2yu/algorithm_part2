import edu.princeton.cs.algs4.*;

public class CircularSuffixArray {
	char[] st;
	int length;
	int[] index;
    public CircularSuffixArray(String s)  // circular suffix array of s
	{
		if (s == null) throw new java.lang.NullPointerException();
		//StdOut.println(s);
		length = s.length();
		index = new int[length];
		for (int i = 0; i < length; i++)
			index[i] = i;
		st = s.toCharArray();
		sort(index);
	}

	private void exch(int[] arr, int a, int b)
	{	int tmp = arr[a]; arr[a] = arr[b]; arr[b] = tmp;	}

	private void sort (int[] a)
	{	sort(index, 0, index.length - 1, 0);	}

	private void sort (int[] a, int lo, int hi, int d)
	{
		if (hi <= lo) return;
		int lt = lo, gt = hi;
		int vPos = index[lo] + d;
		if (vPos >= length) vPos -= length;
		int v = st[vPos];
		int i = lo + 1;
		//StdOut.print("lo: " + lo + " hi: " + hi + " d: " + d + " vPos: " + vPos + " |");
		while ( i <= gt)
		{
			int tPos = index[i] + d;	
			if (tPos >= length)	tPos -= length;
		//	StdOut.print(tPos + " ");
			int t = st[tPos];
			if (t < v) exch(a, lt++, i++);
			else if (t > v) exch(a, i, gt--);
			else i++;
		}
		//StdOut.println();
		sort(a, lo, lt-1, d);
		if (d < length) sort(a, lt, gt, d+1);
		sort(a, gt+1, hi, d);
	}

    public int length()                   // length of s
	{	return length;	}

    public int index(int i)               // returns index of ith sorted suffix
	{	
		if (i < 0 || i >= length) throw new java.lang.IndexOutOfBoundsException();
		return index[i];
	}
    public static void main(String[] args)// unit testing of the methods (optional)
	{
		String s = BinaryStdIn.readString();
		CircularSuffixArray csa = new CircularSuffixArray(s);
		StdOut.println("length: " + csa.length());
		for (int i = 0; i < s.length(); i++)
			StdOut.println(csa.index(i));
	}
}
