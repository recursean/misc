function [r,s,t,u,v,w] = prep_check1(x,y)
    r = x + 1;
    s = 'Hello, world!';
    if x > 13
        t = 1;
    else
        t = -1;
    end;
    u = [1;1;1;1;1;1;1;1;1;1;];
    y = u(2,1);
    v = y;
    w = y';
end
    