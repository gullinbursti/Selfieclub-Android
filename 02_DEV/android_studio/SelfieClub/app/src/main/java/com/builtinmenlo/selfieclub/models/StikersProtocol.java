package com.builtinmenlo.selfieclub.models;

import com.picocandy.android.data.PCContent;
import com.picocandy.android.data.PCContentGroup;

import java.util.ArrayList;

/**
 * Created by Leonardo on 8/5/14.
 */
public interface StikersProtocol {
    public void didReceiveStickers(ArrayList<PCContentGroup> contentGroupsList,ArrayList<PCContent> stickerList);

}
