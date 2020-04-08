function [w,X,y] = randProb(N,d)
%randProb Create a random non-trivially separable data set.

% Ensure that it's actually random
rng('shuffle','twister');

% Ensure that data's not trivialy separable
keepGoing = true;
while keepGoing
    w = randSepDimd(d);
    X = randX(N,d);
    y = sign(X*w(1:d)+w(end));
    if abs(sum(y))~=N
        keepGoing = false;
    end
end


