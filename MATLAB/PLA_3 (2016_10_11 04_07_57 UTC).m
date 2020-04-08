function [pctWin, rIt, fIt] = PLA_3(Xs, ys)
randPLA = 0;
totalTR = 0;
totalTNR = 0;
  for j=1:100
        currentX = Xs(:,:,j);
        currentY = ys(:,j);
        [gNR, wNR, tNR] = PLANR(currentX, currentY);
        [gR, wR, tR] = PLAR(currentX, currentY);
        if tR < tNR
            randPLA = randPLA + 1;
        end
        totalTR = totalTR + tR;
        totalTNR = totalTNR + tNR;
    end
pctWin = randPLA / 100;
rIt = totalTR / 100;
fIt = totalTNR / 100;
end

function [g,w,t] = PLANR(X, y)
x = ones([size(X,1),1]);
X = [X, x];
w = zeros(1,size(X,2));
wPos = 1;
notDone = true;
i = 1;
t = 0;

while notDone
    if sign(w(wPos,:) * X(i,:)') ~= y(i)
        w(wPos + 1, :) = w(wPos,:) +  y(i) * X(i,:);
        wPos = wPos + 1;
        i = 1;
        t = t+1;
    else
        i = i + 1;
    end
    if i > size(X, 1)
        notDone = false;     
    end
end

g = @(x) sign(w(wPos,:)*[x;1]);
w = w(size(w,1), :)';

end

function [g,w,t] = PLAR(X, y)
x = ones([size(X,1),1]);
X = [X, x];
w = zeros(1,size(X,2));
wPos = 1;
notDone = true;
t = 0;
misclTracker = [];
while notDone
    for k=1:50
       if sign(w(wPos,:) * X(k,:)') ~= y(k)
            misclTracker = [misclTracker,k];
       end
    end
    if size(misclTracker,2) ~= 0
        iRand = randi([1,size(misclTracker,2)],1,1)
        pos = misclTracker(iRand);
        w(wPos + 1, :) = w(wPos,:) +  y(pos) * X(pos,:);
        wPos = wPos + 1;
        t = t+1;
        misclTracker = [];
    else 
        notDone = false;     
    end
end

g = @(x) sign(w(wPos,:)*[x;1]);
w = w(size(w,1), :)';

end
