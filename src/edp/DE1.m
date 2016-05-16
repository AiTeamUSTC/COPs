function C = DE1(P, Q, start, end1, parent, lu, n, bestIndividual, number);

C = zeros(end1-start+1, n);

count = 1;
F = 0.70; %+rand*0.1;
CR = 0.90; %+rand*0.1;
for i = start : end1
  indexSet = 1 : parent;    % Choose the indices for mutation

    indexSet(i) = [];
     
%     indice = floor(rand*popsize*0.5) + 1;
    numbersize = size(number, 1);
    indice = floor(rand*numbersize) + 1;
    indice = number(indice);
    indexSet(indice) = [];
    
    % Choose the first Index
    temp = floor(rand*(parent - 2)) + 1;
    index(1) = indexSet(temp);
    indexSet(temp) = [];

    % Choose the second index
    temp = floor(rand*(parent - 3)) + 1;
    index(2) = indexSet(temp);
    indexSet(temp) = [];

    % Choose the third Index
    temp = floor(rand*(parent - 4)) + 1;
    index(3) = indexSet(temp);
     
    v = Q(i,:)+F.*(bestIndividual(indice,:)-Q(i,:))+F.*(P(index(1),:)-P(index(2),:));
%     v =bestIndividual(indice, :)+F.*(P(index(1), :)-P(index(2), :));
  
if rand < 0
     vio=find(v < lu(1, : )|v > lu(2, : ));
%      v(1,vio)= lu(1, vio)+rand(1, size(vio, 2)).*(lu(2,vio)-lu(1,vio));
     v(1,vio)= lu(1, vio)+rand(1, size(vio, 2)).*(lu(2,vio)-lu(1,vio));
else
%         vio=find(v < lu(1, : )|v > lu(2, : ));
        vioLow = find(v < lu(1, : ));
        v(1, vioLow) = 2.*lu(1, vioLow) - v(1, vioLow);
        vioLowUpper = find(v(1, vioLow) > lu(2, vioLow));
        v(1,vioLow(vioLowUpper)) = lu(1, vioLow(vioLowUpper))+rand(1, size(vioLowUpper, 2)).*(lu(2,vioLow(vioLowUpper))-lu(1,vioLow(vioLowUpper)));
        
        vioUpper = find(v > lu(2, : ));
        v(1, vioUpper) = 2 .* lu(2, vioUpper) - v(1, vioUpper);
        vioUpperLow = find(v(1, vioUpper) < lu(1, vioUpper));
        v(1,vioUpper(vioUpperLow))= lu(1, vioUpper(vioUpperLow))+rand(1, size(vioUpperLow, 2)).*(lu(2,vioUpper(vioUpperLow))-lu(1,vioUpper(vioUpperLow)));

%          v(1,vio)= lu(1, vio)+rand(1, size(vio, 2)).*(lu(2,vio)-lu(1,vio));
end

% if rand < 0.5
%         vio=find(v < lu(1, : )|v > lu(2, : ));
%         v(1,vio)= lu(1, vio)+rand(1, size(vio, 2)).*(lu(2,vio)-lu(1,vio));
% else
%         % Handle the elements of the mutant vector which violate the
%         % boundary
%         vioLow = find(v < lu(1, : ));
%         v(1, vioLow) = 2.*lu(1, vioLow) - v(1, vioLow);
%         vioLowUpper = find(v(1, vioLow) > lu(2, vioLow));
%         v(1,vioLow(vioLowUpper)) = lu(1, vioLow(vioLowUpper))+rand(1, size(vioLowUpper, 2)).*(lu(2,vioLow(vioLowUpper))-lu(1,vioLow(vioLowUpper)));
%         
%         vioUpper = find(v > lu(2, : ));
%         v(1, vioUpper) = 2 .* lu(2, vioUpper) - v(1, vioUpper);
%         vioUpperLow = find(v(1, vioUpper) < lu(1, vioUpper));
%         v(1,vioUpper(vioUpperLow))= lu(1, vioUpper(vioUpperLow))+rand(1, size(vioUpperLow, 2)).*(lu(2,vioUpper(vioUpperLow))-lu(1,vioUpper(vioUpperLow)));
% 
% end

    % Implement the binomial crossover
    jRand = floor(rand * n) + 1;
    t = rand(1, n) < CR;
    %t(1, jRand) = 1;
    t(jRand)=1;
    t_ = 1 - t;
    u = t .* v + t_ .* Q(i, : );
%     u = (0.9*v+0.1*Q(i));
    C(count, : ) = u;
    count = count+1;

end
