import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
//import java.util.ArrayList;
//import java.util.List;

public class Implication 
{

    public static void main(String[] args)
    {
        System.out.println("--- Debut du main ---");
        Scanner saisie;
        Graph<String> G = new Graph<String>(0);
        Graph<String> Gt = new Graph<String>(0);
        
        try //Passer dans la fonction lecture fichier
        {
        	List<Graph<String>> array = Arrays.asList(G, Gt);
            if (args.length == 0)
            {
            	System.out.println("--- Pas de fichiers ---");
                return;
            }
            
            System.out.println("--- Ouverture du fichier ---");
            saisie = new Scanner(args[0]);
            System.out.println("Fichier : " + args[0]);
            File file = new File(saisie.nextLine());
            saisie.close();
            
            saisie = new Scanner(file);
            array = readFile(saisie,G,Gt); 
            saisie.close();
            System.out.println("--- Fermeture du fichier ---");
            
            G = array[0];
            Gt = array[1];
            
        }
        catch(Exception ex) 
        {
            ex.printStackTrace();
            return;
        }
        
        System.out.println("G \n" + G.toString());
        System.out.println("Gt \n"+ Gt.toString());
       
    }
    
    public static Graph<String>[] readFile(Scanner file,Graph<String> _G,Graph<String> _Gt) 
    {
    	Graph<String> [] returnArray = new Graph<String> [2];
    	
    	System.out.println("--- Lecture du fichier ---");
    	
    	String line = file.nextLine();
    	_G = new Graph<String>((Integer.parseInt(line))*2);
    	_Gt = new Graph<String>((Integer.parseInt(line))*2);
    	String [] Sendarray = new String [2];
    	
    	System.out.println("--- Organisation du graphe ---");
    	while (file.hasNextLine())
        {
            line = file.nextLine();
            Sendarray = line.split(" ");
            if(Sendarray[0] != " "){
            	buildArray(_G,_Gt,Sendarray);
            }   
        }
    	
    	returnArray[0] = _G;
    	returnArray[1] = _Gt;
    	
    	return returnArray;
    	
    }
    
    public static void buildArray(Graph<String> _G,Graph<String> _Gt,String [] array) 
    {
    	
       int [] l1 = new int [2];
   	   int [] l2 = new int [2];
   	   
   	   l1[0] = (-1)*(Integer.parseInt(array[0]));
   	   l1[1] = (Integer.parseInt(array[1]));
   	 
   	   l2[0] = (-1)*(Integer.parseInt(array[1]));
 	   l2[1] = (Integer.parseInt(array[0]));
 	   
 	   _G.addArc(transform(l1[0],_G.order()), transform(l1[1],_G.order()), "");
 	   _G.addArc(transform(l2[0],_G.order()), transform(l2[1],_G.order()), "");
 	   
 	  _Gt.addArc(transform(l1[1],_G.order()), transform(l1[0],_G.order()), "");
	  _Gt.addArc(transform(l2[1],_G.order()), transform(l2[0],_G.order()), "");
 	   
    }
    
    public static int transform(int n, int size) 
	{
    	if(n<0)
    	 n = n + (size/2);
    	else
    	 n = n +(size/2)-1;
		
		return n;
	}    
    
}
