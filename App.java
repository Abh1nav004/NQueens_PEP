import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;

public class App {
    static Scanner scanner = new Scanner(System.in);
    final static int M = scanner.nextInt();
    
    static JLabel[][] jLabel = new JLabel[M][M];
    static int board[][] = new int[M][M];
    static ImageIcon queenIcon;

    static void printSolution() {
        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                System.out.printf("%d ", board[i][j]);
            }
            System.out.printf("\n");
        }
    }

    static boolean isSafe(int row, int col) {
        try {
            Thread.sleep(20); // Adjust sleep time for visualization speed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < col; ++i)
            if (board[row][i] == 1)
                return false;

        for (int i = row, j = col; i >= 0 && j >= 0; --i, --j) {
            if (board[i][j] == 1)
                return false;
        }

        for (int i = row, j = col; i < M && j >= 0; ++i, --j) {
            if (board[i][j] == 1)
                return false;
        }

        return true;
    }

    static boolean findSolution(int col) {
        if (col >= M)
            return true;

        boolean foundSolution = false;

        for (int i = 0; i < M; ++i) {
            try {
                Thread.sleep(500); // Adjust sleep time for visualization speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isSafe(i, col)) {
                board[i][col] = 1;
                jLabel[i][col].setIcon(queenIcon); // Set queen icon instead of background color
                jLabel[i][col].setBackground(Color.GREEN); // Highlight the current placement

                foundSolution = findSolution(col + 1);

                if (foundSolution) {
                    return true;
                } else {
                    board[i][col] = 0;
                    jLabel[i][col].setIcon(null); // Clear icon for backtracking
                    jLabel[i][col].setBackground((i + col) % 2 == 0 ? Color.WHITE : Color.BLACK); // Restore
                                                                                                       // chessboard
                                                                                                       // color
                }
            }
        }

        return foundSolution;
    }

    static void solveNQueen() {
        try {
            Thread.sleep(500); // Initial delay for setup
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Reset board and icons
        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                try {
                    Thread.sleep(20); // Adjust sleep time for visualization speed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                board[i][j] = 0;
                jLabel[i][j].setIcon(null); // Clear any existing icons
                jLabel[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK); // Alternate colors
            }
        }

        // If solution exists, find and display it
        if (findSolution(0)) {
            // Show dialog box for solution found
            JOptionPane.showMessageDialog(null, "Solution found!", "N-Queens Solution",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("No Solution.");
        }
    }

    App() {
        JFrame jFrame = new JFrame("NQueen Visualizer");
        jFrame.setSize(400, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel chessboardPanel = new JPanel(new GridLayout(M, M));
        chessboardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jFrame.add(chessboardPanel);

        // Load queen image from URL
        try {
            URL imageUrl = new URL(
                    "https://png.pngtree.com/png-vector/20220915/ourmid/pngtree-on-a-white-background-a-threedimensional-black-chess-queen-vector-png-image_48720522.jpg");
            BufferedImage queenImage = ImageIO.read(imageUrl);
            if (queenImage != null) {
                queenIcon = new ImageIcon(queenImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            } else {
                System.err.println("Failed to load queen image from URL.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create chessboard with custom grid lines
        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                jLabel[i][j] = new JLabel();
                jLabel[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                jLabel[i][j].setOpaque(true);
                jLabel[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK); // Alternate colors

                // Add borders to simulate grid lines
                jLabel[i][j].setBorder(new LineBorder(Color.BLACK));

                chessboardPanel.add(jLabel[i][j]);
            }
        }

        jFrame.setVisible(true);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            new Thread(() -> {
                app.solveNQueen();
            }).start();
        });
    }
}