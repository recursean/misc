reference.LogReg_2 = @LogReg_2;
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
[ wlog, wlin, Eoutlog, Eoutlin ] = LogReg_2(X,y,eta,delta,Xt,yt);
% [Eoutlog, Eoutlin]
rng(sRng);
[ wlog_ref, wlin_ref, Eoutlog_ref, Eoutlin_ref ] = ...
reference.LogReg_2(X,y,eta,delta,Xt,yt);
% Compare results
msg = sprintf('%s[%.3g %.3g %.3g]\n%s[%.3g %.3g %.3g]',...
'Your wlog was ', wlog(1),wlog(2),wlog(3), ...
'correct wlog was ', wlog_ref(1),wlog_ref(2),wlog_ref(3));
assert(norm(wlog-wlog_ref) < 1e-4, msg);
msg = sprintf('%s[%.3g %.3g %.3g]\n%s[%.3g %.3g %.3g]',...
'Your wlin was ', wlin(1),wlin(2),wlin(3), ...
'correct wlin was ', wlin_ref(1),wlin_ref(2),wlin_ref(3));
assert(norm(wlin-wlin_ref) < 1e-4, msg);
msg = sprintf('%s %.3g\n%s %.3g',...
'Your Eoutlog was ', Eoutlog, ...
'correct Eoutlog was ', Eoutlog_ref);
assert(abs(Eoutlog-Eoutlog_ref) < 1e-4, msg);
msg = sprintf('%s %.3g\n%s %.3g',...
'Your Eoutlin was ', Eoutlin, ...
'correct Eoutlin was ', Eoutlin_ref);
assert(abs(Eoutlin-Eoutlin_ref) < 1e-4, msg);