function [ avgPLAError, avgPocketError, avgUpdatesForPocket ] = Pocket_2(Xs, ys, T)
avgPLAError = 0;
avgPocketError = 0;
avgUpdatesForPocket = 0;
for k=1:20
    currentX = Xs(:,:,k);
    currentY = ys(:,k);
    [hypError, whatError, updates] = Pocket_1(currentX, currentY, T);
    avgPLAError = avgPLAError + hypError;
    avgPocketError = avgPocketError + whatError;
    avgUpdatesForPocket = avgUpdatesForPocket + updates;
end

avgPLAError = avgPLAError / 20;
avgPocketError = avgPocketError / 20;
avgUpdatesForPocket = avgUpdatesForPocket / 20;
end

function [whError] = Pocket_1(X, y, T)
x = ones([size(X,1),1]);
X = [X, x];
w = zeros(1,size(X,2));
w_hat = zeros(1,size(X,2));
notDone = true;
misclTracker = [];
misclTracker2 = [];
updates = 0;
pocketUpdates = 0;
currentEin = 0;

    while notDone && (updates < T)
        for i=1:size(X,1)
            if sign(w * X(i,:)') ~= y(i)
                misclTracker = [misclTracker,i];
            end
        end
        if currentEin == 0
            w_hat = w;
            currentEin = (1/size(X,1))*size(misclTracker,2);
        end
        if size(misclTracker,2) ~= 0
            iRand = randi(size(misclTracker,2));
            pos = misclTracker(iRand);
            w = w +  y(pos) * X(pos,:);
            updates = updates + 1;
            
            for j=1:size(X,1)
                if sign(w * X(j,:)') ~= y(j)
                    misclTracker2 = [misclTracker2,j];
                    hError = (1/size(X,1))*size(misclTracker2,2);
                end
            end
            
            if (currentEin > 1/size(X,1)*size(misclTracker2,2))
                w_hat = w;
                pocketUpdates = updates;
                currentEin = (1/size(X,1))*size(misclTracker2,2);
            end
            
            misclTracker = [];
            misclTracker2 = [];
        else 
            notDone = false;     
        end
    end
whError = currentEin;
end

