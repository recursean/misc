function [w,epochs,Ein,Eout] = LogReg_1(X,y,eta,delta,Xt,yt)
    wOld = [0 0 0];
    w = [0 0 0];
    notDone = true;
    epochs = 0;
    Ein = 0;
    Eout = 0;
    X = [X, ones(size(X,1),1)];
    Xt = [Xt, ones(size(Xt,1),1)];
    
    while notDone
       currentPerm = randperm(size(X,1));
       
       for i=1:size(currentPerm,2)
           n = currentPerm(i);
           newW = (-y(n) * X(n,:)) / (1 + exp(y(n) * w * X(n,:)'));
           w = w - (eta * newW);
       end
       epochs = epochs + 1;
       if norm(wOld - w) < delta
           notDone = false;
       end    
       wOld = w;
    end
    
     for j=1:size(X,1)
         Ein = Ein + log(1 + exp(-y(j) * w * X(j,:)'));
     end
     
     for k=1:size(Xt,1)
         Eout = Eout + log(1 + exp(-yt(k) * w * Xt(k,:)'));
     end
     w = w';
     Ein = Ein / size(X,1)
     Eout = Eout / size(X,1)
end