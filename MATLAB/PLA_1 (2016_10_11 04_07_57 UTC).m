function [g,w,t] = PLA_2(X, y)
x = ones([size(X,1),1]);
X = [X, x];
w = zeros(1,size(X,2));
wPos = 1;
notDone = true;
i = 1;
t = 0;

while notDone
    if sign(w(wPos,:) * X(i,:)') ~= y(i)
        w(wPos + 1, :) = w(wPos,:) +  y(i) * X(i,:)
        wPos = wPos + 1;
        i = 1;
        t = t+1;
    else
        i = i + 1;
    end
    if i > size(X, 1)
        notDone = false;     
    end
end

g = @(x) sign(w(wPos,:)*[x;1]);
w = w(size(w,1), :)';
%W = setdiff(w, w(1));
%w = [W w(1)]';
%w0 = w(size(w,1),1)
%w1 = w(size(w,1),2)
%w2 = w(size(w,1),3)
