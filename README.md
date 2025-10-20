# Maze Generator

A Java-based CLI application for visualizing maze generation and solving algorithms in real-time, directly in your terminal. This project was developed as an exercise to explore graph theory, advanced algorithms, and clean terminal rendering in Java.

### Core Features

-   **Real-time Visualization:** Watch algorithms like Randomized Depth-First Search build the maze step-by-step.
-   **Clean CLI Rendering:** Uses Unicode box-drawing and block characters for a modern and clear visual representation.
-   **Extensible by Design:** The underlying graph structure allows for easy implementation of new generation and solving algorithms.
-   **Pure Java:** Built with standard Java libraries to focus on core language concepts and algorithms.

## Preview
![preview](./docs/generator-preview.gif)

## How it Works

The project is divided into three main components:

### CLI Management
The `src/libraries/cli` directory contains all the necessary classes to build and manage the command-line interface. This module is responsible for:
- Rendering the maze and other UI components in the terminal.
- Handling user input.
- Styling the CLI for a better user experience.

### Graph Representation
The core data structure of the application resides in the `src/libraries/graph` directory. This module handles:
- The representation of the maze as a graph.
- Nodes and Edges of the graph.
- The underlying data structures for storing the graph.

### Maze Generation
The `src/libraries/maze` directory contains the logic for generating the maze. This module handles:
- The implementation of the Randomized Depth-First Search algorithm.
- The creation of the maze structure.
- The visualization of the maze generation process.

## Class Map
```
.
├── run.sh
├── src
│   ├── Main.java
│   └── libraries
│       ├── cli
│       │   ├── CLI.java
│       │   └── CLIStyle.java
│       ├── graph
│       │   ├── Edge.java
│       │   ├── Graph.java
│       │   └── Node.java
│       └── maze
│           ├── Maze.java
│           └── data
│               ├── EdgeData.java
│               └── NodeData.java
└── README.md
```

## Class Diagram
```mermaid
classDiagram
    class Main {
        +main(args) void
    }

    class CLI {
        -scanner Scanner
        +out(message, newline) void
        +out(message) void
        +out(builder) void
        +input(prompt, new_line) String
        +input(prompt) String
        +inputNum(prompt) int
        +inputFlt(prompt) double
        +inputBool(prompt, def) boolean
        +inputDate(prompt, nullable) LocalDate
        +inputDate(prompt) LocalDate
        +getDimensions() int[]
        +getWidth() int
        +getHeight() int
        +clear() void
        +stripAnsiCodes(str) String
        +getVisibleLength(str) int
    }

    class CLIStyle {
        &lt;&lt;enumeration&gt;&gt;
        RESET
        BLACK
        RED
        GREEN
        YELLOW
        BLUE
        MAGENTA
        CYAN
        WHITE
        BOLD
        UNDERLINE
        ITALIC
        -code String
        +toString() String
        +apply(text, styles) String
    }

    class Graph {
        -graph Map~Node, List~Edge~~
        -nodeSupplier Supplier~N~
        -edgeSupplier Supplier~E~
        +Graph(nodeSupplier, edgeSupplier)
        +Graph(builder)
        +addNode(node) Node~N~
        +addNode(data) Node~N~
        +addNode() Node~N~
        +removeNode(node) void
        +getNeighbors(node) List~Node~N~~
        +getEdges(node) List~Edge~E,N~~
        +getEdges() List~Edge~E,N~~
        +getLinkEdge(node1, node2) Optional~Edge~E,N~~
        +areAdjacent(node1, node2) boolean
        +getNodes() Set~Node~N~~
        +addEdge(edge) Edge~E,N~
        +addEdge(node1, node2, data) Edge~E,N~
        +linkNodes(node1, node2) Edge~E,N~
        +removeEdge(edge) void
    }

    class Builder {
        -nodeSupplier Supplier~N~
        -edgeSupplier Supplier~E~
        +withNodeSupplier(supplier) Builder~N,E~
        +withEdgeSupplier(supplier) Builder~N,E~
        +build() Graph~N,E~
    }

    class Node {
        -value T
        +Node(value)
        +getValue() T
        +setValue(value) void
    }

    class Edge {
        -value T
        -node1 Node~N~
        -node2 Node~N~
        +Edge(node1, node2, value)
        +getValue() T
        +setValue(value) void
        +getNode1() Node~N~
        +setNode1(node) void
        +getNode2() Node~N~
        +setNode2(node) void
    }

    class Maze {
        -CPS int
        -NORTH int
        -SOUTH int
        -EAST int
        -WEST int
        -JUNCTION_CHARS char[]
        -width int
        -height int
        -state String
        -graph Graph~NodeData,EdgeData~
        -visualizationMatrix List~List~Node~NodeData~~~
        +Maze(width, height)
        +generateWithRDS() void
        +show(style) void
        +show() void
        +createEntryPoints() void
        -generateMazeBody(builder, style) void
        -generateMazeBottomRow(builder, style) void
        -generateMazeTopRow(builder, style) void
        -getStartIndex() int
        -getEndIndex() int
        -initializeEmptyMaze() void
        -getJunctionChar(n, s, e, w) char
        +getState() String
        +setState(state) void
        +getWidth() int
        +getHeight() int
    }

    class NodeData {
        +STYLE_DEF String
        +STYLE_START String
        +STYLE_END String
        +STYLE_RDS_VIS String
        +STYLE_RDS_TRL String
        +STYLE_RDS_HEAD String
        -start boolean
        -end boolean
        -x int
        -y int
        -rdsVisited boolean
        -rdsTrail boolean
        -rdsHead boolean
        +NodeData(x, y)
        +toString() String
        +toString(style) String
        +setStart(start) void
        +isStart() boolean
        +isEnd() boolean
        +setEnd(end) void
        +getX() int
        +setX(x) void
        +getY() int
        +setY(y) void
        +isRDSTrail() boolean
        +setRDSTrail(trail) void
        +isRDSVisited() boolean
        +setRDSVisited(visited) void
        +isRDSHead() boolean
        +setRDSHead(head) void
    }

    class EdgeData {
        -wall boolean
        +isWall() boolean
        +setWall(wall) void
    }

    Main --> CLI
    Main --> Maze
    CLI --> CLIStyle
    Maze --> Graph
    Maze --> NodeData
    Maze --> EdgeData
    Maze --> CLI
    Maze --> CLIStyle
    Graph --> Node
    Graph --> Edge
    Graph ..> Builder
    Edge --> Node
    Node --> NodeData
    Edge --> EdgeData
```

## How to Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/Maze-Generator.git
    cd Maze-Generator
    ```

2.  **Run the application:**
    Use the provided shell script to compile and run the project.
    ```bash
    ./run.sh
    ```

3.  **Run in Debug Mode:**
    To start the application in debug mode and have it wait for a debugger to attach on port 5005, run:
    ```bash
    ./run.sh debug
    ```
---
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.