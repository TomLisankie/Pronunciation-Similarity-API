
import java.awt.geom.Point2D;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

/**@author Thomas Lisankie*/

public class RhymeDictionaryAssembler {
	
	public final static boolean DEBUGGING = true, SAMPLESIZE = true;
	
	public static void main(String[] args){
		ArrayList<Phoneme> phonemes = null;
		//1
		List<String> linesOfDictionary = null;
	//loads all the lines in the CMU Phonemic Dictionary. Each line contains a word and its phonemic translation.
		try{
			linesOfDictionary = Files.readAllLines(Paths.get("cmudict-0.7b_modified.txt"), Charset.defaultCharset());
			
		 	/*for (String line : linesOfDictionary) {
			 	debugPrint(line);
         	}*/
		}catch(Exception e){
			
			debugPrint("there was an exception");
		
		}
		
		//2
		//ArrayList for the word names
		ArrayList<String> wordNames = new ArrayList<String>();
		
		/**ArrayList of lists of phonemes that compose a word**/
		ArrayList<ArrayList<Phoneme>> listsOfPhonemesForWords = new ArrayList<ArrayList<Phoneme>>();
		String word = "";
		
		//creates word names
		int spacesToSkip = 2;
		for (int i = 0; i < linesOfDictionary.size(); i++){
			
			spacesToSkip = 2;
			
			String lineBeingExamined = linesOfDictionary.get(i);
			int indexOfCharBeingExamined = 0;
			for (int j = 0; j < lineBeingExamined.length(); j++){
				
				indexOfCharBeingExamined = j;
				char charBeingExamined = lineBeingExamined.charAt(j);
				
				if(charBeingExamined != ' ' && charBeingExamined != '('){
					
					word = word + charBeingExamined;
					
				}else if(charBeingExamined == '('){
					
					spacesToSkip = spacesToSkip + 3;
					wordNames.add(word);
					word = "";
					break;
					
				}else{
					
					wordNames.add(word);
					word = "";
					break;
					
				}
				
			}
			
			indexOfCharBeingExamined = indexOfCharBeingExamined + spacesToSkip;
			phonemes = new ArrayList<Phoneme>();
			Phoneme phoneme = new Phoneme();
			String phonemeName = "";
			//creates list of phonemes
			for (int k = indexOfCharBeingExamined; k < lineBeingExamined.length(); k++){
				
				char charBeingExamined = lineBeingExamined.charAt(k);
				//if the character is a letter
				if(Character.isLetter(charBeingExamined)
						/*this didn't work for whatever reason:
						 * charBeingExamined != ' ' || Character.isDigit(charBeingExamined) == false*/){ 
					
					phonemeName = phonemeName + charBeingExamined;
					//debugPrint(phonemeName);
					
				}else if(Character.isDigit(charBeingExamined)){
					//IF THERE IS A CONSONANT BEFORE A VOWEL IT'S MAKING THE VOWEL ISAVOWEL FALSE for some reason
					//it's somewhere around here
					int stress = Character.getNumericValue(charBeingExamined);
					phoneme.setStress(stress);
					
				}else if(charBeingExamined == ' '){
					
					phoneme.setPhoneme(phonemeName);
					phonemes.add(phoneme);
					phoneme = new Phoneme();
					phonemeName = "";
					
					
				}else{}
				
			}
			
			//this is for the last phoneme
			phoneme.setPhoneme(phonemeName);
			phonemes.add(phoneme);
			phoneme = new Phoneme();
			phonemeName = "";
			
			listsOfPhonemesForWords.add(phonemes);
			
		}
		
		
		/*for(int h = 0; h < wordNames.size(); h++){
			
			debugPrint(wordNames.get(h));
			ArrayList<Phoneme> listSample = listsOfPhonemesForWords.get(h);
			
			for(int l = 0; l < listSample.size(); l++){
				
				debugPrint(listSample.get(l).getPhoneme());
				
			}
			
		}*/
		
		//3
		ArrayList<Word> anchorWords = new ArrayList<Word>();
		
		for(int f = 0; f < wordNames.size(); f++){
			
			anchorWords.add(new Word((wordNames.get(f)).toLowerCase(), listsOfPhonemesForWords.get(f)));
			
		}
		
		/*for(int q = 0; q < wordNames.size(); q++){
			
			debugPrint("Word Name (Anchor): " + anchorWords.get(q).getWordName());
			debugPrint("Phonemes (Anchor):");
			anchorWords.get(q).printListOfPhonemes();
			
		}
		
		debugPrint(anchorWords.get(20).getWordName()); //prints out "aardvarks"*/
		
		//find Rhyme Value/Percentile for all Words
		/*for(int i = 0; i < anchorWords.size(); i++){
			
			debugPrint("Anchor Index: " + i);
			
			for(int j = 0; j < anchorWords.size(); j++){
				
				debugPrint("Satellite Index: " + j);
				
				double rhymePercentile = 0.0;
				
				if(j != i){
					
					rhymePercentile = findRhymeValueAndPercentileForWords(anchorWords.get(i), anchorWords.get(j));
				
				}
				
				if(rhymePercentile >= 0.4){
					
					anchorWords.get(i).addWord(j, rhymePercentile);
					
				}
				
			}
			
			for(int k = 0; k < anchorWords.get(i).getWordsThisRhymesWith().size(); k++){
				
				debugPrint(anchorWords.get(i).getWordsThisRhymesWith().get(k).getX() + ", " + anchorWords.get(i).getWordsThisRhymesWith().get(k).getY());
				
			}
			
			if(i == 9 && SAMPLESIZE == true){
				
				System.out.println("Number of words this rhymes with: " + anchorWords.get(i).getWordsThisRhymesWith().size());
				break;
				
			}
			
		}*/
		
		findRhymeValueAndPercentileForWords(anchorWords.get(9), anchorWords.get(133783));
		
		System.out.println("done - rhyme dictionary has been created");

	}

