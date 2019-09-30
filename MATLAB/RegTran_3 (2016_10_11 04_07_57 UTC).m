function [avgPock, avgRfC, avgPockTran, avgRfCTran] = RegTran_3( Xs, ys, T )
    avgPock = 0;
    avgRfC = 0;
    avgPockTran = 0;
    avgRfCTran = 0;
     for l=1:size(Xs,3)
        currentX = Xs(:,:,l);
        currentY = ys(:,l);
        
        [normPocketEin] = Pocket(currentX, currentY, T);
        [normLinReg] = RegTran(currentX, currentY);
        
        [transformedX] = FeatureTransform(currentX);
        
        [transformedPocketEin] = Pocket(transformedX, currentY, T);
        [transformedLinReg] = RegTran(transformedX, currentY);
        
        avgPock = avgPock + normPocketEin;
        avgRfC = avgRfC + normLinReg;
        avgPockTran = avgPockTran + transformedPocketEin;
        avgRfCTran = avgRfCTran + transformedLinReg;
     end
     avgPock = avgPock / size(Xs,3);
     avgRfC = avgRfC / size(Xs,3);
     avgPockTran = avgPockTran / size(Xs,3);
     avgRfCTran = avgRfCTran / size(Xs,3);
end

function [ Ein ] = Pocket(X, y, T)
x = ones([size(X,1),1]);
X = [X, x];
w = zeros(1,size(X,2));
w_hat = zeros(1,size(X,2));
notDone = true;
misclTracker = [];
misclTracker2 = [];
updates = 0;
Ein = 0;

    while notDone && (updates < T)
        for i=1:size(X,1)
            if sign(w * X(i,:)') ~= y(i)
                misclTracker = [misclTracker,i];
            end
        end
        if Ein == 0
            w_hat = w;
            Ein = (1/size(X,1))*size(misclTracker,2);
        end
        if size(misclTracker,2) ~= 0
            iRand = randi(size(misclTracker,2));
            pos = misclTracker(iRand);
            w = w +  y(pos) * X(pos,:);
            updates = updates + 1;
            
            for j=1:size(X,1)
                if sign(w * X(j,:)') ~= y(j)
                    misclTracker2 = [misclTracker2,j];
                end
            end
            
            if (Ein > 1/size(X,1)*size(misclTracker2,2))
                w_hat = w;
                Ein = (1/size(X,1))*size(misclTracker2,2);
            end
            
            misclTracker = [];
            misclTracker2 = [];
        else 
            notDone = false;     
        end
    end
end

function EinClass = RegTran(X, y)
    x = ones([size(X,1),1]);
    X = [X, x];
    wt = X\y;
    errors = 0;
    for k=1:size(X,1)
        if sign(X(k,:) * wt) ~= y(k)
            errors = errors + 1;
        end
    end
    EinClass = errors / size(X,1);
end

function [transformedX] = FeatureTransform(X)
    transformedX = [size(X,1), 6];
    for i=1:size(X,1)
       transformedX(i,1) = 1;
       transformedX(i,2) = X(i, 1);
       transformedX(i,3) = X(i, 2);
       transformedX(i,4) = (X(i, 1))^2;
       transformedX(i,5) = X(i, 1) * X(i, 2);
       transformedX(i,6) = (X(i, 2))^2;
    end
end
