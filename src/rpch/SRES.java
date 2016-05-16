import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class SRES {
	public int lambda; // the number of the population
	public int mu; // the number to be selected as parent
	public double varphi; // the convergence rate, usually is set to one
	public double chi;
	public double gamma;
	public double alpha;
	public int trytimes; // when mutation result beyond the upper or the
	// lower
	// bound, the retried times
	public double pf;
	public int runtimes; // the times of es algorithm will be executed
	public int MaxGen; // maximun of generation
	public double x[][]; // individuals and at last will be solutions
	public int variables; // number of xi
	public int constrained; // the number of constrained functions
	public double lu[][]; // low bound and up bound,the first row is low bound
	// and second row is up bound
	public double eta[][]; // parameter
	public double oldEta[][];
	public double eta_u[]; // the upper bound of eta
	public double tau;
	public double tau_;
	public double f[]; // the objective values of the lambda individuals
	public double phi[][]; // the nine constrained values for all the lambda
	// individuals
	public double quadratic_loss[];
	public ArrayList<Integer> feasible; // all the feasible individuals at a
	// certain generation
	public int ranked[];
	public double accurate;
	public Random rand;
	public int feasibleRun;
//	public double weights[];
//	public int numberOfConstrained[];
	
	public double currentMin;
	public int minIndex;
	public double bestMin;
	public double bestIndividual[];
	public double bestEta[];
	public boolean bestflag; // to indicate whether find a best individual
	public int Gm;// the generation in which bestMin found
	public int SuccessGm;
	public double optimum;
	public double oldOptimum;
	public boolean optimaFlag;
	public double power3[];
	public double power4[];
	public double power5[];
	public double power3Individual[][];
	public double power4Individual[][];
	public double power5Individual[][];
	public int numberOfViolation3[];
	public int numberOfViolation4[];
	public int numberOfViolation5[];
	public double x_[][];
	public double probability[];
	public double common;
	//public int equalIndex[] = {3,5,11,12,13,14,15,17,21,22,23};
	public int equalIndex[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,23,24,26,27,28,30};
	
//	public double newX[][];
//	public double newF[];
//	public double newPhi[][];
//	public double newPhiAll[];
//	public double newEta[][];
	
	public double a20[] = { 0.0693, 0.0577, 0.05, 0.2, 0.26, 0.55, 0.06, 0.1,
			0.12, 0.18, 0.1, 0.09, 0.0693, 0.0577, 0.05, 0.2, 0.26, 0.55, 0.06,
			0.1, 0.12, 0.18, 0.1, 0.09 };
	public double b20[] = { 44.094, 58.12, 58.12, 137.4, 120.9, 170.9, 62.501,
			84.94, 133.425, 82.507, 46.07, 60.097, 44.094, 58.12, 58.12, 137.4,
			120.9, 170.9, 62.501, 84.94, 133.425, 82.507, 46.07, 60.097 };
	public double c20[] = { 123.7, 31.7, 45.7, 14.7, 84.7, 27.7, 49.7, 7.1,
			2.1, 17.7, 0.85, 0.64 };
	public double d20[] = { 31.244, 36.12, 34.784, 92.7, 82.7, 91.6, 56.708,
			82.7, 80.8, 64.517, 49.4, 49.1 };
	public double e20[] = { 0.1, 0.3, 0.4, 0.3, 0.6, 0.3 };
	public double e19[] = { -15, -27, -36, -18, -12 };
	public double d19[] = { 4, 8, 10, 6, 2 };
	public double c19[][] = { { 30, -20, -10, 32, -10 },
			{ -20, 39, -6, -31, 32 }, { -10, -6, 10, -6, -10 },
			{ 32, -31, -6, 39, -20 }, { -10, 32, -10, -20, 30 } };
	public double a19[][] = { { -16, 2, 0, 1, 0 }, { 0, -2, 0, 0.4, 2 },
			{ -3.5, 0, 2, 0, 0 }, { 0, -2, 0, -4, -1 }, { 0, -9, -2, 1, -2.8 },
			{ 2, 0, -4, 0, 0 }, { -1, -1, -1, -1, -1 }, { -1, -2, -3, -2, -1 },
			{ 1, 2, 3, 4, 5 }, { 1, 1, 1, 1, 1 } };
	public double b19[] = { -40, -2, -0.25, -4, -4, -1, -40, -60, 5, 1 };

	
	public double o25[]={0.030858718087483,	-0.078632292353156,	0.048651146638038,-0.069089831066354,-0.087918542941928,0.088982639811141,0.074143235639847,-0.086527593580149,-0.020616531903907,0.055586106499231,0.059285954883598,	-0.040671485554685,-0.087399911887693,-0.01842585125741,-0.005184912793062,-0.039892037937026,0.036509229387458,0.026046414854433,-0.067133862936029,0.082780189144943,-0.049336722577062,0.018503188080959,0.051610619131255,0.018613117768432,0.093448598181657,-0.071208840780873,-0.036535677894572,-0.03126128526933,0.099243805247963,0.053872445945574};
	public double o26[]={-0.066939099286697,0.470966419894494,	-0.490528349401176,-0.312203454689423,-0.124759576300523,-0.247823908806285,-0.448077079941866,0.326494954650117,0.493435908752668,0.061699778818925,-0.30251101183711,	-0.274045146932175,-0.432969960330318,0.062239193145781,-0.188163731545079,-0.100709842052095,-0.333528971180922,-0.496627672944882,-0.288650116941944,0.435648113198148,-0.348261107144255,0.456550427329479,-0.286843419772511,0.145639015401174,-0.038656025783381,0.333291935226012,-0.293687524888766,-0.347859473554797,-0.089300971656411,0.142027393193559};
	public double o27[]={111.17633500088529,92.07880492633424,	417.9818592609036,253.16188128024302,363.5279986597767,314.334093889305,187.32739056163342,240.4363027535162,422.60090880560665,327.63042902581515,62.04762897064405,	25.435663968682125,360.56773191905114,154.9226721156832,33.161292034425806,177.8091733067186,262.58198940407755,436.9800562237075,476.6400624069227,331.2167787340325,75.205948242522,484.33624811710115,258.4696246506982,419.8919566566751,357.51468895930395,166.3771729386268,47.59455935830133,188.20606700809785,184.7964918401363,267.9201349178807};
	//public double o25[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	/**
	 * the values in the constructive function is same for all the benchmark
	 * instances
	 */
	public SRES() {
		
		//gamma = 0.83 alpha = 0.4 lambda = 280 mu = 40 varphi = 2 
		lambda = 280;//280;
		mu = 40;//40;
		gamma = 0.88;
		alpha = 0.2;
		trytimes = 10;
		runtimes = 25;
		pf = 0.45;
		MaxGen = 500000/lambda+1;
		//MaxGen = 3000;
		f = new double[lambda];
		quadratic_loss = new double[lambda];
		feasible = new ArrayList<Integer>();
 
		currentMin = Double.MAX_VALUE;
		ranked = new int[lambda];

		power3 = new double[runtimes];
		power4 = new double[runtimes];
		power5 = new double[runtimes];
		numberOfViolation3 = new int[6];
		numberOfViolation4 = new int[6];
		numberOfViolation5 = new int[6];
		probability = new double[40];
		common = 1;
		for (int i = 0; i < 40; i++) {
			probability[i] = common;
		}
		accurate = 0.0001;
		oldOptimum = Double.MAX_VALUE; 
	}
	/**
	 * Title: setParameters
	 * Description: to make our program flexible with different benchmarks, we
	 * need to set paratmeters mannually
	 * @param var
	 *            the number of variables
	 * @param cons
	 *            the number of constrained functions
	 */
	public void setParameters(int var, int cons,double vars) {
		varphi = 1;
		variables = var;
		constrained = cons;
		chi = 1 / (2 * variables) + 1 / (2 * Math.sqrt(variables));
		varphi = Math.sqrt(2
				/ chi
				* (Math.log(1 / alpha * (Math.exp(varphi * varphi * chi / 2))
						- (1 - alpha))));
	//	System.out.println(varphi);
    	//varphi = 2; 
		varphi = 1.8;

		tau = varphi / Math.sqrt(2 * Math.sqrt(variables));
		tau_ = varphi / Math.sqrt(2 * variables);

		x = new double[lambda][variables];
		lu = new double[2][variables];
		eta = new double[lambda][variables];
		oldEta = new double[lambda][variables];
		eta_u = new double[variables];
		bestIndividual = new double[variables];
		bestEta = new double[variables];
//		weights = new double[constrained];
//		for(int i = 0; i < constrained; i++){
//			weights[i] = 1;
//		}
//		numberOfConstrained = new int[constrained];
		phi = new double[lambda][constrained];
		power3Individual = new double[runtimes][this.variables];
		power4Individual = new double[runtimes][this.variables];
		power5Individual = new double[runtimes][this.variables];
		x_ = new double[lambda][variables];
		feasibleRun = runtimes;
		optimaFlag = false;
		
//		newX = new double[lambda][variables];
//		newF = new double[lambda];
//		newPhi = new double[lambda][constrained];
//		newPhiAll = new double[lambda];
//		newEta = new double[lambda][variables];
	}
	// the array contains lower bound values and upper bound values
	public void setlu(double array[][]) {

		lu = new double[array.length][array[0].length];
		for (int i = 0; i < lu.length; i++) {
			for (int j = 0; j < lu[i].length; j++) {
				lu[i][j] = array[i][j];
			}
		}
	}
	
	
	public void init_x() {
		double tem = 0;
		for (int i = 0; i < lambda; i++) {
			for (int j = 0; j < variables; j++) {
				tem = rand.nextDouble();
				x[i][j] = lu[0][j] + tem* (lu[1][j] - lu[0][j]);
				x_[i][j] = 0;
			}
		}
	}

	// to compute the objective value that is the return value
	public double objective1(double array[]) {
		double sum;
		double sum1; // to store the first sum
		double sum2; // to store the second sum
		double sum3; // to store the third sum
		sum1 = sum2 = sum3 = 0;
		for(int i = 0; i < 4; i++){
			sum1+=array[i];
		}
		sum1*=5;
		
		for(int i = 0; i < 4; i++){
			sum2+=array[i]*array[i];
		}
		sum2*=-5;
		for(int i = 4; i < 13; i++){
			sum3+=array[i];
		}
		sum3*=-1;
		sum = sum1+sum2+sum3;
		return sum;
	}
	
	// compute the constraints of all lambda points for problem1
	public double[] computeConstrain1(double array[]) {
		double values[] = new double[constrained];
		values[0] = 2*array[0]+2*array[1]+array[10]
		             +array[10]-10;
		values[1] = 2 * array[0] + 2 * array[2] + array[9]
		           	+ array[11] - 10;
		values[2] = 2 * array[1] + 2 * array[2]
		            + array[10] + array[11] - 10;
		values[3] = array[0] * (-8) + array[9];
		values[4] = array[1] * (-8) + array[10]; 
		values[5] = array[2] * (-8) + array[11];
		values[6] = (-2) * array[3] - array[4] + array[9];
		values[7] = (-2) * array[5] - array[6] + array[10];
		values[8] = (-2) * array[7] - array[8] + array[11];
		return values;
	}
	
	public double objective2(double array[]) {
		double sum1 = 0;
		double sum2 = 1;
		double sum3 = 0;
		for (int j = 0; j < variables; j++) {
			sum1 += Math.pow(Math.cos(array[j]), 4);
		}
		for (int j = 0; j < variables; j++) {
			sum2 *= Math.pow(Math.cos(array[j]), 2);
		}
		sum2 *= -2;
		for (int j = 0; j < variables; j++) {
			sum3 += ((j + 1) * array[j] * array[j]);
		}
		sum3 = Math.sqrt(sum3);
		return -Math.abs(sum1 + sum2) / sum3;
	}

	public double [] computeConstrain2(double array[]){
		double values[] = new double[constrained];
		double sum1 = 1;
		double sum2 = 0;
		for (int j = 0; j < variables; j++) {
			sum1 *= array[j];
		}
		values[0] = 0.75 - sum1;
		for (int j = 0; j < variables; j++) {
			sum2 += array[j];
		}
		values[1] = sum2 - 7.5 * variables;
		return values;
	}
	
	public double objective3(double array[]) {
		double sum1 = 1;
		double sum2 = 0;
		for (int j = 0; j < variables; j++) {
			sum1 *= array[j];
		}
		sum2 = Math.pow(Math.sqrt(variables), variables);
		return  -sum2 * sum1;
	}
	public double[] computeConstrain3(double array[]) {
		double values[] = new double[constrained];
		double sum = 0;
		for (int j = 0; j < variables; j++) {
			sum += array[j] * array[j];
		}
		values[0] = Math.abs(sum - 1) - accurate;
		return values;
	}
	
	public double objective4(double array[]) {
		double sum1 = 1;
		double sum2 = 1;
		double sum3 = 1;
		sum1 *= (5.3578547 * array[2] * array[2]);
		sum2 *= (0.8356891 * array[0] * array[4]);
		sum3 *= (37.293239 * array[0]);
		return sum1 + sum2 + sum3 - 40792.141;
	}
	
	public double[] computeConstrain4(double array[]) {
		double values[] = new double[constrained];
		values[0] = 85.334407 + 0.0056858 * array[1] * array[4] + 0.0006262
				* array[0] * array[3] - 0.0022053 * array[2] * array[4] - 92;
		values[1] = -85.334407 - 0.0056858 * array[1] * array[4] - 0.0006262
				* array[0] * array[3] + 0.0022053 * array[2] * array[4];
		values[2] = 80.51249 + 0.0071317 * array[1] * array[4] + 0.0029955
				* array[0] * array[1] + 0.0021813 * array[2] * array[2] - 110;
		values[3] = -80.51249 - 0.0071317 * array[1] * array[4] - 0.0029955
				* array[0] * array[1] - 0.0021813 * array[2] * array[2] + 90;
		values[4] = 9.300961 + 0.0047026 * array[2] * array[4] + 0.0012547
				* array[0] * array[2] + 0.0019085 * array[2] * array[3] - 25;
		values[5] = -9.300961 - 0.0047026 * array[2] * array[4] - 0.0012547
				* array[0] * array[2] - 0.0019085 * array[2] * array[3] + 20;
		return values;
	}
	
	public double objective5(double array[]) {
		double sum = 0;
		sum += 3 * array[0] + 0.000001 * Math.pow(array[0], 3);
		sum += 2 * array[1] + (0.000002 / 3) * Math.pow(array[1], 3);
		return sum;
	}
	
	public double[] computeConstrain5(double array[]) {
    	double values[] = new double[constrained];
		values[0] = -array[3] + array[2] - 0.55;
		values[1] = -array[2] + array[3] - 0.55;
		values[2] = Math.abs(1000 * Math.sin(-array[2] - 0.25)
				+ 1000 * Math.sin(-array[3] - 0.25) + 894.8
				- array[0])
				- accurate;
		values[3] = Math.abs(1000 * Math.sin(array[2] - 0.25)
				+ 1000 * Math.sin(array[2] - array[3] - 0.25)
				+ 894.8 - array[1])
				- accurate;
		values[4] = Math.abs(1000 * Math.sin(array[3] - 0.25)
				+ 1000 * Math.sin(array[3] - array[2] - 0.25)
				+ 1294.8)
				- accurate;
		return values;
}
	
	
	public double objective6(double array[]) {
		double sum1 = 0;
		double sum2 = 0;
		sum1 += Math.pow(array[0] - 10, 3);
		sum2 += Math.pow(array[1] - 20, 3);
		return sum1 + sum2;
	}
	
	public double[] computeConstrain6(double array[]) {
		double values[] = new double[constrained];
		values[0] = -Math.pow(array[0] - 5, 2) - Math.pow(array[1] - 5, 2) + 100;
		values[1] = Math.pow(array[0] - 6, 2) + Math.pow(array[1] - 5, 2) - 82.81;
		return values;
	}
	
	public double objective7(double array[]) {
		double sum1 = 0;
		double sum2 = 0;
		sum1 += array[0] * array[0] + array[1] * array[1] + array[0] * array[1]
				- 14 * array[0] - 16 * array[1];
		sum2 += Math.pow(array[2] - 10, 2) + 4 * Math.pow(array[3] - 5, 2)
				+ Math.pow(array[4] - 3, 2);
		sum2 += 2 * Math.pow(array[5] - 1, 2) + 5 * Math.pow(array[6], 2) + 7
				* Math.pow(array[7] - 11, 2);
		sum2 += 2 * Math.pow(array[8] - 10, 2) + Math.pow(array[9] - 7, 2) + 45;
		return sum1 + sum2;
	}
	
	public double[] computeConstrain7(double array[]) {
		double values[] = new double[constrained];
		values[0] = -105 + 4 * array[0] + 5 * array[1] - 3 * array[6] + 9
				* array[7];
		values[1] = 10 * array[0] - 8 * array[1] - 17 * array[6] + 2 * array[7];
		values[2] = -8 * array[0] + 2 * array[1] + 5 * array[8] - 2 * array[9]
				- 12;
		values[3] = 3 * Math.pow(array[0] - 2, 2) + 4
				* Math.pow(array[1] - 3, 2) + 2 * Math.pow(array[2], 2) - 7
				* array[3] - 120;
		values[4] = 5 * Math.pow(array[0], 2) + 8 * array[1]
				+ Math.pow(array[2] - 6, 2) - 2 * array[3] - 40;
		values[5] = Math.pow(array[0], 2) + 2 * Math.pow(array[1] - 2, 2) - 2
				* array[0] * array[1] + 14 * array[4] - 6 * array[5];
		values[6] = 0.5 * Math.pow(array[0] - 8, 2) + 2
				* Math.pow(array[1] - 4, 2) + 3 * Math.pow(array[4], 2)
				- array[5] - 30;
		values[7] = -3 * array[0] + 6 * array[1] + 12
				* Math.pow(array[8] - 8, 2) - 7 * array[9];
		return values;
	}
	
	public double objective8(double array[]) {
		double sum1 = 1;
		double sum2 = 1;
		sum1 *= Math.pow(Math.sin(2 * Math.PI * array[0]), 3);
		sum1 *= Math.sin(2 * Math.PI * array[1]);
		sum2 *= Math.pow(array[0], 3);
		sum2 *= (array[0] + array[1]);
		return -sum1 / sum2;
	}
	
	public double[] computeConstrain8(double array[]) {
		double values[] = new double[constrained];
		values[0] = Math.pow(array[0], 2) - array[1] + 1;
		values[1] = 1 - array[0] + Math.pow(array[1] - 4, 2);
		return values;
	}
	
	public double objective9(double array[]) {
		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		sum1 = Math.pow(array[0] - 10, 2) + 5 * Math.pow(array[1] - 12, 2)
				+ Math.pow(array[2], 4);
		sum2 = 3 * Math.pow(array[3] - 11, 2) + 10 * Math.pow(array[4], 6) + 7
				* Math.pow(array[5], 2);
		sum3 = Math.pow(array[6], 4) - 4 * array[5] * array[6] - 10 * array[5]
				- 8 * array[6];
		return sum1 + sum2 + sum3;
	}
	
	public double[] computeConstrain9(double array[]) {
		double values[] = new double[constrained];
		values[0] = -127 + 2 * Math.pow(array[0], 2) + 3
				* Math.pow(array[1], 4) + array[2] + 4 * Math.pow(array[3], 2)
				+ 5 * array[4];
		values[1] = -282 + 7 * array[0] + 3 * array[1] + 10
				* Math.pow(array[2], 2) + array[3] - array[4];
		values[2] = -196 + 23 * array[0] + Math.pow(array[1], 2) + 6
				* Math.pow(array[5], 2) - 8 * array[6];
		values[3] = 4 * Math.pow(array[0], 2) + Math.pow(array[1], 2) - 3
				* array[0] * array[1] + 2 * Math.pow(array[2], 2) + 5
				* array[5] - 11 * array[6];
		return values;
	}
	
	public double objective10(double array[]) {
		return array[0] + array[1] + array[2];
	}
	
	public double[] computeConstrain10(double array[]) {
		double values[] = new double[constrained];
		values[0] = -1 + 0.0025 * (array[3] + array[5]);
		values[1] = -1 + 0.0025 * (array[4] + array[6] - array[3]);
		values[2] = -1 + 0.01 * (array[7] - array[4]);
		values[3] = -array[0] * array[5] + 833.33252 * array[3] + 100
				* array[0] - 83333.333;
		values[4] = -array[1] * array[6] + 1250 * array[4] + array[1]
				* array[3] - 1250 * array[3];
		values[5] = -array[2] * array[7] + 1250000 + array[2] * array[4] - 2500
				* array[4];
		return values;
	}
	
	public double objective11(double array[]) {
			return Math.pow(array[0], 2) + Math.pow(array[1] - 1, 2);
	}
	
	public double[] computeConstrain11(double array[]) {
		double values[] = new double[constrained];
		values[0] = Math.abs(array[1] - Math.pow(array[0], 2))
				- accurate;
		return values;
	}
	
	public double objective12(double array[]) {
		double sum = 0;
		sum = 100 - Math.pow(array[0] - 5, 2) - Math.pow(array[1] - 5, 2)
				- Math.pow(array[2] - 5, 2);
		sum /= 100;
		return -sum;
	}
	
	public double[] computeConstrain12(double array[]) {
		double values[] = new double[constrained];
		double sum = 0;
		double sum1 = 0;
		sum1 = Math.pow(array[0] - 1, 2) + Math.pow(array[1] - 1, 2)
				+ Math.pow(array[2] - 1, 2) - 0.0625;
		for (int j = 1; j < 10; j++) {
			for (int k = 1; k < 10; k++) {
				for (int l = 1; l < 10; l++) {
					sum = Math.pow(array[0] - j, 2) + Math.pow(array[1] - k, 2)
							+ Math.pow(array[2] - l, 2)-0.0625;
					if (sum < sum1) {
						sum1 = sum;
					}
				}
			}
		}
		values[0] = sum1;
		return values;
	}
	
	public double objective13(double array[]) {
			 return Math.exp(array[0] * array[1] * array[2]
					* array[3] * array[4]);
	}
	
	public double[] computeConstrain13(double array[]) {
		double values[] = new double[constrained];
		values[0] = Math.abs(Math.pow(array[0], 2) + Math.pow(array[1], 2)
				+ Math.pow(array[2], 2) + Math.pow(array[3], 2)
				+ Math.pow(array[4], 2) - 10)
				- accurate;
		values[1] = Math.abs(array[1] * array[2] - 5 * array[3] * array[4])
				- accurate;
		values[2] = Math.abs(Math.pow(array[0], 3) + Math.pow(array[1], 3) + 1)
				- accurate;
		return values;
	}
	
	public double objective14(double array[]) {
		double sum = 0;
		double localSum = 0;
		double c[] = { -6.089, -17.164, -34.054, -5.914, -24.721, -14.986,
				-24.1, -10.708, -26.662, -22.179 };
		for (int j = 0; j < this.variables; j++) {
			localSum += array[j];
		}
		for (int j = 0; j < this.variables; j++) {
			sum += array[j] * (c[j] + Math.log(array[j] / localSum));
		}
		return sum;
	}
	
	public double[] computeConstrain14(double array[]) {
		double values[] = new double[constrained];
		values[0] = Math.abs(array[0] + 2 * array[1] + 2 * array[2] + array[5]
				+ array[9] - 2)
				- accurate;
		values[1] = Math.abs(array[3] + 2 * array[4] + array[5] + array[6] - 1)
				- accurate;
		values[2] = Math.abs(array[2] + array[6] + array[7] + 2 * array[8]
				+ array[9] - 1)
				- accurate;
		return values;
	}
	
	public double objective15(double array[]) {
		return 1000 - array[0] * array[0] - 2 * array[1] * array[1] - array[2]
				* array[2] - array[0] * array[1] - array[0] * array[2];
	}
	

	public double[] computeConstrain15(double array[]) {
		double values[] = new double[constrained];
		values[0] = Math.abs(array[0] * array[0] + array[1] * array[1]
				+ array[2] * array[2] - 25)
				- accurate;
		values[1] = Math.abs(8 * array[0] + 14 * array[1] + 7 * array[2] - 56)
				- accurate;
		return values;
	}
	
	public double[][] computeItem(double array[]) {
		double values[][] = new double[2][17] ;
			values[0][0] = array[1] + array[2] + 41.6;
			values[1][0] = 0.024 * array[3] - 4.62;
			values[0][1] = 12.5 / values[1][0] + 12;
			values[1][1] = 0.0003535 * array[0] * array[0] + 0.5311 * array[0]
					+ 0.08705 * values[0][1] * array[0];
			values[1][2] = 0.052 * array[0] + 78 + 0.002377 * values[0][1]
					* array[0];
			values[0][2] = values[1][1] / values[1][2];
			values[0][3] = 19 * values[0][2];
			values[1][3] = 0.04782 * (array[0] - values[0][2]) + 0.1956
					* (array[0] - values[0][2]) * (array[0] - values[0][2])
					/ array[1] + 0.6376 * values[0][3] + 1.594 * values[0][2];
			values[1][4] = 100 * array[1];
			values[1][5] = array[0] - values[0][2] - values[0][3];
			values[1][6] = 0.950 - values[1][3] / values[1][4];
			values[0][4] = values[1][5] * values[1][6];
			values[0][5] = array[0] - values[0][4] - values[0][3] - values[0][2];
			values[1][7] = 0.995 * (values[0][3] + values[0][4]);
			values[0][6] = values[1][7] / values[0][0];
			values[0][7] = values[1][7] / 3798;
			values[1][8] = values[0][6] - 0.0663 * values[0][6] / values[0][7]
					- 0.3153;
			values[0][8] = 96.82 / values[1][8] + 0.321 * values[0][0];
			values[0][9] = 1.29 * values[0][4] + 1.258 * values[0][3] + 2.29
					* values[0][2] + 1.71 * values[0][5];
			values[0][10] = 1.71 * array[0] - 0.452 * values[0][3] + 0.580
					* values[0][2];
			values[1][9] = 12.3 / 752.3;
			values[1][10] = 1.75 * values[0][1] * 0.995 * array[0];
			values[1][11] = 0.995 * values[0][9] + 1998;
			values[0][11] = values[1][9] * array[0] + values[1][10] / values[1][11];
			values[0][12] = values[1][11] - 1.75 * values[0][1];
			values[0][13] = 3623 + 64.4 * array[1] + 58.4 * array[2] + 146312
					/ (values[0][8] + array[4]);
			values[1][12] = 0.995 * values[0][9] + 60.8 * array[1] + 48 * array[3]
					- 0.1121 * values[0][13] - 5095;
			values[0][14] = values[0][12] / values[1][12];
			values[0][15] = 148000 - 331000 * values[0][14] + 40 * values[0][12]
					- 61 * values[0][14] * values[0][12];
			values[1][13] = 2324 * values[0][9] - 28740000 * values[0][1];
			values[0][16] = 14130000 - 1328 * values[0][9] - 531 * values[0][10]
					+ values[1][13] / values[1][11];
			values[1][14] = values[0][12] / values[0][14] - values[0][12] / 0.52;
			values[1][15] = 1.104 - 0.72 * values[0][14];
			values[1][16] = values[0][8] + array[4];
			return values;
	}
	
	public double objective16(double array[],double yArray[],double cArray[]) {
		return 0.000117 * yArray[13] + 0.1365 + 0.00002358 * yArray[12]
				+ 0.000001502 * yArray[15] + 0.0321 * yArray[11] + 0.004324
				* yArray[4] + 0.0001 * cArray[14] / cArray[15] + 37.48
				* yArray[1] / cArray[11] - 0.0000005843 * yArray[16];
	}
	
	public double[] computeConstrain16(double array[],double yArray[],double cArray[]) {
		double values[] = new double[constrained];
			values[0] = 0.28 / 0.72 * yArray[4] - yArray[3];
			values[1] = array[2] - 1.5 * array[1];
			values[2] = 3496 * yArray[1] / cArray[11] - 21;
			values[3] = 110.6 + yArray[0] - 62212 / cArray[16];
			values[4] = 213.1 - yArray[0];
			values[5] = yArray[0] - 405.23;
			values[6] = 17.505 - yArray[1];
			values[7] = yArray[1] - 1053.6667;
			values[8] = 11.275 - yArray[2];
			values[9] = yArray[2] - 35.03;
			values[10] = 214.228 - yArray[3];
			values[11] = yArray[3] - 665.585;
			values[12] = 7.458 - yArray[4];
			values[13] = yArray[4] - 584.463;
			values[14] = 0.961 - yArray[5];
			values[15] = yArray[5] - 265.916;
			values[16] = 1.612 - yArray[6];
			values[17] = yArray[6] - 7.046;
			values[18] = 0.146 - yArray[7];
			values[19] = yArray[7] - 0.222;
			values[20] = 107.999 - yArray[8];
			values[21] = yArray[8] - 273.366;
			values[22] = 922.693 - yArray[9];
			values[23] = yArray[9] - 1286.105;
			values[24] = 926.832 - yArray[10];
			values[25] = yArray[10] - 1444.046;
			values[26] = 18.766 - yArray[11];
			values[27] = yArray[11] - 537.141;
			values[28] = 1072.163 - yArray[12];
			values[29] = yArray[12] - 3247.039;
			values[30] = 8961.448 - yArray[13];
			values[31] = yArray[13] - 26844.086;
			values[32] = 0.063 - yArray[14];
			values[33] = yArray[14] - 0.386;
			values[34] = 71084.33 - yArray[15];
			values[35] = -140000 + yArray[15];
			values[36] = 2802713 - yArray[16];
			values[37] = yArray[16] - 12146108;
			return values;
	}
	
	public double objective17(double array[]) {
		double sum = 0;
		if (0 <= array[0] && array[0] < 300) {
			sum += 30 * array[0];
		} else {
			sum += 31 * array[0];
		}
		if (0 <= array[1] && array[1] < 100) {
			sum += 28 * array[1];
		} else if (100 <= array[1] && array[1] < 200) {
			sum += 29 * array[1];
		} else {
			sum += 30 * array[1];
		}
		return sum;
	}
	
	public double[] computeConstrain17(double array[]) {
		double values[] = new double[constrained];
		values[0] = Math.abs(-array[0] + 300 - array[2] * array[3] / 131.078
				* Math.cos(1.48477 - array[5]) + 0.90798 * array[2] * array[2]
				/ 131.078 * Math.cos(1.47588))
				- accurate;
		values[1] = Math.abs(-array[1] - array[2] * array[3] / 131.078
				* Math.cos(1.48477 + array[5]) + 0.90798 * array[3] * array[3]
				/ 131.078 * Math.cos(1.47588))
				- accurate;
		values[2] = Math.abs(-array[4] - array[2] * array[3] / 131.078
				* Math.sin(1.48477 + array[5]) + +0.90798 * array[3] * array[3]
				/ 131.078 * Math.sin(1.47588))
				- accurate;
		values[3] = Math.abs(200 - array[2] * array[3] / 131.078
				* Math.sin(1.48477 - array[5]) + 0.90798 * array[2] * array[2]
				/ 131.078 * Math.sin(1.47588));
		return values;
	}
	
	public double objective18(double array[]) {
			return -0.5
					* (array[0] * array[3] - array[1] * array[2] + array[2]
							* array[8] - array[4] * array[8] + array[4] * array[7] - array[5]
							* array[6]);
	}
	
	public double[] computeConstrain18(double array[]) {
		double values[] = new double[constrained];
		values[0] = array[2] * array[2] + array[3] * array[3] - 1;
		values[1] = array[8] * array[8] - 1;
		values[2] = array[4] * array[4] + array[5] * array[5] - 1;
		values[3] = array[0] * array[0] + (array[1] - array[8])
				* (array[1] - array[8]) - 1;
		values[4] = (array[0] - array[4]) * (array[0] - array[4])
				+ (array[1] - array[5]) * (array[1] - array[5]) - 1;
		values[5] = (array[0] - array[6]) * (array[0] - array[6])
				+ (array[1] - array[7]) * (array[1] - array[7]) - 1;
		values[6] = (array[2] - array[4]) * (array[2] - array[4])
				+ (array[3] - array[5]) * (array[3] - array[5]) - 1;
		values[7] = (array[2] - array[6]) * (array[2] - array[6])
				+ (array[3] - array[7]) * (array[3] - array[7]) - 1;
		values[8] = array[6] * array[6] + (array[7] - array[8])
				* (array[7] - array[8]) - 1;
		values[9] = array[1] * array[2] - array[0] * array[3];
		values[10] = -array[2] * array[8];
		values[11] = array[4] * array[8];
		values[12] = array[5] * array[6] - array[4] * array[7];
		return values;
	}

	public double objective19(double array[]) {
		double sum1, sum2, sum3;
		sum1 = sum2 = sum3 = 0;
		for (int m = 0; m < 5; m++) {
			for (int n = 0; n < 5; n++) {
				sum1 += c19[n][m] * array[10 + n] * array[10 + m];
			}
		}
		for (int j = 0; j < 5; j++) {
			sum2 += d19[j] * Math.pow(array[10 + j], 3);
		}
		for (int j = 0; j < 10; j++) {
			sum3 += b19[j] * array[j];
		}
		return sum1 + 2 * sum2 - sum3;
	}

	public double[]computeConstrain19(double array[]) {
		double values[] = new double[constrained];
			for (int j = 0; j < 5; j++) {
				double sum1 = 0;
				double sum2 = 0;
				for (int m = 0; m < 5; m++) {
					sum1 += c19[m][j] * array[10 + m];
				}
				for (int m = 0; m < 10; m++) {
					sum2 += a19[m][j] * array[m];
				}
				values[j] = -2 * sum1 - 3 * d19[j] * array[10 + j]
						* array[10 + j] - e19[j] + sum2;
			}
			return values;
	}
	
	public double objective20(double array[]) {
		double sum = 0;
		for (int j = 0; j < variables; j++) {
			sum += a20[j] * array[j];
		}
		return sum;
	}
	
	public double[] computeConstrain20(double array[]) {
		double values[] = new double[constrained];
		double sum1 = 0;
		for (int j = 0; j < this.variables; j++) {
			sum1 += array[j];
		}
		values[0] = (array[0] + array[12]) / (sum1 + e20[0]);
		values[1] = (array[1] + array[13]) / (sum1 + e20[1]);
		values[2] = (array[2] + array[14]) / (sum1 + e20[2]);
		values[3] = (array[6] + array[18]) / (sum1 + e20[3]);
		values[4] = (array[7] + array[19]) / (sum1 + e20[4]);
		values[5] = (array[8] + array[20]) / (sum1 + e20[5]);
		sum1 = 0;
		for (int j = 12; j < 24; j++) {
			sum1 += array[j] / b20[j];
		}
		double sum2 = 0;
		for (int j = 0; j < 12; j++) {
			sum2 += array[j] / b20[j];
		}
		values[6] = Math.abs(array[12] / (b20[12] * sum1) - c20[0] * array[0]
				/ (40 * b20[0] * sum2))
				- accurate;
		values[7] = Math.abs(array[13] / (b20[13] * sum1) - c20[1] * array[1]
				/ (40 * b20[1] * sum2))
				- accurate;
		values[8] = Math.abs(array[14] / (b20[14] * sum1) - c20[2] * array[2]
				/ (40 * b20[2] * sum2))
				- accurate;
		values[9] = Math.abs(array[15] / (b20[15] * sum1) - c20[3] * array[3]
				/ (40 * b20[3] * sum2))
				- accurate;
		values[10] = Math.abs(array[16] / (b20[16] * sum1) - c20[4] * array[4]
				/ (40 * b20[4] * sum2))
				- accurate;
		values[11] = Math.abs(array[17] / (b20[17] * sum1) - c20[5] * array[5]
				/ (40 * b20[5] * sum2))
				- accurate;
		values[12] = Math.abs(array[18] / (b20[18] * sum1) - c20[6] * array[6]
				/ (40 * b20[6] * sum2))
				- accurate;
		values[13] = Math.abs(array[19] / (b20[19] * sum1) - c20[7] * array[7]
				/ (40 * b20[7] * sum2))
				- accurate;
		values[14] = Math.abs(array[20] / (b20[20] * sum1) - c20[8] * array[8]
				/ (40 * b20[8] * sum2))
				- accurate;
		values[15] = Math.abs(array[21] / (b20[21] * sum1) - c20[9] * array[9]
				/ (40 * b20[9] * sum2))
				- accurate;
		values[16] = Math.abs(array[22] / (b20[22] * sum1) - c20[10]
				* array[10] / (40 * b20[10] * sum2))
				- accurate;
		values[17] = Math.abs(array[23] / (b20[23] * sum1) - c20[11]
				* array[11] / (40 * b20[11] * sum2))
				- accurate;
		sum1 = 0;
		for (int j = 0; j < 24; j++) {
			sum1 += array[j];
		}
		values[18] = Math.abs(sum1 - 1) - accurate;
		sum1 = sum2 = 0;
		for (int j = 0; j < 12; j++) {
			sum1 += array[j] / d20[j];
		}
		for (int j = 12; j < 24; j++) {
			sum2 += array[j] / b20[j];
		}
		values[19] = Math.abs(sum1 + 0.7302 * 530 * (14.7 / 40) * sum2 - 1.671)
				- accurate;
		return values;
	}
	
	public double objective21(double array[]) {
		return array[0];
	}
	
	public double []computeConstrain21(double array[]) {
		double values[] = new double[constrained];
		values[0] = -array[0] + 35 * Math.pow(array[1], 0.6) + 35
				* Math.pow(array[2], 0.6);
		values[1] = Math.abs(-300 * array[2] + 7500 * array[4] - 7500
				* array[5] - 25 * array[3] * array[4] + 25 * array[3]
				* array[5] + array[2] * array[3])
				- accurate;
		values[2] = Math.abs(100 * array[1] + 155.365 * array[3] + 2500
				* array[6] - array[1] * array[3] - 25 * array[3] * array[6]
				- 15536.5)
				- accurate;
		values[3] = Math.abs(-array[4] + Math.log(-array[3] + 900)) - accurate;
		values[4] = Math.abs(-array[5] + Math.log(array[3] + 300)) - accurate;
		values[5] = Math.abs(-array[6] + Math.log(-2 * array[3] + 700))
				- accurate;
		return values;
	}

	public double objective22(double array[]) {
		return array[0];
	}
	
	public double[] computeConstrain22(double array[]) {
		double values[] = new double[constrained];
		values[0] = -array[0] + Math.pow(array[1], 0.6)
				+ Math.pow(array[2], 0.6) + Math.pow(array[3], 0.6);
		values[1] = Math
				.abs(array[4] - 100000 * array[7] + 1 * Math.pow(10, 7))
				- accurate;
		values[2] = Math.abs(array[5] + 100000 * array[7] - 100000 * array[8])
				- accurate;
		values[3] = Math
				.abs(array[6] + 100000 * array[8] - 5 * Math.pow(10, 7))
				- accurate;
		values[4] = Math.abs(array[4] + 100000 * array[9] - 3.3
				* Math.pow(10, 7))
				- accurate;
		values[5] = Math.abs(array[5] + 100000 * array[10] - 4.4
				* Math.pow(10, 7))
				- accurate;
		values[6] = Math.abs(array[6] + 100000 * array[11] - 6.6
				* Math.pow(10, 7))
				- accurate;
		values[7] = Math.abs(array[4] - 120 * array[1] * array[12]) - accurate;
		values[8] = Math.abs(array[5] - 80 * array[2] * array[13]) - accurate;
		values[9] = Math.abs(array[6] - 40 * array[3] * array[14]) - accurate;
		values[10] = Math.abs(array[7] - array[10] + array[15]) - accurate;
		values[11] = Math.abs(array[8] - array[11] + array[16]) - accurate;
		values[12] = Math.abs(-array[17] + Math.log(array[9] - 100)) - accurate;
		values[13] = Math.abs(-array[18] + Math.log(-array[7] + 300))
				- accurate;
		values[14] = Math.abs(-array[19] + Math.log(array[15])) - accurate;
		values[15] = Math.abs(-array[20] + Math.log(-array[8] + 400))
				- accurate;
		values[16] = Math.abs(-array[21] + Math.log(array[16])) - accurate;
		values[17] = Math.abs(-array[7] - array[9] + array[12] * array[17]
				- array[12] * array[18] + 400)
				- accurate;
		values[18] = Math.abs(array[7] - array[8] - array[10] + array[13]
				* array[19] - array[13] * array[20] + 400)
				- accurate;
		values[19] = Math.abs(array[8] - array[11] - 4.60517 * array[14]
				+ array[14] * array[21] + 100)
				- accurate;
		return values;
	}
	
	public double objective23(double array[]) {
		return -9 * array[4] - 15 * array[7] + 6 * array[0] + 16 * array[1]
				+ 10 * (array[5] + array[6]);
	}
	
	public double[] computeConstrain23(double array[]) {
		double values[] = new double[constrained];
		values[0] = array[8] * array[2] + 0.02 * array[5] - 0.025 * array[4];
		values[1] = array[8] * array[3] + 0.02 * array[6] - 0.015 * array[7];
		values[2] = Math.abs(array[0] + array[1] - array[2] - array[3])
				- accurate;
		values[3] = Math.abs(0.03 * array[0] + 0.01 * array[1] - array[8]
				* (array[2] + array[3]))
				- accurate;
		values[4] = Math.abs(array[2] + array[5] - array[4]) - accurate;
		values[5] = Math.abs(array[3] + array[6] - array[7]) - accurate;
		return values;
	}
	
	public double objective24(double array[]) {
			return -array[0] - array[1];
	}
	
	public double[] computeConstrain24(double array[]) {
		double values[] = new double[constrained];
		values[0] = -2 * Math.pow(array[0], 4) + 8 * Math.pow(array[0], 3) - 8
				* array[0] * array[0] + array[1] - 2;
		values[1] = -4 * Math.pow(array[0], 4) + 32 * Math.pow(array[0], 3)
				- 88 * array[0] * array[0] + 96 * array[0] + array[1] - 36;
		return values;
	}
	
	public double objective25(double array[], double o[]) {
		double sum1 = 0;
		double sum2 = 1;
		double sum3 = 0;
		for (int j = 0; j < this.variables; j++) {
			sum1 += Math.pow(Math.cos((array[j] - o[j])), 4);
			sum2 *= Math.pow(Math.cos(array[j] - o[j]), 2);
			sum3 += (j + 1) * (array[j] - o[j]) * (array[j] - o[j]);
		}
		sum2 *= -2;
		sum3 = Math.sqrt(sum3);
		return -Math.abs((sum1 + sum2) / sum3);
	}
	
	public double[] computeConstrain25(double array[],double o[]){
		double values[] = new double[constrained];
		double sum1 = 1;
		double sum2 = 0;
		for (int j = 0; j < variables; j++) {
			sum1 *= (array[j] - o[j]);
			sum2 += (array[j] - o[j]);
		}
		values[0] = 0.75 - sum1;
		values[1] = sum2 - 7.5 * variables;
		return values;
	}
	
	public double objective26(double array[],double o[]){
		double max = array[0]-o[0];
		for(int j = 1; j < variables; j++){
			if((array[j]-o[j]) > max){
				max = array[j]-o[j];
			}
		}
		return max;
	}
	public double[]computeConstrain26(double array[],double o[]){
		double values[] = new double[constrained];
		double sum1 = 0;
		double sum2 = 0;
		for(int j = 0; j < variables; j++){
			sum1+=((array[j]-o[j])*(array[j]-o[j])-10*Math.cos(2*Math.PI*(array[j]-o[j]))+10);
			sum2+=((array[j]-o[j]-0.5)*(array[j]-o[j]-0.5)-10*Math.cos(2*Math.PI*(array[j]-o[j]-0.5))+10);
		}
		values[0] = 10-sum1/(double)variables;
		values[1] = sum1/(double)variables-15;
		values[2] = Math.abs(sum2/(double)variables-20)-accurate;
		return values;
	}
	
	public double objective27(double array[],double o[]){
		double sum = 0;
		double array1 [] = new double[array.length];
		for(int i = 0; i < variables; i++){
			array1[i] = array[i]-o[i];
		}
		
		for(int j = 0; j < variables-1; j++){
			sum+=100*Math.pow(array1[j]*array1[j]-array1[j+1],2)+Math.pow(array1[j]-1, 2);
		}
		return sum;
	}
	public double[]computeConstrain27(double array[],double o[]){
		double values[] = new double[constrained];
		double array1 [] = new double[array.length];
		for(int i = 0; i < variables; i++){
			array1[i] = array[i]-o[i];
		}
		double sum = 0;
		for(int j = 0; j < variables-1; j++){
			sum+=Math.pow(array1[j]-array1[j+1], 2);
		}
		values[0] = Math.abs(sum)-accurate;
		return values;
	}
	
	public double objective28(double array[]){
		return 100*Math.pow((array[1]-array[0]*array[0]),2)+Math.pow(array[0]-1, 2);
	}
	
	public double[] computeConstrain28(double array[]){
		double values[] = new double[constrained];
		values[0] = Math.pow(array[0]-1, 3)-array[1]+1;
		values[1] = array[0]+array[1]-2;
		return values;
	}
	
	public double objective29(double array[]){
		double sum = 0;
		sum+=Math.pow(array[0]-2, 2);
		sum+=Math.pow(array[1]-2, 2);
		sum+=Math.pow(array[2]-3, 2);
		sum+=Math.pow(array[3]-1, 2);
		sum+=Math.pow(array[4]-2, 2);
		sum+=Math.pow(array[5]-1, 2);
		sum-=Math.log(array[6]+1);
		return sum;
	}
	
	public double[] computeConstrain29(double array[]){
		double values[] = new double[constrained];
		values[0] = array[0]+array[1]+array[2]+array[3]+array[4]+array[5]-5;
		values[1] = Math.pow(array[0], 2)+Math.pow(array[1], 2)+Math.pow(array[2], 2)+Math.pow(array[5], 2)-5.5;
		values[2] = array[3]+array[0]-1.2;
		values[3] = array[4]+array[1]-1.8;
		values[4] = array[5]+array[2]-2.5;
		values[5] = array[6]+array[0]-1.2;
		values[6] = Math.pow(array[4], 2)+Math.pow(array[1], 2)-1.64;
		values[7] = Math.pow(array[5], 2)+Math.pow(array[2], 2)-4.25;
		values[8] = Math.pow(array[4], 2)+Math.pow(array[2], 2)-4.64;
		return values;
	}
	
	public double objective30(double array[]){
		return 5.3578547*Math.pow(array[2], 2)+0.8356891*array[0]*array[4]+
		37.293239*array[0]-40792.141;
	}
	public double[] computeConstrain30(double array[]){
		double values[] = new double[constrained];
		values[0] = 85.334407+0.0056858*array[1]*array[4]+0.00026*array[0]*array[3]-0.0022053*array[2]*array[4]-92;
		values[1] = -(85.334407+0.0056858*array[1]*array[4]+0.00026*array[0]*array[3]-0.0022053*array[2]*array[4]);
		values[2] = 80.51249+0.0071317*array[1]*array[4]+0.00026*array[0]*array[1]+0.0021813*array[2]*array[2]-110; 
		values[3] = -(80.51249+0.0071317*array[1]*array[4]+0.00026*array[0]*array[1]+0.0021813*array[2]*array[2])+90;
		values[4] = 9.300961+0.0047026*array[2]*array[4]+0.0012547*array[0]*array[2]+0.0019085*array[2]*array[3]-25;
		values[5] = 20-(9.300961+0.0047026*array[2]*array[4]+0.0012547*array[0]*array[2]+0.0019085*array[2]*array[3]);
		return values;
	}
	public double[] findMin(){
		double array[] = new double[2];
		double minimumValue = Double.MAX_VALUE;
		double minIndex = 0;
		int index = 0;
		double value = 0;
		for(int i = 0; i < feasible.size(); i++){
			index = feasible.get(i);
			value = f[index];
			if(value < minimumValue){
				minimumValue = value;
				minIndex = index;
			}
		}
		array[0] = minimumValue;
		array[1] = minIndex;
		return array;
	}
	
	public void objectiveAndConstrain(int index, double fArray[],
		double phiArray[][], double xArray[][]) {
		double phi1[] = new double[constrained];
		switch (index) {
		case 1: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective1(xArray[j]);
				phi1 = computeConstrain1(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 2: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective2(xArray[j]);
				phi1 = computeConstrain2(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 3: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective3(xArray[j]);
				phi1 = computeConstrain3(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 4: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective4(xArray[j]);
				phi1 = computeConstrain4(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 5: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective5(xArray[j]);
				phi1 = computeConstrain5(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 6: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective6(xArray[j]);
				phi1 = computeConstrain6(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 7: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective7(xArray[j]);
				phi1 = computeConstrain7(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 8: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective8(xArray[j]);
				phi1 = computeConstrain8(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 9: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective9(xArray[j]);
				phi1 = computeConstrain9(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 10: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective10(xArray[j]);
				phi1 = computeConstrain10(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 11: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective11(xArray[j]);
				phi1 = computeConstrain11(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 12: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective12(xArray[j]);
				phi1 = computeConstrain12(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 13: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective13(xArray[j]);
				phi1 = computeConstrain13(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 14: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective14(xArray[j]);
				phi1 = computeConstrain14(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 15: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective15(xArray[j]);
				phi1 = computeConstrain15(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 16: {
			for (int j = 0; j < fArray.length; j++) {
				double item[][] = this.computeItem(xArray[j]);
				double itemY[] = new double[17];
				double itemC[] = new double[17];
				for(int i = 0; i < item[0].length; i++){
					itemY[i] = item[0][i];
					itemC[i] = item[1][i];
				}
				fArray[j] = objective16(xArray[j],itemY,itemC);
				phi1 = computeConstrain16(xArray[j],itemY,itemC);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 17: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective17(xArray[j]);
				phi1 = computeConstrain17(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 18: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective18(xArray[j]);
				phi1 = computeConstrain18(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 19: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective19(xArray[j]);
				phi1 = computeConstrain19(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 20: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective20(xArray[j]);
				phi1 = computeConstrain20(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 21: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective21(xArray[j]);
				phi1 = computeConstrain21(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 22: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective22(xArray[j]);
				phi1 = computeConstrain22(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 23: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective23(xArray[j]);
				phi1 = computeConstrain23(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 24: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective24(xArray[j]);
				phi1 = computeConstrain24(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 25: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective25(xArray[j],o25);
				phi1 = computeConstrain25(xArray[j],o25);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 26: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective26(xArray[j],o26);
				phi1 = computeConstrain26(xArray[j],o26);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 27: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective27(xArray[j],o27);
				phi1 = computeConstrain27(xArray[j],o27);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 28: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective28(xArray[j]);
				phi1 = computeConstrain28(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 29: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective29(xArray[j]);
				phi1 = computeConstrain29(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		case 30: {
			for (int j = 0; j < fArray.length; j++) {
				fArray[j] = objective30(xArray[j]);
				phi1 = computeConstrain30(xArray[j]);
				for (int m = 0; m < constrained; m++) {
					phiArray[j][m] = phi1[m];
				}
			}
			break;
		}
		}
	}
	
	public void findFeasibleIndividuals() {
		boolean flag;
		for (int i = 0; i < lambda; i++) {
			flag = true;
			for (int j = 0; j < constrained; j++) {
				if (phi[i][j] > 0) {
					flag = false;
					break;
				}
			}
			if (flag == true) {
				feasible.add(i);
			}
		}
	}
	public double[] computeMinMean() {
		double array[] = new double[3];
		int minIndex = 0;
		int index;
		double minimumV = Double.MAX_VALUE;
		double sum = 0;
		for (int i = 0; i < feasible.size(); i++) {
			index = feasible.get(i);
			sum += f[index];
			if (f[index] < minimumV) {
				minimumV = f[index];
				minIndex = index;
			}
		}
		array[0] = minimumV;
		array[1] = minIndex;
		array[2] = sum / (double) feasible.size();
		return array;
	}
	public void statisticalFES(int gen,int runNumber){
		if (gen == 5000 / lambda) {
			//if (feasible.size() != 0) {
			if(bestMin!=Double.MAX_VALUE){
				power3[runNumber] = bestMin;
				for (int j = 0; j < variables; j++) {
					power3Individual[runNumber][j] = bestIndividual[j];
				}
			} else {
				double qua = Double.MAX_VALUE;
				double invalue = Double.MAX_VALUE;
				int index = 0;
				for (int j = 0; j < this.lambda; j++) {
					if (quadratic_loss[j] < qua) {
						qua = quadratic_loss[j];
						invalue = f[j];
						index = j;
					}
				}
				power3[runNumber] = invalue;
				for (int j = 0; j < variables; j++) {
					power3Individual[runNumber][j] = x[index][j];
				}
			}

		} else if (gen == 50000 / lambda) {
			//if (feasible.size() != 0) {
			if(bestMin!=Double.MAX_VALUE){
				power4[runNumber] = bestMin;
				for (int j = 0; j < variables; j++) {
					power4Individual[runNumber][j] = bestIndividual[j];
				}
			} else {
				double qua = Double.MAX_VALUE;
				double invalue = Double.MAX_VALUE;
				int index = 0;
				for (int j = 0; j < this.lambda; j++) {
					if (quadratic_loss[j] < qua) {
						qua = quadratic_loss[j];
						invalue = f[j];
						index = j;
					}
				}
				power4[runNumber] = invalue;
				for (int j = 0; j < variables; j++) {
					power4Individual[runNumber][j] = x[index][j];
				}
			}
		} else if (gen == 500000 / lambda) {
			//if (feasible.size() != 0) {
			if(bestMin!=Double.MAX_VALUE){
				power5[runNumber] = bestMin;
				for (int j = 0; j < variables; j++) {
					power5Individual[runNumber][j] = bestIndividual[j];
				}
			} else {
				double qua = Double.MAX_VALUE;
				double invalue = Double.MAX_VALUE;
				int index = 0;
				for (int j = 0; j < this.lambda; j++) {
					if (quadratic_loss[j] < qua) {
						qua = quadratic_loss[j];
						invalue = f[j];
						index = j;
					}
				}
				power5[runNumber] = invalue;
				for (int j = 0; j < variables; j++) {
					power5Individual[runNumber][j] = x[index][j];
				}
			}
		}
	}
	public double findMax_Column(int col) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < lambda; i++) {
			if (phi[i][col] > max) {
				max = phi[i][col];
			}
		}
		return max;
	}

	public double findMin_Column(int col) {
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < lambda; i++) {
			if (min > phi[i][col] && phi[i][col] > 0) {
				min = phi[i][col];
			}
		}
		return min;
	}
	public double mean(int column){
		double sum = 0;
		int number = 0;
		for(int i = 0 ; i < lambda; i++){
			if(phi[i][column]>0){
				sum+=phi[i][column];
				number++;
			}
		}
		return sum/(double)number;
	}
	public void computeQuadratic_loss_Normalize() {
		double array[] = new double[constrained];
		double array1[] = new double[constrained];
		for (int i = 0; i < constrained; i++) {
			array1[i] = findMin_Column(i);
			array[i] = findMax_Column(i);// - array1[i];
			//System.out.println("min "+array1[i]+" max "+array[i]);
		}
		for (int i = 0; i < lambda; i++) {
			for (int j = 0; j < constrained; j++) {
				phi[i][j] = (phi[i][j]) / (array[j]-array1[j]);
			//	System.out.print("phi "+phi[i][j]+" ");
			}
			//System.out.println();
		}
	}
	public void overall_Normal(){
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for(int i = 0; i < lambda; i++){
			if(quadratic_loss[i] > 0&&min > quadratic_loss[i]){
				min = quadratic_loss[i];
			}
			if(quadratic_loss[i] > 0&&max < quadratic_loss[i]){
				max = quadratic_loss[i];
			}
		}
		for(int i = 0; i < lambda; i++){
			quadratic_loss[i] = (quadratic_loss[i])/max;
		}
	}
	public void computeQuadratic_loss() {
		// computeQuadratic_loss_Normalize();
		double sum = 0;
		for (int i = 0; i < lambda; i++) {
			sum = 0;
			for (int j = 0; j < constrained; j++) {
				if (phi[i][j] > 0) {
				//	numberOfConstrained[j]++;
					sum += Math.pow(phi[i][j],4);// * phi[i][j];
				//	System.out.print("phi "+phi[i][j]+" ");
				}
			}
//			System.out.println();
//			System.out.println("sum "+sum);
			quadratic_loss[i] = sum;
		}
	}
	
	public int minConstraints(){
		double min = Double.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < lambda; i++){
			double sum = 0;
			for(int j = 0; j < constrained; j++){
				if(phi[i][j] > 0){
					sum+=phi[i][j];
				}
				if(sum < min){
					min = sum;
					index = i;
				}
			}
		}
		return index;
	}
	// return the best objective value found
	public double es(int indexf, int runNumber, double optimal)
			throws IOException {

		init_x();
		init_eta();
	
//		int constraints[] = new int[constrained];
//		for(int j = 0; j < constrained; j++){
//			weights[j] = 1;
//		}
		bestMin = Double.MAX_VALUE;
		currentMin  = Double.MAX_VALUE;
		SuccessGm = Integer.MAX_VALUE;
		bestflag = false;
		Gm = Integer.MAX_VALUE;
		for(int j = 0; j < variables; j++){
			bestIndividual[j] = Double.MAX_VALUE;
			bestEta[j] = Double.MAX_VALUE;
		}
		boolean flag = false;
		// the mu selected parents,then each parent reproduce lambda/mu children
		double parent[][] = new double[mu][variables];
		// the corresponding eta parameters for the selected parents
		double parent_eta[][] = new double[mu][variables];
		for (int i = 0; i < MaxGen; i++) {
			objectiveAndConstrain(indexf, f, phi, x);
			findFeasibleIndividuals();
			
			if (feasible.size() != 0) {
				double array[] = computeMinMean();
				currentMin = array[0];
				minIndex = (int) array[1];			
				}
			// keep the best individual found
			if (currentMin < bestMin) {
				bestMin = currentMin; // to refresh the bestMin
				Gm = i; // to refresh the Gm
				if (((bestMin - optimal) < 0.0001) && flag == false) {
					SuccessGm = i;
					flag = true;
				}
				// to store the best individual values
				for (int j = 0; j < variables; j++) {
					bestIndividual[j] = x[minIndex][j];
					bestEta[j] = eta[minIndex][j];
					bestflag = true;
				}
			}

//			if(bestMin != Double.MAX_VALUE){
//				if(bestMin > optimal){
//					System.out.println(bestMin+" "+i);
//				}
//			}else{
//				int index = minConstraints();
//				if(f[index] > optimal){
//					System.out.println(f[index]+" "+i);
//				}
//			}
//		if(i != 0){
//			for(int j = 0; j < constrained; j++){
//				constraints[j] = numberOfConstrained[j];
//				numberOfConstrained[j] = 0;
//			}
//		}
			computeQuadratic_loss_Normalize();
			computeQuadratic_loss();
	//		overall_Normal();
			sort(indexf, i);
			//sort1();
		//	sorting(indexf,i);
		//	sortTem(indexf, i);
//			if(i != 0){
//			for(int j = 0; j < constrained; j++){
//				weights[j] = weights[j]*(numberOfConstrained[j]+1)/((double)constraints[j]+1);
//				}
//			}
//			}
			
			for (int j = 0; j < mu; j++) {
				for (int m = 0; m < variables; m++) {
					parent[j][m] = x[ranked[j]][m];
					parent_eta[j][m] = eta[ranked[j]][m];
				}
			}

			// reproduction
			for (int j = 0; j < lambda; j++) {
				for (int m = 0; m < variables; m++) {
					x[j][m] = parent[j % mu][m];
					x_[j][m] = parent[j % mu][m];
					eta[j][m] = parent_eta[j % mu][m];
				}
			}

			
//			if (bestMin != Double.MAX_VALUE) {
//				for (int j = 0; j < lambda / mu; j++) {
//					for (int m = 0; m < variables; m++) {
//						x[(j+1) * mu-1][m] = bestIndividual[m];
//						x_[(j+1) * mu-1][m] = bestIndividual[m];
//						eta[(j+1) * mu-1][m] = bestEta[m];
//					}
//				}
//			}
			statisticalFES(i, runNumber);
			
			if (!this.isContain(indexf)) {
				this.newES(0, lambda,i);
			} else {
				this.newES1(0, lambda,i);
			}
//			newESnew(0, lambda, i);
//			selection();
			feasible.clear();
		}

		if (bestflag == false) {
			feasibleRun--;
			System.out.println("warning: solution is infeasible");
		}
		System.out.println(bestMin + "  " + SuccessGm);
//		if (SuccessGm < Integer.MAX_VALUE) {
//			for (int i = 0; i < variables; i++) {
//				System.out.print(bestIndividual[i] + " ");
//			}
//			System.out.println();
//		}
		return bestMin;
	}
	
	public void newES(int start, int end,int gen) {
		update_eta(3*mu+mu/2,end);
		mutation(0,3*mu+mu/2,3*mu+mu/2,end);
		exponentialSmoothing(3*mu+mu/2,end,gen);
	}
	
//	public void newESnew(int start, int end, int gen){
//		update_etaNew(3*mu+mu/2, end);
//		mutationNew(0,3*mu+mu/2,3*mu+mu/2,end);	
//		exponentialSmoothingNew(3*mu+mu/2, end,gen);
//	}
	
	public void newES1(int start, int end,int gen) {
//		update_eta( 3*mu, 6*mu);
//		mutation(0, 3*mu,3*mu,6*mu);
//		exponentialSmoothing(3*mu, 6*mu,gen);
		update_eta( 3*mu+mu/2, end);
		mutation(0,3*mu+mu/2,3*mu+mu/2,end);
		exponentialSmoothing(3*mu+mu/2, end,gen);
	}
	
//	public void newES(int start, int end,int gen) {
//		update_eta( 2*mu, end);
//		mutation(0,2*mu,2*mu,end);
//		exponentialSmoothing(2*mu, end,gen);
//	}
//	
//	public void newES1(int start, int end,int gen) {
//		update_eta( mu, end);
//		mutation(0,mu,mu,end);
//		exponentialSmoothing(mu, end,gen);
//	}
	
	public void update_eta(int start, int end) {
		// to store the origional eta in oldEta
		for (int i = 0; i < lambda; i++) {
			for (int j = 0; j < variables; j++) {
				oldEta[i][j] = eta[i][j];
			}
		}
		// update eta as Improved SR
		double power = 0;
		double power1 = 0;
		double tem1 = 0;
		for (int i = start; i < end; i++) {
			power = tau_ * rand.nextGaussian();
			for (int j = 0; j < variables; j++) {
				power1 = power;
				tem1 =  rand.nextGaussian();
				power1 += tau * tem1;
				eta[i][j] = eta[i][j] * Math.exp(power1);
		//		System.out.print(power1+"  ");
			}
			//System.out.println();
		}
		// check if eta beyond upper bound
		for (int i = start; i < end; i++) {
			for (int j = 0; j < variables; j++) {
				if (eta[i][j] > eta_u[j]) {
					eta[i][j] = eta_u[j];
				}
			}
		}
	}
	
//	public void update_etaNew(int start, int end) {
//		// to store the origin eta in oldEta
//		for (int i = 0; i < lambda; i++) {
//			for (int j = 0; j < variables; j++) {
//				oldEta[i][j] = eta[i][j];
//			}
//		}
//		// update eta as Improved SR
//		double power = 0;
//		double power1 = 0;
//		double tem1 = 0;
//		for (int i = start; i < end; i++) {
//			power = tau_ * rand.nextGaussian();
//			for (int j = 0; j < variables; j++) {
//				power1 = power;
//				tem1 =  rand.nextGaussian();
//				power1 += tau * tem1;
//				newEta[i][j] = eta[i][j] * Math.exp(power1);
//		//		System.out.print(power1+"  ");
//			}
//			//System.out.println();
//		}
//		// check if eta beyond upper bound
//		for (int i = start; i < end; i++) {
//			for (int j = 0; j < variables; j++) {
//				if (newEta[i][j] > eta_u[j]) {
//					newEta[i][j] = eta_u[j];
//				}
//			}
//		}
//	}
	public void mutation(int start1,int end1,int start2, int end2) {
//		for(int i = start1; i < end1; i++){
//			for(int j = 0; j < variables; j++){
//				x_[i][j] = x[i][j];
//			}
//		}
		int tries = 0;
//		for (int i = 0; i < mu+15; i++) {
//			tries = 1;
//			int tem = Math.abs(rand.nextInt() % mu);
//			int rankes = Math.abs(rand.nextInt()%10);
//		//	gamma = 0.7+3*rand.nextDouble()/(double)10;
//			for (int j = 0; j < variables; j++) {
//				x_[i][j] = x[i][j];
//		//		System.out.println(x_[rankes][j]);
//				x[i][j] = x[rankes][j] + gamma * (x[i][j] - x[tem][j]);
//				//x[i][j] = x_[rankes][j] + gamma * (x[i][j] - x_[tem][j]);
//				while (((x[i][j] > lu[1][j]) || (x[i][j] < lu[0][j]))
//						&& (tries < (trytimes + 1))) {
//					x[i][j] = x_[i][j] + eta[i][j] * rand.nextGaussian();
//					tries++;
//				}
//				if (tries == (trytimes + 1)) {
//					x[i][j] = x_[i][j];
//				}
//			}
//		}
	    tries = 1;
		for (int i = start1; i < end1; i++) {
			tries = 1;
//			int tem = Math.abs(rand.nextInt() % mu)+2*mu;
//			int rankes = Math.abs(rand.nextInt()%4)+2*mu;
			int tem = Math.abs(rand.nextInt() % mu);//+1*mu;
			int tem1 = Math.abs(rand.nextInt() % mu);//+1*mu;
			int rankes = Math.abs(rand.nextInt()%10);//+1*mu;
		//	gamma = 0.7+3*rand.nextDouble()/(double)10;

			for (int j = 0; j < variables; j++) {
		//		x_[i][j] = x[i][j];
		//		System.out.println(x_[rankes][j]);
				x[i][j] = x_[rankes][j] + gamma * (x_[i][j] - x_[tem][j]);
			//	x[i][j] = x_[i][j] + gamma * (x_[rankes][j] - x_[tem][j]);
		//		x[i][j] = gamma*x[i][j];
		//		x[i][j] = x[rankes][j] + gamma * (x[i][j] - x[tem][j]);
				while (((x[i][j] > lu[1][j]) || (x[i][j] < lu[0][j]))
						&& (tries < (trytimes + 1))) {
					x[i][j] = x_[i][j] + eta[i][j] * rand.nextGaussian();
					tries++;
				}
				if (tries == (trytimes + 1)) {
					x[i][j] = x_[i][j];
				}
			}
		}
		// the mutation of lambda-mu+1 individuals are the same as SR
		for (int i = start2; i < end2; i++) {
			tries = 1;
			for (int j = 0; j < variables; j++) {
		//		x_[i][j] = x[i][j];
				x[i][j] += eta[i][j] * rand.nextGaussian();
				while (((x[i][j] > lu[1][j]) || (x[i][j] < lu[0][j]))
						&& (tries < (trytimes + 1))) {
					x[i][j] = x_[i][j] + eta[i][j] * rand.nextGaussian();
					tries++;
				}
				if (tries == (trytimes + 1)) {
					x[i][j] = x_[i][j];
				}
			}
		}
	}
	
//	public void mutationNew(int start1,int end1,int start2, int end2) {
//		int tries = 0;
//	    tries = 1;
//		for (int i = start1; i < end1; i++) {
//			tries = 1;
////			int tem = Math.abs(rand.nextInt() % mu)+2*mu;
////			int rankes = Math.abs(rand.nextInt()%4)+2*mu;
//			int tem = Math.abs(rand.nextInt() % mu);//+1*mu;
//			int tem1 = Math.abs(rand.nextInt() % mu);//+1*mu;
//			int rankes = Math.abs(rand.nextInt()%10);//+1*mu;
//		//	gamma = 0.7+3*rand.nextDouble()/(double)10;
//
//			for (int j = 0; j < variables; j++) {
//		//		x_[i][j] = x[i][j];
//		//		System.out.println(x_[rankes][j]);
//				newX[i][j] = x_[rankes][j] + gamma * (x_[i][j] - x_[tem][j]);
//			//	x[i][j] = x_[i][j] + gamma * (x_[rankes][j] - x_[tem][j]);
//		//		x[i][j] = gamma*x[i][j];
//		//		x[i][j] = x[rankes][j] + gamma * (x[i][j] - x[tem][j]);
//				while (((newX[i][j] > lu[1][j]) || (newX[i][j] < lu[0][j]))
//						&& (tries < (trytimes + 1))) {
//					newX[i][j] = x_[i][j] + eta[i][j] * rand.nextGaussian();
//					tries++;
//				}
//				if (tries == (trytimes + 1)) {
//					newX[i][j] = x_[i][j];
//				}
//			}
//		}
//		// the mutation of lambda-mu+1 individuals are the same as SR
//		for (int i = start2; i < end2; i++) {
//			tries = 1;
//			for (int j = 0; j < variables; j++) {
//		//		x_[i][j] = x[i][j];
//				newX[i][j] += eta[i][j] * rand.nextGaussian();
//				while (((newX[i][j] > lu[1][j]) || (newX[i][j] < lu[0][j]))
//						&& (tries < (trytimes + 1))) {
//					newX[i][j] = x_[i][j] + eta[i][j] * rand.nextGaussian();
//					tries++;
//				}
//				if (tries == (trytimes + 1)) {
//					newX[i][j] = x_[i][j];
//				}
//			}
//		}
//	}
	public boolean isContain(int index){
		for(int i = 0; i < equalIndex.length; i++){
			if(index == equalIndex[i]){
				return true;
			}
		}
		return false;
	}
	
	public void init_eta() {
		for (int i = 0; i < eta.length; i++) {
			for (int j = 0; j < eta[i].length; j++) {
				eta[i][j] = (lu[1][j] - lu[0][j]) / Math.sqrt(variables);
			}
		}
		// we use the initial value of eta as upper bound for eta
		for (int i = 0; i < variables; i++) {
			eta_u[i] = eta[0][i];
		}
	}
	public double findOptimum(int index,int gen) throws IOException {
		if(this.isContain(index)){
			probability[index] = 0.2*(1+5*Math.pow((gen/(double)MaxGen),0.5));
		//	probability[index] = 0.2*(1+5*Math.pow((gen/(double)MaxGen),0.5));
		//	probability[index] = 0.5*(1+Math.pow((gen/(double)MaxGen),4));
		}

		double optim = Double.MAX_VALUE;
		if (feasible.size() != 0) {
			for (int m = 0; m < feasible.size(); m++) {
				if (optim > f[feasible.get(m)]) {
					optim = f[feasible.get(m)];
				}
			}
		}

		double unoptim = Double.MAX_VALUE;
		double qua = Double.MAX_VALUE;
		double qua2 = Double.MAX_VALUE;

		for (int m = 0; m < this.lambda; m++) {
			if (quadratic_loss[m] != 0 && qua > quadratic_loss[m]) {
				qua = quadratic_loss[m];
				unoptim = f[m];
			}
			if (quadratic_loss[m] != 0 && f[m] < qua2) {
				qua2 = f[m];
			}
		}
		if (optim == Double.MAX_VALUE) {
			optim = unoptim;
		} else {
			if (optim > unoptim) {
				optim = (unoptim + optim) / 2;
			}
		}
		if (qua2 == Double.MAX_VALUE) {
		//	optimaFlag = true;
			return optim;
		} else {
			double tem = rand.nextDouble();
			if (tem < this.probability[index]) {
			//	optimaFlag = true;
				return optim;
			} else {
		//		int tem1 = Math.abs(rand.nextInt()%lambda);
		//		return f[tem1];
		//		System.out.println("qua2 "+qua2);
		//  	optimaFlag = false;
				return qua2;
	    //		return (unoptim+qua2)/2;
			}
		}
	}
	
	//it has problem
	public boolean isDominance(int i, int j) {
		if ((f[i] <= f[j] && quadratic_loss[i] <= quadratic_loss[i])
				|| (f[i] >= f[j] && quadratic_loss[i] >= quadratic_loss[j])) {
		//if(f[i] >= f[j]&&quadratic_loss[i] >= quadratic_loss[j]){
			return true;
		}
		return false;
	}
	
//	public boolean isDominanceNew(int index){
//		if ((f[index] <= newF[index] && quadratic_loss[index] <= newPhiAll[index])
//				|| (f[index] >= newF[index] && quadratic_loss[index] >= newPhiAll[index])) {
//			return true;
//		}
//		return false;
//	}
	public void exponentialSmoothing(int start, int end,int gen) {
	//		if(gen < MaxGen/10){
	//			alpha = 0.4;
	//		}else if(gen < MaxGen/5){
	//			alpha = 0.3;
	//		}else{
	//			alpha = 0.2;
	//		}
//		alpha = 0.4;
//		for (int i = start; i < end; i++) {
//		//alpha = 0.25+rand.nextDouble()/(double)10;
//		for (int j = 0; j < variables; j++) {
//			eta[i][j] = oldEta[i][j] + alpha * (eta[i][j] - oldEta[i][j]);
//		}
//	}
			alpha = 0.4;
			for (int i = start; i < start+mu; i++) {
				//alpha = 0.25+rand.nextDouble()/(double)10;
				for (int j = 0; j < variables; j++) {
					eta[i][j] = oldEta[i][j] + alpha * (eta[i][j] - oldEta[i][j]);
				}
			}
			alpha = 0.30;
			for (int i = start+mu; i < start+2*mu; i++) {
				//alpha = 0.25+rand.nextDouble()/(double)10;
				for (int j = 0; j < variables; j++) {
					eta[i][j] = oldEta[i][j] + alpha * (eta[i][j] - oldEta[i][j]);
				}
			}
			alpha = 0.20;
			for (int i = start+2*mu; i < end; i++) {
				//alpha = 0.25+rand.nextDouble()/(double)10;
				for (int j = 0; j < variables; j++) {
					eta[i][j] = oldEta[i][j] + alpha * (eta[i][j] - oldEta[i][j]);
				}
			}
//			for (int i = start; i < end; i++) {
//				//alpha = 0.25+rand.nextDouble()/(double)10;
//				for (int j = 0; j < variables; j++) {
//					eta[i][j] = oldEta[i][j] + alpha * (eta[i][j] - oldEta[i][j]);
//				}
//			}
		}
	
//	public void exponentialSmoothingNew(int start, int end, int gen){
//		for (int i = start; i < end; i++) {
//			//alpha = 0.25+rand.nextDouble()/(double)10;
//			for (int j = 0; j < variables; j++) {
//				newEta[i][j] = oldEta[i][j] + alpha * (eta[i][j] - oldEta[i][j]);
//			}
//		}
//	}
	public int compare(int i,  double optim) {
		boolean flag = isDominance(ranked[i], ranked[i + 1]);
		if (flag == true) {
			return 1;
		}

		if (quadratic_loss[ranked[i]] == 0
				&& quadratic_loss[ranked[i + 1]] != 0) {

			double d1 = f[ranked[i]] - optim;
			double d2 = Math.sqrt(quadratic_loss[ranked[i + 1]]
					* quadratic_loss[ranked[i + 1]]
					+ (optim - f[ranked[i + 1]]) * (optim - f[ranked[i + 1]]));
			if (d1 > d2) {
				return -1;
			} else {
				return 1;
			}

		} else if (quadratic_loss[ranked[i]] != 0
				&& quadratic_loss[ranked[i + 1]] == 0) {

			double d1 = f[ranked[i + 1]] - optim;
			double d2 = Math.sqrt(quadratic_loss[ranked[i]]
					* quadratic_loss[ranked[i]] + (optim - f[ranked[i]])
					* (optim - f[ranked[i]]));
			if (d1 > d2) {
				return -1;
			} else {
				return 1;
			}

		} else {
			if ((optim > f[ranked[i]] && f[ranked[i]] > f[ranked[i + 1]])
					|| (optim > f[ranked[i + 1]] && f[ranked[i + 1]] > f[ranked[i]])) {
				return 1;
			} else {
				double d1 = Math.sqrt(quadratic_loss[ranked[i]]
						* quadratic_loss[ranked[i]] + (optim - f[ranked[i]])
						* (optim - f[ranked[i]]));
				double d2 = Math.sqrt(quadratic_loss[ranked[i + 1]]
						* quadratic_loss[ranked[i + 1]]
						+ (optim - f[ranked[i + 1]])
						* (optim - f[ranked[i + 1]]));
				if (d1 < d2) {
					if (f[ranked[i]] < f[ranked[i + 1]]) {
						return -1;
					} else {
						return 1;
					}
				} else {
					if (f[ranked[i + 1]] < f[ranked[i]]) {
						return -1;
					} else {
						return 1;
					}
				}
			}
		}
	}
	
	public int compare2(int i,  double optim) {
		boolean flag = isDominance(ranked[i], ranked[i + 1]);
		if (flag == true) {
			return 1;
		}

		if (quadratic_loss[ranked[i]] == 0
				&& quadratic_loss[ranked[i + 1]] != 0) {

			double d1 = Math.abs(f[ranked[i]] - optim);
			double d2 = quadratic_loss[ranked[i + 1]]
					+ Math.abs(optim - f[ranked[i + 1]]);
			if (d1 > d2) {
				return -1;
			} else {
				return 1;
			}

		} else if (quadratic_loss[ranked[i]] != 0
				&& quadratic_loss[ranked[i + 1]] == 0) {

			double d1 = Math.abs(f[ranked[i + 1]] - optim);
			double d2 = quadratic_loss[ranked[i]]
					 + Math.abs(optim - f[ranked[i]]);
			if (d1 > d2) {
				return -1;
			} else {
				return 1;
			}

		} else {
			if ((optim > f[ranked[i]] && f[ranked[i]] > f[ranked[i + 1]])
					|| (optim > f[ranked[i + 1]] && f[ranked[i + 1]] > f[ranked[i]])) {
				return 1;
			} else {
				double d1 = quadratic_loss[ranked[i]] + Math.abs(optim - f[ranked[i]]);
				double d2 = quadratic_loss[ranked[i + 1]]
						+ Math.abs(optim - f[ranked[i + 1]]);
				if (d1 < d2) {
					if (f[ranked[i]] < f[ranked[i + 1]]) {
						return -1;
					} else {
						return 1;
					}
				} else {
					if (f[ranked[i + 1]] < f[ranked[i]]) {
						return -1;
					} else {
						return 1;
					}
				}
			}
		}
	}
	
	public int comparison(int i, int j){
		if(f[i]<=f[j]&&quadratic_loss[i]<=quadratic_loss[j]){
			return 1;
		}else if(f[i]>=f[j]&&quadratic_loss[i]>=quadratic_loss[j]){
			return -1;
		}
		double distance1 = 0;
		double distance2 = 0;
		distance1 = Math.abs(optimum-f[i]);
		distance2 = Math.abs(optimum-f[j]);
		if(distance1 <= distance2){
			return 1;
		}else{
			return -1;
		}
	}
	public void sorting(int index, int gen) throws IOException{
		for (int i = 0; i < ranked.length; i++) {
			ranked[i] = i;
		}
		
		optimum = findOptimum(index, gen);
		boolean flag = true;
		int tem = 0; 
		int indicator = 0;
		for(int i = 0; i < lambda; i++){
			flag = false;
			for (int j = 0; j < lambda - 1 - i; j++){
				indicator = this.comparison(ranked[j], ranked[j+1]);
				if(indicator == -1){
					tem = ranked[j];
					ranked[j] = ranked[j+1];
					ranked[j+1] = tem;
					
					flag = true;
				}
			}
			if (flag == false) {
				break;
			}
		}
	}
//	public int compare4(int i,  double optim) {
//		boolean flag = isDominance(ranked[i], ranked[i + 1]);
//		if (flag == true) {
//			return 1;
//		}
//
//		double d1 = Math.abs(f[ranked[i]]-optim);
//		double d2 = Math.abs(optim - f[ranked[i + 1]]);
//		if(d1 < d2){
//			return -1;
//		}else{
//			return 1;
//		}
//	}
	public int compare3(int i,  double optim) {
		boolean flag = isDominance(ranked[i], ranked[i + 1]);
		if (flag == true) {
			return 1;
		}

		if (quadratic_loss[ranked[i]] == 0
				&& quadratic_loss[ranked[i + 1]] != 0) {
			double d1 = f[ranked[i]] - optim;
			double d2 = Math.abs(optim - f[ranked[i + 1]]);
			
			//when d1>d2, then we want to select d2, and d2 is the infeasible one,
			//if we want to choose it, only by comparing the objective value
			//if d1 < d2, then we can choose it by comparing quadratic loss value because d1
			//is a feasible solution
			if (d1 > d2) {
				return -1;
			} else {
				return 1;
			}
		} else if (quadratic_loss[ranked[i]] != 0
				&& quadratic_loss[ranked[i + 1]] == 0) {

			double d1 = Math.abs(f[ranked[i]] - optim);
			double d2 = Math.abs(optim - f[ranked[i+1]]);
			if (d1 > d2) {
				return 1;
			} else {
				return -1;
			}

		} else {
			if ((optim > f[ranked[i]] && f[ranked[i]] > f[ranked[i + 1]])
					|| (optim > f[ranked[i + 1]] && f[ranked[i + 1]] > f[ranked[i]])) {
				return 1;
			} else {
				double d1 = Math.abs(optim - f[ranked[i]]);
				double d2 = Math.abs(optim - f[ranked[i + 1]]);
				if (d1 < d2) {
					if (f[ranked[i]] < f[ranked[i + 1]]) {
						return -1;
					} else {
						return 1;
					}
				} else {
					if (f[ranked[i]] > f[ranked[i+1]]) {
						return -1;
					} else {
						return 1;
					}
				}
			}
		}
	}

//	//-1 means using the objective as the comparion criteria
//	//1 means using the quadratic value as the compariso criteria
//	public int compare3(int i,  double optim) {
//		boolean flag = isDominance(ranked[i], ranked[i + 1]);
//		if (flag == true) {
//			return 1;
//		}
//
//		if (quadratic_loss[ranked[i]] == 0
//				&& quadratic_loss[ranked[i + 1]] != 0) {
//			double d1 = Math.abs(f[ranked[i]] - optim);
//			double d2 = Math.abs(optim - f[ranked[i + 1]]);
//			if (d1 > d2) {
//				return -1;
//			} else {
//				return 1;
//			}
//
//		} else if (quadratic_loss[ranked[i]] != 0
//				&& quadratic_loss[ranked[i + 1]] == 0) {
//			double d1 = Math.abs(f[ranked[i + 1]] - optim);
//			double d2 = Math.abs(optim - f[ranked[i]]);
//			if (d1 > d2) {
//				return -1;
//			} else {
//				return 1;
//			}
//
//		} else {
//			if ((optim > f[ranked[i]] && f[ranked[i]] > f[ranked[i + 1]])
//					|| (optim > f[ranked[i + 1]] && f[ranked[i + 1]] > f[ranked[i]])) {
//				return 1;
//			} else {
//				double d1 = Math.abs(optim - f[ranked[i]]);
//				double d2 = Math.abs(optim - f[ranked[i + 1]]);
//				if (d1 < d2) {
//					if (f[ranked[i]] < f[ranked[i + 1]]) {
//						return -1;
//					} else {
//						return 1;
//					}
//				} else {
//					if (f[ranked[i + 1]] < f[ranked[i]]) {
//						return -1;
//					} else {
//						return 1;
//					}
//				}
//			}
//		}
//	}
	
//	public int compare3New(int i,  double optim) {
//		boolean flag = isDominanceNew(i);
//		if (flag == true) {
//			return 1;
//		}
//
//		if (quadratic_loss[i] == 0 && newPhiAll[i] != 0) {
//			double d1 = f[i] - optim;
//			double d2 = Math.abs(optim - newF[i]);
//			if (d1 > d2) {
//				return -1;
//			} else {
//				return 1;
//			}
//		} else if (quadratic_loss[i] != 0 && newPhiAll[i] == 0) {
//			double d1 = Math.abs(f[i]-optim);
//			double d2 = optim - newF[i];
//			if (d1 > d2) {
//				return -1;
//			} else {
//				return 1;
//			}
//
//		} else {
//			if ((optim > f[i] && f[i] > newF[i])
//					|| (optim > newF[i] && newF[i] > f[i])) {
//				return 1;
//			} else {
//				double d1 = Math.abs(optim - f[i]);
//				double d2 = Math.abs(optim - newF[i]);
//				if (d1 < d2) {
//					if (f[i] < newF[i]) {
//						return -1;
//					} else {
//						return 1;
//					}
//				} else {
//					if (newF[i] < f[i]) {
//						return -1;
//					} else {
//						return 1;
//					}
//				}
//			}
//		}
//	}
	
//	public void selection(){
//		int indicator = 0;
//		for(int i = 0; i < lambda; i++){
//			indicator = compare3New(i, optimum);
//			if(indicator == -1){
//				if(f[i] > newF[i]){
//					//choosing the new individual 
//					//and update the x, f, phi, quadratic_loss, eta
//					for(int j = 0; j < variables; j++){
//						x[i][j] = newX[i][j];
//						eta[i][j] = newEta[i][j];
//					}
//					for(int j = 0; j < constrained; j++){
//						phi[i][j] = newPhi[i][j];
//					}
//					f[i] = newF[i];
//					quadratic_loss[i] = newPhiAll[i];
//				}
//			}else{
//				if(quadratic_loss[i] > newPhiAll[i]){
//					//choosing the new individual 
//					//and update the x, f, phi, quadratic_loss, eta
//					for(int j = 0; j < variables; j++){
//						x[i][j] = newX[i][j];
//						eta[i][j] = newEta[i][j];
//					}
//					for(int j = 0; j < constrained; j++){
//						phi[i][j] = newPhi[i][j];
//					}
//					f[i] = newF[i];
//					quadratic_loss[i] = newPhiAll[i];
//				}
//			}
//		}
//	}
	
	public void sortTem(int index, int gen) throws IOException{
		for (int i = 0; i < ranked.length; i++) {
			ranked[i] = i;
		}
		boolean flag = true;
		int tem = 0; 
		optimum = findOptimum(index, gen);
		
		for (int i = 0; i < lambda; i++) {
			flag = false;
			for (int j = 0; j < lambda - 1 - i; j++) {
				if (quadratic_loss[ranked[j]] > quadratic_loss[ranked[j + 1]]) {
					tem = ranked[j];
					ranked[j] = ranked[j + 1];
					ranked[j + 1] = tem;

					flag = true;
				} else if (quadratic_loss[ranked[j]] == quadratic_loss[ranked[j + 1]]) {
					if (f[ranked[j]] > f[ranked[j + 1]]) {
						tem = ranked[j];
						ranked[j] = ranked[j + 1];
						ranked[j + 1] = tem;
						flag = true;
					}
				}
			}

			if (flag == false) {
				break;
			}
		}
	}
	public void sort(int index,int gen) throws IOException {
		for (int i = 0; i < ranked.length; i++) {
			ranked[i] = i;
		}
		boolean flag = true;
		int tem = 0; 
		//double optimumOld;
//		
//		if(optimaFlag == true){
//			if(oldOptimum > optimum){
//				oldOptimum = optimum;
//			}
//		}
	//	double optimumOld = optimum;
	//	boolean optiFlag = optimaFlag;
	    optimum = findOptimum(index, gen);
//	    if(optimum > -1){
//	    System.out.println(optimum+" "+gen);
//	    }
//	    for(int i = 0; i < lambda; i++){
//	    	System.out.println("distance "+(f[i]-optimum));
//	    }
//	    if((optimaFlag == true)){
//	    	if(optimum > oldOptimum){
//	    		optimum = oldOptimum;
//	    	}
//	    }
		// the values in the ranked indicates the relationship
		for (int i = 0; i < lambda; i++) {
			flag = false;
			for (int j = 0; j < lambda - 1 - i; j++) {
				int value = compare3(j, optimum);
				//int value = 1;
				if (value == -1) {
					if (f[ranked[j]] > f[ranked[j + 1]]) {
						tem = ranked[j];
						ranked[j] = ranked[j + 1];
						ranked[j + 1] = tem;

						flag = true;
					} else if (f[ranked[j]] == f[ranked[j + 1]]) {
						if (quadratic_loss[ranked[j]] > quadratic_loss[ranked[j + 1]]) {
							tem = ranked[j];
							ranked[j] = ranked[j + 1];
							ranked[j + 1] = tem;

							flag = true;
						}
					}
				} else {
					if (quadratic_loss[ranked[j]] > quadratic_loss[ranked[j + 1]]) {
						tem = ranked[j];
						ranked[j] = ranked[j + 1];
						ranked[j + 1] = tem;

						flag = true;
					} else if (quadratic_loss[ranked[j]] == quadratic_loss[ranked[j + 1]]) {
						if (f[ranked[j]] > f[ranked[j + 1]]) {
							tem = ranked[j];
							ranked[j] = ranked[j + 1];
							ranked[j + 1] = tem;
							flag = true;
						}
					}
				}
			}

			if (flag == false) {
				break;
			}
		}
	}
	// stochastic sort
	public void sort1() {
		for (int i = 0; i < ranked.length; i++) {
			ranked[i] = i;
		}
		double truepf = 0;
		boolean flag = true;
		int tem = 0;
		// pf = pf+0.0002;
		// the values in the ranked indicates the relationship
		for (int i = 0; i < lambda; i++) {
			flag = false;
			for (int j = 0; j < lambda - 1 - i; j++) {
				truepf = rand.nextDouble();
				if ((quadratic_loss[ranked[j]] == 0 && quadratic_loss[ranked[j + 1]] == 0)
						|| truepf < pf) {
					if (f[ranked[j]] > f[ranked[j + 1]]) {
						tem = ranked[j];
						ranked[j] = ranked[j + 1];
						ranked[j + 1] = tem;

						flag = true;
					}
				} else {
					if (quadratic_loss[ranked[j]] > quadratic_loss[ranked[j + 1]]) {
						tem = ranked[j];
						ranked[j] = ranked[j + 1];
						ranked[j + 1] = tem;

						flag = true;
					}
				}
			}
			if (flag == false) {
				break;
			}
		}
	}
	public void executeInstance(int indexf,double optima) throws IOException{
		double bestValues[] = new double[runtimes];
		double bestGener[] = new double[runtimes];
		double successGener[] = new double[runtimes];
		for (int i = 0; i < runtimes; i++) {
			rand = new Random(System.currentTimeMillis());
			bestValues[i] = es(indexf, i, optima);
			bestGener[i] = Gm;
			successGener[i] = SuccessGm;
		}
		print(indexf, optima);
		printGeneral(bestValues, bestGener, successGener, optima);
	}
	
	public double[] constrainForSingle(int index,double individual[]){
		double temPhi[] = new double[constrained];
		switch(index){
		case 1:{
			temPhi = computeConstrain1(individual);
			break;
		}
		case 2:{
			temPhi  = computeConstrain2(individual);
			break;
		}
		case 3:{
			temPhi = computeConstrain3(individual);
			break;
		}
		case 4:{
			temPhi = computeConstrain4(individual);
			break;
		}
		case 5:{
			temPhi = computeConstrain5(individual);
			break;
		}
		case 6:{
			temPhi = computeConstrain6(individual);
			break;
		}
		case 7:{
			temPhi = computeConstrain7(individual);
			break;
		}
		case 8:{
			temPhi = computeConstrain8(individual);
			break;
		}
		case 9:{
			temPhi = computeConstrain9(individual);
			break;
		}
		case 10:{
			temPhi = computeConstrain10(individual);
			break;
		}
		case 11:{
			temPhi = computeConstrain11(individual);
			break;
		}
		case 12:{
			temPhi = computeConstrain12(individual);
			break;
		}
		case 13:{
			temPhi = computeConstrain13(individual);
			break;
		}
		case 14:{
			temPhi = computeConstrain14(individual);
			break;
		}
		case 15:{
			temPhi = computeConstrain15(individual);
			break;
		}
		case 16:{
			double item[][] = this.computeItem(individual);
			double itemY[] = new double[17];
			double itemC[] = new double[17];
			for(int i = 0; i < item[0].length; i++){
				itemY[i] = item[0][i];
				itemC[i] = item[1][i];
			}
			temPhi = computeConstrain16(individual,itemY,itemC);
			break;
		}
		case 17:{
			temPhi = computeConstrain17(individual);
			break;
		}
		case 18:{
			temPhi = computeConstrain18(individual);
			break;
		}
		case 19:{
			temPhi = computeConstrain19(individual);
			break;
		}
		case 20:{
			temPhi = computeConstrain20(individual);
			break;
		}
		case 21:{
			temPhi = computeConstrain21(individual);
			break;
		}
		case 22:{
			temPhi = computeConstrain22(individual);
			break;
		}
		case 23:{
			temPhi = computeConstrain23(individual);
			break;
		}
		case 24:{
			temPhi = computeConstrain24(individual);
			break;
		}
		case 25:{
			temPhi = computeConstrain25(individual,o25);
			break;
		}
		case 26:{
			temPhi = computeConstrain26(individual,o26);
			break;
		}
		case 27:{
			temPhi = computeConstrain27(individual,o27);
			break;
		}
		case 28:{
			temPhi = computeConstrain28(individual);
			break;
		}
		case 29:{
			temPhi = computeConstrain29(individual);
			break;
		}
		case 30:{
			temPhi = computeConstrain30(individual);
			break;
		}
		}
		return temPhi;
	}
	public double[] constructViolations(int index, double powerArrays[][]){
		double viola[] = new double[runtimes];
		double temPhi[] = new double[constrained];
		double temp = 0;
		for (int i = 0; i < runtimes; i++) {
		    temp = 0;
		    temPhi = this.constrainForSingle(index, powerArrays[i]);
		    for(int j = 0; j < constrained; j++){
				if(temPhi[j] > 0){
					temp+=temPhi[j];
				}
			}
			viola[i] = temp/(double)constrained;
		}
		return viola;
	}
	
	//the first array is the value array of constraint violations
	//the second array can be the objective value of power3,4,5
	//the third array can be the individuals of power3,4,5
	public void sortOfResult(double constrArrays[],double powerF[],double powerIndividuals[][]){
		double tem = 0;
		for(int i = 0; i < constrArrays.length; ++i){
			for(int j = 0; j < constrArrays.length-1-i; j++){
				if(constrArrays[j] > constrArrays[j+1]){
					tem = constrArrays[j];
					constrArrays[j] = constrArrays[j+1];
					constrArrays[j+1] = tem;
					
					tem = powerF[j];
					powerF[j] = powerF[j+1];
					powerF[j+1] = tem;
					for(int m = 0; m < variables; m++){
						tem =powerIndividuals[j][m];
						powerIndividuals[j][m] = powerIndividuals[j+1][m];
						powerIndividuals[j+1][m] = tem;
					}
				}
			}
		}
		
		//number is the number of feasible solutions
		int number = 0;
		for(int i = 0; i < constrArrays.length; i++){
			if(constrArrays[i] == 0){
				number++;
			}else{
				break;
			}
		}
		
		//to sort the feasible solutions according to their objective value;
		for(int i = 0; i < number; ++i){
			for(int j = 0; j < number-1-i; j++){
				if(powerF[j] > powerF[j+1]){
					tem = powerF[j];
					powerF[j] = powerF[j+1];
					powerF[j+1] = tem;
					for(int m = 0; m < variables; m++){
						tem =powerIndividuals[j][m];
						powerIndividuals[j][m] = powerIndividuals[j+1][m];
						powerIndividuals[j+1][m] = tem;
					}
				}
			}	
		}
	}
	
	public double statistical(int index, double powerIndividuals[][],int powerViolations[]){
		int best = 0;
		int median = 0;
		int worst = 0;
		int n1 = 0;
		int n2 = 0;
		int n3 = 0;
		double temPhi[] = new double[constrained];
		
		//the best
		temPhi = constrainForSingle(index, powerIndividuals[0]);
//		System.out.println("best");
//		for(int i = 0; i < constrained; i++){
//			System.out.println(temPhi[i]);
//		}
		for(int i = 0; i < constrained; i++){
			if(temPhi[i] >0){
				best++;
			}
		}
		
		//the median
		double v = 0;
		temPhi = constrainForSingle(index, powerIndividuals[runtimes/2]);
//		System.out.println("median");
//		for(int i = 0; i < constrained; i++){
//			System.out.println(temPhi[i]);
//		}
		for (int i = 0; i < constrained; i++) {
			if (temPhi[i] > 0) {
				median++;
			}
			if (temPhi[i] > 1.0) {
				n1++;
			} else if (temPhi[i] <= 1.0 && temPhi[i] >= 0.01) {
				n2++;
			} else if (temPhi[i] < 0.01 && temPhi[i] >= 0.0001) {
				n3++;
			}
		}
		
		//the worst
		temPhi = this.constrainForSingle(index, powerIndividuals[runtimes-1]);
//		System.out.println("worst");
//		for(int i = 0; i < constrained; i++){
//			System.out.println(temPhi[i]);
//		}
		for(int i = 0; i < constrained; i++){
			if(temPhi[i] > 0){
				worst++;
			}
		}
		
		powerViolations[0] = best;
		powerViolations[1] = median;
		powerViolations[2] = worst;
		powerViolations[3] = n1;
		powerViolations[4] = n2;
		powerViolations[5] = n3;
		return v;
	}
	public void print(int index,double optimal){
		double v = 0;
		double viola[] = constructViolations(index, power3Individual);
		sortOfResult(viola, power3,power3Individual);
		v  = statistical(index, power3Individual,numberOfViolation3);
		print3(v, optimal);
		
		viola = constructViolations(index, power4Individual);
		sortOfResult(viola, power4,power4Individual);
		v  = statistical(index, power4Individual,numberOfViolation4);
		print4(v, optimal);
		
		viola = constructViolations(index, power5Individual);
		sortOfResult(viola, power5,power5Individual);
		v  = statistical(index, power5Individual,numberOfViolation5);
		print5(v, optimal);
		
	}
	
//	public double findMin(double array[]) {
//		double min = Double.MAX_VALUE;
//		for (int i = 0; i < array.length; i++) {
//			if (array[i] < min) {
//				min = array[i];
//			}
//		}
//		return min;
//	}
	public void bubbleSort(double array[]) {
		double tem = 0;
		for (int i = array.length - 1; i > 0; --i) {
			for (int j = 0; j < i; j++) {
				if (array[j] > array[j + 1]) {
					tem = array[j];
					array[j] = array[j + 1];
					array[j + 1] = tem;
				}
			}
		}
	}
//	public double findMedian(double array[]) {
//		bubbleSort(array);
//		return (array[runtimes / 2 - 1] + array[runtimes / 2]) / 2;
//	}
//	
//	public double findMax(double array[]) {
//		double max = array[0];
//		for (int i = 0; i < array.length; i++) {
//			if (array[i] > max) {
//				max = array[i];
//			}
//		}
//		return max;
//	}
	public double findMean(double array[]) {
		double mean = 0;
		for (int i = 0; i < array.length; i++) {
			mean += array[i];
		}
		return mean / array.length;
	}
	
	public double findStd(double array[], double mean) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += Math.pow(array[i] - mean, 2);
		}
		sum = Math.sqrt(sum / array.length);
		return sum;
	}

	public void print3(double v, double known){
		System.out.println("5*10^3");
		System.out.println("Best " + (power3[0] - known)
				+ "(" + numberOfViolation3[0] + ")");
		System.out.println("Median "
				+ (power3[runtimes / 2] - known) + "("
				+ numberOfViolation3[1] + ")");
		System.out.println("Worst "
				+ (power3[runtimes - 1] - known) + "("
				+ numberOfViolation3[2] + ")");
		System.out.println("c " + numberOfViolation3[3] + ","
				+ numberOfViolation3[4] + "," + numberOfViolation3[5]
				+ "");
		System.out.println("v " + v);
		System.out.println("Mean "
				+ (findMean(power3) - known));
		System.out.println("Std "
				+ findStd(power3, findMean(power3)));
	}

	public void print4(double v, double known){
		  System.out.println("5*10^4");
		  System.out.println("Best "+(power4[0]-known)+"("+numberOfViolation4[0]+")");
		  System.out.println("Median "+(power4[runtimes/2]-known)+"("+numberOfViolation4[1]+")");
		  System.out.println("Worst "+(power4[runtimes-1]-known)+"("+numberOfViolation4[2]+")");
		  System.out.println("c "+numberOfViolation4[3]+","+numberOfViolation4[4]+","+numberOfViolation4[5]+"");
		  System.out.println("v "+v);
		  System.out.println("Mean "+(findMean(power4)-known));
		  System.out.println("Std "+findStd(power4, findMean(power4)));
	}
	
	public void print5(double v,double known){
		  System.out.println("5*10^5");
		  System.out.println("Best "+(power5[0]-known)+"("+numberOfViolation5[0]+")");
		  System.out.println("Median "+(power5[runtimes/2]-known)+"("+numberOfViolation5[1]+")");
		  System.out.println("Worst "+(power5[runtimes-1]-known)+"("+numberOfViolation5[2]+")");
		  System.out.println("c "+numberOfViolation5[3]+","+numberOfViolation5[4]+","+numberOfViolation5[5]+"");
		  System.out.println("v "+v);
		  System.out.println("Mean "+(findMean(power5)-known));
		  System.out.println("Std "+findStd(power5, findMean(power5)));
	}
	
	public void printGeneral(double bestValues[],double bestGener[],double successGener[],double known){
		 double success = 0;
		 double generation = 0;
		 double mean = 0;
		
		 ArrayList<Integer> list = new ArrayList<Integer>();
		 for(int i = 0; i < runtimes; i++){
			 if((bestValues[i]-known) < 0.0001){
				 success++;
				 generation+=successGener[i];
				 list.add((int)successGener[i]);
			 }
		 }
		 if (list.size() != 0) {
			double actualSuccess[] = new double[list.size()];
			for (int i = 0; i < actualSuccess.length; i++) {
				actualSuccess[i] = list.get(i) * lambda;
			}
			bubbleSort(actualSuccess);
			mean = findMean(actualSuccess);
			System.out.print("Best " + actualSuccess[0] + " ");
			System.out
					.print("Median " + actualSuccess[(int) success / 2] + " ");
			System.out.print("Worst " + actualSuccess[(int) success - 1] + " ");
			System.out.print("Mean " + mean + " ");
			System.out.print("Std " + findStd(actualSuccess, mean) + " ");
			System.out.print("Feasible Rate = " + feasibleRun
					/ (double) runtimes);
			System.out.print(" Success Rate = " + success / (double) runtimes);
			System.out.println(" Success Performance = "
					+ (generation * lambda / (double) success)
					* (runtimes / (double) success));
		}
		 bubbleSort(bestValues);
		 mean = findMean(bestValues);
		 System.out.print("Min " + bestValues[0] + " ");
		 System.out.print("Median " +bestValues[runtimes/2] + " ");
		 System.out.print("Mean " + mean + " ");
		 System.out.print("Std " + findStd(bestValues, mean) + " ");
		 System.out.print("Max " + bestValues[runtimes-1] + " ");
		 bubbleSort(bestGener);
		 System.out.println("optimal G " + bestGener[runtimes/2] + " ");
//		 System.out.print("Min " + findMin(bestValues) + " ");
//		 System.out.print("Median " + findMedian(bestValues) + " ");
//		 System.out.print("Mean " + mean + " ");
//		 System.out.print("Std " + findStd(bestValues, mean) + " ");
//		 System.out.print("Max " + findMax(bestValues) + " ");
//		 System.out.println("optimal G " + findMedian(bestGener) + " ");
	}
	
	public static void main(String[] args) throws IOException {
		SRES sres = new SRES();
		double varphis[] = {2,2,2,2,2,2,2,2,2,2,2,2,2,40,2,2,2,2,2,2,2,2,1.5,2,2,2,2,2,2,2,2};
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		double optimal[] = { -15.0000000000, -0.8036191041, -1.0005001000,
				-30665.5386717833, 5126.4967140071, -6961.8138755802,
				24.3062090682, -0.0958250414, 680.6300573744, 7049.2480205287,
				0.7499000000, -1.0000000000, 0.0539415140, -47.7648884595,
				961.7150222900, -1.9051552585, 8853.5338748065, -0.8660254038,
				32.6555929502, 0.2049794002, 193.7245100697, 236.4309755040,
				-400.0551000000, -5.5080132716, 13.59085, 0,0,0 };

		double optimal10[] = { -0.747310361526121,-2.277702,0,0,0,0,0};
		double optimal30[] = {-0.821883271394648,-2.169248,0,0,0,0,0,0};
		int indexf = 1;
//		System.out.println("instance 01 result");
//		double luarray1[][] = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 100, 100, 100, 1 } };
//		sres.setParameters(13, 9, varphis[indexf]);
//		sres.setlu(luarray1);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//	
		indexf = 2;
		System.out.println("instance 02 result");
		double luarray2[][] = new double[2][20];
		for (int i = 0; i < luarray2.length; i++) {
			for (int j = 0; j < luarray2[i].length; j++) {
				luarray2[i][j] = i * 10;
			}
		}
		sres.setParameters(20, 2, varphis[indexf]);
		sres.setlu(luarray2);
		sres.executeInstance(indexf, optimal[indexf-1]);

//		indexf = 3;
//		System.out.println("instance 03 result");
//		double luarray3[][] = new double[2][10];
//		for (int i = 0; i < luarray3.length; i++) {
//			for (int j = 0; j < luarray3[i].length; j++) {
//				luarray3[i][j] = i * 1;
//			}
//		}
//		sres.setParameters(10, 1, varphis[indexf]);
//		sres.setlu(luarray3);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 4;
//		System.out.println("instance 04 result");
//		double luarray4[][] = { { 78, 33, 27, 27, 27 }, { 102, 45, 45, 45, 45 } };
//		sres.setParameters(5, 6, varphis[indexf]);
//		sres.setlu(luarray4);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 5;
//		System.out.println("instance 05 result");
//		double luarray5[][] = { { 0, 0, -0.55, -0.55 },
//				{ 1200, 1200, 0.55, 0.55 } };
//		sres.setParameters(4, 5, varphis[indexf]);
//		sres.setlu(luarray5);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 6;
//		System.out.println("instance 06 result");
//		double luarray6[][] = { { 13, 0 }, { 100, 100 } };
//		sres.setParameters(2, 2, varphis[indexf]);
//		sres.setlu(luarray6);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 7;
//		System.out.println("instance 07 result");
//		double luarray7[][] = new double[2][10];
//		for (int i = 0; i < luarray7.length; i++) {
//			for (int j = 0; j < luarray7[i].length; j++) {
//				luarray7[i][j] = Math.pow(-1, i + 1) * 10;
//			}
//		}
//		sres.setParameters(10, 8, varphis[indexf]);
//		sres.setlu(luarray7);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 8;
//		System.out.println("instance 08 result");
//		double luarray8[][] = { { 0, 0 }, { 10, 10 } };
//		sres.setParameters(2, 2, varphis[indexf]);
//		sres.setlu(luarray8);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 9;
//		System.out.println("instance 09 result");
//		double luarray9[][] = new double[2][7];
//		for (int i = 0; i < luarray9.length; i++) {
//			for (int j = 0; j < luarray9[i].length; j++) {
//				luarray9[i][j] = -10 * Math.pow(-1, i);
//			}
//		}
//		sres.setParameters(7, 4, varphis[indexf]);
//		sres.setlu(luarray9);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 10;
//		System.out.println("instance 10 result");
//		double luarray10[][] = { { 100, 1000, 1000, 10, 10, 10, 10, 10 },
//				{ 10000, 10000, 10000, 1000, 1000, 1000, 1000, 1000 } };
//		sres.setParameters(8, 6, varphis[indexf]);
//		sres.setlu(luarray10);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 11;
//		System.out.println("instance 11 result");
//		double luarray11[][] = { { -1, -1 }, { 1, 1 } };
//		sres.setParameters(2, 1, varphis[indexf]);
//		sres.setlu(luarray11);
//		sres.executeInstance(indexf, optimal[indexf-1]);

		indexf = 12;
		// sres.MaxGen = 500;
		System.out.println("instance 12 result");
		double luarray12[][] = { { 0, 0, 0 }, { 10, 10, 10 } };
		sres.setParameters(3, 1, varphis[indexf]);
		sres.setlu(luarray12);
		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 13;
//		System.out.println("instance 13 result");
//		double luarray13[][] = { { -2.3, -2.3, -3.2, -3.2, -3.2 },
//				{ 2.3, 2.3, 3.2, 3.2, 3.2 } };
//		sres.setParameters(5, 3, varphis[indexf]);
//		sres.setlu(luarray13);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//		
//		indexf = 14;
//		System.out.println("instance 14 result");
//		double luarray14[][] = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 } };
//		sres.setParameters(10, 3,varphis[indexf]);
//		sres.setlu(luarray14);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//		
//		indexf = 15;
//		System.out.println("instance 15 result");
//		double luarray15[][] = { { 0, 0, 0 }, { 10, 10, 10 } };
//		sres.setParameters(3, 2,varphis[indexf]);
//		sres.setlu(luarray15);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//		
//		indexf = 16;
//		System.out.println("instance 16 result");
//		double luarray16[][] = { { 704.4148, 68.6, 0, 193, 25 },
//				{ 906.3855, 288.88, 134.75, 287.0966, 84.1988 } };
//		sres.setParameters(5, 38,varphis[indexf]);
//		sres.setlu(luarray16);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//		
//		indexf = 17;
//		System.out.println("instance 17 result");
//		double luarray17[][] = { { 0, 0, 340, 340, -1000, 0 },
//				{ 400, 1000, 420, 420, 1000, 0.5236 } };
//		sres.setParameters(6, 4,varphis[indexf]);
//		sres.setlu(luarray17);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//		
//		indexf = 18;
//		System.out.println("instance 18 result");
//		double luarray18[][] = { { -10, -10, -10, -10, -10, -10, -10, -10, 0 },
//				{ 10, 10, 10, 10, 10, 10, 10, 10, 20 } };
//		sres.setParameters(9, 13,varphis[indexf]);
//		sres.setlu(luarray18);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//		
//		indexf = 19;
//		System.out.println("instance 19 result");
//		double luarray19[][] = {
//				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 } };
//		sres.setParameters(15, 5,varphis[indexf]);
//		sres.setlu(luarray19);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//		
////		indexf = 20;
////		System.out.println("instance 20 result");
////		double luarray20[][] = {
////				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
////						0, 0, 0, 0 },
////				{ 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
////						10, 10, 10, 10, 10, 10, 10, 10, 10 } };
////		sres.setParameters(24, 20,varphis[indexf]);
////		sres.setlu(luarray20);
////		sres.executeInstance(indexf, optimal[indexf-1]);
//		
//		indexf = 21;
//		System.out.println("instance 21 result");
//		double luarray21[][] = { { 0, 0, 0, 100, 6.3, 5.9, 4.5 },
//				{ 1000, 40, 40, 300, 6.7, 6.4, 6.25 } };
//		sres.setParameters(7, 6,varphis[indexf]);
//		sres.setlu(luarray21);
//		sres.executeInstance(indexf, optimal[indexf-1]);		
//	
//		indexf = 22;
//		System.out.println("instance 22 result");
//		double luarray22[][] = {
//				{ 0, 0, 0, 0, 0, 0, 0, 100, 100, 100.01, 100, 100, 0, 0, 0,
//						0.01, 0.01, -4.7, -4.7, -4.7, -4.7, -4.7 },
//				{ 20000, 1 * Math.pow(10, 6), 1 * Math.pow(10, 6),
//						1 * Math.pow(10, 6), 4 * Math.pow(10, 7),
//						4 * Math.pow(10, 7), 4 * Math.pow(10, 7), 299.99,
//						399.99, 300, 400, 600, 500, 500, 500, 300, 400, 6.25,
//						6.25, 6.25, 6.25, 6.25 } };
//		sres.setParameters(22, 20,varphis[indexf]);
//		sres.setlu(luarray22);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 23;
//		System.out.println("instance 23 result");
//		double luarray23[][] = { { 0, 0, 0, 0, 0, 0, 0, 0, 0.01 },
//				{ 300, 300, 100, 200, 100, 300, 100, 200, 0.03 } };
//		sres.setParameters(9, 6,varphis[indexf]);
//		sres.setlu(luarray23);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//
//		indexf = 24;
//		System.out.println("instance 24 result");
//		double luarray24[][] = { { 0, 0 }, { 3, 4 } };
//		sres.setParameters(2, 2,varphis[indexf]);
//		sres.setlu(luarray24);
//		sres.executeInstance(indexf, optimal[indexf-1]);
//			
		indexf = 25;
		System.out.println("instance 25 result");
		sres.setParameters(10, 2,varphis[indexf]);
		double luarray25[][] = new double[2][sres.variables];
		for(int i = 0; i < sres.variables; i++){
			luarray25[0][i] = 0;
			luarray25[1][i] = 10;
		}
		sres.setlu(luarray25);
		sres.executeInstance(indexf, optimal10[indexf-25]);
		
//		indexf = 26;
//		System.out.println("instance 26 result");
//		sres.setParameters(30, 3,varphis[indexf]);
//		double luarray26[][] = new double[2][sres.variables];
//		for(int i = 0; i < sres.variables; i++){
//			luarray26[0][i] = -5.12;
//			luarray26[1][i] = 5.12;
//		}
//		sres.setlu(luarray26);
//		sres.executeInstance(indexf, optimal30[indexf-25]);
//		
//		indexf = 27;
//		System.out.println("instance 27 result");
//		sres.setParameters(10,1,varphis[indexf]);
//		double luarray27[][] = new double[2][sres.variables];
//		for(int i = 0; i < sres.variables; i++){
//			luarray27[0][i] = -1000;
//			luarray27[1][i] = 1000;
//		}
//		sres.setlu(luarray27);
//		sres.executeInstance(indexf, optimal10[indexf-25]);
		
//		indexf = 28;
//		System.out.println("instance 28 result");
//		sres.setParameters(2,2,varphis[indexf]);
//		double luarray28[][] = {{-2,-1},{2,3}};
//		sres.setlu(luarray28);
//		sres.executeInstance(indexf, optimal30[indexf-25]);
		
//		indexf = 29;
//		System.out.println("instance 29 result");
//		sres.setParameters(7,9,varphis[indexf]);
//		double luarray29[][] = {{0,0,0,0,0,0,0},{2,2,2,1,1,1,1}};
//		sres.setlu(luarray29);
//		sres.executeInstance(indexf, optimal30[indexf-25]);
//		
//		indexf = 30;
//		System.out.println("instance 30 result");
//		sres.setParameters(5,6,varphis[indexf]);
//		double luarray30[][] = {{78,33,27,27,27},{102,45,45,45,45}};
//		sres.setlu(luarray30);
//		sres.executeInstance(indexf, optimal30[indexf-25]);
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
	}
}