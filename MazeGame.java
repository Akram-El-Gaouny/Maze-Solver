

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MazeGame {

    public static JFrame mainFrame = new JFrame("MazeProgram");
    public static JLabel mazeLabel = new JLabel();
    public static boolean exitFound = false;
    public static JButton Maze[][] = new JButton[14][13];
    public static ActionListener click;
    public static ActionListener Done;
    public static int h = 0;

    public static int columnindex = 1;
    public static int rowindex = 1;
    public static int filesnum = 2;
    public static int rows = 14;
    public static int columns = 13;
    public static char[][] puzzle = new char[rows][columns];
    public static boolean clicked[] = new boolean[5];
    public static JButton selection[] = new JButton[5];

    public static class MyClass implements Runnable {

        public void run() {
            try {
                move(rowindex, columnindex);
            } catch (InterruptedException ex) {
                Logger.getLogger(MazeGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void move(int row, int col) throws InterruptedException {
            if (puzzle[row][col] == 'X') { //checks if the maze is at the end
                exitFound = true;
            } else {
                Maze[rowindex][columnindex].setBackground(Color.orange);
                puzzle[row][col] = '-'; // change the current location symbol to indicate that the spot has been visited
                Thread.sleep(500);
                Maze[row][col].setBackground(Color.red);

                if ((exitFound == false) && (puzzle[row][col + 1] == ' ' || puzzle[row][col + 1] == 'X')) { // checks if the maze is not done and if the space to the right can be moved to
                    System.out.print(col + " ");
                    move(row, col + 1);
                }
                if ((exitFound == false) && (puzzle[row + 1][col] == ' ' || puzzle[row + 1][col] == 'X')) { // checks if the maze is not done and if the space to the right can be moved to
                    move(row + 1, col);
                }
                if ((exitFound == false) && (puzzle[row][col - 1] == ' ' || puzzle[row][col - 1] == 'X')) { // checks if the maze is not done and if the space to the right can be moved to
                    move(row, col - 1);
                }
                if ((exitFound == false) && (puzzle[row - 1][col] == ' ' || puzzle[row - 1][col] == 'X')) { // checks if the maze is not done and if the space to the right can be moved to
                    move(row - 1, col);
                }

                if (exitFound == true) {
                    puzzle[row][col] = '.';
                    Thread.sleep(500);
                    Maze[row][col].setBackground(Color.blue);
                    Maze[rowindex][columnindex].setBackground(Color.orange);
                }
            }
        }

    }

    public static Thread t1;

    /*  public static int rows = puzzle.length();*/
    public static void main(String[] args) throws InterruptedException, IOException {
        t1 = new Thread(new MyClass());
        String[] Options = new String[]{"Random maze", "create a maze"};
        int schoice = JOptionPane.showOptionDialog(null, "Please Select an option", "Game mode", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, Options, Options);
        loadinitial();
        //  save();
        if (schoice == 0) {

            randomizer();
            initializeWindow();

        } else {
            Maker();
        }

    }

    public static void loadinitial() throws FileNotFoundException, IOException {

        try (BufferedReader in = new BufferedReader(new FileReader("Mazes\\files.txt"))) {// refers to the location of the file

            while (in.ready()) {

                String g = in.readLine();
                filesnum = Integer.parseInt(g);
            }

        }

    }

    public static void save() throws FileNotFoundException {
        filesnum++;
        String savedfile = "Mazes\\\\" + filesnum + ".txt";
        String start = rowindex + " " + columnindex;
        System.out.print(start);

        try (
                PrintWriter out = new PrintWriter(savedfile)) {
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < columns; y++) {
                    out.print(puzzle[x][y]);
                }
                out.println();
            }
            out.println(start);
        }

        try (
                PrintWriter out = new PrintWriter("Mazes\\files.txt")) {
            out.print(filesnum);
        }

    }

    public static void randomizer() throws IOException {
        String file;
        Random v = new Random();
        int g = v.nextInt(filesnum) + 1;

        file = ("Mazes" + "\\\\" + g + ".txt");
        System.out.println(file + " ");
        LoadMaze(file);

    }

    public static void Maker() {

        String options[] = new String[]{"Start", "Borders", "Pathway", "Finish", "Solve"};

        click = (ActionEvent e) -> {
            if (e.getSource() instanceof JButton) {
                JButton BC
                        = (JButton) e.getSource();
                int r = (int) BC.getClientProperty("rows");
                int c = (int) BC.getClientProperty("col");
                int temp = 10;

                for (int z = 0; z < clicked.length; z++) {
                    if (clicked[z] == true) {
                        temp = z;
                    }

                }

                if (temp == 0) {
                    BC.setBackground(Color.orange);
                    if (r != 0 && r != 13 && c != 0 && c != 12) {

                        columnindex = c;
                        rowindex = r;
                        puzzle[r][c] = ' ';
                        mainFrame.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Puzzle Must be Enclosed");
                    }

                } else if (temp == 1) {
                    BC.setBackground(Color.black);

                    puzzle[r][c] = '#';
                    mainFrame.repaint();
                } else if (temp == 2) {
                    if (r != 0 && r != 13 && c != 0 && c != 12) {

                        BC.setBackground(Color.white);
                        puzzle[r][c] = ' ';
                        mainFrame.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Puzzle Must be Enclosed");
                    }

                } else if (temp == 3) {

                    BC.setBackground(Color.green);
                    puzzle[r][c] = 'X';
                    mainFrame.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Please Select an Option");
                }

            }
        };

        Done = (ActionEvent e) -> {
            int index;
            if (e.getSource() instanceof JButton) {
                JButton bClicked = new JButton();
                bClicked = (JButton) e.getSource();

                for (int y = 0; y < clicked.length; y++) {
                    clicked[y] = false;
                }
                index = (int) bClicked.getClientProperty("index");
                clicked[index] = true;
                if (index == 4) {
                    try {
                        save();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MazeGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    t1.start();
                }

            }

        };

        mainFrame = new JFrame("Maze Solver");

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLayout(
                null);
        mainFrame.setSize(
                550, 800);
        JPanel maze = new JPanel();

        maze.setSize(
                500, 600);
        maze.setLocation(
                0, 0);
        maze.setLayout(
                new GridLayout(rows, columns));
        mainFrame.add(maze);

        for (int k = 0;
                k < rows;
                k++) {
            for (int i = 0; i < columns; i++) {
                Maze[k][i] = new JButton();
                Maze[k][i].setBackground(Color.black);
                puzzle[k][i] = '#';
                Maze[k][i].putClientProperty("rows", k);
                Maze[k][i].putClientProperty("col", i);
                Maze[k][i].addActionListener(click);
                // Maze[k][i].setEnabled(false);

                maze.add(Maze[k][i]);

            }
        }

        for (int i = 0;
                i < clicked.length;
                i++) {
            selection[i] = new JButton(options[i]);
            clicked[i] = false;
            selection[i].setSize(100, 100);
            selection[i].setLocation((100) * i, 600);
            selection[i].putClientProperty("index", i);
            selection[i].addActionListener(Done);
            mainFrame.add(selection[i]);

        }

        mainFrame.setVisible(
                true);
    }

    public static void LoadMaze(String file) throws FileNotFoundException, IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {// refers to the location of the file
            String temp[] = new String[rows];
            char[] p = new char[columns];
            while (in.ready()) {

                for (int k = 0; k < rows; k++) {
                    temp[k] = in.readLine();// read the file

                    p = temp[k].toCharArray();

                    for (int j = 0; j < columns; j++) {
                        puzzle[k][j] = p[j];

                    }
                }
                String IN = in.readLine();
                String indexs[] = IN.split(" ");
                rowindex = Integer.parseInt(indexs[0]);
                columnindex = Integer.parseInt(indexs[1]);
            }

        }
    }

    public static void initializeWindow() {
        mainFrame = new JFrame("Maze Solver");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(rows, columns));
        mainFrame.setSize(500, 700);


        for (int k = 0; k < rows; k++) {
            for (int i = 0; i < columns; i++) {
                Maze[k][i] = new JButton();

                Maze[k][i].putClientProperty("rows", k);
                Maze[k][i].putClientProperty("col", i);
                Maze[k][i].addActionListener(click);
                Maze[k][i].setEnabled(false);
                if (puzzle[k][i] == '#') {
                    Maze[k][i].setBackground(Color.black);
                } else if (puzzle[k][i] == ' ') {
                    Maze[k][i].setBackground(Color.white);
                } else if (puzzle[k][i] == 'X') {
                    Maze[k][i].setBackground(Color.green);
                }
                mainFrame.add(Maze[k][i]);

            }
        }

        mainFrame.setVisible(true);
        t1.start();
    }

}
