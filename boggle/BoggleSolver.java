import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
public class BoggleSolver
{
	private	TrieST<Integer> wordTree; 
	private int n;
	private BoggleBoard board;
	private Bag<Integer>[] adj; 
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
	{
		wordTree = new TrieST<Integer>();
		for (int i = 0; i < dictionary.length; i++)
			wordTree.put(dictionary[i], i);
		n = 0;
		//StdOut.println(wordTree.size());
	}

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
	{
		SET<String> q = new SET<String>();
		this.board = board;

		int m = board.rows();
		n = board.cols();
		boolean[] marked = new boolean[m*n+1];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				marked[i*n+j] = false;
		marked[m*n] = false;

		@SuppressWarnings({"rawtypes","unchecked"})	Bag<Integer>[] adjTmp = (Bag<Integer>[]) new Bag[m*n+1];
		adj = adjTmp;
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++) 
				adj[i*n+j] = new Bag<Integer>();
		adj[m*n] = new Bag<Integer>();
		
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++) {
				if (i != 0 && j != 0)		adj[i*n+j+1].add((i-1) * n + j-1 + 1);
				if (i != 0 && j != n-1)		adj[i*n+j+1].add((i-1) * n + j+1 + 1); 
				if (i != m-1 && j != 0)		adj[i*n+j+1].add((i+1) * n + j-1 + 1);
				if (i != m-1 && j != n-1)	adj[i*n+j+1].add((i+1) * n + j+1 + 1); 
				if (i != 0)					adj[i*n+j+1].add((i-1) * n + j + 1);
				if (i != m-1)				adj[i*n+j+1].add((i+1) * n + j + 1);
				if (j != 0)					adj[i*n+j+1].add(i * n + j-1 + 1); 
				if (j != n-1)				adj[i*n+j+1].add(i * n + j+1 + 1); 
				adj[0].add(i * n + j + 1);
			}
		//StdOut.printf("index adjcent to vertex 0:");
		//for ( int k : adj[0])
		//	StdOut.print(" " + k);
		//StdOut.println();
	//	for (int i = 0; i < m; i++)
	//		for (int j = 0; j < n; j++) {
	//			StdOut.printf("index adjecent to (%d, %d) : ", i, j);
	//			for (int k : adj[i*n+j+1])
	//				StdOut.print(" " + k);
	//			StdOut.println();
	//		}
		dfs(0, marked, q);
		return q;
	}

	private void dfs(int v, boolean[] marked, SET<String> q)
	{
		marked[v] = true;
		String s = "";
		for (int w : adj[v])
			dfs(w, marked, q, s);
		marked[v] = false;
	}

	private void dfs(int v, boolean[] marked, SET<String> q, String s)
	{
		//StdOut.println("check: " + v);
		marked[v] = true;
		char c = board.getLetter((v-1)/n, (v-1)%n);
		if (c == 'Q') 
			s += "QU";
		else
			s += String.valueOf(c);
		//StdOut.println(s);
		if (wordTree.contains(s) && s.length() > 2 && !q.contains(s))
			q.add(s);
		if (!wordTree.containPrefix(s))	{ marked[v] = false;	return;	}
		for (int w : adj[v]) {
		//	StdOut.println("adjacent index: " + w);
			if (!marked[w]) { 
				dfs(w, marked, q, s);
			}
		}
		marked[v] = false;
	}


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
	{
		if (!wordTree.contains(word) || word.length() < 3)	return 0;
		if (word.length() <= 4) return 1;
		if (word.length() == 5) return 2;
		if (word.length() == 6) return 3;
		if (word.length() == 7) return 5;
		return 11;
	}

	public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard(args[1]);
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
        StdOut.println(word);
        score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);

	Stopwatch timer = new Stopwatch();
	for (int i = 0; i < 10000; i++)
		solver.getAllValidWords(board);
	double time = timer.elapsedTime();
	StdOut.printf("%.2f calls per second.\n", 10000/time);
}
}
