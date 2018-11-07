import java.util.Random;

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
		breakdown();
		this.remainTime = PROCESSTIME;
		this.PROCESSTIME = STOREPROCESSTIME; //???
	}
	
	public void breakdown() {
		Random rand = new Random();
		if ((int)rand.nextInt(100) == 1) {
			int startTimeLength = (int) (PROCESSTIME * rand.nextInt(PROCESSTIME) / (double)PROCESSTIME);
			errorStartTime = control.getSystemRunningTime() + startTimeLength;
			int errorLength = rand.nextInt(600)+600;
			errorEndTime = errorStartTime + errorLength;
			hasMaterial = false;
			PROCESSTIME = startTimeLength + errorLength;
			System.out.println("Error material index:" + 
								   material.getIndex() + 
								  ", error CNC index:" + 
								           CNCPosition +
								 ", error start time:" + 
								        errorStartTime + ", error end time:" + errorEndTime);
		}
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
	
	public void finish() {
		material.output();
		hasMaterial = false;
	}
	
	public Material getProcessedMaterial() {
		return this.material;
	}

	private boolean hasMaterial;
	private int errorStartTime;
	private int errorEndTime;
	private int CNCPosition;
	private int remainTime;
	private Controller control;
	private Material material;
	private int PROCESSTIME = 545;
	private final int STOREPROCESSTIME = 545;
}
