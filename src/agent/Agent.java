package agent;

import org.encog.ml.bayesian.BayesianNetwork;

import java.util.ArrayList;

import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.bif.BIFUtil;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;

public class Agent {
	private BayesianNetwork network;
	private ArrayList<BayesianEvent> evidence = new ArrayList<BayesianEvent>();
	private ArrayList<BayesianEvent> outcome = new ArrayList<BayesianEvent>();
	private String queryType;
	
	public Agent(String file) {
		this.network =  BIFUtil.readBIF(file);
	}
	
	public void makeQuery() {
		
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public ArrayList<BayesianEvent> getEvidence() {
		return evidence;
	}

	public void setEvidence(ArrayList<BayesianEvent> evidence) {
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
