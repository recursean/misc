function [w1,w2,w0] = Adaline_1(X, y, eta)

% Initialize the random number functions
rng = RandStream('mt19937ar', 'Seed', 42);
RandStream.setGlobalStream(rng);

% YOUR CODE GOES AFTER THIS LINE

x = ones([size(X,1),1]);
X = [x, X];
w = zeros(1,size(X,2)); 

updates = 0;
nCorrect = 0;
notDone = true;
miscl = [];
correctMiscl = [];

while notDone && (updates < 500)
    for i=1:size(X,1)
       if y(i) * (w*X(i,:)') <= 1
            miscl = [miscl, i];
       end
       if sign(w * X(i,:)') ~= y(i)
          correctMiscl = [correctMiscl, i]; 
       end
    end
    
    if isempty(correctMiscl)
        if nCorrect == 0
           firstCorrect = updates; 
        end
        nCorrect = nCorrect + 1;
        correctMiscl = [];
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
w0 = w(1);
w1 = w(2);
w2 = w(3);
end