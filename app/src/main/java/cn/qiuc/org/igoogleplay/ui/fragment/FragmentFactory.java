package cn.qiuc.org.igoogleplay.ui.fragment;

import java.util.HashMap;

/**
 * Created by admin on 2016/5/4.
 */
public class FragmentFactory {

    private static HashMap<Integer, BaseFragment> fragmentCache = new HashMap<>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = fragmentCache.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new HomeFragment(); //TODO
                    break;
                case 3:
                    fragment = new HomeFragment(); //TODO
                    break;
                case 4:
                    fragment = new HomeFragment(); //TODO
                    break;
                case 5:
                    fragment = new HomeFragment(); //TODO
                    break;
                case 6:
                    fragment = new HomeFragment(); //TODO
                    break;
                default:
                    fragment = new HomeFragment();
                    break;
            }
            fragmentCache.put(position, fragment);
        }
        return fragment;
    }
}
