function [ area ] = symDiffPerceptrons( wf, wg )
%SYMDIFFPERCEPTRONS Compute area of symmetric difference of perceptrons.
%   Given weight vectors for two-dimensional perceptrons f and g,
%   compute the area of their symmetric difference in the (-1,-1)
%   to (1,1) box.
%   Assuming weight vectors are in the form [w1,w2,w0]'.
%   Handles cases where f and/or g do not pass through the box.
%   Returns -4 if there is a numerical precision error (probably due to
%      one or both weight vectors having very large values).

% Create functional versions of weight vectors
f = @(x1,x2) sign(wf(1)*x1 + wf(2)*x2 + wf(end));
g = @(x1,x2) sign(wg(1)*x1 + wg(2)*x2 + wg(end));

% x and y coordinates of corners of the box. Used in inpolygon tests.
xbox = [-1; -1; 1;  1; -1]; % not sure I need to close the box polygon,
ybox = [-1;  1; 1; -1; -1]; % but erring on the safe side

% Create vector cw of points in clockwise order beginning at (-1,-1).
% The points of interest are the corners of the box and the intersections
% of the perceptron surfaces with the sides of the box.
% cw is a cell array in which each row contains a point (vector), label
% 'f', 'g', or 'c', and the clockwise distance from (-1,-1) to the point.
% Cell array is sorted by the distance value in each row.
% Begin by inserting the corners ('c' is for corner).
cw = {{[-1;-1], 'c', 0}, {[-1; 1], 'c', 2}, ...
    {[ 1; 1], 'c', 4}, {[ 1;-1], 'c', 6}};

