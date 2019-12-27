public class Material {
	
	public Material(int index, int CNCindex) {		
		materialIndex = index;
		this.CNCindex = CNCindex;
		this.materialStatus = 0;
	}
	
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	public void setStartTimeII(int startTime) {
		this.startTimeII = startTime;
	}
	
	public void setEndTimeII(int endTime) {
		this.endTimeII = endTime;
	}
	
	public void setCNCIndexII(int CNCIndex) {
		this.CNCindexII = CNCIndex;
	}

	public int getCNCIndex() {
		return this.CNCindex;
	}

	public int getMaterialStatus() {
		return this.materialStatus;
	}

	public void changeStatus() {
		this.materialStatus = 1;
	}

	public void output() {
		this.setEndTimeII(Controller.getInstance().getSystemRunningTime());
		System.out.printf("%-4d%-3d%-6d%-6d%-7d%-7d%-7d\n", materialIndex, (CNCindex + 1) ,startTime, endTime, (CNCindexII + 1),startTimeII, endTimeII);
	}
	
	private int CNCindex;
	private int CNCindexII;
	private int materialIndex; //
	private int materialStatus;
	private int startTime; //
	private int endTime; //
	private int startTimeII;
	private int endTimeII;
	
}
