function [bias, var] = BiasVar_2(Xs, ys, XTest, yTest)
    abar2 = 0;
    bbar2 = 0;
    abar3 = 0;
    bbar3 = 0;
    abar4 = 0;
    bbar4 = 0;
    bias = [0 0 0];
    var = [];
    g = [];
    w = [];
    Ed = [];
    Ex = [];
    
    %gbar
    for i=1:size(Xs,2)
        currentX = [ones(2,1) Xs(1:2,i)];
        g2(:,i) = currentX \ ys(1:2,i);
        
        
        currentX = [ones(3,1) Xs(1:3,i)];
        g3(:,i) = currentX \ ys(1:3,i);
        
        
        currentX = [ones(4,1) Xs(1:4,i)];
        g4(:,i) = currentX \ ys(1:4,i);
    end
    
    for j=1:size(g2,2)
       abar2 = abar2 + g2(2,j);
       bbar2 = bbar2 + g2(1,j);
    end
    abar2 = abar2 / size(g2,2);
    bbar2 = bbar2 / size(g2,2);
    
    for j=1:size(g3,2)
       abar3 = abar3 + g3(2,j);
       bbar3 = bbar3 + g3(1,j);
    end
    abar3 = abar3 / size(g3,2);
    bbar3 = bbar3 / size(g3,2);
    
    for j=1:size(g4,2)
       abar4 = abar4 + g4(2,j);
       bbar4 = bbar4 + g4(1,j);
    end
    abar4 = abar4 / size(g4,2);
    bbar4 = bbar4 / size(g4,2);
    
    %bias/var
    for l=1:size(g2,2)
        wTilde2(l,:) = [g2(1,l)^2, (2 * g2(2,l) * g2(1,l)), g2(2,l)^2]; 
    end
    
    for l=1:size(g3,2)
        wTilde3(l,:) = [g3(1,l)^2, (2 * g3(2,l) * g3(1,l)), g3(2,l)^2]; 
    end
    
    for l=1:size(g4,2)
        wTilde4(l,:) = [g4(1,l)^2, (2 * g4(2,l) * g4(1,l)), g4(2,l)^2]; 
    end
    
    wTildeBar2 = sum(wTilde2) / size(wTilde2,1);
    wTildeBar3 = sum(wTilde3) / size(wTilde3,1);
    wTildeBar4 = sum(wTilde4) / size(wTilde4,1);
    
    for k=1:size(XTest,1)
        bias(1) = bias(1) + ((abar2 * XTest(k) + bbar2) - yTest(k))^2;
        bias(2) = bias(2) + ((abar3 * XTest(k) + bbar3) - yTest(k))^2;
        bias(3) = bias(3) + ((abar4 * XTest(k) + bbar4) - yTest(k))^2;
         
        z = [1; XTest(k); XTest(k)^2];
        
        Ed2(k) = wTildeBar2 * z;
        Ed3(k) = wTildeBar3 * z;
        Ed4(k) = wTildeBar4 * z;
        
        Ex2(k) = (abar2 * XTest(k) + bbar2)^2;
        Ex3(k) = (abar3 * XTest(k) + bbar3)^2;
        Ex4(k) = (abar4 * XTest(k) + bbar4)^2;
    end
    
    AvgEd2 = sum(Ed2) / size(Ed2,2);
    AvgEx2 = sum(Ex2) / size(Ex2,2);
    
    AvgEd3 = sum(Ed3) / size(Ed3,2);
    AvgEx3 = sum(Ex3) / size(Ex3,2);
    
    AvgEd4 = sum(Ed4) / size(Ed4,2);
    AvgEx4 = sum(Ex4) / size(Ex4,2);
    
    var(1) = AvgEd2 - AvgEx2;
    var(2) = AvgEd3 - AvgEx3;
    var(3) = AvgEd4 - AvgEx4;
    
    bias(1) = bias(1) / size(XTest,1);
    bias(2) = bias(2) / size(XTest,1);
    bias(3) = bias(3) / size(XTest,1);
    
    var
    bias
end