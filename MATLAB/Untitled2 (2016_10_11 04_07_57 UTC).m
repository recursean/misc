reference.Pocket_1 = @Pocket_1;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randSepDimd.m', 'randSepDimd.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randProb.m', 'randProb.m');
addpath('.');
% Define constants
d=2; % input dimension
N=100; % size of training data set
T=1000; % number of iterations for Pocket Algorithm
noiseRate = 0.1; % percentage of target labels flipped
% Create a random (presumably) non-separable problem
[w,X,y] = randProb(N,d);
flip = randsample(N, floor(N*noiseRate));
y(flip) = -y(flip);
% Run student and professor solutions with same random number stream
% (but a different stream for each run of this test code).
rng('shuffle','twister');
sRng = rng;
[wh1,wh2,wh0] = Pocket_1(X,y,T);
rng(sRng);
[wh1_ref,wh2_ref,wh0_ref] = reference.Pocket_1(X,y,T);
% Compare results
msg = sprintf('%s[%.3g %.3g %.3g]\n%s[%.3g %.3g %.3g]',...
'Your vector was ', wh1,wh2,wh0, ...
'correct vector was ', wh1_ref,wh2_ref,wh0_ref);
assert(norm([wh1,wh2,wh0]-[wh1_ref,wh2_ref,wh0_ref]) < 1e-4, msg);