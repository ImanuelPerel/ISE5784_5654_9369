package lighting;

import geometries.Intersectable;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;

/**
 * testing softShadows
 * @author Shneor and Emanuel
 */
public class SoftShadowTests {
    /** Scene of the tests */
    private final Scene scene      = new Scene("Test scene");
    /** Camera builder of the tests */
    private final Camera.Builder camera     = Camera.getBuilder()
            .setDirection(new Vector(0,0,-1), Vector.Y)
            .setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene));

    /** The sphere in the tests */
    private final Intersectable sphere     = new Sphere( 60d,new Point(0, 0, -200))
            .setEmission(new Color(BLUE))
            .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30));
    /** The material of the triangles in the tests */
    private final Material       trMaterial = new Material().setKD(0.5).setKS(0.5).setNShininess(30);

    /** Helper function for the tests in this module
     * @param pictName     the name of the picture generated by a test
     * @param triangle     the triangle in the test
     * @param spotLocation the spotlight location in the test */
    private void sphereTriangleHelper(String pictName, Triangle triangle, Point spotLocation) throws CloneNotSupportedException {
        scene.geometries.add(sphere, triangle.setEmission(new Color(BLUE)).setMaterial(trMaterial));
        scene.lights.add( //
                new SpotLight(new Color(400, 240, 0), spotLocation, new Vector(1, 1, -3)) //
                        .setKL(1E-5).setKQ(1.5E-7).setRadius(10));
        Camera cam =camera.setImageWriter(new ImageWriter(pictName, 400, 400).setNumberOfSamples(9))
                .build();
        cam.renderImage(); //
        cam.writeToImage();
    }

    /** Produce a picture of a sphere and triangle with point light and shade */
    @Test
    public void sphereTriangleInitial() throws CloneNotSupportedException {
        sphereTriangleHelper("shadowSphereTriangleInitialSoft", //
                new Triangle(new Point(-70, -40, 0), new Point(-40, -70, 0), new Point(-68, -68, -4)), //
                new Point(-100, -100, 200));
    }

    /** Sphere-Triangle shading - move triangle up-right */
    @Test
    public void sphereTriangleMove1() throws CloneNotSupportedException {
        Vector Movement = new Vector(0,0,10).scale(3);
        sphereTriangleHelper("shadowSphereTriangleMove1soft", //
                new Triangle(
                        new Point(-70, -40, 0).add(Movement),
                        new Point(-40, -70, 0).add(Movement),
                        new Point(-68, -68, -4).add(Movement)), //
                new Point(-100, -100, 200));
    }


    /** Sphere-Triangle shading - move triangle upper-righter */
    @Test
    public void sphereTriangleMove2() throws CloneNotSupportedException {
        Vector Movement = new Vector(1,1,0).scale(21);
        double displacementZ = Math.sqrt(60 * 60 - Movement.lengthSquared());
        //Movement.add(new Vector(0,0, displacementZ));
        sphereTriangleHelper("shadowSphereTriangleMove2soft", //
                new Triangle(
                        new Point(-70, -40, 0).add(Movement),
                        new Point(-40, -70, 0).add(Movement),
                        new Point(-68, -68, -4).add(Movement)),
                new Point(-100, -100, 200));
    }

    /** Sphere-Triangle shading - move spot closer */
    @Test
    public void sphereTriangleSpot1() throws CloneNotSupportedException {
        sphereTriangleHelper("shadowSphereTriangleSpot1soft", //
                new Triangle(
                        new Point(-70, -40, 0),
                        new Point(-40, -70, 0),
                        new Point(-68, -68, -4)),
                new Point(-115, -115, 200).add(new Vector(1, 1, -3).scale(28)));
    }

    /** Sphere-Triangle shading - move spot even more close */
    @Test
    public void sphereTriangleSpot2() throws CloneNotSupportedException {
        sphereTriangleHelper("shadowSphereTriangleSpot2soft", //
                new Triangle(
                        new Point(-70, -40, 0),
                        new Point(-40, -70, 0),
                        new Point(-68, -68, -4)),
                new Point(-119.5, -119.5, 200).add(new Vector(1, 1, -3).scale(44)));
    }

    /** Produce a picture of a two triangles lighted by a spot light with a Sphere
     * producing a shading */
    @Test
    public void trianglesSphere() throws CloneNotSupportedException {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKS(0.8).setNShininess(60)), //
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKS(0.8).setNShininess(60)), //
                new Sphere(30d,new Point(0, 0, -11)) //
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)) //
        );
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4)) //
                        .setKL(4E-4).setKQ(2E-5).setRadius(10));

        Camera cam=camera.setImageWriter(new ImageWriter("shadowTrianglesSphereSoft", 600, 600).setNumberOfSamples(9))
                .build();
        cam.renderImage();
        cam.writeToImage();
    }

}
