package textprocessing;
import java. util. Scanner;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class WebBrowser {
	Hashtable< String, Map<String, Integer>> outerhashtable = new Hashtable<>();
	int count=0;
	//Reading text files and storing them into string  
	String readFile(String fileName) throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(fileName));
	try {
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
		    sb.append("\n");
		    line = br.readLine();
		 }
		 return sb.toString();
		 } 
	finally {
		 br.close();
		 }
	}
		
	//converting HTML documents into text files
		public void htmlToText(File path,String filename) throws IOException {
			WebBrowser wb= new WebBrowser();
	    	String htmlString = wb.readFile(path.toString());
	        //StdOut.println("Created " + filename);
	        //Jsoup parsing
	        Document doc = Jsoup.parse(htmlString);
	        
	        
	        //Removing unwanted information like ads, reviews
	        //header/footer, etc from the text files
	        doc.getElementsByClass("recipe-page-right-rail four-tabs sticky-ads").remove();
	        doc.getElementById("pageFooter").remove();
	        doc.getElementById("reviews").remove();
	        doc.getElementById("polaris-app").remove();	        
	        doc.getElementsByClass("heading__h2--gutters share").remove();
	        doc.getElementsByClass("header new-nav").remove();
	        doc.getElementsByClass("similar-recipes--details ng-scope").remove();
	        doc.select("a").remove();
	        
	        String title = doc.title();
	        String body = doc.body().text();        
	        //Writing to a new file
	        BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\textfiles\\"+filename));
	        writer.write(title);
	        writer.write(body);
	        writer.close();
	    }
		//Storing the inverted index in a file
		public void printHashTable() throws IOException{
			
			FileWriter writer = new FileWriter("E:\\keywords.txt");
			//BufferedWriter bw = new BufferedWriter(writer);					
			Iterator<String> itr = outerhashtable.keySet().iterator();         
            while(itr.hasNext()) 
            {
                String htkey = itr.next();
                Map mappedValue = outerhashtable.get(htkey);	                 
                writer.write("\nKey: " + htkey + ", Value: " + mappedValue);
                
            }
            writer.close();
			
		}
		//Removing stop keywords
		public void createIndexFile(StringTokenizer st, String fname) {
			List<String> stopwords = Arrays.asList("a", "able", "about",
					"across","advertisement", "after", "all", "allrecipes", "almost", 
					"also", "am", "among", "an", "and", "any", "are", "as", "at", 
					"be", "because", "been", "but", "by", "can", "cannot", "could", 
					"dear", "did", "do", "does", "either", "else", 
					"ever", "every", "for", "from", "get", "got", "had", "has",
					"have", "he", "her", "hers", "him", "his", "how", "however", 
					"i", "if", "in","ingredients", "into", "is", "it", "its", "just",
					"least", "let", "like", "likely", "may", "me", "might", "most",
					"must", "my", "neither", "no", "nor", "not", "of", "off", "often",
					"on", "only", "or", "other", "our", "own", "rather", "said", "say",
					"says", "she", "should", "since", "so", "some", "than", "that",
					"the", "their", "them", "then", "there", "these", "they", "this",
					"tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
					"what", "when", "where", "which", "while", "who", "whom", "why",
					"will", "with", "would", "yet", "you", "your");
		 
			//Creating Inverted Index
			while(st.hasMoreTokens()) {
	    		String unprocessedkey=st.nextToken();
	    		//System.out.println(unprocessedkey);
	    		String key=unprocessedkey.replaceAll("[^a-zA-Z]", " ");
	    		key=key.toLowerCase().trim();
	    		if (stopwords.contains(key)==false) {
	    			 Map<String, Integer> keyvalue;
	    			if (outerhashtable.containsKey(key)) {
	    					keyvalue=outerhashtable.get(key);
	    					if(keyvalue.containsKey(fname)) {	    						
	    						int val= keyvalue.get(fname);
	    						keyvalue.put(fname,++val);
	    					}
	    					else
	    						keyvalue.put(fname,1);
	    			} 
	    			else {
	    					Map<String, Integer> newkey=new HashMap<>();
	    					newkey.put(fname,1);
	    					outerhashtable.put(key,newkey);	    					
	    				}
	    		}	    		
	    	}  
		}
		//Page Ranking according to count
		public LinkedHashMap<String, Integer> sortHashMapByValues(Map<String, Integer> passedMap) {
		    List<String> mapKeys = new ArrayList<>(passedMap.keySet());
		    List<Integer> mapValues = new ArrayList<>(passedMap.values());
		    Collections.sort(mapValues);
		    Collections.sort(mapKeys);

		    LinkedHashMap<String, Integer> sortedMapTemp = new LinkedHashMap<>();

		    Iterator<Integer> valueIt = mapValues.iterator();
		    while (valueIt.hasNext()) {
		        Integer val = valueIt.next();
		        Iterator<String> keyIt = mapKeys.iterator();

		        while (keyIt.hasNext()) {
		            String key = keyIt.next();
		            Integer comp1 = passedMap.get(key);
		            Integer comp2 = val;

		            if (comp1.equals(comp2)) {
		                keyIt.remove();
		                sortedMapTemp.put(key, val);
		                break;
		            }
		        }
		    }
		    
		    List<String> mapK = new ArrayList<>(sortedMapTemp.keySet());
		    List<Integer> mapV = new ArrayList<>(sortedMapTemp.values());
		    Collections.reverse(mapV);
		    Collections.reverse(mapK);

		    LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
		    Iterator<Integer> vIt = mapV.iterator();
		    Iterator<String> kIt = mapK.iterator();
		    while (vIt.hasNext()) {
		    	sortedMap.put(kIt.next(), vIt.next());
		    }
		    
		    return sortedMap;
		}
		//Edit Distance algorithm
		public static int minDistance(String word1, String word2) {
			int len1 = word1.length();
			int len2 = word2.length();
		 
			// len1+1, len2+1, because finally return dp[len1][len2]
			int[][] dp = new int[len1 + 1][len2 + 1];
		 
			for (int i = 0; i <= len1; i++) {
				dp[i][0] = i;
			}
		 
			for (int j = 0; j <= len2; j++) {
				dp[0][j] = j;
			}
		 
			//iterate though, and check last char
			for (int i = 0; i < len1; i++) {
				char c1 = word1.charAt(i);
				for (int j = 0; j < len2; j++) {
					char c2 = word2.charAt(j);
		 
					//if last two chars equal
					if (c1 == c2) {
						//update dp value for +1 length
						dp[i + 1][j + 1] = dp[i][j];
					} else {
						int replace = dp[i][j] + 1;
						int insert = dp[i][j + 1] + 1;
						int delete = dp[i + 1][j] + 1;
		 
						int min = replace > insert ? insert : replace;
						min = delete > min ? min : delete;
						dp[i + 1][j + 1] = min;
					}
				}
			}
		 
			return dp[len1][len2];
		}
		public static void main(String args[]) throws IOException {
			//Creating objects variables and paths
			WebBrowser wb= new WebBrowser();
			System.out.println("~~~~~Welcome to Recipe Scraper~~~~~");
			System.out.println("This is a collection of all your favourite recipes");
			//Task 1 : HTML to text converter~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		    File directory = new File("E:\\webscarpe\\");   
		    File[] myarray;  
		    myarray=new File[650];
		    myarray=directory.listFiles(); 
		    String strtemp[]= new String[650];
		    String str[]= new String[650];
		    for (int j = 0; j < 650; j++)//myarray.length; j++)
		    {
		    strtemp[j]= myarray[j].getName().replaceFirst("[.][^.]+$", "");
	    	str[j] = strtemp[j].substring(0, strtemp[j].length() - 17)+".txt";
		    }
		    for (int j = 0; j < 650; j++)//myarray.length; j++)
		    {
		    		    	
		    	try {
		    	//wb.htmlToText(myarray[j],str[j]); 		    		
		    	//Task 2 : Creating inverted Index			    
		    	File fcheck=new File("E:\\textfiles\\"+str[j]);
		    	if(fcheck.exists()) {
		    	String txt= wb.readFile("E:\\textfiles\\"+str[j]);
		    	StringTokenizer stkeys = new StringTokenizer(txt);
		    	wb.createIndexFile(stkeys, str[j]);
		    	}
		    	}
		    	catch(NullPointerException npe)
		    	{
		    		continue;
		    	}
		    }
		    /*for (int j = 0; j < 650; j++)//myarray.length; j++)	
		    {
		    	wb.printHashTable();
		    }*/
	    
			//Task 3 : Searching keys ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
		    Map<String, Integer> searchmap;
		    Scanner in = new Scanner(System. in);
		    String searchkey="recipe";
		    while(searchkey!="e") {
		    System.out.println("Search for : ");
		    searchkey = in. nextLine();
		    searchmap=wb.outerhashtable.get(searchkey);
		    //searchma.put(searchmap);
		    
		    Map<String, Integer> sortedsearchmap;
		    try {
		    sortedsearchmap=wb.sortHashMapByValues(searchmap);
		    Iterator<String> itr = sortedsearchmap.keySet().iterator();
            
		    System.out.println("Search Result : ");
            while(itr.hasNext()) 
            {
            	System.out.println("*  "+itr.next());
            	
            }
		    }
		    catch(NullPointerException npe) {
		    
		    
		    //Task 3 : Edit Distance ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		    Set<String> outerkeys = wb.outerhashtable.keySet();
		    System.out.println("Oops! Word not found, try other options ");
	        for(String key: outerkeys)
	        {
	        	int dis=minDistance(searchkey,key);
	        	if(dis==1)
	        	System.out.println(key);
	        }
		    }
		    }
		   
		}
}
