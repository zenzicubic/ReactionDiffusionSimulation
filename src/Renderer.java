import Utils.Cell;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utils.Constants.*;

public class Renderer {
    public Cell[][] grid, nGrid;
    public double f, k, dA, dB;

    public Renderer(double f, double k, double dA, double dB) {
        this.f = f;
        this.k = k;
        this.dA = dA;
        this.dB = dB;

        this.grid = new Cell[WIDTH + 2][HEIGHT + 2];
        this.nGrid = new Cell[WIDTH + 2][HEIGHT + 2];

        reset();
    }

    public void reset() {
        // Fill the grid
        Cell c;
        for (int j = 0; j < HEIGHT + 2; j ++) {
            for (int i = 0; i < WIDTH + 2; i ++) {
                c = new Cell(1, 0);
                this.grid[i][j] = c;
                this.nGrid[i][j] = c;
            }
        }

        // Fill the box at the center
        int x = WIDTH / 2;
        int y = HEIGHT / 2;
        for (int j = y - OFF; j < y + OFF; j ++) {
            for (int i = x - OFF; i < x + OFF; i ++) {
                this.grid[i][j].b = 1;
                this.nGrid[i][j].b = 1;
            }
        }
    }

    private double laplacianA(int x, int y) {
        // This is a 2D Laplacian operator on the A component, using the coefficients given on Karl Sims' website
        double s = -this.grid[x][y].a; // center piece

        s += 0.2 * this.grid[x - 1][y].a; // side pieces
        s += 0.2 * this.grid[x + 1][y].a;
        s += 0.2 * this.grid[x][y - 1].a;
        s += 0.2 * this.grid[x][y + 1].a;

        s += 0.05 * this.grid[x - 1][y + 1].a; // corner pieces
        s += 0.05 * this.grid[x + 1][y + 1].a;
        s += 0.05 * this.grid[x - 1][y - 1].a;
        s += 0.05 * this.grid[x + 1][y - 1].a;
        return s;
    }

    private double laplacianB(int x, int y) {
        // This is a 2D Laplacian operator on the B component, using the coefficients given on Karl Sims' website
        double s = -this.grid[x][y].b; // center piece

        s += 0.2 * this.grid[x - 1][y].b; // side pieces
        s += 0.2 * this.grid[x + 1][y].b;
        s += 0.2 * this.grid[x][y - 1].b;
        s += 0.2 * this.grid[x][y + 1].b;

        s += 0.05 * this.grid[x - 1][y + 1].b; // corner pieces
        s += 0.05 * this.grid[x + 1][y + 1].b;
        s += 0.05 * this.grid[x - 1][y - 1].b;
        s += 0.05 * this.grid[x + 1][y - 1].b;
        return s;
    }

    private double clamp(double n) {
        // Clamp a double
        return Math.min(1.0, Math.max(n, 0.0));
    }

    public void update() {
        // solve the Grey Scott equations using implicit Euler
        double iA, iB, lA, lB, a, b;
        for (int y = 1; y < HEIGHT+1; y ++) {
            for (int x = 1; x < WIDTH+1; x ++) {
                a = this.grid[x][y].a;
                b = this.grid[x][y].b;
                lA = laplacianA(x, y);
                lB = laplacianB(x, y);

                iA = (this.dA * lA) - (a * b * b) + (this.f * (1.0 - a));
                iB = (this.dB * lB) + (a * b * b) - ((this.f + this.k) * b);
                nGrid[x][y].a = clamp(a + iA * dt);
                nGrid[x][y].b = clamp(b + iB * dt);
            }
        }
        Cell[][] temp = grid;
        grid = nGrid;
        nGrid = temp;
    }

    public Color colorRamp(double t) {
        // Generate the Gnuplot color palette for the pictures
        double v = Math.sin(2 * Math.PI * t);

        int r = (int)(255 * Math.sqrt(t));
        int g = (int)(255 * t * t * t);
        int b = (int)(255 * Math.max(v, 0));
        return new Color(r, g, b);
    }
    public BufferedImage paint() {
        // draw the state at current time
        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        double val;
        Color c;
        for (int y = 1; y < HEIGHT + 1; y ++) {
            for (int x = 1; x < WIDTH + 1; x ++) {
                val = 1 - clamp(grid[x][y].a - grid[x][y].b); // for a cool effect

                c = colorRamp(val);
                bi.setRGB(x - 1, y - 1, c.getRGB());
            }
        }
        return bi;
    }
}
