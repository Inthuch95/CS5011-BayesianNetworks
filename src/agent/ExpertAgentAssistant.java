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
		this.getQueryType();
		this.getObservations();
		agent.makeQuery();
	}
	
	private void getQueryType() {
		// ask the user to specify type of query 
		System.out.println("What type of query would you like to make? (diagnostic/predictive)");
		String queryType =this.scanner.nextLine();
		agent.setQueryType(queryType);
	}
	
	private void getObservations() {
		String userInput = "";
		String[] observation;
		while(true) {
			System.out.println("Do you want to add more observation? (node value)");
			userInput = this.scanner.nextLine();
			if (userInput.equals("done")) {
				break;
			}
			observation = userInput.split("\\s+");
			this.createObservationMap(observation);
		}
	}
	
	private void createObservationMap(String[] observation) {
		HashMap<String, Integer> evidence = this.agent.getEvidence();
		BayesianNetwork network = agent.getNetwork(); 
		BayesianEvent node = network.getEvent(observation[0]);
		ArrayList<BayesianChoice> choices = new ArrayList<BayesianChoice>();
		int choiceNumber = 0;
		choices.addAll(node.getChoices());
		for (BayesianChoice choice: choices) {
			if (choice.toString().equals(observation[1])) {
				choiceNumber = choices.indexOf(choice);
			}
		}
		evidence.put(observation[0], choiceNumber);
	}
}
