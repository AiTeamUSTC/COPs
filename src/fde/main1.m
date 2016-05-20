clc; 
clear all; 

tic; 

format long; 
format compact; 

mu = 30 

% Select which problem to be tested
%1 Welded beam design
%2 Tession compression spring design
%3 Speed reducer design
%4 Three-bar truss design
%5 Himmelblau¡¯s Nonlinear Optimization Problem
problemSet = [1 2 3 4 5]; 
optimal=[2.38095658 0.012665233 2994.471066 263.8958434 -31025.56024];

maxFES = 50000;
%to denote whether record the results during evolution into file
fileFlag = 0;
strings=['B', 'C', 'D', 'E', 'F', 'G' 'B', 'C', 'D', 'E', 'F', 'G' 'B', 'C', 'D', 'E', 'F', 'G' 'B', 'C', 'D', 'E', 'F', 'G'];
% strings=['B', 'C', 'D', 'E', 'F', 'G' 'H', 'I', 'J', 'K', 'L', 'M' 'N', 'O', 'P', 'Q', 'R', 'S' 'T', 'U', 'V', 'W', 'X', 'Y'];
theta = 0.0001; % this value is used to transform equality constraints to nonequality constraints
sheets = [];
file1 = [];
for problemIndex = 1 : 5
    problem = problemSet(problemIndex)
    popsize = 30
  
    switch problem

        case 1

            % lu: define the upper and lower bounds of the variables
            lu = [0.125 0.1 0.1 0.1; 10.0 10.0 10 10.0]; 
            % n: define the dimension of the problem
            n = 4; aaa = []; 

        case 2

            lu = [0.25 0.05 2.0; 1.3 2.0 15.0]; 
            n = 3; aaa = []; 

        case 3

            lu = [2.6 0.7 17 7.3 7.3 2.9 5.0; 3.6 0.8 28 8.3 8.3 3.9 5.5]; 
            n = 7; aaa = []; 

        case 4

            lu = [0 0; 1 1]; 
            n = 2; aaa = []; 

%         case 5
% 
%             lu = [0.0625 0.0625 10.0 10.0; 99*0.0625 99*0.0625 240 240];
%             n = 4; aaa = [];
%         case 6
% 
%             lu = [0.0625 0.0625 10.0 10.0; 99*0.0625 99*0.0625 240 240];
%             n = 4; aaa = [];

        case 5

            lu = [78 33 27 27 27; 102 45 45 45 45]; 
            n = 5; aaa = [];
    end

    % Record the best results
    bestResults  = []; 
    % Record the feasibility proportion of the population
    feasiPro = []; 

    %Main body
    if fileFlag == 1
        strBasic = 'C:\Users\Alice\Desktop\middle result\history\history';
%         if mod(problem, 6) == 1     
            instances = num2str(problem);
            file1 = strcat(strBasic, instances, '.xls');
            filehistory = fopen(file1, 'a');
            fclose(filehistory);
%         end
        
        strBasic = 'C:\Users\Alice\Desktop\middle result\performance\solution';
        instances = num2str(problem);
        strBasic = strcat(strBasic, instances, '.txt');
%         fileperformance = fopen(file1, 'w+t');
        filesolution = fopen(strBasic, 'a');
        
        strEvolution = 'C:\Users\Alice\Desktop\middle result\evolution\evolution';
        strEvolution = strcat(strEvolution, instances, '.txt');
        fileevolution = fopen(strEvolution, 'a');
    end
    time = 1; 
    totalTime=30;
    bestFES1=[]; %to record the FES to reach success condition for 25 run
    success=0;
   
    numberMin = 1;
 %   percent = 0.1;
  %  numberMax = 20;
  %  numberMax = floor(percent*popsize);
    numberMax = 5;
    alphabet = (numberMin:numberMax); 
    overall = numberMax*(numberMin+numberMax)/2;
    prob = (numberMax/overall :-1/overall : numberMin/overall);
    prob1 = (numberMax/overall :0 : numberMin/overall);
   
    best = [];
    firsthistory=[];
    secondhistory=[];
    thirdhistory=[];
    feasibleRun = totalTime;
    while time <= totalTime
        bestValue = 1E10;
        firsthistory1=[];
        secondhistory1=[];
        thirdhistory1=[];
        time
        rand('seed', sum(100 * clock)); 

        FES = 0;
        P = ones(popsize, 1) * lu(1, :) + rand(popsize, n) .* (ones(popsize, 1) * (lu(2, :) - lu(1, :))); 
        fit = fitness(P, problem, aaa, theta); 
        FES=FES+popsize;
    
       % Find the best individual in P
       %     Compute the degree of constraint violations of P
       vioSum = sum(fit(:, 2 : size(fit, 2)), 2);
       %Sort all the solutions in P in ascending order by their degree of constraint violations
       [vioVal, vioIndex] = sort(vioSum);
       temP = P(vioIndex, :);
       temfitP = fit(vioIndex, :);
       % Sort the feasible solutions in D in ascending order by their objective function values
       feasiIndex  =  find(vioVal  ==  0);
       if ~isempty(feasiIndex)
           [feasiObjVal, feasiObjIndex]  =  sort(temfitP(feasiIndex, 1));
            temP(feasiIndex, :)  =  temP(feasiObjIndex, :);
            temfitP(feasiIndex, :)  =  temfitP(feasiObjIndex, :);
       end
    
       rank=floor(0.10*popsize);
       initEpsilon=vioVal(rank);
