import java.util.Random;

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
	
	public void startProcess() {
		processTime = PROCESSTIME;
		breakdown();
		this.material.setStartTime(control.getSystemRunningTime());	
		this.remainTime = processTime;
	}

	public void startProcessII() {
		this.material.setStartTimeII(control.getSystemRunningTime());//modified
		processTime = PROCESSTIMEII;
		breakdown();
		this.remainTime = processTime;
	}
	
	public void breakdown() {
		Random rand = new Random();
		if (rand.nextInt(100)==1) {
			int startTimeLength = (int) (processTime * rand.nextInt(processTime)/(double)processTime);
			errorStartTime = control.getSystemRunningTime() + startTimeLength;
			int errorLength = rand.nextInt(600)+600;
			errorEndTime = errorStartTime + errorLength;
			hasMaterial = false;
			processTime = (int)(startTimeLength + errorLength);
			System.out.println("Error material index:" + material.getIndex() + ", error CNC index" + CNCPosition + ", error start time:" + errorStartTime +", error end time:"+errorEndTime);
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
	
	public Material getProcessedMaterial() {
		return this.material;
	}
	
	public void getProcessedMaterialII(Material material) {
		this.material = material;
	}

	private int errorStartTime;
	private int errorEndTime;
	private boolean hasMaterial;
	private int CNCPosition;
	private int remainTime;
	private Material material;
	private Controller control;
	private int processTime;	
	private final int PROCESSTIME = 455;
	private final int PROCESSTIMEII = 182;
}
