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
		int initialIndex;
		if (store != null &&store.getCNCIndex() == 0)
			initialIndex = 1;
		else
			initialIndex = 0;
		
		int min = costTable[initialIndex];
		int index = initialIndex;
		
		for (int i = 1; i < 8; i++) {
			if (costTable[i] < min) {
				min = costTable[i];
				index = i;
			} /** TODO: optimize? */
			if (store != null &&store.getCNCIndex() == i)
				continue;
		}
		
		this.taskSelected = index;
	}
	
	private void operation() {
		if (!wash()) {
			if(!moveTo(taskSelected)) {
				loadMaterial();
			}
		}
	}
	
	private boolean wash() {
		if (holdMaterial) {
			if (store.getMaterialStatus() == 1) {
				this.store.output();
				this.updateTime(this.WASHTIME);
				this.holdMaterial = false;
				return true;
			} else
				return false;
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
		if (!CNCHolder[taskSelected].hasMaterial()) {
			materialIndex++;			
			Material material = new Material(materialIndex, taskSelected);
			CNCHolder[taskSelected].getMaterial(material);	
			CNCHolder[taskSelected].startProcess();	
			updateTime(CNCloadMaterialTime[taskSelected]);
		} else {
			if (!holdMaterial == true) {
				holdMaterial = true;
				materialIndex++;
				store = CNCHolder[taskSelected].getProcessedMaterial();	
				if(store.getMaterialStatus()==0)
					store.setEndTime(controller.getSystemRunningTime());
				updateTime(CNCloadMaterialTime[taskSelected]);			
				Material material = new Material(materialIndex, taskSelected);
				CNCHolder[taskSelected].getMaterial(material);	
				CNCHolder[taskSelected].startProcess();	
			} else {
				Material temp = CNCHolder[taskSelected].getProcessedMaterial();
				CNCHolder[taskSelected].getProcessedMaterialII(store);
				store.setCNCIndexII(taskSelected);
				store.changeStatus();
				store.setEndTime(controller.getSystemRunningTime());//modified
				store = temp;
				updateTime(CNCloadMaterialTime[taskSelected]);	
				CNCHolder[taskSelected].startProcessII();
			}
		}
		
	}
	
	/* get all remain time of CNC */
	private void updateTime(int operationTime) {
		for (int i = 0; i < 8; i++) {
			CNCHolder[i].timeMinus(operationTime);
		} //update all CNC processing time.
		
		this.controller.addSystemRunningTime(operationTime); //update the system time;
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
	
	
	//private boolean hasProcessedMaterial = false;
	private boolean holdMaterial = false;
	private int RGVposition = 0; //indicate the current position of RGV;
	private int materialIndex = 0;
	private int taskSelected;
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
