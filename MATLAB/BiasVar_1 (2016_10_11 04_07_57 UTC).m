function [abar, bbar, bias, var] = BiasVar_1(Xs, ys, XTest, yTest)
    abar = 0;
    bbar = 0;
    bias = 0;
    var = 0;
    g = [];
    w = [];
    Ed = [];
    Ex = [];
    
    %gbar
    for i=1:size(Xs,2)
        g(:,i) = [ones(2,1) Xs(:,i)] \ ys(:,i);
    end
    for j=1:size(g,2)
       abar = abar + g(2,j);
       bbar = bbar + g(1,j);
    end
    abar = abar / size(g,2);
    bbar = bbar / size(g,2);
    
    %bias/var
    for l=1:size(g,2)
        wTilde(l,:) = [g(1,l)^2, (2 * g(2,l) * g(1,l)), g(2,l)^2]; 
    end
    
    wTildeBar = sum(wTilde) / size(wTilde,1);
    
    for k=1:size(XTest,1)
        bias = bias + ((abar * XTest(k) + bbar) - yTest(k))^2;
        
        z = [1; XTest(k); XTest(k)^2];
        Ed(k) = wTildeBar * z;
        
        Ex(k) = (abar * XTest(k) + bbar)^2;
    end
    
    AvgEd = sum(Ed) / size(Ed,2);
    AvgEx = sum(Ex) / size(Ex,2);
    
    var = AvgEd - AvgEx;
    bias = bias / size(XTest,1);
end