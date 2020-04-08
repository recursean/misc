function [ defaultUpdates, regressionUpdates ] = RegTran_2( X, y )
x = ones([size(X,1),1]);
X = [X, x];

initW = zeros(1,size(X,2))';
[defaultUpdates] = PLAR(X, y, initW);
linRegW = X\y
[regressionUpdates] = PLAR(X, y, linRegW);
end

function [updates] = PLAR(X, y, w)
%x = ones([size(X,1),1]);
%X = [X, x];
%w = zeros(1,size(X,2));
notDone = true;
updates = 0;
misclTracker = [];
while notDone 
    for k=1:size(X,1)
       if sign(X(k,:) * w) ~= y(k)
            misclTracker = [misclTracker,k];
       end
    end
    if size(misclTracker,2) ~= 0
        iRand = randi(size(misclTracker,2));
        pos = misclTracker(iRand);
        w = w +  y(pos) * X(pos,:)';
        updates = updates+1;
        misclTracker = [];
    else 
        notDone = false;     
    end
end
end