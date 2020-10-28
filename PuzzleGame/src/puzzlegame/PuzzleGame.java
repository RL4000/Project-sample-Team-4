package puzzlegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PuzzleGame {

    Puzzle p;

    private ArrayList<JButton> arrBtn;
    private Timer t;
    private int numMove = 0;

    public PuzzleGame() {
    }

    public void setUp() {
        p.lblCount.setText("0");
        p.lblTime.setText("00:00");
    }

    public void countTime() {
        t = new Timer(1000, new ActionListener() {
            int minutes = 0;
            int second = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                second++;
                p.lblTime.setText(second + "");
            }
        });
        t.start();
    }

    public boolean checkMove(JButton btn, int size) {
        if (btn.getText().equals("")) {
            return false;
        }
        int startCol = 0;
        int startRow = 0;
        int desCol = 0;
        int desRow = 0;
        for (int i = 0; i < arrBtn.size(); i++) {
            if (arrBtn.get(i).getText().equals(btn.getText())) {
                startCol = i % size;
                startRow = i / size;
            }
            if (arrBtn.get(i).getText().equals("")) {
                desCol = i % size;
                desRow = i / size;
            }
        }
        if (startCol == desCol) {
            if (startRow == (desRow + 1) || startRow == (desRow - 1)) {
                return true; // move up or move down
            }
        }
        if (startRow == desRow) {
            if (startCol == (desCol + 1) || startCol == (desCol - 1)) {
                return true; // move right or move left
            }
        }
        return false;
    }

    public void moveBtn(JButton btn) {
        for (int i = 0; i < arrBtn.size(); i++) {
            if (arrBtn.get(i).getText().equals("")) {
                arrBtn.get(i).setText(btn.getText());
                break;
            }
        }
        btn.setText("");
        numMove++;
        p.lblCount.setText(numMove + "");

    }

    public void setWindowSize(int num) {
        int heightButton = 60;
        int widthButton = 60;
        //set cho panel dung layout
        p.pnLayout.setPreferredSize(new Dimension(num * widthButton, num * widthButton));
        p.pnLayout.setMaximumSize(new Dimension(num * widthButton, num * widthButton));
        p.pnLayout.setMinimumSize(new Dimension(num * widthButton, num * widthButton));
        p.setResizable(false);
        p.pack();
        System.out.println(p.pnLayout.getSize());
    }

    public void startMatrix() {
        String s = p.jComboBox1.getSelectedItem().toString();
        String[] temp = s.split("x");
        initButon(Integer.parseInt(temp[0]));
        setWindowSize(Integer.parseInt(temp[0]));
    }

    public ArrayList randomMatrix(int num) {
        ArrayList<String> data = new ArrayList<>();
        ArrayList<Integer> d = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            d.add(i + 1);
        }

        int dem = 0;
        int s = 0;

        do {
            Collections.shuffle(d);
            s = 0;
            for (int i = 0; i < num; i++) {
                System.out.print(d.get(i) + " ");
                for (int j = i + 1; j < num; j++) {
                    if (d.get(i) > d.get(j)) {
                        dem++;
                    }
                }
                s = s + dem;
                dem = 0;

            }
        } while (s > 0 && s % 2 == 1);
        System.out.println("s = " + s);
        for (int i = 0; i < num; i++) {
            data.add(d.get(i) + "");
        }
        data.add("");
        return data;
    }

    public void initButon(int size) {
        arrBtn = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        int num = size * size - 1;
        data = randomMatrix(num);
        p.pnLayout.removeAll();
        p.pnLayout.setLayout(new GridLayout(size, size, 10, 10));

        for (int i = 0; i < data.size(); i++) {
            JButton btn = new JButton(data.get(i));
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkMove(btn, size)) {
                        moveBtn(btn);
                        if (checkWin()) {
                            t.stop();
                            isWon();
                        }
                    }
                }
            });
            p.pnLayout.add(btn);
            arrBtn.add(btn);
        }

    }

    private boolean checkWin() {
        for (int i = 0; i < arrBtn.size() - 1; i++) {
            for (int j = i; j < arrBtn.size() - 1; j++) {
                if (arrBtn.get(j).getText().equals("")) {
                    return false;
                }
                if (Integer.parseInt(arrBtn.get(j).getText()) < Integer.parseInt(arrBtn.get(i).getText())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void isWon() {
        JOptionPane.showMessageDialog(null, "You Won!");
    }

    public void newGame() {
        t.stop();
        int confirm = JOptionPane.showConfirmDialog(null, "Do you want to make new game?", "New Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            setUp();
            countTime();
            startMatrix();
        } else {
            t.start();
        }

    }

}
