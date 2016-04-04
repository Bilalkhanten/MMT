package eu.modernmt.processing.recaser;

import eu.modernmt.model.Translation;
import eu.modernmt.model.Word;
import eu.modernmt.processing.framework.TextProcessor;

/**
 * Created by davide on 03/03/16.
 */
public class Recaser implements TextProcessor<Translation, Translation> {

    @Override
    public Translation call(Translation translation) {
        Word[] source = translation.getSource().getWords();
        Word[] target = translation.getWords();

        if (source.length > 0 && target.length > 0) {
            String sourceText = source[0].getText();
            String targetText = target[0].getText();

            if (sourceText.length() > 0 && targetText.length() > 0) {
                boolean sourceIsUpper = Character.isUpperCase(sourceText.charAt(0));
                char targetChar = targetText.charAt(0);

                targetText = (sourceIsUpper ? Character.toUpperCase(targetChar) : Character.toLowerCase(targetChar)) + targetText.substring(1);
                target[0].setText(targetText);
            }
        }

        return translation;
    }

    @Override
    public void close() {

    }

}