%     rank=0.20*popsize;
       epsilon=initEpsilon;
       epsilon = 0;
       endFES=floor(0.10*maxFES);
       cp = (-5-log(epsilon))/log(0.05);
       cpmin = 3;
       if cp < cpmin
           cp = cpmin;
       end
       cp;
       bestIndividual=temP(1:popsize, :);
       fitBestIndividual=temfitP(1:popsize, :);
  
       P = temP(1:popsize, :);
       fit = temfitP(1:popsize, :);
    
       bestFES = 0; %FES to reach success condition
       flag=0; %to set to 1 once reaching to success condition
       number = randsrc(10000,1,[alphabet; prob]) ;

        while FES <= maxFES

%             % Randomly select mu individuals from P that constitute the set
%             randSel = floor(rand(1, mu) * popsize) + 1;  
%               a = randperm(popsize);
%               randSel = a(1:mu);
              randSel = (1:1:mu);
              Q = P(randSel, :); 
              fitQ = fit(randSel, :); 
              
              vioSum = sum(fit(:, 2 : size(fit, 2)), 2);
%             vioSum = vioSum - epsilon;
              vioSum(vioSum < 0) = 0;
              %Sort all the solutions in P in ascending order by their degree of constraint violations
              [vioVal, vioIndex] = sort(vioSum);
              % Sort the feasible solutions in D in ascending order by their objective function values
              feasiIndex  =  find(vioVal  ==  0);
              proportion = size(feasiIndex)/popsize;
              
              if rand < 1%1*(1-proportion)
                  %     Compute the degree of constraint violations of P
                  vioSum = sum(fit(:, 2 : size(fit, 2)), 2);
                  %Sort all the solutions in P in ascending order by their degree of constraint violations
                  [vioVal, vioIndex] = sort(vioSum);
                  temP = P(vioIndex, :);
                  temfitP = fit(vioIndex, :);
                  % Sort the feasible solutions in D in ascending order by their objective function values
                  feasiIndex  =  find(vioVal  ==  0);
                  
                  if ~isempty(feasiIndex)
                      [feasiObjVal, feasiObjIndex]  =  sort(temfitP(feasiIndex, 1));
                      temP(feasiIndex, :)  =  temP(feasiObjIndex, :);
                      temfitP(feasiIndex, :)  =  temfitP(feasiObjIndex, :);
                  end
              else
                  temP = P;
                  temfitP = fit;  
                  [~, objIndex] = sort(temfitP(:, 1));
                  temP = temP(objIndex, :);
                  temfitP = temfitP(objIndex, :);
              end
              bestIndividual=temP(1:popsize, :);
              fitbestIndividual=temfitP(1:popsize, :);
             
              C = DE1(P,  Q, 1, mu, popsize, lu, n, bestIndividual, number);
              [fitC] = fitness(C, problem, aaa, theta);
              FES = FES+mu;
              
