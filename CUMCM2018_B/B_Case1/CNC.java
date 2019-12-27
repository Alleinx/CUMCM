public class CNC {
	public CNC(int CNCPosition, Controller control) {
		this.CNCPosition = CNCPosition + 1;
		this.control = control;
		this.remainTime = 0;
	}
	
	public void getMaterial(Material material) {
		this.material = material;
		this.hasMaterial = true;
	}
	
	public void startProcess() {
		this.material.setStartTime(control.getSystemRunningTime());	
		this.remainTime = PROCESSTIME;
	}
	
	public int getRemainTime() {
		return remainTime;
	}
	
	public void timeMinus(int operationTime) {
		if (remainTime - operationTime < 0) {
			remainTime = 0;
		} else {
			remainTime -= operationTime;
		}
	}
	
	public boolean hasMaterial() {
		return hasMaterial;
	}
	
	public Material getProcessedMaterial() {
		return this.material;
	}
	
	private boolean hasMaterial;
	private int CNCPosition;
	private Material material;
	private final int PROCESSTIME = 545;
	private Controller control;
	private int remainTime;
}
