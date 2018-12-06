package src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame implements MouseListener {
	JFrame frame = new JFrame("PixelGen");
	JPanel panel = new JPanel();
	int PixelArray[];
	JButton ButtonArray[];
	boolean MirrorMode = false;
	int PaintMode = 1;
	JButton SelectedButton;
	Timer RainbowTimer = new Timer();
	int[] rainbow = new int[3];
	int saver;
	int saveg;
	int saveb;
	
	// A boolean to say whether or not the mouse is currently pressed
	boolean mousePressed;
	// Since we want to be able to erase with dragging, we need to store the previous paint mode
	int previousPaintMode;

	public void createDefaultFrame() {
		Image icon = null;
		try {
			icon = (ImageIO.read(new File("PixelGen.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.getContentPane().setBackground(new Color(40, 40, 44));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(icon);
	}
	
	public void displayFrame () {
		frame.setVisible(true);
	}
	
	public void resetPixelButtons() {
		int vertical = frame.getSize().height /5;
		int horizon = frame.getSize().width /2 + frame.getSize().width /10;
		int horizontal = 0;
		PixelArray = new int[1024];
		ButtonArray = new JButton[1024];
		for (int i = 0; i < 1024; i++) {
			horizontal = i*10;
			if (horizontal % 320 == 0 && i > 1) {
				vertical += 15;
				horizon = frame.getSize().width /2 + frame.getSize().width /10;
			}
			PixelArray[i] = 0;
			ButtonArray[i] = new JButton("Pixel" + String.valueOf(i));
			ButtonArray[i].setText("");
			ButtonArray[i].setSize(15, 15);
			ButtonArray[i].setLocation(horizon, vertical);
			ButtonArray[i].setBackground(new Color(255, 255, 255));
			ButtonArray[i].setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
			final int x = i;
			ButtonArray[i].addMouseListener(this);
			ButtonArray[i].addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					ButtonArray[x].setBorder(BorderFactory.createLineBorder(new Color(100, 50, 200)));
				}
				
				public void mouseExited(java.awt.event.MouseEvent evt) {
					ButtonArray[x].setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
				}
			});
			ButtonArray[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonShift(ButtonArray[x], x);
				}
				
			});
			panel.add(ButtonArray[i]);
			panel.setBackground(new Color(40, 40, 44));
			panel.setLayout(null);
			frame.getContentPane().add(panel);
			horizon += 15;
		}
	}
	
	public void addCommandButtons() {
		JButton ResetButton = new JButton();
		JButton GenerateButton = new JButton();
		JButton MirrorModeButton = new JButton();
		ResetButton.setText("Reset");
		ResetButton.setSize(200, 25);
		ResetButton.setLocation(frame.getSize().width /50, frame.getSize().height /2 - frame.getSize().height /3);
		ResetButton.setBackground(new Color(255, 255, 255));
		ResetButton.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
		ResetButton.setFocusPainted(false);
		ResetButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				ResetButton.setBorder(BorderFactory.createLineBorder(new Color(100, 50, 200)));
			}
			
			public void mouseExited(java.awt.event.MouseEvent evt) {
				ResetButton.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
			}
		});
		ResetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				resetPixelStates();
			}
			
		});
		GenerateButton.setText("Generate");
		GenerateButton.setSize(200, 25);
		GenerateButton.setLocation(frame.getSize().width /50, frame.getSize().height /2 - frame.getSize().height /3 + 50);
		GenerateButton.setBackground(new Color(255, 255, 255));
		GenerateButton.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
		GenerateButton.setFocusPainted(false);
		GenerateButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				GenerateButton.setBorder(BorderFactory.createLineBorder(new Color(100, 50, 200)));
			}
			
			public void mouseExited(java.awt.event.MouseEvent evt) {
				GenerateButton.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
			}
		});
		GenerateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				generateSprites();
			}
			
		});
		MirrorModeButton.setText("Mirror X Axis");
		MirrorModeButton.setSize(200, 25);
		MirrorModeButton.setLocation(frame.getSize().width /50, frame.getSize().height /2 - frame.getSize().height /3 + 100);
		MirrorModeButton.setBackground(new Color(255, 255, 255));
		MirrorModeButton.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
		MirrorModeButton.setFocusPainted(false);
		MirrorModeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				MirrorModeButton.setBorder(BorderFactory.createLineBorder(new Color(100, 50, 200)));
			}
			
			public void mouseExited(java.awt.event.MouseEvent evt) {
				MirrorModeButton.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
			}
		});
		MirrorModeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mirrorAxis();
			}
			
		});
		panel.add(ResetButton);
		panel.add(GenerateButton);
		panel.add(MirrorModeButton);
	}
	
	public void addPaintButtons() {
		JButton OrangeButton = new JButton();
		JButton GreenButton = new JButton();
		JButton RedButton = new JButton();
		JButton DarkRedButton = new JButton();
		JButton BlackButton = new JButton();
		JButton WhiteButton = new JButton();
		JLabel OrangeText = new JLabel("Always Generate");
		JLabel GreenText = new JLabel("50% Chance To Generate");
		JLabel RedText = new JLabel("Always Border");
		JLabel DarkRedText = new JLabel("50% Chance Of Border");
		JLabel BlackText = new JLabel("Never Generate/No Border");
		JLabel WhiteText = new JLabel("Never Generate/Possible Border");
		OrangeText.setSize(150, 50);
		OrangeText.setForeground(new Color (255, 155, 0));
		OrangeText.setLocation(frame.getSize().width /50 + frame.getSize().width /5 + 40, frame.getSize().height /2 - frame.getSize().height /3 - 13);
		GreenText.setSize(150, 50);
		GreenText.setForeground(new Color (50, 190, 50));
		GreenText.setLocation(frame.getSize().width /50 + frame.getSize().width /5 + 40, frame.getSize().height /2 - frame.getSize().height /3 + 37);
		RedText.setSize(150, 50);
		RedText.setForeground(new Color (255, 50, 50));
		RedText.setLocation(frame.getSize().width /50 + frame.getSize().width /5 + 40, frame.getSize().height /2 - frame.getSize().height /3 + 87);
		DarkRedText.setSize(150, 50);
		DarkRedText.setForeground(new Color (155, 50, 50));
		DarkRedText.setLocation(frame.getSize().width /50 + frame.getSize().width /5 + 40, frame.getSize().height /2 - frame.getSize().height /3 + 137);
		BlackText.setSize(150, 50);
		BlackText.setForeground(new Color (150, 0, 150));
		BlackText.setLocation(frame.getSize().width /50 + frame.getSize().width /5 + 40, frame.getSize().height /2 - frame.getSize().height /3 + 187);
		WhiteText.setSize(200, 50);
		WhiteText.setForeground(new Color (255, 255, 255));
		WhiteText.setLocation(frame.getSize().width /50 + frame.getSize().width /5 + 40, frame.getSize().height /2 - frame.getSize().height /3 + 237);
		doDefaultPaintButtonStuff(OrangeButton);
		OrangeButton.setLocation(frame.getSize().width /50 + frame.getSize().width /5, frame.getSize().height /2 - frame.getSize().height /3);
		OrangeButton.setBackground(new Color(255, 155, 0));
		OrangeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setPaintMode(1, OrangeButton);
			}
			
		});
		doDefaultPaintButtonStuff(GreenButton);
		GreenButton.setLocation(frame.getSize().width /50 + frame.getSize().width /5, frame.getSize().height /2 - frame.getSize().height /3 + 50);
		GreenButton.setBackground(new Color(50, 190, 50));
		GreenButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setPaintMode(2, GreenButton);
			}
			
		});
		doDefaultPaintButtonStuff(RedButton);
		RedButton.setLocation(frame.getSize().width /50 + frame.getSize().width /5, frame.getSize().height /2 - frame.getSize().height /3 + 100);
		RedButton.setBackground(new Color(255, 50, 50));
		RedButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setPaintMode(3, RedButton);
			}
			
		});
		doDefaultPaintButtonStuff(DarkRedButton);
		DarkRedButton.setLocation(frame.getSize().width /50 + frame.getSize().width /5, frame.getSize().height /2 - frame.getSize().height /3 + 150);
		DarkRedButton.setBackground(new Color(155, 50, 50));
		DarkRedButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setPaintMode(4, DarkRedButton);
			}
			
		});
		doDefaultPaintButtonStuff(BlackButton);
		BlackButton.setLocation(frame.getSize().width /50 + frame.getSize().width /5, frame.getSize().height /2 - frame.getSize().height /3 + 200);
		BlackButton.setBackground(new Color(0, 0, 0));
		BlackButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setPaintMode(5, BlackButton);
			}
			
		});
		doDefaultPaintButtonStuff(WhiteButton);
		WhiteButton.setLocation(frame.getSize().width /50 + frame.getSize().width /5, frame.getSize().height /2 - frame.getSize().height /3 + 250);
		WhiteButton.setBackground(new Color(255, 255, 255));
		WhiteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setPaintMode(0, WhiteButton);
			}
			
		});
		panel.add(OrangeButton);
		panel.add(GreenButton);
		panel.add(RedButton);
		panel.add(DarkRedButton);
		panel.add(BlackButton);
		panel.add(WhiteButton);
		panel.add(OrangeText);
		panel.add(GreenText);
		panel.add(RedText);
		panel.add(DarkRedText);
		panel.add(BlackText);
		panel.add(WhiteText);
	}
	
	public void doDefaultPaintButtonStuff(JButton button) {
		button.setText("");
		button.setSize(25, 25);
		button.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
		button.setFocusPainted(false);
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			boolean mousedOver = false;
			Timer x = new Timer();
			
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				mousedOver = true;
				x.schedule(new TimerTask() {

					@Override
					public void run() {
						if (mousedOver == true) {
							button.setBorder(BorderFactory.createLineBorder(new Color(getRainbowRGB(0), getRainbowRGB(1), getRainbowRGB(2))));
						}
					}
					
				}, 0, 4);
				button.setBorder(BorderFactory.createLineBorder(new Color(getRainbowRGB(0), getRainbowRGB(1), getRainbowRGB(2))));
			}
			
			public void mouseExited(java.awt.event.MouseEvent evt) {
				mousedOver = false;
				button.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 44)));
			}
		});
	}
	
	public void setPaintMode(int state, JButton button) {
		if (SelectedButton != null) {
			SelectedButton.setBackground(new Color(saver, saveg, saveb));
		}
		SelectedButton = button;
		saver = SelectedButton.getBackground().getRed();
		saveg = SelectedButton.getBackground().getGreen();
		saveb = SelectedButton.getBackground().getBlue();
		int reducer = SelectedButton.getBackground().getRed() - 50;
		int reduceg = SelectedButton.getBackground().getGreen() - 50;
		int reduceb  = SelectedButton.getBackground().getBlue() - 50;
		if (reducer < 0) {
			reducer = 0;
		}
		if (reduceg < 0) {
			reduceg = 0;
		}
		if (reduceb < 0) {
			reduceb = 0;
		}
		SelectedButton.setBackground(new Color(reducer, reduceg, reduceb));
		PaintMode = state;
	}
	
	public void startRainbowTimer() {
		RainbowTimer.schedule(new TimerTask() {
			int r = 0;
			int g = 255;
			int b = 255;

			@Override
			public void run() {
				if (r < 255 && b == 255) {
					r+=1;
					g-=1;
				}
				if (g < 255 && r == 255) {
					g+=1;
					b-=1;
				}
				if (b < 255 && g == 255) {
					b+=1;
					r-=1;
				}
				rainbow[0] = r;
				rainbow[1] = g;
				rainbow[2] = b;
			}
			
		}, 0, 4);
	}
	
	public int getRainbowRGB(int rgb) {
		return rainbow[rgb];
	}
	
	public void resetPixelStates() {
		MirrorMode = false;
		for (int i = 0; i < 1024; i++) {
			ButtonArray[i].setBackground(new Color(255, 255, 255));
			PixelArray[i] = 0;
		}
	}
	
	public void mirrorAxis() {
		if (MirrorMode == false) {
			MirrorMode = true;
			for (int i = 0; i < 1024; i++) {
				for (int x = 1; x <= 16; x++) {
					if (i*32 + 15 + x <= ButtonArray.length) {
						ButtonArray[i *32 + 15 + x].setBackground(new Color(170, 170, 170));
						PixelArray[i *32 + 15 + x] = 7;
					}
				}
			}
			return;
		}
		if (MirrorMode == true) {
			MirrorMode = false;
			for (int i = 0; i < 1024; i++) {
				for (int x = 1; x <= 16; x++) {
					if (i*32 + 15 + x <= ButtonArray.length) {
						ButtonArray[i *32 + 15 + x].setBackground(new Color(255, 255, 255));
						PixelArray[i *32 + 15 + x] = 0;
					}
				}
			}
		}
	}
	
	public void generateSprites() {
		frame.setTitle("PixelGen - Generated!!");
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.schedule(new Runnable() {

			@Override
			public void run() {
				frame.setTitle("PixelGen");
			}
			
		}, 3, TimeUnit.SECONDS);
		int x = 0;
		int y = 0;
		int r = 255;
		int g = 255;
		int b = 255;
		int reduction = 0;
		int randomchance = 0;
		Random numbergen = new Random();
		BufferedImage generated = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		generated.createGraphics();
		for (int gtimes = 0; gtimes < 64; gtimes++) {
			x = 0;
			y = 0;
			generated = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < 1024; i++) {
			r = 255;
			g = 255;
			b = 255;
			if (x == 32) {
				x = 0;
				y += 1;
			}
			if (PixelArray[i] == 0) {
			}
			if (PixelArray[i] == 1) {
				r = r - x*2 - y*2;
				g = g - x*2 - y*2;
				r -= 10;
				g -= 10;
				reduction = numbergen.nextInt(5);
				r -= reduction;
				g -= reduction;
				b -= reduction;
				generated.setRGB(x, y, new Color(r, g, b).getRGB());
				if (i + 1 <= PixelArray.length) {
					if (PixelArray[i + 1] == 0 || PixelArray[i + 1] == 2 && generated.getRGB(x + 1, y) == 0) {
						r = 255;
						g = 255;
						b = 255;
						r = r - x*3 - y*3;
						g = g - x*3 - y*3;
						b = b - x - y*2;
						reduction = numbergen.nextInt(5);
						r -= reduction;
						g -= reduction;
						b -= reduction;
						generated.setRGB(x + 1, y, new Color(r, g, b).getRGB());
						System.out.println("Success!");
					}
				}
				if (i - 1 <= PixelArray.length && i - 1 >= 1 && x - 1 >= 0) {
					if (PixelArray[i - 1] == 0 || PixelArray[i - 1] == 2 && generated.getRGB(x - 1, y) == 0) {
						r = 255;
						g = 255;
						b = 255;
						r = r - x*3 - y*3;
						g = g - x*3 - y*3;
						b = b - x - y*2;
						reduction = numbergen.nextInt(5);
						r -= reduction;
						g -= reduction;
						b -= reduction;
						generated.setRGB(x - 1, y, new Color(r, g, b).getRGB());
						System.out.println("Success!");
					}
				}
				if (i - 32 <= PixelArray.length && i - 32 >= 1) {
					if (PixelArray[i - 32] == 0 || PixelArray[i - 32] == 2 && generated.getRGB(x, y - 1) == 0) {
						r = 255;
						g = 255;
						b = 255;
						r = r - x*3 - y*3;
						g = g - x*3 - y*3;
						b = b - x - y*2;
						reduction = numbergen.nextInt(5);
						r -= reduction;
						g -= reduction;
						b -= reduction;
						generated.setRGB(x, y - 1, new Color(r, g, b).getRGB());
						System.out.println("Success!");
					}
				}
				if (i + 32 <= PixelArray.length && i + 32 >= 1) {
					if (PixelArray[i + 32] == 0 || PixelArray[i + 32] == 2) {
						r = 255;
						g = 255;
						b = 255;
						r = r - x*3 - y*3;
						g = g - x*3 - y*3;
						b = b - x - y*2;
						reduction = numbergen.nextInt(5);
						r -= reduction;
						g -= reduction;
						b -= reduction;
						generated.setRGB(x, y + 1, new Color(r, g, b).getRGB());
						System.out.println("Success!");
					}
				}
			}
			if (PixelArray[i] == 2) {
				randomchance = numbergen.nextInt(100 + 1);
				if (randomchance > 50) {
					r = r - x*2 - y*2;
					g = g - x*2 - y*2;
					r -= 10;
					g -= 10;
					reduction = numbergen.nextInt(5);
					r -= reduction;
					g -= reduction;
					b -= reduction;
					generated.setRGB(x, y, new Color(r, g, b).getRGB());
					if (i + 1 <= PixelArray.length) {
						if (PixelArray[i + 1] == 0 || PixelArray[i + 1] == 2) {
							r = 255;
							g = 255;
							b = 255;
							r = r - x*3 - y*3;
							g = g - x*3 - y*3;
							b = b - x - y*2;
							reduction = numbergen.nextInt(5);
							r -= reduction;
							g -= reduction;
							b -= reduction;
							generated.setRGB(x + 1, y, new Color(r, g, b).getRGB());
							System.out.println("Success!!");
						}
					}
					if (i - 1 <= PixelArray.length && i - 1 >= 1 && x - 1 >= 0) {
						System.out.println(generated.getRGB(x - 1, y));
						if (PixelArray[i - 1] == 0 || PixelArray[i - 1] == 2 && generated.getRGB(x - 1, y) == 0) {
							r = 255;
							g = 255;
							b = 255;
							r = r - x*3 - y*3;
							g = g - x*3 - y*3;
							b = b - x - y*2;
							reduction = numbergen.nextInt(5);
							r -= reduction;
							g -= reduction;
							b -= reduction;
							generated.setRGB(x - 1, y, new Color(r, g, b).getRGB());
							System.out.println("Left border generated at x" + (x - 1));
						}
					}
					if (i - 32 <= PixelArray.length && i - 32 >= 1) {
						if (PixelArray[i - 32] == 0 || PixelArray[i - 32] == 2 && generated.getRGB(x, y - 1) == 0) {
							r = 255;
							g = 255;
							b = 255;
							r = r - x*3 - y*3;
							g = g - x*3 - y*3;
							b = b - x - y*2;
							reduction = numbergen.nextInt(5);
							r -= reduction;
							g -= reduction;
							b -= reduction;
							generated.setRGB(x, y - 1, new Color(r, g, b).getRGB());
							System.out.println("Success!");
						}
					}
					if (i + 32 <= PixelArray.length && i + 32 >= 1) {
						if (PixelArray[i + 32] == 0 || PixelArray[i + 32] == 2) {
							r = 255;
							g = 255;
							b = 255;
							r = r - x*3 - y*3;
							g = g - x*3 - y*3;
							b = b - x - y*2;
							reduction = numbergen.nextInt(5);
							r -= reduction;
							g -= reduction;
							b -= reduction;
							generated.setRGB(x, y + 1, new Color(r, g, b).getRGB());
							System.out.println("Success!");
						}
					}
				}
			}
			if (PixelArray[i] == 3) {
				r = r - x*3 - y*3;
				g = g - x*3 - y*3;
				b = b - x - y*2;
				reduction = numbergen.nextInt(5);
				r -= reduction;
				g -= reduction;
				b -= reduction;
				generated.setRGB(x, y, new Color(r, g, b).getRGB());
			}
			if (PixelArray[i] == 4) {
				randomchance = numbergen.nextInt(100 + 1);
				if (randomchance > 50) {
					r = r - x*3 - y*3;
					g = g - x*3 - y*3;
					b = b - x - y*2;
					reduction = numbergen.nextInt(5);
					r -= reduction;
					g -= reduction;
					b -= reduction;
					generated.setRGB(x, y, new Color(r, g, b).getRGB());
				}
			}
			x = x + 1;
		}
		if (MirrorMode == true) {
			x = 0;
			y = 0;
			for (int i = 0; i < 1024; i++) {
				if (x == 32) {
					x = 0;
					y += 1;
				}
				generated.setRGB(31 - x, y, generated.getRGB(x, y));
				x += 1;
			}
		}
		File generatedfile = new File("generated" + gtimes + ".png");
		try {
			ImageIO.write(generated, "png", generatedfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		BufferedImage finalgen = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		finalgen.createGraphics();
		int row = 0;
		int column = 0;
		for (int i = 0; i < 64; i++) {
			Image img = null;
			try {
				img = (ImageIO.read(new File("generated" + i + ".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (row > 7) {
				row = 0;
				column += 1;
			}
			finalgen.getGraphics().drawImage(img, row*32, column*32, null);
			row += 1;
		}
		File finalgeneration = new File("generated.png");
		try {
			ImageIO.write(finalgen, "png", finalgeneration);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	}
	
	/* States are as follows:
	 * 0 = Never generated/Possible border
	 * 1 = Always generated
	 * 2 = 50% chance of generation
	 * 3 = Always border
	 * 4 = 50% chance of border
	 * 5 = Never generated/No border
	 */
	
	public void buttonShift (JButton button, int id) {
		if (PixelArray[id] == 7) {
			return;
		}
		if (PaintMode == 1) {
			button.setBackground(new Color (255, 155, 0));
			setButtonState(button, 1);
			PixelArray[id] = 1;
			return;
		}
		if (PaintMode == 2) {
			button.setBackground(new Color (50, 190, 50));
			setButtonState(button, 2);
			PixelArray[id] = 2;
			return;
		}
		if (PaintMode == 3) {
			button.setBackground(new Color (255, 50, 50));
			setButtonState(button, 3);
			PixelArray[id] = 3;
			return;
		}
		if (PaintMode == 4) {
			button.setBackground(new Color (150, 50, 50));
			setButtonState(button, 4);
			PixelArray[id] = 4;
			return;
		}
		if (PaintMode == 5) {
			button.setBackground(new Color (0, 0, 0));
			setButtonState(button, 5);
			PixelArray[id] = 5;
			return;
		}
		if (PaintMode == 0) {
			button.setBackground(new Color (255, 255, 255));
			setButtonState(button, 0);
			PixelArray[id] = 0;
			return;
		}
	}
	
	public void setButtonState (JButton button, int state) {
		for (int i = 0; i > 1024; i++) {
			if (button == ButtonArray[i]) {
				PixelArray[i] = state;
			}
		}
	}

	/*
	 * 
	 * Two of the methods done here will not be used, but the ones that are help with clicking and dragging
	 * across boxes.
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Do nothing
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Check which button we just entered, and change it accordingly
		for (int i = 0; i < ButtonArray.length; i++) {
			if (e.getComponent().equals(ButtonArray[i]) && mousePressed) {
				if (e.getButton() == MouseEvent.BUTTON1)
					buttonShift(ButtonArray[i], i);
				else
					buttonShift(ButtonArray[i], i);

			}
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Do nothing
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			previousPaintMode = PaintMode;
			PaintMode = 0;
		}
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3)
			PaintMode = previousPaintMode;
		mousePressed = false;
	}

}
