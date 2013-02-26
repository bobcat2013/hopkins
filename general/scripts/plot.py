#!/usr/bin/python

from pylab import *

x=arange(-2,2,0.01)
y1=x*x+2
y1prime = 2*x

y2=.25*x*x*x*x-(4.0/3.0)*x*x*x+2*x*x
y2prime = x*x*x - 4*x*x + 4*x

plot(x,y1)
xlabel('x')
ylabel('y')
title(r'$y=x^{2}+2$')
show()
plot(x,y1prime)
xlabel('x')
ylabel('y')
title(r'$y^\prime=2x$')
show()


plot(x,y2)
xlabel('x')
ylabel('y')
title(r'$y=x^{4}/4-4x^{2}/3+2x^{2}$')
show()
plot(x,y2prime)
xlabel('x')
ylabel('y')
title(r'$y^\prime=x^{3}-4x^{2}+4x$')
show()
