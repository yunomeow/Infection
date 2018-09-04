package infected.android;


public class EnergyCalculator {
	private Infection infection;
	private int nowEnergy;
	public EnergyCalculator(Infection infection){
		this.infection = infection;
		nowEnergy = 0;
	}
	public int getNowEnergy(){
		return nowEnergy;
	}
	public void useEnergy(int value){
		nowEnergy -= value;
	}
	public void produceEnergy(){
		int cnt = 0;
		for(Node m:infection.getNodePool()){
			if(m == null)continue;
			if(m instanceof EnergyCollector){
				cnt++;
			}
		}
		nowEnergy += (cnt+5);
	}
	
}
