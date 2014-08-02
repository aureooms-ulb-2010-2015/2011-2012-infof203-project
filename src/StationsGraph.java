import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;
import java.util.ArrayList;


public class StationsGraph extends Graph {
	private ArrayList<Node> _stations;
	
	public StationsGraph(){
		super();
		_stations = new ArrayList<Node>();
	}
	
	public void parseFileLine(String s,int numberOfNodes, int k){
		if(s.matches("^[a-zA-Z0-9]{1,}$")){
			 if(k<numberOfNodes){
				 setNode(s,new Node(s));
			 }
			 else{
				 _stations.add(this.getNode(s));
			 }
		}
		else if(s.matches("^[a-zA-Z0-9]{1,} [a-zA-Z0-9]{1,} [1-9][0,9]{0,}$")){
			String[] str;
			str=s.split(" ");
			Node nodeR = this.getNode(str[0]);
			Node nodeL = this.getNode(str[1]);
			if((nodeR != null) && (nodeL != null) && !nodeR.isBoundTo(nodeL)){
				int weight = Integer.parseInt(str[2]);
				Arc arc = new Arc(weight);
				nodeR.bind(nodeL, arc);
			}
		}
	}
	
	public void loadFile(String fileName) throws FileNotFoundException{
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		String line;
		try {
			line = file.readLine();
			int i=0;
			int numberOfNodes=Integer.parseInt(line);
			line = file.readLine();
			while(line != null){
				this.parseFileLine(line,numberOfNodes,i);
				line = file.readLine();
				++i;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runAlgorithm(String index) throws Exception{
		if(index.equals("1")){
			this.runAlgorithmOne();
		}
		else if(index.equals("2")){
			this.runAlgorithmTwo();
		}
		else{
			throw new Exception("L'algorithme "+index+" n'existe pas.");
		}
	}
	
	
	public void runAlgorithmOne(){
		//generated paths
		ArrayList<Path> paths = new ArrayList<Path>();

		//meilleure combinaison
		Path firstBest = new Path();
		Path secondBest = new Path();
		
		NodeCouple firstTravel = new NodeCouple(_stations.get(0),_stations.get(1));
		NodeCouple secondTravel = new NodeCouple(_stations.get(2),_stations.get(3));
		
		//first option
		paths.add(this.getSmallestPath(firstTravel));
		
		this.lockPath(paths.get(0));
		paths.add(this.getSmallestPath(secondTravel));
		this.unlockPath(paths.get(0));
		
		//second option
		paths.add(this.getSmallestPath(secondTravel));

		if (paths.get(1).equals(paths.get(2))){
			//results are the same
			firstBest.copy(paths.get(0));
			secondBest.copy(paths.get(1));
		}
		else{
			this.lockPath(paths.get(2));
			paths.add(this.getSmallestPath(firstTravel));
			this.unlockPath(paths.get(2));
			
			
			if((paths.get(0).size()>0 && paths.get(1).size()>0) &&
				(paths.get(0).weight() + paths.get(1).weight() < paths.get(2).weight() + paths.get(3).weight()
				|| !(paths.get(2).size()>0 && paths.get(3).size()>0) )){
				//first option is the best
				firstBest.copy(paths.get(0));
				secondBest.copy(paths.get(1));
			}
			else if (paths.get(2).size()>0 && paths.get(3).size()>0){
				//second option is the best
				firstBest.copy(paths.get(3));
				secondBest.copy(paths.get(2));
			}
			
		}

		this.printSolution(firstBest,secondBest);
	}
	
	
	public void runAlgorithmTwo(){
		NodeCouple firstTravel = new NodeCouple(_stations.get(0),_stations.get(1));
		NodeCouple secondTravel = new NodeCouple(_stations.get(2),_stations.get(3));



		//meilleure combinaison
		Path firstBest = new Path();
		Path secondBest = new Path();

		Path firstShortest = this.getSmallestPath(firstTravel);
		Path secondShortest = this.getSmallestPath(secondTravel);
		
		if (!firstShortest.crosses(secondShortest)){
			firstBest = firstShortest;
			secondBest = secondShortest;
		}

		else{

			//variables de travail
			Path firstPath = new Path();
			Path secondPath = new Path();
			
			firstPath.add(firstTravel.begin());
			secondPath.add(secondTravel.begin());
			
			this.allCombinationRecc(firstBest, secondBest, firstPath, secondPath, firstTravel.end(), secondTravel.end(), secondShortest);
		}
		
		this.printSolution(firstBest,secondBest);
		
		
	}
	
	public void allCombinationRecc(Path firstBest, Path secondBest, Path firstPath, Path secondPath, Node firstPathEnd, Node secondPathEnd, Path secondShortest){
		
		if ( firstPath.isFromTo(firstPath.first(),firstPathEnd) ){
			//si le chemin 1 est complet
			//création des seconds chemins
			this.secondPathRecc(firstBest, secondBest, firstPath, secondPath, firstPathEnd, secondPathEnd);
		}
		else{
			// ajout d'un noeud
			for (int i = 0; i < firstPath.last().numberOfBoundNodes(); i++){
				
				if ( firstPath.has(firstPath.last().getBoundNodes().get(i))){
					//si le chemin contient déjà ce noeud
					continue;
				}
				
				firstPath.add(firstPath.last().getBoundNodes().get(i));
				
				if ( !(firstBest.isEmpty() && secondBest.isEmpty()) && firstPath.weight() + secondShortest.weight() > firstBest.weight() + secondBest.weight()){
					//si la somme des poids du premier chemin et du meilleur second chemin est plus élevée que celle de la meilleure solution courrante, on passe
					//(dans le meilleur des cas la meilleur combinaison contient le meilleur second chemin)
					firstPath.pop();
					continue;
				}
				
				//appel récursif
				this.allCombinationRecc(firstBest, secondBest, firstPath, secondPath, firstPathEnd, secondPathEnd, secondShortest);
			}
		}
		if (firstPath.size() > 1){
			//si le parcours récursif est terminé, on enlè�ve le dernier noeud
			firstPath.pop();
		}
	}
	
	public void secondPathRecc(Path firstBest, Path secondBest, Path firstPath, Path secondPath, Node firstPathEnd, Node secondPathEnd){

		
		if ( secondPath.isFromTo(secondPath.get(0),secondPathEnd) ){
			//si le second chemin est complet
			
			if ( (firstBest.isEmpty() && secondBest.isEmpty())
			  || ( firstPath.weight() + secondPath.weight() < firstBest.weight() + secondBest.weight())){
				//si la solution est meilleure que la solution courrante
				//ou si la solution courrante n'existe pas
				
				firstBest.copy(firstPath);
				secondBest.copy(secondPath);
			}
		}
		
		else{
			// ajout d'un noeud
			for (int i = 0; i < secondPath.last().numberOfBoundNodes(); i++){
				
				if ( secondPath.has(secondPath.last().getBoundNodes().get(i))){
					//si le chemin contient déjà ce noeud
					continue;
				}
				
				secondPath.add(secondPath.last().getBoundNodes().get(i));
				
				if ( secondPath.crosses(firstPath) || !(firstBest.isEmpty() && secondBest.isEmpty()) && firstPath.weight() + secondPath.weight() > firstBest.weight() + secondBest.weight()){
					//si le second chemin coupe la route du premier
					//ou si la somme des poids du chemin 1 calculé précédemment et du chemin 2 est plus élevé que celle de la solution courrante
					secondPath.pop();
					continue;
				}
				//appel récursif
				this.secondPathRecc(firstBest, secondBest, firstPath, secondPath, firstPathEnd, secondPathEnd);
			}
		}
		if (secondPath.size() > 1){
			//si le parcours récursif est terminé, on enlève le dernier noeud
			secondPath.pop();
		}
	}
	
	public void printSolution(Path first, Path second){
		System.out.print("Existence d'une solution : ");
		if ( !first.isEmpty() && !second.isEmpty() ){
			//solution trouvée
			System.out.println("oui");

			System.out.println("Trajet pour le train 1/temps : " + first.toString());
			System.out.println("Trajet pour le train 2/temps : " + second.toString());
			
			System.out.print("Temps total est : ");
			System.out.println(first.weight() + second.weight());
			
			this.showPath(first, new Color(20,20,255));
			this.showPath(second,new Color(255,20,20));
		}
		else{
			System.out.println("non");
		}
	}
	
	public void showPath(Path path, Color color){
		path.show(color);
	}
	
	public void lockPath(Path path){
		path.lock();
	}

	public void unlockPath(Path path){
		path.unlock();
	}
}