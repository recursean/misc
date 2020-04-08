function [ avgUpdates, avgAngDiff, avgErr ] = Adaline_3( ws, Xs, ys, eta )
% Initialize the random number functions.
% NOTE: Do this just once at the beginning of Adaline_3, no where else.
rng = RandStream('mt19937ar', 'Seed', 42);
RandStream.setGlobalStream(rng);

% Load  utility function for computing area of symmetric difference of two 
% perceptrons within the (-1,-1) to (1,1) box.  
% (Divide by four to get error rate
% of one perceptron relative to the other within this box.)
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/symDiffPerceptrons.m', ...
         'symDiffPerceptrons.m');
addpath('.');

% YOUR CODE GOES BELOW THIS LINE
avgUpdates = 0;
angDiffCount = 0;
errCount = 0;
for k=1:20
    currentX = Xs(:,:,k);
    currentY = ys(:,k);
    currentW = ws(:,k); 
    
    [finalUpdateCount, wout] = Adaline_1(currentX, currentY, eta);
    
    avgUpdates = avgUpdates + finalUpdateCount;
    
    angDiff = calcAngDiff(wout, currentW');
    angDiffCount = angDiffCount + angDiff;
    
    errCount = errCount + (symDiffPerceptrons(wout', currentW') / 4);
    
end
avgUpdates = avgUpdates / 20;
avgAngDiff = angDiffCount / 20;
avgErr = errCount / 20;
end

function [updates, wout] = Adaline_1(X, y, eta)

% YOUR CODE GOES AFTER THIS LINE

x = ones([size(X,1),1]);
X = [X,x];
w = zeros(1,size(X,2)); 

updates = 0;
notDone = true;
miscl = [];

while notDone && (updates < 100)
    for i=1:size(X,1)
       if y(i) * (w*X(i,:)') <= 1
            miscl = [miscl, i];
       end
    end

    if ~isempty(miscl)
        iRand = randi(size(miscl,2));
        pos = miscl(iRand);
        signal = w*X(pos,:)';
 
        w = w + eta * (y(pos) - signal) * X(pos, :);
        
        updates = updates + 1;
        
        miscl = [];
    else
        notDone = false;
    end    
end
wout = w;
end

function [angle] = calcAngDiff(hypW, targetW)
num = dot(hypW, targetW);

den1 = 0;
den2 = 0;
for j=1:size(hypW,2)
    den1 = den1 + hypW(j)^2;
    den2 = den2 + targetW(j)^2;
end
den1 = sqrt(den1);
den2 = sqrt(den2);
den = den1 * den2;

angleRad = acos(num / den);
angle = (180/pi) * angleRad;
end