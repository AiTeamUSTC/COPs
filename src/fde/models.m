function [P, fit] = models(P, fit, Q, fitQ, C, fitC, randSel, mu, epsilon)
    C = [Q; C];
    fitC = [fitQ; fitC];
    % Compute the degree of constraint violations of D
    vioSumC = sum(fitC(:, 2 : size(fitC, 2)), 2);
    
    total = size(C, 1);
    number = zeros(1, total);

    for i = 1:total
        for j = 1 :total
            if vioSumC(i)<=epsilon & vioSumC(j)<=epsilon
                if fitC(i,1)<fitC(j,1)
                    number(1, i) = number(1, i)+1;
                end
            elseif vioSumC(i)<=epsilon & vioSumC(j)>epsilon
                    number(1, i) = number(1, i)+1;
            elseif vioSumC(i)>epsilon & vioSumC(j)>epsilon
                   if vioSumC(i)<=vioSumC(j)
                      number(1, i) = number(1, i)+1;
                   end
            end
        end
    end
   [~, Index] = sort(number, 'descend');
   C = C(Index, :);
   fitC = fitC(Index, :);
    
    P(randSel, :) = C(1:mu, :);
    fit(randSel, :) = fitC(1:mu, :);
  
end
