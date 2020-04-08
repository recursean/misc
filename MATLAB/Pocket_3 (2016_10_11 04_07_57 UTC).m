function [ plaWins, adalineWins ] = Pocket_3(Xs, ys, T, eta, sRng)
plaWins = 0;
adalineWins = 0;

for k=1:20
    currentX = Xs(:,:,k);
    currentY = ys(:,k);
    
    rng(sRng);
    
    [whatError] = Pocket_1(currentX, currentY, T);

    rng(sRng);
      
    [whatError2] = PocketAdaline(currentX, currentY, eta, T);
    
    if whatError < whatError2
        plaWins = plaWins + 1;
    elseif whatError > whatError2
        adalineWins = adalineWins + 1;
    end
          
end
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

function [whError] = PocketAdaline(X, y, eta, T)

% YOUR CODE GOES AFTER THIS LINE

x = ones([size(X,1),1]);
X = [X,x];
w = zeros(1,size(X,2)); 
w_hat = zeros(1,size(X,2));

updates = 0;
notDone = true;
miscl = [];
miscl2 = [];
currentEin = 0;

while notDone && (updates < T)
    for i=1:size(X,1)
       if y(i) * (w*X(i,:)') <= 1
            miscl = [miscl, i];
       end
    end

    if currentEin == 0; 
        w_hat = w;
        currentEin = (1/size(X,1))*size(miscl,2)
    end
        
    if ~isempty(miscl)
        iRand = randi(size(miscl,2));
        pos = miscl(iRand);
        signal = w*X(pos,:)';
 
        w = w + eta * (y(pos) - signal) * X(pos, :);
        
        updates = updates + 1;
        
        for j=1:size(X,1)
                if sign(w * X(j,:)') ~= y(j)
                    miscl2 = [miscl2,j];
                end
        end
            
        if (currentEin > 1/size(X,1)*size(miscl2,2))
              w_hat = w;
              currentEin = (1/size(X,1))*size(miscl2,2);
        end
        
        miscl = [];
        miscl2 = [];
    else
        notDone = false;
    end    
end
whError = currentEin;
end
