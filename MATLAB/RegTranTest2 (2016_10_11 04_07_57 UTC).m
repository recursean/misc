reference.RegTran_2 = @RegTran_2;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randSepDimd.m', 'randSepDimd.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randProb.m', 'randProb.m');
addpath('.');
% Define constants
d=2; % input dimension
N=25 + 25*randi(5); % size of training data set
% Create random separable problem
[~,X,y] = randProb(N,d);
rng('shuffle','twister');
sRng = rng;
[updatesDefault, updatesRegression] = RegTran_2(X, y);
rng(sRng);
[updatesDefault_ref, updatesRegression_ref] = reference.RegTran_2(X, y);
msg1 = ['Solution updatesDefault=' num2str(updatesDefault_ref) ...
', your updatesDefault=' num2str(updatesDefault)];
assert(updatesDefault == updatesDefault_ref, msg1);
msg2 = ['Solution updatesRegression=' num2str(updatesRegression_ref) ...
', your updatesRegression=' num2str(updatesRegression)];
assert(updatesRegression == updatesRegression_ref, msg2);