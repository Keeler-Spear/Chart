import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Base code is from DeepSeek converting my Quiz 1 into Java. I then generalized it.
public class PyGraph {

    //ToDo: Throw in some exception checks
    //x and y should be 1xn matrices.
    public static void contour(Matrix x, Matrix y, Matrix z, Matrix min, String xLabel, String yLabel, String title) {
        try {
            // Create a temporary Python script
            File pythonScript = File.createTempFile("contour_plot", ".py");
            pythonScript.deleteOnExit();

            String xLim = String.valueOf(x.getValue(1, 1));
            String xMax = String.valueOf(x.getValue(1, x.getCols()));
            String yLim = String.valueOf(y.getValue(1, 1));
            String yMax = String.valueOf(y.getValue(1, y.getCols()));
            String bMin = String.valueOf(min.getValue(1, 1));
            String wMin = String.valueOf(min.getValue(2, 1));

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("y = np.array(" + y.npString() + ")");
                out.println("X, Y = np.meshgrid(x, y)");
                out.println("Z = np.array(" + z.npString() + ")");
                //Printing the contour
                out.println("plt.contourf(X, Y, Z, levels=30, alpha=0.5, cmap='jet')");
                //Marking the minimum value
                out.println("plt.plot(" + bMin + ", " + wMin + ", marker='X', markersize=12, markeredgewidth=2, color='blue')");
                //Setting x and y limits
                out.println("plt.xlim(" + xLim + ", " + xMax + ")");
                out.println("plt.ylim(" + yLim + ", " + yMax + ")");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.show()");
            }

            // Run the Python script
            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO(); // Display Python output
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //ToDo: Throw in some exception checks
    //x and y should be 1xn matrices.
    public static void contourWithTracking(Matrix x, Matrix y, Matrix z, Matrix xT, Matrix yT, Matrix min, String xLabel, String yLabel, String title) {
        try {
            // Create a temporary Python script
            File pythonScript = File.createTempFile("contour_plot", ".py");
            pythonScript.deleteOnExit();

            String xLim = String.valueOf(x.getValue(1, 1));
            String xMax = String.valueOf(x.getValue(1, x.getCols()));
            String yLim = String.valueOf(y.getValue(1, 1));
            String yMax = String.valueOf(y.getValue(1, y.getCols()));
            String bMin = String.valueOf(min.getValue(1, 1));
            String wMin = String.valueOf(min.getValue(2, 1));

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("y = np.array(" + y.npString() + ")");
                out.println("xT = np.array(" + xT.npString() + ")");
                out.println("yT = np.array(" + yT.npString() + ")");
                out.println("X, Y = np.meshgrid(x, y)");
                out.println("Z = np.array(" + z.npString() + ")");
                //Printing the contour
                out.println("plt.contourf(X, Y, Z, levels=30, alpha=0.5, cmap='jet')");
                //Marking the minimum value and path of (xT, yT)
                out.println("plt.plot(" + bMin + ", " + wMin + ", marker='X', markersize=12, markeredgewidth=2, color='blue')");
                out.println("plt.plot(xT, yT, 'o-', ms=3, lw=1.5,color='black')");
                //Setting x and y limits
                out.println("plt.xlim(" + xLim + ", " + xMax + ")");
                out.println("plt.ylim(" + yLim + ", " + yMax + ")");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.show()");
            }

            // Run the Python script
            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO(); // Display Python output
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}