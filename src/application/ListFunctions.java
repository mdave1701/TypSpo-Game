package application;

import java.util.*;

/*************************************************
 * This interface lays out the blueprints for the 
 * the functions a class would need to implement 
 * to implement this interface.
 *************************************************/
public interface ListFunctions
{
	public void alphabeticalSort();
	public void revAlphaSort();
	public ArrayList<String> getList();
	public void shuffleList(Queue<String> q);
	public void printList();
}
