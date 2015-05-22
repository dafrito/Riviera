LOGVIEWER_CLASSES = \
	build/gui/EditorRunner.class \
	build/strings/Strings.class \
	build/logging/TreeLog.class

build/%.class: src/%.java
	javac -sourcepath src -d build $<

MAIN_CLASS = gui.EditorRunner
LOGVIEWER=build/logviewer.jar
LOGPORT=28122

$(LOGVIEWER): build $(LOGVIEWER_CLASSES)
	cd `dirname $@` && find . -name '*.class' | xargs jar cfe `basename $@` $(MAIN_CLASS)

build:
	mkdir -p build
	echo "*" >build/.gitignore

jar: $(LOGVIEWER)
.PHONY: jar

run: $(LOGVIEWER)
	java -jar $< $(LOGPORT)

clean:
	rm -rf build
.PHONY: clean
