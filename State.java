import java.util.ArrayList;

public class State {
	public ArrayList<Bag> bags;
	public int indexItemToAdd;
	public State(ArrayList<Bag> bags, int indexAddedItem){
		this.bags = (ArrayList<Bag>) bags; 
		this.indexItemToAdd = indexAddedItem;
	}
	public boolean isGoalState(int totalNumItems) {	
		int totalNumItemsBagged = 0;
		for(Bag bag: bags){
			totalNumItemsBagged += bag.itemsInBag.size();
		}
		return totalNumItemsBagged == totalNumItems;
	}
}
