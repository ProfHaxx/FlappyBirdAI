import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {

    public static int[] key_situationV1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.VK_UP:
                key_situationV1[0] = 1;
                break;
            case KeyEvent.VK_DOWN:
                key_situationV1[1] = 1;
                break;
            case KeyEvent.VK_LEFT:
                key_situationV1[2] = 1;
                break;
            case KeyEvent.VK_RIGHT:
                key_situationV1[3] = 1;
                break;
            case KeyEvent.VK_SPACE:
                key_situationV1[4] = 1;
                break;
            case KeyEvent.VK_ESCAPE:
                key_situationV1[5] = 1;
                break;

            case KeyEvent.VK_1:
                key_situationV1[6] = 1;
                break;
            case KeyEvent.VK_2:
                key_situationV1[7] = 1;
                break;
            case KeyEvent.VK_3:
                key_situationV1[8] = 1;
                break;
            case KeyEvent.VK_4:
                key_situationV1[9] = 1;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.VK_UP:
                key_situationV1[0] = 0;
                break;
            case KeyEvent.VK_DOWN:
                key_situationV1[1] = 0;
                break;
            case KeyEvent.VK_LEFT:
                key_situationV1[2] = 0;
                break;
            case KeyEvent.VK_RIGHT:
                key_situationV1[3] = 0;
                break;
            case KeyEvent.VK_SPACE:
                key_situationV1[4] = 0;
                break;
            case KeyEvent.VK_ESCAPE:
                key_situationV1[5] = 0;
                break;

            case KeyEvent.VK_1:
                key_situationV1[6] = 0;
                break;
            case KeyEvent.VK_2:
                key_situationV1[7] = 0;
                break;
            case KeyEvent.VK_3:
                key_situationV1[8] = 0;
                break;
            case KeyEvent.VK_4:
                key_situationV1[9] = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
        switch (e.getKeyCode()) {
            default:
                break;

        }
    }
}
