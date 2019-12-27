public class RGV {
	
	/* return a RGV object */
	public static RGV getInstance() {
		return HolderClass.instance;
	}
	
	/* set controller for RGV */
	public void setController(Controller c) {
		this.controller = c;
		for (int i = 0; i < 8; i++) {
			CNCHolder[i] = new CNC(i, controller);
		}
	}
	
	/* main function */
	public void running() {
		while (true) {
			cost();
			selectTask();
			if (!controller.couldRun(costTable[taskSelected])) {
				break;
			}
			operation();
		} 
		
		this.moveTo(0);
		System.exit(0);
	}
	
	
	/* calculate the cost for every CNC */
	private void cost() {
		
		int cost = 0;
		int waitingTime = 0;
		int distanceTime;
		for (int i = 0; i < 8; i++) {
			this.CNCRemainTime[i] = this.CNCHolder[i].getRemainTime();
		}
		
		for (int i = 0; i < 8; i++) {
			distanceTime = distance(i);
			waitingTime = CNCRemainTime[i] - distanceTime;
			
			if (waitingTime > 0) {
				cost = distanceTime + CNCloadMaterialTime[i] + waitingTime;
			} else {
				cost = distanceTime + CNCloadMaterialTime[i];
			}

			costTable[i] = cost;
		}
	}

	/* return the distance between the given CNC and the current position of RGV */
	private int distance(int CNCposition) {
		int absoluteValue = Math.abs(CNCPositionTable[RGVposition] - CNCPositionTable[CNCposition]);
		return distance[absoluteValue];
	}
	
	/* select a task with Minimum cost value */
	private void selectTask() {
		int min = costTable[0];
		int index = 0;
		
		for (int i = 1; i < 8; i++) {
			if (costTable[i] < min) {
				min = costTable[i];
				index = i;
			} /** TODO: optimize? */
		}
		
		this.taskSelected = index;
		// System.out.println("Task selected: "  + this.taskSelected);
	}
	
	private void operation() {
		if (!wash()) {
			if(!moveTo(taskSelected)) {
				loadMaterial();
			}
		}
	}
	
	private boolean wash() {
		if (hasProcessedMaterial) {
			this.store.output();
			this.updateTime(this.WASHTIME);
			this.hasProcessedMaterial = false;
			return true;
		}
		
		return false;
	}
	
	private boolean moveTo(int position) {
		int goHome = Math.abs(this.RGVposition - position);
		
		if (this.RGVposition != position) {
			this.RGVposition = position;
			this.updateTime(this.distance(goHome));
			return true;
		}
		this.updateTime(CNCRemainTime[position]);		
		return false; 
	}
	
	private void loadMaterial() {
		if (CNCHolder[taskSelected].hasMaterial()) {
			this.hasProcessedMaterial = true;
			store = CNCHolder[taskSelected].getProcessedMaterial();
		}
		materialIndex++;
		Material material = new Material(materialIndex, taskSelected);
		CNCHolder[taskSelected].getMaterial(material);	
		CNCHolder[taskSelected].startProcess();	


		CNCHolder[taskSelected].timeMinus(-CNCloadMaterialTime[taskSelected]);
		updateTime(CNCloadMaterialTime[taskSelected]);
		
	}
	
	/* get all remain time of CNC */
	private void updateTime(int operationTime) {
		for (int i = 0; i < 8; i++) {
			CNCHolder[i].timeMinus(operationTime);
		}
		
		this.controller.addSystemRunningTime(operationTime);
	}
	
	
	private RGV() {
		costTable = new int[8]; // index : cost
		
		CNCRemainTime = new int[8];
		for (int i = 0; i < 8; i++) {
			CNCRemainTime[i] = 0;
		}
		
		CNCHolder = new CNC[8];	
	}
	
	private static class HolderClass {
		private final static RGV instance = new RGV();
	}
	
	
	private boolean hasProcessedMaterial = false;
	private int materialIndex = 0;
	private int taskSelected;
	private int RGVposition = 0; //indicate the current position of RGV;
	private CNC[] CNCHolder;
	private int[] CNCPositionTable = {0, 0, 1, 1, 2, 2, 3, 3}; //store CNC position;
	private int[] CNCloadMaterialTime = {27, 32, 27, 32, 27, 32, 27, 32};
	private int[] distance = {0, 18, 32, 46};
	private int[] costTable; //store cost value of every CNC;
	private int[] CNCRemainTime; //store the remaining time for CNC to proceed a material;
	private Controller controller;
	private Material store;
	private final int WASHTIME = 25;
}
