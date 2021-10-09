import java.io.*;  
import java.util.*; 

public class Solution {
    static int numItems = 0;
	static int minSize = Integer.MAX_VALUE;
	static int totalSizeTaken = 0;
	
	public static void main(String[] args) throws IOException { 
		boolean arc_consistency = true;
		ArrayList<Item> Items = new ArrayList<Item>();
		File filename = new File(args[0]);
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(filename);
		} catch (FileNotFoundException e) {
			return;
		}
		int numBags = fileScanner.nextInt();
		int bagMaxSize = fileScanner.nextInt();
		fileScanner.nextLine();
		int n = 0;
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			Scanner lineScanner = new Scanner(line);
			Item item = new Item(n++);
			item.name= lineScanner.next();
			item.size= lineScanner.nextInt();
			if(item.size > bagMaxSize) {System.out.println("line64 failure");System.exit(0);}
			totalSizeTaken += item.size;
			if(totalSizeTaken > bagMaxSize*numBags) {System.out.println("line66 failure");System.exit(0);}
			minSize = Integer.min(minSize, item.size);
			if(lineScanner.hasNextLine()) 
				item.constrainDescription= lineScanner.nextLine();
			Items.add(item);
			lineScanner.close();
		}
		numItems = Items.size();
		fileScanner.close();
		for(Item item : Items){
			Scanner constrainScanner = new Scanner(item.constrainDescription);
			if(constrainScanner.hasNext()) {
				item.constrainType = constrainScanner.next();
				if(item.constrainType.equals("-")) {
					while(constrainScanner.hasNext()){
						String constrain = constrainScanner.next();
						int constrainedItemID = Integer.parseInt(constrain.replaceAll("[^0-9]", ""));
						// map current item to constrained item
						item.itemConstraints.add(constrainedItemID);
						// map constrained item to current item
						Items.get(constrainedItemID).constrainType = item.constrainType;
						Items.get(constrainedItemID).itemConstraints.add(item.id);
					}
				}
				else if(item.constrainType.equals("+")){
					HashSet<Integer> positiveConstraints = new HashSet<Integer>();
					while(constrainScanner.hasNext()){
						String constrain = constrainScanner.next();
						int constrainedItemID = Integer.parseInt(constrain.replaceAll("[^0-9]", ""));
						positiveConstraints.add(constrainedItemID);
					}
					HashSet<Integer> allIDs = new HashSet<Integer>();
					for(Item i: Items) {allIDs.add(i.id);}
					allIDs.removeAll(positiveConstraints);
					allIDs.remove(item.id);
					HashSet<Integer> negativeConstraints = allIDs;
					for(int negativeConstrain : negativeConstraints) {
						item.itemConstraints.add(negativeConstrain);
						Items.get(negativeConstrain).itemConstraints.add(item.id);
					}
				}
			}
			constrainScanner.close();
		}
		// SortItems() implements MRV (Most Restrictive Values) strategy to improve the efficiency of the program
		Collections.sort(Items, new SortItems()); 
		ArrayList<Bag> bags = new ArrayList<Bag>();
		for(int i = 0;i<numBags;i++){
			Bag b = new Bag(i, bagMaxSize);
			bags.add(b);
		}
		State currState = new State(bags, 0);  
		HashMap<Integer, ArrayList<Integer>> itemToCompatibleBags = new HashMap<>();
		ArrayList<Integer> compatibleBags = new ArrayList<>();
		for(Bag b: bags) {compatibleBags.add(b.id);}
		for(Item item: Items) {itemToCompatibleBags.put(item.id, compatibleBags);}
		while(!currState.isGoalState(Items.size())) {
			int indexCurrentItem = currState.indexItemToAdd;
			Item currItem = Items.get(indexCurrentItem);
			bags = currState.bags; 
			// SortBags() implements LCV (Least Constraining Value) strategy to improve the efficiency of the program
			Collections.sort(bags, new SortBags());
			// getUniqueBags() remove the duplicate (empty) bags from the states to improve the efficiency of the program
			ArrayList<Bag> uniqueBags = getUniqueBags(bags);
			// if any item that conflicts with ALL other items, reverse the order of the sorted uniqueBags so the empty bag goes first now
			if(currItem.itemConstraints.size() == numItems - 1) 
				Collections.reverse(uniqueBags);
			Stack<State> states = new Stack<State>(); // backtracking using Stack
			Stack<State> tempStates = new Stack<State>();
			for(Bag b : uniqueBags) {	
				if(b.canAdd(currItem)){
					if(arc_consistency) {
						for(int constrainedItem: currItem.itemConstraints) {
							ArrayList<Integer> oldCompatibleBags = itemToCompatibleBags.get(constrainedItem);
							ArrayList<Integer> newCompatibleBags = new ArrayList<Integer>();
							for(int i=0; i<oldCompatibleBags.size(); i++) {
								if(oldCompatibleBags.get(i)!= b.id) {
									newCompatibleBags.add(oldCompatibleBags.get(i));
								}
							}
							if(newCompatibleBags.isEmpty()) {
							}
							itemToCompatibleBags.replace(constrainedItem, newCompatibleBags);
						} 
					}
					ArrayList<Bag> bagsNextState = new ArrayList<Bag>(bags);
					bagsNextState.set(bags.indexOf(b), b.add(currItem));
					tempStates.push(new State(bagsNextState, indexCurrentItem+1)); 
				}
			}
			while(!tempStates.isEmpty()) {
				states.push(tempStates.pop());
			}
			if(!states.empty()) {
				currState = states.pop();
			}else {
				System.out.println("failure");System.exit(0);
			}
		} //end of while loop
		System.out.println("success");
		ArrayList<Bag> usedBags = new ArrayList<Bag>();
		for(Bag b: currState.bags){
			if(b.itemsInBag.size() != 0) {
				usedBags.add(b);
			}
		} 
		printBags(usedBags);
	} //end of Main
	
	// negative if item a is more constrained than item b
	// positive if item a is less constrained than item b
	// 0 if item a and item b are equally constrained
	public static class SortItems implements Comparator<Item> { 
		@Override
	    public int compare(Item a, Item b) { 
			if(a.itemConstraints.size() > b.itemConstraints.size())
	    		return -1; 
	    	else if(a.itemConstraints.size() < b.itemConstraints.size())
	    		return 1;
	    	else {
	    		if(a.size > b.size)
	    			return -1;
	    		else if(a.size < b.size)
	    			return 1;
	    		else
	    			return 0;
	    	}
	    } 
	} 

	public static ArrayList<Bag> getUniqueBags(ArrayList<Bag> bags) {
		ArrayList<Bag> uniqueBags = new ArrayList<Bag>();
		boolean isUnique = true;
		for(Bag b: bags) {
			if(b.itemsInBag.isEmpty()) { 
				// only remain the first (empty) bag
				if(isUnique){
					uniqueBags.add(b);
					isUnique= false;
				}
			}
			else {
				if(b.sizeTaken + minSize <= b.maxSize && (numItems - b.bagConstraints.size()) > b.itemsInBag.size()) 
					uniqueBags.add(b);
			}
		}
		Collections.sort(bags, new SortBags()); 
		return uniqueBags;
	}
	
	public static void printBags(ArrayList<Bag> bags) {
		for(Bag b: bags) {
			for(int item: b.itemsInBag) {
				System.out.print("Item"+item+"\t");
			}
			System.out.println();
		}
	} 
	
	// negative if bag a is more constrained than bag b
	// positive if bag a is less constrained than bag b
	// 0 if bag a and bag b are equally constrained
	public static class SortBags implements Comparator<Bag> { 
		@Override
	    public int compare(Bag a, Bag b) { 
	    	if(a.bagConstraints.size() > b.bagConstraints.size()) 
	    		return -1;
	    	else if(a.bagConstraints.size() < b.bagConstraints.size())
	    		return 1;
	    	else {
	    		if(a.sizeTaken > b.sizeTaken)
	    			return -1;
	    		else if(a.sizeTaken < b.sizeTaken)
	    			return 1;
	    		else
	    			return 0;
	    	}
	    } 
	} 
}
