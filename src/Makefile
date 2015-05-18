SOURCES = \
	gui/EditorRunner.class \
	strings/Strings.class \
	logging/TreeLog.class

%.class: %.java
	javac $<

run: $(SOURCES)
	java gui.EditorRunner

clean:
	find -name '*.class' -delete
.PHONY: clean
