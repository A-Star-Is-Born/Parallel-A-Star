# Parallel-A-Star
A parallelized A* search, done for Seattle University's Parallel Computing class, WI Quarter 2022.

Collaborators: Anh Tran, Peter Loyd, Ulysses Lin

NOTE: to run our files, you must have the algs4.jar file working. To do so in VSCode:
In the left side of the window, expand JAVA PROJECTS. Look for Referenced Libraries. Click the "+". Select algs4.jar which we have included.
You may also watch this YouTube tutorial on how to do so: https://www.youtube.com/watch?v=3Qm54znQX2E

There are three A Star methods we explored: 1) Sequential, 2) ParallelPriorityQueue, and 3) Bidirectional - the last two being parallel.

## Running Animations of A Star Methods
Open any of the following files and run their main methods individually. After an animation of the maze and the shortest path completes, you may need to force quit the file running to then execute the next file(s).
* Sequential.java
* ParallelPriorityQueue.java
* Bidirectional.java

## Running Metrics
We prepared tests to time finding the shortest path using Sequential, ParallelPriorityQueue, and Bidirectional for three maze sizes (DIM = 20, 200, 500).
Sequential and Bidirectional will do 20 tests per DIM, while ParallelPriorityQueue does 20 tests per thread count (1, 2, 4, 8) per DIM.

Running metrics should be done without graphics. To do so comment out these three lines in the Maze.java constructor:

#
# 57        StdDraw.setXscale(0, n+2);
# 58        StdDraw.setYscale(0, n+2);
# 73        draw();
#

Then run the main method in MetricGathering.java.

NOTE: when you run MetricGathering.java, the 500 DIM tests take a very long time, perhaps more than 15 minutes.