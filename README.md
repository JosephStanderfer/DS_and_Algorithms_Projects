# Data Structures and Algorithms Projects
Contains 6 Java projects from my Data Structures and Algorithms class at Carnegie Mellon (Spring 2019). These projects include:

* P1 Merkle-Hellman Knapsack Cryptosystem
		*	Uses my implementation of the Merkel-Hellman Knapsack algorithm to encrypt/decrypt files. The files are decrypted using a private key – a super increasing sequence of BigIntegers. The files are encrypted using a modified (scrambled) version of the private key, by taking a summation of each index and matching file bit. The encrypted file cannot be decrypted in less than polynomial time without knowing the private key.

* P1 Merkle Tree
	Creates a hash-tree where every node in the tree is the cryptographic hash of its children. Used for efficiently verifying the integrity of large data structures. 

* P2 Red-Black Tree Spell Checker
	Implements a type of self-balancing binary tree called a Red-Black tree to create an efficient file spell checker. The program reads and stores a word dictionary within the Red-Black tree, then the user is prompted for input. The user can either choose to look-up a word, spell check a file, add a word to the dictionary, or retrieve a level order print-off of the tree. The project also includes a Queue class implementation (stored within an array), for tree traversals. The worst case time complexity for each dictionary lookup is 2 * Lg( n+1). See pictures within project folder to see how program operates

* P2 World Series Odds
	This program uses dynamic programming to reduce the time complexity of a recursive algorithm.

* P3 Stacks, Red Black Trees and Reverse Polish Notation Calculator
	The calculator created by the program computes the results of an equation written in reverse polish notation, arguments before operators (see details in project folder). The program uses my implementations of a dynamic stack and queue to store/feed the equation passed through the console. It also uses my implementation of a red-black tree to store variables and their values as declared by the user.

* P4 Traveling Salesman Problem - Graph Searching Algorithms
	Reads in a list of crimes committed in Pittsburgh in 1990 and their locations. Prompts the user for a range of dates and constructs a graph of the crimes committed within that date range. Each crime is represented as a node and their distances from one another as graph edges. The program then uses a couple graph searching algorithms, including the approx-TSP-Tour algorithm, to efficiently find a minimal spanning tree. The minimal spanning tree is the shortest possible route to reach all the crimes. The program then outputs the route and total distance. A KML file is also created for the route to be viewed on Google Maps.

* P5 Lempel-Ziv Welch File Compression
	Uses my implementation of the LZW 12-bit compression algorithm to compress and decompress files. The algorithm works by collecting unique sequences of bytes and storing them in a HashMap (also my implementation) under a 12-bit key. The codes are output into the zipped file when they are found. The files are decrypted using a similar method, but with each sequence of bytes (uncompressed) being stored in an array at the index of the 12 bit key. The files to use are designated by the user through the console.

* P6 Turing Machines
	Creates 3 Turing Machines to perform a series of tasks. All the transitions are stored in a Red-Black tree for each state.