%               [P, fit]=models(P, fit, Q, fitQ, C, fitC, randSel, mu, 0);
              [P, fit]=models(P, fit, Q, fitQ, C, fitC, randSel, mu, 0);
              
              % Compute the degree of constraint violations of P
              vioSumP = sum(fit(:, 2 : size(fit, 2)), 2);
              % Find the best individual in P
              feasiIndex = find(vioSumP == 0); %feasible individual index
              if ~isempty(feasiIndex)
                  [minFeasiVal, minFeasiIndex] = min(fit(feasiIndex, 1)); %minFeasiIndex is the index in feasible not whole population
                  if ~isempty(feasiIndex)
                      [minFeasiVal, minFeasiIndex] = min(fit(feasiIndex, 1));
                      if minFeasiVal < bestValue 
                          bestValue = minFeasiVal;
                          bestFES = FES;
                          bestIndex = feasiIndex(minFeasiIndex);
                      end
                  end
                  else
                      [bestVal, bestIndex] = min(vioSumP);
              end
              
              if time==totalTime/2 && fileFlag == 1
                  %Compute the degree of constraint violations of P
                  vioSum = sum(fit(:, 2 : size(fit, 2)), 2);
                  %Sort all the solutions in P in ascending order by their degree of constraint violations
                  [vioVal, vioIndex] = sort(vioSum);
                  temP = P(vioIndex, :);
                  temfitP = fit(vioIndex, :);
                  % Sort the feasible solutions in D in ascending order by their objective function values
                  feasiIndex  =  find(vioVal  ==  0);
                  
                  if ~isempty(feasiIndex)
                      [feasiObjVal, feasiObjIndex]  =  sort(temfitP(feasiIndex, 1));
                      temP(feasiIndex, :)  =  temP(feasiObjIndex, :);
                      temfitP(feasiIndex, :)  =  temfitP(feasiObjIndex, :);
                  end
                  writeIndividual = temfitP(1, :);
                  if fileFlag == 1 && writeIndividual(1, 1)> optimal(problemIndex)
                      fprintf(fileevolution, '%f\t%f\t%d\n', writeIndividual(1, 1), sum(writeIndividual(:, 2 : size(writeIndividual, 2)), 2), FES);
                  end
              end
             
              %to record the history information for general performance
              if fileFlag == 1 && (FES-mu)<1.0E4 && FES>=1.0E4
                  %     Compute the degree of constraint violations of P
                  vioSum = sum(fit(:, 2 : size(fit, 2)), 2);
                  %Sort all the solutions in P in ascending order by their degree of constraint violations
                  [vioVal, vioIndex] = sort(vioSum);
                  temP = P(vioIndex, :);
                  temfitP = fit(vioIndex, :);
                  % Sort the feasible solutions in P in ascending order by their objective function values
                  feasiIndex  =  find(vioVal  ==  0);
                  if ~isempty(feasiIndex)
                      [feasiObjVal, feasiObjIndex]  =  sort(temfitP(feasiIndex, 1));
                      temP(feasiIndex, :)  =  temP(feasiObjIndex, :);
                      temfitP(feasiIndex, :)  =  temfitP(feasiObjIndex, :);
                  end
                  firsthistory1=temfitP(1, :);
              elseif fileFlag == 1 && (FES-mu)<2.5E4 && FES>=2.5E4
                  %     Compute the degree of constraint violations of P
                  vioSum = sum(fit(:, 2 : size(fit, 2)), 2);
                  %Sort all the solutions in P in ascending order by their degree of constraint violations
                  [vioVal, vioIndex] = sort(vioSum);
                  temP = P(vioIndex, :);
                  temfitP = fit(vioIndex, :);
                  % Sort the feasible solutions in P in ascending order by their objective function values
                  feasiIndex  =  find(vioVal  ==  0);
                  if ~isempty(feasiIndex)
                      [feasiObjVal, feasiObjIndex]  =  sort(temfitP(feasiIndex, 1));
                      temP(feasiIndex, :)  =  temP(feasiObjIndex, :);
                      temfitP(feasiIndex, :)  =  temfitP(feasiObjIndex, :);
                  end
                  secondhistory1=temfitP(1, :);
              elseif fileFlag == 1 && (FES-mu)<5.0E4 && FES>=5.0E4
                  %     Compute the degree of constraint violations of P
                  vioSum = sum(fit(:, 2 : size(fit, 2)), 2);
                  %Sort all the solutions in P in ascending order by their degree of constraint violations
                  [vioVal, vioIndex] = sort(vioSum);
                  temP = P(vioIndex, :);
                  temfitP = fit(vioIndex, :);
                  % Sort the feasible solutions in P in ascending order by their objective function values
                  feasiIndex  =  find(vioVal  ==  0);
                  if ~isempty(feasiIndex)
                      [feasiObjVal, feasiObjIndex]  =  sort(temfitP(feasiIndex, 1));
                      temP(feasiIndex, :)  =  temP(feasiObjIndex, :);
                      temfitP(feasiIndex, :)  =  temfitP(feasiObjIndex, :);
                  end
                  thirdhistory1=temfitP(1, :);
                  if size(feasiIndex, 2)==0
                      feasibleRun = feasibleRun-1;
                  end
              end
        end

        % Compute the degree of constraint violations of P
        vioSumP = sum(fit(:, 2 : size(fit, 2)), 2);
        % Record the best results and the feasibility proportion
        feasiIndex = find(vioSumP == 0); 
        if ~isempty(feasiIndex)
            [value, index] = min(fit(feasiIndex, 1));
            bestResults = [bestResults value]; 
            best = [best; P(feasiIndex(index), :)];
            feasiPro = [feasiPro length(feasiIndex) / popsize]; 
            bestFES1=[bestFES1, bestFES];       
            firsthistory=[firsthistory; firsthistory1];
            secondhistory=[secondhistory; secondhistory1];
            thirdhistory=[thirdhistory; thirdhistory1];
        else
            [minVioValP, minVioIndexP] = min(vioSumP); 
            bestResults = [bestResults fit(minVioIndexP, 1)]; 
            feasiPro = [feasiPro 0]; 
            bestFES1=[bestFES1, 0];
        end
        time = time + 1;

    end
    success
    % Show the best results and the feasibility proportion
    [bestResults, index] = sort(bestResults);
    best(:, :)=best(index, :);
    bestResults
    best;
