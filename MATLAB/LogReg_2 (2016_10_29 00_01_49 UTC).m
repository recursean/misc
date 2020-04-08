function [ wlog, wlin, Eoutlog, Eoutlin ] = LogReg_2( X, y, eta, delta, Xt, yt )
    X = [X, ones(size(X,1),1)];
    Xt = [Xt, ones(size(Xt,1),1)];
    wlog = LogReg_1(X, y, eta, delta);
    wlin = X\y;
    Eoutlog = 0;
    Eoutlin = 0;
    
    for j=1:size(Xt,1)
       if sign(wlog' * Xt(j,:)') ~= yt(j)
           Eoutlog = Eoutlog + 1;
       end
       if sign(wlin' * Xt(j,:)') ~= yt(j)
           Eoutlin = Eoutlin + 1;
       end
    end
    
    Eoutlog = Eoutlog / size(Xt,1)
    Eoutlin = Eoutlin / size(Xt,1)
end

function [w] = LogReg_1(X,y,eta,delta,Xt,yt)
    wOld = [0 0 0];
    w = [0 0 0];
    notDone = true;
    
    while notDone
       currentPerm = randperm(size(X,1));
       
       for i=1:size(currentPerm,2)
           n = currentPerm(i);
           newW = (-y(n) * X(n,:)) / (1 + exp(y(n) * w * X(n,:)'));
           w = w - (eta * newW);
       end
       if norm(wOld - w) < delta
           notDone = false;
       end    
       wOld = w;
    end
     w = w';
end