package src;

import src.Frame;

public class PixelGen {
	static Frame instance = new Frame();
	
	public static void main(String[] args) {
		System.out.println("Hello World.");
		instance.createDefaultFrame();
		instance.resetPixelButtons();
		instance.displayFrame();
		instance.addCommandButtons();
		instance.addPaintButtons();
		instance.startRainbowTimer();
	}

}
