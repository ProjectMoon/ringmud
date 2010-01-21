package ring.nrapi.mobiles;

public class Language {
	//This class describes and implements a language in the MUD. The objective for this class is
	//to, given a general description of the "sound" of the language, translate words based on the
	//base word's string hashcode. There patterns should generate vowel-consonant placement, as well 
	//as which vowels and consonants to use in these places.
	//
	//However, that can be hard to implement. The CURRENT implementation is similar to World of Warcraft
	//where each language has a set of a few hundred words (a fair amount for each letter amount, up to an
	//infinite amount of letters, although authors can stop at a good number, like 15). Words from the base
	//text are translated by using the base word's hashcode and using it to choose a word out of the choices
	//from the language's word list.
	//
	//If a word is too long to be translated (i.e. the highest word length in the language is 14 letters, but a
	//15 letter word is given to it), it should translate as if it were missing its extra letters and then
	//append vowels and consonants in a specific pattern to bring it back up to proper length. If the word
	//ends in a vowel, append a consonant, then a vowel, and so forth.

	//This describes the words of the language. It is two dimensional. The first dimension (rows) are the length
	//of the word: index + 1. The second dimension is each of the words in that row. Words must conform to the
	//length of the row they're in. Otherwise, an exception is thrown. A row must be filled with 10 columns.
	private String[][] words;

	public Language() {
		words = new String[15][10];
	}

	public Language(int rows) {
		words = new String[rows][10];
	}

	public Language(String[][] words) {
		if (words[0].length != 10) throw(new IllegalArgumentException("word column length must be 10!"));
		else {
			this.words = words;
		}
	}

	public static void main(String[] args) {
		/*
		System.out.println("blah".hashCode());
		System.out.println("hello".hashCode());
		System.out.println("bye".hashCode());
		System.out.println("remember".hashCode());
		System.out.println("because".hashCode());
		System.out.println("eating".hashCode());
		System.out.println("I".hashCode());
		System.out.println("CONST".hashCode());
		String[][] trans = new String[][] {
				{ "a", "u", "e", "o", "r", "z", "b", "w", "p", "u" }, 
				{ "bu", "ze", "ka", "ne", "ro", "de", "po", "ya", "yo", "ze" },
				{ "aze", "bzu", "kro", "eyn", "das", "zen", "ame", "zoi", "nae", "kes" },
				{ "zora", "mora", "pora", "lora", "dasa", "deya", "orra", "zeya", "kaya", "dora" },
				{ "milza", "ka'de", "zokol", "denai", "zet'i", "eyn'a", "zilaa", "orinn", "zensu", "do'ma" }
		};
		System.out.println(new Language(trans).translate("hello there my good man; Ao is... fine..."));
		*/
		
		String reg = "Mobile mob1 { attack1 = blah; attack2 = blah; name = A Troll; } Mobile mob2 { blah; }";
		String[] splitness = reg.split("\\b");
		for (String s : splitness) System.out.println("s: " + s);
		System.out.println("Mobile mob1 {".matches("\\w+\\s*\\w+\\s*\\{"));
	}

	public String translate(String sentence) {
		String[] sentenceWords = sentence.split(" ");
		String res = "";
		for (String word : sentenceWords) {
			String transWord = removePunctuation(word);
			int hash = transWord.hashCode();
			String hashString = new Integer(hash).toString();
			int choice = Math.abs(Integer.parseInt(hashString.substring(1, 2)));
			String s = words[transWord.length() - 1][choice];
			
			if (s.length() != word.length()) //this means we probably took punctuation out; stick it back on.
				s = s + word.substring(s.length());
			
			res += s + " ";
		}
		
		return res.trim();
	}
	
	private String removePunctuation(String word) {
		//This is kind of a ghetto way to do this currently...
		//it assumes the punctuation is on the start or the end of the string.
		//basically it just replaces all punctuation with spaces and then returns
		//a trimmed string. needs to be worked on later.
		word = word.replace(".", " ");
		word = word.replace(",", " ");
		word = word.replace("!", " ");
		word = word.replace("?", " ");
		word = word.replace(";", " ");
		word = word.replace(":", " ");
		return word.trim();
	}
}
