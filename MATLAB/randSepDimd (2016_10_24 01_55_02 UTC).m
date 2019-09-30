function w = randSepDimd( d )
%randSepDimd Generate a random perceptron in d-dimensional space.
%   The w0 weight will be in element d+1 of w.
%   The hyperplane will pass through the (-1,-1,...,-1) to (1,1,...,1) box.
%   Approach: choose d random points x in the box.
%   Randomly choose bias w0 as +1 or -1 (direction of positives).
%   Solve system v'*x = -w0, where v is w1 through wd.

  X = randX(d,d);
  w0 = 2*randi(2)-3;
  b = -w0*ones(d,1);
  v = X\b; % I'm assuming this will have a solution.  Not sure what
           % MATLAB does if it doesn't have a solution...
  w = [v; w0];
end

