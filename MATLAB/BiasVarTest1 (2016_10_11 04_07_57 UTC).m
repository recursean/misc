%%
reference.BiasVar_1 = @BiasVar_1;
urlwrite('http://www.mathcs.duq.edu/~jackson/ML/MATLAB/utilities/randX.m', 'randX.m');
% Target function. Not symmetric, not a polynomial, high var...
f = @(x) 3*(sqrt(0.25*(x+1.01))*exp(-x)) - 0.8;
N = 2;
K = 10000;
Xs = randX(N,K);
ys = arrayfun(f,Xs);
T = 1000;
XTest = randX(T, 1);
yTest = arrayfun(f,XTest);
[abar, bbar, bias, var] = BiasVar_1(Xs,ys,XTest,yTest);
[abar_ref, bbar_ref, bias_ref, var_ref] = ...
reference.BiasVar_1(Xs,ys,XTest,yTest);
msg1 = ['abar is wrong: reference is ', num2str(abar_ref), ...
' yours is ', num2str(abar)];
assert( abs(abar-abar_ref) < 1e-4, msg1 );
msg2 = ['bbar is wrong: reference is ', num2str(bbar_ref), ...
' yours is ', num2str(bbar)];
assert( abs(bbar-bbar_ref) < 1e-4, msg2 );
msg3 = ['bias is wrong: reference is ', num2str(bias_ref), ...
' yours is ', num2str(bias)];
assert( abs(bias-bias_ref) < 1e-4, msg3 );
msg4 = ['var is wrong: reference is ', num2str(var_ref), ...
' yours is ', num2str(var)];
assert( abs(var-var_ref) < 1e-4, msg4 );