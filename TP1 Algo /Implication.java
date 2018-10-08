import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
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
            
            G = array.get(0);
            Gt = array.get(1);
            
        }
        catch(Exception ex) 
        {
            ex.printStackTrace();
            return;
        }
        
        System.out.println("G \n" + G.toString());
        System.out.println("Gt \n"+ Gt.toString());
        //Mettre les variables à 0.
        int fin = 0;
        int [] array = new int [G.order()];
        
        for(int i = 0;i<array.length;i++)
        {
        	
        	array[i] = 0;
        }
        
        //Mettre les dates de fin sur chaque sommet
        //Fonction à faire
        for(int i = 0; i < G.order();i++)
    	{
        	if(array[i] == 0) 
        	{
        		fin = pathDepthFirstSearch(G,i,array,fin);
        	}
    	}
        
        
        
        for(int i = 0; i<array.length;i++)
        {
        	 System.out.println("Date de fin du sommet "+ i + " : " + array[i]);
        }
        
    }
    
    public static List<Graph<String>> readFile(Scanner file,Graph<String> _G,Graph<String> _Gt) 
    {
    	List<Graph<String>> returnArray = Arrays.asList(_G,_Gt);
    	
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
    	
    	returnArray.set(0,_G);
    	returnArray.set(1,_Gt);
    	
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
   
    public static int  pathDepthFirstSearch(Graph<String> _g,int _cardinal,int [] array,int dateFin)
    {
    	
    	if(array[_cardinal] == 0)
    	{	
    		System.out.println(_cardinal);
    		array[_cardinal] = ++dateFin;
    		for(int i = 0; i< _g.getIncidency(_cardinal).size();i++) 
    		{
    			System.out.println("->");
    			dateFin = pathDepthFirstSearch(_g,_g.getDestinationEdge(_cardinal, i),array,dateFin);
    			System.out.println("stop");
    		}
    		array[_cardinal] = ++dateFin;
    		System.out.println(_cardinal +" = " + array[_cardinal] + "(date de fin)");
    	}
    	
    	
    	return dateFin;
    }

    public static Graph<String>  PutArray(Graph<String> Gt,int [] array)
    {
    	int Bignomber;

    	int o = 0;
    	Graph<String> Gconnexe = new Graph<String>(0);
    	
    	for(int i = 0; i < array.length;i++)
    	{
    		Bignomber = 0;
    		for(int p = 0; p < array.length; p++)
    		{
    			if(Bignomber < array[p])
    			{
    				o = p;
    				Bignomber = array[p];
    			}
    		}
    		
    		array[o] = 0;
    		
    		if(Gt.getIncidency(o).size() > 0)
    		{
    			for(int l = 0; l < Gt.getIncidency(o).size();l++)
    			{
    				///Gt.getDestinationEdge(o, l)
    			}
    		}
    		
    	}
    	
    	return Gconnexe;
    }
}
