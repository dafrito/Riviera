
LOGVIEWER_CLASSES = \
	dist/gui/EditorRunner.class \
	dist/strings/Strings.class \
	dist/logging/TreeLog.class

MAIN_CLASS = gui.EditorRunner
LOGVIEWER=dist/logviewer.jar
LOGPORT=28122

build: $(LOGVIEWER)
.PHONY: build

dist/%.class: src/%.java
	javac -sourcepath src -d dist $<

$(LOGVIEWER): dist $(LOGVIEWER_CLASSES)
	cd `dirname $@` && find . -name '*.class' | xargs jar cfe `basename $@` $(MAIN_CLASS)

dist:
	mkdir -p dist
	echo "*" >dist/.gitignore

doc:
	cd src && javadoc -link 'https://fritocomp.aaronfaanes/riviera/' --source-path src -d ../dist/doc `find . -name '*.java' | grep -v examples/opengl | grep -v '^./tests' | grep -v '^./opengl'`
.PHONY: doc

jar: $(LOGVIEWER)
.PHONY: jar

run: $(LOGVIEWER)
	java -jar $< $(LOGPORT)

clean:
	rm -rf dist
.PHONY: clean
