package main;

import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;
import org.encog.ml.bayesian.bif.BIFUtil;

public class Main {

	public static void main(String[] args) {
		BayesianNetwork network =  BIFUtil.readBIF("prob2.xml");
		// display basic stats
		System.out.println(network.getContents());
		System.out.println("Parameter count: " + network.calculateParameterCount());
		BayesianEvent therm = network.getEvent("Therm");
		BayesianEvent fever = network.getEvent("Fever");
		EnumerationQuery query = new EnumerationQuery(network);
		query.defineEventType(fever, EventType.Evidence);
		query.defineEventType(therm, EventType.Outcome);
		query.setEventValue(fever, 1);
		query.setEventValue(therm, 1);
		query.execute();
		System.out.println(query.toString());
	}

}