% Now add intersections of f with sides of box.
x = 2*ones(2,4); % initialize to points outside the box
try
    x(:,1) = intersect(wf, [1,0, 1]');
    x(:,3) = intersect(wf, [1,0,-1]');
catch ME % If we get here, f is (nearly) vertical. Ignore exception.
end
try
    x(:,2) = intersect(wf, [0,1,-1]');
    x(:,4) = intersect(wf, [0,1, 1]');
catch ME % If we get here, f is (nearly) horizontal. Ignore exception.
end
% Remove any duplicates. They will be in consecutive array locations.
for i=1:4
    if x(:,i)==x(:,mod(i,4)+1)
        x(:,i) = [2;2]; % point outside the box
    end
end
nf = 0;
for p=x
    if inpolygon(p(1),p(2),xbox,ybox)
        cw = add(cw,p,'f',CWdist(p));
        nf = nf+1;
    end
end
%{
if nf < 2
    throw(MException('symDiffPerceptrons:badf', 'f does not pass through box'));
end
%}

% And finally add intersections of g with sides of box.
x = 2*ones(2,4);
try
    x(:,1) = intersect(wg, [1,0, 1]');
    x(:,3) = intersect(wg, [1,0,-1]');
catch ME %#ok<*NASGU>
end
try
    x(:,2) = intersect(wg, [0,1,-1]');
    x(:,4) = intersect(wg, [0,1, 1]');
catch ME
end
% Remove any duplicates. They will be in consecutive array locations.
for i=1:4
    if x(:,i)==x(:,mod(i,4)+1)
        x(:,i) = [2;2]; % point outside the box
    end
end
ng = 0;
for p=x
    if inpolygon(p(1),p(2),xbox,ybox)
        cw = add(cw,p,'g',CWdist(p));
        ng = ng+1;
    end
end

% If neither line passes through box, area is determined by whether or
% not the functions agree on the origin
if nf<2 && ng<2
    area = 0;
    if f(0,0) ~= g(0,0)
        area = 4;
    end
    
    % If nf does not pass through box, switch f and g so that
    % we can then assume f does pass through and handle two cases of g.
else
    if nf<2
        tfunc = f;
        f = g;
        g = tfunc;
        tw = wf;
        wf = wg;
        wg = tw;
        tn = nf;
        nf = ng;
        ng = tn;
        for i=1:length(cw)
            if cw{i}{2} == 'f'
                cw{i}{2} = 'g';
            elseif cw{i}{2} == 'g'
                cw{i}{2} = 'f';
            end
        end
    end
    
    
    % If g does not pass through box, find area defined by f, possibly
    % complemented.
    if ng<2
        poly = [];
        ind = findLabel(cw,'f',1);
        [poly, ~] = addpoints(poly, cw, ind, 'f');
        poly = [poly cw{ind}{1}];
        area = polyarea(poly(1,:),poly(2,:));
        
        % If area is 0, f surface coincides with an edge of the box.
        % Use origin to determine whether or not to complement area.
        if area == 0
            if f(0,0) ~= g(0,0)
                area = 4;
            end
            
            % Otherwise, if label of this poly agrees with label from g,
            % symmetric difference is complementary area.
            % There will be a corner following the first f.
            % Use it to determine label assigned by f to poly.
            % Use origin to determine label assigned by g to box (and poly).
        else
            corner = cw{ind+1}{1};
            if f(corner(1),corner(2)) == g(0,0)
                area = 4-area;
            end
        end
        
        % Typical processing: f and g both pass through box
    else
        
        % Check for sharing of points between f and g.
        [shareType, cw] = checkShareType(cw);
        switch shareType
            
            % Coincident lines. Use dot prod to determine whether
            % perfect agreement or perfect disagreement
            case 1 
                if dot(wf,wg) > 0
                    area = 0;
                else
                    area = 4;
                end
                
            % Segments share a single point labeled d.
            case 2
                % Compute area of "wedge" involving d, f, g, and
                % edges between f and g.
                indd = findLabel(cw,'d',1);
                indf = findLabel(cw,'f',1);
                indg = findLabel(cw,'g',1);
                % determine whether f or g is first in clockwise order
                % beginning at d
                if (indd<indf && indd<indg) || (indd>indf && indd>indg)
                     if indf<indg
                        ind1=indf;
                        stopLabel='g';
                    else
                        ind1=indg;
                        stopLabel='f';
                     end
                elseif indd<indf
                     ind1=indf;
                     stopLabel='g';
                else
                     ind1=indg;
                     stopLabel='f';
                end
                % create wedge polygon beginning at d
                poly = cw{indd}{1};
                [poly, ~] = addpoints(poly, cw, ind1, stopLabel);
                area = polyarea(poly(1,:),poly(2,:));
                % Use midpoint between f and g points to test
                % whether wedge is area of agreement.
                xmid = 0.5*(cw{indf}{1} + cw{indg}{1});
                if f(xmid(1),xmid(2)) == g(xmid(1),xmid(2))
                    area = 4-area;
                end
                    
            % Segments share one interior point
            case 3
                % Find intersection point of surfaces of f and g
                coincident = false;
                try
                    c = intersect(wf,wg);
                catch ME
                    % Should not happen, but just in case there's a
                    % numerical problem finding intersection let's flag this.
                    coincident = true;
                end
                if coincident % use area -4 to signal error
                    area = -4;
                else
                    % Find areas of both polygons from center to f point 
                    % to g point to center.
                    poly = c;
                    ind = findLabel(cw,'f',1);
                    [poly, ind] = addpoints(poly, cw, ind, 'g');
                    poly = [poly c];
                    area = polyarea(poly(1,:), poly(2,:));
                    poly = c;
                    ind = findLabel(cw,'f',ind);
                    [poly, ~] = addpoints(poly, cw, ind, 'g');
                    poly = [poly c];
                    area = area + polyarea(poly(1,:), poly(2,:));
                    
                    % Complement area if we computed the "wrong" one.
                    % Can tell if we computed symmetric difference area by
                    % checking how f and g label the midpoint of segment between
                    % the f and g points of a polygon.  The midpoint will be in the
                    % area computed by convexity.
                    % A point x in the symmetric difference area will have opposite
                    % signs for f(x) and g(x).  So complement area if signs are equal.
                    xf = poly(:,2);
                    xg = poly(:,end-1);
                    xmid = 0.5*(xf+xg);
                    if f(xmid(1),xmid(2)) == g(xmid(1),xmid(2))
                        area = 4-area;
                    end
                    
                end
                
                % No point in common
            case 4
                                % Starting at first f point, search for subsequent f and g points
                len = length(cw);
                indf1 = findLabel(cw,'f',1);
                indf2 = findLabel(cw,'f',indf1+1);
                indg1 = findLabel(cw,'g',indf1+1);
                indg2 = findLabel(cw,'g',mod(indg1,len)+1);
                
                % If g1 point comes before f2, find area of f1,g1,g2,f2 polygon.
                if mod(indg1-indf1,len) < mod(indf2-indf1,len)
                    poly = [];
                    [poly, ~] = addpoints(poly, cw, indf1, 'g');
                    [poly, ~] = addpoints(poly, cw, indg2, 'f');
                    poly = [poly cw{indf1}{1}];
                    area = polyarea(poly(1,:),poly(2,:));
                    
                    % Otherwise, find area of f2,g1,g2,f1 polygon.
                else
                    poly = [];
                    [poly, ~] = addpoints(poly, cw, indf2, 'g');
                    [poly, ~] = addpoints(poly, cw, indg2, 'f');
                    poly = [poly cw{indf2}{1}];
                    area = polyarea(poly(1,:),poly(2,:));
                end
                
                % As above, test midpoint and adjust area accordingly.
                % Can use any f/g combination to obtain an appropriate midpoint.
                xf = cw{indf1}{1};
                xg = cw{indg1}{1};
                xmid = 0.5*(xf+xg);
                if f(xmid(1),xmid(2)) == g(xmid(1),xmid(2))
                    area = 4-area;
                end
 
        end
        
    end
end

end

function d = CWdist(x)
% CWdist Compute the clockwise distance of point x on the box
%        starting from (-1,-1).

if x(1) == -1
    d = 0 + 1+x(2);
elseif x(2) == 1
    d = 2 + 1+x(1);
elseif x(1) == 1
    d = 4 + 1-x(2);
else
    d = 6 + 1-x(1);
end
end

function x = intersect(w1, w2)
% intersect Find intersection of two lines represented by weight vectors.
A = [w1(1),w1(2); w2(1),w2(2)];
test = rcond(A);
if isnan(test) || (test < 1e-12)
    throw(MException('intersect:conditioning', 'cannot find intersection'));
end
b = [-w1(end); -w2(end)];
x = A\b;
end

function newcw = add(cw, p, label, dist)
% add Add point p to the appropriate location in the cw array.
i = 1;
while (i<=length(cw)) && (dist>cw{i}{end})
    i = i+1;
end
newcw = [cw(1:i-1) {{p, label, dist}} cw(i:end)];
end

function ind = findLabel(cw, label, start)
% findLabel Find the specified label in cw beginning at specified start.
%   Wrap around cw if end reached.
ind = start;
while cw{ind}{2} ~= label
    ind = mod(ind,length(cw))+1;
end
end

function [newpoly,ind] = addpoints(poly, cw, start, label)
% addpoints Add points of cw to poly from given start to later point with
% given label, wrapping in cw as needed.
% ind is the index of the last point added.
ind = start;
len = length(cw);
newpoly = [poly cw{ind}{1}];
ind = mod(ind,len)+1;
while cw{ind}{2} ~= label
    newpoly = [newpoly cw{ind}{1}]; %#ok<AGROW>
    ind = mod(ind,len)+1;
end
newpoly = [newpoly cw{ind}{1}];
end

%{
function areCoincident = coincident(wf,wg)
% coincident Return true if vectors are (nearly) coincident.
%  Determine this by computing cos of angle between, accepting 1 or -1.
costheta = abs(dot(wf,wg))/(norm(wf)*norm(wg));
areCoincident = abs(costheta-1) < 1e-7;
end
%}

        % Cases:
        % 1) Coincident line segments (f and g share two side points)
        % 2) Line segments share one point on a side
        % 3) Line segments share a point interior to the box, off sides
        % 4) Line segments share no point
        % The cw array can tell us which case we're in.
        % Check adjacent f/g pairs for duplication (cases 1 and 2; 
        %   dup will be removed and single point labeled 'd').
        % Otherwise, check for fgfg or gfgf (indicates case 3).
function [shareType, newcw] = checkShareType(cw)
    % First, check for duplicate f/g points and replace with label d
    ndup = 0;
    newcw = cw;
    for i=1:length(cw)-1
        if cw{i}{3} == cw{i+1}{3}
            if cw{i}{2}=='f' && cw{i+1}{2}=='g' || ...
               cw{i}{2}=='g' && cw{i+1}{2}=='f'
                ndup = ndup + 1;
                if ndup<2 % if ndup is 2, we don't be using cw, so...
                    newcw = [cw(1:i-1) ...
                             {{cw{i}{1}, 'd', cw{i}{3}}} ...
                             cw(i+2:end)];
                end
            end
        end
    end
    if ndup==2
        shareType = 1;
    elseif ndup==1
        shareType = 2;
    else
        indf1 = findLabel(cw, 'f', 1);
        indg1 = findLabel(cw, 'g', 1);
        indf2 = findLabel(cw, 'f', indf1+1);
        indg2 = findLabel(cw, 'g', indg1+1);
        if (indf1<indg1 && indg1<indf2 && indf2<indg2) || ...
           (indg1<indf1 && indf1<indg2 && indg2<indf2)
            shareType = 3;
        else
            shareType = 4;
        end
    end
end