
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A node implementation for a simple trie (http://en.wikipedia.org/wiki/Trie)
 * Note: this implementation is not thread safe
 * 
 * @author Parth Parekh
 * @author Thomas Lisankie
 **/
public class RhymeDictionaryTrieNode {
	// to be set by Trie implementor
	private boolean isFinalChar;
	private Word word;
	private char charValue; //last character of Word name
	private int depth;
	private Map<Character, RhymeDictionaryTrieNode> childrenMap;

	// creates empty root trie node
	public RhymeDictionaryTrieNode() {
	}

	// this should be called by addChild only
	private RhymeDictionaryTrieNode(char charValue) {
		
		this.charValue = charValue;
		childrenMap = null;
		
	}

	/**
	 * adds child to the existing Trie node, if there is no child with given character value present already
	 * 
	 * @param charValue
	 * @return returns true if the add is successful, false if there was already a child with that charValue
	 */
	public boolean addChild(char charValue) {
		// only create children when you're adding first child
		if (childrenMap == null) {
			
			childrenMap = new TreeMap<Character, RhymeDictionaryTrieNode>();
			
		}
		
		this.setFinalChar(false);
		
		Character charValueObject = Character.valueOf(charValue);
		
		if (childrenMap.containsKey(charValueObject)) {
			
			return false;
			
		}
		
		childrenMap.put(charValueObject, new RhymeDictionaryTrieNode(charValue).setDepth(this.depth + 1));
		
		//TODO Need to fix this so words aren't added to every child
		
		return true;
		
	}

	/**
	 * removes child from the existing Trie node
	 * 
	 * @param charValue
	 * @return returns true if the remove was successful, false if there was no child found with that charValue
	 */
	public boolean removeChild(char charValue) {
		// return false if there are no children or children does not contain
		// the character to be removed
		Character charValueObject = Character.valueOf(charValue);
		if (childrenMap == null || !childrenMap.containsKey(charValueObject)) {
			return false;
		}
		childrenMap.remove(charValueObject);
		return true;
	}

	/**
	 * returns the child TrieNode if it exists
	 * 
	 * @param charValue
	 * @return returns TrieNode object for child if it exists, null otherwise
	 */
	public RhymeDictionaryTrieNode getChild(char charValue) {
		// return null if there are no children
		if (childrenMap == null) {
			return null;
		}
		return childrenMap.get(Character.valueOf(charValue));
	}

	/**
	 * returns Set of all the children char values of current TrieNode
	 * 
	 * @return returns Set of all the Character objects if it exists, null otherwise
	 */
	public Set<Character> getChildrenValues() {
		// return null if there are no children
		if (childrenMap == null) {
			return null;
		}
		return childrenMap.keySet();
	}

    /**
   	 * returns Set of all the children nodes of current TrieNode
   	 *
   	 * @return returns Set of all the TrieNode objects if it exists, null otherwise
   	 */
   	public Set<RhymeDictionaryTrieNode> getChildrenNodes() {
   		// return null if there are no children
   		if (childrenMap == null) {
   			return null;
   		}
        Set<RhymeDictionaryTrieNode> trieNodes = new HashSet<RhymeDictionaryTrieNode>();
        for (Character childChar : childrenMap.keySet()) {
            trieNodes.add(getChild(childChar));
        }
   		return trieNodes;
   	}

	public char getCharValue() {
		return charValue;
	}

	public boolean isFinalChar() {
		return isFinalChar;
	}

	public void setFinalChar(boolean isFinalChar) {
		this.isFinalChar = isFinalChar;
	}

	public int getDepth() {
		return depth;
	}

	public RhymeDictionaryTrieNode setDepth(int depth) {
		this.depth = depth;
		return this;
	}

	@Override
	public int hashCode() {
		// need to think of something better
		return charValue + (childrenMap!=null ? childrenMap.hashCode() : 0);
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("nodeValue: " + charValue + "; isFinalChar: "
				+ isFinalChar + "; Word: "
						+ word + "; depth: " + depth + "; children: ");
		if (childrenMap != null) {
			return toString.append(childrenMap.keySet().toString()).toString();
		}
		return toString.append("no children").toString();
	}
}