%     bestResults %= sort(bestResults)
    Mean = mean(bestResults)
    Std = std(bestResults)
%     best
    success
    bestFES1
    mean(bestFES1)
   
   %to record the best solution and its corresponding objective value
   if fileFlag == 1 && problem == 1
       fprintf(filesolution,'%.10f\t%.10f\t%.10f\t%.10f\t%.10f\n',best(1, 1), best(1, 2), best(1, 3), best(1, 4), bestResults(1, 1));
   elseif fileFlag == 1 && problem == 2
       fprintf(filesolution,'%.10f\t%.10f\t%.10f\t%.10f\n',best(1, 1), best(1, 2), best(1, 3), bestResults(1, 1));
   elseif fileFlag == 1 && problem == 3
       fprintf(filesolution,'%.7f\t%.7f\t%.7f\t%.7f\t%.7f\t%.7f\t%.7f\t%.7f\n',best(1, 1), best(1, 2), best(1, 3), best(1, 4), best(1, 5), best(1, 6), best(1, 7), bestResults(1, 1));
   elseif fileFlag == 1 && problem == 4
       fprintf(filesolution,'%.10f\t%.10f\t%.10f\n',best(1, 1), best(1, 2), bestResults(1, 1));
   elseif fileFlag == 1 && problem == 5
       fprintf(filesolution,'%.8f\t%.8f\t%.8f\t%.8f\t%.8f\t%.8f\n',best(1, 1), best(1, 2), best(1, 3), best(1, 4), best(1, 5), bestResults(1, 1));
   end
    %it is convenient to transfer to latex table data
    if fileFlag ==1 && mod(problem, 6) == 1 
        A = [{'&Best'} {'&Median'} {'&Worst'} {'&c'} {'&v'} {'&Mean'} {'&Std'}];
        A=[A A A];
