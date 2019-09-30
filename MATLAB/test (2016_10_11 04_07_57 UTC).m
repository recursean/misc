function [c] = test(a, b)
[e, f] = mul(a,b);
e
f
end

function [d,m] = mul(a,b)
d = a/b;
m = a*b;
end