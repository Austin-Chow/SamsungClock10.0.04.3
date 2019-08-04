package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class DropdownListAdapter extends ArrayAdapter<String> {
    private static final char[][][] CHAR_CATEGORY;
    private static final char[] KOREAN_JAUM_CONVERT_MAP = new char[]{'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    private EditText mAutoText;
    private ArrayList<String> mCityList;
    private Context mContext;
    private int mListItemId;
    private boolean mSelectCurrentLocation;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.DropdownListAdapter$1 */
    class C08561 extends Filter {
        String keyword;

        C08561() {
        }

        public CharSequence convertResultToString(Object resultValue) {
            return this.keyword;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                DropdownListAdapter.this.notifyDataSetChanged();
            } else {
                DropdownListAdapter.this.notifyDataSetInvalidated();
            }
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            this.keyword = constraint.toString();
            FilterResults filterResults = new FilterResults();
            filterResults.values = DropdownListAdapter.this.mCityList;
            filterResults.count = DropdownListAdapter.this.mCityList.size();
            return filterResults;
        }
    }

    private static class ViewHolder {
        private int color;
        private View divider;
        private TextView gmtView;
        private LinearLayout linearLayout;
        private TextView subHeaderView;
        private TextView textView;

        private ViewHolder() {
        }
    }

    static {
        r0 = new char[12][][];
        r0[0] = new char[][]{new char[]{'C', 'Č'}, new char[]{'Č', 'C'}};
        r0[1] = new char[][]{new char[]{'Á', 'A'}, new char[]{'A', 'Á'}};
        r0[2] = new char[][]{new char[]{'O', 'Ó'}, new char[]{'Ó', 'O'}};
        r0[3] = new char[][]{new char[]{'U', 'Ü'}, new char[]{'Ü', 'U'}};
        r0[4] = new char[][]{new char[]{'U', 'Ú'}, new char[]{'Ú', 'U'}};
        r0[5] = new char[][]{new char[]{'Y', 'Ü'}, new char[]{'Ü', 'Y'}};
        r0[6] = new char[][]{new char[]{'A', 'Â'}, new char[]{'Â', 'A'}};
        r0[7] = new char[][]{new char[]{'L', 'Ľ'}, new char[]{'Ľ', 'L'}};
        r0[8] = new char[][]{new char[]{'Ά', 'Α'}, new char[]{'Α', 'Ά'}, new char[]{'Ό', 'Ο'}, new char[]{'Ο', 'Ό'}};
        r0[9] = new char[][]{new char[]{'I', 'İ'}, new char[]{'İ', 'I'}, new char[]{'V', 'W'}, new char[]{'W', 'V'}};
        r0[10] = new char[][]{new char[]{'C', 'Č'}, new char[]{'Č', 'C'}, new char[]{'D', 'Đ'}, new char[]{'Đ', 'D'}, new char[]{'S', 'Š'}, new char[]{'Š', 'S'}, new char[]{'Z', 'Ž'}, new char[]{'Ž', 'Z'}};
        r0[11] = new char[][]{new char[]{'ホ', 'ボ'}, new char[]{'ホ', 'ポ'}, new char[]{'ボ', 'ホ'}, new char[]{'ボ', 'ポ'}, new char[]{'ポ', 'ホ'}, new char[]{'ポ', 'ボ'}, new char[]{'ペ', 'ベ'}, new char[]{'ペ', 'ヘ'}, new char[]{'ベ', 'ペ'}, new char[]{'ベ', 'ヘ'}, new char[]{'ヘ', 'ペ'}, new char[]{'ヘ', 'ベ'}, new char[]{'フ', 'ブ'}, new char[]{'フ', 'プ'}, new char[]{'ブ', 'フ'}, new char[]{'ブ', 'プ'}, new char[]{'プ', 'フ'}, new char[]{'プ', 'ブ'}, new char[]{'ヒ', 'ビ'}, new char[]{'ビ', 'ヒ'}, new char[]{'ハ', 'バ'}, new char[]{'ハ', 'パ'}, new char[]{'バ', 'ハ'}, new char[]{'バ', 'パ'}, new char[]{'パ', 'ハ'}, new char[]{'パ', 'バ'}, new char[]{'ト', 'ド'}, new char[]{'ド', 'ト'}, new char[]{'テ', 'デ'}, new char[]{'デ', 'テ'}, new char[]{'タ', 'ダ'}, new char[]{'ダ', 'タ'}, new char[]{'シ', 'ジ'}, new char[]{'ジ', 'シ'}, new char[]{'サ', 'ザ'}, new char[]{'ザ', 'サ'}, new char[]{'ク', 'グ'}, new char[]{'グ', 'ク'}, new char[]{'カ', 'ガ'}, new char[]{'ガ', 'カ'}};
        CHAR_CATEGORY = r0;
    }

    DropdownListAdapter(Context context, int layoutId, ArrayList<String> cityList, EditText text) {
        super(context, layoutId, cityList);
        this.mContext = context;
        this.mCityList = cityList;
        this.mAutoText = text;
        this.mListItemId = layoutId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(this.mListItemId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(C0836R.id.auto_list_city_name);
            viewHolder.gmtView = (TextView) convertView.findViewById(C0836R.id.auto_list_city_gmt);
            viewHolder.color = this.mContext.getColor(C0836R.color.primary_dark_color);
            viewHolder.divider = convertView.findViewById(C0836R.id.auto_list_city_divider);
            viewHolder.subHeaderView = (TextView) convertView.findViewById(C0836R.id.item_subheader);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(C0836R.id.auto_list_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String title = (String) getItem(position);
        if (title != null) {
            String keyWordString;
            if (this.mAutoText != null) {
                keyWordString = this.mAutoText.getText().toString();
            } else {
                keyWordString = "";
            }
            setCityAndCountry(viewHolder, title, keyWordString);
            setHeaderView(viewHolder, position, keyWordString);
            setGmt(viewHolder, title);
            setListRounded(viewHolder, position, keyWordString);
        }
        return convertView;
    }

    private void setCityAndCountry(ViewHolder viewHolder, String title, String keyWordString) {
        Spannable spanTitle = new SpannableString(title);
        String[] arrayKey = keyWordString.split(" ");
        int addOffset = 0;
        for (int i = 0; i < arrayKey.length; i++) {
            String keyword = getPrefixCharForIndianString(viewHolder.textView, title, arrayKey[i]);
            if (keyword == null) {
                int indexOf = getSpannableTextIndex(title, arrayKey[i]);
                int length = arrayKey[i].length();
                if (indexOf != -1) {
                    int l = spanTitle.length();
                    if ((addOffset + length) + indexOf <= l) {
                        spanTitle.setSpan(new ForegroundColorSpan(viewHolder.color), indexOf + addOffset, (length + indexOf) + addOffset, 18);
                        title = title.substring(indexOf + length);
                        addOffset += length + indexOf;
                    } else {
                        spanTitle.setSpan(new ForegroundColorSpan(viewHolder.color), indexOf + addOffset, l - 1, 18);
                        title = title.substring(l - 1);
                        addOffset += l - 1;
                    }
                }
            } else {
                int startOffset;
                int endOffset;
                String lowerStr = title.toLowerCase();
                String upperStr = title;
                if (upperStr.length() == lowerStr.length()) {
                    startOffset = lowerStr.indexOf(keyword.toLowerCase());
                    endOffset = startOffset + keyword.length();
                } else {
                    startOffset = upperStr.indexOf(keyword);
                    endOffset = startOffset + keyword.length();
                }
                if (startOffset >= 0) {
                    spanTitle.setSpan(new ForegroundColorSpan(viewHolder.color), startOffset + addOffset, endOffset + addOffset, 33);
                    title = title.substring(endOffset);
                    addOffset += endOffset;
                }
            }
        }
        if (viewHolder.textView != null) {
            viewHolder.textView.setText(spanTitle);
            viewHolder.textView.setMarqueeRepeatLimit(0);
            ClockUtils.setTextSize(viewHolder.textView, (float) getHugeFontSize(this.mContext));
        }
    }

    private void setHeaderView(ViewHolder viewHolder, int position, String keyWordString) {
        if (viewHolder.subHeaderView != null && viewHolder.divider != null) {
            String firstChar = ((String) getItem(position)).subSequence(0, 1).toString();
            if (Locale.getDefault().equals(Locale.KOREA)) {
                firstChar = convertHangulChoSung(firstChar);
            } else if (isChineseLanguage()) {
                firstChar = convertPinyin((String) getItem(position));
            }
            viewHolder.subHeaderView.setText(firstChar);
            viewHolder.subHeaderView.setContentDescription(firstChar + ' ' + getContext().getResources().getString(C0836R.string.header));
            if (keyWordString.length() != 0 || isSameFirstChar(position) || this.mSelectCurrentLocation) {
                int i;
                View access$400 = viewHolder.divider;
                if (position == 0) {
                    i = 8;
                } else {
                    i = 0;
                }
                access$400.setVisibility(i);
                viewHolder.subHeaderView.setVisibility(8);
                return;
            }
            if (isDualIndexChar(position)) {
                viewHolder.subHeaderView.setText(((String) getItem(position)).subSequence(0, 2).toString().toUpperCase());
            }
            checkFirstIndexChar(position, viewHolder.subHeaderView);
            viewHolder.divider.setVisibility(8);
            viewHolder.subHeaderView.setVisibility(0);
        }
    }

    private void setListRounded(ViewHolder viewHolder, int position, String keyWordString) {
        Throwable e;
        try {
            int corners;
            if (keyWordString.length() == 0 && !this.mSelectCurrentLocation) {
                switch (compareFirstChar(position)) {
                    case 1:
                        corners = 3;
                        break;
                    case 2:
                        corners = 12;
                        break;
                    case 3:
                        corners = 15;
                        break;
                    default:
                        corners = 0;
                        break;
                }
            } else if (getCount() == 1) {
                corners = 15;
            } else if (position == 0) {
                corners = 3;
            } else if (position == getCount() - 1) {
                corners = 12;
            } else {
                corners = 0;
            }
            viewHolder.linearLayout.semSetRoundedCorners(corners);
            if (corners != 0) {
                viewHolder.linearLayout.semSetRoundedCornerColor(corners, this.mContext.getResources().getColor(C0836R.color.window_background_color, null));
            }
        } catch (NoSuchMethodError e2) {
            e = e2;
            Log.secE("DropdownListAdapter", "NoSuchMethodError : " + e.toString());
        } catch (NullPointerException e3) {
            e = e3;
            Log.secE("DropdownListAdapter", "NoSuchMethodError : " + e.toString());
        }
    }

    private void setGmt(ViewHolder viewHolder, String cityName) {
        City city = CityManager.findCityObjectByName(cityName);
        if (city != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(city.getCurrentTimeGMT());
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                sb.replace(0, 3, this.mContext.getString(C0836R.string.gmt));
                int i = 0;
                while (i < sb.length()) {
                    if (sb.charAt(i) >= '0' && sb.charAt(i) <= '9') {
                        sb.setCharAt(i, (char) ((sb.charAt(i) - 48) + 1632));
                    }
                    i++;
                }
            }
            if (viewHolder.gmtView != null) {
                viewHolder.gmtView.setText(sb.toString());
            }
        }
    }

    private String getPrefixCharForIndianString(TextView view, String title, String keyword) {
        TextPaint paint = view.getPaint();
        if (paint == null || keyword == null || title == null) {
            return null;
        }
        char[] prefixForIndian = TextUtils.semGetPrefixCharForSpan(paint, title, keyword.toCharArray());
        if (prefixForIndian != null) {
            return new String(prefixForIndian);
        }
        return null;
    }

    private int getSpannableTextIndex(String title, String keyword) {
        return title.toLowerCase().indexOf(keyword.toLowerCase());
    }

    public Filter getFilter() {
        return new C08561();
    }

    private boolean isDualIndexChar(int position) {
        if (position == 0) {
            return false;
        }
        boolean z = ("cs".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "sk".equalsIgnoreCase(Locale.getDefault().getLanguage())) && ((String) getItem(position)).substring(0, 1).charAt(0) == 'C' && ((String) getItem(position - 1)).substring(0, 1).charAt(0) == 'H';
        return z;
    }

    private void checkFirstIndexChar(int position, TextView view) {
        if (position != 0 && view != null) {
            String CurrentTitle = ((String) getItem(position)).substring(0, 1);
            String PrevTitle = ((String) getItem(position - 1)).substring(0, 1);
            char currChar = CurrentTitle.charAt(0);
            char prevChar = PrevTitle.charAt(0);
            if ("sr".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
                char[] charIndex = new char[]{'Č', 'B', 'C'};
                if (currChar == charIndex[0] && prevChar == charIndex[1]) {
                    view.setText(Character.toString(charIndex[2]));
                }
            }
            if ("el".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
                for (char[] chars : new char[][]{new char[]{'Ό', 'Ξ', 'Ο'}, new char[]{'Ώ', 'Χ', 'Ω'}}) {
                    if (currChar == chars[0] && prevChar == chars[1]) {
                        view.setText(Character.toString(chars[2]));
                    }
                }
            }
            if ("ja".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
                for (char[] chars2 : new char[][]{new char[]{'グ', 'キ', 'ク'}, new char[]{'ザ', 'コ', 'サ'}, new char[]{'ダ', 'ソ', 'タ'}, new char[]{'デ', 'チ', 'テ'}, new char[]{'ド', 'デ', 'ト'}, new char[]{'パ', 'ノ', 'ハ'}, new char[]{'ビ', 'ハ', 'ヒ'}, new char[]{'ベ', 'ブ', 'ヘ'}}) {
                    if (currChar == chars2[0] && prevChar == chars2[1]) {
                        view.setText(Character.toString(chars2[2]));
                    }
                }
            }
        }
    }

    private boolean isSameFirstChar(int position) {
        if (position == 0) {
            return false;
        }
        boolean z;
        String CurrentTitle = ((String) getItem(position)).substring(0, 1);
        String PrevTitle = ((String) getItem(position - 1)).substring(0, 1);
        if (Locale.getDefault().equals(Locale.KOREA)) {
            CurrentTitle = convertHangulChoSung(CurrentTitle);
            PrevTitle = convertHangulChoSung(PrevTitle);
        } else if (isChineseLanguage()) {
            CurrentTitle = convertPinyin((String) getItem(position));
            PrevTitle = convertPinyin((String) getItem(position - 1));
        }
        if (isCheckSubTitleSpell(CurrentTitle, PrevTitle) || CurrentTitle.equals(PrevTitle)) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    private boolean isCheckSubTitleSpell(String CurrentTitle, String PrevTitle) {
        if (CurrentTitle == null || CurrentTitle.length() == 0 || PrevTitle == null || PrevTitle.length() == 0) {
            return false;
        }
        int countryNum = getCountryNumber();
        if (countryNum < 0) {
            return false;
        }
        char[] charCurrPrev = new char[]{CurrentTitle.charAt(0), PrevTitle.charAt(0)};
        for (char[] equals : CHAR_CATEGORY[countryNum]) {
            if (Arrays.equals(charCurrPrev, equals)) {
                return true;
            }
        }
        return false;
    }

    private int compareFirstChar(int position) {
        if (position == 0) {
            return 1;
        }
        if (position == getCount() - 1) {
            return 2;
        }
        String currentTitle = ((String) getItem(position)).substring(0, 1);
        String prevTitle = ((String) getItem(position - 1)).substring(0, 1);
        String nextTitle = ((String) getItem(position + 1)).substring(0, 1);
        if (Locale.getDefault().equals(Locale.KOREA)) {
            currentTitle = convertHangulChoSung(currentTitle);
            prevTitle = convertHangulChoSung(prevTitle);
            nextTitle = convertHangulChoSung(nextTitle);
        } else if (isChineseLanguage()) {
            currentTitle = convertPinyin((String) getItem(position));
            prevTitle = convertPinyin((String) getItem(position - 1));
            nextTitle = convertPinyin((String) getItem(position + 1));
        }
        if (!currentTitle.equals(prevTitle) && currentTitle.equals(nextTitle)) {
            return 1;
        }
        if (!currentTitle.equals(prevTitle) || currentTitle.equals(nextTitle)) {
            return !currentTitle.equals(prevTitle) ? 3 : 0;
        } else {
            return 2;
        }
    }

    private static String convertHangulChoSung(String name) {
        char ch = name.charAt(0);
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (ch < '가' || ch > '힣') {
            return "";
        }
        return String.valueOf(KOREAN_JAUM_CONVERT_MAP[(ch - 44032) / 588]);
    }

    private static String convertPinyin(String name) {
        if (name.contains(" / ")) {
            name = name.substring(0, name.indexOf(" / "));
        }
        City city = CityManager.findCityByName(name);
        String languageTag = Locale.getDefault().toLanguageTag();
        if (city != null) {
            if ("zh-Hans-CN".equalsIgnoreCase(languageTag) || "zh-Hans-HK".equalsIgnoreCase(languageTag) || "zh-Hans-MO".equalsIgnoreCase(languageTag) || "zh-Hant-TW".equalsIgnoreCase(languageTag)) {
                return city.getNamePinyin().substring(0, 1).toUpperCase(Locale.getDefault());
            }
            if ("zh-Hant-MO".equalsIgnoreCase(languageTag) || "zh-Hant-HK".equalsIgnoreCase(languageTag)) {
                return Integer.parseInt(city.getNamePinyin().substring(0, 2)) + Character.toString('劃');
            }
        }
        return "";
    }

    private int getHugeFontSize(Context context) {
        switch (ClockUtils.getGlobalSettingFontSize(context)) {
            case 7:
                return context.getResources().getDimensionPixelSize(C0836R.dimen.accessibility_huge_font_size8);
            case 8:
                return context.getResources().getDimensionPixelSize(C0836R.dimen.accessibility_huge_font_size9);
            case 9:
                return context.getResources().getDimensionPixelSize(C0836R.dimen.accessibility_huge_font_size10);
            case 10:
                return context.getResources().getDimensionPixelSize(C0836R.dimen.accessibility_huge_font_size11);
            default:
                return context.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_autocompletelistitem_cityname_textview_textsize);
        }
    }

    private int getCountryNumber() {
        String toLowerCase = Locale.getDefault().getLanguage().toLowerCase(Locale.US);
        Object obj = -1;
        switch (toLowerCase.hashCode()) {
            case 3184:
                if (toLowerCase.equals("cs")) {
                    obj = 3;
                    break;
                }
                break;
            case 3239:
                if (toLowerCase.equals("el")) {
                    obj = 11;
                    break;
                }
                break;
            case 3246:
                if (toLowerCase.equals("es")) {
                    obj = 1;
                    break;
                }
                break;
            case 3247:
                if (toLowerCase.equals("et")) {
                    obj = 12;
                    break;
                }
                break;
            case 3267:
                if (toLowerCase.equals("fi")) {
                    obj = 7;
                    break;
                }
                break;
            case 3276:
                if (toLowerCase.equals("fr")) {
                    obj = 4;
                    break;
                }
                break;
            case 3301:
                if (toLowerCase.equals("gl")) {
                    obj = 2;
                    break;
                }
                break;
            case 3341:
                if (toLowerCase.equals("hu")) {
                    obj = 5;
                    break;
                }
                break;
            case 3371:
                if (toLowerCase.equals("it")) {
                    obj = null;
                    break;
                }
                break;
            case 3383:
                if (toLowerCase.equals("ja")) {
                    obj = 14;
                    break;
                }
                break;
            case 3508:
                if (toLowerCase.equals("nb")) {
                    obj = 6;
                    break;
                }
                break;
            case 3588:
                if (toLowerCase.equals("pt")) {
                    obj = 9;
                    break;
                }
                break;
            case 3672:
                if (toLowerCase.equals("sk")) {
                    obj = 10;
                    break;
                }
                break;
            case 3679:
                if (toLowerCase.equals("sr")) {
                    obj = 13;
                    break;
                }
                break;
            case 3683:
                if (toLowerCase.equals("sv")) {
                    obj = 8;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return 0;
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
            case 7:
            case 8:
                return 5;
            case 9:
                return 6;
            case 10:
                return 7;
            case 11:
                return 8;
            case 12:
                return 9;
            case 13:
                return 10;
            case 14:
                return 11;
            default:
                return -1;
        }
    }

    public void setSelectCurrentLocation(boolean selectCurrentLocation) {
        this.mSelectCurrentLocation = selectCurrentLocation;
    }

    private boolean isChineseLanguage() {
        String languageTag = Locale.getDefault().toLanguageTag();
        if ("zh-Hans-CN".equalsIgnoreCase(languageTag) || "zh-Hans-HK".equalsIgnoreCase(languageTag) || "zh-Hans-MO".equalsIgnoreCase(languageTag) || "zh-Hant-TW".equalsIgnoreCase(languageTag) || "zh-Hant-MO".equalsIgnoreCase(languageTag) || "zh-Hant-HK".equalsIgnoreCase(languageTag)) {
            return true;
        }
        return false;
    }
}
