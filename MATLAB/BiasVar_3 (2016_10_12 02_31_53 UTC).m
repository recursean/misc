function [bias, var] = BiasVar_3(Xs, ys, XTest, yTest)
    abar = 0;
    bbar = 0;
    abar2 = 0;
    bbar2 = 0;
    cbar2 = 0;
    abar3 = 0;
    bbar3 = 0;
    cbar3 = 0;
    dbar3 = 0;
    bias = [0 0 0];
    var = [0 0 0];
    g1 = [];
    g2 = [];
    g3 = [];
    w = [];
    Ed = [];
    Ex = [];
    
    %gbar
    for i=1:size(Xs,2)
        currentX = [ones(5,1) Xs(:,i)];
        g1(:,i) =  currentX \ ys(:,i);
        
        trans1 = Phi1(Xs(:,i));
        trans2 = Phi2(Xs(:,i));
        
        g2(:,i) = trans1 \ ys(:,i);
        g3(:,i) = trans2 \ ys(:,i);
    end
    
    for j=1:size(g1,2)
       abar = abar + g1(2,j);
       bbar = bbar + g1(1,j);
    end
    abar = abar / size(g1,2);
    bbar = bbar / size(g1,2);
    
    for j=1:size(g2,2)
       abar2 = abar2 + g2(2,j);
       bbar2 = bbar2 + g2(3,j);
       cbar2 = cbar2 + g2(1,j);
    end
    abar2 = abar2 / size(g2,2);
    bbar2 = bbar2 / size(g2,2);
    cbar2 = cbar2 / size(g2,2);
    
    for j=1:size(g3,2)
       abar3 = abar3 + g3(2,j);
       bbar3 = bbar3 + g3(3,j);
       cbar3 = cbar3 + g3(4,j);
       dbar3 = dbar3 + g3(1,j);
    end
    abar3 = abar3 / size(g3,2);
    bbar3 = bbar3 / size(g3,2);
    cbar3 = cbar3 / size(g3,2);
    dbar3 = dbar3 / size(g3,2);
    
    %bias/var
    for l=1:size(g1,2)
        wTilde(l,:) = [g1(1,l)^2, (2 * g1(2,l) * g1(1,l)), g1(2,l)^2]; 
    end
    
    wTildeBar = sum(wTilde) / size(wTilde,1);
    
    for k=1:size(XTest,1)
        bias(1) = bias(1) + ((abar * XTest(k) + bbar) - yTest(k))^2;
        
        TransXTest1 = Phi1(XTest(k));
        TransXTest2 = Phi2(XTest(k));
        
        bias(2) = bias(2) + ((abar2 * TransXTest1(2) + bbar2 * TransXTest1(3) + cbar2) - yTest(k))^2;
        bias(3) = bias(3) + ((abar3 * TransXTest2(2) + bbar3 * TransXTest2(3) + cbar3 * TransXTest2(4) + dbar3) - yTest(k))^2;
        z = [1; XTest(k); XTest(k)^2];
        Ed(k) = wTildeBar * z;
        
        Ex(k) = (abar * XTest(k) + bbar)^2;
    end
    
    AvgEd = sum(Ed) / size(Ed,2);
    AvgEx = sum(Ex) / size(Ex,2);
    
    var(1) = AvgEd - AvgEx;
    bias(1) = bias(1) / size(XTest,1);
    bias(2) = bias(2) / size(XTest,1);
    bias(3) = bias(3) / size(XTest,1);
end

function [transformedX] = Phi1(X)
    transformedX = [size(X,1), 3];
    for i=1:size(X,1)
       transformedX(i,1) = 1;
       transformedX(i,2) = X(i);
       transformedX(i,3) = X(i)^2;
    end
end

function [transformedX] = Phi2(X)
    transformedX = [size(X,1), 4];
    for i=1:size(X,1)
       transformedX(i,1) = 1;
       transformedX(i,2) = X(i);
       transformedX(i,3) = X(i)^2;
       transformedX(i,4) = X(i)^3;
    end
end
