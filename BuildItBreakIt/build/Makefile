JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Atm.java \
	Bank.java \

default: classes
	@rm -f atm bank *.card *.auth
	@echo Main-Class: Atm >> atm.MF
	@jar -cvfm0 atm atm.MF  Atm*.class
	@chmod +x atm
	@echo Main-Class: Bank >> bank.MF
	@jar -cvfm0 bank bank.MF  Bank*.class
	@rm -f *.class *.MF
	@chmod +x bank

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class *.MF atm bank
