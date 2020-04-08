/**
 * Construct xorshift pseudo-random number generator.
 * Argument is a seed value.  Only the lower 32 bits of each are used.
 * Reference: http://www.jstatsoft.org/v08/i14/paper 
 */
function xorshift(y) {
  this.y = y;
}
/**
 *  Return a random number in [0,1)
 *  Period of this generator is 2^{32}-1.
 */
xorshift.prototype.random = function() {
  this.y ^= (this.y<<13);
  this.y ^= (this.y>>>17);
  this.y ^= (this.y<<5);
  // Return as real in [0,1)
  // Unsigned right-shift by 0 converts y to unsigned.
  return (this.y >>> 0)/(Math.pow(2,32)-1);
}