package com.mana;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class Zookeeper {/*

	static Map<Integer, List<Dimension>> trees = new HashMap<Integer, List<Dimension>>();
	static int size = 0, lizards = 0;

	public static void main(String[] args) {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(new File("src\\com\\mana\\input")));
			String technique = bf.readLine().trim();
			size = Integer.parseInt(bf.readLine());
			lizards = Integer.parseInt(bf.readLine());
			String str = null;
			int r = 0, c = 0;

			// read the configuration of trees
			while ((str = bf.readLine()) != null) {
				c = 0;
				for (String s : str.split(" ")) {
					if (s.equalsIgnoreCase("2")) {
						List<Dimension> t = Zookeeper.trees.get(c);
						if (t == null) {
							t = new LinkedList<Dimension>();
						}
						t.add(new Dimension(r, c));
						Zookeeper.trees.put(Integer.valueOf(c), t);
                      }
					c++;
				}
				r++;
			}

			if (r != size || c != size) {// CASE:Input lines and size unequal
				technique = "";
			}

			State s = null;

			if (trees.size() == 0 && lizards > size) {
				technique = "";// intentional exit to skip all processing
			}

			switch (technique) {
			case "BFS":
				s = performBFS(size, lizards);
				break;

			case "DFS":
				s = performDFS(size, lizards);
				break;

			case "SA":
				break;
			}

			showOutput(size, s == null ? null : s.getConfiguration());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isGoalState(State s) {
		List<Dimension> c = s.getConfiguration();
		if (c.size() == lizards && !c.contains(Dimension.DUMMY_DIMENSION)) {
			return true;
		}
		return false;
	}

	private static State performBFS(int size, int lizards) {
		// set initial state
		Queue<State> frontiers = new LinkedList<State>();
		frontiers.add(new State(new ArrayList<Dimension>()));

		while (!frontiers.isEmpty()) {
			List<State> childNodes = generateChildNodes(size, frontiers.poll());

			for (State s : childNodes) {
				if (isGoalState(s)) {// Goal reached
					return s;
				} else {
					frontiers.add(s);
				}
			}
		}
		return null;
	}

	private static State performDFS(int size, int lizards) {

		// set initial state
		Stack<State> frontiers = new Stack<State>();
		frontiers.add(new State(new ArrayList<Dimension>()));

		while (!frontiers.isEmpty()) {
			System.out.println(frontiers);
			List<State> childNodes = generateChildNodes(size, frontiers.pop());
			Collections.reverse(childNodes);
			for (State s : childNodes) {
				if (isGoalState(s)) {// Goal reached
					return s;
				} else {
					frontiers.add(s);
				}
			}
		}
		return null;
	}

	private static void showOutput(int n, List<Dimension> list) {
		// print the output configuration
		Map<Integer, List<Dimension>> trees = Zookeeper.trees;

		if (list == null) {
			System.out.println("FAIL");
			return;
		}

		System.out.println("PASS");

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
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}

	}

	private static List<State> generateChildNodes(int n, State s) {
		List<Dimension> lizardConfig = s.getConfiguration();

		List<Dimension> candidateChildConfig = new LinkedList<Dimension>();
		List<State> childNodes = new ArrayList<State>();
		int colToFill = 0, dummyIndex = 0;
		if (!lizardConfig.isEmpty()) {
			dummyIndex = lizardConfig.lastIndexOf(Dimension.DUMMY_DIMENSION);
			if (dummyIndex == -1) {
				colToFill = lizardConfig.get(lizardConfig.size() - 1).col + 1;
			} else {
				colToFill = lizardConfig.get(dummyIndex - 1).col + 1;
			}
		}
		
		List<Dimension> treeConfig = Zookeeper.trees.get(Integer.valueOf(colToFill));

		// generate all the candidates
		for (int i = 0; i < n; i++) {
			candidateChildConfig.add(new Dimension(i, colToFill));
		}

		// remove candidates which have trees
		// candidateChildConfig.removeAll(treeConfig);

		for (Dimension q : lizardConfig) {
			if (q.col < colToFill) {
				candidateChildConfig.remove(new Dimension(q.row, colToFill));
			}

			Dimension d1 = new Dimension(q.row, q.col);
			Dimension d2 = new Dimension(q.row, q.col);

			boolean treeTwoFound = false, treeOneFound = false;
			while ((d1.col != colToFill && d1.col <= colToFill) || (d2.col != colToFill && d1.col <= colToFill)
					|| (treeOneFound && treeTwoFound)) {

				d1.setCol(d1.getCol() + 1);
				d1.setRow(d1.getRow() - 1);
				List<Dimension> l = Zookeeper.trees.get(d1.getCol());
				if (l != null && l.contains(d1)) {
					treeOneFound = true;
				}

				d2.setCol(d2.getCol() + 1);
				d2.setRow(d2.getRow() + 1);
				l = Zookeeper.trees.get(d2.getCol());
				if (l != null && l.contains(d2)) {
					treeTwoFound = true;
				}
			}

			if (d1.col == colToFill || d2.col == colToFill) {
				if(!treeOneFound)
					candidateChildConfig.remove(d1);
				if(!treeTwoFound)
				candidateChildConfig.remove(d2);
			}
		}
		// -------------CANDIDATES FOUND---------------------
		if (candidateChildConfig.isEmpty()) {// In case no candidate is found then don't return as there may be a tree
												// in next columns **NOT A DEAD STATE**

			if (treeConfig == null || treeConfig.isEmpty())// If no tree available its a purely dead state
				return childNodes;

			List newConfig = new LinkedList(lizardConfig);
			newConfig.add(Dimension.DUMMY_DIMENSION);
			childNodes.add(new State(newConfig));
		} else {
			createChildStates(0, candidateChildConfig, lizardConfig, childNodes,treeConfig==null?null:new ArrayList<Dimension>(treeConfig));
		}
		return childNodes;
	}

	private static void createChildStates(int index, List<Dimension> candidateChildConfig, List<Dimension> lizardConfig,
			List<State> childNodes, List<Dimension> treeConfig) {

		int n = candidateChildConfig.size();
		int nextObstacleIndex = 0;

		if (index >= n) {
			if (lizardConfig.lastIndexOf(Dimension.DUMMY_DIMENSION) != -1) {
				lizardConfig.removeAll(Collections.singletonList(Dimension.DUMMY_DIMENSION));
			}
			if (!lizardConfig.isEmpty()) {
				childNodes.add(new State(lizardConfig));
			}
			return;
		}

		if (treeConfig == null || treeConfig.isEmpty()) {
			nextObstacleIndex = n;
		} else {
			nextObstacleIndex = treeConfig.indexOf(treeConfig.get(0).row);
			treeConfig.remove(0);
		}

		List<Dimension> newConfig = null;

		for (int i = index; i <= nextObstacleIndex; i++) {
			newConfig = new ArrayList<Dimension>(lizardConfig);
			if (i == nextObstacleIndex && i<n) {
				// newConfig.add(Dimension.DUMMY_DIMENSION);
				createChildStates(nextObstacleIndex + 1, candidateChildConfig, newConfig, childNodes, treeConfig);
			} else if(i!=nextObstacleIndex) {
				newConfig.add(candidateChildConfig.get(i));
				createChildStates(nextObstacleIndex + 1, candidateChildConfig, newConfig, childNodes, treeConfig);
			}
		}
	}
*/}
