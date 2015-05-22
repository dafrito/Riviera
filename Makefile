SOURCES = \
	build/gui/EditorRunner.class \
	build/strings/Strings.class \
	build/logging/TreeLog.class

build/%.class: src/%.java
	mkdir -p build
	javac -sourcepath src -d build $<

run: $(SOURCES)
	java -cp build gui.EditorRunner

clean:
	rm -rf build
.PHONY: clean
