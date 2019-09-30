function EinClass = RegTran_1(X, y)
    [transformedX] = FeatureTransform(X);
    wt = transformedX\y;
    errors = 0;
    for k=1:size(transformedX,1)
        if sign(transformedX(k,:) * wt) ~= y(k)
            errors = errors + 1;
        end
    end
    EinClass = errors / size(transformedX,1);
end

function [transformedX] = FeatureTransform(X)
    transformedX = [size(X,1), 6];
    for i=1:size(X,1)
       transformedX(i,1) = 1;
       transformedX(i,2) = X(i, 1);
       transformedX(i,3) = X(i, 2);
       transformedX(i,4) = (X(i, 1))^2;
       transformedX(i,5) = X(i, 1) * X(i, 2);
       transformedX(i,6) = (X(i, 2))^2;
    end
end