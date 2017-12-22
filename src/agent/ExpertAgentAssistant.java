package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.encog.ml.bayesian.BayesianChoice;

/**
 * Handles interaction between the agent and the user. 
 *
 */
public class ExpertAgentAssistant {
	private Agent agent;
	private HashMap<String, String> observations = new HashMap<String, String>(); 
	private Scanner scanner = new Scanner(System.in);
	private String queryType;
	
	public ExpertAgentAssistant(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Interacts with the user and pass the information to the agent in order to make queries. 
	 */
	public void run() {
		String userInput;
		// let the user make observations
		this.getObservations();
		agent.defineEvidence();
		while (true) {
			// ask the user to specify query type
			this.getQueryType();
			// make the query and display the result
			agent.makeQuery(this.queryType);
			this.printQueryResult();
			agent.getQueryResult().clear();
			System.out.println("Do you want to make more queries? (y/n)");
			userInput = scanner.nextLine();
			if (userInput.equals("n")) {
				agent.getEvidence().clear();
				this.observations.clear();
				break;
			}
		}
	}
	
	private void getQueryType() {
		// ask the user to specify type of query 
		System.out.println("What type of query would you like to make? (diagnostic/predictive)");
		this.queryType = this.scanner.nextLine();
		if (!this.queryType.equals("diagnostic") && !this.queryType.equals("predictive")) {
			System.out.println("Unsupoorted query type!");
			this.getQueryType();
		}
	}
	
	private void getObservations() {
		String userInput = "";
		String[] observation;
		String nodeLabel;
		String nodeValue;
		boolean choiceMatched = false;
		int choiceIndex;
		this.printUsage();
		while(true) {
			try {
				this.printObservations();
				userInput = this.scanner.nextLine();
				// proceed to making queries when the user finished making observations
				if (userInput.equals("done")) {
					break;
				}
				// split the input using comma
				observation = userInput.split("\\s*,\\s*");
				nodeLabel = observation[0];
				nodeValue = observation[1];
				ArrayList<BayesianChoice> choices = new ArrayList<BayesianChoice>();
				choices.addAll(this.agent.getNetwork().getEvent(nodeLabel).getChoices());
				// create mapping between nodes and observed values
				for (BayesianChoice choice: choices) {
					if (choice.toString().equals(nodeValue)) {
						this.observations.put(nodeLabel, nodeValue);
						choiceIndex = choices.indexOf(choice);
						choiceMatched = true;
						this.createObservationMap(nodeLabel, choiceIndex);
					}
				}
				if (!choiceMatched) {
					System.out.println("Invalid command!");
				}
				choiceMatched = false;
			} catch (Exception e) {
				System.out.println("Invalid command!");
			}
		}
	}
	
	private void printObservations() {
		// show current observations to the user
		System.out.println("Current observations:");
		String key, value;
		for (Map.Entry<String, String> entry : this.observations.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    System.out.println(key + " " + value);
		}
	}
	
	private void printQueryResult() {
		// show query results in a user friendly way
		HashMap<String, Double> queryResult = agent.getQueryResult();
		double probability;
		if (this.queryType.equals("diagnostic")) {
			System.out.println("The likely causes are:");
		} else {
			System.out.println("The effects are:");
		}
		for (Map.Entry<String, Double> entry : queryResult.entrySet()) {
			probability = entry.getValue() * 100.0;
			System.out.println(entry.getKey() + " with " + probability + "% probability");
		}
		System.out.println("");
	}
	private void printUsage() {
		// show list of commands when making observations about the patient
		System.out.println("Make observation(s)");
        System.out.println("Usage: ");
        System.out.printf("%-30s %29s", "node, value", "Make observation about a node\n");
        System.out.printf("%-30s %22s", "done", "Confirm observation(s)\n\n");
	}
	
	private void createObservationMap(String nodeLabel, int choiceIndex) {
		// create mapping between nodes and observed values
		HashMap<String, Integer> evidence = this.agent.getEvidence();
		evidence.put(nodeLabel, choiceIndex);
	}
}
