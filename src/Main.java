
public class Main {

	public static void main(String[] args) {
		try {
			if(args.length != 2){
				throw new Exception("Nombre d'arguments incorrect!");
			}
			if( !args[1].equals("1") && !args[1].equals("2")){
				throw new Exception("L'algorithme "+args[1]+" n'existe pas.");
			}
			StationsGraph g = new StationsGraph();
			g.loadFile(args[0]);
			g.runAlgorithm(args[1]);
			g.displayGraph();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}

