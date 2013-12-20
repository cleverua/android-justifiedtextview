package com.eyeawards.android.components;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.eyeawards.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 11/14/13.
 */
public class JustifiedTextView extends TextView {

    private int maxTextWidth;
    private TextPaint paint;
    private float spaceWidth;

    public JustifiedTextView(Context context) {
        super(context);
    }

    public JustifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifiedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint = getPaint();
        paint.setColor(getCurrentTextColor());
        spaceWidth = paint.measureText(" ");
        maxTextWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int rowIndex = getPaddingTop();
        int colIndex = getPaddingLeft();

        List<String> lineList = divideLines();
        for (int i=0;i<lineList.size();i++){
            rowIndex+=getLineHeight();
            canvas.drawText(lineList.get(i), colIndex, rowIndex , paint);
        }
    }

    private List<String> divideLines() {
        List<String> resultLines = new ArrayList<String>();
        String[] paragraphs = ((String)getText()).split("\n");
        int lineWidth;

        for (String paragraph : paragraphs) {
            String[] words = paragraph.split(" ");
            String line = "";
            String previousLine = "";

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                previousLine = line;
                if (!TextUtils.isEmpty(line)) {
                    line += " ";
                }
                line += word;
                lineWidth = (int) paint.measureText(line);

                if (lineWidth == maxTextWidth || (lineWidth < maxTextWidth && i == (words.length - 1))) {
                    resultLines.add(line);
                    line = "";
                } else {
                    if (lineWidth > maxTextWidth) {
                        resultLines.add(justifyLine(previousLine));
                        line = word;
                    }
                }
            }
            if (!TextUtils.isEmpty(line)) {
                resultLines.add(line);
            }
        }
        return resultLines;
    }

    private String justifyLine(String line) {
        float lineWidth = paint.measureText(line);

        int additionalSpacesCount = (int) (Math.floor(maxTextWidth - lineWidth) / spaceWidth);
        if (additionalSpacesCount < 1){
            return line;
        } else {
            String[] words = line.split(" ");
            double spacesPerDelimiter = 1.0 * additionalSpacesCount / (words.length - 1);

            StringBuilder builder = new StringBuilder();
            for(int k = 0; k < words.length; k++) {
                String word = words[k];
                builder.append(word).append(" ");

                for (int i = 0; i < (int)spacesPerDelimiter ; i++ ) {
                    builder.append(" ");
                }
                additionalSpacesCount -= (int)spacesPerDelimiter;
                spacesPerDelimiter = 1.0 * additionalSpacesCount / (words.length - 2 - k);
            }
            return builder.toString();
        }
    }
}
