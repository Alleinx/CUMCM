
public class Controller {
	public int getSystemRunningTime() {
		return systemRunningTime;
	}	
	
	public void addSystemRunningTime(int time) {
		systemRunningTime += time;
	}
	
	/* operationTime means the minimum cost value */
	public boolean couldRun(int operationTime) {
		return (TIMELIMIT - systemRunningTime) >= operationTime;
	}
	
	public static Controller getInstance() {
		return HolderClass.instance;
	}
	
	public void start() {
		rgv.running();
	}
	
	private Controller() {
		this.rgv = RGV.getInstance();
		this.rgv.setController(this);
	}

	public static class HolderClass {
		private final static Controller instance = new Controller();
	}
	
	private RGV rgv;
	private final int TIMELIMIT = 60 * 60 * 8;
	private int systemRunningTime = 0;
}
