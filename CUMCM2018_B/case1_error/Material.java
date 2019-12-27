public class Material {
	
	public Material(int index, int CNCindex) {
		
		materialIndex = index;
		this.CNCindex = CNCindex;
	}
	
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	private void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	public void output() {
		this.setEndTime(Controller.getInstance().getSystemRunningTime());
		System.out.printf("%-4d%-3d%-6d%-7d\n", materialIndex, (CNCindex + 1), startTime, endTime);
	}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getIndex() {
		return materialIndex;
	}
	
	private int CNCindex;
	private int materialIndex; //
	private int startTime; //
	private int endTime; //
}
