CC = g++
CFLAGS = -g -Wall -std=c++0x
EXEC_NAME = main
INCLUDES = .
LIBS =
OBJ_FILES = Cell.o CrossRoads.o CrossRoadsCell.o Road.o RoundAbout.o RoundAboutCell.o main.o
INSTALL_DIR = .

all : $(EXEC_NAME)

clean :
	rm $(EXEC_NAME) $(OBJ_FILES)

$(EXEC_NAME) : $(OBJ_FILES)
	$(CC) -o $(EXEC_NAME) $(OBJ_FILES) $(LIBS)

%.o: %.cpp
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $<

%.o: %.cc
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $<

%.o: %.c
	gcc $(CFLAGS) $(INCLUDES) -o $@ -c $<

install :
	cp $(EXEC_NAME) $(INSTALL_DIR)
