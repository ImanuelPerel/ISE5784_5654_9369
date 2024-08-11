package renderer;

import org.jetbrains.annotations.NotNull;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * This class represents a Camera in a 3D space.
 * The camera is responsible for projecting a 3D scene onto a 2D view plane.
 * It includes methods for setting the camera's position, orientation, and view plane size,
 * as well as for constructing rays through pixels and rendering an image.
 * <p>
 * The Camera class uses a builder pattern to facilitate the construction of a Camera instance.
 * </p>
 * <p>
 * The Camera also supports printing a grid on the image and writing the rendered image to a file.
 * </p>
 *
 * @author Shneor and Emanuel
 * @see primitives.Point
 * @see primitives.Vector
 * @see primitives.Ray
 * @see renderer.ImageWriter
 * @see renderer.RayTracerBase
 */
public class Camera implements Cloneable {
    private Point location;
    private Vector vUp, vRight, vTo;
    private double height = 0.0, width = 0.0, distance = 0.0;

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    private PixelManager pixelManager;
    private int threadsCount;

    /**
     * Gets the location of the camera.
     *
     * @return the location of the camera.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Gets the "up" vector of the camera.
     *
     * @return the "up" vector of the camera.
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * Gets the "right" vector of the camera.
     *
     * @return the "right" vector of the camera.
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * Gets the "to" vector of the camera.
     *
     * @return the "to" vector of the camera.
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * Gets the height of the view plane.
     *
     * @return the height of the view plane.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the width of the view plane.
     *
     * @return the width of the view plane.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the distance from the camera to the view plane.
     *
     * @return the distance from the camera to the view plane.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Private constructor to prevent direct instantiation.
     * Use the Builder to create an instance.
     */
    public Camera() {
    }

    /**
     * Returns a new Builder instance to build a Camera.
     *
     * @return a new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a given pixel.
     *
     * @param nX the number of horizontal pixels.
     * @param nY the number of vertical pixels.
     * @param j  the pixel column.
     * @param i  the pixel row.
     * @return the constructed ray.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Image center
        Point pointCenter = location.add(vTo.scale(distance));

        // Calculate the size of each pixel
        double Rx = width / nX;
        double Ry = height / nY;

        // Calculation of displacement according to i j
        double Xj = (j - (double) (nX - 1) / 2) * Rx;
        double Yi = -(i - (double) (nY - 1) / 2) * Ry;

        // Calculating the pixel's position according to i j and gives a point
        Point Pij = pointCenter;
        if (!isZero(Xj)) {
            Pij = Pij.add(vRight.scale(Xj));
        }
        if (!isZero(Yi)) {
            Pij = Pij.add(vUp.scale(Yi));
        }

        // Calculation of the vector from the point to the screen according to i j
        Vector viewIJ = Pij.subtract(location);

        // Returns the ray from the point by i j
        return new Ray(location, viewIJ);
    }

    /**
     * Builder class to build a Camera instance.
     * This class follows the builder pattern to provide a flexible way to construct a Camera object.
     */
    public static class Builder {

        private final Camera camera = new Camera();

        /**
         * Sets the location for the camera.
         *
         * @param location the location to set for the camera.
         * @return the Builder instance.
         */
        public Builder setLocation(Point location) {
            camera.location = location;
            return this;
        }

        /**
         * Sets the threadsCount for the camera.
         *
         * @param threadsCount the threads count to set for the camera.
         * @return the Builder instance.
         */
        public Builder setMultithreading(int threadsCount) {
            if (camera.threadsCount < 0)
                throw new MissingResourceException("threads count can't be smaller than 0", "Camera", "");
            camera.threadsCount = threadsCount;
            return this;
        }

