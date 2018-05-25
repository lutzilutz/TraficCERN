CC = g++
CFLAGS = -g -Wall
EXEC_NAME = main
INCLUDES = .
LIBS =
OBJ_FILES = Cellule.o Route.o RondPoint.o main.o
INSTALL_DIR = .

all : $(EXEC_NAME)

clean :
	rm $(EXEC_NAME) $(OBJ_FILES)

$(EXEC_NAME) : $(OBJ_FILES)
	$(CC) -o $(EXEC_NAME) $(OBJ_FILES) $(LIBS)

%.o: %.cpp
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $< -std=c++0x

%.o: %.cc
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $< -std=c++0x

%.o: %.c
	gcc $(CFLAGS) $(INCLUDES) -o $@ -c $<

install :
	cp $(EXEC_NAME) $(INSTALL_DIR)
