# define a makefile variable for the java compiler
#
JCC = javac

# define a makefile variable for compilation flags
# the -g flag compiles with debugging information
#
JFLAGS = -g

.SUFFIXES: .java .class
.java.class:
	$(JCC) $(JFLAGS) $*.java

CLASSES = \
	Bag.java \
	Item.java \
	State.java \
	Solution.java 

default: classes

classes: $(CLASSES:.java=.class)

.run:
	java Solution

clean:
	$(RM) *.class
