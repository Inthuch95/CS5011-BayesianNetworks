package agent;

import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.bif.BIFUtil;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Receive patient information from the user and make diagnostic or predictive queries
 *
 */
public class Agent {
	private BayesianNetwork network;
	private HashMap<String, Integer> evidence = new HashMap<String, Integer>();
	private HashMap<String, Double> queryResult = new HashMap<String, Double>(); 
	private Set<BayesianEvent> outcome = new HashSet<BayesianEvent>(); 
	private EnumerationQuery query;
	
	public Agent(String file) {
		this.network =  BIFUtil.readBIF(file);
	}
	
	public void makeQuery(String queryType) {
		// define outcome, then execute the query
		this.defineOutcome(queryType);
		this.executeQuery();
	}
	
	/**
	 * Prepare to make queries by defining evidences
	 */
	public void defineEvidence() {
		this.query = new EnumerationQuery(this.network);
		BayesianEvent evidenceNode;
		String key;
		int value;
		// define evidence nodes and set appropriate values
		for (Map.Entry<String, Integer> entry : this.evidence.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    evidenceNode = this.network.getEvent(key);
		    query.defineEventType(evidenceNode, EventType.Evidence);
		    query.setEventValue(evidenceNode, value);
		}
	}
	private void defineOutcome(String queryType) {
	    Set<BayesianEvent> evidenceSet = new HashSet<BayesianEvent>();
	    ArrayList<BayesianEvent> removeList = new ArrayList<BayesianEvent>();
		for (String label : this.evidence.keySet()) {
			List<BayesianEvent> causes = this.network.getEvent(label).getParents();
			List<BayesianEvent> effects = this.network.getEvent(label).getChildren();
	    	// make diagnostic or predictive query based on the user's choice
	    	if (queryType.equals("diagnostic") && !causes.isEmpty()) {
	    		this.outcome.addAll(this.network.getEvent(label).getParents());
	    	} else if (queryType.equals("predictive") && !effects.isEmpty()) {
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
		// execute query if possible 
		if (!this.outcome.isEmpty()) {
			ArrayList<BayesianEvent> outcomeList = new ArrayList<BayesianEvent>();
			outcomeList.addAll(this.outcome);
			for (int i = 0; i < outcomeList.size(); i++) {
		    	// clear previous value
				if (i > 0) {
		    		query.defineEventType(outcomeList.get(i - 1), EventType.Hidden);
		    	}
				// define new outcome
				query.defineEventType(outcomeList.get(i), EventType.Outcome);
		    	query.setEventValue(outcomeList.get(i), 0);
		    	query.execute();
		    	// maps node label to probability
				queryResult.put(outcomeList.get(i).getLabel(), query.getProbability());
				this.outcome.clear();
		    }
		} else {
			System.out.println("Unable to make requested query");
		}
	}

	/**
	 * get evidence mapping
	 * @return mapping between node and observed value 
	 */
	public HashMap<String, Integer> getEvidence() {
		return evidence;
	}

	/**
	 * Assign new evidence mapping
	 * @param evidence - mapping between node and observed value 
	 */
	public void setEvidence(HashMap<String, Integer> evidence) {
		this.evidence = evidence;
	}

	/**
	 * Get current Bayesian network
	 * @return Bayesian network
	 */
	public BayesianNetwork getNetwork() {
		return network;
	}

	/**
	 * Assign new Bayesian network
	 * @param network - Bayesian network
	 */
	public void setNetwork(BayesianNetwork network) {
		this.network = network;
	}

	/**
	 * Get list of queried variables
	 * @return Set of quroed variables
	 */
	public Set<BayesianEvent> getOutcome() {
		return outcome;
	}

	/**
	 * Assign new set of queried variables
	 * @param outcome - set of queried variables
	 */
	public void setOutcome(Set<BayesianEvent> outcome) {
		this.outcome = outcome;
	}

	/**
	 * Get mapping of queried nodes and probabilities
	 * @return Mapping of queried nodes and probabilities
	 */
	public HashMap<String, Double> getQueryResult() {
		return queryResult;
	}

	/**
	 * Assign new Mapping of queried nodes and probabilities
	 * @param queryResult - Mapping of queried nodes and probabilities
	 */
	public void setQueryResult(HashMap<String, Double> queryResult) {
		this.queryResult = queryResult;
	}
}