        /**
         * Sets the "up" and "to" vectors for the camera.
         *
         * @param vUp the "up" vector to set for the camera.
         * @param vTo the "to" vector to set for the camera.
         * @return the Builder instance.
         * @throws IllegalArgumentException if the vectors are not perpendicular.
         */
        public Builder setDirection(@NotNull Vector vTo, @NotNull Vector vUp) throws IllegalArgumentException {
            if (!isZero(vUp.dotProduct(vTo)))
                throw new IllegalArgumentException("the vectors vTo and vUp are not perpendicular");
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width to set for the view plane.
         * @param height the height to set for the view plane.
         * @return the Builder instance.
         */
        public Builder setVpSize(double width, double height) {
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance to set from the camera to the view plane.
         * @return the Builder instance.
         */
        public Builder setVpDistance(double distance) {
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param rayTracer the ray tracer to set.
         * @return the Builder instance.
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Sets the image writer for the camera.
         *
         * @param imageWriter the image writer to set.
         * @return the Builder instance.
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Builds and returns the Camera instance.
         *
         * @return the built Camera instance.
         */
        public Camera build() {
            String h = "height", w = "width", d = "distance";

            if (isZero(camera.height))
                throw new MissingResourceException("Missing data to render", "Camera", h);
            if (isZero(camera.width))
                throw new MissingResourceException("Missing data to render", "Camera", w);
            if (isZero(camera.distance))
                throw new MissingResourceException("Missing data to render", "Camera", d);
            if (camera.location == null)
                throw new MissingResourceException("Missing data to render", "Camera", "location");
            if (camera.vUp == null)
                throw new MissingResourceException("Missing data to render", "Camera", "vUp");
            if (camera.vTo == null)
                throw new MissingResourceException("Missing data to render", "Camera", "vTo");

            if (camera.height < 0)
                throw new IllegalArgumentException("The " + h + " value is invalid");
            if (camera.width < 0)
                throw new IllegalArgumentException("The " + w + " value is invalid");
            if (camera.distance < 0)
                throw new IllegalArgumentException("The " + d + " value is invalid");

            if (camera.rayTracer == null)
                throw new MissingResourceException("Missing data to render", "Camera", "rayTracer");
            if (camera.imageWriter == null)
                throw new MissingResourceException("Missing data to render", "Camera", "imageWriter");

            camera.vRight = camera.vTo.crossProduct(camera.vUp);    //since the to and up vectors are normalized, we don't need to normalize the right vector
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException ex) {
                throw new MissingResourceException(ex.getMessage(), "", "");
            }
        }
    }

    /**
     * Prints a grid on the image with a specified interval and color.
     *
     * @param interval The interval between grid lines.
     * @param color    The color of the grid lines.
     * @throws MissingResourceException if the {@code imageWriter} is not initialized.
     */
    public Camera printGrid(int interval, Color color) throws MissingResourceException {
        if (imageWriter != null) {
            for (int i = 0; i < imageWriter.getNx(); i++) {
                for (int j = 0; j < imageWriter.getNy(); j++) {
                    if (i % interval == 0 || j % interval == 0) {
                        imageWriter.writePixel(i, j, color);
                    }
                }
            }
        } else {
            throw new MissingResourceException("ImageWriter not initialized", "ImageWriter", "Missing");
        }
        return this;
    }

    /**
     * Writes the rendered image to a file.
     *
     * @throws MissingResourceException if the {@code imageWriter} is not initialized.
     */
    public Camera writeToImage() {
        if (imageWriter == null) {
            throw new MissingResourceException("ImageWriter not initialized.", "Camera", "Missing");
        }
        imageWriter.writeToImage();
        return this;
    }

    /**
     * Renders the image by casting rays through each pixel of the view plane.
     *
     * The method operates in two modes:
     * 1. **Without Threads:** If `threadsCount` is 0, the method processes each pixel sequentially in the main thread.
     * 2. **With Threads:** If `threadsCount` is greater than 0, the method creates and starts a specified number of threads.
     *    Each thread processes pixels in parallel by fetching the next available pixel from the `PixelManager`.
     *
     * The method waits for all threads to complete before returning the `Camera` instance.
     *
     * @return the {@link Camera} instance after the image has been rendered.
     */
    public Camera renderImage() {
        final int nx = imageWriter.getNx(), ny = imageWriter.getNy();
        pixelManager = new PixelManager(ny, nx);

        if (threadsCount == 0) //without threads
            for (int i = 0; i < ny; i++) {
                for (int j = 0; j < nx; j++) {
                    castRay(nx, ny, j, i);
                }
            }
        else { //with threads
            var threads = new LinkedList<Thread>();
            while (threadsCount-- > 0)
                threads.add(new Thread(() -> { // add threads
                    PixelManager.Pixel pixel;
                    while ((pixel = pixelManager.nextPixel()) != null)
                        castRay(nx, ny, pixel.col(), pixel.row());
                }));
            for (var thread : threads) thread.start();
            try {
                for (var thread : threads) thread.join();
            } catch (InterruptedException ignore) {
            }
        }

        return this;
    }

    /**
     * Casts a ray through a specific pixel and writes the resulting color to the image.
     *
     * @param Nx     the number of horizontal pixels.
     * @param Ny     the number of vertical pixels.
     * @param column the column of the pixel.
     * @param row    the row of the pixel.
     */
    private void castRay(int Nx, int Ny, int column, int row) {
        imageWriter.writePixel(column, row, rayTracer.traceRay(constructRay(Nx, Ny, column, row), imageWriter.getNumberOfSamples()));
    }


}
