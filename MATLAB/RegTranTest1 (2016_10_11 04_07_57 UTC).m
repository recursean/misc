reference.RegTran_1 = @RegTran_1;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randSepDimd.m', 'randSepDimd.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randProb.m', 'randProb.m');
addpath('.');
% Define constants
d=2; % input dimension
N=25 + 25*randi(5); % size of training data set
noiseRate = 0.1; % percentage of target labels flipped
% Create a random, (presumably) non-separable problem
[~,X,y] = randProb(N,d);
flip = randsample(N, floor(N*noiseRate));
y(flip) = -y(flip);
EinClass = RegTran_1(X, y);
EinClass_ref = reference.RegTran_1(X, y);
msg = ['Solution EinClass=' num2str(EinClass_ref) ...
', your EinClass=' num2str(EinClass)];
assert(abs(EinClass-EinClass_ref)<1e-4, msg);