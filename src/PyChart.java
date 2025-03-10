import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

public class PyChart {

    //ToDo: Throw in some exception checks
    //x and y should be 1xn matrices.
    //CS 3100 Notes: Base code is from DeepSeek converting my Quiz 1 into Java. I then generalized it.
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
    public static void contourTrack(Matrix x, Matrix y, Matrix z, Matrix xT, Matrix yT, Matrix min, String xLabel, String yLabel, String title) {
        try {
            File pythonScript = File.createTempFile("contour_plot", ".py");
            pythonScript.deleteOnExit();

            String xLim = String.valueOf(x.getValue(1, 1));
            String xMax = String.valueOf(x.getValue(1, x.getCols()));
            String yLim = String.valueOf(y.getValue(1, 1));
            String yMax = String.valueOf(y.getValue(1, y.getCols()));
            String bMin = String.valueOf(min.getValue(1, 1));
            String wMin = String.valueOf(min.getValue(2, 1));

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
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

            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void scatter(Matrix x, Matrix y, String dataName, String xLabel, String yLabel, String title) {
        try {
            File pythonScript = File.createTempFile("scatter_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("y = np.array(" + y.npString() + ")");
                //Printing the scatter plot
                out.println("plt.scatter(x, y, color='blue', s=10, label='" + dataName + "')");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.gca().set_aspect(1.0)");
                out.println("plt.show()");
            }

            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Created on my own, using the others for reference
    public static void scatterWFnc(Matrix x, Matrix y, Matrix xAp, Matrix yAp, String xLabel, String yLabel, String title) {
        try {
            File pythonScript = File.createTempFile("scatter_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("y = np.array(" + y.npString() + ")");
                out.println("xAp = np.array(" + xAp.npString() + ")");
                out.println("yAp = np.array(" + yAp.npString() + ")");
                //Printing the scatter plot
                out.println("plt.scatter(x, y, color='blue', s=10, label='Data Points')");
                out.println("plt.plot(xAp, yAp, color='red', label='Best Fit')");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.legend()");
                out.println("plt.show()");
            }

            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Created on my own, using the others for reference
    public static void fnc(Matrix x, Matrix f, String fnc, String xLabel, String yLabel, String title) {
        try {
            File pythonScript = File.createTempFile("scatter_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("f = np.array(" + f.npString() + ")");
                //Printing the scatter plot
                out.println("plt.plot(x, f, color='blue', label='" + fnc + "')");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.legend()");
                out.println("plt.show()");
            }

            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Created on my own, using the others for reference
    public static void twoFnc(Matrix x, Matrix f1, String fnc1, Matrix f2, String fnc2, String xLabel, String yLabel, String title) {
        try {
            File pythonScript = File.createTempFile("scatter_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("f1 = np.array(" + f1.npString() + ")");
                out.println("f2 = np.array(" + f2.npString() + ")");
                //Printing the scatter plot
                out.println("plt.plot(x, f1, color='blue', label='" + fnc1 + "')");
                out.println("plt.plot(x, f2, color='red', label='" + fnc2 + "')");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.legend()");
                out.println("plt.show()");
            }

            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Same as the above method but it allows the user to pass function objects
    public static void twoFnc(Matrix x, Function fnc1, String fnc1Name, Function fnc2, String fnc2Name, String xLabel, String yLabel, String title) {
        try {
            //Building function data
            Matrix f1 = LinearAlgebra.applyFunction(x, fnc1);
            Matrix f2 = LinearAlgebra.applyFunction(x, fnc2);

            File pythonScript = File.createTempFile("scatter_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("f1 = np.array(" + f1.npString() + ")");
                out.println("f2 = np.array(" + f2.npString() + ")");
                //Printing the scatter plot
                out.println("plt.plot(x, f1, color='blue', label='" + fnc1Name + "')");
                out.println("plt.plot(x, f2, color='red', label='" + fnc2Name + "')");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.legend()");
                out.println("plt.show()");
            }

            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Created on my own, using the others for reference
    public static void threeFnc(Matrix x, Matrix f1, String fnc1, Matrix f2, String fnc2, Matrix f3, String fnc3, String xLabel, String yLabel, String title) {
        try {
            File pythonScript = File.createTempFile("scatter_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                out.println("f1 = np.array(" + f1.npString() + ")");
                out.println("f2 = np.array(" + f2.npString() + ")");
                out.println("f3 = np.array(" + f3.npString() + ")");
                //Printing the scatter plot
                out.println("plt.plot(x, f1, color='blue', label='" + fnc1 + "')");
                out.println("plt.plot(x, f2, color='red', label='" + fnc2 + "')");
                out.println("plt.plot(x, f3, color='green', label='" + fnc3 + "')");
                //Titling chart
                out.println("plt.xlabel('" + xLabel + "')");
                out.println("plt.ylabel('" + yLabel + "')");
                out.println("plt.title('" + title + "')");
                out.println("plt.legend()");
                out.println("plt.show()");
            }

            ProcessBuilder pb = new ProcessBuilder("python", pythonScript.getAbsolutePath());
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}