package agent;

import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.bif.BIFUtil;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Agent {
	private BayesianNetwork network;
	private HashMap<String, Integer> evidence = new HashMap<String, Integer>(); 
	private Set<BayesianEvent> outcome = new HashSet<BayesianEvent>(); 
	private String queryType;
	private EnumerationQuery query;
	
	public Agent(String file) {
		this.network =  BIFUtil.readBIF(file);
	}
	
	public void makeQuery() {
		this.query = new EnumerationQuery(this.network);
		// define evidence and outcome, then execute the query
		this.defineEvidence();
		this.defineOutcome();
		this.executeQuery();
	}
	
	private void defineEvidence() {
		BayesianEvent evidenceNode;
		String key;
		int value;
		for (Map.Entry<String, Integer> entry : this.evidence.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    evidenceNode = this.network.getEvent(key);
		    query.defineEventType(evidenceNode, EventType.Evidence);
		    query.setEventValue(evidenceNode, value);
		}
	}
	private void defineOutcome() {
	    Set<BayesianEvent> evidenceSet = new HashSet<BayesianEvent>();
	    ArrayList<BayesianEvent> removeList = new ArrayList<BayesianEvent>();
		for (String label : this.evidence.keySet()) {
	    	// make diagnostic or predictive query based on the user's choice
	    	if (this.queryType.equals("diagnostic")) {
	    		this.outcome.addAll(this.network.getEvent(label).getParents());
	    	} else {
	    		this.outcome.addAll(this.network.getEvent(label).getChildren());
	    	}
	    	evidenceSet.add(this.network.getEvent(label));
	    }
	    // remove node(s) from outcome if they are being observed
	    for (BayesianEvent outcomeNode: this.outcome) {
	    	if (evidenceSet.contains(outcomeNode)) {
	    		removeList.add(outcomeNode);
	    	}
	    }
	    this.outcome.removeAll(removeList);
	}
	
	private void executeQuery() {
		ArrayList<BayesianEvent> outcomeList = new ArrayList<BayesianEvent>();
		outcomeList.addAll(this.outcome);
		for (int i = 0; i < outcomeList.size(); i++) {
	    	if (i > 0) {
	    		query.defineEventType(outcomeList.get(i - 1), EventType.Hidden);
	    	}
			query.defineEventType(outcomeList.get(i), EventType.Outcome);
	    	query.setEventValue(outcomeList.get(i), 0);
	    	query.execute();
			System.out.println(query.getProblem());
			System.out.println(query.toString());
	    }
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

	public BayesianNetwork getNetwork() {
		return network;
	}

	public void setNetwork(BayesianNetwork network) {
		this.network = network;
	}

	public Set<BayesianEvent> getOutcome() {
		return outcome;
	}

	public void setOutcome(Set<BayesianEvent> outcome) {
		this.outcome = outcome;
	}
}
