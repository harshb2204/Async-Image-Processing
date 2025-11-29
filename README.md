# AsyncImageProcessing

A JavaFX-based image processing application that demonstrates asynchronous (multi-threaded) image processing using Java's `ExecutorService`, `Callable`, and `Future` APIs. The application divides large images into tiles and processes them in parallel using a thread pool.

## Features

- **Parallel Image Processing**: Divides images into smaller tiles and processes them concurrently
- **Thread Pool Management**: Uses a fixed thread pool (100 threads) for efficient resource management
- **Real-time Rendering**: Displays processed image tiles on a JavaFX canvas as they complete
- **Extensible Filter System**: Implements a filter interface for easy addition of new image filters
- **Asynchronous Task Execution**: Uses `Callable` and `Future` for handling concurrent image processing tasks

## Technologies Used

- **Java 21**: Modern Java features including virtual threads support
- **JavaFX 21**: For the graphical user interface
- **Maven**: Build and dependency management
- **AWT/Swing**: For image manipulation and processing

## Project Structure

```
AsyncImageProcessing/
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── harsh/
│                   └── asyncimageprocessing/
│                       ├── HelloApplication.java      # Main application entry point
│                       ├── processor/
│                       │   └── ImageProcessor.java    # Core image processing logic
│                       ├── filter/
│                       │   ├── ImageFilter.java       # Filter interface
│                       │   └── GreyScaleFilter.java  # Grayscale filter implementation
│                       ├── image/
│                       │   ├── ImageData.java         # Data class for processed tiles
│                       │   └── DrawMultipleImagesOnCanvas.java  # Canvas rendering
│                       └── io/
│                           ├── ImageIOInterface.java  # I/O interface
│                           └── FileImageIO.java       # File I/O implementation
├── docs/
│   └── concepts.md          # Threading concepts documentation
├── pom.xml                  # Maven configuration
└── README.md
```

## Architecture Overview

### Core Components

1. **ImageProcessor**: 
   - Divides images into tiles
   - Submits processing tasks to a thread pool
   - Manages `Future<ImageData>` objects for each tile
   - Collects results and passes them to the renderer

2. **ImageFilter Interface**:
   - Defines the contract for image filters
   - Allows easy extension with new filter types
   - Current implementation: `GreyScaleFilter`

3. **DrawMultipleImagesOnCanvas**:
   - Singleton pattern for canvas management
   - Uses a queue to receive processed image tiles
   - Renders tiles on JavaFX canvas as they become available
   - Uses `AnimationTimer` for continuous rendering

4. **ImageData**:
   - Data transfer object containing processed image and position information
   - Used to reconstruct the full image from tiles

### Threading Model

The application uses a **pseudo-asynchronous** approach:

- **CPU-bound tasks** (pixel manipulation): Handled by a fixed thread pool
- **I/O-bound tasks** (image division): Leverages Java 21's virtual threads
- **Thread Pool**: 100 worker threads created upfront for efficient task execution
- **Task Submission**: Each tile is processed as a `Callable<ImageData>`
- **Result Retrieval**: Uses `Future.get()` to retrieve processed tiles

### Architecture Diagram

![](/diagrams/architecture.png)

## Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use the included Maven Wrapper)
- An image file to process (currently hardcoded path in `HelloApplication.java`)

## Building and Running

### Using Maven Wrapper

**Build the project:**
```bash
./mvnw clean compile
```

**Run the application:**
```bash
./mvnw javafx:run
```

### Using Maven (if installed)

**Build the project:**
```bash
mvn clean compile
```

**Run the application:**
```bash
mvn javafx:run
```

### Configuration

Before running, update the image path in `HelloApplication.java`:

```java
BufferedImage image = imageIO.readImage("path/to/your/image.jpg");
```

## How It Works

1. **Image Loading**: The application loads an image from the specified file path
2. **Image Division**: The image is divided into tiles of a specified size (e.g., 10x10 pixels)
3. **Parallel Processing**: Each tile is submitted as a `Callable` task to the thread pool
4. **Filter Application**: Each tile is processed independently with the selected filter (e.g., grayscale)
5. **Result Collection**: `Future<ImageData>` objects are collected for each tile
6. **Rendering**: Processed tiles are queued and rendered on the JavaFX canvas as they complete
7. **Display**: The final processed image is displayed tile by tile in real-time




## Key Concepts

This project demonstrates several important Java concurrency concepts:

- **Runnable vs Callable**: Why `Callable` is needed when you need return values
- **Future**: How to retrieve results from asynchronous tasks
- **ExecutorService**: Managing thread pools efficiently
- **Thread Safety**: Using queues and proper synchronization

For detailed explanations, see [docs/concepts.md](docs/concepts.md).

## Extending the Project

### Adding New Filters

1. Implement the `ImageFilter` interface:
```java
public class BlurFilter implements ImageFilter {
    @Override
    public BufferedImage filter(BufferedImage image) {
        // Your filter logic here
        return processedImage;
    }
}
```

2. Use it in `HelloApplication.java`:
```java
ImageFilter imageFilter = new BlurFilter();
processor.processImage(image, 10, imageFilter, drawMultipleImagesOnCanvas);
```

### Adjusting Thread Pool Size

Modify the thread pool size in `ImageProcessor.java`:
```java
executorService = Executors.newFixedThreadPool(50); // Change from 100 to 50
```

### Changing Tile Size

Adjust the tile size parameter in `HelloApplication.java`:
```java
processor.processImage(image, 20, imageFilter, drawMultipleImagesOnCanvas); // 20x20 tiles
```

## Limitations and Future Improvements

- **Hardcoded Image Path**: Currently requires manual path update in code
- **Fixed Thread Pool Size**: Could be made configurable
- **No Progress Indicator**: Could add progress bar for large images
- **Limited Filter Options**: Only grayscale filter implemented
- **Error Handling**: Could be more robust with better exception handling
- **Image Format Support**: Currently limited to formats supported by `ImageIO`
.

