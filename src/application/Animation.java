package application;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/***********************************************************************
 * This class sets up the GUI and initializes the necessary components *
 * but the main purpose of this class is to do the animation of the    *
 * words from top to bottom.                                           *  
 ***********************************************************************/
public class Animation extends Application 
{
	// class instance variables
	private BorderPane mainPane = new BorderPane();						// main container in which everything is contained
	private Pane statsPane = new Pane();								// shows the score, typing speed, typed words, and takes input 
	private Pane wordsPane = new Pane();								// the animation of the words occur in this pane
	private Button start = new Button();								// triggers the animation when pressed
	private TextField input = new TextField();							// gets user input
	private Text score = new Text();									// displays the user's score
	private Text wpm = new Text();										// displays the words typed by the user
	private Text cpm = new Text();										// displays the characters typed by the user
	private Text missedWords = new Text();								// shows the number of words the user missed
	private int missed = 0;												// counter variable that keeps track of the words missed
	private Queue<String> words = new LinkedList<>();					// stores all the words read from the file which will be used for animation
	private LinkedList<String> remWords = new LinkedList<>();			// stores the words that are dequeued from "words"
	private ArrayList<String> userWords = new ArrayList<>();			// stores the words entered by the user	
	private String removedWord; 										// stores the most recently dequeued word	
	private String typedIn;												// stores the most recently typed word by the user
	private int charsTyped = 0;											// stores the total number of characters typed by the user
	private int wordsTyped = 0;											// stores the total number of words typed by the user	
	private int scoreNum = 0;											// stores the running total of user's score
	private TextArea showUserWords = new TextArea();					// displays the correctly typed words by the user
	
	
	/********************************************************************
	 * This function initializes the background colors of wordsPane and *
	 * statsPane                                                        *
	 ********************************************************************/
	public void initBackground()
	{
		wordsPane.setBackground(new Background(new BackgroundFill(Color.AZURE, null, null)));
		statsPane.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, null, null)));
	}
	
	/*********************************************************************
	 * This function reads all the words from the file and shuffles them *
	 * so when the animation starts, the words appear in random order.   *                                                   
	 *********************************************************************/
	public void readWords() throws IOException
	{
		try
		{
			File file = new File("words.txt");
			Scanner sc = new Scanner(file);

			while(sc.hasNext())
			{
				String wordItem = sc.next();
				words.add(wordItem);	// add the word read to the queue
			}
			sc.close();		// close the scanner
			
			SortList shuff = new SortList(words);
			shuff.shuffleList(words);		// shuffles the queue
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}

	/*********************************************************************
	 * This function initializes the fx components and hence sets up the
	 * statsPane.                                                     
	 *********************************************************************/
	public void initStatsPane()
	{
		start.setText("Start!");
		start.setLayoutX(23);
		start.setLayoutY(18);
		start.setPrefSize(101, 25);
		statsPane.getChildren().add(start);
		
		Label lblScore = new Label("Score:");
		lblScore.setLayoutX(28);
		lblScore.setLayoutY(72);
		lblScore.setPrefSize(45, 20);
		statsPane.getChildren().add(lblScore);
		
		score.setText("0");
		score.setLayoutX(73);
		score.setLayoutY(87);
		statsPane.getChildren().add(score);
		
		Label in = new Label(">");
		in.setFont(new Font(22));
		in.setLayoutX(156);
		in.setLayoutY(18);
		statsPane.getChildren().add(in);
		
		input.setLayoutX(180);
		input.setLayoutY(20);
		statsPane.getChildren().add(input);
		
		Label tyWpm = new Label("tyWords");
		tyWpm.setPrefSize(65, 20);
		tyWpm.setLayoutX(191);
		tyWpm.setLayoutY(72);
		statsPane.getChildren().add(tyWpm);
		
		wpm.setLayoutX(280);
		wpm.setLayoutY(87);
		statsPane.getChildren().add(wpm);
		
		Label tyCpm = new Label("tyChars");
		tyCpm.setPrefSize(65, 20);
		tyCpm.setLayoutX(191);
		tyCpm.setLayoutY(137);
		statsPane.getChildren().add(tyCpm);
		
		cpm.setLayoutX(280);
		cpm.setLayoutY(153);
		statsPane.getChildren().add(cpm);
		
		Label mW = new Label("Missed Words: ");
		mW.setLayoutX(28);
		mW.setLayoutY(140);
		statsPane.getChildren().add(mW);
		
		missedWords.setLayoutX(120);
		missedWords.setLayoutY(155);
		statsPane.getChildren().add(missedWords);
		
		showUserWords.setLayoutX(400);
		showUserWords.setLayoutY(10);
		showUserWords.setPrefSize(150, 180);
		showUserWords.setText("Correctly Typed Words\n\n");
		showUserWords.setWrapText(true);
		statsPane.getChildren().add(showUserWords);
		
		statsPane.setPrefSize(600, 200);
	}
	
	/*********************************************************************
	 * This function shows the number of typed words in the field wpm
	 * located in the statsPane.                                                   
	 *********************************************************************/
	public void calcTyWords()
	{
		wpm.setText(Integer.toString(wordsTyped));
	}
	
	/*********************************************************************
	 * This function shows the number of characters typed in the field cpm
	 * located in the statsPane.                                                   
	 *********************************************************************/
	public void calcTyChars()
	{
		cpm.setText(Integer.toString(charsTyped));
	}
	
	/*********************************************************************
	 * This function updates the missed words and shows them in the 
	 * appropriate field in statsPane.                                                   
	 *********************************************************************/
	public void updateMissed()
	{
		missed++;
		missedWords.setText(Integer.toString(missed));
	}
	
	/*********************************************************************
	 * This function updates the score in real time and displays it in the
	 * appropriate field in statsPane.                                                  
	 *********************************************************************/
	public void updateScore()
	{
		scoreNum += 10;
		score.setText(Integer.toString(scoreNum));
	}
	
	/*********************************************************************
	 * This function mainly handles the animation and input from the user.                                                  
	 *********************************************************************/
	public void start(Stage primaryStage) throws IOException {
	    		
		readWords();		// function call
		initStatsPane();	// function call		
		initBackground();	// function call
	    
		wordsPane.setPrefSize(600, 500);
	    wordsPane.setMaxWidth(Region.USE_PREF_SIZE);		// sets the max width as the preferred width so that
	    													// the words do not go beyond the pane bounds
	    final long wordDelay = 25_00_000_000L; // 250 ms - time delay between two words before they both appear on the screen		
	    final long fallDuration = 12_000_000_000L; // 12 s - the time duration in which the word will fall from top to bottom	

	    // event handler for clicking the start button
	    start.setOnMouseClicked(e -> 
	    {
	    	AnimationTimer animation = new AnimationTimer() 
		    {
	    			         
		        private long lastWordAdd = Long.MIN_VALUE;	 // never added a word before
		        private final Map<Long, Text> nodes = new LinkedHashMap<>();	// map whose key is the time in long and value is the Text object
		        Random rand = new Random();		// seed the random generator to use later
		        
		        /************************************************************
		         * This function assigns a random X position from which the * 
		         * Text object will begin falling from.                     *
		         ************************************************************/
		        private void assignXPosition(Text text) 
		        {
		        	int wordsPaneWidth = (int)wordsPane.getPrefWidth() - 100;
		        	int nextX = rand.nextInt((wordsPaneWidth - 10) + 1) + 10;
		            text.setTranslateX(nextX);
		        }

		        /************************************************************
		         * This function animates the words on wordsPane and allows
		         * them to fall from top of the wordsPane to the bottom.
		         * 
		         * Parameter: long now
		         * It is an internal timer of the AnimationTimer class
		         ************************************************************/
		        @Override
		        public void handle(long now) 
		        {
		            // updates & cleanup
		            long deletionLimit = now - fallDuration;	// the time in which the word should be deleted
		            
		            // iterate through the map 
		            for (Iterator<Map.Entry<Long, Text>> iter = nodes.entrySet().iterator(); iter.hasNext();) 
		            {
		                Entry<Long, Text> entry = iter.next();		// point to the next entry in the map
		                final long startTime = entry.getKey();		// get the entry's key
		                final Text text = entry.getValue();			// get the entry's value
		                
		                // deletes the word if it falls down without typing
		                if (startTime <= deletionLimit) 		
		                {
		                    // delete old word by removing the iterator pointing to that entry		  
		                    iter.remove();
		                    wordsPane.getChildren().remove(text);	// remove that entry from the wordsPane	 
		                   
		                    // if user enters the words that is not at the bottom
		                    // then we set it to an empty string. It still continues
		                    // to fall but since we don't want to count it as a missed word
		                    // we simply ignore it if it falls to the bottom
		                    if (!text.getText().equals(""))
		                    {
		                    	updateMissed();
		                    }
		                    
		                    // the user has missed 10 words by this point
		                    if (missed == 10)
		            		{
		            			calcTyChars();		// display the characters typed
		            			calcTyWords();		// display the words typed	
		            			stop();				// stop the animation
		            			wordsPane.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));	// make the background red
		            																								// to signify game is over
		            			// sort the list alphabetically and append it to the textArea
		            			SortList sortedlist = new SortList(userWords, showUserWords);						
		            			sortedlist.alphabeticalSort();
		            			sortedlist.printList();
		            			
		            		}
		                } 
		                
		                // if the user types the word correctly 
		                else if (remWords.remove(typedIn))		// cannot check against map because it cannot be modified while it's 
		                										// being iterated through so check against remWords
		                {	
		                	// this is a workaround for deleting/modifying a map 
		                	// while it's being iterated through
		                	Text tmp = new Text("");
		                	for (Map.Entry<Long, Text> e: nodes.entrySet())
		                	{
		                		if (e.getValue().getText().equalsIgnoreCase(typedIn))
		                		{
		                			// set the color of the node to transparent so the user cannot see it
		                			// in effect it pseudo-deletes the word
		                			e.getValue().setFill(Color.TRANSPARENT);
		                			e.setValue(tmp);
		                			wordsPane.getChildren().remove(tmp);
		                		}
		                	}
		                	
		                	// if user enters the word that is at the bottom
		                	if (text.getText().equalsIgnoreCase(typedIn))
		                	{
		                		wordsPane.getChildren().remove(text);
		                	}
		                	
		                	wordsTyped++;	// increment the counter
		                	updateScore();	// update the score      	
		                }
		                
		                else 
		                {
		                    // update existing word
		                    double factor = ((double) (now - startTime)) / fallDuration;
		                    Bounds bounds = text.getBoundsInLocal();
		                    // mathematical factors for animating down the Y-axis smoothly
		                    text.setTranslateY((wordsPane.getHeight() + bounds.getHeight()) * factor - bounds.getMaxY()); 
		                }
		            }

		            if (words.isEmpty()) 
		            {
		                if (nodes.isEmpty()) {
		                    stop(); // end animation since there are no more words
		                }
		            } 
		            
		            // this will execute when new words need to be added to the map
		            else if (lastWordAdd + wordDelay <= now) 
		            {
		            	// add new word
		                lastWordAdd = now;
		                removedWord = words.remove();	// removedWord stores the dequeued word from the queue "words"
		                remWords.add(removedWord);		// add the word to the remwords so we can check the typed word against it in the loop	
		                Text text = new Text(removedWord);			
		                text.setFill(Color.RED);
		                wordsPane.getChildren().add(text);
		                assignXPosition(text);
		                text.setTranslateY(-text.getBoundsInLocal().getMaxY());
		                nodes.put(now, text);		                
		            }
		        }
		    };
		    animation.start();	    	
	    });
	    
	    input.setOnKeyPressed(e -> 
	    {
	    	// capture the word typed by the user when followed by an ENTER key press	
			if (e.getCode() == KeyCode.ENTER)
			{
				typedIn = input.getText();
				if (remWords.contains(typedIn))
				{
					input.clear();				// only clear the textfield if the word is correctly typed
					userWords.add(typedIn);
					charsTyped += typedIn.length();		// add the number of characters to the running total
				}
			}
		});
	    
	    mainPane.setPrefSize(600, 700);
	    mainPane.setTop(wordsPane);
	    mainPane.setBottom(statsPane);
	    Scene scene = new Scene(mainPane);
	    
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}