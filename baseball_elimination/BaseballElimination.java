import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import java.util.Map;
public class BaseballElimination {

	private int teams;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	private String[] name;
	private boolean[] isEliminated;
	private Queue<String>[] eliminateSubset;

	public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
	{
		In in = new In(filename);
		teams = in.readInt();
		w = new int[teams];
		l = new int[teams];
		r = new int[teams];
		g = new int[teams][teams];
		@SuppressWarnings({"unchecked","rawtypes"}) Queue<String>[] q = new Queue[teams];
		eliminateSubset = q;
		for (int i = 0; i < teams; i++)
			eliminateSubset[i] = new Queue<String>();
		name = new String[teams];
		isEliminated = new boolean[teams];
		for (int i = 0; i < teams; i++)
			isEliminated[i] = false;

		for (int i = 0; i < teams; i++) {
			name[i] = in.readString();
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			for (int j = 0; j < teams; j++) {
				g[i][j] = in.readInt();
			}
		}
		
		int gameVerticesNumber = (teams - 1) * (teams - 2) / 2;
		int teamVerticesNumber = teams - 1;
		int v = gameVerticesNumber + teamVerticesNumber + 2;
		for (int m = 0; m < teams; m++) {
			eliminateSubset[m] = new Queue<String>();
			boolean trivialSolved = false;
			for (int i = 0; i < teams; i++)
				if (i != m && (w[m]+r[m]<w[i])) {
					isEliminated[m] = true;
					trivialSolved = true;
					eliminateSubset[m].enqueue(name[i]);
					break;
				}
			if (!trivialSolved) {
					
			FlowNetwork n = new FlowNetwork(v);
			// vertices counter
			int capacityFromS = 0;
			int count = 0;
			for (int i = 0; i < teams; i++)
				for (int j = i + 1; j < teams; j++)
					if (i != m && j != m) {
						// add edge adj to s
					    FlowEdge edge =	new FlowEdge(0, ++count, g[i][j]);
						n.addEdge(edge);
						// capacity of edges from s
						capacityFromS += g[i][j];	
						// add edge from game to team
						edge = new FlowEdge(count, 1+gameVerticesNumber+(i>=m?i-1:i), Double.POSITIVE_INFINITY);
						n.addEdge(edge);
						edge = new FlowEdge(count, 1+gameVerticesNumber+(j>=m?j-1:j), Double.POSITIVE_INFINITY);
						n.addEdge(edge);
					}
			// add edge from team to t
			int t = v - 1;
			for (int i = 0; i < teams; i++)
				if (i != m) {
					//StdOut.println(w[m] + " " + r[m] + " " + w[i]);
					n.addEdge(new FlowEdge(++count, t, Math.max(w[m]+r[m]-w[i],0)));
				}

			FordFulkerson ff = new FordFulkerson(n, 0, t);
			if (Math.abs(ff.value() - capacityFromS) > 0.01) isEliminated[m] = true;
			
			for (int i = 1+gameVerticesNumber; i < t; i++)
				if (ff.inCut(i)) {
					int nameIndex = i - (1 + gameVerticesNumber);
					nameIndex += (nameIndex >= m ? 1 : 0);
					eliminateSubset[m].enqueue(name[nameIndex]);
				}
			}
		}

	}

	private int stringToIndex(String s)
	{
		int index = 0;
		boolean finds = false;
		for (int i = 0; i < teams; i++)
			if (s.equals(name[i])) {
				index = i;
				finds = true;
			}
		if (!finds) throw new java.lang.IllegalArgumentException();
		return index;
	}

	public int numberOfTeams()                        // number of teams
	{	return teams;	}
	
	public Iterable<String> teams()                                // all teams
	{	
		Queue<String> t = new Queue<String>();
		for (int i = 0; i < teams; i++)
			t.enqueue(name[i]);
		return t;
	}

	public int wins(String team)                      // number of wins for given team
	{	return w[stringToIndex(team)];	}	

	public int losses(String team)                    // number of losses for given team
	{	return l[stringToIndex(team)];	}	

	public int remaining(String team)                 // number of remaining games for given team
	{	return r[stringToIndex(team)];	}	

	public int against(String team1, String team2)    // number of remaining games between team1 and team2
	{	return g[stringToIndex(team1)][stringToIndex(team2)];	}

	public boolean isEliminated(String team)              // is given team eliminated?
	{	return isEliminated[stringToIndex(team)];	}	

	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		if (eliminateSubset[stringToIndex(team)].isEmpty())	return null;
		return eliminateSubset[stringToIndex(team)];	
	}


	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}

