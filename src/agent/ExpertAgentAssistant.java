package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.encog.ml.bayesian.BayesianChoice;
import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;

public class ExpertAgentAssistant {
	private Agent agent;
	private HashMap<String, String> observations = new HashMap<String, String>(); 
	private Scanner scanner = new Scanner(System.in);
	
	public ExpertAgentAssistant(Agent agent) {
		this.agent = agent;
	}
	
	public void run() {
		String userInput;
		while (true) {
			this.getObservations();
			agent.defineEvidence();
			while (true) {
				this.getQueryType();
				agent.makeQuery();
				System.out.println("Do you want to make more queries? (y/n)");
				userInput = scanner.nextLine();
				if (userInput.equals("n")) {
					agent.getEvidence().clear();
					this.observations.clear();
					break;
				}
			}
			System.out.println("Do you want to exit the application? (y/n)");
			userInput = scanner.nextLine();
			if (userInput.equals("y")) {
				break;
			}
		}
	}
	
	private void getQueryType() {
		// ask the user to specify type of query 
		System.out.println("What type of query would you like to make? (diagnostic/predictive)");
		String queryType = this.scanner.nextLine();
		agent.setQueryType(queryType);
		if (!queryType.equals("diagnostic") && !queryType.equals("predictive")) {
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
		System.out.println("Make observation(s)");
        System.out.println("Usage: ");
        System.out.printf("%-30s %29s", "node, value", "Make observation about a node\n");
        System.out.printf("%-30s %22s", "done", "Confirm observation(s)\n\n");
		while(true) {
			try {
				this.printObservations();
				userInput = this.scanner.nextLine();
				if (userInput.equals("done")) {
					break;
				}
				observation = userInput.split("\\s*,\\s*");
				nodeLabel = observation[0];
				nodeValue = observation[1];
				this.observations.put(nodeLabel, nodeValue);
				ArrayList<BayesianChoice> choices = new ArrayList<BayesianChoice>();
				choices.addAll(this.agent.getNetwork().getEvent(nodeLabel).getChoices());
				for (BayesianChoice choice: choices) {
					if (choice.toString().equals(nodeValue)) {
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
		System.out.println("Current observations:");
		String key, value;
		for (Map.Entry<String, String> entry : this.observations.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    System.out.println(key + " " + value);
		}
	}
	
	private void createObservationMap(String nodeLabel, int choiceIndex) {
		HashMap<String, Integer> evidence = this.agent.getEvidence();
		evidence.put(nodeLabel, choiceIndex);
	}
}
