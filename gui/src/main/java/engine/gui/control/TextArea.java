package engine.gui.control;

import engine.gui.input.KeyEvent;
import engine.gui.layout.ScrollPane;
import engine.gui.text.WrapText;
import engine.graphics.font.FontHelper;
import engine.input.KeyCode;

public class TextArea extends TextInput {

    private ScrollPane scrollPane;
    private WrapText wrapText;

    public TextArea(){
        super();
//        removeEventHandler(KeyEvent.KEY_PRESSED, super::onKeyPressed);
//        addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        scrollPane = new ScrollPane();
        wrapText = new WrapText();
        wrapText.text().bindBidirectional(this.text());
        scrollPane.setContent(wrapText);
        width().addChangeListener((observable,o,n) -> scrollPane.getSize().prefWidth().set(n));
        height().addChangeListener((observable, oldValue, newValue) -> scrollPane.getSize().prefHeight().set(newValue));
        this.getChildren().add(scrollPane);
    }

    public void onKeyPressed(KeyEvent event) {
        super.onKeyPressed(event);
        if(event.getKey() == KeyCode.ENTER || event.getKey() == KeyCode.NUMPAD_ENTER){
            if (caret.get() != anchor.get()) {
                replaceSelection("");
                deselect();
            }
            insertText(Math.max(caret.get(), 0), "\n");
        }
    }

    @Override
    protected int getNearestMousePos(float posX, float posY) {
        //TODO
        float adjustedX = posX - scrollPane.xOffset().getFloat();
        int posExclusive = 1;
        float x = 0;
        while (posExclusive <= length()) {
            x += FontHelper.instance().computeTextWidth(getTextInRange(posExclusive - 1, posExclusive), font().getValue());
            if (x > adjustedX)
                break;
            posExclusive++;
        }
        return posExclusive - 1;
    }

    @Override
    protected void updatePointer() {

    }
}