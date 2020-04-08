function [ wlog, wlin, entropy, crossEntropy ] = LogReg_3( X, y, eta, delta, Xt)
    X = [X, ones(size(X,1),1)];
    Xt = [Xt, ones(size(Xt,1),1)];
    wlog = LogReg_1(X, y, eta, delta);
    wlin = X\y;
    entropy = 0;
    crossEntropy = 0;
    
    for j=1:size(Xt,1)
        fx = (1 / (1 + exp(-wlog' * Xt(j,:)')));
        
        hx = (1 / (1 + exp(-wlin' * Xt(j,:)')));
        
        entropy = entropy + H(fx, fx)
        
        crossEntropy = crossEntropy + H(fx, hx)
    end
    
    entropy = entropy / size(Xt,1);
    crossEntropy = crossEntropy / size(Xt,1);
end

function [expValue] = H(p,q)
    expValue = p * log2(1/q) + (1-p) * log2(1/(1-q));
end
function [w] = LogReg_1(X,y,eta,delta)
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
