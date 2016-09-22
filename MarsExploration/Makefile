APP=ernie.ripley.App
SRC:=$(shell find src -regex '[^._].*\.java$$')

all : build/app.jar

build/app.jar : $(SRC)
	if [ ! -d build/classes ] ; then mkdir -p build/classes ; fi
	cd build/classes; for jar in ../../lib/*.jar; do jar xf $$jar ; done
	javac -cp "build/classes" -d "build/classes" -s "src" $(SRC)
	cd build/classes; jar cfe ../app.jar kiss.util.Run .

run : build/app.jar
	java -jar build/app.jar --app $(APP)

.PHONY: test
test: 
	java -jar build/app.jar --norun --app $(APP)

.PHONY: clean
clean:
	/bin/rm -rf build/*
	/bin/rm -rf $$(find . -name '*~' -o -name '._*' -o -name '#*')
