# Parallel-A-Star
A parallelized A* search, done for Seattle University's Parallel Computing class, WI Quarter 2022.

Collaborators: Anh Tran, Peter Loyd, Ulysses Lin

There are three A Star methods we explored: 1) Sequential, 2) ParallelPriorityQueue, and 3) Bidirectional - the last two being parallel.

## Running Animations of A Star Methods
Open any of the following files and run their main methods individually. After an animation of the maze and the shortest path completes, you may need to force quit the file running to then execute the next file(s).
* Sequential.java
* ParallelPriorityQueue.java
* Bidirectional.java

## Running Metrics
We prepared tests to time finding the shortest path using Sequential, ParallelPriorityQueue, and Bidirectional for three maze sizes (DIM = 20, 200, 500).
Sequential and Bidirectional will do 20 tests per DIM, while ParallelPriorityQueue does 20 tests per thread count (1, 2, 4, 8) per DIM.

Running metrics does not induce animation. To run the metrics, follow the following steps:
// TODO