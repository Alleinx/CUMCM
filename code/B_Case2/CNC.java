public class CNC {

	public CNC(int CNCPosition, Controller control) {
		this.CNCPosition = CNCPosition + 1;
		this.control = control;
		this.remainTime = 0;
		this.hasMaterial = false;
	}
	
	public void getMaterial(Material material) {
		this.material = material;
		this.hasMaterial = true;
	}
	
	public void getProcessedMaterialII(Material material) {
		this.material = material;
	}

	public Material getProcessedMaterial() {
		return this.material;
	}

	public void startProcess() {
		this.material.setStartTime(control.getSystemRunningTime());	
		this.remainTime = PROCESSTIME;
	}
	
	public void startProcessII() {
		
		this.material.setStartTimeII(control.getSystemRunningTime());//modified
		this.remainTime = PROCESSTIMEII;
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


	private boolean hasMaterial;
	private int CNCPosition;
	private int remainTime;
	private Material material;
	private Controller control;
	
	private final int PROCESSTIME = 455;
	private final int PROCESSTIMEII = 182;
}
