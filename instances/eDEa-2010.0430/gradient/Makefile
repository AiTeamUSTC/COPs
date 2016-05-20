CFLAGS=-g -I.. -DTEST -DCEC2010
SRCS=operation.c gsolve-gsl.o
FCNSUITE=../CEC2010/dll.o
LIB=../constraints.o ../problem.o ../nabla.o ../new.o ../lib/rand.o $(FCNSUITE)

gradient: $(SRCS) $(LIB)
	$(CC) $(CFLAGS) -o gradient $(SRCS) $(LIB) -lgsl

clean:
	rm -f *.o *.bak
