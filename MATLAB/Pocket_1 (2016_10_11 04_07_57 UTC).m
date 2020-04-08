function [ wh1,wh2,wh0 ] = Pocket_1(X, y, T)
x = ones([size(X,1),1]);
X = [X, x];
w = zeros(1,size(X,2));
w_hat = zeros(1,size(X,2));
notDone = true;
misclTracker = [];
misclTracker2 = [];
updates = 0;
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
                end
            end
            
            if (currentEin > 1/size(X,1)*size(misclTracker2,2))
                w_hat = w;
                currentEin = (1/size(X,1))*size(misclTracker2,2);
            end
            
            misclTracker = [];
            misclTracker2 = [];
        else 
            notDone = false;     
        end
    end
    wh1 = w_hat(1,1);
    wh2 = w_hat(1,2);
    wh0 = w_hat(1,3);
end