	/**This method goes through the entire process of finding how well two words rhyme with one another.
	 * How well two words rhyme is given by the Rhyme Percentile returned. The higher the Rhyme Percentile, the better they rhyme.
	 * @return */
	public static double findRhymeValueAndPercentileForWords(Word anchor, Word satellite) {
		//add printlns throughout this to make sure everything is working right
		System.out.println("Anchor: " + anchor.getWordName() + ", Satellite: " + satellite.getWordName());
		double rhymeValue = 0.0;
		double rhymePercentile = 0.0;
		
		//System.out.println();
		debugPrint("---------------------------------------------");
		//System.out.println();
		
		if(anchor.getListOfPhonemes().size() == satellite.getListOfPhonemes().size()){
			
			debugPrint("REGULAR RHYME VALUE");
			
			debugPrint("Anchor:");
			anchor.printListOfPhonemes();
			debugPrint("Satellite:");
			satellite.printListOfPhonemes();
			
			for(int s = 0; s < anchor.getListOfPhonemes().size(); s++){
				
				rhymeValue = (double) rhymeValue + (double)findRVBetweenPhonemes(anchor.getListOfPhonemes().get(s), 
						satellite.getListOfPhonemes().get(s));
				
			}
			
			debugPrint("Rhyme Value:" + rhymeValue);
			
			/*rhyme percentile for words of same phonemic length uses the anchor word as the denominator. This is to keep the focus on
			 * the anchor word which is the focus word*/
			rhymePercentile = (double) findRhymePercentile(rhymeValue, anchor); 
			
		}else{//do ideal Rhyme Value process
			
			debugPrint("IDEAL RHYME VALUE");
			
			debugPrint("Anchor:");
			anchor.printListOfPhonemes();
			debugPrint("Satellite:");
			satellite.printListOfPhonemes();
			
			Word shorterWord = null;
			Word longerWord = null;
			
			//these conditionals find which word is longer and which is shorter
			if(anchor.getListOfPhonemes().size() < satellite.getListOfPhonemes().size()){
				
				shorterWord = anchor;
				longerWord = satellite;
				
			}else{
				
				shorterWord = satellite;
				longerWord = anchor;
				
			}
			
			debugPrint("Shorter Word: " + shorterWord.getWordName());
			debugPrint("Longer Word: " + longerWord.getWordName());
			
			double idealRhymeValue = 0.0;
			
			//start here
			//too much hatred for too long
			
			boolean firstSearch = true;
			boolean foundStartingIndex = false;
			ArrayList<Layer> layers = new ArrayList<Layer>();
			ArrayList<Node> nodesForThisLayer = new ArrayList<Node>();
			
			for(int s = 0; s < shorterWord.getListOfPhonemes().size(); s++){
				
				if(firstSearch == true){
					
					Node startNode = new Node();
					for(int l = 0; l < longerWord.getListOfPhonemes().size(); l++){
						
						double RVBetweenPhonemes = findRVBetweenPhonemes(shorterWord.getListOfPhonemes().get(s), longerWord.getListOfPhonemes().get(l));
						if(RVBetweenPhonemes > 0){
							
							foundStartingIndex = true;
							
							IndexSet indexSet = new IndexSet(l, RVBetweenPhonemes);
							
							startNode.addIndexSet(indexSet);
							
						}
						
						
					}
					
					if(foundStartingIndex == true){
						
						nodesForThisLayer.add(startNode);
						layers.add(new Layer(nodesForThisLayer));
						firstSearch = false;
						
					}
					
					nodesForThisLayer = new ArrayList<Node>();
					
				}else{
					
					//left off here
					
				}
				
			}
			
			
			rhymePercentile = (double) findRhymePercentile(rhymeValue, longerWord);
			
		}
		
		System.out.println("Rhyme Percentile: " + rhymePercentile);
		
		return rhymePercentile;
		
	}
	
