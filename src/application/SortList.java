package application;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.scene.control.TextArea;

/*******************************************************************
 * This class performs different operations on Lists some of which
 * include sorting in various orders, shuffling in random order,
 * and printing the list.
 *******************************************************************/
public class SortList implements ListFunctions
{
	// class instance variables
	private ArrayList<String> unsortedList = new ArrayList<>();
	private TextArea txtArea = new TextArea();
	private Queue<String> l = new LinkedList<>();
	
	// constructor for list
	public SortList(ArrayList<String> l, TextArea txtA)
	{
		unsortedList = l;
		txtArea = txtA;
	}
	
	// constructor for queue
	public SortList(Queue<String> q)
	{
		l = q;
	}
	
	/***********************************************************
	 * This function sorts the given list in alphabetical order.
	 ***********************************************************/
	public void alphabeticalSort() 
	{
		Collections.sort(unsortedList, new Comparator<String>() 
		{
			// have to override the compare method because we want to
			// sort correctly with Upper case letters as well
			// by default the compare function does not do that
			@Override
			public int compare(String obj1, String obj2)
			{
				return Collator.getInstance().compare(obj1, obj2);
			}
		});
	}
	
	/**************************************************************
	 * This function sorts the given list in reverse lexicological 
	 * (alphabetical) order
	 ***************************************************************/
	public void revAlphaSort()
	{
		unsortedList.sort(Collections.reverseOrder());
	}
	
	/************************************************************
	 * This function returns the list in whichever order (sorted/ 
	 * unsorted) it is in currently.
	 ***********************************************************/
	public ArrayList<String> getList()
	{
		return unsortedList;
	}

	/**************************************************************
	 * This function shuffles the queue in random order.
	 ***************************************************************/
	public void shuffleList(Queue<String> in)
	{
		Collections.shuffle((List<?>) l);
	}
	
	
	/**************************************************************
	 * This function prints the list in the current order
	 * (sorted/unsorted) it is in. It prints it in the textArea
	 * that has been passed into the class earlier.
	 ***************************************************************/
	public void printList()
	{
		
		for (int i = 0; i < unsortedList.size(); i++)
		{
			txtArea.appendText(unsortedList.get(i) + "\n");
		}
	}
}

