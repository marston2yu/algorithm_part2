import edu.princeton.cs.algs4.*;
public class MoveToFront {
	private static final int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
	{
		char[] st = new char[R];
		for (int i = 0; i < R; i++)
			st[i] = (char)i;
		while (!BinaryStdIn.isEmpty()) {
		char curCh = BinaryStdIn.readChar();
		int pos;
		for (pos = 0;  pos < R; pos++)
			if (st[pos] == curCh) {
				break;
			}
		// 输出8位
		BinaryStdOut.write((char)pos);
		System.arraycopy(st, 0, st, 1, pos);
		st[0] = curCh;
		}
		BinaryStdOut.close();
	}


    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
	{
		char[] st = new char[R];
		for (int i = 0; i < R; i++)
			st[i] = (char)i;
		while(!BinaryStdIn.isEmpty()) {
			char pos = BinaryStdIn.readChar();
			char curCh = st[pos];
			BinaryStdOut.write(curCh);
			System.arraycopy(st, 0, st, 1, pos);
			st[0] = curCh;
		}
		BinaryStdOut.close();
	}


    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
		if (args[0].equals("-")) { encode(); }
		else if (args[0].equals("+")) {	decode();	}
        else throw new IllegalArgumentException("Illegal command line argument");
	}
}
