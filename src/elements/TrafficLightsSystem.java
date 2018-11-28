package elements;

import java.util.ArrayList;

public class TrafficLightsSystem {
	private ArrayList<Phase> allPhases = new ArrayList<Phase>();
	public boolean interPhase = false;
	private int t=0;
	public int interPhaseDuration = 10;
	private int currentPhaseIndex=0;
	
	public void incrementIndex() {
		currentPhaseIndex = (currentPhaseIndex+1)%(allPhases.size());
	}
	
	
	public void tReset() {
		t = 0;
	}
	
	public void tIncrementation() {
		t = t+1;
	}
	
	public void setTrafficLightsRed() {
		for (Phase p: this.allPhases) {
			p.setAllConcernedTrafficLightsRed(true);
		}
	}
	
	public void initializePhases() {
		setTrafficLightsRed();
		this.interPhase = true;
		
	}
	
	public void nextStep() {
		if (interPhase) {
			if (t < interPhaseDuration) {
				this.tIncrementation();
			} else {
				//this.tReset();
				this.tIncrementation();
				this.interPhase = false;
			}
		} else {
			if (t >= this.interPhaseDuration) {
				this.incrementIndex();
				for (int i=0; i<this.allPhases.size()-1; ++i) {
					this.allPhases.get(currentPhaseIndex).tReset();
					if (this.allPhases.get(currentPhaseIndex).gettMin()==0 && this.allPhases.get(currentPhaseIndex).phaseShouldStop()) {
						this.allPhases.get(currentPhaseIndex).endPhase();
						this.incrementIndex();
					} else /*if (!this.allPhases.get(currentPhaseIndex).checkRoadsToCheck())*/ {
						break;
					}
				}
				this.allPhases.get(currentPhaseIndex).startPhase();
				this.tReset();
				return;
			}
			if (this.allPhases.get(currentPhaseIndex).phaseShouldStop()) {
				this.allPhases.get(currentPhaseIndex).endPhase();
				this.interPhase = true;
				return;
			}
			this.allPhases.get(currentPhaseIndex).increment();
		}
	}
	
	public void addPhase(Phase p) {
		this.allPhases.add(p);
	}
	
	public void clearPhases() {
		this.allPhases.clear();
	}
	// Getters & setters ------------------------------------------------------------------------------------
	public Phase getCurrentPhase() {
		return this.allPhases.get(currentPhaseIndex);
	}
	public ArrayList<Phase> getPhases() {
		return this.allPhases;
	}
}
