package com.moselin.rmlib.widget.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moselin.rmlib.R;
import com.moselin.rmlib.util.ScreenUtils;


/*** 
 * 类名: BottomMenuDialog
 * 描述: 从底部出现的菜单
 * @author Moselin
 * @date 2016年3月8日
 */
public class BottomMenuDialog extends Dialog implements View.OnClickListener{
	public interface MenuDialogOnButtonClickListener {
        public void onButtonClick(final String name, int index);
    }

    private Context context;
    private ViewGroup rootView;
    private LinearLayout itemsRootView;

    private List<String> btnNames;
    private List<View> itemViews;
    private MenuDialogOnButtonClickListener clickListener;
    private boolean selectMode = false;
    private int selectIndex = -1; // 要勾选的项
    private int invalidSelectIndex = -1; // 不能勾选的项目
    private int preSelectIndex = -1; // 之前勾选的项目

    public BottomMenuDialog(Context context, List<String> btnNames, MenuDialogOnButtonClickListener listener) {
        super(context, R.style.BottomMenuDialog);
        this.context = context;
        this.btnNames = btnNames;
        this.clickListener = listener;
        Window window = getWindow();
        LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = ScreenUtils.getScreenWidth(context);
		window.setAttributes(params);
		window.setWindowAnimations(R.style.fromBottomToUp);
    }

    public BottomMenuDialog(Context context, List<String> btnNames, int selectIndex, int invalidSelectIndex,
                      MenuDialogOnButtonClickListener listener) {
        this(context, btnNames, listener);

        if (selectIndex >= 0 && selectIndex < btnNames.size()) {
            this.selectMode = true;
            this.selectIndex = selectIndex;
            this.preSelectIndex = selectIndex;
            this.invalidSelectIndex = invalidSelectIndex;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        rootView = (ViewGroup) View.inflate(context, R.layout.bottom_menu_layout, null);
        itemsRootView = (LinearLayout) rootView.findViewById(R.id.menu_dialog_items_root);
        if (selectMode) {
            itemViews = new ArrayList<>();
        }

        View itemView = null;
        for (int i = 0; i < btnNames.size(); i++) {
            if (i== 0) {
            	if (btnNames.size() > 2) {
            		itemView = View.inflate(context, R.layout.bottom_menu_item_top, null);
				}else {
					itemView = View.inflate(context, R.layout.bottom_menu_item_cancel, null);
				}
			}else if (i == btnNames.size() -2) {
				itemView = View.inflate(context, R.layout.bottom_menu_item_bottom, null);
			}else if(i == btnNames.size()-1){
				itemView = View.inflate(context, R.layout.bottom_menu_item_cancel, null);
			}else {
				itemView = View.inflate(context, R.layout.bottom_menu_dialog_item, null);
			}
            ((TextView) itemView.findViewById(R.id.menu_button)).setText(btnNames.get(i));
            itemView.setTag(i);
            itemView.setOnClickListener(this);
            if (selectMode) {
                itemViews.add(itemView);
            }

            itemsRootView.addView(itemView);
        }

        selectItem();

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(rootView);
    }

    // 撤销最后一次选择，恢复上一次选择
    public void undoLastSelect() {
        if (selectMode && preSelectIndex >= 0 && preSelectIndex < btnNames.size()) {
            selectIndex = preSelectIndex;
            selectItem();
        }
    }

    private void selectItem() {
        if (!selectMode || selectIndex < 0 || selectIndex >= btnNames.size() || itemViews == null || itemViews
                .isEmpty()) {
            return;
        }

        View item;
        for (int i = 0; i < itemViews.size(); i++) {
            item = itemViews.get(i);
            item.findViewById(R.id.menu_select_icon).setVisibility(selectIndex == i ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (selectMode && index != invalidSelectIndex) {
            preSelectIndex = selectIndex;
            selectIndex = index;
            selectItem();
        }

        String btnName = btnNames.get(index);
        if (index !=btnNames.size()-1 && clickListener != null) {
            clickListener.onButtonClick(btnName,index);
        }
        dismiss();
    }
}

