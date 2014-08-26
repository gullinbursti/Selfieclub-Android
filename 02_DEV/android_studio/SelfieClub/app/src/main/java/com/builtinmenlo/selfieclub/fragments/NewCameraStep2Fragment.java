/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*._
 /**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~,
 *
 * @class SettingsTabBtnFragment
 * @project SelfieClub
 * @package com.builtinmenlo.selfieclub.fragments
 *
 * @author Matt.H <matt@builtinmenlo.com>
 * @created 16-Apr-2014 @ 16:11
 * @copyright (c) 2014 Built in Menlo, LLC. All rights reserved.
 *
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~·'
/**~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~**/


package com.builtinmenlo.selfieclub.fragments;


//] includes [!]>
//]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.dataSources.Emoticon;
import com.builtinmenlo.selfieclub.models.PicoCandyManager;
import com.builtinmenlo.selfieclub.models.SCDialogProtocol;
import com.builtinmenlo.selfieclub.models.StikersProtocol;
import com.picocandy.android.data.PCContent;
import com.picocandy.android.data.PCContentGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

;
//]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[


// <[!] class delaration [¡]>
public class NewCameraStep2Fragment extends Fragment implements StikersProtocol, SCDialogProtocol {
//]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._

    //] class properties ]>
    //]=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~.
    public ListView lv;
    public ArrayList<Emoticon> emoticons;
    private MyCustomAdapter adapter;

    private Bundle bundle;
    private ProgressBar loadingIcon;
    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~[]~=~=~=~=~=~=~=~=~=~=~=~=~=~[

    private Fragment backView;
    private Fragment nextView;

    private static String NO_EMOTICON_TAG = "no_emoticon";

    // <*] class constructor [*>
    public NewCameraStep2Fragment() {/*..\(^_^)/..*/}

    //]~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=[>
    //]~=~=~=~=~=~=~=~=~=[>

    @Override
    public void onDetach() {
        super.onDetach();
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.show();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        View view = inflater.inflate(R.layout.new_camera_step_2, container, false);

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.hide();

        loadingIcon = (ProgressBar) view.findViewById(R.id.loadingIcon);
        lv = (ListView) view.findViewById(android.R.id.list);
        emoticons = new ArrayList<Emoticon>();
        populate();

        PicoCandyManager.sharedInstance().requestStickers(this);

        bundle = getArguments();
        /*byte[] avatarImage = null;
        if (bundle != null) {
            avatarImage = bundle.getByteArray(CameraPreview.EXTRA_IMAGE);
        }*/

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraFragment newFragment = new CameraFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(NewCameraStep2Fragment.this);
                transaction.replace(R.id.fragment_container, newFragment);
                //newFragment.setNextView(new NewCameraStep2Fragment());
                transaction.commit();
            }
        });

        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> selected = new ArrayList<String>();
                for (Emoticon emoticon:emoticons){
                    if (emoticon.isSelected()){
                        String []stickerName = emoticon.getContent().getName().split("\\.");
                        selected.add(stickerName[0]);
                    }
                }
                if (selected.size() < 1){
                    showNoEmoticonDialog();
                }else{
                    Fragment newFragment = new CameraStep3Fragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add(R.id.fragment_container, newFragment);
                    bundle.putStringArrayList(CameraStep3Fragment.EXTRA_STICKERS,selected);
                    newFragment.setArguments(bundle);
                    transaction.commit();
                }
                /*;*/
            }
        });

        hideKeyboard(view);
        return view;
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    private void showNoEmoticonDialog(){
        String message = getString(R.string.message_no_selected_emotions);
        SCDialog dialog = new SCDialog();
        dialog.setScDialogProtocol(this);
        dialog.setMessage(message);
        dialog.setPositiveButtonTitle(getResources().getString(R.string.ok_button_title));
        dialog.showTwoButtons = true;
        dialog.show(getFragmentManager(),NO_EMOTICON_TAG);
    }

    private void hideKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void onCreate(Bundle savedInstanceState) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onCreate(savedInstanceState);
    }//]~*~~*~~*~~*~~*~~*~~*~~*~~·¯

    public void onAttach(Activity activity) {
        //]~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~~*~._
        super.onAttach(activity);
    }

    private static class ViewHolder {
        TextView lblName;
        ImageView imgAddOrCheck;
        ImageView imgEmoticon;
        ProgressBar imgLoading;
    }

    public void populate() {
        if (lv != null) {
            adapter = new MyCustomAdapter(getActivity().getBaseContext(), emoticons);
            lv.post(new Runnable() {
                public void run() {
                    lv.setAdapter(adapter);
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if (emoticons.get(position).isSelected()) {
                        arg1.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.gray_selection_dot);
                        emoticons.get(position).setSelected(false);
                    } else {
                        arg1.findViewById(R.id.imgAddOrCheck).setBackgroundResource(R.drawable.green_selection_dot);
                        emoticons.get(position).setSelected(true);
                    }
                }
            });
        }
    }


    private class MyCustomAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<Emoticon> listData;

        public MyCustomAdapter(Context context, ArrayList<Emoticon> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return emoticons.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            //if (convertView == null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            convertView = inflater.inflate(R.layout.camera_step_2_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgEmoticon = (ImageView) convertView.findViewById(R.id.imgEmoticon);
            viewHolder.lblName = (TextView) convertView.findViewById(R.id.lblName);
            viewHolder.imgAddOrCheck = (ImageView) convertView.findViewById(R.id.imgAddOrCheck);
            viewHolder.imgLoading = (ProgressBar) convertView.findViewById(R.id.loadingImage);
            convertView.setTag(viewHolder);


            Emoticon emoticon = emoticons.get(position);

            String []stickerName = emoticon.getContent().getName().split("\\.");
            viewHolder.lblName.setText(stickerName[0]);

            Picasso.with(getActivity()).load(emoticon.getContent().getSmall_image()).into(viewHolder.imgEmoticon, new Callback() {

                @Override
                public void onSuccess() {
                    viewHolder.imgEmoticon.setVisibility(View.VISIBLE);
                    viewHolder.imgLoading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    viewHolder.imgEmoticon.setVisibility(View.VISIBLE);
                    viewHolder.imgLoading.setVisibility(View.INVISIBLE);
                }
            });

            return convertView;
        }
    }

    @Override
    public void didReceiveStickers(ArrayList<PCContentGroup> contentGroupsList, ArrayList<PCContent> stickerList) {
        if (getActivity() != null) {
            for (PCContent sticker : stickerList)
                emoticons.add(new Emoticon(sticker));
            //emoticons = stickerList;
            if (loadingIcon != null)
                loadingIcon.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void didClickedButton(String dialogTag, int buttonIndex){
        if(dialogTag.equalsIgnoreCase(NO_EMOTICON_TAG)){
            if(buttonIndex==1){

            }
        }
    }
}
