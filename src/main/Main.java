package main;

import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;

import agent.Agent;
import agent.ExpertAgentAssistant;

import org.encog.ml.bayesian.bif.BIFUtil;

public class Main {

	public static void main(String[] args) {
		String file = args[0];
		Agent agent = new Agent(file);
		ExpertAgentAssistant eaa = new ExpertAgentAssistant(agent);
		eaa.run();
		
//		BayesianNetwork network =  BIFUtil.readBIF("prob2.xml");
//		// display basic stats
//		System.out.println(network.getContents());
//		System.out.println("Parameter count: " + network.calculateParameterCount());
//		BayesianEvent influenza = network.getEvent("Influenza");
//		BayesianEvent therm = network.getEvent("Therm");
//		BayesianEvent fever = network.getEvent("Fever");
//		// query the network
//		EnumerationQuery query = new EnumerationQuery(network);
//		System.out.println(query.getProblem());
//		query.defineEventType(fever, EventType.Evidence);
//		query.defineEventType(therm, EventType.Outcome);
//		query.setEventValue(fever, 0);
//		query.setEventValue(therm, 1);
//		query.execute();
//		System.out.println(query.getProblem());
//		System.out.println(query.toString());
////		query = new EnumerationQuery(network);
//		query.defineEventType(fever, EventType.Outcome);
//		query.defineEventType(influenza, EventType.Evidence);
//		query.defineEventType(therm, EventType.Hidden);
//		query.setEventValue(fever, 0);
//		query.setEventValue(influenza, 0);
//		query.execute();
//		System.out.println(query.getProblem());
//		System.out.println(query.toString());
	}

}
