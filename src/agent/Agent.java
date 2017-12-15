package agent;

import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.bif.BIFUtil;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Agent {
	private BayesianNetwork network;
	private HashMap<String, Integer> evidence = new HashMap<String, Integer>(); 
	private ArrayList<BayesianEvent> outcome = new ArrayList<BayesianEvent>(); 
	private String queryType;
	private EnumerationQuery query;
	
	public Agent(String file) {
		this.network =  BIFUtil.readBIF(file);
	}
	
	public void makeQuery() {
		this.query = new EnumerationQuery(this.network);
		// define evidence and outcome
		this.defineEvidence();
		this.defineOutcome();
		query.execute();
		System.out.println(query.getProblem());
		System.out.println(query.toString());
	}
	
	private void defineEvidence() {
		for (Map.Entry<String, Integer> entry : this.evidence.entrySet()) {
		    String key = entry.getKey();
		    int value = entry.getValue();
		    BayesianEvent event = this.network.getEvent(key);
		    query.defineEventType(event, EventType.Evidence);
		    query.setEventValue(event, value);
		}
	}
	private void defineOutcome() {
		BayesianEvent fever = this.network.getEvent("Fever");
	    query.defineEventType(fever, EventType.Outcome);
	    query.setEventValue(fever, 0);
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public HashMap<String, Integer> getEvidence() {
		return evidence;
	}

	public void setEvidence(HashMap<String, Integer> evidence) {
		this.evidence = evidence;
	}

	public ArrayList<BayesianEvent> getOutcome() {
		return outcome;
	}

	public void setOutcome(ArrayList<BayesianEvent> outcome) {
		this.outcome = outcome;
	}

	public BayesianNetwork getNetwork() {
		return network;
	}

	public void setNetwork(BayesianNetwork network) {
		this.network = network;
	}
}
