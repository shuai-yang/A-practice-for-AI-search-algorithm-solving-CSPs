import java.util.HashSet;

public class Item {
	public int id = 0; //unique ID for each item, following the text 'Item' composing the name
	public String name = ""; 
	public int size = 0; 
	public String constrainDescription = ""; 
	public String constrainType=""; 
	//hashset contains a list of unique items that are not compatible with current item
	public HashSet<Integer> itemConstraints = new HashSet<Integer>();

	public Item(int n) {
		this.id = n;
	}
}