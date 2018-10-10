import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class SAT 
{

	public static void main(String[] args)
	{
		System.out.println("--- Debut du main ---");
		Scanner saisie;

		Graph<String> G = new Graph<String>(0);
		Graph<String> Gt = new Graph<String>(0);

		try
		{
			List<Graph<String>> array = Arrays.asList(G, Gt);
			if (args.length == 0)
			{
				System.out.println("--- Pas de fichiers ---");
				return;
			}

			System.out.println("--- Ouverture du fichier nommé : " + args[0] +" ---");
			saisie = new Scanner(args[0]);
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

		System.out.println("\n--- Graphe des implications de G avec les sommets correctement indexés ---\n" + G.toString());
		System.out.println("--- Graphe des implications de Gt avec les sommets correctement indexés ---\n"+ Gt.toString());


		int [] endDate = new int [G.order()];
		boolean[] color = new boolean [G.order()];

		for(int i = 0;i<endDate.length;i++)
		{
			color[i] = false;
			endDate[i] = 0;
		}

		System.out.println("--- Parcours en profondeur sur le graphe G ---");
		int fin = 0;
		for(int i = 0; i < G.order();i++)
		{
			if(color[i] == false) 
			{
				fin = pathDepthFirstSearch(G,i,endDate,color,fin);
			}
			System.out.println("Date de fin du sommet "+ i + " : " + endDate[i] );
		}
		
		for(int i = 0;i<color.length;i++)
		{
			color[i] = false;
		}


		System.out.println("\n--- Calcul des composantes fortement connexes ---");
		List<List<Integer>> TESTarray =  new ArrayList<>();
		int Bignomber =0;
		int index =0;
		
		for(int i = 0; i < Gt.order();i++)
		{
			Bignomber =0;
			for(int p = 0; p<Gt.order();p++)
			{
				if(Bignomber < endDate[p] && color[p] == false)
				{
					Bignomber = endDate[p];
					index = p;
				}
			}
			
			if(color[index] == false) 
			{
				TESTarray.add(new ArrayList<>()); 
				PutArray(Gt,TESTarray,index,color);
				
			}
		}

		

		 for(int i = 0; i< TESTarray.size();i++)
        {
        	System.out.println(i+1 +" = " +TESTarray.get(i));
        }

        System.out.println("Il y a  "+ TESTarray.size() + " composantes fortement connexes\n");

        System.out.println("--- Test si la composante contient son littéral et son opposé ---");
        boolean l = TestopposedLiteral(TESTarray,endDate.length);

        if(!l)
        	System.out.println("\nRésultat = Le 2SAT est satisfaisable");
        else
        	System.out.println("\nRésultat = Le 2SAT n'est pas satisfaisable");

	}

	/*ReadFile va lire le fichier et retourné 2 graphes (Le graphe normale et la transposé du graphe)*/
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

	/*Calcule les litteraux*/
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

	/*Transforme les litteraux en index*/
	public static int transform(int n, int size) 
	{
		if(n<0)
			n = n + (size/2);
		else
			n = n +(size/2)-1;

		return n;
	}

	/*Parcours en profondeur (fonction récursive) et met en place les dates de fin dans le tableau array*/
	public static int  pathDepthFirstSearch(Graph<String> _g,int _cardinal,int [] array,boolean [] color,int dateFin)
	{
		if(color[_cardinal] == false)
		{	
			color[_cardinal] = true;
			++dateFin;
			for(int i = 0; i< _g.getIncidency(_cardinal).size();i++) 
			{
				dateFin = pathDepthFirstSearch(_g,_g.getDestinationEdge(_cardinal, i),array,color,dateFin);
			}	
			array[_cardinal] = ++dateFin;
		}

		return dateFin;
	}

	/*Fait le parcours en profondeur sur la transposé en partant du sommet possédant  la plus grande date de fin*/
	public static void PutArray(Graph<String> Gt,List<List<Integer>> returnList,int _cardinal,boolean [] color)
	{
		
		if(color[_cardinal] == false)
		{	
			color[_cardinal] = true;
			returnList.get(returnList.size()-1).add(_cardinal);
			//System.out.println(_cardinal +" = " + Gt.getIncidency(_cardinal).size());
			for(int i = 0; i< Gt.getIncidency(_cardinal).size();i++) 
			{
				PutArray(Gt,returnList,Gt.getDestinationEdge(_cardinal, i),color);	
			}	
			
		}

	}



	public static boolean  TestopposedLiteral(List<List<Integer>> array,int size)
	{
		boolean test = false;
		for(int i = 0; i < array.size();i++)
		{
			for(int p = 0; p < array.get(i).size();p++)
			{
				test = array.get(i).contains((size-1)-array.get(i).get(p));
				System.out.println("Sommet " + array.get(i).get(p) + " | " + test);
				if(test) {
					return true;
				}
			}
		}
		return test;
	}
}

