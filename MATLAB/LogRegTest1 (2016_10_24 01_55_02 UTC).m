%%
reference.LogReg_1 = @LogReg_1;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randSepDimd.m', 'randSepDimd.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randProb.m', 'randProb.m');
addpath('.');
% Define constants
d=2; % input dimension
N=100; % size of training data set
T=1000; % number of examples used to estimate Eout
eta = 0.01; % learning rate
delta = 0.01;
% Create a random separable problem and random test set for estimating
% Eout.
[wf,X,y] = randProb(N,d);
Xt = randX(T,d);
yt = sign(Xt*wf(1:d)+wf(end));
% Run student and professor solutions with same random number stream
% (but a different stream for each run of this test code).
rng('shuffle','twister');
sRng = rng;
[w,epochs,Ein,Eout] = LogReg_1(X,y,eta,delta,Xt,yt);
% [epochs, Ein, Eout]
rng(sRng);
[w_ref,epochs_ref,Ein_ref,Eout_ref] = reference.LogReg_1(X,y,eta,delta,Xt,yt);
% Compare results
msg = sprintf('%s[%.3g %.3g %.3g]\n%s[%.3g %.3g %.3g]',...
'Your vector was ', w(1),w(2),w(3), ...
'correct vector was ', w_ref(1),w_ref(2),w_ref(3));
assert(norm(w-w_ref) < 1e-4, msg);
msg = sprintf('%s %d\n%s %d',...
'Your epochs: ', epochs,...
'correct epochs: ', epochs_ref);
assert(epochs == epochs_ref, msg);
msg = sprintf('%s %.3g\n%s %.3g',...
'Your Ein was ', Ein, ...
'correct Ein was ', Ein_ref);
assert(abs(Ein-Ein_ref) < 1e-4, msg);
msg = sprintf('%s %.3g\n%s %.3g',...
'Your Eout was ', Eout, ...
'correct Eout was ', Eout_ref);
assert(abs(Eout-Eout_ref) < 1e-4, msg);