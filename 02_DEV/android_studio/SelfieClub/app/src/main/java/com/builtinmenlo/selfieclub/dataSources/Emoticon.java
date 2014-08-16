package com.builtinmenlo.selfieclub.dataSources;

import com.picocandy.android.data.PCContent;

/**
 * Created by Javy on 8/14/14.
 */
public class Emoticon{

    private boolean selected;
    private PCContent content;

    public Emoticon(PCContent content){
        this.content = content;
    }

    public PCContent getContent() {
        return content;
    }

    public void setContent(PCContent content) {
        this.content = content;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
