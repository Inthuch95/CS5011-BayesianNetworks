package main;

import agent.Agent;
import agent.ExpertAgentAssistant;

public class Main {

	public static void main(String[] args) {
		Agent agent = new Agent("prob2.xml");
		ExpertAgentAssistant eaa = new ExpertAgentAssistant(agent);
		eaa.run();
	}

}