%         R = cell(1, 21);
%         R{1, 1:length(A)} = A;
        sheets = strcat('history', num2str(problem));
        xlswrite(file1, A', sheets,'A1');
%        fprintf(fileperhistory, '%d\t%d\t%d\t%f\t%f\t%s\t%s\t%d\n', bestFES1(1), bestFES1(floor(totalTime/2)+1), bestFES1(totalTime), mean(bestFES1), std(bestFES1), feasibleRate, successRate, floor(mean(bestFES1)*totalTime/success));
    end
    count = 1;
    
    if fileFlag == 1
        %to record the first history evolution
        %     Compute the degree of constraint violations of P
        vioSum1 = sum(firsthistory(:, 2 : size(firsthistory, 2)), 2);
        %Sort all the solutions in P in ascending order by their degree of constraint violations
        [vioVal, vioIndex] = sort(vioSum1);
        firsthistory=firsthistory(vioIndex, :);
        % Sort the feasible solutions in D in ascending order by their objective function values
        feasiIndex  =  find(vioVal  ==  0);
        if ~isempty(feasiIndex)
            [feasiObjVal, feasiObjIndex]  =  sort(firsthistory(feasiIndex, 1));
            firsthistory(feasiIndex, :) = firsthistory(feasiObjIndex, :);
        end
        best = firsthistory(1, 1)-optimal(problem);
        t1 = firsthistory(1, 2:size(firsthistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', best);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        best = strcat('&', a, '(', num2str(size(t2, 2)),')');
        best={best};
        xlswrite(file1, best, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        middle = firsthistory(floor(totalTime/2)+1, 1)-optimal(problem);
        t1 = firsthistory(floor(totalTime/2)+1, 2:size(firsthistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', middle);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        middle = strcat('&', a, '(', num2str(size(t2, 2)),')');
        middle={middle};
        xlswrite(file1, middle, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        worst = firsthistory(totalTime)-optimal(problem);
        t1 = firsthistory(totalTime, 2:size(firsthistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', worst);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        worst = strcat('&', a, '(', num2str(size(t2, 2)),')');
        worst={worst};
        xlswrite(file1, worst, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        t1 = firsthistory(floor(totalTime/2)+1, 2:size(firsthistory, 2));
        t2 = t1(t1>1.0);
        t3 = t1(t1>0.01);
        t4 = t1(t1>0.0001);
        c = strcat('&', num2str(size(t2, 2)), ',', num2str(size(t3, 2)),',', num2str(size(t4, 2)));
        c={c};
        xlswrite(file1, c, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        v = mean(sum(t1, 2));
        a=sprintf('%.4E', v);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        v = strcat('&', a);
        v={v};
        xlswrite(file1, v, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        mean1 = mean(firsthistory(:, 1))-optimal(problem);
        a=sprintf('%.4E', mean1);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        mean1 = strcat('&', a);
        mean1 = {mean1};
        xlswrite(file1, mean1, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        std1 = std(firsthistory(:, 1));
        a=sprintf('%.4E', std1);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        std1 = strcat('&', a);
        std1 = {std1};
        xlswrite(file1, std1, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
    end
    
    if fileFlag == 1
        %to record the second history evolution
        %Compute the degree of constraint violations of P
        vioSum2 = sum(secondhistory(:, 2 : size(secondhistory, 2)), 2);
        %Sort all the solutions in P in ascending order by their degree of constraint violations
        [vioVal, vioIndex] = sort(vioSum2);
        secondhistory=secondhistory(vioIndex, :);
        % Sort the feasible solutions in D in ascending order by their objective function values
        feasiIndex  =  find(vioVal  ==  0);
        if ~isempty(feasiIndex)
            [feasiObjVal, feasiObjIndex]  =  sort(secondhistory(feasiIndex, 1));
            secondhistory(feasiIndex, :) = secondhistory(feasiObjIndex, :);
        end
        best = secondhistory(1, 1)-optimal(problem);
        t1 = secondhistory(1, 2:size(secondhistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', best);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        best = strcat('&', a, '(', num2str(size(t2, 2)),')');
        best={best};
        xlswrite(file1, best, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        middle = secondhistory(floor(totalTime/2)+1, 1)-optimal(problem);
        t1 = secondhistory(floor(totalTime/2)+1, 2:size(secondhistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', middle);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        middle = strcat('&', a, '(', num2str(size(t2, 2)),')');
        middle={middle};
        xlswrite(file1, middle, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        worst = secondhistory(totalTime)-optimal(problem);
        t1 = firsthistory(totalTime, 2:size(secondhistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', worst);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        worst = strcat('&', a, '(', num2str(size(t2, 2)),')');
        worst={worst};
        xlswrite(file1, worst, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        t1 = secondhistory(floor(totalTime/2)+1, 2:size(secondhistory, 2));
        t2 = t1(t1>1.0);
        t3 = t1(t1>0.01);
        t4 = t1(t1>0.0001);
        c = strcat('&', num2str(size(t2, 2)), ',', num2str(size(t3, 2)),',', num2str(size(t4, 2)));
        c={c};
        xlswrite(file1, c, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        v = mean(sum(t1, 2));
        a=sprintf('%.4E', v);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        v = strcat('&', a);
        v={v};
        xlswrite(file1, v, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        mean1 = mean(secondhistory(:, 1))-optimal(problem);
        a=sprintf('%.4E', mean1);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        mean1 = strcat('&', a);
        mean1 = {mean1};
        xlswrite(file1, mean1, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        std1 = std(secondhistory(:, 1));
        a=sprintf('%.4E', std1);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        std1 = strcat('&', a);
        std1 = {std1};
        xlswrite(file1, std1, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
    end
    
    if fileFlag == 1
        %to record the third history evolution
        %Compute the degree of constraint violations of P
        vioSum3 = sum(thirdhistory(:, 2 : size(thirdhistory, 2)), 2);
        %Sort all the solutions in P in ascending order by their degree of constraint violations
        [vioVal, vioIndex] = sort(vioSum3);
        thirdhistory=thirdhistory(vioIndex, :);
        % Sort the feasible solutions in D in ascending order by their objective function values
        feasiIndex  =  find(vioVal  ==  0);
        if ~isempty(feasiIndex)
            [feasiObjVal, feasiObjIndex]  =  sort(thirdhistory(feasiIndex, 1));
            thirdhistory(feasiIndex, :) = thirdhistory(feasiObjIndex, :);
        end
        best = thirdhistory(1, 1)-optimal(problem);
        t1 = thirdhistory(1, 2:size(thirdhistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', best);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        best = strcat('&', a, '(', num2str(size(t2, 2)),')');
        best={best};
        xlswrite(file1, best, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        middle = thirdhistory(floor(totalTime/2)+1, 1)-optimal(problem);
        t1 = thirdhistory(floor(totalTime/2)+1, 2:size(thirdhistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', middle);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        middle = strcat('&', a, '(', num2str(size(t2, 2)),')');
        middle={middle};
        xlswrite(file1, middle, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        worst = thirdhistory(totalTime)-optimal(problem);
        t1 = thirdhistory(totalTime, 2:size(thirdhistory, 2));
        t2 = t1(t1>0);
        a=sprintf('%.4E', worst);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        worst = strcat('&', a, '(', num2str(size(t2, 2)),')');
        worst={worst};
        xlswrite(file1, worst, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        t1 = thirdhistory(floor(totalTime/2)+1, 2:size(thirdhistory, 2));
        t2 = t1(t1>1.0);
        t3 = t1(t1>0.01);
        t4 = t1(t1>0.0001);
        c = strcat('&', num2str(size(t2, 2)), ',', num2str(size(t3, 2)),',', num2str(size(t4, 2)));
        c={c};
        xlswrite(file1, c, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        v = mean(sum(t1, 2));
        a=sprintf('%.4E', v);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        v = strcat('&', a);
        v={v};
        xlswrite(file1, v, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        mean1 = mean(thirdhistory(:, 1))-optimal(problem);
        a=sprintf('%.4E', mean1);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        mean1 = strcat('&', a);
        mean1 = {mean1};
        xlswrite(file1, mean1, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
        
        std1 = std(thirdhistory(:, 1));
        a=sprintf('%.4E', std1);
        a = strrep(a, 'E+0','E+');
        a = strrep(a, 'E-0','E-');
        std1 = strcat('&', a);
        std1 = {std1};
        xlswrite(file1, std1, sheets, strcat(strings(problem), num2str(count)));
        count = count+1;
    end
    
%     if fileFlag ==1 && mod(problem, 6) == 1
%         A = [{'\\ \cline{2-8}'} {'\\ \cline{2-8}'} {'\\ \cline{2-8}'} {'\\ \cline{2-8}'} {'\\ \cline{2-8}'} {'\\ \cline{2-8}'} {'\\ \hline'}];
%         A=[A A A];
%         xlswrite(file1, A', sheets,'H1');
%     end
    %to record the success performance
    bestFES1=sort(bestFES1);
%     percentFeasible = feasibleRun/totalTime;
%     feasibleRate = strcat(num2str(percentFeasible*100),'\%');
%     percentSuccess = success/totalTime;
%     successRate = strcat(num2str(percentSuccess*100),'\%');
    
    if fileFlag == 1
%         fclose(filehistory);
        fclose(filesolution);
        fclose(fileevolution);
    end
    %feasiPro
    %mean(bestFES1)
end

toc; 

% profview
