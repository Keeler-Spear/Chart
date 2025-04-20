import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.BiFunction;
import java.util.function.Function;

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
     * Graphs the provided data on a plot together in the Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. The largest matrix will be used to form the x-axis.
     * @param fncs The set of function outputs to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have a name.
     * @throws IllegalArgumentException If each function does not have the same number of entries as the input variable x.
     */
    public static void plot(Matrix x[], Matrix[] fncs, String[] fncSymbols, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        for (int i = 0; i < fncs.length; i++) {
            if (fncs[i].getRows() != x[i].getRows()) {
                throw new IllegalArgumentException("This function must have " + x[i].getRows() + " entries! The function at index " + i + " has " + fncs[i].getRows() + " entries!");
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
                for (int i = 0; i < x.length; i ++) {
                    out.println("x" + i + " = np.array(" + x[i].npString() + ")");
                }
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("f" + i + " = np.array(" + fncs[i].npString() + ")");
                }
                //Printing the scatter plot
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("plt.plot(x" + i + ", f" + i + ", '" + fncSymbols[i] + "', color='" + colors[i] + "', label='" + fncNames[i] + "')");
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
     * Graphs the provided data on a plot together in the Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. The largest matrix will be used to form the x-axis.
     * @param fncs The set of functions to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have a name.
     */
    public static void plot(Matrix x[], Function<Double, Double>[] fncs, String[] fncSymbols, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncSymbols.length) {
            throw new IllegalArgumentException("Each function must have a symbol! " + fncs.length + " != " + fncSymbols.length + "!");
        }

        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        //Building function data
        Matrix[] functions = new Matrix[fncs.length];
        for (int i = 0; i < fncs.length; i ++) {
            functions[i] = LinearAlgebra.applyFunction(x[i], fncs[i]);
        }

        plot(x, functions, fncSymbols, fncNames, xLabel, yLabel, title);
    }

    public static void plot(Matrix x, Matrix f, String fnc, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Matrix[] fncs = {f};
        String[] names = {fnc};
        String[] symbols = {"-"};
        plot(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plot(Matrix x, Function<Double, Double> fnc, String fncName, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Function<Double, Double>[] fncs = new Function[]{fnc};
        String[] names = {fncName};
        String[] symbols = {"-"};
        plot(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plot(Matrix x, Matrix f1, String fnc1, Matrix f2, String fnc2, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Matrix[] fncs = {f1, f2};
        String[] names = {fnc1, fnc2};
        String[] symbols = {"-", "-"};
        plot(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plot(Matrix x, Function<Double, Double> fnc1, String fnc1Name, Function<Double, Double> fnc2, String fnc2Name, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Function<Double, Double>[] fncs = new Function[]{fnc1, fnc2};
        String[] names = {fnc1Name, fnc2Name};
        String[] symbols = {"-", "-"};
        plot(xs, fncs, symbols, names, xLabel, yLabel, title);
    }


    /**
     * Graphs the provided data on a semilogy plot together in the Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. The largest matrix will be used to form the x-axis.
     * @param fncs The set of function outputs to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have a name.
     * @throws IllegalArgumentException If each function does not have the same number of entries as the input variable x.
     */
    public static void plotSemilogy(Matrix x[], Matrix[] fncs, String[] fncSymbols, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        for (int i = 0; i < fncs.length; i++) {
            if (fncs[i].getRows() != x[i].getRows()) {
                throw new IllegalArgumentException("This function must have " + x[i].getRows() + " entries! The function at index " + i + " has " + fncs[i].getRows() + " entries!");
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
                for (int i = 0; i < x.length; i ++) {
                    out.println("x" + i + " = np.array(" + x[i].npString() + ")");
                }

                for (int i = 0; i < fncs.length; i ++) {
                    out.println("f" + i + " = np.array(" + fncs[i].npString() + ")");
                }
                //Printing the scatter plot
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("plt.semilogy(x" + i + ", f" + i + ", '" + fncSymbols[i] + "', color='" + colors[i] + "', label='" + fncNames[i] + "')");
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
     * Graphs the provided data on a semilogy plot together in the Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. This matrix will be used to form the x-axis.
     * @param fncs The set of functions to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have a name.
     */
    public static void plotSemilogy(Matrix x[], Function<Double, Double>[] fncs, String[] fncSymbols, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncSymbols.length) {
            throw new IllegalArgumentException("Each function must have a symbol! " + fncs.length + " != " + fncSymbols.length + "!");
        }

        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        //Building function data
        Matrix[] functions = new Matrix[fncs.length];
        for (int i = 0; i < fncs.length; i ++) {
            functions[i] = LinearAlgebra.applyFunction(x[i], fncs[i]);
        }

        plotSemilogy(x, functions, fncSymbols, fncNames, xLabel, yLabel, title);
    }

    public static void plotSemilogy(Matrix x, Matrix f, String fnc, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Matrix[] fncs = {f};
        String[] names = {fnc};
        String[] symbols = {"-"};
        plotSemilogy(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plotSemilogy(Matrix x, Function<Double, Double> fnc, String fncName, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Function<Double, Double>[] fncs = new Function[]{fnc};
        String[] names = {fncName};
        String[] symbols = {"-"};
        plotSemilogy(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plotSemilogy(Matrix x, Matrix f1, String fnc1, Matrix f2, String fnc2, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Matrix[] fncs = {f1, f2};
        String[] names = {fnc1, fnc2};
        String[] symbols = {"-", "-"};
        plotSemilogy(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plotSemilogy(Matrix x, Function<Double, Double> fnc1, String fnc1Name, Function<Double, Double> fnc2, String fnc2Name, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Function<Double, Double>[] fncs = new Function[]{fnc1, fnc2};
        String[] names = {fnc1Name, fnc2Name};
        String[] symbols = {"-", "-"};
        plotSemilogy(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    /**
     * Graphs the provided data on a loglog plot together in the Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. The largest matrix will be used to form the x-axis.
     * @param fncs The set of function outputs to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have a name.
     * @throws IllegalArgumentException If each function does not have the same number of entries as the input variable x.
     */
    public static void plotLogLog(Matrix x[], Matrix[] fncs, String[] fncSymbols, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        for (int i = 0; i < fncs.length; i++) {
            if (fncs[i].getRows() != x[i].getRows()) {
                throw new IllegalArgumentException("This function must have " + x[i].getRows() + " entries! The function at index " + i + " has " + fncs[i].getRows() + " entries!");
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
                for (int i = 0; i < x.length; i ++) {
                    out.println("x" + i + " = np.array(" + x[i].npString() + ")");
                }

                for (int i = 0; i < fncs.length; i ++) {
                    out.println("f" + i + " = np.array(" + fncs[i].npString() + ")");
                }
                //Printing the scatter plot
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("plt.loglog(x" + i + ", f" + i + ", '" + fncSymbols[i] + "', color='" + colors[i] + "', label='" + fncNames[i] + "')");
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
     * Graphs the provided data on a loglog plot together in the Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. This matrix will be used to form the x-axis.
     * @param fncs The set of functions to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have a name.
     */
    public static void plotLogLog(Matrix x[], Function<Double, Double>[] fncs, String[] fncSymbols, String[] fncNames, String xLabel, String yLabel, String title) {
        if (fncs.length != fncSymbols.length) {
            throw new IllegalArgumentException("Each function must have a symbol! " + fncs.length + " != " + fncSymbols.length + "!");
        }

        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        //Building function data
        Matrix[] functions = new Matrix[fncs.length];
        for (int i = 0; i < fncs.length; i ++) {
            functions[i] = LinearAlgebra.applyFunction(x[i], fncs[i]);
        }

        plotLogLog(x, functions, fncSymbols, fncNames, xLabel, yLabel, title);
    }

    public static void plotLogLog(Matrix x, Matrix f, String fnc, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Matrix[] fncs = {f};
        String[] names = {fnc};
        String[] symbols = {"-"};
        plotLogLog(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plotLogLog(Matrix x, Function<Double, Double> fnc, String fncName, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Function<Double, Double>[] fncs = new Function[]{fnc};
        String[] names = {fncName};
        String[] symbols = {"-"};
        plotLogLog(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plotLogLog(Matrix x, Matrix f1, String fnc1, Matrix f2, String fnc2, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Matrix[] fncs = {f1, f2};
        String[] names = {fnc1, fnc2};
        String[] symbols = {"-", "-"};
        plotLogLog(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    public static void plotLogLog(Matrix x, Function<Double, Double> fnc1, String fnc1Name, Function<Double, Double> fnc2, String fnc2Name, String xLabel, String yLabel, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Function<Double, Double>[] fncs = new Function[]{fnc1, fnc2};
        String[] names = {fnc1Name, fnc2Name};
        String[] symbols = {"-", "-"};
        plotLogLog(xs, fncs, symbols, names, xLabel, yLabel, title);
    }

    /**
     * Graphs the provided data on a polar together.
     *
     * @param x The values at which the functions are evaluated at.
     * @param fncs The set of function outputs to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have the same number of entries as the input variable x.
     */
    public static void plotPolar(Matrix x[], Matrix[] fncs, String[] fncSymbols, String title) {
        for (int i = 0; i < fncs.length; i++) {
            if (fncs[i].getRows() != x[i].getRows()) {
                throw new IllegalArgumentException("This function must have " + x[i].getRows() + " entries! The function at index " + i + " has " + fncs[i].getRows() + " entries!");
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
                for (int i = 0; i < x.length; i ++) {
                    out.println("x" + i + " = np.array(" + x[i].npString() + ")");
                }

                for (int i = 0; i < fncs.length; i ++) {
                    out.println("f" + i + " = np.array(" + fncs[i].npString() + ")");
                }
                //Printing the scatter plot
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("plt.polar(x" + i + ", f" + i + ", '" + fncSymbols[i] + "', color='" + colors[i] + "')");
                }
                //Titling chart
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

    /**
     * Graphs the provided data on a polar together.
     *
     * @param x The values at which the functions are evaluated at.
     * @param fncs The set of function outputs to be plotted.
     * @param fncSymbols The symbols used to represent the function. For continuous plotting, use "-".
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have symbol.
     * @throws IllegalArgumentException If each function does not have the same number of entries as the input variable x.
     */
    public static void plotPolar(Matrix x[], Function<Double, Double>[] fncs, String[] fncSymbols, String title) {
        if (fncs.length != fncSymbols.length) {
            throw new IllegalArgumentException("Each function must have a symbol! " + fncs.length + " != " + fncSymbols.length + "!");
        }

        //Building function data
        Matrix[] functions = new Matrix[fncs.length];
        for (int i = 0; i < fncs.length; i ++) {
            functions[i] = LinearAlgebra.applyFunction(x[i], fncs[i]);
        }

        plotPolar(x, functions, fncSymbols, title);
    }

    public static void plotPolar(Matrix x, Matrix f, String title) {
        Matrix[] xs = new Matrix[]{x};
        Matrix[] fncs = {f};
        String[] symbols = {"-"};
        plotPolar(xs, fncs, symbols, title);
    }

    public static void plotPolar(Matrix x, Function<Double, Double> fnc, String title) {
        Matrix[] xs = new Matrix[]{x};
        Function<Double, Double>[] fncs = new Function[]{fnc};
        String[] symbols = {"-"};
        plotPolar(xs, fncs, symbols, title);
    }

    public static void plotPolar(Matrix x, Matrix f1, Matrix f2, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Matrix[] fncs = {f1, f2};
        String[] symbols = {"-", "-"};
        plotPolar(xs, fncs, symbols, title);
    }

    public static void plotPolar(Matrix x, Function<Double, Double> fnc1, Function<Double, Double> fnc2, String title) {
        Matrix[] xs = new Matrix[]{x, x};
        Function<Double, Double>[] fncs = new Function[]{fnc1, fnc2};
        String[] symbols = {"-", "-"};
        plotPolar(xs, fncs, symbols, title);
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

    //3d Plotting

    /**
     * Graphs the provided data on a plot together in the 3D Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. The largest matrix will be used to form the x-axis.
     * @param y The values at which the functions are evaluated at. The largest matrix will be used to form the y-axis.
     * @param fncs The set of function outputs to be plotted.
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param zLabel The label to be used for the z-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have a name.
     * @throws IllegalArgumentException If each function does not have the same number of entries as the input variable x.
     */
    public static void plot3D(Matrix x[], Matrix y[], Matrix[] fncs, String[] fncNames, String xLabel, String yLabel, String zLabel, String title) {
        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        for (int i = 0; i < fncs.length; i++) {
            if (fncs[i].getCols() != x[i].getRows()) {
                throw new IllegalArgumentException("This function must have " + x[i].getRows() + " entries! The function at index " + i + " has " + fncs[i].getCols() + " entries!");
            }
            if (fncs[i].getRows() != y[i].getRows()) {
                throw new IllegalArgumentException("This function must have " + y[i].getRows() + " entries! The function at index " + i + " has " + fncs[i].getRows() + " entries!");
            }
        }

        try {
            File pythonScript = File.createTempFile("functions_plot", ".py");
            pythonScript.deleteOnExit();

            try (PrintWriter out = new PrintWriter(new FileWriter(pythonScript))) {
                //Imports
                out.println("import matplotlib.pyplot as plt");
                out.println("import numpy as np");
                out.println("fig = plt.figure()");
                out.println("ax = fig.add_subplot(projection='3d')");
                //Initializing data
                for (int i = 0; i < x.length; i ++) {
                    out.println("x" + i + " = np.array(" + x[i].npString() + ")");
                }
                for (int i = 0; i < x.length; i ++) {
                    out.println("y" + i + " = np.array(" + y[i].npString() + ")");
                }
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("f" + i + " = np.array(" + fncs[i].npString() + ")");
                }
                //Printing the scatter plot
                for (int i = 0; i < fncs.length; i ++) {
                    out.println("ax.plot_surface(x" + i + ", y" + i + ", f" + i + ", color='" + colors[i] + "', label='" + fncNames[i] + "')");
                }
                //Titling chart
                out.println("ax.set_xlabel('" + xLabel + "')");
                out.println("ax.set_ylabel('" + yLabel + "')");
                out.println("ax.set_zlabel('" + zLabel + "')");
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
     * Graphs the provided data on a plot together in the 3D Cartesian Plane.
     *
     * @param x The values at which the functions are evaluated at. The largest matrix will be used to form the x-axis.
     * @param y The values at which the functions are evaluated at. The largest matrix will be used to form the y-axis.
     * @param fncs The set of functions to be plotted.
     * @param fncNames The set of names corresponding to the functions provided.
     * @param xLabel The label to be used for the x-axis.
     * @param yLabel The label to be used for the y-axis.
     * @param zLabel The label to be used for the z-axis.
     * @param title The title to be used for the plot
     * @throws IllegalArgumentException If each function does not have a name.
     */
    public static void plot3D(Matrix x[], Matrix y[], BiFunction<Double, Double, Double>[] fncs, String[] fncNames, String xLabel, String yLabel, String zLabel, String title) {
        if (fncs.length != fncNames.length) {
            throw new IllegalArgumentException("Each function must have a name! " + fncs.length + " != " + fncNames.length + "!");
        }

        //Building function data
        Matrix[] functions = new Matrix[fncs.length];
        Matrix fncVals;
        for (int i = 0; i < fncs.length; i ++) {
            fncVals = new Matrix(y[i].getRows(), x[i].getRows());
            for (int j = 1; j <= fncVals.getRows(); j++) {
                for (int k = 1; k <= fncVals.getCols(); k++) {
                    fncVals.setValue(j, k, fncs[i].apply(x[i].getValue(k, 1), y[i].getValue(j, 1)));
                }
            }

            functions[i] = fncVals;
        }

        plot3D(x, y, functions, fncNames, xLabel, yLabel, zLabel, title);
    }

    public static void plot3D(Matrix x, Matrix y, Matrix f, String fnc, String xLabel, String yLabel, String zLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Matrix[] ys = new Matrix[]{y};
        Matrix[] fncs = {f};
        String[] names = {fnc};
        plot3D(xs, ys, fncs, names, xLabel, yLabel, zLabel, title);
    }

    public static void plot3D(Matrix x, Matrix y, BiFunction<Double, Double, Double> fnc, String fncName, String xLabel, String yLabel, String zLabel, String title) {
        Matrix[] xs = new Matrix[]{x};
        Matrix[] ys = new Matrix[]{y};
        BiFunction<Double, Double, Double>[] fncs = new BiFunction[]{fnc};
        String[] names = {fncName};
        plot3D(xs, ys, fncs, names, xLabel, yLabel, zLabel, title);
    }
}