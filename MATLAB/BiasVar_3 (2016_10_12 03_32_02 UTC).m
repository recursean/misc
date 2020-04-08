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
        wTilde1(l,:) = [g1(1,l)^2, (2 * g1(2,l) * g1(1,l)), g1(2,l)^2]; 
    end
    
    for l=1:size(g2,2)
        wTilde2(l,:) = [g2(1,l)^2, (2 * g2(2,l) * g2(1,l)), (2 * g2(1,l) * g2(3,l)), g2(2,l)^2, (2 * g2(2,l) * g2(3,l)), g2(3,l)^2]; 
    end
    
    for l=1:size(g3,2)
        wTilde3(l,:) = [g3(1,l)^2, (2 * g3(2,l) * g3(1,l)), g3(2,l)^2]; 
    end
    
    wTildeBar1 = sum(wTilde1) / size(wTilde1,1);
    wTildeBar2 = sum(wTilde2) / size(wTilde2,1);
    wTildeBar3 = sum(wTilde3) / size(wTilde3,1);
    
    for k=1:size(XTest,1)
        bias(1) = bias(1) + ((abar * XTest(k) + bbar) - yTest(k))^2;
        
        TransXTest1 = Phi1(XTest(k));
        TransXTest2 = Phi2(XTest(k));
        
        bias(2) = bias(2) + ((abar2 * TransXTest1(2) + bbar2 * TransXTest1(3) + cbar2) - yTest(k))^2;
        bias(3) = bias(3) + ((abar3 * TransXTest2(2) + bbar3 * TransXTest2(3) + cbar3 * TransXTest2(4) + dbar3) - yTest(k))^2;
        
        z1 = [1; XTest(k); XTest(k)^2];
        
        Ed1(k) = wTildeBar1 * z1;
        Ed2(k) = wTildeBar2 * z;
        Ed3(k) = wTildeBar3 * z;
        
        Ex1(k) = (abar * XTest(k) + bbar)^2;
        Ex2(k) = (abar2 * XTest(k) + bbar2)^2;
        Ex3(k) = (abar3 * XTest(k) + bbar3)^2;
    end
    
    AvgEd1 = sum(Ed1) / size(Ed1,2);
    AvgEx1 = sum(Ex1) / size(Ex1,2);
    AvgEd2 = sum(Ed2) / size(Ed2,2);
    AvgEx2 = sum(Ex2) / size(Ex2,2);
    AvgEd3 = sum(Ed3) / size(Ed3,2);
    AvgEx3 = sum(Ex3) / size(Ex3,2);
    
    var(1) = AvgEd1 - AvgEx1;
    var(2) = AvgEd2 - AvgEx2;
    var(2) = AvgEd3 - AvgEx3;
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
