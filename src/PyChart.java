import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

//ToDo: Allow for the user to input a line type
public class PyChart {

    final static String[] colors = {
            "blue", "red", "green", "gold", "gray", "brown", "cyan", "grey", "indigo", "ivory", "lavender", "lime",
            "magenta", "maroon", "navy", "olive", "orange", "pink", "purple", "black", "silver", "teal", "violet", "white",
            "yellow", "aliceblue", "antiquewhite", "aqua", "aquamarine", "azure", "beige", "bisque", "blanchedalmond",
            "blueviolet", "burlywood", "cadetblue", "chartreuse", "chocolate", "coral", "cornflowerblue", "cornsilk",
            "crimson", "darkblue", "darkcyan", "darkgoldenrod", "darkgray", "darkgreen", "darkgrey", "darkkhaki",
            "darkmagenta", "darkolivegreen", "darkorange", "darkorchid", "darkred", "darksalmon", "darkseagreen",
            "darkslateblue", "darkslategray", "darkslategrey", "darkturquoise", "darkviolet", "deeppink", "deepskyblue",
            "dimgray", "dimgrey", "dodgerblue", "firebrick", "floralwhite", "forestgreen", "fuchsia", "gainsboro",
            "ghostwhite", "goldenrod", "greenyellow", "honeydew", "hotpink", "indianred", "khaki", "lavenderblush",
            "lawngreen", "lemonchiffon", "lightblue", "lightcoral", "lightcyan", "lightgoldenrodyellow", "lightgray",
            "lightgreen", "lightgrey", "lightpink", "lightsalmon", "lightseagreen", "lightskyblue", "lightslategray",
            "lightslategrey", "lightsteelblue", "lightyellow", "linen", "mediumaquamarine", "mediumblue", "mediumorchid",
            "mediumpurple", "mediumseagreen", "mediumslateblue", "mediumspringgreen", "mediumturquoise", "mediumvioletred",
            "midnightblue", "mintcream", "mistyrose", "moccasin", "navajowhite", "oldlace", "olivedrab", "orangered",
            "orchid", "palegoldenrod", "palegreen", "paleturquoise", "palevioletred", "papayawhip", "peachpuff", "peru",
            "plum", "powderblue", "rebeccapurple", "rosybrown", "royalblue", "saddlebrown", "salmon", "sandybrown",
            "seagreen", "seashell", "sienna", "skyblue", "slateblue", "slategray", "slategrey", "snow", "springgreen",
            "steelblue", "tan", "thistle", "tomato", "turquoise", "wheat", "whitesmoke", "yellowgreen"
    };

    /**
     * Graphs the provided functions on a plot together.
     *
     * @param x The values at which the functions are evaluated at. This matrix will be used to form the x-axis.
     * @param fncs The set of function outputs to be plotted.
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have a name.
     * @throws IllegalArgumentException If each function does not have the same number of entries as the input variable x.
     */
    public static void plotFunctions(Matrix x, Matrix[] fncs, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        for (int i = 0; i < fncs.length; i++) {
            if (fncs[i].getRows() != x.getRows()) {
                throw new IllegalArgumentException("Each function must have " + x.getRows() + " entries! The function at index " + i + " has " + fncs[i].getRows() + " entries!");
            }
        }

        try {
            File pythonScript = File.createTempFile("functions_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("f" + i + " = np.array(" + fncs[i].npString() + ")");
                }
                //Printing the scatter plot
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("plt.plot(x, f" + i + ", color='" + colors[i] + "', label='" + fncNames[i] + "')");
                }
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

    /**
     * Graphs the provided functions on a plot together.
     *
     * @param x The values at which the functions are evaluated at. This matrix will be used to form the x-axis.
     * @param fncs The set of functions to be plotted.
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have a name.
     */
    public static void plotFunctions(Matrix x, Function<Double, Double>[] fncs, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        try {
            //Building function data
            Matrix[] functions = new Matrix[fncs.length];
            for (int i = 0; i < fncs.length; i ++) {
                functions[i] = LinearAlgebra.applyFunction(x, fncs[i]);
            }

            File pythonScript = File.createTempFile("function_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                //Initializing data
                out.println("x = np.array(" + x.npString() + ")");
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("f" + i + " = np.array(" + functions[i].npString() + ")");
                }
                //Printing the scatter plot
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("plt.plot(x, f" + i + ", color='" + colors[i] + "', label='" + fncNames[i] + "')");
                }
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

    public static void plotFunction(Matrix x, Matrix f, String fnc, String xLabel, String yLabel, String title) {
        Matrix[] fncs = {f};
        String[] names = {fnc};
        plotFunctions(x, fncs, names, xLabel, yLabel, title);
    }

    public static void plotFunction(Matrix x, Function<Double, Double> fnc, String fncName, String xLabel, String yLabel, String title) {
        Function<Double, Double>[] fncs = new Function[]{fnc};
        String[] names = {fncName};
        plotFunctions(x, fncs, names, xLabel, yLabel, title);
    }

    public static void plotTwoFunctions(Matrix x, Matrix f1, String fnc1, Matrix f2, String fnc2, String xLabel, String yLabel, String title) {
        Matrix[] fncs = {f1, f2};
        String[] names = {fnc1, fnc2};
        plotFunctions(x, fncs, names, xLabel, yLabel, title);
    }

    public static void plotTwoFunctions(Matrix x, Function<Double, Double> fnc1, String fnc1Name, Function<Double, Double> fnc2, String fnc2Name, String xLabel, String yLabel, String title) {
        Function<Double, Double>[] fncs = new Function[]{fnc1, fnc2};
        String[] names = {fnc1Name, fnc2Name};
        plotFunctions(x, fncs, names, xLabel, yLabel, title);
    }
}