	
%%
reference.BiasVar_2 = @BiasVar_2;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
% Target function. Not symmetric, not a polynomial, high var...
f = @(x) 3*(sqrt(0.25*(x+1.01))*exp(-x)) - 0.8;
N = 4;
K = 10000;
Xs = randX(N,K);
ys = arrayfun(f,Xs);
T = 1000;
XTest = randX(T, 1);
yTest = arrayfun(f,XTest);
[bias, var] = BiasVar_2(Xs,ys,XTest,yTest);
[bias_ref, var_ref] = ...
reference.BiasVar_2(Xs,ys,XTest,yTest);
msg1 = ['bias is wrong: reference is ', num2str(bias_ref), ...
' yours is ', num2str(bias)];
assert( abs(norm(bias)-norm(bias_ref)) < 1e-4, msg1 );
msg2 = ['var is wrong: reference is ', num2str(var_ref), ...
' yours is ', num2str(var)];
assert( abs(norm(var)-norm(var_ref)) < 1e-4, msg2 );