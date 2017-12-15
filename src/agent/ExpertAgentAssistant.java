package agent;

import java.util.ArrayList;
import java.util.Scanner;

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
		this.getObservation();
		agent.makeQuery();
	}
	
	private void getQueryType() {
		// ask the user to specify type of query 
		System.out.println("What type of query would you like to make? (diagnostic/predictive)");
		String queryType =this.scanner.nextLine();
		agent.setQueryType(queryType);
	}
	
	private void getObservation() {
		BayesianNetwork network = agent.getNetwork(); 
		ArrayList<BayesianEvent> evidence = this.agent.getEvidence();
		String userInput = "";
		String[] observation;
		while(true) {
			System.out.println("Do you want to add more observation? (node value)");
			userInput = this.scanner.nextLine();
			if (userInput.equals("done")) {
				break;
			}
			observation = userInput.split("\\s+");
			BayesianEvent node = network.getEvent(observation[0]);
			evidence.add(node);
		}
		System.out.println(evidence);
	}
}
