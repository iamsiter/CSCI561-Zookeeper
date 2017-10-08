package com.mana;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class homework{

	static class State {

		List<Dimension> configuration;
		int presentColumn;
		int lizCount;

		public int getPresentColumn() {
			return presentColumn;
		}

		public void setPresentColumn(int presentColumn) {
			this.presentColumn = presentColumn;
		}

		public int getLizCount() {
			return lizCount;
		}

		public void setLizCount(int lizCount) {
			this.lizCount = lizCount;
		}

		public State(List<Dimension> configuration, int lizCount, int presentColoumn) {
			this.configuration = configuration;
			this.presentColumn = presentColoumn;
			this.lizCount = lizCount;
		}

		public List<Dimension> getConfiguration() {
			return configuration;
		}

		public void setConfiguration(List<Dimension> configuration) {
			this.configuration = configuration;
		}

		public String toString() {
			return "State:" + configuration.toString();

		}

		public State getRandomState() {
			List<Dimension> newConfig = new ArrayList<Dimension>(this.getConfiguration());
			Random rand = new Random();
			boolean flag = false;
			Dimension dummy = new Dimension(0,0);
            while (!flag) {
					int x = rand.nextInt(homework.size);
					int y = rand.nextInt(homework.size);
					dummy.setCol(y);
					dummy.setRow(x);
					if (!homework.treeList.contains(dummy) && !this.configuration.contains(dummy)) {
						flag = true;
					}
				}
				newConfig.remove(rand.nextInt(homework.lizards));
				newConfig.add(new Dimension(dummy.getRow(), dummy.getCol()));
				return new State(newConfig, 0, 0);
		}


		public int getCost() {
			List<Dimension> lizConfig = this.getConfiguration();
			int cost = 0;
			for (int i = 0; i < homework.lizards; i++) {
				Dimension q1 = lizConfig.get(i);
				for (int j = i + 1; j < homework.lizards; j++) {
					Dimension q2 = lizConfig.get(j);
					boolean rowMatch = (q1.getRow() == q2.getRow()) ? true : false;
					boolean colMatch = (q1.getCol() == q2.getCol()) ? true : false;
					boolean diagMatch = (Math.abs(q1.getRow() - q2.getRow()) == Math
							.abs(q1.getCol() - q2.getCol())) ? true : false;
					//Check if tree lies in the path
					if (rowMatch) {
						int min = Math.min(q1.getCol(), q2.getCol());
						int max = Math.max(q1.getCol(), q2.getCol());
						boolean flag = false;
						
						for (int k = min; k < max; k++) {
							if (treeList.contains(new Dimension(q1.getRow(), k)))
							{
								flag = true; // tree encountered
							}
						}
						if (!flag)
							cost++;
					}

					if (diagMatch) {
						int minColFrom = 0;
						int minRow = Math.min(q1.getRow(), q2.getRow());
						int minCol = Math.min(q1.getCol(), q2.getCol());
						if (minCol == q1.getCol()) {
							minColFrom = 1;
							minRow=q1.getRow();
						} else {
							minColFrom = 2;
							minRow=q2.getRow();
						}
							int maxCol = Math.max(q1.getCol(), q2.getCol());
						
						boolean flag = false;
						while (minCol < maxCol) {
							if (treeList.contains(new Dimension(minRow, minCol))) {
								flag = true; // tree encountered
								break;
							}

							switch (minColFrom) {

							case 1:
								if (q1.getRow() < q2.getRow()) {
									minRow++;
								} else {
									minRow--;
								}
								break;

							case 2:
								if (q2.getRow() < q1.getRow()) {
									minRow++;
								} else {
									minRow--;
								}
							}
							minCol++;
						}
						if (!flag)
							cost++;
					}
					
					if (colMatch) {
						int min = Math.min(q1.getRow(), q2.getRow());
						int max = Math.max(q1.getRow(), q2.getRow());
						boolean flag = false;
						for (int k = min; k < max; k++) {
							if (treeList.contains(new Dimension(k, q1.getCol())))
							{
								flag = true; // tree encountered
							}
						}
						if (!flag)
							cost++;
					}
				}
			}
			return cost;
		}
	}

	static class Dimension {

		int row;
		int col;

		Dimension(int r, int c) {
			this.row = r;
			this.col = c;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + col;
			result = prime * result + row;
			return result;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Dimension other = (Dimension) obj;
			if (col != other.col)
				return false;
			if (row != other.row)
				return false;
			return true;
		}

		public String toString() {
			return "[" + this.row + "," + this.col + "]";

		}

	}

	static Map<Integer, List<Dimension>> trees = new HashMap<Integer, List<Dimension>>();
	static List<Dimension> treeList = new ArrayList<Dimension>();
	static int size = 0, lizards = 0;
	static String technique;
	public static final double INIT_TEMP = 1.00;
	public static final double COOLING_FACTOR = 0.0005;

	

	public static void main(String[] args) throws IOException {

		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(new File("src\\com\\mana\\input")));
			technique = bf.readLine().trim();
			size = Integer.parseInt(bf.readLine());
			lizards = Integer.parseInt(bf.readLine());
		

			String str = null;
			int r = 0, c = 0;

			// read the configuration of trees
			while ((str = bf.readLine()) != null) {
				c = 0;
				for (String s : str.split("")) {
					if (s.equalsIgnoreCase("2")) {
						List<Dimension> t = homework.trees.get(c);
						if (t == null) {
							t = new LinkedList<Dimension>();
						}
						t.add(new Dimension(r, c));
						treeList.add(new Dimension(r, c));
						homework.trees.put(Integer.valueOf(c), t);
					}
					c++;
				}
				r++;
			}

			if (r != size || c != size ||(trees.size() == 0 && lizards > size)||((size*size)-treeList.size()<lizards)) {
				// CASE:Input lines and size unequal || CASE:p>n && t=0 || CASE:n^2-t<p
				showOutput(size,null);
				return;
			}

            State s = null;

			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(291000);
						showOutput(size, null);
						System.exit(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			});
			 t.setDaemon(true);
			 t.start();
			
			 switch (technique) {
			case "BFS":
				s = performBFS(size, lizards);
				break;

			case "DFS":
				s = performDFS(size, lizards);
				break;

			case "SA":
				s = performSA(size, lizards);
				break;
			}
			showOutput(size, s == null ? null : s.getConfiguration());
		}catch (Exception e) {
			showOutput(size, null);
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				showOutput(size, null);

			}
		}
	}

	private static State performSA(int size, int lizards) {
		State nextState, currentState = null;

		try {
			List<Dimension> configuration = new ArrayList<Dimension>();
			Random rand = new Random();
			double random = 0.0;
			boolean flag = false;
			Dimension dummy = new Dimension(0,0);			
			
			for (int i = 0; i < lizards; i++) {
				while (!flag) {
					int x = rand.nextInt(size);
					int y = rand.nextInt(size);
					dummy.setCol(y);
					dummy.setRow(x);
					if (!homework.treeList.contains(dummy) && !configuration.contains(dummy)) {
						flag = true;
					}
				}
				configuration.add(new Dimension(dummy.row, dummy.col));
				flag = false;
			}

			currentState = new State(configuration, lizards, 0);
			double temperature = INIT_TEMP;
			double change;
			double prob;
			while (currentState.getCost() != 0 && temperature > 0) {
				nextState = currentState.getRandomState();
				change = currentState.getCost() - nextState.getCost();
				prob = Math.exp(change / temperature);
				random = Math.random();

				if (change > 0) {
					currentState = nextState;
					temperature=temperature-COOLING_FACTOR;
				} else if (random <= prob) {
					currentState = nextState;
				}
			}
		} catch (Exception e) {
			throw e;
		} 
		return currentState;
	}

	private static boolean isGoalState(State s) {
		if (s.getLizCount() == lizards) {
			return true;
		}
		return false;
	}

	private static State performBFS(int size, int lizards) {
		// set initial state
		Queue<State> frontiers = new LinkedList<State>();
		frontiers.add(new State(new ArrayList<Dimension>(), 0, -1));

		while (!frontiers.isEmpty()) {
			List<State> childNodes = generateChildNodes(size, frontiers.poll());

			for (State s : childNodes) {
				if (isGoalState(s)) {// Goal reached
					return s;
				} else {
					if (s.getPresentColumn() < homework.size)
						frontiers.add(s);
				}
			}
		}
		return null;
	}

	private static State performDFS(int size, int lizards) {

		// set initial state
		Stack<State> frontiers = new Stack<State>();
		frontiers.add(new State(new ArrayList<Dimension>(), 0, -1));

		while (!frontiers.isEmpty()) {
			State popped = frontiers.pop();
			List<State> childNodes = generateChildNodes(size, popped);

			Collections.reverse(childNodes);
			for (State s : childNodes) {
				if (isGoalState(s)) {// Goal reached
					return s;
				} else {
					if (s.getPresentColumn() < homework.size - 1)
						frontiers.add(s);
				}
			}
		}
		return null;
	}

	private static void showOutput(int n, List<Dimension> list) throws IOException {
		// print the output configuration
		Map<Integer, List<Dimension>> trees = homework.trees;
		StringBuilder str = new StringBuilder();
		if (list == null) {
			str.append("FAIL");
		} else {
			str.append("OK\n");
			int matrix[][] = new int[n][n];

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					matrix[i][j] = 0;
				}
			}
			for (Dimension d : list) {
				matrix[d.row][d.col] = 1;
			}
			for (List<Dimension> d : trees.values()) {
				for (Dimension dim : d) {
					matrix[dim.row][dim.col] = 2;
				}
			}

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					str.append(matrix[i][j]);
				}
				str.append("\n");
			}
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("src\\com\\mana\\output"));
			bw.write(str.toString());
			System.out.println(str);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}

	private static List<State> generateChildNodes(int n, State s) {
		List<Dimension> lizardConfig = s.getConfiguration();

		List<Dimension> candidateChildConfig = new LinkedList<Dimension>();
		List<State> childNodes = new ArrayList<State>();
		int colToFill = s.getPresentColumn() + 1;
		if (colToFill > homework.size - 1)
			return childNodes;

		List<Dimension> d = null;
		List<Dimension> treeConfig = null;
		d = homework.trees.get(Integer.valueOf(colToFill));
		if (d != null) {
			treeConfig = new ArrayList<Dimension>(d);
		}

		// generate all the candidates
		for (int i = 0; i < n; i++) {
			candidateChildConfig.add(new Dimension(i, colToFill));
		}

		// remove candidates which have trees
		for (Dimension q : lizardConfig) {
			int col = q.col + 1;
			List<Dimension> l = null;
			while (col < colToFill) {
				l = homework.trees.get(col);
				if (l != null && l.contains(new Dimension(q.row, col))) {
					break;
				} else {
					col++;
				}
			}
			l = homework.trees.get(col);

			if (col == colToFill && !(l != null && l.contains(new Dimension(q.row, col)))) {
				candidateChildConfig.remove(new Dimension(q.row, colToFill));
			}

			Dimension d1 = new Dimension(q.row, q.col);
			Dimension d2 = new Dimension(q.row, q.col);

			boolean treeTwoFound = false, treeOneFound = false;
			while ((d1.col != colToFill && d1.col <= colToFill) || (d2.col != colToFill && d1.col <= colToFill)
					|| (treeOneFound && treeTwoFound && d1.col <= colToFill)) {

				d1.setCol(d1.getCol() + 1);
				d1.setRow(d1.getRow() - 1);
				l = homework.trees.get(d1.getCol());

				if (l != null && l.contains(d1)) {
					treeOneFound = true;
				}

				d2.setCol(d2.getCol() + 1);
				d2.setRow(d2.getRow() + 1);
				l = homework.trees.get(d2.getCol());
				if (l != null && l.contains(d2)) {
					treeTwoFound = true;
				}
			}

			if (d1.col == colToFill || d2.col == colToFill) {
				if (!treeOneFound)
					candidateChildConfig.remove(d1);
				if (!treeTwoFound)
					candidateChildConfig.remove(d2);
			}
		}
		// -------------CANDIDATES FOUND---------------------
		if (candidateChildConfig.isEmpty()) {// In case no candidate is found then don't return as there may be a tree
												// in next columns **NOT A DEAD STATE**

			if (treeConfig == null || treeConfig.isEmpty())// If no tree available its a purely dead state
				return childNodes;

			int presentCol = s.getPresentColumn();
			if (presentCol < homework.size - 1) {
				s.setPresentColumn(s.getPresentColumn() + 1);
				childNodes.add(s);
			}
		} else {

			if (treeConfig != null) {
				// Refinement for optimizing
				if (treeConfig.size() == candidateChildConfig.size()) {// Initial check
					s.setPresentColumn(s.getPresentColumn() + 1);
					childNodes.add(s);
					return childNodes;
				}

				// trim candidate array if tree in end
				if (!treeConfig.isEmpty() && treeConfig.get(0).row == 0) {
					candidateChildConfig.remove(0);
					treeConfig.remove(0);
				}

				if (!treeConfig.isEmpty() && treeConfig.get(treeConfig.size() - 1).row == homework.size - 1) {
					candidateChildConfig.remove(candidateChildConfig.size() - 1);
					treeConfig.remove(treeConfig.size() - 1);
				}

				// Merge continuous trees
				d = new ArrayList<Dimension>();

				for (int k = 0; k < candidateChildConfig.size() - 1; k++) {
					if (isTree(candidateChildConfig.get(k)) && isTree(candidateChildConfig.get(k + 1))) {
						d.add(candidateChildConfig.get(k));
					}
				}

				treeConfig.removeAll(d);
				candidateChildConfig.removeAll(d);
			}
		}
		enumerateChildStates(0, candidateChildConfig, s, childNodes, treeConfig == null ? null : treeConfig,
				new ArrayList<Dimension>(s.getConfiguration()));

		return childNodes;
	}

	private static boolean isTree(Dimension dimension) {
		if (homework.treeList.contains(dimension))
			return true;
		return false;
	}

	private static void enumerateChildStates(int index, List<Dimension> candidateChildConfig, State s,
			List<State> childNodes, List<Dimension> treeConfigRcvd, List<Dimension> newConfig) {
		int nextObstacleIndex = 0;

		List<Dimension> treeConfig = null;
		if (treeConfigRcvd != null)
			treeConfig = new ArrayList<Dimension>(treeConfigRcvd);

		if (index >= candidateChildConfig.size()) {
			if (!newConfig.isEmpty()) {
				State childState = new State(newConfig, newConfig.size(), s.getPresentColumn() + 1);
				childNodes.add(childState);
			}
			return;
		}

		if (treeConfig == null || treeConfig.isEmpty()) {
			nextObstacleIndex = candidateChildConfig.size();
		} else {
			nextObstacleIndex = candidateChildConfig.indexOf(treeConfig.get(0));
			treeConfig.remove(0);
		}

		List<Dimension> myConfig = null;
		for (int i = index; i <= nextObstacleIndex; i++) {
			myConfig = new ArrayList<Dimension>(newConfig);
			if (i == nextObstacleIndex && i < candidateChildConfig.size() + 1) {// include
				enumerateChildStates(nextObstacleIndex + 1, candidateChildConfig, s, childNodes, treeConfig, myConfig);
			} else if (i != nextObstacleIndex) {
				myConfig.add(candidateChildConfig.get(i));
				enumerateChildStates(nextObstacleIndex + 1, candidateChildConfig, s, childNodes, treeConfig, myConfig);
			}
		}
	}
}
