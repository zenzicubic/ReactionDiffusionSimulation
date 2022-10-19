import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import static Utils.Constants.*;

public class UI implements ActionListener, ChangeListener {
    // Window components
    public JFrame frame;
    public JPanel panel, dispPanel, uiPanel;

    // UI elements: text, buttons, etc
    public JLabel img, heading, description, labelPreset, labelChoose,
            label1, label2, label3, label4;
    public JSlider slider1, slider2, slider3, slider4;
    public JComponent options;
    public JButton playBtn;
    public JComboBox<String> preset;
    public static final int GAP_SZ = 12;

    //Renderer things
    public Renderer renderer;
    public boolean playing = true;
    public final double[][] presets = new double[][] {
            {0.035, 0.065, 1, 0.5},
            {0.055, 0.062, 1, 0.5},
            {0.04, 0.06, 1, 0.5},
            {0.04, 0.062, 1, 0.5},
            {0.023, 0.052, 1, 0.5}
    };

    public UI() {
        renderer = new Renderer(0.055, 0.062, 1, 0.5);
        panel = new JPanel();

        frame = new JFrame("Reaction-Diffusion Simulator");
        frame.setSize(WIDTH, HEIGHT);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        initializeComponents();
        addComponents();
        loop();
    }

    public void initializeComponents() {
        // Initialize necessary components
        img = new JLabel("");
        heading = new JLabel("<html><h1>Reaction-Diffusion Simulator</h1>");
        description = new JLabel("<html>This program allows you to run a simulation<br>of two virtual chemicals reacting, using an<br>equation called the Gray-Scott model.<br>Play with the parameters and see what you get.");

        // Initialize labels
        label1 = new JLabel("<html>Feed rate <i>f</i>: " + renderer.f);
        label2 = new JLabel("<html>Removal rate <i>k</i>: " + renderer.k);
        label3 = new JLabel("<html>Diffusion coefficient A, <i>r<sub>A</sub></i>: " + renderer.dA);
        label4 = new JLabel("<html>Diffusion coefficient B, <i>r<sub>B</sub></i>: " + renderer.dB);

        // Initialize preset menu
        labelPreset = new JLabel("<html><h3>Pick a preset:</h3>");
        String[] names = new String[]{"Preset 1", "Preset 2", "Preset 3", "Preset 4", "Preset 5"};
        preset = new JComboBox<>(names);
        preset.setAlignmentX(Component.LEFT_ALIGNMENT);
        preset.setSelectedIndex(1);
        preset.addActionListener(this);

        // Initialize sliders
        labelChoose = new JLabel("<html><h3>Or play with the sliders:</h3>");
        slider1 = new JSlider(1, 100, (int)(renderer.f * 1000));
        slider2 = new JSlider(1, 100, (int)(renderer.k * 1000));
        slider3 = new JSlider(1, 1000, (int)(renderer.dA * 1000));
        slider4 = new JSlider(1, 1000, (int)(renderer.dB * 1000));

        slider1.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider1.addChangeListener(this);
        slider1.setMajorTickSpacing(10);
        slider1.setMinorTickSpacing(1);
        slider1.setPaintTicks(true);

        slider2.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider2.addChangeListener(this);
        slider2.setMajorTickSpacing(10);
        slider2.setMinorTickSpacing(1);
        slider2.setPaintTicks(true);

        slider3.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider3.addChangeListener(this);
        slider3.setMajorTickSpacing(100);
        slider3.setMinorTickSpacing(10);
        slider3.setPaintTicks(true);

        slider4.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider4.addChangeListener(this);
        slider4.setMajorTickSpacing(100);
        slider4.setMinorTickSpacing(10);
        slider4.setPaintTicks(true);

        // Initialize buttons
        options = createButtons();
        options.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void repaintLabels() {
        // Reload and repaint labels as params change
        label1.setText("<html>Feed rate <i>f</i>: " + renderer.f);
        label2.setText("<html>Removal rate <i>k</i>: " + renderer.k);
        label3.setText("<html>Diffusion coefficient A, <i>r<sub>A</sub></i>: " + renderer.dA);
        label4.setText("<html>Diffusion coefficient B, <i>r<sub>B</sub></i>: " + renderer.dB);

        label1.repaint();
        label2.repaint();
        label3.repaint();
        label4.repaint();
    }

    public JComponent createButtons() {
        // Create a small JPanel with the buttons in it, for horizontal alignment
        JPanel buttons = new JPanel();

        JButton reset = new JButton("Reset Simulation");
        reset.addActionListener(this);
        reset.setActionCommand("reset");

        playBtn = new JButton("Pause");
        playBtn.addActionListener(this);
        playBtn.setActionCommand("pause");

        buttons.add(reset);
        buttons.add(playBtn);
        return buttons;
    }

    public void addComponents() {
        // Set up internal JPanels and add components to them
        dispPanel = new JPanel();
        dispPanel.add(img);
        uiPanel = new JPanel();
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));

        uiPanel.add(heading);
        uiPanel.add(description);
        uiPanel.add(Box.createVerticalStrut(GAP_SZ));

        uiPanel.add(labelPreset);
        uiPanel.add(preset);
        uiPanel.add(Box.createVerticalStrut(GAP_SZ));

        uiPanel.add(labelChoose);
        uiPanel.add(label1);
        uiPanel.add(slider1);
        uiPanel.add(label2);
        uiPanel.add(slider2);
        uiPanel.add(label3);
        uiPanel.add(slider3);
        uiPanel.add(label4);
        uiPanel.add(slider4);

        uiPanel.add(Box.createVerticalStrut(GAP_SZ));
        uiPanel.add(Box.createHorizontalGlue());
        uiPanel.add(options);

        panel.add(dispPanel);
        panel.add(uiPanel);
    }

    public void loop() {
        while(playing) {
            update();
        }
    }


    public void update() {
        // Update the RD equation and image
        for (int i = 0; i < STEPS; i ++) {
            renderer.update();
        }
        BufferedImage b = renderer.paint();

        img.setIcon(new ImageIcon(b));
        img.repaint();
        frame.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks and inputs
        if (e.getSource().equals(preset)) {
            // handle preset selection
            int i = preset.getSelectedIndex();

            renderer.reset();
            renderer.f = presets[i][0];
            renderer.k = presets[i][1];
            renderer.dA = presets[i][2];
            renderer.dB = presets[i][3];

            slider1.setValue((int) (1000 * presets[i][0]));
            slider2.setValue((int) (1000 * presets[i][1]));
            slider3.setValue((int) (1000 * presets[i][2]));
            slider4.setValue((int) (1000 * presets[i][3]));
            repaintLabels();
        } else if (e.getActionCommand().equals("reset")) {
            renderer.reset();
        } else {
            // Handle playing and pausing
            playing = !playing;

            if (playing) {
                playBtn.setText("Pause");
                new Thread(this::loop).start();
            } else {
                playBtn.setText("Play");
            }
            playBtn.repaint();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // Handle slider changes
        if (e.getSource() == slider1) {
            renderer.f = slider1.getValue() / 1000.0;
        } else if (e.getSource() == slider2) {
            renderer.k = slider2.getValue() / 1000.0;
        } else if (e.getSource() == slider3) {
            renderer.dA = slider3.getValue() / 1000.0;
        } else {
            renderer.dB = slider4.getValue() / 1000.0;
        }
        renderer.reset();
        repaintLabels();
    }
}