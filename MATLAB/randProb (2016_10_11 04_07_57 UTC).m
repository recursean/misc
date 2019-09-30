function [w,X,y] = randProb(N,d)
%randProb Create a random non-trivially separable data set.

% Ensure that it's actually random
RandStream.setGlobalStream(RandStream('mt19937ar','Seed','shuffle'));

% Ensure that data's not trivialy separable
keepGoing = true;
while keepGoing
    w = randSepDimd(d);
    f = @(x) sign(w'*[x; 1]);
    X = randX(N,d);
    y = zeros(N,1);
    for i=1:N
        y(i) = f((X(i,:))');
    end
    if abs(sum(y))~=N
        keepGoing = false;
    end
end


