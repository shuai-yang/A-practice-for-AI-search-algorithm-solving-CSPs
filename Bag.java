import java.util.ArrayList;
import java.util.HashSet;

public class Bag {
	public int id = 0; //unique ID for each bag because arc_consistency methods requires to distinguish domains(bags) 
	public int maxSize = 0;
	public int sizeTaken = 0;
	public ArrayList<Integer> itemsInBag = new ArrayList<Integer>();
	//hashset maps current bag to a list of items that are not compatible with this bag
	public HashSet<Integer> bagConstraints = new HashSet<Integer>();
	
	public Bag(int id, int maxSize) {
		this.id = id; 
		this.maxSize = maxSize;
	}
	public Bag(Bag b) {
		id = b.id;
		maxSize = b.maxSize;
		sizeTaken = b.sizeTaken;
		//copy a collection of items in bag b into the newly-created bag
		itemsInBag.addAll(b.itemsInBag);
		//copy a hashset of constrains on bag b onto the newly-created bag
		bagConstraints.addAll(b.bagConstraints);
	}
	public boolean canAdd(Item item) {
		return (sizeTaken + item.size <= maxSize) && (!bagConstraints.contains(item.id));
	}
	public Bag add(Item item) {
		Bag b = new Bag(this);
		b.sizeTaken += item.size;
		//add one item into the bag
		b.itemsInBag.add(item.id);
		//append the hashset of constraints on the bag with the constrains from the newly-added item
		b.bagConstraints.addAll(item.itemConstraints);
		return b;
	}	
	public String toString() {
		String s = "";
		for(int item: itemsInBag) { s += (item+"\t");}
		return s;
	}
}
