function [w,epochs,Ein,Eout] = LogReg_1(X,y,eta,delta,Xt,yt)
    w = [0 0 0];
    newW = [0 0 0];
    notDone = true;
    epochs = 0;
    Ein = 0;
    Eout = 0;
    X = [X, ones(size(X,1),1)];
    
    while notDone
       currentPerm = randperm(size(X,1));
       
       for i=1:size(currentPerm,2)
           n = currentPerm(i);
           newW = (-y(n) * X(n,:)) / (1 + exp(y(n) * newW * X(n,:)'));
           newW = newW - eta * newW;
       end
      
       if norm(w - newW) < delta
           w = newW
           notDone = false;
       else
           w = newW;
       end       
    end
end