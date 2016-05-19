	
	
	public void sort(int index,int gen,double optima,int runNumber) throws IOException {
		for (int i = 0; i < ranked.length; i++) {
			ranked[i] = i;
		}
		boolean flag = true;
		int tem = 0; 
	
	    optimum = findOptimum(index, gen); // the definition of findOptimum can be found in this file as well

		// the values in the ranked indicates the relationship
		for (int i = 0; i < lambda; i++) {
			flag = false;
			for (int j = 0; j < lambda - 1 - i; j++) {
				int value = compare3(j, optimum);
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


	public double findOptimum(int index,int gen) throws IOException {
		if(this.isContain(index)){
			probability[index] = 0.2*(1+5*Math.pow((gen/(double)MaxGen),0.5));
		}

		double optim = Double.MAX_VALUE;

		// to record the minimum objective value of feasible individuals
		if (feasible.size() != 0) {
			for (int m = 0; m < feasible.size(); m++) {
				if (optim > f[feasible.get(m)]) {
					optim = f[feasible.get(m)];
				}
			}
		}

		double unoptim = Double.MAX_VALUE; //the objective value of infeasible individual with the smallest violation degree
		double qua = Double.MAX_VALUE; //the violation degree of the individual with the smallest violation degree
		double qua2 = Double.MAX_VALUE; //the smallest objectvie value of the infeasible individual, without considering its violation degree

		for (int m = 0; m < this.lambda; m++) {

			// to find the infeasible individual with the smallest violation degree and record its corresponding objective values
			if (quadratic_loss[m] != 0 && qua > quadratic_loss[m]) {
				qua = quadratic_loss[m];
				unoptim = f[m];
			}

			//to find the smallest objective value of infeasible individual, without considering its violation degree 
			if (quadratic_loss[m] != 0 && f[m] < qua2) {
				qua2 = f[m];
			}
		}
 
        // if there is no feasible individual in the present population
		if (optim == Double.MAX_VALUE) {
			optim = unoptim;
		} else {
			if (optim > unoptim) {
				optim = (unoptim + optim) / 2;
			}
		}
		// if there are all feasible individuals, 
		if (qua2 == Double.MAX_VALUE) {
		//	optimaFlag = true;
			return optim;
		} else {
			double tem = rand.nextDouble();
			if (tem < this.probability[index]) {
			//	optimaFlag = true;
				return optim;
			} else {
				return qua2;
			}
		}
	}


    // if the ith individual is superior to the i+1 th individual, the returned value is 1. Otherwise it is -1, which denotes the i+1 th individual is better than the ith individual
	public int compare3(int i,  double optim) {
		boolean flag = isDominance(ranked[i], ranked[i + 1]);
		if (flag == true) {
			return 1;
		}

		if (quadratic_loss[ranked[i]] == 0
				&& quadratic_loss[ranked[i + 1]] != 0) {

			double d1 = f[ranked[i]] - optim;
			double d2 = Math.abs(optim - f[ranked[i + 1]]);
			if (d1 > d2) {
				return -1;
			} else {
				return 1;
			}

		} else if (quadratic_loss[ranked[i]] != 0
				&& quadratic_loss[ranked[i + 1]] == 0) {

			double d1 = f[ranked[i + 1]] - optim;
			double d2 = Math.abs(optim - f[ranked[i]]);
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
				double d1 = Math.abs(optim - f[ranked[i]]);
				double d2 = Math.abs(optim - f[ranked[i + 1]]);
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



// to check if the adjacent individuals having pareto dominance relationship
	public boolean isDominance(int i, int j) {
		if ((f[i] <= f[j] && quadratic_loss[i] <= quadratic_loss[i])
				|| (f[i] >= f[j] && quadratic_loss[i] >= quadratic_loss[j])) {
			return true;
		}
		return false;
	}