	/**Finds the Rhyme Value that a word has with itself (homophonic Rhyme Value) and then finds the percentage that the 
	 * actual Rhyme Value matches with the homophonic RV*/
	public static double findRhymePercentile(double rhymeValue, Word longerWord){
		
		double homophonicRhymeValue = 0.0;
		double rhymePercentile = 0.0;
		
		for(int i = 0; i < longerWord.getListOfPhonemes().size(); i++){
			
			homophonicRhymeValue = homophonicRhymeValue + 
					findRVBetweenPhonemes(longerWord.getListOfPhonemes().get(i), longerWord.getListOfPhonemes().get(i));
			
		}
		debugPrint("RV: " + rhymeValue);
		debugPrint("HRV: " + homophonicRhymeValue);
		
		rhymePercentile = (double) rhymeValue / (double)homophonicRhymeValue;
		
		return rhymePercentile;
		
	}
	
	/**Takes in two Phonemes and finds the amount that should be added to the Rhyme Value based on how well the two Phonemes match.*/
	public static double findRVBetweenPhonemes(Phoneme p1, Phoneme p2){
		
		debugPrint("			In method");
		debugPrint("			p1 is a vowel:" + p1.isAVowelPhoneme());
		debugPrint("			p2 is a vowel:" + p2.isAVowelPhoneme());
		
		if(p1.isAVowelPhoneme() && p2.isAVowelPhoneme()){
			debugPrint("			-Both vowels");
			if(p1.isEqualTo(p2)){
				debugPrint("			--Equal");
				return 2.0;
				
			}else{
				debugPrint("			--Not equal");
				return 1.0;
				
			}
		}else if(!p1.isAVowelPhoneme() && !p1.isAVowelPhoneme()){
			debugPrint("			-Both consonants");
			if(p1.isEqualTo(p2)){
				debugPrint("			--Equal");
				return 1.0;
				
			}else{
				debugPrint("			--Not equal");
				return 0.5;
				
			}
			
		}else{
			debugPrint("			-No reasonable relation");
			return 0.0;
			
		}
		
	}
	
	public static void debugPrint(Object x){
		
		if(DEBUGGING == true){
			
			System.out.println(x);
			
		}
		
	}
}