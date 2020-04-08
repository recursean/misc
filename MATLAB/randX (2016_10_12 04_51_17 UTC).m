function X = randX( N, d )
%randX Generate data set inputs.
%   Given dimension d and data set size N, generate data set matrix X.
%   By default, generate randomly in box from (-1,-1,...,-1) to 
%   (1,1,...,1).

    X = 2*rand(N,d)-1;

end

