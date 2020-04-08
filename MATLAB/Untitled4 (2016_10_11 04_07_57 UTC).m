reference.Adaline_3 = @Adaline_3;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randSepDimd.m', 'randSepDimd.m');
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randProb.m', 'randProb.m');
addpath('.');
N = 50;
d = 2;
nTests = 20;
Xs = zeros(N, d, nTests);
ys = zeros(N, nTests);
ws = zeros(d+1, nTests);
for i=1:nTests
[ws(:,i), Xs(:,:,i), ys(:,i)] = randProb(N,d);
end
etas = [1.8, 1, 0.1];
for eta=etas
[avgUpdates, avgAngle, avgErr] = Adaline_3(ws, Xs, ys, eta);
[ref_avgUpdates, ref_avgAngle, ref_avgErr] = reference.Adaline_3(ws, Xs, ys, eta);
assert(avgUpdates == ref_avgUpdates, 'avgUpdates do not agree');
assert(abs(avgAngle-ref_avgAngle)<1e-4, 'avgAngles do not agree');
assert(abs(avgErr-ref_avgErr)<1e-4, 'avgErrors do not agree');
end