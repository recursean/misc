%
% Uncomment to run this test in MATLAB
%
reference.RegTran_3 = @RegTran_3;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randSepDimd.m', 'randSepDimd.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randProb.m', 'randProb.m');
addpath('.');
% Define constants
d=2; % input dimension
rng('shuffle','twister');
N=25 + 25*randi(5); % size of training data set
noiseRate = 0.05 + 0.05*randi(5); % percentage of target labels flipped
nTests = 14 + 2*randi(5);
T = 440 + 10*randi(11);
% Create a set of random (presumably) non-separable problems
Xs = zeros(N, d);
ys = zeros(N);
for test=1:nTests
[~,Xs(:,:,test),ys(:,test)] = randProb(N,d);
flip = randsample(N, floor(N*noiseRate));
ys(flip,test) = -ys(flip,test);
end
% Run student and professor solutions with same random number stream
% (but a different stream for each run of this test code).
sRng = rng;
[avgPock, avgRfC, avgPockTran, avgRfCTran] = RegTran_3( Xs, ys, T );
rng(sRng);
[avgPock_ref, avgRfC_ref, avgPockTran_ref, avgRfCTran_ref] = ...
reference.RegTran_3( Xs, ys, T );
msg1 = ['Solution avgPock=' num2str(avgPock_ref) ...
', your avgPock=' num2str(avgPock)];
assert(abs(avgPock-avgPock_ref)<1e-4, msg1);
msg2 = ['Solution avgRfC=' num2str(avgRfC_ref) ...
', your avgRfC' num2str(avgRfC)];
assert(abs(avgRfC-avgRfC_ref)<1e-4, msg2);
msg3 = ['Solution avgPockTran=' num2str(avgPockTran_ref) ...
', your avgPockTran' num2str(avgPockTran)];
assert(abs(avgPockTran-avgPockTran_ref)<1e-4, msg3);
msg4 = ['Solution avgRfCTran=' num2str(avgRfCTran_ref) ...
', your avgRfCTran' num2str(avgRfCTran)];
assert(abs(avgRfCTran-avgRfCTran_ref)<1e-4, msg4);