package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.encog.ml.bayesian.BayesianChoice;
import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;

public class ExpertAgentAssistant {
	private Agent agent;
	private Scanner scanner = new Scanner(System.in);
	
	public ExpertAgentAssistant(Agent agent) {
		this.agent = agent;
	}
	
	public void run() {
		this.getObservations();
		this.getQueryType();
		agent.makeQuery();
	}
	
	private void getQueryType() {
		// ask the user to specify type of query 
		System.out.println("What type of query would you like to make? (diagnostic/predictive)");
		String queryType =this.scanner.nextLine();
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
				userInput = this.scanner.nextLine();
				if (userInput.equals("done")) {
					break;
				}
				observation = userInput.split("\\s*,\\s*");
				nodeLabel = observation[0];
				nodeValue = observation[1];
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
	
	private void createObservationMap(String nodeLabel, int choiceIndex) {
		HashMap<String, Integer> evidence = this.agent.getEvidence();
		evidence.put(nodeLabel, choiceIndex);
	}
}
