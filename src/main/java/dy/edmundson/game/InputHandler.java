package dy.edmundson.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key space = new Key();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        toggle(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggle(e.getKeyCode(), false);
    }

    public void toggle(int event, boolean isPressed) {
        if (event == KeyEvent.VK_UP || event == KeyEvent.VK_W) {
            up.toggle(isPressed);
        } else if (event == KeyEvent.VK_DOWN || event == KeyEvent.VK_S) {
            down.toggle(isPressed);
        } else if (event == KeyEvent.VK_LEFT || event == KeyEvent.VK_A) {
            left.toggle(isPressed);
        } else if (event == KeyEvent.VK_RIGHT || event == KeyEvent.VK_D) {
            right.toggle(isPressed);
        } else if (event == KeyEvent.VK_SPACE) {
            space.toggle(isPressed);
        }
    }

    public class Key {
        private boolean isPressed = false;
        public void toggle(boolean isPressed) {
            this.isPressed = isPressed;
        }

        public boolean isPressed() {
            return isPressed;
        }
    }
}
