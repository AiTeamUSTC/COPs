function [P, fit] = models1(P, fit, Q, fitQ, C, fitC, randSel, mu, epsilon)

    % Compute the degree of constraint violations of D
    vioSumQ = sum(fitQ(:, 2 : size(fitQ, 2)), 2);
    vioSumC = sum(fitC(:, 2 : size(fitC, 2)), 2);

    offspring=[];
    fitOffspring=[];
    for i = 1:mu
        if vioSumQ(i)<=epsilon & vioSumC(i)<=epsilon 
            if fitQ(i,1)>fitC(i,1)
                offspring=[offspring; C(i,:)];
                fitOffspring=[fitOffspring; fitC(i,:)];
            else
                offspring=[offspring; Q(i,:)];
                fitOffspring=[fitOffspring; fitQ(i,:)];
            end
%         else
%                 if vioSumQ(i)>vioSumC(i)
%                     offspring=[offspring; C(i,:)];
%                     fitOffspring=[fitOffspring; fitC(i,:)];
%                 else
%                     offspring=[offspring; Q(i,:)];
%                     fitOffspring=[fitOffspring; fitQ(i,:)];
%                 end
        elseif vioSumQ(i)<=epsilon & vioSumC(i)>epsilon
                offspring=[offspring; Q(i,:)];
                fitOffspring=[fitOffspring; fitQ(i,:)];
        elseif vioSumQ(i)>epsilon & vioSumC(i)<=epsilon
                offspring=[offspring; C(i,:)];
                fitOffspring=[fitOffspring; fitC(i,:)];
        elseif vioSumQ(i)>epsilon & vioSumC(i)>epsilon
                if vioSumQ(i)>vioSumC(i)
                    offspring=[offspring; C(i,:)];
                    fitOffspring=[fitOffspring; fitC(i,:)];
                else
                    offspring=[offspring; Q(i,:)];
                    fitOffspring=[fitOffspring; fitQ(i,:)];
                end
        end
    end
   
    P(randSel, :) = offspring;
    fit(randSel, :) = fitOffspring;
end
